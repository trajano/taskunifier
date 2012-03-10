package com.leclercb.commons.api.event.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;

public class WeakActionListener implements ActionListener {
	
	private ActionSupported support;
	private WeakReference<ActionListener> reference;
	
	public WeakActionListener(ActionSupported support, ActionListener listener) {
		this.support = support;
		this.reference = new WeakReference<ActionListener>(listener);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		ActionListener listener = this.reference.get();
		
		if (listener == null)
			this.support.removeActionListener(this);
		else
			listener.actionPerformed(evt);
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
