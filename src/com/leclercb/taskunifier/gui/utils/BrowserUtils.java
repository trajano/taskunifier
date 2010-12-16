package com.leclercb.taskunifier.gui.utils;

import java.awt.Desktop;
import java.net.URI;

public final class BrowserUtils {

	private BrowserUtils() {

	}

	public static void openDefaultBrowser(String url) throws Exception {
		if (!Desktop.isDesktopSupported()) {
			throw new Exception("Desktop is not supported");
		}

		Desktop desktop = Desktop.getDesktop();

		if (!desktop.isSupported(Desktop.Action.BROWSE)) {
			throw new Exception("Desktop doesn't support the browse action");
		}

		URI uri = new URI(url);
		desktop.browse(uri);
	}

}
