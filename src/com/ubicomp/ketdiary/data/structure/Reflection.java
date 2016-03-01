package com.ubicomp.ketdiary.data.structure;

import java.util.Calendar;

public class Reflection {
	private TimeValue tv;
	private String action;
	private String feeling;
	private String thinking;
	private int key;
	
	public Reflection(long tv, String action, String feeling, String thinking, int key)
	{
		this.tv = TimeValue.generate(tv);
		this.action = action;
		this.feeling = feeling;
		this.thinking = thinking;
		this.key = key;
	}

	public TimeValue getTv() {
		return tv;
	}
	
	public String getAction() {
		return action;
	}
	
	public String getFeeling() {
		return feeling;
	}
	
	public String getThinking() {
		return thinking;
	}
	
	public int getKey() {
		return key;
	}
}
