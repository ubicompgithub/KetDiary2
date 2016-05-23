package com.ubicomp.ketdiary.dialog;

import java.util.Calendar;
import java.util.Random;

import com.ubicomp.ketdiary.AlarmReceiver;
import com.ubicomp.ketdiary.App;
import com.ubicomp.ketdiary.MainActivity;
import com.ubicomp.ketdiary.data.db.DatabaseControl;
import com.ubicomp.ketdiary.data.structure.AddScore;
import com.ubicomp.ketdiary.data.structure.IdentityScore;
import com.ubicomp.ketdiary.data.structure.ThinkingEvent;
import com.ubicomp.ketdiary.system.Config;
import com.ubicomp.ketdiary.system.clicklog.ClickLog;
import com.ubicomp.ketdiary.system.clicklog.ClickLogId;
import com.ubicomp.ketdiary.ui.CustomToast;
import com.ubicomp.ketdiary.ui.Typefaces;
import com.ubicomp.ketdiary2.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class NotifyThinkingDialog {
	private Activity activity;
	private NotifyThinkingDialog noteFragment = this;
	private QuestionCaller questionCaller;
	private static final String TAG = "ADD_PAGE";
	private AddNoteDialog2 addNoteDialog;
	
	private TestQuestionCaller testQuestionCaller;
	private Context context;
	private LayoutInflater inflater;
	private RelativeLayout boxLayout = null;
	private LinearLayout questionLayout;
	
	private static String[] selection;
	private static int typeID;

	private RelativeLayout mainLayout;
	private String[] type_str;
	
	private TextView tv_confirm, tv_cancel;
	private CheckBox[] checkBox = new CheckBox[4];
	private int[] checkBoxValue = {0, 1, 2, 4};
	private ImageView radio1, radio2, radio3, radio4;
	private static ImageView iv_type;
	private SeekBar bar;
	
	private LinearLayout layout1, layout2, layout3, layout4;
	/** @see Typefaces */
	private Typeface wordTypeface, wordTypefaceBold, digitTypeface,
			digitTypefaceBold;
	
	private int select = -1;
	private DatabaseControl db;
	private int questionType = 1;
	private static boolean change = true;
	public static boolean inState;
	private static long last_visit= 0;

	
	private int isReflection;
	private int score;
	private int key;
	
	public static final int requestCodeRegularNotificationBase = 0x3000;
	
	public NotifyThinkingDialog(AddNoteDialog2 addNoteDialog, RelativeLayout mainLayout){
		
		this.context = App.getContext();
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mainLayout = mainLayout;
		this.addNoteDialog = addNoteDialog;
		db = new DatabaseControl();
		
		wordTypeface = Typefaces.getWordTypeface();
		wordTypefaceBold = Typefaces.getWordTypefaceBold();
		digitTypeface = Typefaces.getDigitTypeface();
		digitTypefaceBold = Typefaces.getDigitTypefaceBold();
		
		type_str = context.getResources().getStringArray(R.array.trigger_list);
		
		select = 0;
		
	    setting();
	    mainLayout.addView(boxLayout);

	}
	
	protected void setting() {
		
		boxLayout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_notify_thinking, null);
		boxLayout.setVisibility(View.INVISIBLE);
		
		radio1 = (ImageView) boxLayout.findViewById(R.id.notify_radio1);
		radio2 = (ImageView) boxLayout.findViewById(R.id.notify_radio2);
		radio3 = (ImageView) boxLayout.findViewById(R.id.notify_radio3);
		radio4 = (ImageView) boxLayout.findViewById(R.id.notify_radio4);
		
		radio1.setImageResource(R.drawable.radio_node_select);
		radio2.setImageResource(R.drawable.radio_node);
		radio3.setImageResource(R.drawable.radio_node);
		radio4.setImageResource(R.drawable.radio_node);
		
		layout1 = (LinearLayout) boxLayout.findViewById(R.id.notify_answer1_layout);
		layout2 = (LinearLayout) boxLayout.findViewById(R.id.notify_answer2_layout);
		layout3 = (LinearLayout) boxLayout.findViewById(R.id.notify_answer3_layout);
		layout4 = (LinearLayout) boxLayout.findViewById(R.id.notify_answer4_layout);
		
		layout1.setOnClickListener(new RadioOnClickListener());
		layout2.setOnClickListener(new RadioOnClickListener());
		layout3.setOnClickListener(new RadioOnClickListener());
		layout4.setOnClickListener(new RadioOnClickListener());
		
		tv_confirm = (TextView) boxLayout.findViewById(R.id.notify_confirm);
		tv_cancel = (TextView)boxLayout.findViewById(R.id.notify_cancel);
		
		tv_confirm.setOnClickListener(new ConfirmOnClickListener());
		tv_cancel.setOnClickListener(new CancelOnClickListener());
		
		/*Random ran = new Random();
        int index = ran.nextInt(3);
        
        tv_event.setText(canEvents[index]);
        tv_feeling.setText(canFeelings[index]);
        tv_thinking.setText(canThinkings[index]);*/
        
        //settingQuestion();
	}
	

	
	/** Initialize the dialog */
	public void initialize() {
		//RelativeLayout.LayoutParams boxParam = (RelativeLayout.LayoutParams) boxLayout.getLayoutParams();
		RelativeLayout.LayoutParams boxParam = (LayoutParams) boxLayout
				.getLayoutParams();
		boxParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		boxParam.width = LayoutParams.MATCH_PARENT;
		boxParam.height = LayoutParams.MATCH_PARENT;
		
		
	}
	
	/** show the dialog */
	public void show(int type) {
		
		MainActivity.getMainActivity().enableTabAndClick(false);
		boxLayout.setVisibility(View.VISIBLE);
		inState = true;
		

	}
	
	/** remove the dialog and release the resources */
	public void clear() {
		if (mainLayout != null && boxLayout != null
				&& boxLayout.getParent() != null
				&& boxLayout.getParent().equals(mainLayout))
			mainLayout.removeView(boxLayout);
	}
	
	/** close the dialog */
	public void close() {
		//resetAllImage();
		if (boxLayout != null)
			boxLayout.setVisibility(View.INVISIBLE);
	}
	

	private static void shuffleArray(String[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; --i) {
			int index = rnd.nextInt(i + 1);
			String a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	private class ConfirmOnClickListener implements View.OnClickListener {

		@Override
		/**Cancel and dismiss the check check dialog*/
		public void onClick(View v) {
			int nowKey = addNoteDialog.sendNoteAdd();
			
			//不需提醒
			if(select == 0)
			{
				close();
				return;
			}
			
			AlarmManager alarm = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			Intent service_intent = new Intent();
			service_intent.setClass(context, AlarmReceiver.class);
			service_intent.setAction(Config.ACTION_THINKING_NOTIFICATION);

			Calendar c = Calendar.getInstance();
			
			int cur_year = c.get(Calendar.YEAR);
			int cur_month = c.get(Calendar.MONTH);
			int cur_date = c.get(Calendar.DAY_OF_MONTH);
			int cur_hour = c.get(Calendar.HOUR_OF_DAY);
			int cur_min = c.get(Calendar.MINUTE);
			
			//testing
			if(select == 1)
				c.set(cur_year, cur_month, cur_date, cur_hour, cur_min + 2, 0);
			else
				c.set(cur_year, cur_month, cur_date, cur_hour + checkBoxValue[select], cur_min, 0);
			
			
			PendingIntent pending = PendingIntent.getBroadcast(context,
					requestCodeRegularNotificationBase + nowKey, service_intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			
			alarm.cancel(pending);
			alarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pending);
			
			close();
		}

	}


	private class CancelOnClickListener implements View.OnClickListener {
		@Override
		/**Calling out*/
		public void onClick(View v) {
			
        	close();
		}
	}
	
	/** OnClickListener for checking out */
	private class RadioOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			//resetAllImage();
			radio1.setImageResource(R.drawable.radio_node);
			radio2.setImageResource(R.drawable.radio_node);
			radio3.setImageResource(R.drawable.radio_node);
			radio4.setImageResource(R.drawable.radio_node);
			switch (v.getId()){
			case R.id.notify_answer1_layout:
				ClickLog.Log(ClickLogId.DAYBOOK_RANDOMTEST_SELECT_A);
				radio1.setImageResource(R.drawable.radio_node_select);
				select = 0;
				break;
			case R.id.notify_answer2_layout:
				ClickLog.Log(ClickLogId.DAYBOOK_RANDOMTEST_SELECT_B);
				radio2.setImageResource(R.drawable.radio_node_select);
				select = 1;
				break;
			case R.id.notify_answer3_layout:
				ClickLog.Log(ClickLogId.DAYBOOK_RANDOMTEST_SELECT_C);
				radio3.setImageResource(R.drawable.radio_node_select);
				select = 2;
				break;
			case R.id.notify_answer4_layout:
				ClickLog.Log(ClickLogId.DAYBOOK_RANDOMTEST_SELECT_D);
				radio4.setImageResource(R.drawable.radio_node_select);
				select = 3;
				break;
			/*
			default:				
				((ImageView)v).setImageResource(R.drawable.radio_node_select);
				break;
				*/				
			}
	
		}
	}
	
}
