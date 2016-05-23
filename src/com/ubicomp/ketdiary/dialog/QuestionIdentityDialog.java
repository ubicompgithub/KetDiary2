package com.ubicomp.ketdiary.dialog;

import java.sql.Date;
import java.util.Calendar;
import java.util.Random;

import com.ubicomp.ketdiary.App;
import com.ubicomp.ketdiary.MainActivity;
import com.ubicomp.ketdiary.data.db.DatabaseControl;
import com.ubicomp.ketdiary.data.structure.AddScore;
import com.ubicomp.ketdiary.data.structure.IdentityScore;
import com.ubicomp.ketdiary.data.structure.NoteAdd;
import com.ubicomp.ketdiary.data.structure.QuestionTest;
import com.ubicomp.ketdiary.data.structure.ThinkingEvent;
import com.ubicomp.ketdiary.system.PreferenceControl;
import com.ubicomp.ketdiary.system.clicklog.ClickLog;
import com.ubicomp.ketdiary.system.clicklog.ClickLogId;
import com.ubicomp.ketdiary.ui.CustomToast;
import com.ubicomp.ketdiary.ui.Typefaces;
import com.ubicomp.ketdiary2.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;

public class QuestionIdentityDialog {
	private Activity activity;
	private QuestionIdentityDialog noteFragment = this;
	private QuestionCaller questionCaller;
	private static final String TAG = "ADD_PAGE";
	
	private TestQuestionCaller testQuestionCaller;
	private Context context;
	private LayoutInflater inflater;
	private RelativeLayout boxLayout = null;
	private LinearLayout questionLayout;
	
	private static String question = "";
	private static String answer = "";
	private String selectedAnswer = "";
	private static String[] selection;
	private static int typeID;

	private RelativeLayout mainLayout;
	private String[] type_str;
	
	private TextView tv_event, tv_feeling, tv_thinking, tv_confirm, tv_cancel;
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

	private int canNum = 3;
	private String[] canEvents = {"跟朋友聊天", "瘋狂", "好久沒見，找他一起拉K好了"};
	private String[] canFeelings = {"媽媽又碎碎念", "無言以對", "找事情做，忘記媽媽念的內容"};
	private String[] canThinkings = {"在家看球賽", "不爽", "輸球很不爽，用K感覺比較好"};
	
	private int isReflection;
	private int score;
	private int key;
	
	public QuestionIdentityDialog(RelativeLayout mainLayout){
		
		this.context = App.getContext();
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mainLayout = mainLayout;
		
		db = new DatabaseControl();
		
		wordTypeface = Typefaces.getWordTypeface();
		wordTypefaceBold = Typefaces.getWordTypefaceBold();
		digitTypeface = Typefaces.getDigitTypeface();
		digitTypefaceBold = Typefaces.getDigitTypefaceBold();
		
		type_str = context.getResources().getStringArray(R.array.trigger_list);
	    setting();
	    mainLayout.addView(boxLayout);

	}
	
