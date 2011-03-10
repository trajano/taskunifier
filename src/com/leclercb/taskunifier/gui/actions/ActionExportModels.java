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

import com.leclercb.taskunifier.gui.components.export_data.ExportModelsDialog;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionExportModels extends AbstractAction {
	
	public ActionExportModels() {
		this(32, 32);
	}
	
	public ActionExportModels(int width, int height) {
		super(
				Translations.getString("action.name.export_models"),
				Images.getResourceImage("upload.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.export_models"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		this.exportModels();
	}
	
	public void exportModels() {
		ExportModelsDialog exportDialog = new ExportModelsDialog(
				MainFrame.getInstance().getFrame(),
				true);
		exportDialog.setVisible(true);
	}
	
}
