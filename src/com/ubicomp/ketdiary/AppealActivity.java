package com.ubicomp.ketdiary;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import java.util.Collections;  
import java.util.Comparator;  
import java.util.List; 

import com.ubicomp.ketdiary2.R;
import com.ubicomp.ketdiary.data.file.MainStorage;
import com.ubicomp.ketdiary.main.fragment.TestFragment2;
import com.ubicomp.ketdiary.system.cleaner.Cleaner;
import com.ubicomp.ketdiary.ui.BarButtonGenerator;
import com.ubicomp.ketdiary.ui.LoadingDialogControl;
import com.ubicomp.ketdiary.ui.Typefaces;

/**
 * Activity for Appeal
 * 
 * @author Ivan 
 */


public class AppealActivity extends Activity implements SurfaceHolder.Callback, SensorEventListener {
	private SurfaceView surfaceView1;
	private ImageView ImageView1, lineView;
	private SurfaceHolder holder;   
	private Camera myCamera;  
    private File dir;
    private boolean picTaked;
    public Bitmap jpegPic;
	public static Bitmap showPic;
    private ImageView takePicView, nextView, cancelView, undoView;
    private TextView  nextText, cancelText, undoText,headerText, hintText, hint2Text;
    
    public Activity activity = null;
    private long ts;
    private Handler mhandler;
    private boolean camera_using;
    
    public static boolean End;
    public static Activity fa;
    
    private SensorManager aSensorManager;
    private Sensor aSensor;
    
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 30;
    private static Size pictureSize,previewSize;  
    private final CameraSizeComparator sizeComparator = new CameraSizeComparator();  
    private int vHeight, vWidth;
    
    private boolean focusing = false;
    private boolean pic_save = false; 
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //LoadingDialogControl.show(AppealActivity.this,0);
        fa = this;
        
        setContentView(R.layout.activity_get_picture);
        
        Log.d("GG", "01");
        ImageView1 = (ImageView)findViewById(R.id.imageView1);
        ImageView1.setVisibility(View.INVISIBLE); 
        Log.d("GG", "02");
        lineView = (ImageView)findViewById(R.id.take_pic_line);
        Log.d("GG", "03");
        surfaceView1 = (SurfaceView)findViewById(R.id.surfaceView_1);
        holder = surfaceView1.getHolder();
    	holder.addCallback(this);
    	holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    	Log.d("GG", "04");
        picTaked = false;
        camera_using = false;
        
        takePicView = (ImageView)findViewById(R.id.take_pic_icon);
        nextView = (ImageView)findViewById(R.id.take_pic_next);
        undoView = (ImageView)findViewById(R.id.take_pic_re);
        cancelView = (ImageView)findViewById(R.id.take_pic_cancel);
        nextText = (TextView)findViewById(R.id.take_pic_next_text);
        undoText = (TextView)findViewById(R.id.take_pic_re_text);
        cancelText = (TextView)findViewById(R.id.take_pic_cancel_text);
        //headerText = (TextView)findViewById(R.id.take_pic_header);
        hintText = (TextView)findViewById(R.id.take_pic_hint);
        hint2Text = (TextView)findViewById(R.id.take_pic_hint2);
        Log.d("GG", "05");
        End = false;
        LoadingDialogControl.dismiss();
        Log.d("GG", "06");
        aSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        aSensor = aSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        aSensorManager.registerListener(this, aSensor, aSensorManager.SENSOR_DELAY_NORMAL);   
        Log.d("GG", "07");
        DisplayMetrics metrics = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d("GG", "08");
        //vWidth = metrics.widthPixels;
        //vHeight = metrics.heightPixels;
        vWidth = surfaceView1.getWidth();
        vHeight = surfaceView1.getHeight();
        //new initThread().start();  
        dir = MainStorage.getMainStorageDirectory();
        
	       ts = TestFragment2.appealTimeValue.getTimestamp();
	        
