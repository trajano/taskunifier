package com.leclercb.taskunifier.gui.api.synchronizer.dummy;

import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;

public class DummyGuiPlugin extends DummyPlugin implements SynchronizerGuiPlugin {
	
	private static DummyGuiPlugin INSTANCE;
	
	public static DummyGuiPlugin getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DummyGuiPlugin();
		
		return INSTANCE;
	}
	
	private static String VERSION = "1.0";
	
	private DummyGuiPlugin() {

	}
	
	@Override
	public String getVersion() {
		return VERSION;
	}
	
	@Override
	public ConfigurationPanel getConfigurationPanel(boolean welcome) {
		return new DummyConfigurationPanel(welcome);
	}
	
	@Override
	public Connection getConnection() throws SynchronizerException {
		throw new SynchronizerException(
				true,
				"You must select an API in order to synchronize your tasks");
	}
	
}
