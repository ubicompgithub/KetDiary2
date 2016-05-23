package com.ubicomp.ketdiary.data.structure;

public class AddScore {
	private TimeValue tv;
	private int addScore;
	private int accumulation;
	private String reason;
	
	public AddScore(long ts, int addScore, int accumulation, String reason){
		this.tv = TimeValue.generate(ts);
		this.addScore = addScore;
		this.accumulation = accumulation;
		this.reason = reason;
	}
	
	public TimeValue getTv() {
		return tv;
	}
	
	public int getAddScore(){
		return addScore;
	}
	
	public int getAccumulation(){
		return accumulation;
	}
	
	public String getReason(){
		return reason;
	}
}
