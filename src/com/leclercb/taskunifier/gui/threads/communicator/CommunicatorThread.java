package com.leclercb.taskunifier.gui.threads.communicator;

import java.io.IOException;
import java.net.ServerSocket;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.main.Main;

public class CommunicatorThread extends Thread {
	
	public CommunicatorThread() {
		
	}
	
	@Override
	public void run() {
		int port = Main.SETTINGS.getIntegerProperty("general.communicator.port");
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(port);
			
			GuiLogger.getLogger().info(
					"Communicator initialized on port " + port);
			
			while (true)
				new CommunicatorClient(serverSocket.accept()).start();
		} catch (Exception e) {
			GuiLogger.getLogger().warning(
					"Cannot initialize communicator on port " + port);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				GuiLogger.getLogger().warning(
						"Cannot close communicator on port " + port);
			}
		}
	}
	
}
