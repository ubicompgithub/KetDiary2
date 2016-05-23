package com.ubicomp.ketdiary.data.structure;

public class ThinkingEvent {
	private String event, thinking, feeling;
	private int key;
	private int isReflection;
	
	public ThinkingEvent(String event, String feeling, String thinking, int key, int isReflection) {
		this.event = event;
		this.feeling = feeling;
		this.thinking = thinking;
		this.key = key;
		this.isReflection = isReflection;
	}
	
	public String getEvent(){
		return event;
	}
	
	public String getFeeling(){
		return feeling;
	}
	
	public String getThinking(){
		return thinking;
	}
	
	public int getKey(){
		return key;
	}
	
	public int getIsReflection(){
		return isReflection;
	}
	
	
	
}
