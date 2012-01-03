package com.leclercb.taskunifier.gui.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;

import com.leclercb.taskunifier.gui.api.models.beans.ComBean;

public final class CommunicatorUtils {
	
	private CommunicatorUtils() {
		
	}
	
	public static void send(ComBean bean, String host, int port)
			throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bean.encodeToXML(output);
		output.close();
		
		Socket socket = new Socket(host, port);
		BufferedOutputStream stream = new BufferedOutputStream(
				socket.getOutputStream());
		stream.write(output.toByteArray());
		
		stream.close();
		socket.close();
	}
	
}
