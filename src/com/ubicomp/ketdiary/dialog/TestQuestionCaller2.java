package com.ubicomp.ketdiary.dialog;

/**
 * Interface for calling the test questionnaire dialog
 * 
 * @author Andy Chen
 */
public interface TestQuestionCaller2 {
	/** write the questionnaire results into a file */
	//public void writeQuestionFile(int day, int timeslot, int type, int item, int impact, String description);
	public int writeQuestionFile(int day, int timeslot, int type, int item, int impact, String action,String feeling, String thinking, int finished, int key);
	public void updateList();
	public void updateRankList();
	public void resetView();
	public void blockView();
}
