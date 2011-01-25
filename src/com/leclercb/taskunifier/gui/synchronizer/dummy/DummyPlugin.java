package com.leclercb.taskunifier.gui.synchronizer.dummy;

import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerPlugin;

public class DummyPlugin implements SynchronizerPlugin {
	
	private static String VERSION = "1.0";
	
	public DummyPlugin() {

	}
	
	@Override
	public String getAuthor() {
		return "Benjamin Leclerc";
	}
	
	@Override
	public String getVersion() {
		return VERSION;
	}
	
	@Override
	public SynchronizerApi getSynchronizerApi() {
		return DummyApi.getInstance();
	}
	
}
