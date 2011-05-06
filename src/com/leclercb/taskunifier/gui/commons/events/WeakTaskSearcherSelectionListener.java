package com.leclercb.taskunifier.gui.commons.events;

import java.lang.ref.WeakReference;

public class WeakTaskSearcherSelectionListener implements TaskSearcherSelectionListener {
	
	private TaskSearcherSelectionChangeSupport support;
	private WeakReference<TaskSearcherSelectionListener> reference;
	
	public WeakTaskSearcherSelectionListener(
			TaskSearcherSelectionChangeSupport support,
			TaskSearcherSelectionListener listener) {
		this.support = support;
		this.reference = new WeakReference<TaskSearcherSelectionListener>(
				listener);
	}
	
	@Override
	public void taskSearcherSelectionChange(TaskSearcherSelectionChangeEvent evt) {
		TaskSearcherSelectionListener listener = this.reference.get();
		
		if (listener == null)
			this.support.removeTaskSearcherSelectionChangeListener(this);
		else
			listener.taskSearcherSelectionChange(evt);
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
