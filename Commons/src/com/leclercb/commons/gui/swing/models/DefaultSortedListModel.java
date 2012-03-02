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
package com.leclercb.commons.gui.swing.models;

import java.util.Comparator;

import javax.swing.AbstractListModel;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.api.utils.SortedArrayList;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DefaultSortedListModel extends AbstractListModel {
	
	private SortedArrayList model;
	
	public DefaultSortedListModel(Comparator comparator) {
		this.model = new SortedArrayList(comparator);
	}
	
	public boolean contains(Object element) {
		return this.model.contains(element);
	}
	
	public int getIndexOf(Object element) {
		Object[] array = this.model.toArray();
		for (int i = 0; i < array.length; i++)
			if (EqualsUtils.equals(array[i], element))
				return i;
		
		return -1;
	}
	
	@Override
	public int getSize() {
		return this.model.size();
	}
	
	@Override
	public Object getElementAt(int index) {
		return this.model.get(index);
	}
	
	public void addElement(Object element) {
		if (this.model.add(element)) {
			int index = this.model.indexOf(element);
			this.fireIntervalAdded(this, index, index);
		}
	}
	
	public void addAll(Object[] elements) {
		for (Object element : elements) {
			this.addElement(element);
		}
	}
	
	public void removeElement(Object element) {
		int index = this.model.indexOf(element);
		if (this.model.remove(element)) {
			this.fireIntervalRemoved(this, index, index);
		}
	}
	
	public void removeAll() {
		int size = this.getSize();
		this.model.clear();
		this.fireIntervalRemoved(this, 0, size);
	}
	
	public void fireStructureChanged(Object source) {
		this.fireIntervalAdded(this, 0, this.getSize() - 1);
	}
	
}
