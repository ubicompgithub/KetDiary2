package com.ubicomp.ketdiary;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
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
import com.ubicomp.ketdiary.ui.Typefaces;

/**
 * Activity for Appeal
 * 
 * @author Ivan 
 */


public class AppealActivity extends Activity implements SurfaceHolder.Callback{
	private SurfaceView surfaceView1;
	private ImageView ImageView1;
	private SurfaceHolder holder;   
	private Camera myCamera;  
    private Button takePicButton, nextButton, undoButton;
    private File dir;
    private boolean picTaked;
    private Bitmap jpegPic, showPic;
    
    public Activity activity = null;
    private long ts;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_picture);
        
        ts = TestFragment2.appealTimeValue.getTimestamp();
        
        ImageView1 = (ImageView)findViewById(R.id.imageView1);
        ImageView1.setVisibility(View.INVISIBLE); 
        
        surfaceView1 = (SurfaceView)findViewById(R.id.surfaceView1);
        holder = surfaceView1.getHolder();//獲得surfaceHolder引用   
        holder.addCallback(this);   
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//設置類型 
        
        picTaked = false;
        
        dir = MainStorage.getMainStorageDirectory();
        
        takePicButton = (Button)findViewById(R.id.takePic);
        takePicButton.setOnClickListener(new OnClickListener(){
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if(picTaked)
					return;
				
				myCamera.autoFocus(new AutoFocusCallback() {
			        @Override
			        public void onAutoFocus(boolean success, Camera camera) {  
			            // TODO Auto-generated method stub 
			            if(success)  
			             {  
			                //設置参數,並拍照  
			                Camera.Parameters params = myCamera.getParameters();   
			                params.setPictureFormat(PixelFormat.JPEG);  
			                params.setPreviewSize(640,480);  
			                myCamera.setParameters(params);   
			                myCamera.takePicture(null, null, jpegCallback);  
			            }  
			        }
				});
			}
			
			
		});
        
        nextButton = (Button)findViewById(R.id.nextPic);
        nextButton.setOnClickListener(new OnClickListener(){
			
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
					
					Intent intent = new Intent();
	                intent.setClass(AppealActivity.this, RecordVoiceActivity.class);
	                startActivity(intent); 
					
					onDestroy();
					finish();
					
				} catch (IOException e) {    
		             e.printStackTrace();  
		        } 
			}		
			
			
			
		});
        
        undoButton = (Button)findViewById(R.id.undoPic);
        undoButton.setOnClickListener(new OnClickListener(){
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if(!picTaked)
					return;
				
				ImageView1.setVisibility(View.INVISIBLE); 
				picTaked = false;
				takePicButton.setVisibility(View.VISIBLE); 
	            nextButton.setVisibility(View.INVISIBLE); 
	            undoButton.setVisibility(View.INVISIBLE); 
			}
			
			
		});
        
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
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		myCamera.stopPreview();//停止預覽   
        myCamera.release();//釋放相機資源  
        myCamera = null;  

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
               ImageView1.setVisibility(View.VISIBLE); 
               //File file = new File("/sdcard/wjh.jpg");   
            
               
               picTaked = true;
               takePicButton.setVisibility(View.INVISIBLE); 
               nextButton.setVisibility(View.VISIBLE); 
               undoButton.setVisibility(View.VISIBLE); 
               
           }catch(Exception e){ 
               e.printStackTrace();   
           }      
	   } 
	};
	
	@Override
    protected void onDestroy(){ //真正作用區
        super.onDestroy();
        finish();
    }
}