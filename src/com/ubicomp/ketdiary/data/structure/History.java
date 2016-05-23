package com.ubicomp.ketdiary.data.structure;

public class History {
	
	private TimeValue tv;
	private int item;
	private int type;
	private String content;
	
	public final static int EVENT_TYPE = 1;
	public final static int THINKING_TYPE = 2;
	public final static int EVENT_REFLECTION_TYPE = 3;
	public final static int THINKING_REFLECTION_TYPE = 4;
	
	public History(long ts, int item, int type, String content){
		this.tv = TimeValue.generate(ts);
		this.item = item;
		this.type = type;
		this.content = content;
	}
	
	public TimeValue getTv() {
		return tv;
	}
	
	public int getItem(){
		return item;
	}
	
	public int getType(){
		return type;
	}
	
	public String getContent(){
		return content;
	}
	
}
