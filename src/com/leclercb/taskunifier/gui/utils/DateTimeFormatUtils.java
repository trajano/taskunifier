/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.utils;

import java.text.SimpleDateFormat;

public final class DateTimeFormatUtils {
	
	private DateTimeFormatUtils() {
		
	}
	
	private static DateTimeFormatInfo[] dateFormats = new DateTimeFormatInfo[] {
			new DateTimeFormatInfo(
					new SimpleDateFormat("MM/dd/yyyy"),
					"##/##/####"),
			new DateTimeFormatInfo(
					new SimpleDateFormat("MM-dd-yyyy"),
					"##-##-####"),
			new DateTimeFormatInfo(
					new SimpleDateFormat("dd/MM/yyyy"),
					"##/##/####"),
			new DateTimeFormatInfo(
					new SimpleDateFormat("dd-MM-yyyy"),
					"##-##-####"),
			new DateTimeFormatInfo(
					new SimpleDateFormat("dd.MM.yyyy"),
					"##.##.####"),
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
