package com.leclercb.taskunifier.api.models;

import com.leclercb.taskunifier.api.models.beans.converters.ModelIdConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@Deprecated
@XStreamAlias("modelid")
@XStreamConverter(ModelIdConverter.class)
public class DeprecatedModelId extends ModelId {
	
	private boolean isNew;
	
	public DeprecatedModelId() {
		super();
	}
	
	public DeprecatedModelId(String id, boolean isNew) {
		super(id);
		this.isNew = isNew;
	}
	
	public boolean isNew() {
		return this.isNew;
	}
	
}
