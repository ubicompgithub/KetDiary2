package com.ubicomp.ketdiary.system;

import java.util.Calendar;

import android.app.AlarmManager;
import android.content.SharedPreferences;
import android.util.Log;

import com.ubicomp.ketdiary.App;
import com.ubicomp.ketdiary.MainActivity;
import com.ubicomp.ketdiary2.R;
import com.ubicomp.ketdiary.SelectActivity;
import com.ubicomp.ketdiary.data.db.DatabaseControl;
import com.ubicomp.ketdiary.data.structure.ExchangeHistory;
import com.ubicomp.ketdiary.data.structure.TimeValue;
import com.ubicomp.ketdiary.noUse.NoteCatagory3;

/**
 * Class for controlling Android Preference
 * 
 * @author Stanley Wang
 */
public class PreferenceControl {

	private static final SharedPreferences sp = App.getSp();
	private static final String TAG = "Preference";
	/* UID
	 * DeviceId
	 * sensorId
	 * sponsor_name
	 * sponsor_phone
	 * TestResult
	 * LatestDetectionTimstamp
	 * LatestDoneDetectionTimstamp
	 * latestTestFail
	 * TestSuccess
	 * LatestTestTime
	 * debug
	 * debugType
	 * firstTime
	 * showedCoupon
	 * targetGood
	 * targetMoney
	 * perDrinkCost
	 * startdate
	 * systemLock
	 * appealOpen
	 * appealAble
	 * appealFail
	 * appealPos
	 * appealLastFail
	 * appealLastPos
	 * appealStartTime
	 */
	
	/** Default setting at the first time of launching SoberDiary */
	public static void defaultSetting() {
		setUID("rehab_default_test");
		setDeviceId("ket_000");
		setIsDeveloper(false);
		Calendar cal = Calendar.getInstance();		
		setFirstUsedDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		
		cal.add(Calendar.DATE, 1);
		setStartDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		
		setOpenAppTimestamp();
		
		setAppealOpen(false);
		setAppealAble(0);
		setAppealFail(false);
		setAppealPos(false);
		setAppealLastFail(0);
		setAppealLastPos(0);
		setAppealStartTime(0);
	}

	/**
	 * get UID
	 * 
	 * @return uid (default: sober_default_test)
	 */
	public static String getUID() {
		return sp.getString("uid", "rehab_default_test");
	}

