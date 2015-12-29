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
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;

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


public class AppealActivity extends Activity implements SurfaceHolder.Callback{
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
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //LoadingDialogControl.show(AppealActivity.this,0);
        
        fa = this;
        
        setContentView(R.layout.activity_get_picture);
        
        ts = TestFragment2.appealTimeValue.getTimestamp();
        
        ImageView1 = (ImageView)findViewById(R.id.imageView1);
        ImageView1.setVisibility(View.INVISIBLE); 
        
        lineView = (ImageView)findViewById(R.id.take_pic_line);
        
        surfaceView1 = (SurfaceView)findViewById(R.id.surfaceView1);
        
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
        
        End = false;
        
        takePicView.setOnClickListener(new View.OnClickListener(){
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if(picTaked)
					return;
				
                myCamera.takePicture(null, null, jpegCallback); 

			}
			
			
		});
        
        nextView.setOnClickListener(new View.OnClickListener(){
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if(!picTaked)
					return;
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
					camera_using = false;
					
					Intent intent = new Intent();
	                intent.setClass(AppealActivity.this, RecordVoiceActivity.class);
	                startActivity(intent); 
					
					//onDestroy();
					//finish();
					
				} catch (IOException e) {    
		             e.printStackTrace();  
		        } 
			}		
			
			
			
		});

        undoView.setOnClickListener(new View.OnClickListener(){
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if(!picTaked)
					return;
				
				
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
    }
    
    private void TakePicState(){
    	camera_using = true;
    	
    	mhandler = new Handler();
        mhandler.postDelayed(updateTimer, 2000);
        
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
    	
    	if(mhandler != null)
    		mhandler.removeCallbacksAndMessages(null);
    	mhandler = null; 
    	
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
               jpegPic = BitmapFactory.decodeByteArray(data, 0, data.length);  
               
               int _width = jpegPic.getWidth();
               int _height = jpegPic.getHeight();
               Display display = getWindowManager().getDefaultDisplay(); 
               int width = display.getWidth();  // deprecated
               int height = display.getHeight();
               float scaleWidth = (float)width / _width;
               float scaleHeight = (float)height / _height;
               Matrix matrix = new Matrix();
               matrix.postScale(scaleWidth, scaleHeight);
               
               showPic = Bitmap.createBitmap(jpegPic, 0, 0, _width, _height, matrix, true);
               ImageView1.setImageBitmap(showPic);
              
               ViewPicState();
               
           }catch(Exception e){ 
               e.printStackTrace();   
           }      
	   } 
	};
	
	
	private Runnable updateTimer = new Runnable(){
    	@SuppressWarnings("deprecation")
    	public void run(){
    		
    		if(camera_using){
    			myCamera.autoFocus(new AutoFocusCallback() {
    				@Override
    				public void onAutoFocus(boolean success, Camera camera) {  
    					// TODO Auto-generated method stub 
    					if(success)  
    					{  
    						Camera.Parameters params = myCamera.getParameters();   
    						params.setPictureFormat(PixelFormat.JPEG);  
    						params.setPreviewSize(1280,720);  
    						myCamera.setParameters(params);    
    					}  
    				}
    			});
    			mhandler.postDelayed(this, 2000);
    		}
		 }
    	
    };
    
    @Override
    protected void onStart()
	{ 
        super.onStart();
      
    	holder = surfaceView1.getHolder();
        holder.addCallback(this);   
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        dir = MainStorage.getMainStorageDirectory();
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	
    	camera_using = false;
    	
    	if(mhandler != null)
    		mhandler.removeCallbacksAndMessages(null);
    	
    	mhandler = null; 
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	if(picTaked)
    	{
    		ViewPicState();
    	}
    	else if(!picTaked)
    	{
    		TakePicState();
    	}
    	
    	//LoadingDialogControl.dismiss();
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

}