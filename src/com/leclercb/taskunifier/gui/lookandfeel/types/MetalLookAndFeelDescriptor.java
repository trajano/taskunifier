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

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import com.leclercb.taskunifier.gui.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.taskunifier.gui.lookandfeel.exc.LookAndFeelException;

public class MetalLookAndFeelDescriptor extends LookAndFeelDescriptor {

	public MetalLookAndFeelDescriptor(String name, String identifier) {
		super(name, identifier);
	}

	@Override
	public void setLookAndFeel(Window window) throws LookAndFeelException {
		try {
			MetalLookAndFeel.setCurrentTheme((MetalTheme) Class.forName(this.getIdentifier()).newInstance());

			UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());

			if (window != null) {
				SwingUtilities.updateComponentTreeUI(window);
				window.pack();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LookAndFeelException("Error while setting look and feel", e);
		}
	}

}
