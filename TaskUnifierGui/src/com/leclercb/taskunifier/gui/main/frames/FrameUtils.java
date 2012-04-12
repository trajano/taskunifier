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
package com.leclercb.taskunifier.gui.main.frames;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang3.SystemUtils;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.actions.ActionQuit;
import com.leclercb.taskunifier.gui.components.traypopup.TrayPopup;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public final class FrameUtils {
	
	private FrameUtils() {
		
	}
	
	private static int FRAME_ID = 0;
	
	private static List<FrameView> frames = new ArrayList<FrameView>();
	
	public static int getFrameCount() {
		return frames.size();
	}
	
	public static List<FrameView> getFrameViews() {
		return new ArrayList<FrameView>(frames);
	}
	
	public static FrameView createFrameView() {
		String propertyName = "window.main";
		
		if (FRAME_ID != 0)
			propertyName = "window.sub";
		
		MainFrame frame = new MainFrame(FRAME_ID, propertyName);
		
		FRAME_ID++;
		
		frames.add(frame);
		
		frame.setVisible(true);
		frame.requestFocus();
		
		return frame;
	}
	
	public static void deleteFrameView(FrameView frame) {
		for (ViewItem view : ViewList.getInstance().getViews()) {
			if (frame.getFrameId() == view.getFrameId()) {
				ViewList.getInstance().removeView(view);
			}
		}
		
		frame.getFrame().dispose();
		
		frames.remove(frame);
		
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isSytemLookAndFeel())
			return;
		
		if (getFrameCount() == 0)
			ActionQuit.quit();
	}
	
	public static Frame getCurrentFrame() {
		if (getCurrentFrameView() != null)
			return getCurrentFrameView().getFrame();
		
		return null;
	}
	
	public static FrameView getCurrentFrameView() {
		if (ViewList.getInstance().getCurrentView() == null)
			return null;
		
		int frameId = ViewList.getInstance().getCurrentView().getFrameId();
		return getFrameView(frameId);
	}
	
	public static FrameView getFrameView(int frameId) {
		for (FrameView frame : getFrameViews()) {
			if (frameId == frame.getFrameId())
				return frame;
		}
		
		return null;
	}
	
	public static void initializeSystemTray() {
		if (!SystemTray.isSupported()
				|| !Main.getSettings().getBooleanProperty(
						"window.minimize_to_system_tray")) {
			return;
		}
		
		final SystemTray tray = SystemTray.getSystemTray();
		final TrayIcon trayIcon = new TrayIcon(ImageUtils.getResourceImage(
				"logo.png",
				(int) tray.getTrayIconSize().getWidth(),
				(int) tray.getTrayIconSize().getHeight()).getImage());
		
		trayIcon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (FrameView frame : getFrameViews()) {
					frame.getFrame().setVisible(true);
					frame.getFrame().setState(Frame.NORMAL);
				}
			}
			
		});
		
		trayIcon.setPopupMenu(new TrayPopup());
		
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			GuiLogger.getLogger().log(Level.WARNING, "Cannot add tray icon", e);
		}
	}
	
}
