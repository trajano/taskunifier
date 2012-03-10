package com.leclercb.commons.api.event.listchange;

import java.lang.ref.WeakReference;

public class WeakListChangeListener implements ListChangeListener {
	
	private ListChangeSupported support;
	private WeakReference<ListChangeListener> reference;
	
	public WeakListChangeListener(
			ListChangeSupported support,
			ListChangeListener listener) {
		this.support = support;
		this.reference = new WeakReference<ListChangeListener>(listener);
	}
	
	@Override
	public void listChange(ListChangeEvent evt) {
		ListChangeListener listener = this.reference.get();
		
		if (listener == null)
			this.support.removeListChangeListener(this);
		else
			listener.listChange(evt);
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
