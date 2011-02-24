package com.leclercb.taskunifier.gui.utils;

import java.text.SimpleDateFormat;

public class DateTimeFormatUtils {
	
	private DateTimeFormatUtils() {

	}
	
	private static DateTimeFormatInfo[] dateFormats = new DateTimeFormatInfo[] {
			new DateTimeFormatInfo(
					new SimpleDateFormat("MM/dd/yyyy"),
					"##/##/####"),
			new DateTimeFormatInfo(
					new SimpleDateFormat("dd/MM/yyyy"),
					"##/##/####"),
			new DateTimeFormatInfo(
					new SimpleDateFormat("yyyy-MM-dd"),
					"####-##-##") };
	
	private static DateTimeFormatInfo[] timeFormats = new DateTimeFormatInfo[] {
			new DateTimeFormatInfo(new SimpleDateFormat("hh:mm aa"), "##:## UU"),
			new DateTimeFormatInfo(new SimpleDateFormat("HH:mm"), "##:##") };
	
	public static SimpleDateFormat[] getAvailableDateFormats() {
		SimpleDateFormat[] formats = new SimpleDateFormat[dateFormats.length];
		
		for (int i = 0; i < dateFormats.length; i++)
			formats[i] = dateFormats[i].getFormat();
		
		return formats;
	}
	
	public static SimpleDateFormat[] getAvailableTimeFormats() {
		SimpleDateFormat[] formats = new SimpleDateFormat[timeFormats.length];
		
		for (int i = 0; i < timeFormats.length; i++)
			formats[i] = timeFormats[i].getFormat();
		
		return formats;
	}
	
	public static String getMask(String format) {
		for (DateTimeFormatInfo info : dateFormats) {
			if (info.getFormat().toPattern().equals(format))
				return info.getMask();
		}
		
		for (DateTimeFormatInfo info : timeFormats) {
			if (info.getFormat().toPattern().equals(format))
				return info.getMask();
		}
		
		return null;
	}
	
	private static class DateTimeFormatInfo {
		
		private SimpleDateFormat format;
		private String mask;
		
		public DateTimeFormatInfo(SimpleDateFormat format, String mask) {
			this.format = format;
			this.mask = mask;
		}
		
		public SimpleDateFormat getFormat() {
			return this.format;
		}
		
		public String getMask() {
			return this.mask;
		}
		
	}
	
}
