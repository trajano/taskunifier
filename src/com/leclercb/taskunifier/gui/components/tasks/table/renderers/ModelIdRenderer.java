package com.leclercb.taskunifier.gui.components.tasks.table.renderers;

import com.leclercb.taskunifier.api.models.Model;

public class ModelIdRenderer extends DefaultRenderer {

	public ModelIdRenderer() {

	}

	@Override
	public void setValue(Object value) {
		if (!(value instanceof Model)) {
			this.setText("");
			return;
		}

		this.setText(((Model) value).getModelId().toString());
	}

}
