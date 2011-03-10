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
package com.leclercb.taskunifier.gui.components.review;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.leclercb.commons.gui.utils.BrowserUtils;
import com.leclercb.taskunifier.gui.actions.ActionDonate;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public class ReviewPanel extends JPanel {
	
	public ReviewPanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = null;
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JLabel icon = new JLabel(Constants.TITLE, Images.getResourceImage(
				"logo.png",
				48,
				48), SwingConstants.CENTER);
		
		panel.add(icon, BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.NORTH);
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JEditorPane pane = new JEditorPane();
		pane.setContentType("text/html");
		pane.setEditable(false);
		pane.setText(Translations.getString("review.message"));
		pane.setCaretPosition(0);
		
		JButton reviewButton = new JButton(
				Translations.getString("review.link"));
		reviewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					BrowserUtils.openDefaultBrowser(Constants.REVIEW_URL);
				} catch (Exception e) {
					ErrorDialog dialog = new ErrorDialog(
							MainFrame.getInstance().getFrame(),
							null,
							e,
							false);
					dialog.setVisible(true);
				}
			}
			
		});
		
		JButton donateButton = new JButton(new ActionDonate(16, 16));
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		buttonsPanel.add(reviewButton);
		buttonsPanel.add(donateButton);
		
		panel.add(
				ComponentFactory.createJScrollPane(pane, true),
				BorderLayout.CENTER);
		panel.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.add(panel, BorderLayout.CENTER);
	}
}
