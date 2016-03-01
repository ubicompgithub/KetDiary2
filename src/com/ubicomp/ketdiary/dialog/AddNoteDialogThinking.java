package com.ubicomp.ketdiary.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.RecognizerIntent;

import com.ubicomp.ketdiary.App;
import com.ubicomp.ketdiary.MainActivity;
import com.ubicomp.ketdiary2.R;
import com.ubicomp.ketdiary.data.file.QuestionFile;
import com.ubicomp.ketdiary.data.structure.NoteAdd;
import com.ubicomp.ketdiary.noUse.NoteCatagory3;
import com.ubicomp.ketdiary.system.PreferenceControl;
import com.ubicomp.ketdiary.system.check.NetworkCheck;
import com.ubicomp.ketdiary.system.check.TimeBlock;
import com.ubicomp.ketdiary.system.clicklog.ClickLog;
import com.ubicomp.ketdiary.system.clicklog.ClickLogId;
import com.ubicomp.ketdiary.ui.BarButtonGenerator;
import com.ubicomp.ketdiary.ui.CustomScrollView;
import com.ubicomp.ketdiary.ui.CustomToast;
import com.ubicomp.ketdiary.ui.CustomToastSmall;
import com.ubicomp.ketdiary.ui.Typefaces;

public class AddNoteDialogThinking implements ChooseItemCaller{
	
	private Activity activity;
	private AddNoteDialogThinking addNoteDialog = this;
	private static final String TAG = "ADD_PAGE";
	private ReflectionFirstPage reflectionPage1;
	
	private TestQuestionCaller2 testQuestionCaller;
	private Context context;
	private Resources resource;
	private LayoutInflater inflater;
	private RelativeLayout boxLayout = null;
	private LinearLayout center_layout, title_layout, main_layout, bottom_layout, 
						title, date_layout, timeslot_layout;
						//description_date_layout, description_event_layout, description_mood_layout, description_thinking_layout;
	
	private ImageView speech_button;
	private EditText thinking_text;
	private TextView date_text, mood_text, event_text, reaction_text;
	
	private RelativeLayout mainLayout;
	private View view;
	
	private ViewPager vPager;
	private ImageView iv_try, iv_smile, iv_urge,
					  iv_cry, iv_not_good;
	private ImageView iv_conflict, iv_social, iv_playing;
	private ImageView iv_self_others_bar;
	private Spinner sp_date, sp_timeslot;// sp_item;
	private Button bt_confirm, bt_cancel;
	private SeekBar impactSeekBar;
	private TextView text_self, text_other, text_item, text_impact, text_description,
	     tv_knowdlege, tv_title, note_title, sp_content, date_txt, timeslot_txt, title_txt, typetext;
	
	private TextView edtext;
	private ListView listView;
	private ListView listView2;
	
	private String[] coping_msg;
	private String[] knowing_msg;
	private static int knowing_index=-1;
	
	private int state;
	private ChooseItemDialog chooseBox;
	private NoteCatagory3 noteCategory;
	
	
	//write File
	private File mainDirectory;
	private long timestamp = 0;
	private QuestionFile questionFile; 
	
	//Listener

	private EndOnClickListener endOnClickListener;
	private NextOnClickListener nextOnClickListener;
	private GoResultOnClickListener goResultOnClickListener;
	private GoCopingToResultOnClickListener goCopingToResultOnClickListener;
	private MyOnPageChangeListener myOnPageChangeListener;
	
	private int day=0;
	private int timeslot=0; //TODO: default
	private int type;
	private int items, items2;
	private int impact;
	private int moodNum;
	private boolean[] moodFlag = new boolean[20];
	private String description;
	private boolean viewshow = false, viewshow2 = false;
	
	private CustomScrollView sv;
	private boolean done = false;
	
	public static final int STATE_TEST = 0;
	public static final int STATE_NOTE = 1;
	public static final int STATE_COPE = 2;
	public static final int STATE_KNOW = 3;
	
	private static final String[] Timeslot_str = {"上午", "下午", "晚上"};
	private static final String[] Date_str = {"今天", "昨天", "前天"};
	protected static final int RESULT_SPEECH = 0;
	
	private static Typeface wordTypefaceBold = Typefaces.getWordTypefaceBold();
	private static Typeface wordTypeface = Typefaces.getWordTypeface();
	
	private LinearLayout description_date_layout, description_event_layout, description_mood_layout,
						description_thinking_layout, description_reaction_layout;
	
	private String actionNoteAdd, feelingNoteAdd;
	public AddNoteDialogThinking(TestQuestionCaller2 testQuestionCaller, RelativeLayout mainLayout, Activity activity){
		
		this.activity = activity;
		this.testQuestionCaller = testQuestionCaller;
		this.context = App.getContext();
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mainLayout = mainLayout;
		resource = context.getResources();
		
		coping_msg = resource.getStringArray(R.array.coping_list);
		
	    
		//view = inflater.inflate(R.layout.fragment_note, container, false);
		endOnClickListener = new EndOnClickListener();
		nextOnClickListener = new NextOnClickListener();
		goResultOnClickListener = new GoResultOnClickListener();
		goCopingToResultOnClickListener = new GoCopingToResultOnClickListener();
				
		noteCategory = new NoteCatagory3();
	    
	
	}
	
