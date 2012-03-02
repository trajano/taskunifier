package com.leclercb.taskunifier.api.models;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.IgnoreCaseString;

public class Tag extends IgnoreCaseString {
	
	public Tag(String tag) {
		super(tag.trim());
		CheckUtils.hasContent(tag);
	}
	
	public static boolean isValid(String tag) {
		if (tag == null || tag.trim().length() == 0)
			return false;
		
		return true;
	}
	
}
