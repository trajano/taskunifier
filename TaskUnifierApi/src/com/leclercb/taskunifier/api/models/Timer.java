package com.leclercb.taskunifier.api.models;

import java.util.Calendar;

import com.leclercb.commons.api.utils.DateUtils;

public class Timer implements Comparable<Timer> {
	
	private long value;
	private Calendar startDate;
	
	public Timer() {
		this(0, null);
	}
	
	public Timer(long value) {
		this(value, null);
	}
	
	public Timer(long value, Calendar startDate) {
		this.value = value;
		this.startDate = DateUtils.cloneCalendar(startDate);
	}
	
	public Timer(Timer timer) {
		this(
				(timer == null ? 0 : timer.getValue()),
				(timer == null ? null : timer.getStartDate()));
	}
	
	public long getValue() {
		return this.value;
	}
	
	public Calendar getStartDate() {
		return DateUtils.cloneCalendar(this.startDate);
	}
	
	public long getTimerValue() {
		if (this.startDate == null)
			return this.value;
		
		Calendar now = Calendar.getInstance();
		long second = now.getTimeInMillis() - this.startDate.getTimeInMillis();
		second = second / 1000;
		
		return this.value + second;
	}
	
	public void setValue(long value) {
		this.value = value;
		
		if (this.startDate != null)
			this.startDate = Calendar.getInstance();
	}
	
	public boolean isStarted() {
		return this.startDate != null;
	}
	
	public void start() {
		if (this.isStarted())
			return;
		
		this.startDate = Calendar.getInstance();
	}
	
	public void stop() {
		if (!this.isStarted())
			return;
		
		this.value = this.getTimerValue();
		this.startDate = null;
	}
	
	@Override
	public String toString() {
		return this.getTimerValue() + "";
	}
	
	@Override
	public int compareTo(Timer timer) {
		if (timer == null)
			return 1;
		
		return ((Long) this.getTimerValue()).compareTo(timer.getTimerValue());
	}
	
}
