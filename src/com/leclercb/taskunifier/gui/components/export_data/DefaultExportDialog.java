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
package com.leclercb.taskunifier.gui.components.export_data;

import java.awt.Frame;
import java.io.FileOutputStream;

import com.leclercb.commons.api.coder.FactoryCoder;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.translations.Translations;

public class DefaultExportDialog extends AbstractExportDialog {
	
	private FactoryCoder coder;
	
	public DefaultExportDialog(
			FactoryCoder coder,
			String title,
			Frame frame,
			boolean modal) {
		super(
				title,
				frame,
				modal,
				"xml",
				Translations.getString("general.xml_files"));
		
		CheckUtils.isNotNull(coder, "Coder cannot be null");
		
		this.coder = coder;
	}
	
	@Override
	protected void exportToFile(String file) throws Exception {
		FileOutputStream output = new FileOutputStream(file);
		this.coder.encode(output);
	}
	
}
