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
package com.leclercb.taskunifier.gui.swing;

import java.awt.Frame;
import java.awt.Point;

import javax.swing.JDialog;

import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.gui.utils.ScreenUtils;
import com.leclercb.taskunifier.gui.main.Main;

public class TUDialog extends JDialog {
	
	public TUDialog() {
		super();
	}
	
	public TUDialog(Frame owner) {
		super(owner);
	}
	
	public void loadWindowSettings(final String windowProperty) {
		if (windowProperty == null)
			return;
		
		int width = Main.getSettings().getIntegerProperty(
				windowProperty + ".width");
		int height = Main.getSettings().getIntegerProperty(
				windowProperty + ".height");
		int locationX = Main.getSettings().getIntegerProperty(
				windowProperty + ".location_x");
		int locationY = Main.getSettings().getIntegerProperty(
				windowProperty + ".location_y");
		
		this.setSize(width, height);
		
		if (ScreenUtils.isLocationInScreen(new Point(locationX, locationY)))
			this.setLocation(locationX, locationY);
		else if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		else
			this.setLocation(0, 0);
		
		Main.getSettings().addSavePropertiesListener(
				new SavePropertiesListener() {
					
					@Override
					public void saveProperties() {
						Main.getSettings().setIntegerProperty(
								windowProperty + ".width",
								TUDialog.this.getWidth());
						Main.getSettings().setIntegerProperty(
								windowProperty + ".height",
								TUDialog.this.getHeight());
						Main.getSettings().setIntegerProperty(
								windowProperty + ".location_x",
								TUDialog.this.getX());
						Main.getSettings().setIntegerProperty(
								windowProperty + ".location_y",
								TUDialog.this.getY());
					}
					
				});
	}
	
}