	       Log.d("GG", "09");
		   takePicView.setOnClickListener(new View.OnClickListener(){
				
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					if(picTaked && camera_using)
						return;
					
					LoadingDialogControl.show(AppealActivity.this);
					new Thread(new Runnable(){
			            @Override
			            public void run() {
			            	try{
			            		Thread.sleep(500);
			            	} catch (Exception e) { 
								//Log.d("GG", "save pic fail");
					             e.printStackTrace();  
					        } 
			            	picTaked = true;
			                myCamera.takePicture(null, null, jpegCallback); 
			                try{
			            		Thread.sleep(500);
			            	} catch (Exception e) { 
								//Log.d("GG", "save pic fail");
					             e.printStackTrace();  
					        } 
			                LoadingDialogControl.dismiss();
			            } 
				    }).start();
					

				}
				
				
			});
	        
	        nextView.setOnClickListener(new View.OnClickListener(){
				
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					if(!picTaked)
						return;
					
					LoadingDialogControl.show(AppealActivity.this);
					
					new Thread(new Runnable(){
			            @Override
			            public void run() {
			            	try{
			            		Thread.sleep(500);
			            	} catch (Exception e) { 
								//Log.d("GG", "save pic fail");
					             e.printStackTrace();  
					        } 
			            	
			            	while(focusing || !pic_save);
							
							try  
							{ 
								
								File picFile = new File(dir + File.separator + "Appeal");
								if(!picFile.exists())
									picFile.mkdir();
								
								picFile = new File(dir + File.separator + "Appeal" + 
												File.separator + String.valueOf(ts));
								
								if(!picFile.exists())
									picFile.mkdir();
								
								picFile = new File(dir + File.separator + "Appeal" + 
										File.separator + String.valueOf(ts) + File.separator +"picture.jpeg");
								
								BufferedOutputStream bos  = new BufferedOutputStream(new FileOutputStream(picFile));  
								jpegPic.compress(Bitmap.CompressFormat.JPEG,100,bos);   
								bos.flush();   
								bos.close(); 
								
								picTaked = false;
								//camera_using = false;
								//TakePicState();
								
								CameraPreviewSet();
					            
								
								//onDestroy();
								//finish();
								
							} catch (IOException e) { 
								//Log.d("GG", "save pic fail");
					             e.printStackTrace();  
					        } 
							LoadingDialogControl.dismiss();
							Intent intent = new Intent();
				            intent.setClass(AppealActivity.this, RecordVoiceActivity.class);
				            startActivity(intent);
			            } 
				    }).start();
					//wait for auto focus finish
					
			           
					
				}		
				
				
				
			});

	        undoView.setOnClickListener(new View.OnClickListener(){
				
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					if(!picTaked)
						return;						
					
					CameraPreviewSet();
					TakePicState();
				}
				
				
			});
	        
	        cancelView.setOnClickListener(new View.OnClickListener(){
				
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					camera_using = false;
					
					onDestroy();
					finish();
				}
				
				
			});
	        Log.d("GG", "10");
	        TakePicState();
	        Log.d("GG", "11");
    }
    
    @SuppressWarnings("deprecation")
	public void onSensorChanged(SensorEvent sensorEvent) {
    	Sensor mySensor = sensorEvent.sensor;
    	if(picTaked)
    		return;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            
            long curTime = System.currentTimeMillis();
            
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
     
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
                
                if (speed > SHAKE_THRESHOLD) {
                	
                	if(camera_using){
            			myCamera.autoFocus(new AutoFocusCallback() {
            				@Override
            				public void onAutoFocus(boolean success, Camera camera) {  
            					// TODO Auto-generated method stub 
            					if(success)  
            					{ 
            						focusing  = true;
            						Camera.Parameters params = myCamera.getParameters();   
            						params.setPictureFormat(PixelFormat.JPEG); 
            						
            						/*if(pictureSize == null)
            							pictureSize = getPictureSize(myCamera.getParameters().getSupportedPictureSizes(), vHeight);    
            						if(previewSize == null)
            							previewSize = getPreviewSize(myCamera.getParameters().getSupportedPreviewSizes(), vHeight); 
            						
            						if(previewSize!=null)  
            				            params.setPreviewSize(previewSize.width,previewSize.height);  
            				        if(pictureSize!=null)  
            				            params.setPictureSize(pictureSize.width,pictureSize.height); */ 
            						//params.setPreviewSize(vWidth,vHeight);  
            						pictureSize = params.getPreviewSize();
            				        params.setPictureSize(pictureSize.width,pictureSize.height);
            				        
            						myCamera.setParameters(params);				
            						
            						focusing  = false;
            					}  
            				}
            			});
            			
            		}
                }
     
                last_x = x;
                last_y = y;
                last_z = z;
            }

        }
    	
    }
    
    private void TakePicState(){
    	focusing  = false;
    	camera_using = true;
    	
    	//mhandler = new Handler();
        //mhandler.postDelayed(updateTimer, 2000);
        
    	ImageView1.setVisibility(View.INVISIBLE); 
		picTaked = false;
		takePicView.setVisibility(View.VISIBLE); 
        nextText.setVisibility(View.INVISIBLE); 
        undoText.setVisibility(View.INVISIBLE);
        lineView.setVisibility(View.VISIBLE);
        hintText.setVisibility(View.VISIBLE);
        hint2Text.setVisibility(View.INVISIBLE);
        
        cancelText.setVisibility(View.INVISIBLE); 
    }
    
    private void ViewPicState(){
    	
    	camera_using = false;
    	
    	//if(mhandler != null)
    	//mhandler.removeCallbacksAndMessages(null);
    	//mhandler = null; 
    	
    	ImageView1.setVisibility(View.VISIBLE); 
		picTaked = true;
		takePicView.setVisibility(View.INVISIBLE); 
        nextText.setVisibility(View.VISIBLE); 
        undoText.setVisibility(View.VISIBLE);
        lineView.setVisibility(View.INVISIBLE);
        hintText.setVisibility(View.INVISIBLE);
        hint2Text.setVisibility(View.VISIBLE);
        
        cancelText.setVisibility(View.VISIBLE); 
    }
    
    
	@SuppressWarnings("deprecation")
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		 myCamera.startPreview();  
		 
 		
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(myCamera == null)  
			 
        {   
            myCamera = Camera.open();//開启相機,不能放在構造函數中，不然不會顯示畫面.  
 
            try {   
                myCamera.setPreviewDisplay(holder);  
 
            } catch (IOException e) {  
                 // TODO Auto-generated catch block   
                e.printStackTrace();  
 
            }   
        }   
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		if(myCamera != null){
        	myCamera.stopPreview(); 
        	myCamera.release(); 
        	myCamera = null;  
        }
	}
	 
	private PictureCallback jpegCallback = new PictureCallback() {         
	   public void onPictureTaken(byte[] data, Camera camera) {  
	 
	   // TODO Auto-generated method stub   
		   try  
           {  
			   pic_save = false;
			   jpegPic = BitmapFactory.decodeByteArray(data, 0, data.length);  
               
               showPic = jpegPic;
               ImageView1.setImageBitmap(showPic);
              
               ViewPicState();
               pic_save = true;
               
           }catch(Exception e){ 
               e.printStackTrace();   
           }      
	   } 
	};
	
    
    private void CameraPreviewSet(){
    	if(myCamera != null){
			try {   
				myCamera.setPreviewDisplay(holder);  
				myCamera.startPreview();
				
			} catch (IOException e) {  
				// TODO Auto-generated catch block  
				
				e.printStackTrace();  

			}
		}
    }
    
    @Override
    protected void onStart()
	{ 
        super.onStart();
      
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	aSensorManager.unregisterListener(this);
    	
    	camera_using = false;
    	
    	if(mhandler != null)
    		mhandler.removeCallbacksAndMessages(null);
    	
    	mhandler = null; 
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	if(aSensorManager != null)
    		aSensorManager.registerListener(this, aSensor, aSensorManager.SENSOR_DELAY_NORMAL);
    	
    	if(picTaked)
    	{
    		ViewPicState();
    	}
    	else if(!picTaked)
    	{
    		CameraPreviewSet();
    		TakePicState();
    		
    	}
    	
    	LoadingDialogControl.dismiss();
    }
    
	@Override
    protected void onDestroy()
	{ 
        super.onDestroy();
        
        if(myCamera != null){
        	myCamera.stopPreview(); 
        	myCamera.release(); 
        	myCamera = null;  
        }
        finish();
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	public  Size getPreviewSize(List<Camera.Size> list, int th){  
        Collections.sort(list, sizeComparator);  
        Size size=null;  
        for(int i=0;i<list.size();i++){  
            size=list.get(i);  
            if((size.width>th)&&equalRate(size, 1.3f)){  
                break;  
            }  
        }  
        return size;  
    }  
    public Size getPictureSize(List<Camera.Size> list, int th){  
        Collections.sort(list, sizeComparator);  
        Size size=null;  
        for(int i=0;i<list.size();i++){  
            size=list.get(i);  
            if((size.width>th)&&equalRate(size, 1.3f)){  
                break;  
            }  
        }  
        return size;  
          
    }  
      
    public boolean equalRate(Size s, float rate){  
        float r = (float)(s.width)/(float)(s.height);  
        if(Math.abs(r - rate) <= 0.2)  
        {  
            return true;  
        }  
        else{  
            return false;  
        }  
    }  
      
    public  class CameraSizeComparator implements Comparator<Camera.Size>{  
        //按升序排列  
        @Override  
        public int compare(Size lhs, Size rhs) {  
            // TODO Auto-generated method stub  
            if(lhs.width == rhs.width){  
            return 0;  
            }  
            else if(lhs.width > rhs.width){  
                return 1;  
            }  
            else{  
                return -1;  
            }  
        }  
          
    }  

}