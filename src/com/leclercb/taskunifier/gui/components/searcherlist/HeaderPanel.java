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
package com.leclercb.taskunifier.gui.components.searcherlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

class HeaderPanel extends JPanel {

	private String title;

	public HeaderPanel(String title) {
		this.title = title;

		this.initialize();
	}

	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setBorder(new LineBorder(Color.GRAY));
		this.setBackground(UIManager.getColor("TableHeader.background"));

		JLabel label = new JLabel(this.title);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		label.setHorizontalAlignment(SwingConstants.CENTER);

		this.add(label, BorderLayout.CENTER);
	}

}
