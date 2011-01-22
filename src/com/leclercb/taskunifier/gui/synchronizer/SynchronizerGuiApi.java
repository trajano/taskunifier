package com.leclercb.taskunifier.gui.synchronizer;

import java.awt.Frame;

import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizerDialog;

public interface SynchronizerGuiApi {
	
	public abstract SynchronizerApi getSynchronizerApi();
	
	public abstract ConfigurationPanel getConfigurationPanel(boolean welcome);
	
	public abstract SynchronizerDialog getSynchronizerDialog(Frame frame);
	
}
