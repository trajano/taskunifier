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

import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.import_searcher.ImportSearcherDialog;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionImportSearchers extends AbstractAction {
	
	public ActionImportSearchers() {
		this(32, 32);
	}
	
	public ActionImportSearchers(int width, int height) {
		super(
				Translations.getString("action.name.import_searchers"),
				Images.getResourceImage("download.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.import_searchers"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		this.importSearcher();
	}
	
	public void importSearcher() {
		ImportSearcherDialog importDialog = new ImportSearcherDialog(
				MainFrame.getInstance().getFrame(),
				true);
		importDialog.setVisible(true);
	}
	
}