	protected void setting() {
		
		/*day = 0;
		type = -1;
		items = -1;
		impact = 0 ;
		description = "";*/
		
		boxLayout = (RelativeLayout) inflater.inflate(
				R.layout.note, null);
		boxLayout.setVisibility(View.INVISIBLE);
		title_layout = (LinearLayout) boxLayout.findViewById(R.id.note_title_layout);
		main_layout = (LinearLayout) boxLayout.findViewById(R.id.note_main_layout);
		
		
		
		bottom_layout = (LinearLayout) boxLayout.findViewById(R.id.note_bottom_layout);
		sv = (CustomScrollView) boxLayout.findViewById(R.id.note_main_scroll);
		
		//
		
		//Title View
		//View title = BarButtonGenerator.createAddNoteView(new DateSelectedListener(), new TimeslotSelectedListener() );
		
		title = (LinearLayout) inflater.inflate(
				R.layout.bar_addnote3, null);
		title_txt = (TextView)title.findViewById(R.id.note_title);
		date_layout = (LinearLayout) title.findViewById(R.id.note_date_layout);
		timeslot_layout = (LinearLayout) title.findViewById(R.id.note_timeslot_layout);
		date_txt = (TextView)title.findViewById(R.id.note_tx_date);
		timeslot_txt= (TextView)title.findViewById(R.id.note_tx_timeslot);
		
		checkAndSetTimeSlot();
			
		title_txt.setTypeface(wordTypefaceBold);
		date_txt.setTypeface(wordTypefaceBold);
		timeslot_txt.setTypeface(wordTypefaceBold);
		
		title_txt.setText("記錄想法");
		
		/*date_layout.setOnClickListener(new OnClickListener(){
			

			@Override
			public void onClick(View v) {
				
				ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_SELECT_DATE);
				//listView.setVisibility(View.GONE);
				setEnabledAll(boxLayout, false);
				
				chooseBox = new ChooseItemDialog(addNoteDialog,boxLayout, 1, day);
				chooseBox.initialize();
				chooseBox.show();
			}
			
		});
		
		timeslot_layout.setOnClickListener(new OnClickListener(){
			

			@Override
			public void onClick(View v) {
				ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_SELECT_SLOT);
				//listView.setVisibility(View.GONE);
				setEnabledAll(boxLayout, false);
				chooseBox = new ChooseItemDialog(addNoteDialog,boxLayout, 2, day);
				chooseBox.initialize();
				chooseBox.show();			
			}
			
		});*/
		
		title_layout.addView(title);
		
		description_date_layout = (LinearLayout) inflater.inflate(
				R.layout.bar_description_date, null);
		description_event_layout = (LinearLayout) inflater.inflate(
				R.layout.bar_description_event, null);
		description_mood_layout = (LinearLayout) inflater.inflate(
				R.layout.bar_description_mood, null);
		description_thinking_layout = (LinearLayout) inflater.inflate(
				R.layout.bar_edit_thinking, null);
		description_reaction_layout = (LinearLayout) inflater.inflate(
				R.layout.bar_description_reaction, null);
		
		TextView date_title, event_title, mood_title, thinking_title, reaction_title;
		date_title = (TextView) description_date_layout.findViewById(R.id.description_date_title);
		event_title = (TextView) description_event_layout.findViewById(R.id.description_event_title);
		mood_title = (TextView) description_mood_layout.findViewById(R.id.description_mood_title);
		thinking_title = (TextView) description_thinking_layout.findViewById(R.id.edit_thinking_title);
		reaction_title = (TextView) description_reaction_layout.findViewById(R.id.description_reaction_title);
		
		date_text = (TextView) description_date_layout.findViewById(R.id.description_date_content);
		event_text = (TextView) description_event_layout.findViewById(R.id.description_event_content);
		mood_text = (TextView) description_mood_layout.findViewById(R.id.description_mood_content);
		reaction_text = (TextView) description_reaction_layout.findViewById(R.id.description_reaction_content);
		
		thinking_title.setText("當時在想什麼 :");
		date_title.setText("發生日期 :");
		event_title.setText("發生事件 :");
		mood_title.setText("當時情緒 :");
		reaction_title.setText("當時反應 :");
		
		date_title.setTypeface(wordTypefaceBold);
		event_title.setTypeface(wordTypefaceBold);
		mood_title.setTypeface(wordTypefaceBold);
		thinking_title.setTypeface(wordTypefaceBold);
		reaction_title.setTypeface(wordTypefaceBold);
		
		speech_button = (ImageView) description_thinking_layout.findViewById(R.id.speech_to_text);
		thinking_text = (EditText) description_thinking_layout.findViewById(R.id.edit_thinking_content);
		
		thinking_text.setText("");
		
			
		speech_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (!NetworkCheck.networkCheck()) {
					MainActivity.networkState = false;
					speech_button.setImageResource(R.drawable.speech_icon_unable);
				}
				else
				{
					MainActivity.networkState = true;
					speech_button.setImageResource(R.drawable.speech_icon);
				}
				if(!MainActivity.networkState){
					CustomToastSmall.generateToast("請先開啟網路");
					return;
				}
				
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "zh-TW"); // en-US
				try {
					activity.startActivityForResult(intent, RESULT_SPEECH);
				 } catch (ActivityNotFoundException a) {
	                    Toast t = Toast.makeText(activity.getApplicationContext(),
	                            "Opps! Your device doesn't support Speech to Text",
	                            Toast.LENGTH_SHORT);
	                    t.show();	       
	             }
				
			}
								
		});
		
		/*center_layout = (LinearLayout) inflater.inflate(
				R.layout.note_main2, null);
		text_self = (TextView)center_layout.findViewById(R.id.text_self);
	    text_other = (TextView)center_layout.findViewById(R.id.text_other);
	    
		text_self.setTypeface(wordTypefaceBold);
	    text_other.setTypeface(wordTypefaceBold);
	    
		text_self.setTextColor(resource.getColor(R.color.blue));    
	    text_self.setOnClickListener(new MyClickListener(0));
	    text_other.setOnClickListener(new MyClickListener(1));*/
	    
		//initTypePager();
			
		//Type
		/*LinearLayout type_layout = (LinearLayout) inflater.inflate(
				R.layout.bar_type_name, null);
		
		TextView type_title = (TextView)type_layout.findViewById(R.id.type_title);
		type_title.setText("事件類型：");
		type_title.setTypeface(wordTypefaceBold);
		typetext = (TextView)type_layout.findViewById(R.id.type_content);
		typetext.setTypeface(wordTypefaceBold);*/

		
		//Spinner
		/*LinearLayout spinner_layout = (LinearLayout) inflater.inflate(
				R.layout.bar_spinner, null);
			
		//sp_item = (Spinner)spinner_layout.findViewById(R.id.spinner_content);
		//SetItem(sp_item, R.array.item_select);
		
		TextView spin_title = (TextView)spinner_layout.findViewById(R.id.spinner_title);
		spin_title.setText("發生事件：");
		spin_title.setTypeface(wordTypefaceBold);
		
		sp_content = (TextView)spinner_layout.findViewById(R.id.spinner_content);
		sp_content.setText("");
		sp_content.setTypeface(wordTypefaceBold);
		sp_content.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//listView.setVisibility(View.VISIBLE);
				if(items!= -1)
					listViewShowHide(listView);
			}
								
		});
		*/
		//listView = (ListView)spinner_layout.findViewById(R.id.item_listview);
		
		
		//Impact
		/*LinearLayout impact_layout = (LinearLayout) inflater.inflate(
				R.layout.bar_impact, null);
		impactSeekBar=(SeekBar)impact_layout.findViewById(R.id.impact_seek_bar);
		impactSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_IMPACT);
				done = true;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {				
			}
			
		});
		TextView impact_title = (TextView)impact_layout.findViewById(R.id.impact_title);
		impact_title.setTypeface(wordTypefaceBold);*/
		
		//Description
		/*LinearLayout discription_layout = (LinearLayout) inflater.inflate(
				R.layout.bar_description, null);
		
		TextView dec_title = (TextView)discription_layout.findViewById(R.id.description_title);
		dec_title.setText("當時我感覺：");
		dec_title.setTypeface(wordTypefaceBold);*/
		
		/*TODO*/
		/*edtext = (TextView)discription_layout.findViewById(R.id.description_content);
		edtext.setText("");
		edtext.setTypeface(wordTypefaceBold);
		edtext.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

	        	//Log.d("GG", "01");
				listViewShowHide(listView2);
				
			}
								
		});
		
		listView2 = (ListView)discription_layout.findViewById(R.id.item_listview2);
		
		SetListItemFeeling();
		
		for(int i = 0;i < 10; ++i)
    		moodFlag[i] = false;*/
		
		//Bottom View
		//View bottom = BarButtonGenerator.createTwoButtonView(R.string.cancel, R.string.ok, new CancelOnClickListener(), endOnClickListener);
		View bottom = BarButtonGenerator.createThreeButtonView(R.string.pre_step, R.string.start_reflection,R.string.done, 
				new CancelOnClickListener(), nextOnClickListener, endOnClickListener);
		
		//main_layout.addView(center_layout);
		//main_layout.addView(type_layout);
		//main_layout.addView(spinner_layout);
		//main_layout.addView(impact_layout);
		//main_layout.addView(discription_layout);
		main_layout.addView(description_date_layout);
		main_layout.addView(description_event_layout);
		main_layout.addView(description_reaction_layout);
		main_layout.addView(description_mood_layout);
		main_layout.addView(description_thinking_layout);
		
