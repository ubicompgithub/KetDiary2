package com.ubicomp.ketdiary.data.upload;

import com.ubicomp.ketdiary2.R;
import com.ubicomp.ketdiary.system.PreferenceControl;


/** 
 * Return Server's Url
 * @author Andy Chen
 */
public class ServerUrl {
	private static final String SERVER_URL = "https://140.112.30.165/rehabdiary2/";
	private static final String SERVER_URL_DEVELOP = "https://140.112.30.171/rehabdiary2/";
	
	/** create instance*/
	//public static ServerUrl inst = new ServerUrl();
	//private ServerUrl(){}
	
	/**
	 * 
	 * @return upload TestDetail server url
	 */
	public static String getTestDetailUrl(){
		final String URL = "test/add_test_detail.php";;
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;	
	}
	
	public static String getPatientUrl(){
		final String URL = "test/Patient2.php";;
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	public static String getTestDetail2Url(){
		final String URL = "test/TestDetail.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	public static String getTestResultUrl(){
		final String URL = "test/TestResult.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	public static String getNoteAddUrl(){
		final String URL = "test/NoteAdd.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	public static String getUpdateThinkingUrl(){
		final String URL = "test/UpdateThinking.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	public static String getQuestionTestUrl(){
		final String URL = "test/QuestionTest.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	public static String getCopingSkillUrl(){
		final String URL = "test/CopingSkill.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	/**
	 * URL for querying the long-term ranking
	 * 
	 * @return url
	 */
	public static String SERVER_URL_RANK_ALL() {
		final String URL = "rank/rankAll.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	/**
	 * URL for querying the short-term ranking
	 * 
	 * @return url
	 */
	public static String SERVER_URL_RANK_WEEK() {
		final String URL = "rank/rankWeek.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	/**
	 * URL for update Cassette ID
	 * 
	 * @return url
	 */
	public static String SERVER_URL_CASSETTE() {
		final String URL = "test/cassette.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	/**
	 * URL for inserting table ExchangeHistory
	 * 
	 * @return url
	 */
	public static String SERVER_URL_EXCHANGE_HISTORY() {
		final String URL = "test/exchangeHistory.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	/**
	 * URL for inserting table Appeal
	 * 
	 * @return url
	 */
	public static String SERVER_URL_APPEAL() {
		final String URL = "test/Appeal.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	/**
	 * URL for inserting table Reflection
	 * 
	 * @return url
	 */
	public static String getReflectionUrl(){
		final String URL = "test/Reflection.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	/**
	 * URL for inserting table Score
	 * 
	 * @return url
	 */
	public static String getAddScoreUrl(){
		final String URL = "test/AddScore.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	
	/**
	 * URL for inserting table IdentityScore
	 * 
	 * @return url
	 */
	public static String getIdentityScoreUrl(){
		final String URL = "test/IdentityScore.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	/**
	 * URL for uploading clicklog
	 * 
	 * @return url
	 */
	public static String SERVER_URL_CLICKLOG() {
		final String URL = "test/clickLog.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	public static int SERVER_CERTIFICATE(){
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			//return R.raw.keys;
			return R.raw.server171;
		else
			return R.raw.alcohol_certificate;
		
	}
	
	/**
	 * URL for update SVM Model
	 * 
	 * @return url
	 */
	public static String SERVER_URL_SVM_MODEL() {
		final String URL = "SVM/model.out";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	public static String SERVER_URL_SCALE_PARAM() {
		final String URL = "SVM/scale_param.out";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	public static String SERVER_URL_VERSION() {
		final String URL = "test/Version.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	public static String SERVER_URL_TRIGGER() {
		final String URL = "test/Trigger.php";
		boolean develop = PreferenceControl.isDeveloper();
		if (develop)
			return SERVER_URL_DEVELOP + URL;
		else
			return SERVER_URL + URL;
	}
	
	
}
