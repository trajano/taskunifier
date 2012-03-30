package com.leclercb.taskunifier.gui.components.configuration;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.fields.lists.TaskPostponeListFieldType;

public class TaskPostponeListConfigurationPanel extends DefaultConfigurationPanel {
	
	public TaskPostponeListConfigurationPanel(ConfigurationGroup configuration) {
		super(configuration);
		this.initialize();
		this.pack();
	}
	
	private void initialize() {
		this.addField(new ConfigurationField(
				"TASK_POSTPONE_LIST",
				null,
				true,
				new TaskPostponeListFieldType()));
	}
	
}
