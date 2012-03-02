package com.leclercb.taskunifier.plugin.sample;

import java.util.Properties;

import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;

public class SampleApi extends SynchronizerApi {
	
	private static SampleApi INSTANCE;
	
	public static final SampleApi getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SampleApi();
		
		return INSTANCE;
	}
	
	public SampleApi() {
		super("SAMPLE", "Sample", "http://www.taskunifier.com");
	}
	
	@Override
	public Connection getConnection(Properties properties)
			throws SynchronizerException {
		return new SampleConnection();
	}
	
	@Override
	public Synchronizer getSynchronizer(
			Properties properties,
			Connection connection) throws SynchronizerException {
		return new SampleSynchronizer();
	}
	
	@Override
	public void resetConnectionParameters(Properties properties) {
		
	}
	
	@Override
	public void resetSynchronizerParameters(Properties properties) {
		
	}
	
}
