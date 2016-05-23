package com.ubicomp.ketdiary.statistic;

import java.util.Calendar;

import com.ubicomp.ketdiary.MainActivity;
import com.ubicomp.ketdiary.daybook.SectionsPagerAdapter;
import com.ubicomp.ketdiary.system.PreferenceControl;
import com.ubicomp.ketdiary2.R;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class StatisticMonthView extends StatisticPageView {
	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private View[] pageViewList = null;
	private static final int sustainMonth = PreferenceControl.getSustainMonth();
	private Calendar startDay = PreferenceControl.getStartDate();
	private int startYear = startDay.get(Calendar.YEAR);
	private int startMonth = startDay.get(Calendar.MONTH) + 1;
	private int currentPageIdx = sustainMonth - 1;
	private ViewPager mViewPager;
	public static final int TAG_PAGE_YEAR = R.string.TAG_PAGE_YEAR;
	public static final int TAG_PAGE_MONTH = R.string.TAG_PAGE_MONTH;
	
	public StatisticMonthView() {
		super(R.layout.calendar_main);
		
		pageViewList = new View[sustainMonth];
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(startYear, startMonth - 1, 1);
		
		Context mContext = MainActivity.getMainActivity();
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < sustainMonth; i++) {

			pageViewList[i] = (View) inflater.inflate(R.layout.fragment_calendar, null);
			
			pageViewList[i].setTag(TAG_PAGE_YEAR, tempCalendar.get(Calendar.YEAR));
			pageViewList[i].setTag(TAG_PAGE_MONTH, tempCalendar.get(Calendar.MONTH));
			tempCalendar.add(Calendar.MONTH, 1);
		}
		mSectionsPagerAdapter = new SectionsPagerAdapter(pageViewList);
		
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		// Set up the ViewPager with the sections adapter.
		
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
