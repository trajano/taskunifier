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

import java.io.File;
import java.net.URL;
import java.util.logging.Level;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JButton;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public final class Help {
	
	private static final String DEFAULT_ID = "taskunifier";
	
	private static Help INSTANCE;
	
	public static Help getInstance() {
		if (INSTANCE == null) {
			try {
				File file = new File(Main.getResourcesFolder()
						+ File.separator
						+ "help"
						+ File.separator
						+ "help.xml");
				
				INSTANCE = new Help(file.toURI().toURL());
			} catch (Exception e) {
				GuiLogger.getLogger().log(
						Level.SEVERE,
						"Cannot load default help set",
						e);
			}
		}
		
		return INSTANCE;
	}
	
	private HelpSet helpSet;
	private HelpBroker helpBroker;
	
	public Help(URL url) throws Exception {
		this.helpSet = new HelpSet(null, url);
		this.helpBroker = this.helpSet.createHelpBroker();
	}
	
	public HelpSet getHelpSet() {
		return this.helpSet;
	}
	
	public HelpBroker getHelpBroker() {
		return this.helpBroker;
	}
	
	public void showHelpDialog(String id) {
		if (id == null)
			id = DEFAULT_ID;
		
		this.helpBroker.setCurrentID(id);
		this.helpBroker.setDisplayed(true);
	}
	
	public JButton getHelpButton(String id) {
		if (id == null)
			id = DEFAULT_ID;
		
		JButton button = new JButton(ImageUtils.getResourceImage(
				"help.png",
				16,
				16));
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		
		CSH.setHelpIDString(button, id);
		button.addActionListener(new CSH.DisplayHelpFromSource(this.helpBroker));
		
		return button;
	}
	
	public static void main(String[] args) throws Exception {
		Help help = new Help(
				new File("resources/help/help.xml").toURI().toURL());
		help.showHelpDialog(null);
	}
	
}
