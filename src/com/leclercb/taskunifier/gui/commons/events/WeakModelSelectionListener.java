package com.leclercb.taskunifier.gui.commons.events;

import java.lang.ref.WeakReference;

import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class WeakModelSelectionListener implements ModelSelectionListener {
	
	private ModelSelectionChangeSupport support;
	private WeakReference<ModelSelectionListener> reference;
	
	public WeakModelSelectionListener(
			ModelSelectionChangeSupport support,
			ModelSelectionListener listener) {
		this.support = support;
		this.reference = new WeakReference<ModelSelectionListener>(listener);
	}
	
	@Override
	public void modelSelectionChange(ModelSelectionChangeEvent evt) {
		ModelSelectionListener listener = this.reference.get();
		
		if (listener == null)
			this.support.removeModelSelectionChangeListener(this);
		else
			listener.modelSelectionChange(evt);
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
