package com.leclercb.taskunifier.gui.threads.communicator;

import java.io.IOException;
import java.net.ServerSocket;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.main.Main;

public class CommunicatorThread extends Thread {
	
	private int port;
	private ThreadGroup group;
	
	public CommunicatorThread() {
		super("CommunicatorThread");
		
		this.port = Main.SETTINGS.getIntegerProperty("general.communicator.port");
		this.group = new ThreadGroup("CommunicatorGroup");
	}
	
	@Override
	public void interrupt() {
		try {
			this.group.interrupt();
		} catch (Throwable t) {
			
		}
		
		GuiLogger.getLogger().info("Communicator closed on port " + this.port);
		
		super.interrupt();
	}
	
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(this.port);
			
			GuiLogger.getLogger().info(
					"Communicator initialized on port " + this.port);
			
			while (true) {
				CommunicatorClient client = new CommunicatorClient(
						this.group,
						serverSocket.accept());
				
				client.start();
			}
		} catch (Exception e) {
			GuiLogger.getLogger().warning(
					"Cannot initialize communicator on port " + this.port);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				GuiLogger.getLogger().warning(
						"Cannot close communicator on port " + this.port);
			}
		}
	}
	
}
