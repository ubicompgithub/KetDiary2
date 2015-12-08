package com.ubicomp.ketdiary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ubicomp.ketdiary.data.db.DatabaseControl;
import com.ubicomp.ketdiary.data.file.MainStorage;
import com.ubicomp.ketdiary.data.structure.Appeal;
import com.ubicomp.ketdiary.data.structure.ExchangeHistory;
import com.ubicomp.ketdiary.main.fragment.TestFragment2;
import com.ubicomp.ketdiary.system.Config;
import com.ubicomp.ketdiary.system.clicklog.ClickLog;
import com.ubicomp.ketdiary.system.clicklog.ClickLogId;
import com.ubicomp.ketdiary.ui.CustomToastSmall;
import com.ubicomp.ketdiary2.R;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * Activity for Record Voice
 * 
 * @author Ivan 
 */


public class RecordVoiceActivity extends Activity {
    private TextView help, countDownText;
    private ImageView bar_bg, barStart, barEnd, bar;
    
    private ImageView topIcon, bottomIcon;
    private TextView stateText;
    private Button nextButton;
    
    private int recordState;
    
    private static int NOT_RECORD = 0;
    private static int RECORDING = 1;
    private static int RECORDED = 2;
    private static int PLAYING = 3;
    private static int RECORD_STOP = 4;
    private static int PLAY_STOP = 4;
    
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private File dir, recordFile;
    private long ts;
    
