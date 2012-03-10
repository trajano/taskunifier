package com.leclercb.commons.api.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class SortedProperties extends Properties {
	
	public SortedProperties() {
		super();
	}
	
	public SortedProperties(Properties defaults) {
		super(defaults);
	}
	
	@Override
	public Enumeration<Object> keys() {
		Enumeration<Object> keys = super.keys();
		List<Object> list = new ArrayList<Object>();
		
		while (keys.hasMoreElements()) {
			list.add(keys.nextElement());
		}
		
		Collections.sort(list, new Comparator<Object>() {
			
			@Override
			public int compare(Object o1, Object o2) {
				return ((String) o1).compareTo((String) o2);
			}
			
		});
		
		return Collections.enumeration(list);
	}
	
}
