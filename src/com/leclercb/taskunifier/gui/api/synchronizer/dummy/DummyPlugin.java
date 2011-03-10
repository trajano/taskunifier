package com.leclercb.taskunifier.gui.api.synchronizer.dummy;

import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerPlugin;

public class DummyPlugin implements SynchronizerPlugin {
	
	private static String VERSION = "1.0";
	
	public DummyPlugin() {

	}
	
	@Override
	public String getId() {
		return "0";
	}
	
	@Override
	public String getName() {
		return "No Service Plugin";
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
