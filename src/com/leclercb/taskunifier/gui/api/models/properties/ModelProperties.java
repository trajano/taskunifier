package com.leclercb.taskunifier.gui.api.models.properties;

import com.leclercb.taskunifier.api.models.Model;

public interface ModelProperties<M extends Model> {
	
	public abstract Object getProperty(M model);
	
	public abstract void setProperty(M model, Object value);
	
	public abstract Class<?> getType();
	
}
