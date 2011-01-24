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
package com.leclercb.taskunifier.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.apple.eawt.Application;
import com.leclercb.commons.api.properties.SavePropertiesListener;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.api.utils.OsUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.actions.ActionAbout;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTemplateTask;
import com.leclercb.taskunifier.gui.actions.ActionBatchAddTasks;
import com.leclercb.taskunifier.gui.actions.ActionCheckVersion;
import com.leclercb.taskunifier.gui.actions.ActionConfiguration;
import com.leclercb.taskunifier.gui.actions.ActionCopy;
import com.leclercb.taskunifier.gui.actions.ActionCut;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.actions.ActionExportSearchers;
import com.leclercb.taskunifier.gui.actions.ActionHelp;
import com.leclercb.taskunifier.gui.actions.ActionImportSearchers;
import com.leclercb.taskunifier.gui.actions.ActionManageModels;
import com.leclercb.taskunifier.gui.actions.ActionPaste;
import com.leclercb.taskunifier.gui.actions.ActionPrint;
import com.leclercb.taskunifier.gui.actions.ActionQuit;
import com.leclercb.taskunifier.gui.actions.ActionRedo;
import com.leclercb.taskunifier.gui.actions.ActionScheduledSync;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.actions.ActionUndo;
import com.leclercb.taskunifier.gui.actions.MacApplicationAdapter;
import com.leclercb.taskunifier.gui.components.searcherlist.SearcherPanel;
import com.leclercb.taskunifier.gui.components.searcherlist.SearcherView;
import com.leclercb.taskunifier.gui.components.statusbar.StatusBar;
import com.leclercb.taskunifier.gui.components.tasks.TaskPanel;
import com.leclercb.taskunifier.gui.components.tasks.TaskView;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.reminder.ReminderThread;
import com.leclercb.taskunifier.gui.scheduledsync.ScheduledSyncThread;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;

public class MainFrame extends JFrame implements MainView, ListSelectionListener, SavePropertiesListener, ActionListener {
	
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
	
	private JMenuBar menuBar;
	private JToolBar toolBar;
	private SearcherPanel searcherPanel;
	private TaskPanel taskPanel;
	private JTextArea taskNote;
	private StatusBar statusBar;
	
	private Task previousSelectedTask;
	
	private MainFrame() {
		this.initialize();
	}
	
