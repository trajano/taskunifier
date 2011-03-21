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

import com.leclercb.commons.gui.utils.BrowserUtils;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionGetSerial extends AbstractAction {
	
	private String url;
	
	public ActionGetSerial(String url) {
		this(url, 32, 32);
	}
	
	public ActionGetSerial(String url, int width, int height) {
		super(
				Translations.getString("general.get_serial"),
				Images.getResourceImage("key.png", width, height));
		
		this.url = url;
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("general.get_serial"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionGetSerial.getSerial(this.url);
	}
	
	public static void getSerial(String url) {
		try {
			BrowserUtils.openDefaultBrowser(url);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					MainFrame.getInstance().getFrame(),
					Translations.getString("general.please_visit", url),
					Translations.getString("error.cannot_open_browser"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
