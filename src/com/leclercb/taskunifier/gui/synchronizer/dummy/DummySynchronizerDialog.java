package com.leclercb.taskunifier.gui.synchronizer.dummy;

import java.awt.Frame;

import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.gui.components.synchronize.SynchronizerDialog;

public class DummySynchronizerDialog extends SynchronizerDialog {
	
	public DummySynchronizerDialog(Frame frame) {
		super(frame);
	}
	
	@Override
	protected Connection getConnection() throws SynchronizerException {
		throw new SynchronizerException(true, 
				"You must select an API in order to synchronize your tasks");
	}
	
}
