package com.leclercb.taskunifier.gui.models;

import java.util.Comparator;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.taskunifier.gui.template.Template;

public class TemplateComparator implements Comparator<Template> {
	
	@Override
	public int compare(Template t1, Template t2) {
		String s1 = t1 == null ? null : t1.getTitle().toLowerCase();
		String s2 = t2 == null ? null : t2.getTitle().toLowerCase();
		
		int result = CompareUtils.compare(s1, s2);
		
		if (result == 0) {
			s1 = t1 == null ? null : t1.getId();
			s2 = t2 == null ? null : t2.getId();
			
			result = CompareUtils.compare(s1, s2);
		}
		
		return result;
	}
	
}
