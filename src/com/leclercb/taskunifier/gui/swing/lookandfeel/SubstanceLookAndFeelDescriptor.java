package com.leclercb.taskunifier.gui.swing.lookandfeel;

import java.awt.Window;
import java.util.logging.Level;

import javax.swing.JFrame;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.commons.gui.swing.lookandfeel.exc.LookAndFeelException;

public class SubstanceLookAndFeelDescriptor extends LookAndFeelDescriptor {
	
	public SubstanceLookAndFeelDescriptor(String name, String identifier) {
		super(name, identifier);
	}
	
	@Override
	public void setLookAndFeel(Window window) throws LookAndFeelException {
		try {
			JFrame.setDefaultLookAndFeelDecorated(true);
			SubstanceLookAndFeel.setSkin(this.getIdentifier());
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.WARNING,
					"Error while setting look and feel \""
							+ this.getName()
							+ "\"",
					e);
			
			throw new LookAndFeelException(
					"Error while setting look and feel \""
							+ this.getName()
							+ "\"",
					e);
		}
	}
	
}
