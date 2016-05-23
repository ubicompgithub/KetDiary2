package com.ubicomp.ketdiary.data.structure;

import java.util.Comparator;

public class RankingCount {
	private int id, id2, numerator, denominator;
	private int value;
	public RankingCount(int id, int id2)
	{	
		this.id = id;
		this.id2 = id2;
		this.numerator = 0;
		this.denominator = 0;
		this.value = 0;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void AddTrue()
	{
		numerator++;
		denominator++;
	}
	
	public void AddFalse()
	{
		denominator++;
	}
	
	public void AddImpact(int x)
	{
		value += x;
	}
	
	
	public int getValue()
	{
		return value;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getId2()
	{
		return id2;
	}
	
	public int getNumerator()
	{
		return numerator;
	}
	public int getDenominator()
	{
		return denominator;
	}
	
	public static Comparator<RankingCount> RankingCountTypeComparator = new Comparator<RankingCount>() {

		public int compare(RankingCount Note1, RankingCount Note2) {
		
			int t1 = Note1.getNumerator() * Note2.getDenominator();
			int t2 = Note2.getNumerator() * Note1.getDenominator();
		
			return t2 - t1;
			
		}
	};
	
	public static Comparator<RankingCount> RankingCountTypeComparatorValue = new Comparator<RankingCount>() {

		public int compare(RankingCount Note1, RankingCount Note2) {
		
			int t1 = Note1.getValue();
			int t2 = Note2.getValue();
		
			return t2 - t1;
			
		}
	};
}
