package com.ubicomp.ketdiary.data.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;

import com.ubicomp.ketdiary.data.file.MainStorage;
import com.ubicomp.ketdiary.data.structure.Cassette;
import com.ubicomp.ketdiary.data.upload.ServerUrl;
import com.ubicomp.ketdiary.system.PreferenceControl;

import android.content.Context;
import android.util.Log;

public class SvmModelDownloader {
	private static String SERVER_URL_SVM = null;
	private static String SERVER_URL_SCALE = null;
	private static String SERVER_URL_VERSION = null;
	private static String TAG = "SvmModelDownloader";

	private ResponseHandler<String> responseHandler;
	private Context context;
	
	private int SvmVersion, TriggerVersion;
	
	
	public SvmModelDownloader(Context context) {
		this.context = context;
		SERVER_URL_SVM = ServerUrl.SERVER_URL_SVM_MODEL();
		SERVER_URL_SCALE = ServerUrl.SERVER_URL_SCALE_PARAM();
		SERVER_URL_VERSION = ServerUrl.SERVER_URL_VERSION();
		responseHandler = new BasicResponseHandler();
	}
	
	public void update()
	{	
		
		getSvmVersion();
		int nowSvmVersion = PreferenceControl.getSvmVersion(); 
		//int nowTriggerVersion = PreferenceControl.getTriggerVersion(); 
		
		if(nowSvmVersion < SvmVersion)
		{
			Log.d(TAG, "Update SVM Model from version"+nowSvmVersion+" to version"+SvmVersion);
			downloadFile(SERVER_URL_SVM, "SVM/model.out");
			downloadFile(SERVER_URL_SCALE, "SVM/scale_param.out");
			Log.d(TAG, "Update SVM Model Finished");
			
			PreferenceControl.setSvmVersion(SvmVersion); 
		}
		
		return;   	
    }
	
	public void getSvmVersion()
	{
		int version = 0;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			InputStream instream = context.getResources().openRawResource(
					ServerUrl.SERVER_CERTIFICATE());
			try {
				trustStore.load(instream, null);
			} finally {
				instream.close();
			}
			SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
			Scheme sch = new Scheme("https", socketFactory, 443);

			httpClient.getConnectionManager().getSchemeRegistry().register(sch);

			HttpPost httpPost = new HttpPost(SERVER_URL_VERSION);
			httpClient.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					3000);

			HttpResponse httpResponse;
			httpResponse = httpClient.execute(httpPost);
			String responseString = responseHandler
					.handleResponse(httpResponse);
			int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			
			
			
			if (responseString != null && httpStatusCode == HttpStatus.SC_OK) {
				
				 StringTokenizer st = new StringTokenizer(responseString,"[],\"");
				
				 int t = 0;
				 while(st.hasMoreTokens()) { 
					 if(t == 0)
						 SvmVersion = Integer.parseInt(st.nextToken());
					 if(t == 1)
						 TriggerVersion = Integer.parseInt(st.nextToken());
					 t++;
                 }
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}
	
	public void downloadFile(String source, String destination)
	{
		HttpClient httpClient = null;
		try {
			File mainStorage = MainStorage.getMainStorageDirectory();
			if(!mainStorage.exists())
				mainStorage.mkdir();
			File SvmFile = new File(mainStorage + File.separator + "SVM");
			if(!SvmFile.exists())
				SvmFile.mkdir();
			
	        File file = new File(mainStorage, destination);
	        //File fileScale = new File(mainStorage,"SVM/scale_param.out");
	        
			//DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient = new DefaultHttpClient();
	
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			
			InputStream instream = context.getResources().openRawResource(
					ServerUrl.SERVER_CERTIFICATE());
			try {
				trustStore.load(instream, null);
			} finally {
				instream.close();
			}
			SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
			Scheme sch = new Scheme("https", socketFactory, 443);
	
			httpClient.getConnectionManager().getSchemeRegistry().register(sch);
	
			HttpGet httpGet = new HttpGet(source);
			httpClient.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					3000);
	
			HttpResponse httpResponse;
			
			httpResponse = httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
	        
			InputStream in = entity.getContent();
			FileOutputStream out = new FileOutputStream(file);
			byte[] b = new byte[4096];
			int len = 0;
			while((len=in.read(b))!= -1)
			{
				out.write(b,0,len);
			}
			out.flush();

			in.close();
			out.close();
			
			httpClient.getConnectionManager().shutdown();    
			 
		

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();    
		}
		

		return;
	}
    
  
}
