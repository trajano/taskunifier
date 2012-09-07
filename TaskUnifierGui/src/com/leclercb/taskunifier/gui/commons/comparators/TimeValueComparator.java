package com.leclercb.taskunifier.gui.commons.comparators;

import java.util.Comparator;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.taskunifier.gui.utils.TimeValue;

public class TimeValueComparator implements Comparator<TimeValue> {
	
	public static final TimeValueComparator INSTANCE = new TimeValueComparator();
	
	private TimeValueComparator() {
		
	}
	
	@Override
	public int compare(TimeValue tv1, TimeValue tv2) {
		Integer i1 = (tv1 == null ? null : tv1.getField());
		Integer i2 = (tv2 == null ? null : tv2.getField());
		
		int result = CompareUtils.compare(i1, i2);
		result *= -1;
		
		if (result != 0)
			return result;
		
		i1 = (tv1 == null ? null : tv1.getAmount());
		i2 = (tv2 == null ? null : tv2.getAmount());
		
		return CompareUtils.compare(i1, i2);
	}
	
}
