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
package com.leclercb.taskunifier.gui.lookandfeel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.OceanTheme;

import com.leclercb.taskunifier.gui.lookandfeel.types.DefaultLookAndFeelDescriptor;
import com.leclercb.taskunifier.gui.lookandfeel.types.MetalLookAndFeelDescriptor;

public final class LookAndFeelUtils {

	private LookAndFeelUtils() {

	}

	private static List<LookAndFeelDescriptor> LOOK_AND_FEELS;

	static {
		LOOK_AND_FEELS = new ArrayList<LookAndFeelDescriptor>();

		// Installed Look And Feels
		{
			LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
			for (int i = 0; i < lafs.length; i++)
				LOOK_AND_FEELS.add(new DefaultLookAndFeelDescriptor(lafs[i]
						.getName(), lafs[i].getClassName()));
		}

		// Metal Themes
		{
			LOOK_AND_FEELS.add(new MetalLookAndFeelDescriptor(
					"Metal - Default", DefaultMetalTheme.class.getName()));
			LOOK_AND_FEELS.add(new MetalLookAndFeelDescriptor("Metal - Ocean",
					OceanTheme.class.getName()));
		}
	}

	public static void addLookAndFeel(LookAndFeelDescriptor lookAndFeel) {
		LOOK_AND_FEELS.add(lookAndFeel);
	}

	public static LookAndFeelDescriptor getLookAndFeel(String className) {
		for (LookAndFeelDescriptor lookAndFeel : LOOK_AND_FEELS)
			if (lookAndFeel.getIdentifier().equals(className))
				return lookAndFeel;

		return null;
	}

	public static List<LookAndFeelDescriptor> getLookAndFeels() {
		return Collections.unmodifiableList(LOOK_AND_FEELS);
	}

}
