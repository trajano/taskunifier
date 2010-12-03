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
import java.text.MessageFormat;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable.PrintMode;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.settings.SaveSettingsListener;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.actions.ActionAbout;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionConfiguration;
import com.leclercb.taskunifier.gui.actions.ActionCopy;
import com.leclercb.taskunifier.gui.actions.ActionCut;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.actions.ActionManageModels;
import com.leclercb.taskunifier.gui.actions.ActionPaste;
import com.leclercb.taskunifier.gui.actions.ActionPrint;
import com.leclercb.taskunifier.gui.actions.ActionQuit;
import com.leclercb.taskunifier.gui.actions.ActionRedo;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.actions.ActionUndo;
import com.leclercb.taskunifier.gui.components.searcherlist.SearcherPanel;
import com.leclercb.taskunifier.gui.components.statusbar.StatusBar;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.reminder.ReminderThread;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.translations.Translations;

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
	private TaskTable taskTable;
	private JTextArea taskNote;
	private StatusBar statusBar;

	private Task previousSelectedTask;

	private MainFrame() {
		this.initialize();
	}

	private void initialize() {
		Settings.addSaveSettingsListener(this);

		this.setIconImage(Images.getImage("logo.png", 16, 16).getImage());
		this.setTitle(Constants.TITLE + " - " + Constants.VERSION);
		this.loadWindowSizeSettings();

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent event) {
				Main.stop();
			}

			@Override
			public void windowClosed(WindowEvent event) {

			}

		});

		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		this.loadTaskColumnSettings();

		this.horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		this.horizontalSplitPane.setBorder(new EmptyBorder(3, 0, 3, 0));

		this.loadSplitPaneSettings();

		this.initializeSearcherList(horizontalSplitPane);
		this.initializeTaskTable(verticalSplitPane);
		this.initializeTaskNote(verticalSplitPane);
		this.initializeDefaultTaskSearcher();

		horizontalSplitPane.setRightComponent(verticalSplitPane);

		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(horizontalSplitPane, BorderLayout.CENTER);

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
		this.taskTable.setSelectedTask(task);
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
	public Task getSelectedTask() {
		return this.taskTable.getSelectedTask();
	}

	@Override
	public void refreshTasks() {
		this.taskTable.refreshTasks();
	}

	@Override
	public void printTasks() throws HeadlessException, PrinterException {
		this.taskTable.print(
				PrintMode.FIT_WIDTH, 
				new MessageFormat(Constants.TITLE + " - " + this.taskTable.getTaskSearcher().getTitle()), 
				new MessageFormat(this.taskTable.getRowCount() + " tasks | Page - {0}"), 
				true, 
				null, 
				true);
	}

	private void loadWindowSizeSettings() {
		Integer extendedState = Settings.getIntegerProperty("window.extended_state");
		Integer width = Settings.getIntegerProperty("window.width");
		Integer height = Settings.getIntegerProperty("window.height");
		Integer locationX = Settings.getIntegerProperty("window.location_x");
		Integer locationY = Settings.getIntegerProperty("window.location_y");

		if (width == null || height == null || extendedState == null) {
			this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
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
			horizontalSplitPane.setDividerLocation(hSplit);

		if (vSplit != null)
			verticalSplitPane.setDividerLocation(vSplit);
	}

	private void loadTaskColumnSettings() {
		TaskColumn[] columns = TaskColumn.values();
		for (int i=0; i<columns.length; i++) {
			Integer order = Settings.getIntegerProperty("taskcolumn." + columns[i].name().toLowerCase() + ".order");
			Integer width = Settings.getIntegerProperty("taskcolumn." + columns[i].name().toLowerCase() + ".width");
			Boolean visible = Settings.getBooleanProperty("taskcolumn." + columns[i].name().toLowerCase() + ".visible");

			if (order == null)
				order = 0;

			if (width == null)
				width = 100;

			if (visible == null)
				visible = true;

			columns[i].setOrder(order);
			columns[i].setWidth(width);
			columns[i].setVisible(visible);
		}
	}

	@Override
	public void saveSettings() {
		Settings.setIntegerProperty("window.extended_state", this.getExtendedState());
		Settings.setIntegerProperty("window.width", this.getWidth());
		Settings.setIntegerProperty("window.height", this.getHeight());
		Settings.setIntegerProperty("window.location_x", (int) this.getLocationOnScreen().getX());
		Settings.setIntegerProperty("window.location_y", (int) this.getLocationOnScreen().getY());

		Settings.setIntegerProperty("window.horizontal_split", this.horizontalSplitPane.getDividerLocation());
		Settings.setIntegerProperty("window.vertical_split", this.verticalSplitPane.getDividerLocation());

		TaskColumn[] taskColumns = TaskColumn.getValues(false);
		for (int i=0; i<taskColumns.length; i++) {
			Settings.setBooleanProperty("taskcolumn." + taskColumns[i].name().toLowerCase() + ".visible", false);
		}

		int i = 0;
		Enumeration<TableColumn> columns = this.taskTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();
			TaskColumn taskColumn = (TaskColumn) column.getIdentifier();

			Settings.setIntegerProperty("taskcolumn." + taskColumn.name().toLowerCase() + ".order", i);
			Settings.setIntegerProperty("taskcolumn." + taskColumn.name().toLowerCase() + ".width", column.getWidth());
			Settings.setBooleanProperty("taskcolumn." + taskColumn.name().toLowerCase() + ".visible", taskColumn.isVisible());

			i++;
		}
	}

	private void initializeMenuBar() {
		this.menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(Translations.getString("menu.file"));
		fileMenu.setMnemonic('F');
		this.menuBar.add(fileMenu);

		fileMenu.add(new ActionAbout(16, 16));
		fileMenu.add(new ActionPrint(16, 16));
		fileMenu.add(new ActionQuit(16, 16));

		JMenu editMenu = new JMenu(Translations.getString("menu.edit"));
		editMenu.setMnemonic('E');
		this.menuBar.add(editMenu);

		editMenu.add(new ActionUndo(16, 16));
		editMenu.add(new ActionRedo(16, 16));
		editMenu.addSeparator();
		editMenu.add(new ActionCut(16, 16));
		editMenu.add(new ActionCopy(16, 16));
		editMenu.add(new ActionPaste(16, 16));

		this.setJMenuBar(this.menuBar);
	}

	private void initializeToolBar() {
		this.toolBar = new JToolBar(JToolBar.HORIZONTAL);
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

		this.add(this.toolBar, BorderLayout.NORTH);
	}

	private void initializeStatusBar() {
		this.statusBar = new StatusBar();

		this.add(this.statusBar, BorderLayout.SOUTH);
	}

	private void initializeSearcherList(JSplitPane horizontalSplitPane) {
		this.searcherPanel = new SearcherPanel();
		this.searcherPanel.addActionListener(this);

		horizontalSplitPane.setLeftComponent(new JScrollPane(this.searcherPanel));
	}

	private void initializeTaskTable(JSplitPane verticalSplitPane) {
		this.taskTable = new TaskTable();
		this.taskTable.getSelectionModel().addListSelectionListener(this);

		verticalSplitPane.setTopComponent(new JScrollPane(this.taskTable));
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
		if (event.getSource().equals(this.taskTable.getSelectionModel())) {
			if (this.previousSelectedTask != null) {
				if (!EqualsUtils.equals(this.previousSelectedTask.getNote(), this.taskNote.getText()))
					this.previousSelectedTask.setNote(this.taskNote.getText());
			}

			Task task = this.taskTable.getSelectedTask();

			this.previousSelectedTask = task;

			if (task == null) {
				this.taskNote.setText("");
				this.taskNote.setEnabled(false);
			} else {
				this.taskNote.setText(task.getNote() == null? "" : task.getNote());
				this.taskNote.setEnabled(true);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals(SearcherPanel.ACT_SEARCHER_SELECTED)) {
			if (searcherPanel.getSelectedTaskSearcher() == null)
				return;

			this.taskTable.setTaskSearcher(searcherPanel.getSelectedTaskSearcher());
		}
	}

}
