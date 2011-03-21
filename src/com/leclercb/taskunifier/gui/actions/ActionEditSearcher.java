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

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.components.searcheredit.SearcherEditDialog;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionEditSearcher extends AbstractAction {
	
	public ActionEditSearcher() {
		this(32, 32);
	}
	
	public ActionEditSearcher(int width, int height) {
		super(
				Translations.getString("action.name.edit_searcher"),
				Images.getResourceImage("search.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.edit_searcher"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionEditSearcher.editSearcher(
				MainFrame.getInstance().getSearcherView().getSelectedTaskSearcher());
	}
	
	public static void editSearcher(TaskSearcher searcher) {
		if (searcher == null) {
			ErrorDialog errorDialog = new ErrorDialog(
					MainFrame.getInstance().getFrame(),
					Translations.getString("error.select_searcher"),
					null,
					false);
			errorDialog.setVisible(true);
			return;
		}
		
		SearcherEditDialog dialog = new SearcherEditDialog(
				MainFrame.getInstance().getFrame(),
				true,
				searcher);
		
		dialog.setVisible(true);
		
		MainFrame.getInstance().getSearcherView().refreshTaskSearcher();
	}
	
}
