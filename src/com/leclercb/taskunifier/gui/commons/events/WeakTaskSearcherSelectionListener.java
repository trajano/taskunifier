package com.leclercb.taskunifier.gui.commons.events;

import java.lang.ref.WeakReference;

public class WeakTaskSearcherSelectionListener implements TaskSearcherSelectionListener {

	private TaskSearcherSelectionChangeSupport support;
	private WeakReference<TaskSearcherSelectionListener> reference;

	public WeakTaskSearcherSelectionListener(TaskSearcherSelectionChangeSupport support, TaskSearcherSelectionListener listener) {
		this.support = support;
		this.reference = new WeakReference<TaskSearcherSelectionListener>(listener);
	}

	@Override
	public void taskSearcherSelectionChange(TaskSearcherSelectionChangeEvent evt){ 
		TaskSearcherSelectionListener listener = reference.get(); 

		if (listener == null) 
			support.removeTaskSearcherSelectionChangeListener(this);
		else 
			listener.taskSearcherSelectionChange(evt); 
	}

	@Override
	public boolean equals(Object obj) {
		if (reference != null)
			return reference.equals(obj);

		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		if (reference != null)
			return reference.hashCode();

		return super.hashCode();
	}

}
