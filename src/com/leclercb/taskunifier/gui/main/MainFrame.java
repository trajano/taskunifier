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
package com.leclercb.taskunifier.gui.main;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXStatusBar;

import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.actions.ActionQuit;
import com.leclercb.taskunifier.gui.actions.MacApplicationAdapter;
import com.leclercb.taskunifier.gui.components.menubar.MenuBar;
import com.leclercb.taskunifier.gui.components.statusbar.DefaultStatusBar;
import com.leclercb.taskunifier.gui.components.statusbar.MacStatusBar;
import com.leclercb.taskunifier.gui.components.statusbar.StatusBar;
import com.leclercb.taskunifier.gui.components.toolbar.DefaultToolBar;
import com.leclercb.taskunifier.gui.components.toolbar.MacToolBar;
import com.leclercb.taskunifier.gui.components.traypopup.TrayPopup;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.threads.communicator.CommunicatorThread;
import com.leclercb.taskunifier.gui.threads.reminder.ReminderThread;
import com.leclercb.taskunifier.gui.threads.scheduledsync.ScheduledSyncThread;
import com.leclercb.taskunifier.gui.utils.Images;

public class MainFrame extends JXFrame implements MainView, SavePropertiesListener, PropertyChangeSupported {
	
	private static MainFrame INSTANCE;
	
