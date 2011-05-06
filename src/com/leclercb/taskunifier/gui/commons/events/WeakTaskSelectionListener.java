package com.leclercb.taskunifier.gui.commons.events;

import java.lang.ref.WeakReference;

public class WeakTaskSelectionListener implements TaskSelectionListener {
	
	private TaskSelectionChangeSupport support;
	private WeakReference<TaskSelectionListener> reference;
	
	public WeakTaskSelectionListener(
			TaskSelectionChangeSupport support,
			TaskSelectionListener listener) {
		this.support = support;
		this.reference = new WeakReference<TaskSelectionListener>(listener);
	}
	
	@Override
	public void taskSelectionChange(TaskSelectionChangeEvent evt) {
		TaskSelectionListener listener = this.reference.get();
		
		if (listener == null)
			this.support.removeTaskSelectionChangeListener(this);
		else
			listener.taskSelectionChange(evt);
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
