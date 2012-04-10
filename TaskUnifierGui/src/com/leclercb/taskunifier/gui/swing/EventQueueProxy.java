package com.leclercb.taskunifier.gui.swing;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.util.logging.Level;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.translations.Translations;

public class EventQueueProxy extends EventQueue {
	
	@Override
	protected void dispatchEvent(AWTEvent newEvent) {
		try {
			super.dispatchEvent(newEvent);
		} catch (Throwable t) {
			GuiLogger.getLogger().log(Level.SEVERE, t.getMessage(), t);
			
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					t.getMessage(),
					null,
					"GUI",
					t,
					Level.SEVERE,
					null);
			
			JXErrorPane.showDialog(null, info);
		}
	}
	
}
