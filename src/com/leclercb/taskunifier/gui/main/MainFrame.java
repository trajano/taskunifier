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
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXStatusBar;

import com.explodingpixels.macwidgets.SourceListStandardColorScheme;
import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.actions.ActionQuit;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.NoteSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.components.menubar.MenuBar;
import com.leclercb.taskunifier.gui.components.modelnote.ModelNotePanel;
import com.leclercb.taskunifier.gui.components.notes.NoteView;
import com.leclercb.taskunifier.gui.components.notes.table.NoteTable;
import com.leclercb.taskunifier.gui.components.notesearchertree.NoteSearcherPanel;
import com.leclercb.taskunifier.gui.components.notesearchertree.NoteSearcherView;
import com.leclercb.taskunifier.gui.components.statusbar.DefaultStatusBar;
import com.leclercb.taskunifier.gui.components.statusbar.MacStatusBar;
import com.leclercb.taskunifier.gui.components.statusbar.StatusBar;
import com.leclercb.taskunifier.gui.components.tasks.TaskView;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherPanel;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;
import com.leclercb.taskunifier.gui.components.toolbar.DefaultToolBar;
import com.leclercb.taskunifier.gui.components.toolbar.MacToolBar;
import com.leclercb.taskunifier.gui.components.traypopup.TrayPopup;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.threads.reminder.ReminderThread;
import com.leclercb.taskunifier.gui.threads.scheduledsync.ScheduledSyncThread;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public class MainFrame extends JXFrame implements MainView, SavePropertiesListener, PropertyChangeSupported {
	
	private static MainView INSTANCE;
	
	public static MainView getInstance() {
		if (INSTANCE == null)
			INSTANCE = new MainFrame();
		
		return INSTANCE;
	}
	
	private ReminderThread reminderThread;
	private ScheduledSyncThread scheduledSyncThread;
	
	private JSplitPane horizontalSplitPane;
	private JSplitPane verticalSplitPane;
	
	private JPanel searcherPane;
	private JPanel middlePane;
	
	private JXSearchField searchField;
	private JCheckBox showCompletedTasksCheckBox;
	
	private NoteSearcherPanel noteSearcherPanel;
	private TaskSearcherPanel taskSearcherPanel;
	
	private NoteTable noteTable;
	private TaskTable taskTable;
	
	private ModelNotePanel modelNote;
	
	private View selectedView;
	
	private MainFrame() {
		this.initialize();
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
		this.loadWindowSizeSettings();
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent event) {
				ActionQuit.quit();
			}
			
		});
		
		{
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			
			if (SystemUtils.IS_OS_MAC
					&& LookAndFeelUtils.isCurrentLafSystemLaf()) {
				this.horizontalSplitPane = ComponentFactory.createThinJSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			} else {
				panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
				
				this.horizontalSplitPane = new JSplitPane(
						JSplitPane.HORIZONTAL_SPLIT);
			}
			
			this.verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			this.verticalSplitPane.setOneTouchExpandable(true);
			this.verticalSplitPane.setBorder(BorderFactory.createEmptyBorder());
			
			this.middlePane = new JPanel();
			this.middlePane.setLayout(new CardLayout());
			this.verticalSplitPane.setTopComponent(this.middlePane);
			
			this.horizontalSplitPane.setRightComponent(this.verticalSplitPane);
			
			panel.add(this.horizontalSplitPane, BorderLayout.CENTER);
			
			this.add(panel, BorderLayout.CENTER);
			
			this.loadSplitPaneSettings();
		}
		
		{
			this.initializeSearchField();
			this.initializeShowCompletedTasksCheckBox();
			this.initializeSearcherList(this.horizontalSplitPane);
			this.initializeNoteTable(this.middlePane);
			this.initializeTaskTable(this.middlePane);
			this.initializeModelNote(this.verticalSplitPane);
			
			this.setSelectedView(View.TASKS);
			
			this.initializeReminderThread();
			this.initializeScheduledSyncThread();
			
			this.initializeMenuBar();
			this.initializeToolBar();
			this.initializeStatusBar();
			
			this.initializeSystemTray();
			
			this.noteSearcherPanel.refreshNoteSearcher();
			this.taskSearcherPanel.refreshTaskSearcher();
		}
	}
	
	@Override
	public Frame getFrame() {
		return this;
	}
	
	@Override
	public NoteSearcherView getNoteSearcherView() {
		return this.noteSearcherPanel;
	}
	
	@Override
	public TaskSearcherView getTaskSearcherView() {
		return this.taskSearcherPanel;
	}
	
	@Override
	public View getSelectedView() {
		return this.selectedView;
	}
	
	@Override
	public void setSelectedView(View view) {
		CheckUtils.isNotNull(view, "View cannot be null");
		
		if (this.selectedView == view)
			return;
		
		CardLayout layout = null;
		
		layout = (CardLayout) this.searcherPane.getLayout();
		layout.show(this.searcherPane, view.name());
		
		layout = (CardLayout) this.middlePane.getLayout();
		layout.show(this.middlePane, view.name());
		
		if (view == View.NOTES)
			this.modelNote.modelSelectionChange(new ModelSelectionChangeEvent(
					this,
					this.noteTable.getSelectedNotes()));
		else if (view == View.TASKS)
			this.modelNote.modelSelectionChange(new ModelSelectionChangeEvent(
					this,
					this.taskTable.getSelectedTasks()));
		
		View oldSelectedView = this.selectedView;
		this.selectedView = view;
		
		this.firePropertyChange(PROP_SELECTED_VIEW, oldSelectedView, view);
	}
	
	@Override
	public NoteView getNoteView() {
		return this.noteTable;
	}
	
	@Override
	public TaskView getTaskView() {
		return this.taskTable;
	}
	
	private void loadWindowSizeSettings() {
		int extendedState = Main.SETTINGS.getIntegerProperty("window.extended_state");
		int width = Main.SETTINGS.getIntegerProperty("window.width");
		int height = Main.SETTINGS.getIntegerProperty("window.height");
		int locationX = Main.SETTINGS.getIntegerProperty("window.location_x");
		int locationY = Main.SETTINGS.getIntegerProperty("window.location_y");
		
		this.setSize(width, height);
		this.setExtendedState(extendedState);
		this.setLocation(locationX, locationY);
	}
	
	private void loadSplitPaneSettings() {
		int hSplit = Main.SETTINGS.getIntegerProperty("window.horizontal_split");
		int vSplit = Main.SETTINGS.getIntegerProperty("window.vertical_split");
		
		this.horizontalSplitPane.setDividerLocation(hSplit);
		this.verticalSplitPane.setDividerLocation(vSplit);
	}
	
	@Override
	public void saveProperties() {
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
		
		Main.SETTINGS.setIntegerProperty(
				"window.horizontal_split",
				this.horizontalSplitPane.getDividerLocation());
		Main.SETTINGS.setIntegerProperty(
				"window.vertical_split",
				this.verticalSplitPane.getDividerLocation());
	}
	
	private void initializeMenuBar() {
		this.setJMenuBar(new MenuBar(
				this,
				this.taskTable,
				this.taskSearcherPanel));
	}
	
	private void initializeToolBar() {
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf()) {
			this.add(
					new MacToolBar(this, this.taskTable, this.searchField).getComponent(),
					BorderLayout.NORTH);
		} else {
			this.add(
					new DefaultToolBar(this, this.taskTable),
					BorderLayout.NORTH);
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
	
	private void initializeSearchField() {
		this.searchField = new JXSearchField(
				Translations.getString("general.search"));
		this.searchField.setColumns(15);
		
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.noteSearcherPanel.setTitleFilter(e.getActionCommand());
				MainFrame.this.taskSearcherPanel.setTitleFilter(e.getActionCommand());
			}
			
		});
	}
	
	private void initializeShowCompletedTasksCheckBox() {
		this.showCompletedTasksCheckBox = new JCheckBox(
				Translations.getString("configuration.general.show_completed_tasks"));
		
		this.showCompletedTasksCheckBox.setOpaque(false);
		this.showCompletedTasksCheckBox.setFont(this.showCompletedTasksCheckBox.getFont().deriveFont(
				10.0f));
		
		this.showCompletedTasksCheckBox.setSelected(Main.SETTINGS.getBooleanProperty("searcher.show_completed_tasks"));
		
		this.showCompletedTasksCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.SETTINGS.setBooleanProperty(
						"searcher.show_completed_tasks",
						MainFrame.this.showCompletedTasksCheckBox.isSelected());
			}
			
		});
	}
	
	private void initializeSearcherList(JSplitPane horizontalSplitPane) {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(new SourceListStandardColorScheme().getActiveBackgroundColor());
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(new SourceListStandardColorScheme().getActiveBackgroundColor());
		
		panel.add(northPanel, BorderLayout.NORTH);
		
		if (!(SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf())) {
			northPanel.add(this.searchField, BorderLayout.NORTH);
		}
		
		northPanel.add(this.showCompletedTasksCheckBox, BorderLayout.SOUTH);
		
		this.searcherPane = new JPanel(new CardLayout());
		panel.add(this.searcherPane, BorderLayout.CENTER);
		
		this.initializeNoteSearcherList();
		this.initializeTaskSearcherList();
		
		horizontalSplitPane.setLeftComponent(panel);
	}
	
	private void initializeNoteSearcherList() {
		this.noteSearcherPanel = new NoteSearcherPanel();
		
		this.noteSearcherPanel.addPropertyChangeListener(
				NoteSearcherPanel.PROP_TITLE_FILTER,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						String filter = (String) evt.getNewValue();
						if (!MainFrame.this.searchField.getText().equals(filter))
							MainFrame.this.searchField.setText(filter);
					}
					
				});
		
		this.searcherPane.add(this.noteSearcherPanel, View.NOTES.name());
	}
	
	private void initializeTaskSearcherList() {
		this.taskSearcherPanel = new TaskSearcherPanel();
		
		this.taskSearcherPanel.addPropertyChangeListener(
				TaskSearcherPanel.PROP_TITLE_FILTER,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						String filter = (String) evt.getNewValue();
						if (!MainFrame.this.searchField.getText().equals(filter))
							MainFrame.this.searchField.setText(filter);
					}
					
				});
		
		this.searcherPane.add(this.taskSearcherPanel, View.TASKS.name());
	}
	
	private void initializeNoteTable(JPanel middlePane) {
		this.noteTable = new NoteTable();
		
		JPanel notePanel = new JPanel(new BorderLayout());
		notePanel.add(
				ComponentFactory.createJScrollPane(this.noteTable, false),
				BorderLayout.CENTER);
		
		this.noteSearcherPanel.addNoteSearcherSelectionChangeListener(this.noteTable);
		this.noteSearcherPanel.addPropertyChangeListener(
				NoteSearcherPanel.PROP_TITLE_FILTER,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						MainFrame.this.noteTable.noteSearcherSelectionChange(new NoteSearcherSelectionChangeEvent(
								evt.getSource(),
								MainFrame.this.noteSearcherPanel.getSelectedNoteSearcher()));
					}
					
				});
		
		middlePane.add(notePanel, View.NOTES.name());
	}
	
	private void initializeTaskTable(JPanel middlePane) {
		this.taskTable = new TaskTable();
		
		JPanel taskPanel = new JPanel(new BorderLayout());
		taskPanel.add(
				ComponentFactory.createJScrollPane(this.taskTable, false),
				BorderLayout.CENTER);
		
		this.taskSearcherPanel.addTaskSearcherSelectionChangeListener(this.taskTable);
		this.taskSearcherPanel.addPropertyChangeListener(
				TaskSearcherPanel.PROP_TITLE_FILTER,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						MainFrame.this.taskTable.taskSearcherSelectionChange(new TaskSearcherSelectionChangeEvent(
								evt.getSource(),
								MainFrame.this.taskSearcherPanel.getSelectedTaskSearcher()));
					}
					
				});
		
		middlePane.add(taskPanel, View.TASKS.name());
	}
	
	private void initializeModelNote(JSplitPane verticalSplitPane) {
		JPanel panel = new JPanel(new BorderLayout());
		
		this.modelNote = new ModelNotePanel();
		
		this.noteTable.addModelSelectionChangeListener(new ModelSelectionListener() {
			
			@Override
			public void modelSelectionChange(ModelSelectionChangeEvent event) {
				if (MainFrame.this.getSelectedView() == View.NOTES)
					MainFrame.this.modelNote.modelSelectionChange(event);
			}
			
		});
		
		this.taskTable.addModelSelectionChangeListener(new ModelSelectionListener() {
			
			@Override
			public void modelSelectionChange(ModelSelectionChangeEvent event) {
				if (MainFrame.this.getSelectedView() == View.TASKS)
					MainFrame.this.modelNote.modelSelectionChange(event);
			}
			
		});
		
		panel.add(this.modelNote, BorderLayout.CENTER);
		
		verticalSplitPane.setBottomComponent(panel);
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
		if (SystemTray.isSupported()
				&& Main.SETTINGS.getBooleanProperty("window.minimize_to_system_tray")) {
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
			
			trayIcon.setPopupMenu(new TrayPopup(
					this,
					this.taskTable,
					this.noteTable));
			
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
	}
	
	@Override
	public void dispose() {
		this.reminderThread.interrupt();
		this.scheduledSyncThread.interrupt();
		super.dispose();
	}
	
}