	private void initialize() {
		Main.SETTINGS.addSavePropertiesListener(this);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setIconImage(Images.getResourceImage("logo.png", 16, 16).getImage());
		this.setTitle(Constants.TITLE + " - " + Constants.VERSION);
		this.loadWindowSizeSettings();
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent event) {
				Main.stop();
			}
			
		});
		
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		this.horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		this.horizontalSplitPane.setBorder(new EmptyBorder(3, 0, 0, 0));
		
		this.loadSplitPaneSettings();
		
		this.initializeSearcherList(this.horizontalSplitPane);
		this.initializeTaskPanel(this.verticalSplitPane);
		this.initializeTaskNote(this.verticalSplitPane);
		this.initializeDefaultTaskSearcher();
		
		this.horizontalSplitPane.setRightComponent(this.verticalSplitPane);
		
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		panel.add(this.horizontalSplitPane, BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.CENTER);
		
		this.initializeReminderThread();
		this.initializeScheduledSyncThread();
		
		this.initializeMenuBar();
		this.initializeToolBar();
		this.initializeStatusBar();
	}
	
	@Override
	public Frame getFrame() {
		return this;
	}
	
	@Override
	public SearcherView getSearcherView() {
		return this.searcherPanel;
	}
	
	@Override
	public TaskView getTaskView() {
		return this.taskPanel;
	}
	
	private void loadWindowSizeSettings() {
		Integer extendedState = Main.SETTINGS.getIntegerProperty("window.extended_state");
		Integer width = Main.SETTINGS.getIntegerProperty("window.width");
		Integer height = Main.SETTINGS.getIntegerProperty("window.height");
		Integer locationX = Main.SETTINGS.getIntegerProperty("window.location_x");
		Integer locationY = Main.SETTINGS.getIntegerProperty("window.location_y");
		
		if (width == null || height == null || extendedState == null) {
			this.setExtendedState(this.getExtendedState()
					| Frame.MAXIMIZED_BOTH);
		} else {
			this.setSize(width, height);
			this.setExtendedState(extendedState);
		}
		
		if (locationX != null && locationY != null) {
			this.setLocation(locationX, locationY);
		}
	}
	
	private void loadSplitPaneSettings() {
		Integer hSplit = Main.SETTINGS.getIntegerProperty("window.horizontal_split");
		Integer vSplit = Main.SETTINGS.getIntegerProperty("window.vertical_split");
		
		if (hSplit != null)
			this.horizontalSplitPane.setDividerLocation(hSplit);
		
		if (vSplit != null)
			this.verticalSplitPane.setDividerLocation(vSplit);
	}
	
	@Override
	public void saveSettings() {
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
	
	@SuppressWarnings("deprecation")
	private void initializeMenuBar() {
		if (OsUtils.isMacOSX()) {
			Application application = Application.getApplication();
			MacApplicationAdapter adapter = new MacApplicationAdapter();
			application.setEnabledPreferencesMenu(true);
			application.addApplicationListener(adapter);
		}
		
		this.menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu(Translations.getString("menu.file"));
		this.menuBar.add(fileMenu);
		
		fileMenu.add(new ActionImportSearchers(16, 16));
		fileMenu.add(new ActionExportSearchers(16, 16));
		fileMenu.addSeparator();
		fileMenu.add(new ActionConfiguration(16, 16));
		fileMenu.addSeparator();
		fileMenu.add(new ActionPrint(16, 16));
		fileMenu.addSeparator();
		fileMenu.add(new ActionQuit(16, 16));
		
		JMenu editMenu = new JMenu(Translations.getString("menu.edit"));
		this.menuBar.add(editMenu);
		
		editMenu.add(new ActionUndo(16, 16));
		editMenu.add(new ActionRedo(16, 16));
		editMenu.addSeparator();
		editMenu.add(new ActionCut(16, 16));
		editMenu.add(new ActionCopy(16, 16));
		editMenu.add(new ActionPaste(16, 16));
		
		JMenu tasksMenu = new JMenu(Translations.getString("menu.tasks"));
		this.menuBar.add(tasksMenu);
		
		tasksMenu.add(new ActionSynchronize(16, 16));
		tasksMenu.add(new ActionScheduledSync(16, 16));
		tasksMenu.addSeparator();
		tasksMenu.add(new ActionAddTask(16, 16));
		tasksMenu.add(new ActionAddTemplateTask(16, 16));
		tasksMenu.add(new ActionBatchAddTasks(16, 16));
		tasksMenu.add(new ActionDelete(16, 16));
		
		JMenu viewMenu = new JMenu(Translations.getString("menu.view"));
		this.menuBar.add(viewMenu);
		
		ButtonGroup group = new ButtonGroup();
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.taskPanel.setView(TaskPanel.View.valueOf(e.getActionCommand()));
			}
			
		};
		
		for (TaskPanel.View view : TaskPanel.View.values()) {
			JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(
					TranslationsUtils.translateTaskPanelView(view));
			menuItem.setActionCommand(view.name());
			menuItem.addActionListener(listener);
			viewMenu.add(menuItem);
			group.add(menuItem);
			
			if (this.taskPanel.getView() == view)
				menuItem.setSelected(true);
		}
		
		JMenu helpMenu = new JMenu(Translations.getString("menu.help"));
		this.menuBar.add(helpMenu);
		
		helpMenu.add(new ActionCheckVersion(false, 16, 16));
		helpMenu.addSeparator();
		helpMenu.add(new ActionHelp(16, 16));
		helpMenu.add(new ActionAbout(16, 16));
		
		this.setJMenuBar(this.menuBar);
	}
	
	private void initializeToolBar() {
		this.toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		this.toolBar.setFloatable(false);
		
		this.toolBar.add(new ActionAddTask());
		this.toolBar.add(new ActionAddTemplateTask());
		this.toolBar.add(new ActionDelete());
		this.toolBar.addSeparator();
		this.toolBar.add(new ActionManageModels());
		this.toolBar.addSeparator();
		this.toolBar.add(new ActionSynchronize());
		this.toolBar.add(new ActionScheduledSync());
		this.toolBar.addSeparator();
		this.toolBar.add(new ActionConfiguration());
		this.toolBar.addSeparator();
		this.toolBar.add(new ActionPrint());
		this.toolBar.addSeparator();
		this.toolBar.add(new ActionHelp());
		
		this.add(this.toolBar, BorderLayout.NORTH);
	}
	
	private void initializeStatusBar() {
		this.statusBar = new StatusBar();
		this.statusBar.initializeScheduledSyncStatus(this.scheduledSyncThread);
		
		this.add(this.statusBar, BorderLayout.SOUTH);
	}
	
	private void initializeSearcherList(JSplitPane horizontalSplitPane) {
		this.searcherPanel = new SearcherPanel();
		this.searcherPanel.addActionListener(this);
		
		horizontalSplitPane.setLeftComponent(this.searcherPanel);
	}
	
	private void initializeTaskPanel(JSplitPane verticalSplitPane) {
		this.taskPanel = new TaskPanel();
		this.taskPanel.addListSelectionListener(this);
		
		verticalSplitPane.setTopComponent(this.taskPanel);
	}
	
	private void initializeTaskNote(JSplitPane verticalSplitPane) {
		this.taskNote = new JTextArea();
		this.taskNote.setText(Translations.getString("error.select_one_task"));
		this.taskNote.setEnabled(false);
		
		verticalSplitPane.setBottomComponent(new JScrollPane(this.taskNote));
	}
	
	private void initializeDefaultTaskSearcher() {
		this.searcherPanel.selectDefaultTaskSearcher();
	}
	
	private void initializeReminderThread() {
		this.reminderThread = new ReminderThread();
		this.reminderThread.start();
	}
	
	private void initializeScheduledSyncThread() {
		this.scheduledSyncThread = new ScheduledSyncThread();
		this.scheduledSyncThread.start();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (this.previousSelectedTask != null) {
			if (!EqualsUtils.equals(
					this.previousSelectedTask.getNote(),
					this.taskNote.getText()))
				this.previousSelectedTask.setNote(this.taskNote.getText());
		}
		
		Task[] tasks = this.taskPanel.getSelectedTasks();
		
		if (tasks.length != 1) {
			this.previousSelectedTask = null;
			
			this.taskNote.setText(Translations.getString("error.select_one_task"));
			this.taskNote.setEnabled(false);
		} else {
			this.previousSelectedTask = tasks[0];
			
			this.taskNote.setText((tasks[0].getNote() == null ? "" : tasks[0].getNote()));
			this.taskNote.setEnabled(true);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals(SearcherPanel.ACT_SEARCHER_SELECTED)) {
			if (this.searcherPanel.getSelectedTaskSearcher() == null)
				return;
			
			this.taskPanel.setTaskSearcher(this.searcherPanel.getSelectedTaskSearcher());
		}
	}
	
	@Override
	public void dispose() {
		this.reminderThread.interrupt();
		this.scheduledSyncThread.interrupt();
		super.dispose();
	}
	
}
