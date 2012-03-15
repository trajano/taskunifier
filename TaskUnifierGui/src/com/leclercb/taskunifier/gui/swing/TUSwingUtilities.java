package com.leclercb.taskunifier.gui.swing;

import javax.swing.SwingUtilities;

public final class TUSwingUtilities {
	
	private TUSwingUtilities() {
		
	}
	
	public static void invokeLater(Runnable doRun) {
		SwingUtilities.invokeLater(doRun);
	}
	
	public static void invokeAndWait(Runnable doRun) {
		try {
			SwingUtilities.invokeAndWait(doRun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void executeOrInvokeAndWait(Runnable doRun) {
		if (SwingUtilities.isEventDispatchThread()) {
			doRun.run();
		} else {
			invokeAndWait(doRun);
		}
	}
	
}
