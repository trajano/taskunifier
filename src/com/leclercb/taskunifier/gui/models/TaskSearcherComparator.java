package com.leclercb.taskunifier.gui.models;

import java.util.Comparator;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class TaskSearcherComparator implements Comparator<TaskSearcher> {
	
	@Override
	public int compare(TaskSearcher ts1, TaskSearcher ts2) {
		String s1 = ts1 == null ? null : ts1.getTitle().toLowerCase();
		String s2 = ts2 == null ? null : ts2.getTitle().toLowerCase();
		
		return CompareUtils.compare(s1, s2);
	}
	
}
