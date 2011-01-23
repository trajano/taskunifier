package com.leclercb.taskunifier.gui.synchronizer.dummy;

import java.awt.Frame;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizerDialog;
import com.leclercb.taskunifier.gui.synchronizer.SynchronizerGuiPlugin;

public class DummyGuiPlugin extends DummyPlugin implements SynchronizerGuiPlugin {
	
	private static String VERSION = "1.0";
	
	public DummyGuiPlugin() {

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
	public SynchronizerDialog getSynchronizerDialog(Frame frame) {
		return new DummySynchronizerDialog(frame);
	}
	
}
