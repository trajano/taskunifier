package com.leclercb.taskunifier.gui.utils;

import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.api.models.TaskFactory;

public final class ModelUtils {
	
	private ModelUtils() {

	}
	
	public static Model getModel(ModelType modelType, ModelId modelId) {
		if (modelType == null || modelId == null)
			return null;
		
		switch (modelType) {
			case CONTEXT:
				return ContextFactory.getInstance().get(modelId);
			case FOLDER:
				return FolderFactory.getInstance().get(modelId);
			case GOAL:
				return GoalFactory.getInstance().get(modelId);
			case LOCATION:
				return LocationFactory.getInstance().get(modelId);
			case NOTE:
				return NoteFactory.getInstance().get(modelId);
			case TASK:
				return TaskFactory.getInstance().get(modelId);
			default:
				return null;
		}
	}
	
	public static ModelFactory<?, ?> getFactory(ModelType modelType) {
		if (modelType == null)
			return null;
		
		switch (modelType) {
			case CONTEXT:
				return ContextFactory.getInstance();
			case FOLDER:
				return FolderFactory.getInstance();
			case GOAL:
				return GoalFactory.getInstance();
			case LOCATION:
				return LocationFactory.getInstance();
			case NOTE:
				return NoteFactory.getInstance();
			case TASK:
				return TaskFactory.getInstance();
			default:
				return null;
		}
	}
	
}