	/**
	 * set UID
	 * 
	 * @param uid
	 *            new UID
	 */
	public static void setUID(String uid) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("uid", uid);
		edit.commit();
	}

	/**
	 * Check if the UID is default UID
	 * 
	 * @return true of the UID is default UID
	 */
	public static boolean defaultCheck() {
		return getUID().equals("rehab_default_test");
	}
     
	
	/**
	 * get DeviceId
	 * 
	 * @return DeviceId (default: device_default)
	 */
	public static String getDeviceId() {
		return sp.getString("device_id", "device_default");
	}

	/**
	 * set DeviceId
	 * 
	 * @param device_id
	 *            new DeviceId
	 */
	public static void setDeviceId(String device_id) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("device_id", device_id);
		edit.commit();
	}

	public static boolean isDeveloper() {
		return sp.getBoolean("developer", false);
	}

	public static void setIsDeveloper(boolean developer) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("developer", developer);
		edit.commit();
	}
	
	public static boolean isSkip() {
		return sp.getBoolean("skip", false);
	}

	public static void setIsSkip(boolean skip) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("skip", skip);
		edit.commit();
	}
	
	public static boolean isDemo() {
		return sp.getBoolean("demo", false);
	}

	public static void setIsDemo(boolean demo) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("demo", demo);
		edit.commit();
	}
	
	public static boolean getInTest() {
		return sp.getBoolean("intest", false);
	}

	public static void setInTest(boolean intest) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("intest", intest);
		edit.commit();
	}
	
	public static boolean getCollectData() {
		return sp.getBoolean("collectdata", false);
	}

	public static void setCollectData(boolean collectdata) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("collectdata", collectdata);
		edit.commit();
	}
	
	/**
	 * set Test state
	 * 
	 */
	public static void setAfterTestState(int state) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("after_test_state", state);
		edit.commit();
	}
	
	public static int getAfterTestState() {
		return sp.getInt("after_test_state", 0);
	}
	
	public static void setLatestTestCompleteTime(long timestamp) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("testCompleteTime", timestamp);
		edit.commit();
	}

	public static long getLatestTestCompleteTime() {
		return sp.getLong("testCompleteTime", 0);
	}

	public static boolean questionnaireShowUpdateDetection() {
		long curTs = System.currentTimeMillis();
		long prevTs = sp.getLong("testCompleteTime", 0);
		if (curTs - prevTs < 3 * 60 * 1000) {
			long addTs = prevTs + 5 * 60 * 1000;
			TimeValue prevTv = TimeValue.generate(prevTs);
			TimeValue addTv = TimeValue.generate(addTs);
			return prevTv.getTimeslot() == addTv.getTimeslot();
		}
		return false;
	}

	public static boolean getCheckResult() {
		return sp.getBoolean("checkResult", false);
	}

	public static void setCheckResult(boolean checkResult) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("checkResult", checkResult);
		edit.commit();
	}
	
	public static boolean getResultServiceRun() {
		return sp.getBoolean("resultServiceRun", false);
	}

	public static void setResultServiceRun(boolean run) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("resultServiceRun", run);
		edit.commit();
	}
	
	public static boolean getCheckBars() {
		return sp.getBoolean("checkBars", false);
	}

	public static void setCheckBars(boolean checkBars) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("checkBars", checkBars);
		edit.commit();
	}
	
	
	public static boolean getInApp() {
		return sp.getBoolean("inApp", true);
	}

	public static void setInApp(boolean inApp) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("inApp", inApp);
		edit.commit();
	}
	
	public static int getPoint() {
		DatabaseControl db = new DatabaseControl();
		//int resultScore = db.getLatestTestResult().getScore();
		//int resultScore = db.getLatestTestResultID().getScore();
		//int noteScore = db.getLatestNoteAdd().getScore();
		//int questionScore = db.getLatestQuestionTest().getScore();
		//int copingScore = db.getLatestCopingSkill().getScore();
		
		//Log.i(TAG, "result: "+resultScore + "note: "+noteScore + "question: "+questionScore +"coping: " +copingScore);

		int total_point = db.getLastestAddScore().getAccumulation();//= resultScore + noteScore + questionScore + copingScore;
		return total_point;	
		//return sp.getInt("Point", 0);
	}
	
	public static void setPoint(int addPoint) {
		int last_point = getPoint();
		SharedPreferences.Editor edit = sp.edit();
		int point = last_point + addPoint;
		edit.putInt("Point", point);
//		if(point % Config.COUPON_CREDITS == 0)
//			setCouponChange(true);
		
		Log.d(TAG, "point: " + point);
		edit.commit();
	}
	
	public static int getPosition() {
		return sp.getInt("Postion", 50);
	}
	
	public static void setPosition(int addPos) {
		int last_position= getPosition();
		int pos = last_position + addPos;
		if(pos<0){
			pos = 0;
		}
		else if(pos > 100){
			pos = 100;
		}
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("Postion", pos);
		edit.commit();
	}
	
	public static void setAbsPosition(int Pos) {
		Pos = Pos + 10;
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("Postion", Pos);
		edit.commit();
	}
	
	public static int getLastPosition() {
		return sp.getInt("LastPostion", 10);
	}
	
	public static void setLastPosition(int Pos) {
//		int last_position= getLastPosition();
		SharedPreferences.Editor edit = sp.edit();
		if(Pos<0){
			Pos = 0;
		}
		else if(Pos > 20){
			Pos = 20;
		}
		
		edit.putInt("LastPostion", Pos);
		edit.commit();
	}
	
	public static int getTestAddScore() {
		return sp.getInt("TestAddScore", 0);
	}
	
	public static void setTestAddScore(int addScore) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("TestAddScore", addScore);
		edit.commit();
	}
	
	
	public static int getIsFilled() {
		return sp.getInt("IsFilled", 0);
	}
	
	public static void setIsFilled(int isFilled) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("IsFilled", isFilled);
		edit.commit();
	}
	
	
	public static int getPowerNotEnough() {
		return sp.getInt("PowerNotEnough", 0);
	}
	
	public static void setPowerNotEnough(int power_notenough) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("PowerNotEnough", power_notenough);
		edit.commit();
	}
	
	public static int getVoltag1() {
		return sp.getInt("Voltage1", 200);
	}
	
	public static void setVoltage1(int voltage1) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("Voltage1", voltage1);
		edit.commit();
	}
	
	public static int getVoltag2() {
		return sp.getInt("Voltage2", 240);
	}
	
	public static void setVoltage2(int voltage2) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("Voltage2", voltage2);
		edit.commit();
	}
	
	public static int getAfterCountDown() {
		return sp.getInt("AfterCountDown", 720);
	}
	
	public static void setAfterCountDown(int afterCountDown) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("AfterCountDown", afterCountDown);
		edit.commit();
	}
	
	public static int getVoltageCountDown() {
		return sp.getInt("VoltageCountDown", 80);
	}
	
	public static void setVoltageCountDown(int voltageCountDown) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("VoltageCountDown", voltageCountDown);
		edit.commit();
	}
	
	public static int getVoltage2CountDown() {
		return sp.getInt("Voltage2CountDown", 120);
	}
	
	public static void setVoltage2CountDown(int voltageCountDown) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("Voltage2CountDown", voltageCountDown);
		edit.commit();
	}
	
	// public static int getSustainMonth(){
	// 	int sustainMonth = 0;
	// 	Calendar cal = Calendar.getInstance();
	// 	int data = sp.getInt("sMonth", cal.get(Calendar.MONTH));
	// 	sustainMonth = cal.get(Calendar.MONTH) - data + 1;
	// 	return sustainMonth;
	// }
	
	// Blue Zhong
	public static int getSustainMonth(){
		int sustainMonth = 0;
		Calendar cal = Calendar.getInstance();
		int sYear = sp.getInt("sYear", cal.get(Calendar.YEAR));
		int sMonth = sp.getInt("sMonth", cal.get(Calendar.MONTH));
		sustainMonth = (cal.get(Calendar.YEAR) - sYear) * 12 + cal.get(Calendar.MONTH) - sMonth + 1;
		return sustainMonth;
	}

	public static Calendar getStartDate() {
		int[] data = new int[3];
		Calendar cal = Calendar.getInstance();
		data[0] = sp.getInt("sYear", cal.get(Calendar.YEAR));
		data[1] = sp.getInt("sMonth", cal.get(Calendar.MONTH));
		data[2] = sp.getInt("sDay", cal.get(Calendar.DATE));

		cal.set(data[0], data[1], data[2], 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static int[] getStartDateData() {
		int[] data = new int[3];
		Calendar cal = Calendar.getInstance();
		data[0] = sp.getInt("sYear", cal.get(Calendar.YEAR));
		data[1] = sp.getInt("sMonth", cal.get(Calendar.MONTH));
		data[2] = sp.getInt("sDay", cal.get(Calendar.DATE));
		return data;
	}

	public static void setStartDate(int year, int month, int day) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("sYear", year);
		edit.putInt("sMonth", month);
		edit.putInt("sDay", day);
		edit.commit();
	}
	
	public static Calendar getStartDateMinus() {
		int[] data = new int[3];
		Calendar cal = Calendar.getInstance();
		data[0] = sp.getInt("sYear", cal.get(Calendar.YEAR));
		data[1] = sp.getInt("sMonth", cal.get(Calendar.MONTH));
		data[2] = sp.getInt("sDay", cal.get(Calendar.DATE));

		cal.set(data[0], data[1], data[2], 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, -2);
		return cal;
	}
	
	public static Calendar getFirstUsedDate() {
		int[] data = new int[3];
		Calendar cal = Calendar.getInstance();
		data[0] = sp.getInt("fYear", cal.get(Calendar.YEAR));
		data[1] = sp.getInt("fMonth", cal.get(Calendar.MONTH));
		data[2] = sp.getInt("fDay", cal.get(Calendar.DATE));

		cal.set(data[0], data[1], data[2], 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		Calendar startCal = getStartDate();
		
		if(cal.compareTo(startCal) == 1 )
			cal = startCal;
		
		return cal;
	}
	
	public static Calendar getFirstUsedDateMinus() {
		int[] data = new int[3];
		Calendar cal = Calendar.getInstance();
		data[0] = sp.getInt("fYear", cal.get(Calendar.YEAR));
		data[1] = sp.getInt("fMonth", cal.get(Calendar.MONTH));
		data[2] = sp.getInt("fDay", cal.get(Calendar.DATE));

		cal.set(data[0], data[1], data[2], 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, -2);
		
		Calendar startCal = getStartDateMinus();
		
		if(cal.compareTo(startCal) == 1 )
			cal = startCal;
		
		return cal;
	}
	
	public static int[] getFirstUsedDateData() {
		int[] data = new int[3];
		Calendar cal = Calendar.getInstance();
		data[0] = sp.getInt("fYear", cal.get(Calendar.YEAR));
		data[1] = sp.getInt("fMonth", cal.get(Calendar.MONTH));
		data[2] = sp.getInt("fDay", cal.get(Calendar.DATE));
		return data;
	}
	

	public static void setFirstUsedDate(int year, int month, int day) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("fYear", year);
		edit.putInt("fMonth", month);
		edit.putInt("fDay", day);
		edit.commit();
	}
	
	public static void setGoal2(String positive, String negative) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("targetGood", positive);
		edit.putString("targetBad", negative);
		edit.commit();
	}

	public static String getPostiveGoal() {
		return sp.getString("targetGood", "家人認同");	
	}
	
	public static String getNegativeGoal() {
		return sp.getString("targetBad", "尿失禁");	
	}
	
	public static boolean checkCouponChange() {
		int prevCoupon = PreferenceControl.lastShowedCoupon();
		int curCoupon = getCoupon();
		return curCoupon != prevCoupon;
	}
	
	public static boolean getCouponChange() {
		return sp.getBoolean("couponChange", false);
	}

	public static void setCouponChange(boolean change) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("couponChange", change);
		edit.commit();
		if (MainActivity.getMainActivity() != null)
			MainActivity.getMainActivity().setCouponChange(change);
	}
	
	public static boolean getRandomQustion() {
		return sp.getBoolean("randomQuestion", false);
	}

	public static void setRandomQustion(boolean change) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("randomQuestion", change);
		edit.commit();
	}
	
	public static long getRandomTs() {
		return sp.getLong("randomTs", 0);
	}
	
	public static boolean getRandomDiff(Long newTs){
		return (newTs - getRandomTs()) > 30*60*1000;
	}
	
	public static void setRandomTs(Long ts) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("randomTs", ts);
		edit.commit();
	}
	
	public static String[] getRecreations() {
		String[] recreation = new String[5];
		recreation[0] = sp.getString("recreation0",
				App.getContext().getString(R.string.default_recreation_1));
		recreation[1] = sp.getString("recreation1",
				App.getContext().getString(R.string.default_recreation_2));
		recreation[2] = sp.getString("recreation2",
				App.getContext().getString(R.string.default_recreation_3));
		recreation[3] = sp.getString("recreation3", "");
		recreation[4] = sp.getString("recreation4", "");
		return recreation;
	}

	public static void setRecreation(String recreation, int id) {
		String key = "recreation" + id;
		SharedPreferences.Editor edit = sp.edit();
		edit.putString(key, recreation);
		edit.commit();

	}
	
	public static void setTestResult(int result) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("testResult", result);
		edit.commit();
	}

	public static int getTestResult() {
		return sp.getInt("testResult", -1);
	}
	
	public static int getCoupon(){
		int point = getPoint();
		int usedScore = getUsedCounter();
		int coupon = (point-usedScore)/Config.COUPON_CREDITS;
		return coupon;
	}
	
	public static void exchangeCoupon() {
		int currentCounter = getPoint();
		int usedCounter = sp.getInt("usedCounter", 0);
		int coupon = getCoupon();
		int exchangeCounter = coupon * Config.COUPON_CREDITS;
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("usedCounter", usedCounter + exchangeCounter);
		edit.commit();
		DatabaseControl db = new DatabaseControl();
		db.insertExchangeHistory(new ExchangeHistory(
				System.currentTimeMillis(), exchangeCounter));
	}

