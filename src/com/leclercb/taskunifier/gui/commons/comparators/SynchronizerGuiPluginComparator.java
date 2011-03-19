package com.leclercb.taskunifier.gui.commons.comparators;

import java.util.Comparator;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;

public class SynchronizerGuiPluginComparator implements Comparator<SynchronizerGuiPlugin> {
	
	@Override
	public int compare(SynchronizerGuiPlugin p1, SynchronizerGuiPlugin p2) {
		String s1 = p1 == null ? null : (p1.getSynchronizerApi().getApiName()
				+ " ("
				+ p1.getName()
				+ " - "
				+ p1.getVersion() + ")");
		String s2 = p2 == null ? null : (p2.getSynchronizerApi().getApiName()
				+ " ("
				+ p2.getName()
				+ " - "
				+ p2.getVersion() + ")");
		
		return CompareUtils.compare(s1, s2);
	}
	
}
