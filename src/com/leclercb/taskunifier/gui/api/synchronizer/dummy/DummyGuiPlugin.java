package com.leclercb.taskunifier.gui.api.synchronizer.dummy;

import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.api.synchronizer.exc.SynchronizerLicenseException;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.translations.Translations;

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
	public int getPluginApiVersion() {
		return Constants.PLUGIN_API_VERSION;
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
	public boolean needsLicense() {
		return false;
	}
	
	@Override
	public boolean checkLicense() throws SynchronizerLicenseException {
		return true;
	}
	
	@Override
	public Connection getConnection() throws SynchronizerException {
		throw new SynchronizerException(
				true,
				Translations.getString("synchronizer.select_an_api"));
	}
	
}
