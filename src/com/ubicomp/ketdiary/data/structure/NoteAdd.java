package com.ubicomp.ketdiary.data.structure;

import java.util.Calendar;
import java.util.Comparator;


public class NoteAdd {
	private int isAfterTest;
	private TimeValue tv;
	private TimeValue recordTv;
	private int timeslot;
	private int category;
	private int type;
	private int items;
	private int impact;
	//private String description;
	private String feeling;
	private String action;
	private String thinking;
	private int finished;
	private int weeklyScore;
	private int score;
	private int key;
	private int reflection;
	private int result;

	public NoteAdd(int isAfterTest, long tv, int rYear, int rMonth, int rDay, int timeslot,
			int category, int type, int items, int impact, String action, String feeling, String thinking, int finished, int weeklyScore, int score, int key) {
		this.isAfterTest = isAfterTest;
		this.tv = TimeValue.generate(tv);
		
		Calendar cal = Calendar.getInstance();
		cal.set(rYear, rMonth, rDay, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		this.recordTv = TimeValue.generate(cal.getTimeInMillis());
		this.timeslot = timeslot;
		this.category = category;
		this.type = type;
		this.items = items;
		this.impact = impact;
		//this.description=description;
		this.action = action;
		this.thinking = thinking;
		this.feeling = feeling;
		this.finished = finished;
		this.weeklyScore = weeklyScore;
		this.score = score;
		this.key = key;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(tv.toString());
		sb.append(' ');
		sb.append(recordTv.toString());
		sb.append(' ');
		sb.append(isAfterTest);
		sb.append(' ');
		sb.append(category);
		sb.append(' ');
		sb.append(type);
		sb.append(' ');
		sb.append(items);
		sb.append(' ');
		sb.append(impact);
		sb.append(' ');
		//sb.append(description);
		sb.append(action);
		sb.append(' ');
		sb.append(feeling);
		sb.append(' ');
		sb.append(thinking);
		sb.append(' ');
		sb.append(finished);
		sb.append(' ');
		sb.append(score);
		return sb.toString();
	}

	public TimeValue getTv() {
		return tv;
	}

	public TimeValue getRecordTv() {
		return recordTv;
	}
	
	public int getTimeSlot() {
		return timeslot;
	}

	public int getIsAfterTest() {
		return isAfterTest;
	}
	
	
	public int getCategory() {
		return category;
	}

	public int getType() {
		return type;
	}
	
	public int getItems() {
		return items;
	}
	
	public int getImpact() {
		return impact;
	}

	/*public String getDescription() {
		return description;
	}*/
	
	public String getAction() {
		return action;
	}
	
	public String getFeeling() {
		return feeling;
	}
	
	public String getThinking() {
		return thinking;
	}
	
	public int getFinished() {
		return finished;
	}
	
	public int getWeeklyScore() {
		return weeklyScore;
	}

	public int getScore() {
		return score;
	}

	public int getKey() {
		return key;
	}
	
	public int getReflection() {
		return reflection;
	}
	
	public void setReflection(int reflection) {
		this.reflection = reflection;
	}
	
	public void setResult(int result) {
		this.result = result;
	}
	
	public static Comparator<NoteAdd> NoteAddTypeComparator = new Comparator<NoteAdd>() {

		public int compare(NoteAdd Note1, NoteAdd Note2) {
		
			int type1 = Note1.getItems();
			int type2 = Note2.getItems();
			
			//ascending order
			return type1 - type2;
			
			//descending order
			//return fruitName2.compareTo(fruitName1);
			
		}
	};
	
	public static Comparator<NoteAdd> ImpactComparator = new Comparator<NoteAdd>() {

		public int compare(NoteAdd Note1, NoteAdd Note2) {
		
			int type1 = Note1.getImpact();
			int type2 = Note2.getImpact();
			
			//ascending order
			return type1 - type2;
			

			
		}
	};
	
	public static Comparator<NoteAdd> ResultComparator = new Comparator<NoteAdd>() {

		public int compare(NoteAdd Note1, NoteAdd Note2) {
		
			int type1 = Note1.getIsAfterTest();
			int type2 = Note2.getIsAfterTest();
			
			//ascending order
			return type1 - type2;
			
			//descending order
			//return fruitName2.compareTo(fruitName1);
			
		}
	};
	
	public static Comparator<NoteAdd> ReflectionComparator = new Comparator<NoteAdd>() {

		public int compare(NoteAdd Note1, NoteAdd Note2) {
		
			int type1 = Note1.getReflection();
			int type2 = Note2.getReflection();
			
			//ascending order
			return type1 - type2;
			
			//descending order
			//return fruitName2.compareTo(fruitName1);
			
		}
	};
	
}
