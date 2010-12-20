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
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;

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

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.settings.SaveSettingsListener;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.actions.ActionAbout;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionCheckVersion;
import com.leclercb.taskunifier.gui.actions.ActionConfiguration;
import com.leclercb.taskunifier.gui.actions.ActionCopy;
import com.leclercb.taskunifier.gui.actions.ActionCut;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.actions.ActionHelp;
import com.leclercb.taskunifier.gui.actions.ActionManageModels;
import com.leclercb.taskunifier.gui.actions.ActionPaste;
import com.leclercb.taskunifier.gui.actions.ActionPrint;
import com.leclercb.taskunifier.gui.actions.ActionQuit;
import com.leclercb.taskunifier.gui.actions.ActionRedo;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.actions.ActionUndo;
import com.leclercb.taskunifier.gui.actions.MacApplicationAdapter;
import com.leclercb.taskunifier.gui.components.searcherlist.SearcherPanel;
import com.leclercb.taskunifier.gui.components.statusbar.StatusBar;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.TaskPanel;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.reminder.ReminderThread;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;
import com.leclercb.taskunifier.gui.utils.OsUtils;

public class MainFrame extends JFrame implements ListSelectionListener, SaveSettingsListener, ActionListener, ServiceFrame {
	
	private static ServiceFrame INSTANCE;
	
	public static ServiceFrame getInstance() {
		if (INSTANCE == null)
			INSTANCE = new MainFrame();
		
		return INSTANCE;
	}
	
	private ReminderThread reminderThread;
	
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
		Settings.addSaveSettingsListener(this);
		
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
		
		this.horizontalSplitPane.setBorder(new EmptyBorder(3, 0, 3, 0));
		
		this.loadSplitPaneSettings();
		
		this.initializeSearcherList(this.horizontalSplitPane);
		this.initializeTaskPanel(this.verticalSplitPane);
		this.initializeTaskNote(this.verticalSplitPane);
		this.initializeDefaultTaskSearcher();
		
		this.horizontalSplitPane.setRightComponent(this.verticalSplitPane);
		
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(this.horizontalSplitPane, BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.CENTER);
		
		this.initializeMenuBar();
		this.initializeToolBar();
		this.initializeStatusBar();
		
		this.initializeReminderThread();
	}
	
	@Override
	public Frame getFrame() {
		return this;
	}
	
	@Override
	public void setSelectedTask(Task task) {
		this.taskPanel.setSelectedTask(task);
	}
	
	@Override
	public void selectDefaultTaskSearcher() {
		this.searcherPanel.selectDefaultTaskSearcher();
	}
	
	@Override
	public TaskSearcher getSelectedTaskSearcher() {
		return this.searcherPanel.getSelectedTaskSearcher();
	}
	
	@Override
	public void showColumn(TaskColumn taskColumn, boolean show) {
		this.taskPanel.showColumn(taskColumn, show);
	}
	
	@Override
	public Task getSelectedTask() {
		return this.taskPanel.getSelectedTask();
	}
	
	@Override
	public void refreshTasks() {
		this.taskPanel.refreshTasks();
	}
	
	@Override
	public void printTasks() throws HeadlessException, PrinterException {
		this.taskPanel.printTasks();
	}
	
	private void loadWindowSizeSettings() {
		Integer extendedState = Settings.getIntegerProperty("window.extended_state");
		Integer width = Settings.getIntegerProperty("window.width");
		Integer height = Settings.getIntegerProperty("window.height");
		Integer locationX = Settings.getIntegerProperty("window.location_x");
		Integer locationY = Settings.getIntegerProperty("window.location_y");
		
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
		Integer hSplit = Settings.getIntegerProperty("window.horizontal_split");
		Integer vSplit = Settings.getIntegerProperty("window.vertical_split");
		
		if (hSplit != null)
			this.horizontalSplitPane.setDividerLocation(hSplit);
		
		if (vSplit != null)
			this.verticalSplitPane.setDividerLocation(vSplit);
	}
	
	@Override
	public void saveSettings() {
		Settings.setIntegerProperty(
				"window.extended_state",
				this.getExtendedState());
		Settings.setIntegerProperty("window.width", this.getWidth());
		Settings.setIntegerProperty("window.height", this.getHeight());
		Settings.setIntegerProperty(
				"window.location_x",
				(int) this.getLocationOnScreen().getX());
		Settings.setIntegerProperty(
				"window.location_y",
				(int) this.getLocationOnScreen().getY());
		
		Settings.setIntegerProperty(
				"window.horizontal_split",
				this.horizontalSplitPane.getDividerLocation());
		Settings.setIntegerProperty(
				"window.vertical_split",
				this.verticalSplitPane.getDividerLocation());
	}
	
	@SuppressWarnings("deprecation")
	private void initializeMenuBar() {
		if (OsUtils.isMacOSX()) {
			com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
			MacApplicationAdapter adapter = new MacApplicationAdapter();
			application.setEnabledPreferencesMenu(true);
			application.addApplicationListener(adapter);
		}
		
		this.menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu(Translations.getString("menu.file"));
		fileMenu.setMnemonic('F');
		this.menuBar.add(fileMenu);
		
		fileMenu.add(new ActionPrint(16, 16));
		
		if (!OsUtils.isMacOSX()) {
			fileMenu.add(new ActionConfiguration(16, 16));
			fileMenu.add(new ActionQuit(16, 16));
		}
		
		JMenu editMenu = new JMenu(Translations.getString("menu.edit"));
		editMenu.setMnemonic('E');
		this.menuBar.add(editMenu);
		
		editMenu.add(new ActionUndo(16, 16));
		editMenu.add(new ActionRedo(16, 16));
		editMenu.addSeparator();
		editMenu.add(new ActionCut(16, 16));
		editMenu.add(new ActionCopy(16, 16));
		editMenu.add(new ActionPaste(16, 16));
		
		JMenu viewMenu = new JMenu(Translations.getString("menu.view"));
		viewMenu.setMnemonic('V');
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
		helpMenu.setMnemonic('H');
		this.menuBar.add(helpMenu);
		
		helpMenu.add(new ActionCheckVersion(false, 16, 16));
		editMenu.addSeparator();
		helpMenu.add(new ActionHelp(16, 16));
		
		if (!OsUtils.isMacOSX())
			helpMenu.add(new ActionAbout(16, 16));
		
		this.setJMenuBar(this.menuBar);
	}
	
	private void initializeToolBar() {
		this.toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		this.toolBar.setFloatable(false);
		
		this.toolBar.add(new ActionAddTask());
		this.toolBar.add(new ActionDelete());
		this.toolBar.addSeparator();
		this.toolBar.add(new ActionManageModels());
		this.toolBar.addSeparator();
		this.toolBar.add(new ActionSynchronize());
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
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (this.previousSelectedTask != null) {
			if (!EqualsUtils.equals(
					this.previousSelectedTask.getNote(),
					this.taskNote.getText()))
				this.previousSelectedTask.setNote(this.taskNote.getText());
		}
		
		Task task = this.taskPanel.getSelectedTask();
		
		this.previousSelectedTask = task;
		
		if (task == null) {
			this.taskNote.setText("");
			this.taskNote.setEnabled(false);
		} else {
			this.taskNote.setText(task.getNote() == null ? "" : task.getNote());
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
		super.dispose();
	}
	
}
