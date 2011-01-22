package com.leclercb.taskunifier.gui.synchronizer;

import java.awt.Frame;

import com.leclercb.taskunifier.api.synchronizer.SynchronizerPlugin;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizerDialog;

public interface SynchronizerGuiPlugin extends SynchronizerPlugin {
	
	public abstract ConfigurationPanel getConfigurationPanel(boolean welcome);
	
	public abstract SynchronizerDialog getSynchronizerDialog(Frame frame);
	
}
