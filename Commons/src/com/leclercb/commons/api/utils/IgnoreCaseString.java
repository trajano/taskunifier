package com.leclercb.commons.api.utils;

import java.io.Serializable;

public class IgnoreCaseString implements Serializable, Comparable<IgnoreCaseString> {
	
	private final String string;
	private final String normalized;
	
	public IgnoreCaseString(String string) {
		CheckUtils.isNotNull(string);
		
		this.string = string;
		this.normalized = string.toUpperCase();
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
	@Override
	public int hashCode() {
		return this.normalized.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IgnoreCaseString) {
			return ((IgnoreCaseString) obj).normalized.equals(this.normalized);
		}
		
		return false;
	}
	
	@Override
	public int compareTo(IgnoreCaseString o) {
		return this.string.compareToIgnoreCase(o.string);
	}
	
}
