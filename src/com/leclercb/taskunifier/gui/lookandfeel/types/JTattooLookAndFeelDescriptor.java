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
package com.leclercb.taskunifier.gui.lookandfeel.types;

import java.awt.Window;
import java.lang.reflect.Method;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.taskunifier.gui.lookandfeel.exc.LookAndFeelException;

public class JTattooLookAndFeelDescriptor extends LookAndFeelDescriptor {

	public JTattooLookAndFeelDescriptor(String name, String identifier) {
		super(name, identifier);
	}

	@Override
	public void setLookAndFeel(Window window) throws LookAndFeelException {
		try {
			String valueClass = this.getIdentifier().substring(0, this.getIdentifier().lastIndexOf("$"));
			String valueTheme = this.getIdentifier().substring(this.getIdentifier().lastIndexOf("$") + 1, this.getIdentifier().length());

			Class<?> lafClass = Class.forName(valueClass);
			Method lafMethod = lafClass.getDeclaredMethod("setTheme", new Class<?>[] { String.class, String.class, String.class });
			Object lafInstance = lafClass.newInstance();

			lafMethod.invoke(lafInstance, new Object[] { valueTheme, "", Constants.TITLE });

			UIManager.setLookAndFeel(valueClass);

			if (window != null) {
				SwingUtilities.updateComponentTreeUI(window);
				window.pack();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LookAndFeelException("Error while setting look and feel \"" + this.getName() + "\"", e);
		}
	}

}
