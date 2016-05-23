package com.ubicomp.ketdiary;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SvmService extends Service {
	@Override 
	public void onCreate() {  
        super.onCreate();  
        Log.d("GG", "onCreate() executed");  
    }  
	
	@Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        Log.d("GG", "onStartCommand() executed");  
        return super.onStartCommand(intent, flags, startId);  
    }  
	
	 @Override  
	    public void onDestroy() {  
	        super.onDestroy();  
	        Log.d("GG", "onDestroy() executed");  
	    }  
	  
  
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	class MyBinder extends Binder {  
		  
        public void startDownload() {  
            Log.d("GG", "startDownload() executed");  
            // 执行具体的下载任务  
        }  
  
    } 

}