//		center_layout.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				listView.setVisibility(View.GONE);
//			}
//			
//		});
		
//		main_layout.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				listView.setVisibility(View.GONE);
//			}
//			
//		});
		
		//main_layout.addView(bottom);
		bottom_layout.addView(bottom);
		title_layout.bringToFront();
		//bottom_layout.setVisibility(View.GONE);
		bottom_layout.bringToFront();
		//main_layout.addView(bottom);
	}
	
	private void checkAndSetTimeSlot(){ 
		Calendar cal = Calendar.getInstance();
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int time_slot = TimeBlock.getTimeBlock(hours);
		timeslot = time_slot;
		timeslot_txt.setText(Timeslot_str[time_slot]);
	}
	
	public void setEnabledAll(View v, boolean enabled) {
	    v.setEnabled(enabled);
	    v.setFocusable(enabled);

	    if(v instanceof ViewGroup) {
	        ViewGroup vg = (ViewGroup) v;
	        for (int i = 0; i < vg.getChildCount(); i++)
	            setEnabledAll(vg.getChildAt(i), enabled);
	    }
	}
	
	private void listViewShowHide(ListView listview){
		if(listview == listView){
			if(!viewshow)
				listview.setVisibility(View.VISIBLE);
			else
				listview.setVisibility(View.GONE);
			viewshow = !viewshow;
		}
		if(listview == listView2){
			//Log.d("GG","02");
			if(!viewshow2)
				listview.setVisibility(View.VISIBLE);
			else
				listview.setVisibility(View.GONE);
			viewshow2 = !viewshow2;
		}
	}
	
	public void copingSetting(){
		//boxLayout = (RelativeLayout) inflater.inflate(R.layout.activity_qtip, null);
		//mainLayout.addView(boxLayout);
		state = STATE_COPE;
		PreferenceControl.setAfterTestState(STATE_COPE);
		
		title_layout.removeAllViews();
		main_layout.removeAllViews();
		bottom_layout.removeAllViews();
		bottom_layout.setVisibility(View.VISIBLE);
		
		//Title View
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.bar_addnote, null);
		
		note_title = (TextView) layout
				.findViewById(R.id.note_title);
		//Spinner sp_date = (Spinner)layout.findViewById(R.id.note_tx_date);
	    //Spinner sp_timeslot = (Spinner)layout.findViewById(R.id.note_sp_timeslot);
	    
	    note_title.setTypeface(wordTypefaceBold);
	    note_title.setTextColor(resource.getColor(R.color.text_gray2));
	    note_title.setText(R.string.countdown);
	    
	    sp_date.setVisibility(View.INVISIBLE);
	    sp_timeslot.setVisibility(View.INVISIBLE);
		title_layout.addView(layout);
		
		
		//View title = BarButtonGenerator.createWaitingTitle();
		
		
		center_layout = (LinearLayout) inflater.inflate(R.layout.knowledge, null);
		tv_knowdlege = (TextView)center_layout.findViewById(R.id.qtip_tv_tips);
		tv_title = (TextView)center_layout.findViewById(R.id.text_knowing_title);
		
		tv_title.setText(R.string.coping_page);
		
		Random rand = new Random();
		int idx = rand.nextInt(coping_msg.length);
		tv_knowdlege.setText(coping_msg[idx]);
		main_layout.addView(center_layout);
		
		View bottom = BarButtonGenerator.createOneButtonView( R.string.Iknow, endOnClickListener );
		bottom_layout.addView(bottom);
		
	}
	
	
	public void copingSettingToResult(){
		//boxLayout = (RelativeLayout) inflater.inflate(R.layout.activity_qtip, null);
		//mainLayout.addView(boxLayout);
		state = STATE_COPE;
		PreferenceControl.setAfterTestState(STATE_COPE);
		
		title_layout.removeAllViews();
		main_layout.removeAllViews();
		bottom_layout.removeAllViews();
		
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.bar_addnote, null);
		
		note_title = (TextView) layout
				.findViewById(R.id.note_title);
		//Spinner sp_date = (Spinner)layout.findViewById(R.id.note_tx_date);
	    //Spinner sp_timeslot = (Spinner)layout.findViewById(R.id.note_sp_timeslot);
	    
	    note_title.setTypeface(wordTypefaceBold);
	    note_title.setTextColor(resource.getColor(R.color.text_gray2));
	    note_title.setText(R.string.countdown);
	    
	    sp_date.setVisibility(View.INVISIBLE);
	    sp_timeslot.setVisibility(View.INVISIBLE);
		title_layout.addView(layout);
		
		//View title = BarButtonGenerator.createWaitingTitle();
		//title_layout.addView(title);
		
		center_layout = (LinearLayout) inflater.inflate(R.layout.knowledge, null);
		tv_knowdlege = (TextView)center_layout.findViewById(R.id.qtip_tv_tips);
		tv_title = (TextView)center_layout.findViewById(R.id.text_knowing_title);
		
		tv_title.setText(R.string.coping_page);
		
		Random rand = new Random();
		int idx = rand.nextInt(coping_msg.length);
		tv_knowdlege.setText(coping_msg[idx]);
		main_layout.addView(center_layout);
		
		View bottom = BarButtonGenerator.createOneButtonView( R.string.go_result, goResultOnClickListener );
		
		bottom_layout.addView(bottom);
		
	}
	
	
	
	public void setResult(){
		bottom_layout.removeAllViews();
		//Toast.makeText(context, "倒數結束", Toast.LENGTH_SHORT).show();
		
		if(state == STATE_NOTE){
			Toast.makeText(context, "請完成新增記事以查看檢測結果", Toast.LENGTH_SHORT).show();
			View bottom = BarButtonGenerator.createTwoButtonView(R.string.cancel, R.string.ok, goCopingToResultOnClickListener, goCopingToResultOnClickListener);
			bottom_layout.addView(bottom);
		}
		else if(state == STATE_COPE){
			Toast.makeText(context, "請點選以查看檢測結果", Toast.LENGTH_SHORT).show();
			note_title.setText(R.string.test_done);
			View bottom = BarButtonGenerator.createOneButtonView( R.string.go_result, goResultOnClickListener );
			bottom_layout.addView(bottom);
		}	
	}
	
	
	/** Initialize the dialog */
	public void initialize() {
		
		setting();
	    mainLayout.addView(boxLayout);
		
	}
	
	/** show the dialog */
	public void show() {
		state = STATE_NOTE;
		ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_ENTER);
		//PreferenceControl.setAfterTestState(STATE_NOTE);
		//questionLayout.setVisibility(View.VISIBLE);
		
		//MainActivity.getMainActivity().enableTabAndClick(false);
		boxLayout.setVisibility(View.VISIBLE);
		
		//chooseBox = new ChooseItemDialog(boxLayout, 1);
		//chooseBox.initialize();
		//chooseBox.show();

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
		ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_LEAVE);
		testQuestionCaller.resetView();
		MainActivity.getMainActivity().enableTabAndClick(true);
		if (boxLayout != null)
			boxLayout.setVisibility(View.INVISIBLE);
	}
	
	
	
	private void SetListItem2(int type){
		ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_SELECT_TYPE + type);
		//ArrayAdapter adapter = ArrayAdapter.createFromResource(context, array, android.R.layout.simple_list_item_1);
		items = -1;
		sp_content.setText(""); //TODO: 假如點到同一個不要清掉
		//ArrayAdapter adapter = ArrayAdapter.createFromResource(context, array, R.layout.my_listitem);
		String[] type1 = PreferenceControl.getType1();
		switch(type){
		case 1:
			type1 = PreferenceControl.getType1();
			break;
		case 2:
			type1 = PreferenceControl.getType2();
			break;
		case 3:
			type1 = PreferenceControl.getType3();
			break;
		case 4:
			type1 = PreferenceControl.getType4();
			break;
		case 5:
			type1 = PreferenceControl.getType5();
			break;
		case 6:
			type1 = PreferenceControl.getType6();
			break;
		case 7:
			type1 = PreferenceControl.getType7();
			break;
		case 8:
			type1 = PreferenceControl.getType8();
			break;
	
		}
				
		String[] after = clean(type1);
		ArrayAdapter adapter = new ArrayAdapter<String>(context, R.layout.my_listitem, after);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

		   @Override
		   public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			   ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_SELECT_ITEM);
			   TextView c = (TextView) view.findViewById(android.R.id.text1);
			    String playerChanged = c.getText().toString();
			    
			    items = noteCategory.myNewHashMap.get(playerChanged);
				Log.d(TAG, items+"");
			    //Toast.makeText(Settings.this,playerChanged, Toast.LENGTH_SHORT).show();  
			 sp_content.setText(playerChanged);
			 listView.setVisibility(View.GONE);
			 viewshow = false;
		   }
		   
		});
		setListViewHeightBasedOnItems(listView);
		listView.setVisibility(View.VISIBLE);
		viewshow = true;
		sv.smoothScrollTo(0 , (int)convertDpToPixel((float)200));
		
		//.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
	}
	 public static String[] clean(final String[] v) {
		    List<String> list = new ArrayList<String>(Arrays.asList(v));
		    list.removeAll(Collections.singleton(""));
		    return list.toArray(new String[list.size()]);
		}
	
	
	//把所選取的結果送出 
	class EndOnClickListener implements View.OnClickListener{
		public void onClick(View v){
			testQuestionCaller.writeQuestionFile(day, timeslot, type, items, impact, 
					actionNoteAdd, feelingNoteAdd,thinking_text.getText().toString(),1 ,0);
			close();
			clear();
			
			//Log.d("GG","01");
			/*reflectionPage1 = new ReflectionFirstPage(testQuestionCaller, mainLayout, activity);
			//Log.d("GG","02");
			reflectionPage1.initialize();
			//Log.d("GG","03");
			reflectionPage1.setAllText(date_text.getText().toString(),event_text.getText().toString(), 
								reaction_text.getText().toString(), thinking_text.getText().toString());
			//Log.d("GG","04");
			reflectionPage1.show();
			
			close();
			clear();*/
			/*if(!done){
				//Toast.makeText(context, "確定要送出結果嗎?" ,Toast.LENGTH_SHORT).show();
				CustomToastSmall.generateToast("確定要送出結果嗎?");
				done = true;
			}
			else{
				Log.d(TAG, items+" "+impact);
				if(state == STATE_NOTE){
					if(type <= 0 || items < 100){
						//CustomToastSmall.generateToast(R.string.note_check);
						//Toast.makeText(context, R.string.note_check ,Toast.LENGTH_SHORT).show();
						CustomToastSmall.generateToast(R.string.note_check);
					}
					else{
						if(listView.getVisibility() == View.VISIBLE){
							//Toast.makeText(context, "請選擇項目再送出", Toast.LENGTH_SHORT).show();
							CustomToastSmall.generateToast("請選擇項目再送出");
							listView.setVisibility(View.GONE);
						}
						else{
							ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_CONFIRM);
							
							impact = impactSeekBar.getProgress();
							testQuestionCaller.writeQuestionFile(day, timeslot, type, items, impact, edtext.getText().toString());
							close();
							clear();
							//copingSetting();
							//testQuestionCaller.resetView();
							
						}
					}
					
				}
				
				else if(state == STATE_COPE){
					//knowingSetting();
					//
					//testQuestionCaller.resetView();
					close();
					clear();
				}
			}*/
	    }
	}
	
	//把所選取的結果取消
	class CancelOnClickListener implements View.OnClickListener{
		public void onClick(View v){
			
			ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_CANCEL);
			
			//testQuestionCaller.writeQuestionFile(day, timeslot, -1, -1, -1, edtext.getText().toString());
			close();
			clear();
			//testQuestionCaller.resetView();
			
				//copingSetting();

		}
	}
	
	//到下一步
		class NextOnClickListener implements View.OnClickListener{
			public void onClick(View v){
				int nowKey = testQuestionCaller.writeQuestionFile(day, timeslot, type, items, impact, 
						actionNoteAdd, feelingNoteAdd,thinking_text.getText().toString(),1 ,0);
				
				reflectionPage1 = new ReflectionFirstPage(testQuestionCaller, mainLayout, activity, nowKey);
				reflectionPage1.initialize();
				reflectionPage1.setAllText(date_text.getText().toString(),event_text.getText().toString(), 
									reaction_text.getText().toString(), thinking_text.getText().toString());
				reflectionPage1.show();
				
				close();
				clear();
							
				
		    }
		}
	
	class GoResultOnClickListener implements View.OnClickListener{
		public void onClick(View v){
			CustomToast.generateToast(R.string.after_test_pass, 2);
			MainActivity.getMainActivity().changeTab(1);
			
	    }
	}
	
	class GoCopingToResultOnClickListener implements View.OnClickListener{
		public void onClick(View v){
			
			
			if(state == STATE_NOTE){
				impact = impactSeekBar.getProgress();
				//testQuestionCaller.writeQuestionFile(day, timeslot, type, items, impact, edtext.getText().toString());
			
				Log.d(TAG, items+" "+impact);

				copingSettingToResult();
			}

	    }
	}
	
	private void initTypePager(){
	    vPager = (ViewPager) center_layout.findViewById(R.id.viewpager);
	    vPager.setOnPageChangeListener(new MyOnPageChangeListener());
	    
		//LayoutInflater li = LayoutInflater.from(context); //getLayoutInflater();
		ArrayList<View> aList = new ArrayList<View>();
		aList.add(inflater.inflate(R.layout.typepager_self, null));
		aList.add(inflater.inflate(R.layout.typepager_other, null));
		TypePageAdapter mAdapter = new TypePageAdapter(aList);		
		vPager.setAdapter(mAdapter);
	}
	
	
	public class TypePageAdapter extends PagerAdapter{
		
		private ArrayList<View> viewLists;	

		public TypePageAdapter() {}	
		public TypePageAdapter(ArrayList<View> viewLists)
		{
			super();
			this.viewLists = viewLists;
		}
		
		@Override
		public int getCount() {
			return viewLists.size();
		}
		
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		private void resetView(){
			iv_smile.setImageResource(R.drawable.type_icon3);
			iv_not_good.setImageResource(R.drawable.type_icon2);
			iv_urge.setImageResource(R.drawable.type_icon5);
			iv_cry.setImageResource(R.drawable.type_icon1);
			iv_try.setImageResource(R.drawable.type_icon4);
			
			iv_social.setImageResource(R.drawable.type_icon7);
			iv_playing.setImageResource(R.drawable.type_icon8);
			iv_conflict.setImageResource(R.drawable.type_icon6);
		}
		
		/** 初始化Type*/
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewLists.get(position));
			if(position == 0){
				iv_smile = (ImageView) center_layout.findViewById(R.id.vts_iv_smile);
				iv_not_good = (ImageView) center_layout.findViewById(R.id.vts_iv_not_good);
				iv_urge = (ImageView) center_layout.findViewById(R.id.vts_iv_urge);
				iv_cry = (ImageView) center_layout.findViewById(R.id.vts_iv_cry);
				iv_try = (ImageView) center_layout.findViewById(R.id.vts_iv_try);
				iv_self_others_bar = (ImageView) center_layout.findViewById(R.id.self_others_bar);
				iv_smile.setOnClickListener(SelectItem);
				iv_not_good.setOnClickListener(SelectItem);
				iv_urge.setOnClickListener(SelectItem);
				iv_cry.setOnClickListener(SelectItem);
				iv_try.setOnClickListener(SelectItem);
			}else{
				iv_social = (ImageView) center_layout.findViewById(R.id.vts_iv_social);
				iv_playing = (ImageView) center_layout.findViewById(R.id.vts_iv_playing);
				iv_conflict = (ImageView) center_layout.findViewById(R.id.vts_iv_conflict);
				iv_self_others_bar = (ImageView) center_layout.findViewById(R.id.self_others_bar);
				iv_social.setOnClickListener(SelectItem);
				iv_playing.setOnClickListener(SelectItem);
				iv_conflict.setOnClickListener(SelectItem);
			}
			//Log.d("FORTEST", "aabb");
			
			return viewLists.get(position);	
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewLists.get(position));
		}
		
		View.OnClickListener SelectItem = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		        switch(v.getId()){
		        
		        case R.id.self_bottom_layout:
		        	listView.setVisibility(View.GONE);
		        	break;
		        	
		        case R.id.other_bottom_layout:
		        	listView.setVisibility(View.GONE);
		        	break;
		        
		        case R.id.vts_iv_cry:
		        	resetView();
		        	iv_cry.setImageResource(R.drawable.type_icon1_pressed);
		        	typetext.setText(R.string.note_negative);
		        	
		        	//SetListItem(R.array.note_negative);
		        	SetListItem2(1);
		        	//listViewShowHide();
	        		//SetItem(sp_item,R.array.note_negative);
	        		//spinner_content.performClick();
	        		type = 1;
	        		break;
		        case R.id.vts_iv_not_good:
		        	resetView();
		        	iv_not_good.setImageResource(R.drawable.type_icon2_pressed);
		        	typetext.setText(R.string.note_notgood);
		        	
		        	//SetListItem(R.array.note_notgood);
		        	SetListItem2(2);
		        	//SetItem(sp_item,R.array.note_notgood);
		        	//spinner_content.performClick();
		        	type = 2;
			        break;
		        case R.id.vts_iv_smile:
		        	resetView();
		        	iv_smile.setImageResource(R.drawable.type_icon3_pressed);
		        	typetext.setText(R.string.note_positive);
		        	
		        	//SetListItem(R.array.note_positive);
		        	SetListItem2(3);
		        	//SetItem(sp_item, R.array.note_positive);
		        	//spinner_content.performClick();
		        	type = 3;
		        	break;
		        case R.id.vts_iv_try:
		        	resetView();
		        	iv_try.setImageResource(R.drawable.type_icon4_pressed);
		        	typetext.setText(R.string.note_selftest);
		        	
		        	//SetListItem(R.array.note_selftest);
		        	SetListItem2(4);
		        	//SetItem(sp_item,R.array.note_selftest);
		        	//sp_item.performClick();
		        	type = 4; 
		        	break;
		        case R.id.vts_iv_urge:
		        	resetView();
		        	iv_urge.setImageResource(R.drawable.type_icon5_pressed);
		        	typetext.setText(R.string.note_temptation);
		        	
		        	//SetListItem(R.array.note_temptation);
		        	SetListItem2(5);
		        	//SetItem(sp_item,R.array.note_temptation);
		        	//sp_item.performClick();
		        	type = 5;
		        	break;
		        case R.id.vts_iv_playing:
		        	resetView();
		        	iv_playing.setImageResource(R.drawable.type_icon8_pressed);
		        	typetext.setText(R.string.note_play);
		        	
		        	//SetListItem(R.array.note_play);
		        	SetListItem2(8);
		        	//SetItem(sp_item,R.array.note_play);
		        	//sp_item.performClick();
		        	type = 8;
		        	break;
		        case R.id.vts_iv_social:
		        	resetView();
		        	iv_social.setImageResource(R.drawable.type_icon7_pressed);
		        	typetext.setText(R.string.note_social);
		        	
		        	//SetListItem(R.array.note_social);
		        	SetListItem2(7);
		        	//SetItem(sp_item,R.array.note_social);
		        	//sp_item.performClick();
		        	type = 7;
		        	break;
		        case R.id.vts_iv_conflict:
		        	resetView();
		        	iv_conflict.setImageResource(R.drawable.type_icon6_pressed);
		        	typetext.setText(R.string.note_conflict);
		        	
		        	//SetListItem(R.array.note_conflict);
		        	SetListItem2(6);
		        	//SetItem(sp_item,R.array.note_conflict);
		        	//sp_item.performClick();
		        	type = 6;
		        	break;
		        	
		        }
			}
		};
	}
	public class MyClickListener implements OnClickListener
	{
		private int index = 0;
		public MyClickListener(int i){
			index = i;
		}
		
		@Override
		public void onClick(View arg0) {
			vPager.setCurrentItem(index);
			switch(index){
			case 0:
				ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_CLICK_SELF);
				text_self.setTextColor(resource.getColor(R.color.blue));
				text_other.setTextColor(resource.getColor(R.color.text_gray3));
				iv_self_others_bar.setImageResource(R.drawable.note_slide_line1);
				break;
			case 1:
				ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_CLICK_OTHER);
				text_self.setTextColor(resource.getColor(R.color.text_gray3));
				text_other.setTextColor(resource.getColor(R.color.blue));
				iv_self_others_bar.setImageResource(R.drawable.note_slide_line2);
				break;

			}
		}
		
	}
	
	
	//監聽頁面切換時間,主要做的是動畫處理,就是移動條的移動
		public class MyOnPageChangeListener implements OnPageChangeListener {


			@Override
			public void onPageSelected(int index) {

				switch (index) {
				case 0:
					ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_SCROLL_SELF);
					text_self.setTextColor(resource.getColor(R.color.blue));
					text_other.setTextColor(resource.getColor(R.color.text_gray3));
					iv_self_others_bar.setImageResource(R.drawable.note_slide_line1);
					break;
				case 1:
					ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_SCROLL_OTHER);
					text_self.setTextColor(resource.getColor(R.color.text_gray3));
					text_other.setTextColor(resource.getColor(R.color.blue));
					iv_self_others_bar.setImageResource(R.drawable.note_slide_line2);
					break;
				}

			}
			@Override
			public void onPageScrollStateChanged(int arg0) {}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
		
		}
		
		public float getDensity(){
			 DisplayMetrics metrics = resource.getDisplayMetrics();
			 return metrics.density;
			}
		
		public float convertDpToPixel(float dp){
		    float px = dp * getDensity();
		    return px;
		}
		/**
		 * Sets ListView height dynamically based on the height of the items.   
		 *
		 * @param listView to be resized
		 * @return true if the listView is successfully resized, false otherwise
		 */
		public boolean setListViewHeightBasedOnItems(ListView listview) {

		    ListAdapter listAdapter = listview.getAdapter();
		    if (listAdapter != null) {
		    	
		        int numberOfItems = listAdapter.getCount();
		        
		        //listview2 : mood
		        if(listview == listView2)
		        {
		        	moodNum = numberOfItems;
		   
		        }
		        
		        // Get total height of all items.
		        int totalItemsHeight = 0;
		        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
		            View item = listAdapter.getView(itemPos, null, listview);
		            
		            item.measure(0, 0);
		            
		            totalItemsHeight += item.getMeasuredHeight();
		        }
		        
		        // Get total height of all item dividers.
		        int totalDividersHeight = listview.getDividerHeight() * 
		                (numberOfItems - 1);
		        
		        // Set list height.
		        ViewGroup.LayoutParams params = listview.getLayoutParams();
		        //params.height = totalItemsHeight + totalDividersHeight;
		        params.height = (int) (convertDpToPixel((float)40)* numberOfItems) + totalDividersHeight;
		        
		        listview.setLayoutParams(params);
		        listview.requestLayout();
		        	
		        //Log.d("GG", "Iheight : "+totalItemsHeight);
		        //Log.d("GG", "Dheight : "+totalDividersHeight);
		        
		        return true;

		    } else {
		        return false;
		    }

		}

		@Override
		public void resetView(int type, int select) {
			setEnabledAll(boxLayout, true);
			edtext.setEnabled(true);
			edtext.setInputType(InputType.TYPE_CLASS_TEXT);
			edtext.setFocusable(true);
			edtext.setFocusableInTouchMode(true);
			if(select == -1) //什麼都沒選
				return;
			
			if(type == 1){
				day = select;
				date_txt.setText(Date_str[select]);
				
				if(day == 0)//在別天選晚上時段, 回到今天還是要擋掉未來時段
					checkAndSetTimeSlot();
			}
			else{
				timeslot = select;
				timeslot_txt.setText(Timeslot_str[select]);
			}
			
			
		}
		
		/* feeling : listview item*/
		private void SetListItemFeeling(){
			//listView2.setVisibility(View.GONE);
			//viewshow2 = false;
			
			String[] type1 = PreferenceControl.getTypeMood();
			String[] after = clean(type1);
			
			ArrayAdapter adapter = new ArrayAdapter<String>(context, R.layout.my_listitem, after);
			listView2.setAdapter(adapter);
			listView2.setOnItemClickListener(new OnItemClickListener(){

			   @Override
			   public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				   //ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_SELECT_ITEM);	
				   
				   TextView c = (TextView) view.findViewById(android.R.id.text2);
				    String playerChanged = c.getText().toString();
				    String showText = "";
				    int nowMood = noteCategory.myNewHashMap.get(playerChanged);
				    
				    int tm = 0;
					for(int i = 0; i < moodNum; i++)
					{
					    if(!moodFlag[i])
					    	continue;
					   	tm++;
					}
					if(tm >= 3 && !moodFlag[nowMood - 900])
					   return;
				    
				   	
				   	moodFlag[nowMood - 900] = !moodFlag[nowMood - 900];
				   	

				   	
				    //Toast.makeText(Settings.this,playerChanged, Toast.LENGTH_SHORT).show();  
				    for(int i = 0; i < moodNum; i++)
				    {
				    	if(!moodFlag[i])
				    		continue;
				    	String ts =  noteCategory.dictionary.get(i+900);
				    	if(showText != "")
				    		showText += ", ";
				    	showText += ts;
				    	
				    		
				    }
				   	
					edtext.setText(showText);
				    //listView2.setVisibility(View.GONE);
				    //viewshow2 = false;
			   }
			   
			});
			
			setListViewHeightBasedOnItems(listView2);
			listView2.setVisibility(View.GONE);
			viewshow2 = false;
			sv.smoothScrollTo(0 , (int)convertDpToPixel((float)200));
		}
		
	public void setAllText(String s_date, String s_event, String s_mood, String s_reaction)
	{
		TextView _date_text = (TextView)description_date_layout.findViewById(R.id.description_date_content);
		
		TextView _event_text = (TextView)description_event_layout.findViewById(R.id.description_event_content);
		
		TextView _mood_text = (TextView)description_mood_layout.findViewById(R.id.description_mood_content);
		
		TextView _reaction_text = (TextView)description_reaction_layout.findViewById(R.id.description_reaction_content);
		
		_date_text.setText(s_date);
		_event_text.setText(s_event);
		_mood_text.setText(s_mood);
		_reaction_text.setText(s_reaction);
	}
	
	public void setAddNoteDetail(int _day, int _slot, int _type, int _item, int _impact, String _action, String _feeling)
	{
		day = _day;
		timeslot = _slot;
		type = _type;
		impact = _impact;
		items = _item;
		
		actionNoteAdd = _action;
		feelingNoteAdd = _feeling;
	}
}
