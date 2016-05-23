package com.ubicomp.ketdiary.data.download;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;

import com.ubicomp.ketdiary.data.db.DatabaseControl;
import com.ubicomp.ketdiary.data.structure.TriggerItem;
import com.ubicomp.ketdiary.data.upload.ServerUrl;
import com.ubicomp.ketdiary.system.PreferenceControl;

import android.content.Context;
import android.util.Log;

public class TriggerListCollect {
	private static String SERVER_URL_SVM = null;
	private static String SERVER_URL_SCALE = null;
	private static String SERVER_URL_VERSION = null;
	private static String SERVER_URL_TRIGGER = null;
	private static String TAG = "SvmModelDownloader";

	private ResponseHandler<String> responseHandler;
	private Context context;
	
	private int SvmVersion, TriggerVersion;
	private ArrayList<TriggerItem> list = new ArrayList<TriggerItem>();
	private DatabaseControl db = new DatabaseControl();
	
	public TriggerListCollect(Context context) {
		this.context = context;
		SERVER_URL_SVM = ServerUrl.SERVER_URL_SVM_MODEL();
		SERVER_URL_SCALE = ServerUrl.SERVER_URL_SCALE_PARAM();
		SERVER_URL_VERSION = ServerUrl.SERVER_URL_VERSION();
		SERVER_URL_TRIGGER = ServerUrl.SERVER_URL_TRIGGER();
		responseHandler = new BasicResponseHandler();
	}
	
	public void update()
	{	
		getTriggerVersion();
		
		int nowTriggerVersion = PreferenceControl.getTriggerVersion(); 
		//int nowTriggerVersion = PreferenceControl.getTriggerVersion(); 
		//Log.d("GG", "check Trigger List from version"+nowTriggerVersion+" to version"+TriggerVersion);
		if(nowTriggerVersion < TriggerVersion)
		{
			Log.d("GG", "Update Trigger List from version"+nowTriggerVersion+" to version"+TriggerVersion);
			getTriggerList();
			addTriggetItem();
			Log.d("GG", "Update Trigger List Finished");
			
			PreferenceControl.setTriggerVersion(TriggerVersion); 
		}
			
		
		return;  
	}
	
	public void getTriggerVersion()
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
	
	public void getTriggerList()
	{
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

			HttpPost httpPost = new HttpPost(SERVER_URL_TRIGGER);
			httpClient.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					3000);

			HttpResponse httpResponse;
			httpResponse = httpClient.execute(httpPost);
			String responseString = responseHandler
					.handleResponse(httpResponse);
			int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			
			//responseString = unicode2String(responseString);
			
			String itemName = "", itemContent = "";
			if (responseString != null && httpStatusCode == HttpStatus.SC_OK) {
				
				 StringTokenizer st = new StringTokenizer(responseString,"[],\"");
				 Log.d("GG", "string : "+ responseString);
				 int t = 0;
				 while(st.hasMoreTokens()) { 
					 if(t % 2 == 0)
					 {
						 itemName = st.nextToken();
						 //Log.d("GG", "item number : "+ itemName);
					 }
					 else
					 {
						 itemContent = unicode2String(st.nextToken());
						 //Log.d("GG", "item content : "+ itemContent);
						 
						 TriggerItem triggerItem = new TriggerItem(Integer.valueOf(itemName), itemContent, false);
						 list.add(triggerItem);
					 }
					 t++;
                 }
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}
	
	public static String unicode2String(String unicode) {
		 
	    StringBuffer string = new StringBuffer();
	 
	    String[] hex = unicode.split("\\\\u");
	 
	    for (int i = 1; i < hex.length; i++) {
	 
	        // 转换出每一个代码点
	    	hex[i] = hex[i].trim();
	    	//Log.d("GG", "now string : "+ hex[i]);
	        int data = Integer.parseInt(hex[i], 16);
	 
	        // 追加成string
	        string.append((char) data);
	    }
	 
	    return string.toString();
	}
	
	public void addTriggetItem() {
		
		int len = list.size();
		
		for(int i = 0; i < len; i++)
		{
			//Log.d("GG", "item"+(i+1)+" : "+ list.get(i).getItem() +" - "+ list.get(i).getContent() );
			if(!db.findTriggerItem(list.get(i).getItem()))
			{
				db.insertTriggerItem(list.get(i));
				//Log.d("GG", "insert item"+(i+1)+" : "+ list.get(i).getItem() +" - "+ list.get(i).getContent() );
			}
		}
	}
}
