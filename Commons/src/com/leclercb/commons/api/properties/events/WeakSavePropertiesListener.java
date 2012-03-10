package com.leclercb.commons.api.properties.events;

import java.lang.ref.WeakReference;

public class WeakSavePropertiesListener implements SavePropertiesListener {
	
	private SavePropertiesSupported support;
	private WeakReference<SavePropertiesListener> reference;
	
	public WeakSavePropertiesListener(
			SavePropertiesSupported support,
			SavePropertiesListener listener) {
		this.support = support;
		this.reference = new WeakReference<SavePropertiesListener>(listener);
	}
	
	@Override
	public void saveProperties() {
		SavePropertiesListener listener = this.reference.get();
		
		if (listener == null)
			this.support.removeSavePropertiesListener(this);
		else
			listener.saveProperties();
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
