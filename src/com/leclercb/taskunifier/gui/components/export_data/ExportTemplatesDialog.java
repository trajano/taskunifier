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

import com.leclercb.taskunifier.gui.api.templates.coders.TemplateFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ExportTemplatesDialog extends DefaultExportDialog {
	
	public ExportTemplatesDialog(Frame frame, boolean modal) {
		super(
				new TemplateFactoryXMLCoder(true),
				Translations.getString("general.export_templates"),
				frame,
				modal);
	}
	
}
