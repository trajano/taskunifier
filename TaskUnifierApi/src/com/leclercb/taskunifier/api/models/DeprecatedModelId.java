package com.leclercb.taskunifier.api.models;

@Deprecated
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
