package com.leclercb.taskunifier.gui.threads.communicator;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.main.Main;

public class CommunicatorThread extends Thread {
	
	private int port;
	private List<CommunicatorClient> clients;
	
	public CommunicatorThread() {
		this.port = Main.SETTINGS.getIntegerProperty("general.communicator.port");
		this.clients = new ArrayList<CommunicatorClient>();
	}
	
	@Override
	public void interrupt() {
		for (CommunicatorClient client : this.clients) {
			try {
				if (!client.isInterrupted())
					client.interrupt();
			} catch (Throwable t) {
				
			}
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
						serverSocket.accept());
				this.clients.add(client);
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
