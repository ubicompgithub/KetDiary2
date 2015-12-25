package com.ubicomp.ketdiary.data.structure;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;

public class ClockTimer {
	
	private boolean startflag = false, pauseflag = false;
	private int tsec=0;
	
	private Handler mhandler;
	
	public ClockTimer(){
	}
	
	public void clockStart(){
		startflag = true;
		pauseflag = false;
		tsec = 0;
		
		mhandler = new Handler();
		mhandler.postDelayed(updateTimer, 1);
		
	}
	private Runnable updateTimer = new Runnable(){
    	@SuppressWarnings("deprecation")
		 public void run(){
			 //每秒要執行的程式
    		if(!startflag)
    			return;
    		
			if(!pauseflag){
				 tsec++;
			}
			mhandler.postDelayed(this, 1000);
		 }

	};
		
	public void clockPause(){
		pauseflag = true;
	}
	
	public void clockContinue(){
		pauseflag = false;
	}
	
	public void clockStop(){
		startflag = false;
		 
		mhandler.removeCallbacks(updateTimer); 
		mhandler = null;  
	}
	
	public int getClockSec(){
		return tsec;
	}
}
