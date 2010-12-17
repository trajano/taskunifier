package com.leclercb.taskunifier.gui.actions;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

@SuppressWarnings("deprecation")
public class MacApplicationAdapter extends ApplicationAdapter {

	public MacApplicationAdapter() {

	}

	@Override
	public void handleQuit(ApplicationEvent e) {
		new ActionQuit().quit();
	}

	@Override
	public void handleAbout(ApplicationEvent e) {
		e.setHandled(true);
		new ActionAbout().about();
	}

	@Override
	public void handlePreferences(ApplicationEvent e) {
		new ActionConfiguration().configuration();
	}

}