    private int filenum;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal);
        
        ts = TestFragment2.appealTimeValue.getTimestamp();
        
        dir = MainStorage.getMainStorageDirectory();
        
        bar_bg = (ImageView) findViewById(R.id.voice_record_bar_bg);
        barStart = (ImageView) findViewById(R.id.voice_record_bar_start);
        barEnd = (ImageView) findViewById(R.id.voice_record_bar_end);
        bar = (ImageView) findViewById(R.id.voice_record_bar);
        
        topIcon = (ImageView) findViewById(R.id.sound_recorder);
        bottomIcon = (ImageView) findViewById(R.id.sound_startstop);
        
        stateText = (TextView) findViewById(R.id.state_record);
        nextButton = (Button) findViewById(R.id.record_next);
        
        recordState = 0;
        mediaRecorder = null;
        mediaPlayer = null;
        
        topIcon.setImageResource(R.drawable.icon_rec);
        bottomIcon.setVisibility(View.INVISIBLE); 
        
        filenum = 0;
        
        topIcon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(recordState == NOT_RECORD || recordState == RECORDED || recordState == RECORD_STOP){
                    recordState = RECORDING;
                    stateText.setText("錄音中....");
                    topIcon.setImageResource(R.drawable.icon_pause);
                    bottomIcon.setImageResource(R.drawable.icon_stop);
                    bottomIcon.setVisibility(View.VISIBLE); 
                    
                    startRecord();
                }
                else if(recordState == RECORDING){
                    stateText.setText("暫停錄音");
                    recordState = RECORD_STOP;   
                    topIcon.setImageResource(R.drawable.icon_play);
                    bottomIcon.setImageResource(R.drawable.icon_stop);
                    
                    stopRecord();
                }
            
            }
            
        });
        
        bottomIcon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	 if(recordState == RECORDED){
                     recordState = PLAYING;
                     stateText.setText("播放中....");
                     
                     topIcon.setVisibility(View.INVISIBLE); 
                     bottomIcon.setImageResource(R.drawable.icon_stop);
                    
                     
                     startPlay();
                 }
            	 else if(recordState == RECORDING || recordState == RECORD_STOP){
                     stateText.setText("已錄音");
                     recordState = RECORDED;   
                     topIcon.setImageResource(R.drawable.icon_rec);
                     bottomIcon.setImageResource(R.drawable.icon_play);
                     
                     endRecord();
                 }
            	 else if(recordState == PLAYING){
                     recordState = RECORDED;
                     stateText.setText("結束播放，已錄音");
                     
                     topIcon.setImageResource(R.drawable.icon_rec);
                     bottomIcon.setImageResource(R.drawable.icon_play);
                     topIcon.setVisibility(View.VISIBLE); 
                     endPlay();
                 }
            }
            
            
        });
        
        nextButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                if(recordState == RECORDED || recordState == NOT_RECORD){
                	
                	int _type = 0; // tmp modify AppealType
            		int _times = 0; // tmp modify AppealType
            		DatabaseControl db = new DatabaseControl();
            		db.insertAppeal(new Appeal(ts, _type, _times));
            		Log.i("GG", "inserAppeal");
                	
                	onDestroy();
					finish();
                }
                else if(recordState == RECORDING){
                	CustomToastSmall.generateToast("請先結束錄音");
                }
                else if(recordState == PLAYING){
                	CustomToastSmall.generateToast("請先結束播放");
                }
                
            }
            
        });
    }
    
    private void startPlay(){
    	mediaPlayer = new MediaPlayer();
        try {
        	recordFile = new File(dir + File.separator + "Appeal" + 
					File.separator + String.valueOf(ts)  + File.separator + "record.amr");
        	
        	mediaPlayer.setDataSource(recordFile.getAbsolutePath());
        	mediaPlayer.prepare();
        	mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer arg0) {
					try {
						 recordState = RECORDED;
	                     stateText.setText("結束播放，已錄音");
	                     
	                     topIcon.setImageResource(R.drawable.icon_rec);
	                     bottomIcon.setImageResource(R.drawable.icon_play);
	                     topIcon.setVisibility(View.VISIBLE); 
	                     endPlay();
					} catch (IllegalStateException e) {
					}
				}
			});
        	mediaPlayer.start();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
    }
    
    private void stopPlay(){
    	mediaPlayer.stop(); 
    }
    
    private void endPlay(){
    	mediaPlayer.stop();
    	mediaPlayer.release();
    	mediaPlayer = null;  
    }
    
    @SuppressWarnings("deprecation")
	private void startRecord(){
        String fileName = "record"+ String.valueOf(filenum) +".amr";
        try {
        	recordFile = new File(dir + File.separator + "Appeal");
			if(!recordFile.exists())
				recordFile.mkdir();
			
			recordFile = new File(dir + File.separator + "Appeal" + 
							File.separator + String.valueOf(ts));
			if(!recordFile.exists())
				recordFile.mkdir();
			
			recordFile = new File(dir + File.separator + "Appeal" + 
							File.separator + String.valueOf(ts)  + File.separator + fileName);
         
         if(recordFile.exists())
        	 recordFile.delete();
         
         filenum ++;
         
         mediaRecorder = new MediaRecorder();


         mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

         mediaRecorder
           .setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);

         mediaRecorder
           .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

         mediaRecorder
           .setOutputFile(recordFile.getAbsolutePath());

         mediaRecorder.prepare();

         mediaRecorder.start();
        } catch (IOException e) {
         e.printStackTrace();
        }
    }
    
    private void stopRecord(){
        if(mediaRecorder != null) {
        	mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
    
    private void endRecord(){
         if(mediaRecorder != null) {
             mediaRecorder.stop();
             mediaRecorder.release();
             mediaRecorder = null;
         }
         mergeAllAmrFiles();
     }
    
    /**onResume of AboutActivity. Override for ClickLog*/
    public void onResume() {
        super.onResume();
    }

    @Override
    /**onPause of AboutActivity. Override for ClickLog*/
    public void onPause() {
        super.onPause();
    }
    
    /** 合并所有的音频文件 */
    public void mergeAllAmrFiles(){ 
    	int num = filenum;
    	filenum = 0;
            // 创建音频文件,合并的文件放这里
              //File tempFile = new File(recordingFiles.get(0));
              String fileName = "record.amr";
              File mergeFile = new File(dir + File.separator + "Appeal" + 
						File.separator + String.valueOf(ts)  + File.separator + fileName);
              
              //Log.d("RECORD", "MERGE : " + mergeFile.getAbsolutePath());
              FileOutputStream fileOutputStream = null;  
         
              if(!mergeFile.exists()){  
                  try {  
                      mergeFile.createNewFile();  
                  } catch (IOException e){  
                      // TODO Auto-generated catch block  
                      e.printStackTrace();  
                  }  
              }
      
              try {  
                  fileOutputStream=new FileOutputStream(mergeFile);  
              } catch (IOException e) {  
                  // TODO Auto-generated catch block  
                  e.printStackTrace();  
              } 
              
              //list里面为暂停录音 所产生的几段录音文件的名字，中间几段文件的减去前面的6个字节头文件  
              for(int index = 0; index < num ;index++){  
                  File file = new File(dir + File.separator + "Appeal" + 
  						File.separator + String.valueOf(ts)  + File.separator + 
  						"record"+ String.valueOf(index) +".amr");
                  
                  //Log.d("RECORD-PATH", recordingFiles.get(index) + file.length());
                  try {  
                      FileInputStream fileInputStream=new FileInputStream(file);  
                      byte  []myByte = new byte[fileInputStream.available()];
                      //文件长度  
                      int length = myByte.length;
                      //Log.d("RECORD-LENGTH", length  + "");
        
                      //头文件  
                      if(index == 0){  
                          while(fileInputStream.read(myByte) != -1){ 
                              fileOutputStream.write(myByte);
                              //fileOutputStream.write(myByte, 0,length);
                          }
                      }
                      
                      //之后的文件，去掉前面6个字节（头文件） 
                      else{  
                          while(fileInputStream.read(myByte) != -1){            
                              fileOutputStream.write(myByte, 6, length - 6);  
                          }
                      }
                
                      fileOutputStream.flush();
                      fileInputStream.close();
                      //合并之后删除文件
                      file.delete();
                  } catch (Exception e) {  
                      // TODO Auto-generated catch block  
                      e.printStackTrace();  
                  }  
              }  
              
              //结束后关闭流 
              try {
                  fileOutputStream.flush();
                  fileOutputStream.close();  
              } catch (IOException e) {  
                  // TODO Auto-generated catch block  
                  e.printStackTrace();  
              }
              
        }
}
