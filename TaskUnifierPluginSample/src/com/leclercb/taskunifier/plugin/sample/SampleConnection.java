package com.leclercb.taskunifier.plugin.sample;

import java.util.Properties;

import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;

public class SampleConnection implements Connection {
	
	@Override
	public void connect() throws SynchronizerException {
		
	}
	
	@Override
	public void disconnect() {
		
	}
	
	@Override
	public boolean isConnected() {
		return true;
	}
	
	@Override
	public void loadParameters(Properties properties) {
		
	}
	
	@Override
	public void saveParameters(Properties properties) {
		
	}
	
}
