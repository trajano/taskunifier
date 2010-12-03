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
package com.leclercb.taskunifier.gui.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class JCollapsiblePanel extends JPanel {

	private boolean selected;
	private JPanel headerPanel;
	private JPanel contentPanel;

	public JCollapsiblePanel(JPanel headerPanel, JPanel contentPanel) {
		super(new GridBagLayout());

		this.selected = false;
		this.headerPanel = headerPanel;
		this.contentPanel = contentPanel;
		this.contentPanel.setVisible(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 3, 0, 3);
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		add(headerPanel, gbc);
		add(contentPanel, gbc);

		JLabel padding = new JLabel();
		gbc.weighty = 1.0;
		add(padding, gbc);
	}

	public void toggleSelection() {
		selected = !selected;

		if (contentPanel.isShowing())
			contentPanel.setVisible(false);
		else
			contentPanel.setVisible(true);

		validate();
		headerPanel.repaint();
	}

}
