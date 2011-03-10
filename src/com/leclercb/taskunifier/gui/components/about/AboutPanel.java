/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.about;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class AboutPanel extends JPanel {
	
	public AboutPanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = null;
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JLabel icon = new JLabel(
				Constants.TITLE + " - " + Constants.VERSION,
				Images.getResourceImage("logo.png", 48, 48),
				SwingConstants.CENTER);
		
		panel.add(icon, BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.NORTH);
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JEditorPane aboutMessage = new JEditorPane();
		aboutMessage.setContentType("text/html");
		aboutMessage.setEditable(false);
		aboutMessage.setText(Translations.getString("about.message"));
		aboutMessage.setCaretPosition(0);
		
		panel.add(
				ComponentFactory.createJScrollPane(aboutMessage, true),
				BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.CENTER);
	}
	
}
