package com.leclercb.taskunifier.gui.communicator;

import org.jgroups.ChannelException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.ReceiverAdapter;

import com.leclercb.commons.gui.logger.GuiLogger;

public class Communicator extends ReceiverAdapter implements Receiver {
	
	private static final String GROUP = "TaskUnifier";
	
	private boolean started;
	private JChannel channel;
	
	public Communicator() {
		this.started = false;
		this.channel = null;
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public void start() {
		if (this.started)
			return;
		
		try {
			this.channel = new JChannel();
			this.channel.setReceiver(this);
			
			this.channel.connect(GROUP);
			this.started = true;
		} catch (ChannelException e) {
			this.started = false;
			this.channel = null;
			
			GuiLogger.getLogger().warning(
					"Communicator \"" + GROUP + "\": " + e.getMessage());
		}
	}
	
	public void stop() {
		if (!this.started)
			return;
		
		this.channel.disconnect();
		this.channel.close();
		
		this.started = false;
		this.channel = null;
	}
	
	@Override
	public void receive(Message msg) {
		System.out.println("received msg from "
				+ msg.getSrc()
				+ ": "
				+ msg.getObject());
	}
	
}
