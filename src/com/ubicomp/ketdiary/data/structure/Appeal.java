package com.ubicomp.ketdiary.data.structure;

public class Appeal {

	public TimeValue tv;
	public int appealType;
	public int appealTimes;

	public Appeal(long ts, int appealType, int appealTimes) {
		this.tv = TimeValue.generate(ts);
		this.appealType = appealType;
		this.appealTimes = appealTimes;
	}

	public TimeValue getTv() {
		return tv;
	}

	public int getAppealTimes() {
		return appealTimes;
	}

	public int getAppealType() {
		return appealType;
	}
}