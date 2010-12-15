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
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.searcheredit.SearcherEditDialog;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionEditSearcher extends AbstractAction {

	public ActionEditSearcher() {
		this(32, 32);
	}

	public ActionEditSearcher(int width, int height) {
		super(Translations.getString("action.name.edit_searcher"), Images.getResourceImage("search.png", width, height));

		this.putValue(SHORT_DESCRIPTION, Translations.getString("action.description.edit_searcher"));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (MainFrame.getInstance().getSelectedTaskSearcher() == null) {
			JOptionPane.showMessageDialog(null,
					Translations.getString("error.select_searcher"),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.actionPerformed(MainFrame.getInstance().getSelectedTaskSearcher());
	}

	public void actionPerformed(TaskSearcher searcher) {
		CheckUtils.isNotNull(searcher, "Searcher cannot be null");

		SearcherEditDialog dialog = new SearcherEditDialog(MainFrame.getInstance().getFrame(), true, searcher);

		dialog.setVisible(true);
	}

}
