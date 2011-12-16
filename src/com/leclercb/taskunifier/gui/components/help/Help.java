/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.help;

import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JButton;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public final class Help {
	
	public static final Help DEFAULT_HELP = new Help();
	
	private HelpSet helpSet;
	private HelpBroker helpBroker;
	
	private Help() {
		try {
			File file = new File(Main.getResourcesFolder()
					+ File.separator
					+ "help"
					+ File.separator
					+ "help.xml");
			this.helpSet = new HelpSet(null, file.toURI().toURL());
			this.helpBroker = this.helpSet.createHelpBroker();
		} catch (Exception e) {
			GuiLogger.getLogger().warning("Cannot load default help set");
		}
	}
	
	public Help(URL url) {
		try {
			this.helpSet = new HelpSet(null, url);
			this.helpBroker = this.helpSet.createHelpBroker();
		} catch (Exception e) {
			GuiLogger.getLogger().warning("Cannot load help set: " + url);
		}
	}
	
	public HelpSet getHelpSet() {
		return this.helpSet;
	}
	
	public HelpBroker getHelpBroker() {
		return this.helpBroker;
	}
	
	public static void showHelpDialog(String id) {
		showHelpDialog(DEFAULT_HELP.getHelpBroker(), id);
	}
	
	public static void showHelpDialog(HelpBroker hb, String id) {
		if (id == null)
			id = "taskunifier";
		
		hb.setDisplayed(true);
	}
	
	private static JButton getHelpButton() {
		JButton button = new JButton(ImageUtils.getResourceImage(
				"help.png",
				16,
				16));
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		
		return button;
	}
	
	public static JButton getHelpButton(String id) {
		return getHelpButton(DEFAULT_HELP.getHelpBroker(), id);
	}
	
	public static JButton getHelpButton(HelpBroker hb, String id) {
		if (id == null)
			return null;
		
		JButton button = getHelpButton();
		
		if (hb != null) {
			CSH.setHelpIDString(button, id);
			ActionListener listener = null;
			listener = new CSH.DisplayHelpFromSource(hb);
			button.addActionListener(listener);
		}
		
		return button;
	}
	
}
