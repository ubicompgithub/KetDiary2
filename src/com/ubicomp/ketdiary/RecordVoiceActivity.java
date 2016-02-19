package com.ubicomp.ketdiary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.ubicomp.ketdiary.data.db.DatabaseControl;
import com.ubicomp.ketdiary.data.file.MainStorage;
import com.ubicomp.ketdiary.data.structure.Appeal;
import com.ubicomp.ketdiary.data.structure.ClockTimer;
import com.ubicomp.ketdiary.data.structure.ExchangeHistory;
import com.ubicomp.ketdiary.data.structure.TimeValue;
import com.ubicomp.ketdiary.main.fragment.TestFragment2;
import com.ubicomp.ketdiary.system.Config;
import com.ubicomp.ketdiary.system.clicklog.ClickLog;
import com.ubicomp.ketdiary.system.clicklog.ClickLogId;
import com.ubicomp.ketdiary.ui.CustomToastSmall;
import com.ubicomp.ketdiary.ui.LoadingDialogControl;
import com.ubicomp.ketdiary2.R;
import com.ubicomp.ketdiary.system.PreferenceControl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
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
    private ImageView bar;
    
    private ImageView leftIcon, midIcon, rightIcon;
    private ImageView Bottom_Buttons, Bottom_Buttons_left, Bottom_Buttons_right, appeal_pic;
    //private ImageView bar1, bra2, bra3, bra4, bar5, bar6, bar7, bar8, bar9, bar10, bar11;
    private ImageView[] bars = new ImageView[20];
    
    private TextView stateText;
    private Button nextButton;
    
    private int recordState;
    
    private static int NOT_RECORD = 0;
    private static int RECORDING = 1;
    private static int RECORDED = 2;
    private static int PLAYING = 3;
    private static int RECORD_STOP = 4;
    private static int PLAY_STOP = 5;
    
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private File dir, recordFile;
    private long ts;
    
    private int filenum;
    private int fileSeconds;
    
    private String stateString;
    private Handler mHandler;
    
    private Handler mhandler;
    
	private ClockTimer clockTimer;// = new ClockTimer();
	private AlertDialog.Builder builder;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal);
        
        ts = TestFragment2.appealTimeValue.getTimestamp();
        
        dir = MainStorage.getMainStorageDirectory();
        
        bars[0] = (ImageView) findViewById(R.id.appeal_bar1);
        bars[1] = (ImageView) findViewById(R.id.appeal_bar2);
        bars[2] = (ImageView) findViewById(R.id.appeal_bar3);
        bars[3] = (ImageView) findViewById(R.id.appeal_bar4);
        bars[4] = (ImageView) findViewById(R.id.appeal_bar5);
        bars[5] = (ImageView) findViewById(R.id.appeal_bar6);
        bars[6] = (ImageView) findViewById(R.id.appeal_bar7);
        bars[7] = (ImageView) findViewById(R.id.appeal_bar8);
        bars[8] = (ImageView) findViewById(R.id.appeal_bar9);
        bars[9] = (ImageView) findViewById(R.id.appeal_bar10);
        bars[10] = (ImageView) findViewById(R.id.appeal_bar11);
        
        stateText = (TextView) findViewById(R.id.record_state_text);
        
        bar = (ImageView) findViewById(R.id.appeal_bar);
        appeal_pic = (ImageView) findViewById(R.id.final_appeal_pic);
        appeal_pic.setImageBitmap(AppealActivity.showPic);
        
        leftIcon = (ImageView) findViewById(R.id.appeal_left);
        midIcon = (ImageView) findViewById(R.id.appeal_mid);
        rightIcon = (ImageView) findViewById(R.id.appeal_right);
        
        Bottom_Buttons = (ImageView) findViewById(R.id.appeal_button);
        Bottom_Buttons_left = (ImageView) findViewById(R.id.appeal_undo);
        Bottom_Buttons_right = (ImageView) findViewById(R.id.appeal_finish);

        recordState = NOT_RECORD;
        mediaRecorder = null;
        mediaPlayer = null;
        
        leftIcon.setVisibility(View.VISIBLE); 
        midIcon.setVisibility(View.VISIBLE); 
        rightIcon.setVisibility(View.VISIBLE); 
        
        filenum = 0;
        
        builder = new AlertDialog.Builder(this);
        
        //LoadingDialogControl.dismiss();
        
        leftIcon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(recordState == NOT_RECORD){
                    recordState = RECORDING;
                    
                    setState(RECORDING);
                    
                    clockTimer.clockStart();
                    startRecord();
                }
                if(recordState == RECORDED){
                    recordState = PLAYING;
                    
                    setState(PLAYING);
                    
                    clockTimer.clockStart();
                    startPlay();
                }
                if(recordState == PLAY_STOP){
                    recordState = PLAYING;
                    
                    setState(PLAYING);
                    
                    clockTimer.clockContinue();
                    continuePlay();
                }
            }
            
        });
        
        leftIcon.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) 
				{  
					if(recordState == NOT_RECORD)
						leftIcon.setImageResource(R.drawable.voice_record_pressed);
					if(recordState == RECORDED || recordState == PLAY_STOP)
						leftIcon.setImageResource(R.drawable.voice_play_pressed);
                }  
                if (event.getAction() == MotionEvent.ACTION_UP) 
                {  
                	if(recordState == NOT_RECORD || recordState == RECORD_STOP)
						leftIcon.setImageResource(R.drawable.voice_record);
					if(recordState == RECORDED || recordState == PLAY_STOP)
						leftIcon.setImageResource(R.drawable.voice_play);
                } 
				return false;
			}
			
		});
        
        midIcon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(recordState == RECORDING){
                    recordState = RECORD_STOP;
                    
                    setState(RECORD_STOP);
                    
                    clockTimer.clockPause();
                    stopRecord();
                }
                else if(recordState == PLAYING){
                    
                    recordState = PLAY_STOP;  
                    
                    setState(PLAY_STOP);
                    
                    clockTimer.clockPause();
                    stopPlay();
                }
                else if(recordState == RECORDED){
                    
                    recordState = NOT_RECORD;  
                    
                    setState(NOT_RECORD);
                }
                else if(recordState == RECORD_STOP){
                	recordState = RECORDING;
                    
                    setState(RECORDING);
                    
                    clockTimer.clockContinue();
                    startRecord();
                }
            }
            
        });
        
        midIcon.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) 
				{  
					if(recordState == RECORDING || recordState == PLAYING)
						midIcon.setImageResource(R.drawable.voice_stop_pressed);
					if(recordState == RECORDED)
						midIcon.setImageResource(R.drawable.voice_replay_pressed);
					if(recordState == RECORD_STOP)
						midIcon.setImageResource(R.drawable.voice_stop_pressed2);
                }  
                if (event.getAction() == MotionEvent.ACTION_UP) 
                {  
                	if(recordState == RECORDING || recordState == PLAYING)
						midIcon.setImageResource(R.drawable.voice_stop);
					if(recordState == RECORDED)
						midIcon.setImageResource(R.drawable.voice_replay);
					if(recordState == RECORD_STOP)
						midIcon.setImageResource(R.drawable.voice_stop2);
                } 
				return false;
			}
			
		});
        
        rightIcon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	 if(recordState == RECORDING || recordState == RECORD_STOP){
                     recordState = RECORDED;
                     
                     setState(RECORDED);
                     
                     fileSeconds = clockTimer.getClockSec();
                     clockTimer.clockStop();
                     endRecord();
                 }
            	 else if(recordState == PLAYING || recordState == PLAY_STOP){
                     recordState = RECORDED; 
                     
                     recordState = RECORDED;           
                     setState(RECORDED);
                     
                     clockTimer.clockStop();
                     endPlay();
                 }
            }
            
            
        });
        
        rightIcon.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) 
				{  
					if(recordState == RECORDING || recordState == PLAYING  || recordState == RECORD_STOP  || recordState == PLAY_STOP)
						rightIcon.setImageResource(R.drawable.voice_end_pressed);
                }  
                if (event.getAction() == MotionEvent.ACTION_UP) {  
                	if(recordState == RECORDING || recordState == PLAYING  || recordState == RECORD_STOP  || recordState == PLAY_STOP)
						rightIcon.setImageResource(R.drawable.voice_end);
                } 
				return false;
			}
			
		});
        
        Bottom_Buttons_left.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	if(recordState == RECORDED || recordState == NOT_RECORD)
            	{	
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
        
        Bottom_Buttons_left.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) 
				{  
					Bottom_Buttons.setImageResource(R.drawable.appeal_finish_button_3);
                }  
                if (event.getAction() == MotionEvent.ACTION_UP) 
                {  
                	Bottom_Buttons.setImageResource(R.drawable.appeal_finish_button_4);
                } 
				return false;
			}
			
		});
        
        Bottom_Buttons_right.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	if(recordState == RECORDED)
            	{
            		AppealFinish();
            		            		
                	onDestroy();
					finish();
                }
            	 else if(recordState == NOT_RECORD)
            	 {
            		 
         		    builder.setTitle("警告!");
         		    builder.setMessage("尚未錄音，確定要上傳？");
         		    //builder.setIcon(android.R.drawable.ic_dialog_info);
         		    builder.setPositiveButton("取消",
         		       new DialogInterface.OnClickListener()
         		       {
         		           @Override
         		           public void onClick(DialogInterface dialog, int which){ }
         		       });
         		    builder.setNegativeButton("確定",
         		       new DialogInterface.OnClickListener()
         		       {   @Override
         		           public void onClick(DialogInterface dialog, int which)
         		       		{
         		    	   		AppealFinish();
     		        	  
         		    	   		onDestroy();
         		    	   		finish();
         		       		}
         		       });
         		    builder.create().show();
            	 }

                else if(recordState == RECORDING){
                	CustomToastSmall.generateToast("請先結束錄音");
                }
                else if(recordState == PLAYING){
                	CustomToastSmall.generateToast("請先結束播放");
                }

            }
        
        });
        
        Bottom_Buttons_right.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) 
				{  
					Bottom_Buttons.setImageResource(R.drawable.appeal_finish_button_2);
                }  
                if (event.getAction() == MotionEvent.ACTION_UP) 
                {  
                	Bottom_Buttons.setImageResource(R.drawable.appeal_finish_button_4);
                } 
				return false;
			}
			
		});
        
        
    }
    
    private void AppealFinish()
    {
    	int _type = PreferenceControl.getAppealAble(); // tmp modify AppealType
		int _times = 0;
		DatabaseControl db = new DatabaseControl();
		db.insertAppeal(new Appeal(ts, _type, _times));
		
		TimeValue tv = TimeValue.generate(ts);
		
		PreSettingActivity.addTestResult(0, tv.getYear(), tv.getMonth(), tv.getDay(), -1 );
		
		AppealActivity.fa.finish();
    }
    
    private void setBar(int now, int all){
    	int p = now * 11 / all;
    	for(int i = 0; i < p; i++)
    		bars[i].setVisibility(View.VISIBLE); 
    	for(int i = p; i < 11; i++)
    		bars[i].setVisibility(View.INVISIBLE); 
    }
    
    private void setState(int now_state){
    	if(now_state == NOT_RECORD)
    	{
    		leftIcon.setImageResource(R.drawable.voice_record);
    		midIcon.setImageResource(R.drawable.voice_stop_enable);
    		rightIcon.setImageResource(R.drawable.voice_end_enable);
            
            //stateText.setText("開始錄音 [00:00/00:00]");
    		stateString = "開始錄音";
    	}
    	
    	else if(now_state == RECORDING)
    	{
    		leftIcon.setImageResource(R.drawable.voice_record_enable);
    		midIcon.setImageResource(R.drawable.voice_stop);
    		rightIcon.setImageResource(R.drawable.voice_end);

            //stateText.setText("錄音中.. [00:00/00:00]");
    		stateString = "錄音中..";
    	}
    	
    	else if(now_state == RECORDED)
    	{
    		leftIcon.setImageResource(R.drawable.voice_play);
    		midIcon.setImageResource(R.drawable.voice_replay);
    		rightIcon.setImageResource(R.drawable.voice_end_enable);
            
            //stateText.setText("錄音完成 [00:00/00:00]");
    		stateString = "錄音完成";
    	}
    	
    	else if(now_state == PLAYING)
    	{
    		leftIcon.setImageResource(R.drawable.voice_play_enable);
    		midIcon.setImageResource(R.drawable.voice_stop);
    		rightIcon.setImageResource(R.drawable.voice_end);
            
            //stateText.setText("播放中.. [00:00/00:00]");
    		stateString = "播放中..";
    	}
    	
    	else if(now_state == RECORD_STOP)
    	{
    		leftIcon.setImageResource(R.drawable.voice_record_enable);
    		midIcon.setImageResource(R.drawable.voice_stop2);
    		rightIcon.setImageResource(R.drawable.voice_end);
            
            //stateText.setText("暫停錄音 [00:00/00:00]");
    		stateString = "暫停錄音";
    	}
    	
    	else if(now_state == PLAY_STOP)
    	{
    		leftIcon.setImageResource(R.drawable.voice_play);
    		midIcon.setImageResource(R.drawable.voice_stop_enable);
    		rightIcon.setImageResource(R.drawable.voice_end);
    		
            //stateText.setText("播放暫停 [00:00/00:00]");
    		stateString = "播放暫停";
    	}
    	
    	
    	
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
	                 
						 setState(RECORDED);
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
    
    private void continuePlay(){
    	if(mediaPlayer != null)
    		mediaPlayer.start();

    }
    
    private void stopPlay(){
    	mediaPlayer.pause(); 
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
    
    private Runnable updateTimer = new Runnable(){
    	@SuppressWarnings("deprecation")
    	public void run(){
			 if(recordState == NOT_RECORD){
				 setBar(0,1);
				 stateText.setText(stateString + " [00:00/00:30]");
			 }
			 else if(recordState == RECORDING || recordState == RECORD_STOP){
				 String formatStr = "%02d";
				 String formatAns = String.format(formatStr, clockTimer.getClockSec());
				 
				 setBar(clockTimer.getClockSec(), 30);
				 stateText.setText(stateString + " [00:" + formatAns + "/00:30]");
			 }
			 else if(recordState == RECORDED){ 
				 String formatStr = "%02d";
				 String formatAns = String.format(formatStr, fileSeconds);
				 
				 setBar(0,1);
				 stateText.setText(stateString + " [00:00/00:"+ formatAns +"]");
			 }
			 else if(recordState == PLAYING || recordState == PLAY_STOP){
				 String formatStr = "%02d";
				 String formatAns = String.format(formatStr, clockTimer.getClockSec());	 
				 
				 String formatStr2 = "%02d";
				 String formatAns2 = String.format(formatStr, fileSeconds);
				 
				 setBar(clockTimer.getClockSec(), fileSeconds);
				 stateText.setText(stateString + " [00:" + formatAns + "/00:"+ formatAns2 +"]");
			 }
			 mhandler.postDelayed(this, 1000);
		 }
    	
    };
    
    @Override
    protected void onStart()
	{ 
        super.onStart();
      
        setState(NOT_RECORD);
       
        setBar(0,1);
        
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	
    	if(recordState == NOT_RECORD || recordState == RECORDED ||
    	   recordState == RECORD_STOP || recordState == PLAY_STOP)
    	{
    		
    	}
    	
    	else if(recordState == PLAYING)
    	{
    		recordState = RECORDED; 
            
            recordState = RECORDED;           
            setState(RECORDED);
            
            clockTimer.clockStop();
            endPlay();
    	}
    	
    	else if(recordState == RECORDING)
    	{
    		recordState = RECORDED;
            
            setState(RECORDED);
            
            fileSeconds = clockTimer.getClockSec();
            clockTimer.clockStop();
            endRecord();
    	}
    	
    	if(mhandler != null)
    		mhandler.removeCallbacksAndMessages(null);
    	
    	mhandler = null; 
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	mhandler = new Handler();
        mhandler.postDelayed(updateTimer, 1000);
        clockTimer = new ClockTimer();

    }
    
	@Override
    protected void onDestroy()
	{ 
        super.onDestroy();
        finish();
    }
}
