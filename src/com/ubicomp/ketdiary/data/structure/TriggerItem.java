package com.ubicomp.ketdiary.data.structure;

public class TriggerItem {
	private int item;
	private String content;
	private boolean show;
	
	public TriggerItem(int item, String content, boolean show){
		this.item = item;
		this.content = content;
		this.show = show;
	}
	
	public int getItem(){
		return item;
	}
	
	public String getContent(){
		return content;
	}
	
	public boolean getShow(){
		return show;
	}
}
