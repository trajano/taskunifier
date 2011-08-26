package com.leclercb.taskunifier.gui.threads.communicator;

import java.util.logging.Level;

import org.jgroups.Channel;
import org.jgroups.ChannelClosedException;
import org.jgroups.ChannelNotConnectedException;
import org.jgroups.JChannel;
import org.jgroups.Message;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.communicator.messages.AddTaskMessage;

public class CommunicatorThread extends Thread {
	
	private static final String GROUP = "TaskUnifier";
	
	private Channel channel;
	
	public CommunicatorThread() {

	}
	
	@Override
	public void run() {
		try {
			this.channel = new JChannel();
			this.channel.connect(GROUP);
			GuiLogger.getLogger().info("Connected to \"" + GROUP + "\" group");
		} catch (Throwable t) {
			t.printStackTrace();
			
			GuiLogger.getLogger().warning(
					"Cannot connect to \"" + GROUP + "\" group");
			return;
		}
		
		while (true) {
			try {
				@SuppressWarnings("deprecation")
				Object message = this.channel.receive(0);
				
				if (message instanceof Message) {
					Message m = (Message) message;
					
					if (m.getObject() instanceof AddTaskMessage) {
						AddTaskMessageExecutor.execute((AddTaskMessage) m.getObject());
					}
				}
			} catch (ChannelClosedException e) {

			} catch (ChannelNotConnectedException e) {

			} catch (Exception e) {
				GuiLogger.getLogger().log(
						Level.WARNING,
						"Error while receiving message from \""
								+ GROUP
								+ "\" group",
						e);
			}
		}
	}
	
	@Override
	public void interrupt() {
		super.interrupt();
		
		if (this.channel != null) {
			this.channel.disconnect();
			this.channel.close();
			GuiLogger.getLogger().info(
					"Disconnected from \"" + GROUP + "\" group");
		}
	}
	
}