	public static MainView getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MainFrame();
			INSTANCE.initialize();
		}
		
		return INSTANCE;
	}
	
	private boolean minimizeToSystemTray;
	
	private ViewType selectedViewType;
	
	private JTabbedPane mainTabbedPane;
	
	private CommunicatorThread communicatorThread;
	private ReminderThread reminderThread;
	private ScheduledSyncThread scheduledSyncThread;
	
	private MainFrame() {
		
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		
		if (b)
			this.repaint();
	}
	
	private void initialize() {
		Main.SETTINGS.addSavePropertiesListener(this);
		
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setIconImage(Images.getResourceImage("logo.png", 16, 16).getImage());
		this.setTitle(Constants.TITLE + " - " + Constants.VERSION);
		this.loadWindowSettings();
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent event) {
				if (MainFrame.this.minimizeToSystemTray) {
					MainFrame.this.setVisible(false);
				} else {
					ActionQuit.quit();
				}
			}
			
		});
		
		this.mainTabbedPane = new JTabbedPane();
		this.add(this.mainTabbedPane, BorderLayout.CENTER);
		
		ViewType.initialize(this);
		for (ViewType viewType : ViewType.values()) {
			this.mainTabbedPane.addTab(
					viewType.getLabel(),
					viewType.getIcon(),
					viewType.getView().getViewContent());
		}
		
		this.setSelectedViewType(ViewType.TASKS);
		
		this.mainTabbedPane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				MainFrame.this.setSelectedViewType(ViewType.values()[MainFrame.this.mainTabbedPane.getSelectedIndex()]);
			}
			
		});
		
		this.initializeCommunicatorThread();
		this.initializeReminderThread();
		this.initializeScheduledSyncThread();
		
		this.initializeAppMenu();
		this.initializeMenuBar();
		this.initializeToolBar();
		this.initializeStatusBar();
		
		this.initializeSystemTray();
	}
	
	@Override
	public Frame getFrame() {
		return this;
	}
	
	@Override
	public ViewType getSelectedViewType() {
		return this.selectedViewType;
	}
	
	@Override
	public void setSelectedViewType(ViewType viewType) {
		CheckUtils.isNotNull(viewType, "View cannot be null");
		
		if (this.selectedViewType == viewType)
			return;
		
		this.mainTabbedPane.setSelectedIndex(viewType.ordinal());
		
		ViewType oldSelectedViewType = this.selectedViewType;
		this.selectedViewType = viewType;
		
		this.setTitle(Constants.TITLE
				+ " - "
				+ Constants.VERSION
				+ " - "
				+ this.selectedViewType.getLabel());
		
		this.firePropertyChange(
				PROP_SELECTED_VIEW,
				oldSelectedViewType,
				viewType);
	}
	
	private void loadWindowSettings() {
		int extendedState = Main.SETTINGS.getIntegerProperty("window.extended_state");
		int width = Main.SETTINGS.getIntegerProperty("window.width");
		int height = Main.SETTINGS.getIntegerProperty("window.height");
		int locationX = Main.SETTINGS.getIntegerProperty("window.location_x");
		int locationY = Main.SETTINGS.getIntegerProperty("window.location_y");
		
		this.setSize(width, height);
		this.setExtendedState(extendedState);
		this.setLocation(locationX, locationY);
	}
	
	@Override
	public void saveProperties() {
		if (this.isVisible()) {
			Main.SETTINGS.setIntegerProperty(
					"window.extended_state",
					this.getExtendedState());
			Main.SETTINGS.setIntegerProperty("window.width", this.getWidth());
			Main.SETTINGS.setIntegerProperty("window.height", this.getHeight());
			Main.SETTINGS.setIntegerProperty(
					"window.location_x",
					(int) this.getLocationOnScreen().getX());
			Main.SETTINGS.setIntegerProperty(
					"window.location_y",
					(int) this.getLocationOnScreen().getY());
		}
	}
	
	private void initializeAppMenu() {
		if (SystemUtils.IS_OS_MAC) {
			TrayPopup popupMenu = new TrayPopup(false);
			MacApplicationAdapter.setDockMenu(popupMenu);
		}
	}
	
	private void initializeMenuBar() {
		this.setJMenuBar(new MenuBar());
	}
	
	private void initializeToolBar() {
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf()) {
			this.add(new MacToolBar().getComponent(), BorderLayout.NORTH);
		} else {
			this.add(new DefaultToolBar(), BorderLayout.NORTH);
		}
	}
	
	private void initializeStatusBar() {
		StatusBar statusBar = null;
		
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf())
			statusBar = new MacStatusBar(this.scheduledSyncThread);
		else
			statusBar = new DefaultStatusBar(this.scheduledSyncThread);
		
		if (statusBar.getStatusBar() instanceof JXStatusBar)
			this.setStatusBar((JXStatusBar) statusBar.getStatusBar());
		else
			this.add(statusBar.getStatusBar(), BorderLayout.SOUTH);
	}
	
	private void initializeCommunicatorThread() {
		this.communicatorThread = new CommunicatorThread();
		this.communicatorThread.start();
	}
	
	private void initializeReminderThread() {
		this.reminderThread = new ReminderThread();
		this.reminderThread.start();
	}
	
	private void initializeScheduledSyncThread() {
		this.scheduledSyncThread = new ScheduledSyncThread();
		this.scheduledSyncThread.start();
	}
	
	private void initializeSystemTray() {
		if (!SystemTray.isSupported()
				|| !Main.SETTINGS.getBooleanProperty("window.minimize_to_system_tray")) {
			this.minimizeToSystemTray = false;
			return;
		}
		
		this.minimizeToSystemTray = true;
		
		final SystemTray tray = SystemTray.getSystemTray();
		final TrayIcon trayIcon = new TrayIcon(Images.getResourceImage(
				"logo.png",
				(int) tray.getTrayIconSize().getWidth(),
				(int) tray.getTrayIconSize().getHeight()).getImage());
		
		trayIcon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.setVisible(true);
				MainFrame.this.setState(Frame.NORMAL);
			}
			
		});
		
		trayIcon.setPopupMenu(new TrayPopup());
		
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			
		}
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowIconified(WindowEvent event) {
				MainFrame.this.setVisible(false);
			}
			
			@Override
			public void windowDeiconified(WindowEvent event) {
				MainFrame.this.setVisible(true);
				MainFrame.this.setState(Frame.NORMAL);
			}
			
		});
	}
	
	@Override
	public void dispose() {
		this.communicatorThread.interrupt();
		this.reminderThread.interrupt();
		this.scheduledSyncThread.interrupt();
		super.dispose();
	}
	
}
