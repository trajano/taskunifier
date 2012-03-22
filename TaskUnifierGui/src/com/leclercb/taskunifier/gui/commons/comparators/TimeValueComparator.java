package com.leclercb.taskunifier.gui.commons.comparators;

import java.util.Comparator;

import com.leclercb.taskunifier.gui.utils.TimeValue;

public class TimeValueComparator implements Comparator<TimeValue> {
	
	@Override
	public int compare(TimeValue o1, TimeValue o2) {
		int result = new Integer(o1.getField()).compareTo(o2.getField());
		
		if (result != 0)
			return result;
		
		return new Integer(o1.getAmount()).compareTo(o2.getAmount());
	}
	
}
