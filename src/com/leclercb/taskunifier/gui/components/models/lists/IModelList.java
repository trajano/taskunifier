package com.leclercb.taskunifier.gui.components.models.lists;

import com.leclercb.taskunifier.api.models.Model;

public interface IModelList {
	
	public abstract Model getSelectedModel();
	
	public abstract void setSelectedModel(Model model);
	
}
