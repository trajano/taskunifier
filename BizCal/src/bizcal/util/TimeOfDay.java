/*******************************************************************************
 * Bizcal is a component library for calendar widgets written in java using swing.
 * Copyright (C) 2007  Frederik Bertilsson 
 * Contributors:       Martin Heinemann martin.heinemann(at)tudor.lu
 * 
 * http://sourceforge.net/projects/bizcal/
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 * 
 *******************************************************************************/
package bizcal.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Fredrik Bertilsson
 */
public class TimeOfDay implements Comparable {
	
	private long _time;
	
	public TimeOfDay(long time) {
		this._time = time;
	}
	
	public TimeOfDay(int hours, int minutes) {
		this._time = hours * 3600 * 1000 + minutes * 60 * 1000;
	}
	
	public TimeOfDay(Date date) throws Exception {
		Calendar cal = Calendar.getInstance(LocaleBroker.getLocale());
		cal.setTimeZone(TimeZoneBroker.getTimeZone());
		cal.setTime(date);
		this._time = cal.get(Calendar.HOUR_OF_DAY) * 60;
		this._time += cal.get(Calendar.MINUTE);
		this._time *= 60 * 1000;
		
	}
	
	@Override
	public String toString() {
		return "" + this._time;
	}
	
	public long getValue() {
		return this._time;
	}
	
	public int getDay() throws Exception {
		return this.getCalendar().get(Calendar.DAY_OF_YEAR);
	}
	
	public int getHour() throws Exception {
		return this.getCalendar().get(Calendar.HOUR_OF_DAY);
	}
	
	public int getMinute() throws Exception {
		return this.getCalendar().get(Calendar.MINUTE);
	}
	
	private Calendar getCalendar() throws Exception {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.setTime(new Date(this._time));
		return cal;
	}
	
	public Date getDate(Date date) throws Exception {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeZone(TimeZone.getDefault());
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, this.getHour());
		cal.set(Calendar.MINUTE, this.getMinute());
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	@Override
	public int compareTo(Object other) {
		if (other == null)
			return -1;
		TimeOfDay o = (TimeOfDay) other;
		return (int) (this._time - o.getValue());
	}
	
	@Override
	public boolean equals(Object other) {
		return this.compareTo(other) == 0;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int l_Code = 17;
		l_Code = (int) (37 * l_Code + this.getValue());
		return l_Code;
	}
	
}
