package com.ubicomp.ketdiary;

import java.io.File;
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
    
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private File dir, recordFile;
    private long ts;
    
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
        
        topIcon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(recordState == NOT_RECORD || recordState == RECORDED){
                    recordState = RECORDING;
                    stateText.setText("錄音中....");
                    bottomIcon.setImageResource(R.drawable.no_play);
                    
                    startRecord();
                }
                else if(recordState == RECORDING){
                    stateText.setText("已錄音");
                    recordState = RECORDED;   
                    bottomIcon.setImageResource(R.drawable.sound_start);
                    
                    stopRecord();
                }
            }
            
        });
        
        bottomIcon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	 if(recordState == RECORDED){
                     recordState = PLAYING;
                     stateText.setText("播放中...");
                     
                     topIcon.setImageResource(R.drawable.sound_recorder_enable);
                     bottomIcon.setImageResource(R.drawable.sound_stop);
                     
                     startPlay();
                 }
            	 else if(recordState == PLAYING){
                     recordState = RECORDED;
                     stateText.setText("已錄音");
                     
                     topIcon.setImageResource(R.drawable.sound_recorder_able);
                     bottomIcon.setImageResource(R.drawable.sound_start);
                     
                     stopPlay();
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
        	mediaPlayer.setDataSource(recordFile.getAbsolutePath());
        	mediaPlayer.prepare();
        	mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer arg0) {
					try {
						recordState = RECORDED;
	                     stateText.setText("已錄音");
	                     
	                     topIcon.setImageResource(R.drawable.sound_recorder_able);
	                     bottomIcon.setImageResource(R.drawable.sound_start);
	                     
	                     stopPlay();
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
    	mediaPlayer.release();
    	mediaPlayer = null;  
    }
    
    @SuppressWarnings("deprecation")
	private void startRecord(){
        String fileName = "record.amr";
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
    
    /**onResume of AboutActivity. Override for ClickLog*/
    public void onResume() {
        super.onResume();
    }

    @Override
    /**onPause of AboutActivity. Override for ClickLog*/
    public void onPause() {
        super.onPause();
    }
}
