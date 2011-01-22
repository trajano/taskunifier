package com.leclercb.taskunifier.gui.synchronizer.toodledo;

import java.awt.Frame;

import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.api.synchronizer.toodledo.ToodledoApi;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizerDialog;
import com.leclercb.taskunifier.gui.synchronizer.SynchronizerGuiApi;

public class ToodledoSynchronizerGuiApi implements SynchronizerGuiApi {
	
	public static final ToodledoSynchronizerGuiApi INSTANCE = new ToodledoSynchronizerGuiApi();
	
	@Override
	public SynchronizerApi getSynchronizerApi() {
		return ToodledoApi.INSTANCE;
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