//	public static void cleanCoupon() {
//		int currentCounter = getTotalCounter();
//		int usedCounter = sp.getInt("usedCounter", 0);
//		SharedPreferences.Editor edit = sp.edit();
//		edit.putInt("usedCounter", usedCounter + currentCounter);
//		edit.commit();
//	}

	public static int getUsedCounter() {
		return sp.getInt("usedCounter", 0);
	}

	public static void setUsedCounter(int counter) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("usedCounter", counter);
		edit.commit();
	}

	
	
	// haven't use
	/**
	 * Check if it is the first time launching RehabDiary
	 * 
	 * @return true if UID is ""
	 */
	public static boolean checkFirstUID() {
		return sp.getString("uid", "").equals("");
	}

	/**
	 * Get sensorId
	 * 
	 * @return sensor Id. If there are no sensor used before, return "unknown"
	 */
	public static String getSensorID() {
		return sp.getString("sensor_id", "unknown");
	}

	/**
	 * Set sensor id
	 * 
	 * @param sensorID
	 *            sensor ID
	 */
	public static void setSensorID(String sensorID) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("sensor_id", sensorID);
		edit.commit();
	}

	/** Get the names of the */
	public static String[] getSponsorName() {
		String[] names = new String[3];
		names[0] = sp.getString("sponsor_name0", "");
		names[1] = sp.getString("sponsor_name1", "");
		names[2] = sp.getString("sponsor_name2", "");
		return names;
	}

	public static String[] getSponsorPhone() {
		String[] calls = new String[3];
		calls[0] = sp.getString("sponsor_phone0", "");
		calls[1] = sp.getString("sponsor_phone1", "");
		calls[2] = sp.getString("sponsor_phone2", "");
		return calls;
	}

	public static void setSponsorCallData(String name, String phone, int id) {
		String keyName = "sponsor_name" + id;
		String keyPhone = "sponsor_phone" + id;
		SharedPreferences.Editor edit = sp.edit();
		edit.putString(keyName, name);
		edit.putString(keyPhone, phone);
		edit.commit();
	}

	public static int[] getConnectSocialHelpIdx() {
		int[] calls = new int[3];
		calls[0] = sp.getInt("social_help0", 0);
		calls[1] = sp.getInt("social_help1", 1);
		calls[2] = sp.getInt("social_help2", 2);
		return calls;
	}

	public static void setConnectSocialHelpIdx(int[] idx) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("social_help0", idx[0]);
		edit.putInt("social_help1", idx[1]);
		edit.putInt("social_help2", idx[2]);
		edit.commit();
	}
	
	private static int noteNum = SelectActivity.NOTE_UPPER_BOUND;
	private static NoteCatagory3 noteCateogry = new NoteCatagory3();
	/** Get the names of the */
	public static String[] getType1() {
		String[] names = new String[noteNum];
		names[0] = sp.getString("type1_name0", noteCateogry.negative.get(100));
		names[1] = sp.getString("type1_name1", noteCateogry.negative.get(101));
		names[2] = sp.getString("type1_name2", noteCateogry.negative.get(102));
		names[3] = sp.getString("type1_name3", noteCateogry.negative.get(103));
		names[4] = sp.getString("type1_name4", noteCateogry.negative.get(104));
		names[5] = sp.getString("type1_name5", noteCateogry.negative.get(105));
		names[6] = sp.getString("type1_name6", noteCateogry.negative.get(106));
		names[7] = sp.getString("type1_name7", noteCateogry.negative.get(107));
		names[8] = sp.getString("type1_name8", noteCateogry.negative.get(108));
		names[9] = sp.getString("type1_name9", noteCateogry.negative.get(109));
		return names;
	}

	public static void setType1(String[] type1) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("type1_name0", type1[0]);
		edit.putString("type1_name1", type1[1]);
		edit.putString("type1_name2", type1[2]);
		edit.putString("type1_name3", type1[3]);
		edit.putString("type1_name4", type1[4]);
		edit.putString("type1_name5", type1[5]);
		edit.putString("type1_name6", type1[6]);
		edit.putString("type1_name7", type1[7]);
		edit.putString("type1_name8", type1[8]);
		edit.putString("type1_name9", type1[9]);
		edit.commit();
	}
	
	/** Get the names of the */
	public static String[] getType2() {
		String[] names = new String[noteNum];
		names[0] = sp.getString("type2_name0", noteCateogry.notgood.get(200));
		names[1] = sp.getString("type2_name1", noteCateogry.notgood.get(201));
		names[2] = sp.getString("type2_name2", noteCateogry.notgood.get(202));
		names[3] = sp.getString("type2_name3", noteCateogry.notgood.get(203));
		names[4] = sp.getString("type2_name4", noteCateogry.notgood.get(204));
		names[5] = sp.getString("type2_name5", noteCateogry.notgood.get(205));
		names[6] = sp.getString("type2_name6", noteCateogry.notgood.get(206));
		names[7] = sp.getString("type2_name7", noteCateogry.notgood.get(207));
		names[8] = sp.getString("type2_name8", noteCateogry.notgood.get(208));
		names[9] = sp.getString("type2_name9", noteCateogry.notgood.get(209));
		return names;
	}

	public static void setType2(String[] type1) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("type2_name0", type1[0]);
		edit.putString("type2_name1", type1[1]);
		edit.putString("type2_name2", type1[2]);
		edit.putString("type2_name3", type1[3]);
		edit.putString("type2_name4", type1[4]);
		edit.putString("type2_name5", type1[5]);
		edit.putString("type2_name6", type1[6]);
		edit.putString("type2_name7", type1[7]);
		edit.putString("type2_name8", type1[8]);
		edit.putString("type2_name9", type1[9]);
		edit.commit();
	}
	
	/** Get the names of the */
	public static String[] getType3() {
		String[] names = new String[noteNum];
		names[0] = sp.getString("type3_name0", noteCateogry.positive.get(300));
		names[1] = sp.getString("type3_name1", noteCateogry.positive.get(301));
		names[2] = sp.getString("type3_name2", noteCateogry.positive.get(302));
		names[3] = sp.getString("type3_name3", noteCateogry.positive.get(303));
		names[4] = sp.getString("type3_name4", noteCateogry.positive.get(304));
		names[5] = sp.getString("type3_name5", noteCateogry.positive.get(305));
		names[6] = sp.getString("type3_name6", noteCateogry.positive.get(306));
		names[7] = sp.getString("type3_name7", noteCateogry.positive.get(307));
		names[8] = sp.getString("type3_name8", noteCateogry.positive.get(308));
		names[9] = sp.getString("type3_name9", noteCateogry.positive.get(309));
		return names;
	}

	public static void setType3(String[] type1) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("type3_name0", type1[0]);
		edit.putString("type3_name1", type1[1]);
		edit.putString("type3_name2", type1[2]);
		edit.putString("type3_name3", type1[3]);
		edit.putString("type3_name4", type1[4]);
		edit.putString("type3_name5", type1[5]);
		edit.putString("type3_name6", type1[6]);
		edit.putString("type3_name7", type1[7]);
		edit.putString("type3_name8", type1[8]);
		edit.putString("type3_name9", type1[9]);
		edit.commit();
	}
	
	/** Get the names of the */
	public static String[] getType4() {
		String[] names = new String[noteNum];
		names[0] = sp.getString("type4_name0", noteCateogry.selftest.get(400));
		names[1] = sp.getString("type4_name1", noteCateogry.selftest.get(401));
		names[2] = sp.getString("type4_name2", noteCateogry.selftest.get(402));
		names[3] = sp.getString("type4_name3", noteCateogry.selftest.get(403));
		names[4] = sp.getString("type4_name4", noteCateogry.selftest.get(404));
		names[5] = sp.getString("type4_name5", noteCateogry.selftest.get(405));
		names[6] = sp.getString("type4_name6", noteCateogry.selftest.get(406));
		names[7] = sp.getString("type4_name7", noteCateogry.selftest.get(407));
		names[8] = sp.getString("type4_name8", noteCateogry.selftest.get(408));
		names[9] = sp.getString("type4_name9", noteCateogry.selftest.get(409));
		return names;
	}

	public static void setType4(String[] type1) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("type4_name0", type1[0]);
		edit.putString("type4_name1", type1[1]);
		edit.putString("type4_name2", type1[2]);
		edit.putString("type4_name3", type1[3]);
		edit.putString("type4_name4", type1[4]);
		edit.putString("type4_name5", type1[5]);
		edit.putString("type4_name6", type1[6]);
		edit.putString("type4_name7", type1[7]);
		edit.putString("type4_name8", type1[8]);
		edit.putString("type4_name9", type1[9]);
		edit.commit();
	}
	
	/** Get the names of the */
	public static String[] getType5() {
		String[] names = new String[noteNum];
		names[0] = sp.getString("type5_name0", noteCateogry.temptation.get(500));
		names[1] = sp.getString("type5_name1", noteCateogry.temptation.get(501));
		names[2] = sp.getString("type5_name2", noteCateogry.temptation.get(502));
		names[3] = sp.getString("type5_name3", noteCateogry.temptation.get(503));
		names[4] = sp.getString("type5_name4", noteCateogry.temptation.get(504));
		names[5] = sp.getString("type5_name5", noteCateogry.temptation.get(505));
		names[6] = sp.getString("type5_name6", noteCateogry.temptation.get(506));
		names[7] = sp.getString("type5_name7", noteCateogry.temptation.get(507));
		names[8] = sp.getString("type5_name8", noteCateogry.temptation.get(508));
		names[9] = sp.getString("type5_name9", noteCateogry.temptation.get(509));
		return names;
	}

	public static void setType5(String[] type1) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("type5_name0", type1[0]);
		edit.putString("type5_name1", type1[1]);
		edit.putString("type5_name2", type1[2]);
		edit.putString("type5_name3", type1[3]);
		edit.putString("type5_name4", type1[4]);
		edit.putString("type5_name5", type1[5]);
		edit.putString("type5_name6", type1[6]);
		edit.putString("type5_name7", type1[7]);
		edit.putString("type5_name8", type1[8]);
		edit.putString("type5_name9", type1[9]);
		edit.commit();
	}
	
	/** Get the names of the */
	public static String[] getType6() {
		String[] names = new String[noteNum];
		names[0] = sp.getString("type6_name0", noteCateogry.conflict.get(600));
		names[1] = sp.getString("type6_name1", noteCateogry.conflict.get(601));
		names[2] = sp.getString("type6_name2", noteCateogry.conflict.get(602));
		names[3] = sp.getString("type6_name3", noteCateogry.conflict.get(603));
		names[4] = sp.getString("type6_name4", noteCateogry.conflict.get(604));
		names[5] = sp.getString("type6_name5", noteCateogry.conflict.get(605));
		names[6] = sp.getString("type6_name6", noteCateogry.conflict.get(606));
		names[7] = sp.getString("type6_name7", noteCateogry.conflict.get(607));
		names[8] = sp.getString("type6_name8", noteCateogry.conflict.get(608));
		names[9] = sp.getString("type6_name9", noteCateogry.conflict.get(609));
		return names;
	}

	public static void setType6(String[] type1) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("type6_name0", type1[0]);
		edit.putString("type6_name1", type1[1]);
		edit.putString("type6_name2", type1[2]);
		edit.putString("type6_name3", type1[3]);
		edit.putString("type6_name4", type1[4]);
		edit.putString("type6_name5", type1[5]);
		edit.putString("type6_name6", type1[6]);
		edit.putString("type6_name7", type1[7]);
		edit.putString("type6_name8", type1[8]);
		edit.putString("type6_name9", type1[9]);
		edit.commit();
	}
	
	/** Get the names of the */
	public static String[] getType7() {
		String[] names = new String[noteNum];
		names[0] = sp.getString("type7_name0", noteCateogry.social.get(700));
		names[1] = sp.getString("type7_name1", noteCateogry.social.get(701));
		names[2] = sp.getString("type7_name2", noteCateogry.social.get(702));
		names[3] = sp.getString("type7_name3", noteCateogry.social.get(703));
		names[4] = sp.getString("type7_name4", noteCateogry.social.get(704));
		names[5] = sp.getString("type7_name5", noteCateogry.social.get(705));
		names[6] = sp.getString("type7_name6", noteCateogry.social.get(706));
		names[7] = sp.getString("type7_name7", noteCateogry.social.get(707));
		names[8] = sp.getString("type7_name8", noteCateogry.social.get(708));
		names[9] = sp.getString("type7_name9", noteCateogry.social.get(709));
		return names;
	}

	public static void setType7(String[] type1) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("type7_name0", type1[0]);
		edit.putString("type7_name1", type1[1]);
		edit.putString("type7_name2", type1[2]);
		edit.putString("type7_name3", type1[3]);
		edit.putString("type7_name4", type1[4]);
		edit.putString("type7_name5", type1[5]);
		edit.putString("type7_name6", type1[6]);
		edit.putString("type7_name7", type1[7]);
		edit.putString("type7_name8", type1[8]);
		edit.putString("type7_name9", type1[9]);
		
		edit.commit();
	}
	
	/** Get the names of the */
	public static String[] getType8() {
		String[] names = new String[noteNum];
		names[0] = sp.getString("type8_name0", noteCateogry.play.get(800));
		names[1] = sp.getString("type8_name1", noteCateogry.play.get(801));
		names[2] = sp.getString("type8_name2", noteCateogry.play.get(802));
		names[3] = sp.getString("type8_name3", noteCateogry.play.get(803));
		names[4] = sp.getString("type8_name4", noteCateogry.play.get(804));
		names[5] = sp.getString("type8_name5", noteCateogry.play.get(805));
		names[6] = sp.getString("type8_name6", noteCateogry.play.get(806));
		names[7] = sp.getString("type8_name7", noteCateogry.play.get(807));
		names[8] = sp.getString("type8_name8", noteCateogry.play.get(808));
		names[9] = sp.getString("type8_name9", noteCateogry.play.get(809));
		return names;
	}

	public static void setType8(String[] type1) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("type8_name0", type1[0]);
		edit.putString("type8_name1", type1[1]);
		edit.putString("type8_name2", type1[2]);
		edit.putString("type8_name3", type1[3]);
		edit.putString("type8_name4", type1[4]);
		edit.putString("type8_name5", type1[5]);
		edit.putString("type8_name6", type1[6]);
		edit.putString("type8_name7", type1[7]);
		edit.putString("type8_name8", type1[8]);
		edit.putString("type8_name9", type1[9]);
		edit.commit();
	}
	
	/** Get the names of the */
	public static String[] getTypeMood() {
		String[] names = new String[noteNum];
		/*names[0] = noteCateogry.play.get(900);
		names[1] = noteCateogry.play.get(901);
		names[2] = noteCateogry.play.get(902);
		names[3] = "";
		names[4] = "";
		names[5] = "";
		names[6] = "";
		names[7] = "";
		names[8] = "";
		names[9] = "";*/
		names[0] = "生氣";
		names[1] = "難過";
		names[2] = "緊張";
		names[3] = "厭惡";
		names[4] = "開心";
		names[5] = "驚恐";
		names[6] = "無聊";
		names[7] = "";
		names[8] = "";
		names[9] = "";
		return names;
	}
	
	public static String[] getTypeMoodReflection() {
		String[] names = new String[noteNum];
		/*names[0] = noteCateogry.play.get(900);
		names[1] = noteCateogry.play.get(901);
		names[2] = noteCateogry.play.get(902);
		names[3] = "";
		names[4] = "";
		names[5] = "";
		names[6] = "";
		names[7] = "";
		names[8] = "";
		names[9] = "";*/
		names[0] = "冷靜";
		names[1] = "堅定";
		names[2] = "放鬆";
		names[3] = "開心";
		names[4] = "活力十足";
		names[5] = "感覺被愛";
		names[6] = "";
		names[7] = "";
		names[8] = "";
		names[9] = "";
		return names;
	}

	public static void setTypeMood(String[] type1) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("typeMood_name0", type1[0]);
		edit.putString("typeMood_name1", type1[1]);
		edit.putString("typeMood_name2", type1[2]);
		edit.putString("typeMood_name3", type1[3]);
		edit.putString("typeMood_name4", type1[4]);
		edit.putString("typeMood_name5", type1[5]);
		edit.putString("typeMood_name6", type1[6]);
		edit.putString("typeMood_name7", type1[7]);
		edit.putString("typeMood_name8", type1[8]);
		edit.putString("typeMood_name9", type1[9]);
		edit.commit();
	}
	


	public static long getDetectionTimestamp() {
		return sp.getLong("latestDetectionTimestamp", 0);
	}
	
	public static long getLastTestTime() {
		return sp.getLong("latestDetectionDoneTimestamp", 0);
	}
	
	public static long getOpenAppTimestamp() {
		return sp.getLong("latestOpenAppTimestamp", getStartDate().getTimeInMillis());
	}
	
	public static void setOpenAppTimestamp() {//TODO:
		SharedPreferences.Editor edit = sp.edit();
		Calendar startCal = getStartDate();
		Calendar cal =  Calendar.getInstance();
		if(cal.compareTo(startCal) == -1 )
			cal = startCal;
		//edit.putLong("latestOpenAppTimestamp", System.currentTimeMillis());
		edit.putLong("latestOpenAppTimestamp", cal.getTimeInMillis());
		edit.commit();
	}

	public static void setTestFail() {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("latestDetectionDoneTimestamp", System.currentTimeMillis());
		edit.putBoolean("latestTestFail", true);
		edit.commit();
	}

	public static void setTestSuccess() {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("latestDetectionDoneTimestamp", System.currentTimeMillis());
		edit.putBoolean("latestTestFail", false);
		edit.commit();
	}

	
	public static boolean isTestFail() {
		return sp.getBoolean("latestTestFail", false);
	}

	public static void timeReset() {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("latestDetectionDoneTimestamp", 0L);
		edit.putLong("latestStartGPSTimestamp", 0);
		edit.putLong("latestDetectionTimestamp", 0);
		edit.commit();
	}

	public static boolean isDebugMode() {
		return sp.getBoolean("debug", false);
	}

	public static void setDebugMode(boolean debug) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("debug", debug);
		edit.commit();
	}

	public static boolean debugType() {
		return sp.getBoolean("debugType", false);
	}

	public static void setDebugType(boolean type) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("debugType", type);
		edit.commit();
	}

	public static boolean isFirstTime() {
		return sp.getBoolean("firstTime", true);
	}

	public static void setAfterFirstTime() {
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("firstTime", false);
		editor.commit();
	}



	public static int lastShowedCoupon() {
		return sp.getInt("showedCoupon", 0);
	}

	public static void setShowedCoupon(int num) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("showedCoupon", num);
		edit.commit();
	}

	public static void setGoal(String goal, int money, int drink_cost) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("targetGood", goal);
		edit.putInt("targetMoney", money);
		edit.putInt("perDrinkCost", drink_cost);
		edit.commit();
	}
	
	public static String getSavingGoal() {
		return sp.getString("targetGood",
				App.getContext().getString(R.string.default_goal_good));
	}

	public static int getSavingGoalMoney() {
		return sp.getInt("targetMoney", 50000);
	}

	public static int getSavingDrinkCost() {
		return sp.getInt("perDrinkCost", 200);
	}

	

	public static boolean isLocked() {
		return sp.getBoolean("systemLock", false);
	}

	public static void setLocked(boolean lock) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("systemLock", lock);
		edit.commit();
	}

	public static Calendar getLockDate() {
		int[] data = new int[3];
		Calendar cal = Calendar.getInstance();
		data[0] = sp.getInt("lockYear", cal.get(Calendar.YEAR));
		data[1] = sp.getInt("lockMonth", cal.get(Calendar.MONTH));
		data[2] = sp.getInt("lockDay", cal.get(Calendar.DATE));

		cal.set(data[0], data[1], data[2], 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static int[] getLockDateData() {
		int[] data = new int[3];
		Calendar cal = Calendar.getInstance();
		data[0] = sp.getInt("lockYear", cal.get(Calendar.YEAR));
		data[1] = sp.getInt("lockMonth", cal.get(Calendar.MONTH));
		data[2] = sp.getInt("lockDay", cal.get(Calendar.DATE));
		return data;
	}

	public static void setLockDate(int year, int month, int day) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("lockYear", year);
		edit.putInt("lockMonth", month);
		edit.putInt("lockDay", day);
		edit.commit();
	}

	public static void setShowAdditonalQuestionnaire() {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("additionalQuestionTime", System.currentTimeMillis());
		edit.commit();
	}



	


	public static void setUpdateDetection(boolean update) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("updateDetection", update);
		edit.commit();
	}

	public static boolean getUpdateDetection() {
		return sp.getBoolean("updateDetection", false);
	}

	public static void setUpdateDetectionTimestamp(long timestamp) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("updateDetectionTimestamp", timestamp);
		edit.commit();
	}

	public static long getUpdateDetectionTimestamp() {
		return sp.getLong("updateDetectionTimestamp", 0);
	}

	public static int getNotificationTimeIdx() {
		return sp.getInt("notificationTimeGap", 2);
	}

	public static int getNotificationTime() {
		int[] time = { 30, 60, 120, -1 };
		int idx = sp.getInt("notificationTimeGap", 2);
		return time[idx];
	}

	public static void setNotificationTimeIdx(int idx) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("notificationTimeGap", idx);
		edit.commit();
	}

	public static void setDebugDetectionTimestamp(long timestamp) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("debugDetection", timestamp);
		edit.commit();
	}

	public static long getDebugDetectionTimestamp() {
		return sp.getLong("debugDetection", 0);
	}

	public static void setPrevShowWeekState(int week, int state) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("prevShowWeek", week);
		edit.putInt("prevShowWeekState", state);
		edit.commit();
	}

	public static int getPrevShowWeek() {
		return sp.getInt("prevShowWeek", 0);
	}

	public static int getPrevShowWeekState() {
		return sp.getInt("prevShowWeekState", 0);
	}

	public static void setPageChange(boolean change) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("pageChange", change);
		edit.commit();
	}

	public static boolean getPageChange() {
		// TimeValue curTV = TimeValue.generate(System.currentTimeMillis());
		// if (curTV.getWeek() > 11)
		// return false;
		return sp.getBoolean("pageChange", false);
	}

	public static boolean getUseNewSensor() {
		return sp.getBoolean("useNewSensor", false);
	}

	public static void setUseNewSensor(boolean isNew) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("useNewSensor", isNew);
		edit.commit();
	}

	public static boolean showNotificationDialog() {
		long lastTime = sp.getLong("lastShowNotification", 0);
		long curTime = System.currentTimeMillis();
		return (curTime - lastTime > AlarmManager.INTERVAL_DAY * 2);
	}

	public static void setShowedNotificationDialog() {
		long curTime = System.currentTimeMillis();
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("lastShowNotification", curTime);
		edit.commit();
	}
	
	/** Appeal **/
	public static boolean getAppealOpen() {
		return sp.getBoolean("appealOpen", false);
	}

	public static void setAppealOpen(boolean isOpen) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("appealOpen", isOpen);
		edit.commit();
	}

	public static int getAppealAble() {
		return sp.getInt("appealAble", 0);
	}

	public static void setAppealAble(int isOpen) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("appealAble", isOpen);
		edit.commit();
	}
	
	public static boolean getAppealFail() {
		return sp.getBoolean("appealFail", false);
	}

	public static void setAppealFail(boolean is) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("appealFail", is);
		edit.commit();
	}
	
	public static boolean getAppealPos() {
		return sp.getBoolean("appealPos", false);
	}

	public static void setAppealPos(boolean is) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean("appealPos", is);
		edit.commit();
	}
	
	public static long getAppealLastFail() {
		return sp.getLong("appealLastFail", 0);
	}

	public static void setAppealLastFail(long ts) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("appealLastFail", ts);
		edit.commit();
	}
	
	public static long getAppealLastPos() {
		return sp.getLong("appealLastPos", 0);
	}

	public static void setAppealLastPos(long ts) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("appealLastPos", ts);
		edit.commit();
	}
	
	public static long getAppealStartTime() {
		return sp.getLong("appealStartTime", 0);
	}

	public static void setAppealStartTime(long ts) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("appealStartTime", ts);
		edit.commit();
	}
	
	public static void setSvmVersion(int v) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("svmVersion", v);
		edit.commit();
	}
	
	public static int getSvmVersion() {
		return sp.getInt("svmVersion", 0);
	}
	
	public static void setTriggerVersion(int v) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt("triggerVersion", v);
		edit.commit();
	}
	
	public static int getTriggerVersion() {
		return sp.getInt("triggerVersion", 0);
	}
	
	public static long getLastestNoteAddTimestamp() {
		return sp.getLong("lastestNoteAddTimestamp", 0);
	}
	
	public static void setLastestNoteAddTimestamp(long v) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("lastestNoteAddTimestamp", v);
		edit.commit();
	}
	
	public static long getLastestThinkingTimestamp() {
		return sp.getLong("lastestThinkingTimestamp", 0);
	}
	
	public static void setLastestThinkingTimestamp(long v) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("lastestThinkingTimestamp", v);
		edit.commit();
	}
	
	public static long getLastestReflectionTimestamp() {
		return sp.getLong("lastestReflectionTimestamp", 0);
	}
	
	public static void setLastestReflectionTimestamp(long v) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("lastestReflectionTimestamp", v);
		edit.commit();
	}
	
	public static long getLastestIdentifyTimestamp() {
		return sp.getLong("lastestIdentifyTimestamp", 0);
	}
	
	public static void setLastestIdentifyTimestamp(long v) {
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong("lastestIdentifyTimestamp", v);
		edit.commit();
	}
	
	// public static String[] getSponsors() {
	// 	String[] sponsor = new String[5];
	// 	sponsor[0] = sp.getString("sponsor0", "");
	// 	sponsor[1] = sp.getString("sponsor1", "");
	// 	sponsor[2] = sp.getString("sponsor2", "");
	// 	sponsor[3] = sp.getString("sponsor3", "");
	// 	sponsor[4] = sp.getString("sponsor4", "");
	// 	return sponsor;
	// }

	// public static void setSponsor(String sponsor, int id) {
	// 	String key = "recreation" + id;
	// 	SharedPreferences.Editor edit = sp.edit();
	// 	edit.putString(key, recreation);
	// 	edit.commit();

	// }

}
