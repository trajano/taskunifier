package com.leclercb.taskunifier.gui.communicator;

import org.jgroups.ChannelException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.ReceiverAdapter;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.communicator.messages.Executor;

public class Communicator extends ReceiverAdapter implements Receiver {
	
	private static final String GROUP = "TaskUnifier";
	
	private boolean started;
	private JChannel channel;
	
	public Communicator() {
		this.started = false;
		this.channel = null;
	}
	
	protected JChannel getChannel() {
		return this.channel;
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
		this.stop(true);
	}
	
	protected void stop(boolean closeChannel) {
		if (!this.started)
			return;
		
		this.channel.disconnect();
		
		if (closeChannel)
			this.channel.close();
		
		this.started = false;
		this.channel = null;
	}
	
	@Override
	public void receive(Message msg) {
		Object o = msg.getObject();
		
		if (o instanceof Executor)
			((Executor) o).execute();
	}
	
}
