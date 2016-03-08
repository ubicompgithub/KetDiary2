package com.ubicomp.ketdiary.data.structure;

import java.util.Comparator;

public class RankingCount {
	private int id, id2, numerator, denominator;
	private double value;
	public RankingCount(int id, int id2)
	{	
		this.id = id;
		this.id2 = id2;
		this.numerator = 0;
		this.denominator = 0;
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
	
	public double getValue()
	{
		double ans = numerator;
		double t = denominator;
		return ans/t;
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
	
}
