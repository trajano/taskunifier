package com.leclercb.taskunifier.gui.synchronizer.dummy;

import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerPlugin;

public class DummyPlugin implements SynchronizerPlugin {
	
	public DummyPlugin() {

	}
	
	@Override
	public SynchronizerApi getSynchronizerApi() {
		return DummyApi.getInstance();
	}
	
}
