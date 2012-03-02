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

import java.io.Serializable;

/**
 * An Interval represents a peace of something. Can be a time ore some numbers.
 * Restriction is, that the "something" implements Comparable.
 * 
 * @author Fredrik Bertilsson
 * @credits martin.heinemann(at)tudor.lu
 * 
 * @param <T>
 * 
 */
public class Interval implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Comparable start;
	private Comparable end;
	private boolean includeStart = true;
	private boolean includeEnd = false;
	
	public Interval(Comparable start, Comparable end) {
		this.start = start;
		this.end = end;
	}
	
	protected Interval() {}
	
	public Comparable getStart() {
		return this.start;
	}
	
	public void setStart(Comparable start) {
		this.start = start;
	}
	
	public Comparable getEnd() {
		return this.end;
	}
	
	public void setEnd(Comparable end) {
		this.end = end;
	}
	
	/**
	 * @return Returns the includeEnd.
	 */
	public boolean isIncludeEnd() {
		return this.includeEnd;
	}
	
	/**
	 * @param includeEnd
	 *            The includeEnd to set.
	 */
	public void setIncludeEnd(boolean includeEnd) {
		this.includeEnd = includeEnd;
	}
	
	/**
	 * @return Returns the includeStart.
	 */
	public boolean isIncludeStart() {
		return this.includeStart;
	}
	
	/**
	 * @param includeStart
	 *            The includeStart to set.
	 */
	public void setIncludeStart(boolean includeStart) {
		this.includeStart = includeStart;
	}
	
	/**
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean contains(Comparable obj) {
		if (this.start != null) {
			if (obj.compareTo(this.getStart()) < 0)
				return false;
			if (!this.includeStart && obj.compareTo(this.getStart()) == 0)
				return false;
		}
		if (this.end != null) {
			if (obj.compareTo(this.getEnd()) > 0)
				return false;
			if (!this.includeEnd && obj.compareTo(this.getEnd()) == 0)
				return false;
		}
		return true;
	}
	
	/**
	 * @param interval
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean contains(Interval interval) {
		if (this.start != null) {
			int cmp = interval.getStart().compareTo(this.start);
			if (cmp < 0)
				return false;
			if (cmp == 0) {
				if (!this.includeStart && interval.isIncludeStart())
					return false;
			}
		}
		if (this.end != null) {
			if (interval.getEnd() == null)
				return false;
			int cmp = interval.getEnd().compareTo(this.end);
			if (cmp > 0)
				return false;
			if (cmp == 0) {
				if (!this.includeEnd && interval.isIncludeEnd())
					return false;
			}
		}
		return true;
	}
	
	/**
	 * @param interval
	 * @return
	 */
	public boolean overlap(Interval interval) {
		Interval tmpInterv = new Interval(this.getStart(), this.getEnd());
		tmpInterv.setIncludeStart(false);
		tmpInterv.setIncludeEnd(false);
		if (tmpInterv.contains(interval.getStart()))
			return true;
		if (tmpInterv.contains(interval.getEnd()))
			return true;
		if (interval.contains(this.getStart())
				&& interval.contains(this.getEnd()))
			return true;
		if (NullSafe.equals(interval.getStart(), this.getStart())
				&& NullSafe.equals(interval.getEnd(), this.getEnd()))
			return true;
		return false;
	}
	
	/**
	 * @param interval
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Interval intersection(Interval interval) throws Exception {
		Comparable start = this.getStart();
		Comparable end = this.getEnd();
		if (interval.getStart().compareTo(start) > 0)
			start = interval.getStart();
		if (interval.getEnd().compareTo(end) < 0)
			end = interval.getEnd();
		if (start.compareTo(end) > 0)
			return null;
		return new Interval(start, end);
	}
	
	/**
	 * @param interval
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Interval union(Interval interval) throws Exception {
		Comparable start = this.getStart();
		Comparable end = this.getEnd();
		if (interval.getStart().compareTo(start) < 0)
			start = interval.getStart();
		if (interval.getEnd().compareTo(end) > 0)
			end = interval.getEnd();
		return new Interval(start, end);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		try {
			if (other instanceof Interval) {
				Interval interval = (Interval) other;
				return NullSafe.equals(this.getStart(), interval.getStart())
						&& NullSafe.equals(this.getEnd(), interval.getEnd());
			} else
				return false;
		} catch (Exception e) {
			throw BizcalException.create(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		assert false : "hashCode not designed";
		
		return 42;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			StringBuffer str = new StringBuffer();
			if (this.getStart() != null)
				str.append(this.getStart().toString());
			str.append(" - ");
			if (this.getEnd() != null)
				str.append(this.getEnd().toString());
			return str.toString();
		} catch (Exception e) {
			throw BizcalException.create(e);
		}
	}
	
}
