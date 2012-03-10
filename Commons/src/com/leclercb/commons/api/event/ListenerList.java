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
package com.leclercb.commons.api.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.ReadOnlyIterator;

/**
 * Manage a listener list and provide fire actions.
 * 
 * @author Benjamin Leclerc
 * 
 * @param <T>
 *            the listener type
 */
public class ListenerList<T> implements Iterable<T> {
	
	/**
	 * Listener list.
	 */
	private List<T> listeners;
	
	public ListenerList() {
		
	}
	
	/**
	 * Adds a listener into the listener list.
	 * 
	 * @param listener
	 *            listener to add
	 * @throws IllegalArgumentException
	 *             if the listener is null
	 */
	public void addListener(T listener) {
		CheckUtils.isNotNull(listener);
		
		synchronized (this) {
			if (this.listeners == null) {
				this.listeners = new ArrayList<T>();
				this.listeners.add(listener);
				return;
			}
			
			ArrayList<T> newList = new ArrayList<T>(this.listeners);
			newList.add(listener);
			this.listeners = newList;
		}
	}
	
	/**
	 * Removes a listener from the listener list.
	 * 
	 * @param listener
	 *            listener to remove
	 * @throws IllegalArgumentException
	 *             if the listener is null
	 */
	public void removeListener(T listener) {
		CheckUtils.isNotNull(listener);
		
		synchronized (this) {
			if (this.listeners == null || !this.listeners.contains(listener)) {
				return;
			}
			
			ArrayList<T> newList = new ArrayList<T>(this.listeners);
			newList.remove(listener);
			this.listeners = (newList.size() > 0 ? newList : null);
		}
	}
	
	/**
	 * Returns the listeners contained in the listener list.
	 * 
	 * @return listener list
	 */
	public List<T> getListeners() {
		synchronized (this) {
			if (this.listeners == null) {
				return Collections.emptyList();
			}
			
			return new ArrayList<T>(this.listeners);
		}
	}
	
	/**
	 * Returns an iterator of the listener list.
	 * 
	 * @return iterator of the listener list
	 */
	@Override
	public Iterator<T> iterator() {
		synchronized (this) {
			if (this.listeners == null) {
				List<T> emptyList = Collections.emptyList();
				return emptyList.iterator();
			}
			
			return new ReadOnlyIterator<T>(this.listeners.iterator());
		}
	}
	
}
