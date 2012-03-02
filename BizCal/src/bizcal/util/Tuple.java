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
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a number of values.
 * 
 * @author Fredrik Bertilsson
 */
public class Tuple implements Comparable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List itsContent;
	private boolean itsNullFlag = false;
	
	public Tuple() {
		this.itsContent = new ArrayList();
	}
	
	public Tuple(int size) {
		this.itsContent = new ArrayList(size);
	}
	
	public Tuple(Tuple tuple) {
		this.itsContent = new ArrayList(tuple.itsContent);
	}
	
	public Tuple(Object elementA) {
		this.itsContent = new ArrayList(1);
		this.add(elementA);
	}
	
	public Tuple(Object aElementA, Object aElementB) {
		this.itsContent = new ArrayList(2);
		this.add(aElementA);
		this.add(aElementB);
	}
	
	public Tuple(Object aElementA, Object aElementB, Object aElementC) {
		this.itsContent = new ArrayList(3);
		this.add(aElementA);
		this.add(aElementB);
		this.add(aElementC);
	}
	
	public Tuple(
			Object aElementA,
			Object aElementB,
			Object aElementC,
			Object aElementD) {
		this.itsContent = new ArrayList(4);
		this.add(aElementA);
		this.add(aElementB);
		this.add(aElementC);
		this.add(aElementD);
	}
	
	public Tuple(
			Object aElementA,
			Object aElementB,
			Object aElementC,
			Object aElementD,
			Object aElementE) {
		this.itsContent = new ArrayList(5);
		this.add(aElementA);
		this.add(aElementB);
		this.add(aElementC);
		this.add(aElementD);
		this.add(aElementE);
	}
	
	public Tuple(Tuple tuple, Object element) {
		this.itsContent = new ArrayList(tuple.itsContent);
		this.itsContent.add(element);
	}
	
	private Tuple(Object[] content) {
		this.itsContent = new ArrayList(content.length);
		for (int i = 0; i < content.length; i++)
			this.itsContent.add(content[i]);
	}
	
	public void setValueAt(int index, Comparable aElement) throws Exception {
		this.itsContent.set(index, aElement);
	}
	
	@Override
	public boolean equals(Object anOther) {
		if (anOther instanceof Tuple) {
			Tuple other = (Tuple) anOther;
			Object element;
			Object otherElement;
			int tmp;
			int iOther = 0;
			for (int i = 0; i < this.size(); i++) {
				element = this.elementAt(i);
				if (other.size() == iOther)
					return false;
				otherElement = other.elementAt(iOther);
				if (!NullSafe.equals(element, otherElement))
					return false;
				iOther++;
			}
			if (iOther < other.size())
				return false;
			return true;
		}
		return false;
	}
	
	@Override
	public int compareTo(Object anOther) {
		if (anOther instanceof Tuple) {
			Tuple other = (Tuple) anOther;
			Comparable element;
			Comparable otherElement;
			int tmp;
			int iOther = 0;
			for (int i = 0; i < this.size(); i++) {
				element = (Comparable) this.elementAt(i);
				if (other.size() == iOther)
					return 1;
				otherElement = (Comparable) other.elementAt(iOther);
				if (element == null && otherElement != null)
					return -1;
				if (element == null && otherElement == null)
					return 0;
				if (element != null && otherElement == null)
					return 1;
				tmp = element.compareTo(otherElement);
				if (tmp != 0)
					return tmp;
				iOther++;
			}
			if (iOther < other.size())
				return 1;
			return 0;
		}
		return -1;
	}
	
	public boolean hasNullValues() {
		return this.itsNullFlag;
	}
	
	public Object elementAt(int aIndex) {
		return this.itsContent.get(aIndex);
	}
	
	public int size() {
		return this.itsContent.size();
	}
	
	public void add(Object aElement) {
		if (aElement == null)
			this.itsNullFlag = true;
		this.itsContent.add(aElement);
	}
	
	/*
	 * public String toString()
	 * {
	 * StringBuffer str = new StringBuffer();
	 * str.append("(");
	 * for (int i=0; i < size(); i++) {
	 * str.append(itsContent[i]);
	 * if (i+1 < size())
	 * str.append(", ");
	 * }
	 * str.append(")");
	 * return str.toString();
	 * }
	 */
	
	@Override
	public int hashCode() {
		int result = 0;
		for (int i = 0; i < this.size(); i++)
			if (this.itsContent.get(i) != null)
				result += this.itsContent.get(i).hashCode();
		return result;
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < this.itsContent.size(); i++) {
			if (this.itsContent.get(i) == null)
				str.append("null");
			else
				str.append(this.itsContent.get(i).toString());
			str.append("#");
		}
		return str.toString();
	}
	
	public List toList() {
		return this.itsContent;
	}
	
}
