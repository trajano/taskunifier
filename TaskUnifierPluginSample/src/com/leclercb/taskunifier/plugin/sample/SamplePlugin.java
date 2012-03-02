package com.leclercb.taskunifier.plugin.sample;

import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerPlugin;

public class SamplePlugin implements SynchronizerPlugin {
	
	public static final String ID = "999";
	public static final String NAME = "Sample Plugin";
	public static final String AUTHOR = "Benjamin Leclerc";
	public static final String VERSION = "1.0";
	
	public SamplePlugin() {
		
	}
	
	@Override
	public boolean isPublisher() {
		return false;
	}
	
	@Override
	public boolean isSynchronizer() {
		return true;
	}
	
	@Override
	public String getId() {
		return ID;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public String getAuthor() {
		return AUTHOR;
	}
	
	@Override
	public String getVersion() {
		return VERSION;
	}
	
	@Override
	public SynchronizerApi getSynchronizerApi() {
		return SampleApi.getInstance();
	}
	
}