	protected void setting() {
		
		boxLayout = (RelativeLayout) inflater.inflate(
				R.layout.dialog_answer_identity, null);
		boxLayout.setVisibility(View.INVISIBLE);
		
		tv_event = (TextView) boxLayout.findViewById(R.id.question_event);
		tv_feeling = (TextView) boxLayout.findViewById(R.id.question_feeling);
		tv_thinking = (TextView) boxLayout.findViewById(R.id.question_thinking);
		bar = (SeekBar) boxLayout.findViewById(R.id.question_seek_bar);
		
		tv_confirm = (TextView) boxLayout.findViewById(R.id.question_identity_confirm);
		tv_cancel = (TextView)boxLayout.findViewById(R.id.question_identity_cancel);
		
		tv_confirm.setOnClickListener(new ConfirmOnClickListener());
		tv_cancel.setOnClickListener(new CancelOnClickListener());
		
		/*Random ran = new Random();
        int index = ran.nextInt(3);
        
        tv_event.setText(canEvents[index]);
        tv_feeling.setText(canFeelings[index]);
        tv_thinking.setText(canThinkings[index]);*/
        
        settingQuestion();
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
			
			ClickLog.Log(ClickLogId.DAYBOOK_RANDOMTEST_CONFIRM);
			
			MainActivity.getMainActivity().enableTabAndClick(true);
			inState = false;
			close();
			sendAnswer();
			
			Calendar cal = Calendar.getInstance();
			long ts = cal.getTimeInMillis();
			
			long pre_ts = PreferenceControl.getLastestIdentifyTimestamp();
			Date d = new Date(pre_ts);
			Calendar pre_cal = Calendar.getInstance();
			pre_cal.setTime(d);
			
			boolean sameDay = cal.get(Calendar.YEAR) == pre_cal.get(Calendar.YEAR) &&
	                  cal.get(Calendar.DAY_OF_YEAR) == pre_cal.get(Calendar.DAY_OF_YEAR);

			PreferenceControl.setLastestIdentifyTimestamp(ts);
			
			MainActivity.identityScore = false;
			if(AddNoteDialog2.testFinished)
			{
				//finised
				Log.d("GG", "認同度 檢測結束");
				if(!sameDay)
					MainActivity.identityScore = true;
				AddNoteDialog2.goResult();
			}
			else
			{
				Log.d("GG", "認同度 檢測未結束");
				if(sameDay)
				{
					CustomToast.generateToast(R.string.add_note_identity, 1);
					AddScore preScore = db.getLastestAddScore();
					AddScore nowScore = new AddScore(System.currentTimeMillis(), 1, preScore.getAccumulation()+1, "填寫認同度");
					db.insertAddScore(nowScore);
				}
			}
		}

	}


	private class CancelOnClickListener implements View.OnClickListener {
		@Override
		/**Calling out*/
		public void onClick(View v) {
			ClickLog.Log(ClickLogId.DAYBOOK_RANDOMTEST_CANCEL);		
			MainActivity.getMainActivity().enableTabAndClick(true);
			inState = false;
        	close();
			//clear();
        	if(AddNoteDialog2.testFinished)
			{
        		AddNoteDialog2.goResult();
			}
		}
	}
	
	/** OnClickListener for checking out */
	private class RadioOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			//resetAllImage();
			switch (v.getId()){
			case R.id.question_answer1_layout:
				ClickLog.Log(ClickLogId.DAYBOOK_RANDOMTEST_SELECT_A);
				radio1.setImageResource(R.drawable.radio_node_select);
				selectedAnswer = selection[0];
				select = 0;
				break;
			case R.id.question_answer2_layout:
				ClickLog.Log(ClickLogId.DAYBOOK_RANDOMTEST_SELECT_B);
				radio2.setImageResource(R.drawable.radio_node_select);
				selectedAnswer = selection[1];
				select = 1;
				break;
			case R.id.question_answer3_layout:
				ClickLog.Log(ClickLogId.DAYBOOK_RANDOMTEST_SELECT_C);
				radio3.setImageResource(R.drawable.radio_node_select);
				selectedAnswer = selection[2];
				select = 2;
				break;
			case R.id.question_answer4_layout:
				ClickLog.Log(ClickLogId.DAYBOOK_RANDOMTEST_SELECT_D);
				radio4.setImageResource(R.drawable.radio_node_select);
				selectedAnswer = selection[3];
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
	
	private void settingQuestion() {
		ThinkingEvent[] data = db.getAllThinking();
		if(data == null)
			return;
		int len = data.length;
		if(len == 0)
			return;
		Random ran = new Random();
        int index = ran.nextInt(len);
        
        tv_event.setText(data[index].getEvent());
        tv_feeling.setText(data[index].getFeeling());
        tv_thinking.setText(data[index].getThinking());
		
        key = data[index].getKey();
        isReflection = data[index].getIsReflection();
	}
	
	private void sendAnswer() {
		long ts = System.currentTimeMillis();
		score = bar.getProgress();
		IdentityScore data = new IdentityScore(ts, score, key, isReflection);
		db.insertIdentityScore(data);
	}
	
}
