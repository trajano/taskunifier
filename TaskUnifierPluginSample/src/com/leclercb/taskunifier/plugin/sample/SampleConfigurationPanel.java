package com.leclercb.taskunifier.plugin.sample;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;

public class SampleConfigurationPanel extends DefaultConfigurationPanel {
	
	public SampleConfigurationPanel(
			ConfigurationGroup configuration,
			boolean welcome) {
		super(configuration);
		
		this.initialize(welcome);
		this.pack();
	}
	
	@Override
	public void saveAndApplyConfig() {
		
	}
	
	private void initialize(boolean welcome) {
		this.addField(new ConfigurationField(
				"SAMPLE_LABEL",
				null,
				new ConfigurationFieldType.Label("My Sample Plugin")));
	}
	
}
