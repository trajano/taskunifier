package com.leclercb.commons.api.glazedlists;

import ca.odell.glazedlists.event.ListEventListener;

public interface ListEventSupported<E> {
	
	public abstract void addListEventListener(ListEventListener<E> listener);
	
	public abstract void removeListEventListener(ListEventListener<E> listener);
	
}
