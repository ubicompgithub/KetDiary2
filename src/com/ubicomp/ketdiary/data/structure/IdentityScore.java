package com.ubicomp.ketdiary.data.structure;

public class IdentityScore {
	private int key;
	public TimeValue tv;
	private int isReflection;
	private int score;
	
	public IdentityScore(long ts, int score, int key, int isReflection){
		this.tv = TimeValue.generate(ts);
		this.score = score;
		this.key = key;
		this.isReflection = isReflection;
	}
	
	public TimeValue getTv() {
		return tv;
	}

	public int getScore() {
		return score;
	}

	public int getKey() {
		return key;
	}
	
	public int getIsReflection() {
		return isReflection;
	}
	
	
}
