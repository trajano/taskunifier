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
package com.leclercb.taskunifier.gui.main.frame;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXStatusBar;

import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.commons.gui.utils.ScreenUtils;
import com.leclercb.taskunifier.gui.actions.ActionRemoveTab;
import com.leclercb.taskunifier.gui.components.menubar.MenuBar;
import com.leclercb.taskunifier.gui.components.statusbar.DefaultStatusBar;
import com.leclercb.taskunifier.gui.components.statusbar.MacStatusBar;
import com.leclercb.taskunifier.gui.components.statusbar.StatusBar;
import com.leclercb.taskunifier.gui.components.toolbar.DefaultToolBar;
import com.leclercb.taskunifier.gui.components.toolbar.MacToolBar;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.threads.Threads;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class SubFrame extends JXFrame implements FrameView, SavePropertiesListener, PropertyChangeSupported {
	
	private static int SUBFRAME_ID = 1;
	
	public static List<SubFrame> subFrames = new ArrayList<SubFrame>();
	
	public static List<SubFrame> getSubFrames() {
		return new ArrayList<SubFrame>(subFrames);
	}
	
	public static SubFrame createSubFrame() {
		SubFrame subFrame = new SubFrame(SUBFRAME_ID);
		SUBFRAME_ID++;
		
		subFrames.add(subFrame);
		
		subFrame.initialize();
		
		return subFrame;
	}
	
	public static void deleteSubFrame(SubFrame subFrame) {
		int listLength = ViewList.getInstance().getViewCount();
		for (int i = 0; i < listLength; i++) {
			if (subFrame.getFrameId() == ViewList.getInstance().getView(i).getFrameId()) {
				ViewList.getInstance().removeView(
						ViewList.getInstance().getView(i));
			}
		}
		
		subFrame.saveProperties();
		subFrame.setVisible(false);
		subFrame.dispose();
	}
	
	private int frameId;
	private JTabbedPane mainTabbedPane;
	private ViewItem oldSelectedView;
	
	private SubFrame(int frameId) {
		this.frameId = frameId;
	}
	
	@Override
	public int getFrameId() {
		return this.frameId;
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		
		if (b)
			this.repaint();
	}
	
	private void initialize() {
		Main.getSettings().addSavePropertiesListener(this);
		
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setIconImage(ImageUtils.getResourceImage("logo.png").getImage());
		this.setTitle(Constants.TITLE + " - " + Constants.VERSION);
		this.loadWindowSettings();
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent event) {
				deleteSubFrame(SubFrame.this);
			}
			
		});
		
		this.mainTabbedPane = new JTabbedPane();
		this.add(this.mainTabbedPane, BorderLayout.CENTER);
		
		this.initializeViews();
		
		this.mainTabbedPane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				int index = SubFrame.this.mainTabbedPane.getSelectedIndex();
				
				if (index == -1)
					return;
				
				SubFrame.this.setSelectedView(ViewList.getInstance().getView(
						index));
			}
			
		});
		
		this.initializeMenuBar();
		this.initializeToolBar();
		this.initializeStatusBar();
	}
	
	private void initializeViews() {
		for (ViewItem view : ViewList.getInstance().getViews()) {
			if (this.getFrameId() == view.getFrameId()) {
				this.addViewTab(view);
			}
		}
		
		ViewList.getInstance().addListChangeListener(new ListChangeListener() {
			
			@Override
			public void listChange(ListChangeEvent event) {
				ViewItem view = (ViewItem) event.getValue();
				
				if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
					SubFrame.this.addViewTab(view);
				}
				
				if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
					if (SubFrame.this.getFrameId() != view.getFrameId())
						return;
					
					int index = 0;
					for (int i = 0; i < event.getIndex(); i++) {
						if (SubFrame.this.getFrameId() == ViewList.getInstance().getView(
								i).getFrameId()) {
							index++;
						}
					}
					
					SubFrame.this.mainTabbedPane.removeTabAt(index);
					
					if (SubFrame.this.mainTabbedPane.getTabCount() == 0) {
						deleteSubFrame(SubFrame.this);
					}
					
				}
			}
			
		});
		
		ViewList.getInstance().addPropertyChangeListener(
				ViewList.PROP_CURRENT_VIEW,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent event) {
						ViewItem view = (ViewItem) event.getNewValue();
						SubFrame.this.setSelectedView(view);
					}
					
				});
		
		this.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent event) {
				
			}
			
			@Override
			public void windowGainedFocus(WindowEvent event) {
				ViewItem view = SubFrame.this.getSelectedView();
				
				if (view == null)
					return;
				
				ViewList.getInstance().setCurrentView(view);
			}
			
		});
	}
	
	@Override
	public Frame getFrame() {
		return this;
	}
	
	private void addViewTab(final ViewItem view) {
		if (this.getFrameId() != view.getFrameId())
			return;
		
		this.mainTabbedPane.addTab(
				view.getLabel(),
				view.getIcon(),
				view.getView().getViewContent());
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		
		panel.add(new JLabel(
				view.getLabel(),
				view.getIcon(),
				SwingConstants.LEFT), BorderLayout.CENTER);
		
		if (view.isRemovable()) {
			JButton button = new JButton(ImageUtils.getResourceImage(
					"remove.png",
					12,
					12));
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setFocusable(false);
			
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent event) {
					ActionRemoveTab.removeTab(view);
				}
				
			});
			
			panel.add(button, BorderLayout.EAST);
		}
		
		this.mainTabbedPane.setTabComponentAt(
				this.mainTabbedPane.getTabCount() - 1,
				panel);
	}
	
	@Override
	public ViewItem getSelectedView() {
		int selectedIndex = this.mainTabbedPane.getSelectedIndex();
		
		if (selectedIndex == -1)
			return null;
		
		int currentFrameIndex = 0;
		int viewIndex = 0;
		
		int listLength = ViewList.getInstance().getViewCount();
		for (int i = 0; i < listLength; i++) {
			if (selectedIndex == currentFrameIndex
					&& SubFrame.this.getFrameId() == ViewList.getInstance().getView(
							i).getFrameId())
				break;
			
			viewIndex++;
			
			if (SubFrame.this.getFrameId() == ViewList.getInstance().getView(i).getFrameId()) {
				currentFrameIndex++;
			}
		}
		
		return ViewList.getInstance().getView(viewIndex);
	}
	
	@Override
	public void setSelectedView(ViewItem view) {
		CheckUtils.isNotNull(view);
		
		if (this.getFrameId() != view.getFrameId())
			return;
		
		this.requestFocus();
		
		ViewList.getInstance().setCurrentView(view);
		
		int listIndex = ViewList.getInstance().getIndexOf(view);
		int index = 0;
		for (int i = 0; i < listIndex; i++) {
			if (SubFrame.this.getFrameId() == ViewList.getInstance().getView(i).getFrameId()) {
				index++;
			}
		}
		
		this.mainTabbedPane.setSelectedIndex(index);
		
		this.setTitle(Constants.TITLE
				+ " - "
				+ Constants.VERSION
				+ " - "
				+ view.getLabel());
		
		this.firePropertyChange(PROP_SELECTED_VIEW, this.oldSelectedView, view);
		this.oldSelectedView = view;
	}
	
	private void loadWindowSettings() {
		String propertyName = "window.sub";
		
		int extendedState = Main.getSettings().getIntegerProperty(
				propertyName + ".extended_state");
		int width = Main.getSettings().getIntegerProperty(
				propertyName + ".width");
		int height = Main.getSettings().getIntegerProperty(
				propertyName + ".height");
		int locationX = Main.getSettings().getIntegerProperty(
				propertyName + ".location_x");
		int locationY = Main.getSettings().getIntegerProperty(
				propertyName + ".location_y");
		
		this.setSize(width, height);
		this.setExtendedState(extendedState);
		
		if (ScreenUtils.isLocationInScreen(new Point(locationX, locationY)))
			this.setLocation(locationX, locationY);
		else
			this.setLocation(0, 0);
	}
	
	@Override
	public void saveProperties() {
		if (this.isVisible()) {
			String propertyName = "window.sub";
			
			Main.getSettings().setIntegerProperty(
					propertyName + ".extended_state",
					this.getExtendedState());
			Main.getSettings().setIntegerProperty(
					propertyName + ".width",
					this.getWidth());
			Main.getSettings().setIntegerProperty(
					propertyName + ".height",
					this.getHeight());
			Main.getSettings().setIntegerProperty(
					propertyName + ".location_x",
					(int) this.getLocationOnScreen().getX());
			Main.getSettings().setIntegerProperty(
					propertyName + ".location_y",
					(int) this.getLocationOnScreen().getY());
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
			statusBar = new MacStatusBar(
					this.getFrameId(),
					Threads.getScheduledSyncThread());
		else
			statusBar = new DefaultStatusBar(
					this.getFrameId(),
					Threads.getScheduledSyncThread());
		
		if (statusBar.getStatusBar() instanceof JXStatusBar)
			this.setStatusBar((JXStatusBar) statusBar.getStatusBar());
		else
			this.add(statusBar.getStatusBar(), BorderLayout.SOUTH);
	}
	
}
