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
package com.leclercb.commons.api.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class DateUtils {
	
	private DateUtils() {
		
	}
	
	public static String getDateAsString(String format) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		return sdf.format(calendar.getTime());
	}
	
	public static Calendar cloneCalendar(Calendar calendar) {
		if (calendar == null)
			return null;
		
		Calendar clone = Calendar.getInstance();
		clone.setTimeInMillis(calendar.getTimeInMillis());
		
		return clone;
	}
	
	public static void goToFirstDayOfMonth(Calendar c) {
		c.set(Calendar.DAY_OF_MONTH, 1);
	}
	
	public static void goToLastDayOfMonth(Calendar c) {
		int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, day);
	}
	
	public static int getDiffInMonths(Calendar c1, Calendar c2) {
		CheckUtils.isNotNull(c1);
		CheckUtils.isNotNull(c2);
		
		c1 = cloneCalendar(c1);
		c2 = cloneCalendar(c2);
		
		int diffYear = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		int diffMonths = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		
		diffMonths += (diffYear * 12);
		
		return diffMonths;
	}
	
	public static void goToFirstDayOfWeek(Calendar c) {
		int r = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
		
		if (r < 0)
			r = 7 - r;
		
		c.add(Calendar.DAY_OF_MONTH, -r);
	}
	
	public static void goToLastDayOfWeek(Calendar c) {
		goToFirstDayOfWeek(c);
		c.add(Calendar.DAY_OF_MONTH, 6);
	}
	
	public static int getDiffInWeeks(Calendar c1, Calendar c2) {
		CheckUtils.isNotNull(c1);
		CheckUtils.isNotNull(c2);
		
		c1 = cloneCalendar(c1);
		c2 = cloneCalendar(c2);
		
		removeTime(c1);
		removeTime(c2);
		
		goToFirstDayOfWeek(c1);
		goToFirstDayOfWeek(c2);
		
		long milliSeconds1 = c1.getTimeInMillis();
		long milliSeconds2 = c2.getTimeInMillis();
		long diff = milliSeconds2 - milliSeconds1;
		int diffWeeks;
		
		diffWeeks = (int) Math.round(diff / (7 * 24 * 60 * 60 * 1000.0));
		
		return diffWeeks;
	}
	
	public static double getDiffInDays(Calendar c1, Calendar c2, boolean useTime) {
		CheckUtils.isNotNull(c1);
		CheckUtils.isNotNull(c2);
		
		c1 = cloneCalendar(c1);
		c2 = cloneCalendar(c2);
		
		if (!useTime) {
			removeTime(c1);
			removeTime(c2);
		}
		
		long milliSeconds1 = c1.getTimeInMillis();
		long milliSeconds2 = c2.getTimeInMillis();
		long diff = milliSeconds2 - milliSeconds1;
		double diffDays;
		
		if (!useTime) {
			diffDays = Math.round(diff / (24 * 60 * 60 * 1000.0));
		} else {
			diffDays = diff / (24 * 60 * 60 * 1000.0);
		}
		
		return diffDays;
	}
	
	public static double getDiffInHours(Calendar c1, Calendar c2) {
		CheckUtils.isNotNull(c1);
		CheckUtils.isNotNull(c2);
		
		c1 = cloneCalendar(c1);
		c2 = cloneCalendar(c2);
		
		long milliSeconds1 = c1.getTimeInMillis();
		long milliSeconds2 = c2.getTimeInMillis();
		long diff = milliSeconds2 - milliSeconds1;
		
		return diff / (60 * 60 * 1000.0);
	}
	
	public static void removeTime(Calendar c) {
		if (c == null)
			return;
		
		c.set(
				c.get(Calendar.YEAR),
				c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH),
				0,
				0,
				0);
		
		c.set(Calendar.MILLISECOND, 0);
	}
	
}
