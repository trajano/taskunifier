package com.leclercb.taskunifier.gui.threads.communicator.progress;

import com.leclercb.commons.api.progress.DefaultProgressMessage;

public class CommunicatorDefaultProgressMessage extends DefaultProgressMessage implements CommunicatorProgressMessage {
	
	public CommunicatorDefaultProgressMessage(String message) {
		super(message);
	}
	
}
