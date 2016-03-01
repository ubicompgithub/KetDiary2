package com.ubicomp.ketdiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ubicomp.ketdiary.system.check.NetworkCheck;
import com.ubicomp.ketdiary2.R;

/**
 * Receiver for getting the network condition
 * 
 * @author Stanley Wang
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

	private static final String TAG = "NETWORK_CHANGE_RECEIVER";

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		Log.d(TAG, action);
		//Log.d("GG","01");

		if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION)
				&& !action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION))
			return;
		
		/*LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout description_thinking_layout = (LinearLayout) inflater.inflate(
				R.layout.bar_description_thinking, null);
		ImageView speech_button = (ImageView) description_thinking_layout.findViewById(R.id.speech_to_text);
		Log.d("GG","02");*/
		
		if (!NetworkCheck.networkCheck()) {
			Log.d(TAG, "NOT CONNECTED");
			/*MainActivity.networkState = false;
			speech_button.setImageResource(R.drawable.speech_icon_unable);
			Log.d("GG","XX");*/
			return;
		}
		
		/*MainActivity.networkState = true;
		speech_button.setImageResource(R.drawable.speech_icon);
		Log.d("GG","YY");*/

		Intent regularIntent = new Intent(context, UploadService.class);
		context.startService(regularIntent);

	}

}
