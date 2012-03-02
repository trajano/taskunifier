package com.leclercb.commons.api.event.propertychange;

public class PropertyChangeSupport extends java.beans.PropertyChangeSupport implements PropertyChangeSupported {
	
	public PropertyChangeSupport(Object source) {
		super(source);
	}
	
}
