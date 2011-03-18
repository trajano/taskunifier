package com.leclercb.taskunifier.gui.api.synchronizer;

import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerPlugin;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.gui.api.synchronizer.exc.SynchronizerLicenseException;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;

public interface SynchronizerGuiPlugin extends SynchronizerPlugin {
	
	public abstract ConfigurationPanel getConfigurationPanel(boolean welcome);
	
	public abstract boolean needsLicense();
	
	public abstract boolean checkLicense() throws SynchronizerLicenseException;
	
	public abstract Connection getConnection() throws SynchronizerException;
	
}
