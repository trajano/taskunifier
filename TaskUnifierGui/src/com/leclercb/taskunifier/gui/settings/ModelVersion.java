package com.leclercb.taskunifier.gui.settings;

import com.leclercb.taskunifier.api.models.DeprecatedModelId;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelFactory;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.utils.ModelFactoryUtils;

@SuppressWarnings("deprecation")
public final class ModelVersion {
	
	private ModelVersion() {
		
	}
	
	public static void updateModels() {
		for (ModelType type : ModelType.values()) {
			ModelFactory<?, ?, ?, ?> factory = ModelFactoryUtils.getFactory(type);
			for (Object object : factory.getList()) {
				Model model = (Model) object;
				
				if (model.getModelId() instanceof DeprecatedModelId)
					if (!((DeprecatedModelId) model.getModelId()).isNew())
						if (model.getModelReferenceId("toodledo") == null)
							model.addModelReferenceId(
									"toodledo",
									model.getModelId().getId());
			}
		}
	}
	
}
