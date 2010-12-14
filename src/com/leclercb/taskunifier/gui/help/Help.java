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
package com.leclercb.taskunifier.gui.help;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;

public final class Help {

	private Help() {

	}

	private static final String HELP_FILES_FOLDER = Main.RESOURCES_FOLDER + File.separator + "help";

	public static String getContent(String helpFile) {
		CheckUtils.isNotNull(helpFile, "Help file cannot be null");

		helpFile = HELP_FILES_FOLDER + File.separator + helpFile;
		StringBuffer content = new StringBuffer();

		try {
			InputStreamReader inputStream = new InputStreamReader(new FileInputStream(helpFile));
			BufferedReader buffer = new BufferedReader(inputStream);

			String line = null;
			while ((line = buffer.readLine()) != null)  
				content.append(line);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content.toString();
	}

	public static Component getHelp(final String helpFile) {
		CheckUtils.isNotNull(helpFile, "Help file cannot be null");

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.TRAILING));

		JButton component = new JButton(Images.getResourceImage("help.png", 16, 16));
		component.setBorderPainted(false);
		component.setContentAreaFilled(false);
		component.addActionListener(new HelpActionListener(helpFile));

		panel.add(component);

		return panel;
	}

	private static class HelpActionListener implements ActionListener {

		private String helpFile;
		private String helpContent;

		public HelpActionListener(String helpFile) {
			this.helpFile = helpFile;
			this.helpContent = null;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (this.helpContent == null)
				this.helpContent = getContent(this.helpFile);

			HelpDialog dialog = new HelpDialog(this.helpContent);
			dialog.setVisible(true);
		}

	}

	private static class HelpDialog extends JDialog {

		public HelpDialog(String content) {
			super(MainFrame.getInstance().getFrame(), true);

			this.setTitle(Translations.getString("general.help"));
			this.setSize(400, 400);
			this.setResizable(true);
			this.setLayout(new BorderLayout());

			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setLocationRelativeTo(MainFrame.getInstance().getFrame());

			JEditorPane pane = new JEditorPane();
			pane.setContentType("text/html");
			pane.setText(content);
			pane.setEditable(false);
			pane.setCaretPosition(0);

			this.add(new JScrollPane(pane), BorderLayout.CENTER);
		}

	}

}
