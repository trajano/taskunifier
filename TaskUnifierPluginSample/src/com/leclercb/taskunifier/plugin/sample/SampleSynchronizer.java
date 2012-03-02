package com.leclercb.taskunifier.plugin.sample;

import java.util.Properties;

import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizerMainProgressMessage.ProgressMessageType;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizerUpdatedModelsProgressMessage;
import com.leclercb.taskunifier.gui.plugins.PluginApi;

public class SampleSynchronizer implements Synchronizer {
	
	@Override
	public void publish() throws SynchronizerException {
		
	}
	
	@Override
	public void publish(ProgressMonitor monitor) throws SynchronizerException {
		
	}
	
	@Override
	public void synchronize() throws SynchronizerException {
		this.synchronize(SynchronizerChoice.KEEP_LAST_UPDATED, null);
	}
	
	@Override
	public void synchronize(SynchronizerChoice choice)
			throws SynchronizerException {
		this.synchronize(choice, null);
	}
	
	@Override
	public void synchronize(ProgressMonitor monitor)
			throws SynchronizerException {
		this.synchronize(SynchronizerChoice.KEEP_LAST_UPDATED, monitor);
	}
	
	@Override
	public void synchronize(SynchronizerChoice choice, ProgressMonitor monitor)
			throws SynchronizerException {
		// Synchronize...
		
		if (monitor != null)
			monitor.addMessage(new SynchronizerUpdatedModelsProgressMessage(
					PluginApi.getPlugin(SamplePlugin.ID),
					ProgressMessageType.SYNCHRONIZER_START,
					ModelType.TASK,
					1));
		
		// Synchronize Tasks...
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			
		}
		
		if (monitor != null)
			monitor.addMessage(new SynchronizerUpdatedModelsProgressMessage(
					PluginApi.getPlugin(SamplePlugin.ID),
					ProgressMessageType.SYNCHRONIZER_END,
					ModelType.TASK,
					1));
	}
	
	@Override
	public void loadParameters(Properties properties) {
		
	}
	
	@Override
	public void saveParameters(Properties properties) {
		
	}
	
}
