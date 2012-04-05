/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.commons.gui.swing.lookandfeel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.OceanTheme;

import com.leclercb.commons.gui.swing.lookandfeel.types.DefaultLookAndFeelDescriptor;
import com.leclercb.commons.gui.swing.lookandfeel.types.MetalLookAndFeelDescriptor;

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
				LOOK_AND_FEELS.add(new DefaultLookAndFeelDescriptor(
						lafs[i].getName(),
						lafs[i].getClassName()));
		}
		
		// Metal Themes
		{
			LOOK_AND_FEELS.add(new MetalLookAndFeelDescriptor(
					"Metal - Default",
					DefaultMetalTheme.class.getName()));
			LOOK_AND_FEELS.add(new MetalLookAndFeelDescriptor(
					"Metal - Ocean",
					OceanTheme.class.getName()));
		}
	}
	
	public static void addLookAndFeel(LookAndFeelDescriptor lookAndFeel) {
		LOOK_AND_FEELS.add(lookAndFeel);
	}
	
	public static boolean isSytemLookAndFeel() {
		if (UIManager.getLookAndFeel() == null)
			return false;
		
		return UIManager.getSystemLookAndFeelClassName().equals(
				UIManager.getLookAndFeel().getClass().getName());
	}
	
	public static LookAndFeelDescriptor getLookAndFeel(String className) {
		for (LookAndFeelDescriptor lookAndFeel : LOOK_AND_FEELS)
			if (lookAndFeel.getIdentifier().equals(className))
				return lookAndFeel;
		
		return null;
	}
	
	public static List<LookAndFeelDescriptor> getLookAndFeels() {
		return new ArrayList<LookAndFeelDescriptor>(LOOK_AND_FEELS);
	}
	
}
