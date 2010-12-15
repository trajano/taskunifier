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
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.translations.Translations;

class SearcherListRenderer extends JPanel implements ListCellRenderer {

	private Color colorEven;
	private Color colorOdd;

	private JLabel icon;
	private JLabel text;

	public SearcherListRenderer() {
		this.colorEven = Settings.getColorProperty("theme.color.even");
		this.colorOdd = Settings.getColorProperty("theme.color.odd");

		this.updateUI();
	}

	@Override
	public void updateUI() {
		super.updateUI();

		this.icon = new JLabel();
		this.icon.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 10));

		this.text = new JLabel();

		this.text.setPreferredSize(new Dimension(100, 30));

		this.setLayout(new BorderLayout());
		this.add(this.icon, BorderLayout.WEST);
		this.add(this.text, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (value == null) {
			this.icon.setIcon(null);
			this.text.setText(Translations.getString("searcherlist.none"));
		} else if (value instanceof TaskSearcher) {
			if (((TaskSearcher) value).getIcon() != null)
				this.icon.setIcon(Images.getImage(
						((TaskSearcher) value).getIcon(), 24, 24));
			else
				this.icon.setIcon(null);

			this.text.setText(value.toString());
		} else {
			this.icon.setIcon(null);
			this.text.setText(value.toString());
		}

		if (isSelected) {
			this.setBackground(list.getSelectionBackground());
		} else if (Settings.getBooleanProperty("theme.color.enabled")) {
			Color bg = (index % 2 == 0 ? this.colorEven : this.colorOdd);
			this.setBackground(bg);
		} else {
			this.setBackground(UIManager.getColor("List.background"));
		}

		return this;
	}

}
