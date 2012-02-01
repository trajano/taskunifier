package com.leclercb.taskunifier.gui.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public final class ProtocolUtils {
	
	private ProtocolUtils() {
		
	}
	
	public static void registerCustomProtocolHandlers() {
		URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
			
			@Override
			public URLStreamHandler createURLStreamHandler(String protocol) {
				if (protocol.equalsIgnoreCase("evernote"))
					return new EvernoteURLStreamHandler();
				
				return null;
			}
			
		});
	}
	
	private static class EvernoteURLStreamHandler extends URLStreamHandler {
		
		@Override
		protected URLConnection openConnection(URL u) throws IOException {
			return null;
		}
		
	}
	
}
