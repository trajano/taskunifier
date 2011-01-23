package com.leclercb.taskunifier.gui.synchronizer.toodledo;

import java.awt.Frame;

import com.leclercb.taskunifier.api.synchronizer.toodledo.ToodledoPlugin;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizerDialog;
import com.leclercb.taskunifier.gui.synchronizer.SynchronizerGuiPlugin;

public class ToodledoGuiPlugin extends ToodledoPlugin implements SynchronizerGuiPlugin {
	
	private static String VERSION = "0.1";
	
	public ToodledoGuiPlugin() {

	}
	
	@Override
	public String getVersion() {
		return VERSION;
	}
	
	@Override
	public ConfigurationPanel getConfigurationPanel(boolean welcome) {
		return new ToodledoConfigurationPanel(welcome);
	}
	
	@Override
	public SynchronizerDialog getSynchronizerDialog(Frame frame) {
		return new ToodledoSynchronizerDialog(frame);
	}
	
}
