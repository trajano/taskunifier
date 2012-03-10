package com.leclercb.commons.api.properties.events;

import java.lang.ref.WeakReference;

public class WeakReloadPropertiesListener implements ReloadPropertiesListener {
	
	private ReloadPropertiesSupported support;
	private WeakReference<ReloadPropertiesListener> reference;
	
	public WeakReloadPropertiesListener(
			ReloadPropertiesSupported support,
			ReloadPropertiesListener listener) {
		this.support = support;
		this.reference = new WeakReference<ReloadPropertiesListener>(listener);
	}
	
	@Override
	public void reloadProperties() {
		ReloadPropertiesListener listener = this.reference.get();
		
		if (listener == null)
			this.support.removeReloadPropertiesListener(this);
		else
			listener.reloadProperties();
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
