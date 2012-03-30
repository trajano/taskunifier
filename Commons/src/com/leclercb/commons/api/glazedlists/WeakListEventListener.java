package com.leclercb.commons.api.glazedlists;

import java.lang.ref.WeakReference;

import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

public class WeakListEventListener<E> implements ListEventListener<E> {
	
	private ListEventSupported<E> support;
	private WeakReference<ListEventListener<E>> reference;
	
	public WeakListEventListener(
			ListEventSupported<E> support,
			ListEventListener<E> listener) {
		this.support = support;
		this.reference = new WeakReference<ListEventListener<E>>(listener);
	}
	
	@Override
	public void listChanged(ListEvent<E> event) {
		ListEventListener<E> listener = this.reference.get();
		
		if (listener == null)
			this.support.removeListEventListener(this);
		else
			listener.listChanged(event);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this.reference != null)
			return this.reference.equals(obj);
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		if (this.reference != null)
			return this.reference.hashCode();
		
		return super.hashCode();
	}
	
}
