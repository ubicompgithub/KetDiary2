package com.ubicomp.ketdiary.data.structure;

import java.util.Comparator;

public class SortItem {
	private int index;
	private int value;
	
	public SortItem(int index, int value){
		this.index = index;
		this.value = value;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public static Comparator<SortItem> Comparator = new Comparator<SortItem>() {

		public int compare(SortItem item1, SortItem item2) {
		
			int type1 = item1.getValue();
			int type2 = item2.getValue();
			
			return type1 - type2;			
		}
	};
	
}
