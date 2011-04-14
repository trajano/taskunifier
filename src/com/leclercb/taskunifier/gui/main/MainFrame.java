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
package com.leclercb.taskunifier.gui.main;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.apple.eawt.Application;
import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.properties.SavePropertiesListener;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.actions.ActionAbout;
import com.leclercb.taskunifier.gui.actions.ActionAddSubTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTemplateTask;
import com.leclercb.taskunifier.gui.actions.ActionBatchAddTasks;
import com.leclercb.taskunifier.gui.actions.ActionCheckVersion;
import com.leclercb.taskunifier.gui.actions.ActionConfiguration;
import com.leclercb.taskunifier.gui.actions.ActionCopy;
import com.leclercb.taskunifier.gui.actions.ActionCut;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.actions.ActionDonate;
import com.leclercb.taskunifier.gui.actions.ActionEditTask;
import com.leclercb.taskunifier.gui.actions.ActionExportModels;
import com.leclercb.taskunifier.gui.actions.ActionExportSearchers;
import com.leclercb.taskunifier.gui.actions.ActionExportSettings;
import com.leclercb.taskunifier.gui.actions.ActionExportTemplates;
import com.leclercb.taskunifier.gui.actions.ActionHelp;
import com.leclercb.taskunifier.gui.actions.ActionImportModels;
import com.leclercb.taskunifier.gui.actions.ActionImportSearchers;
import com.leclercb.taskunifier.gui.actions.ActionImportSettings;
import com.leclercb.taskunifier.gui.actions.ActionImportTemplates;
import com.leclercb.taskunifier.gui.actions.ActionLogBug;
import com.leclercb.taskunifier.gui.actions.ActionLogFeatureRequest;
import com.leclercb.taskunifier.gui.actions.ActionManageModels;
import com.leclercb.taskunifier.gui.actions.ActionManagePlugins;
import com.leclercb.taskunifier.gui.actions.ActionManageTemplates;
import com.leclercb.taskunifier.gui.actions.ActionPaste;
import com.leclercb.taskunifier.gui.actions.ActionPrint;
import com.leclercb.taskunifier.gui.actions.ActionQuit;
import com.leclercb.taskunifier.gui.actions.ActionRedo;
import com.leclercb.taskunifier.gui.actions.ActionReview;
import com.leclercb.taskunifier.gui.actions.ActionScheduledSync;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.actions.ActionUndo;
import com.leclercb.taskunifier.gui.actions.MacApplicationAdapter;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.api.templates.TemplateFactory;
import com.leclercb.taskunifier.gui.commons.comparators.TemplateComparator;
import com.leclercb.taskunifier.gui.components.searcherlist.SearcherPanel;
import com.leclercb.taskunifier.gui.components.searcherlist.SearcherView;
import com.leclercb.taskunifier.gui.components.statusbar.AbstractStatusBar;
import com.leclercb.taskunifier.gui.components.statusbar.DefaultStatusBar;
import com.leclercb.taskunifier.gui.components.statusbar.MacStatusBar;
import com.leclercb.taskunifier.gui.components.tasknote.TaskNotePanel;
import com.leclercb.taskunifier.gui.components.tasks.TaskPanel;
import com.leclercb.taskunifier.gui.components.tasks.TaskView;
import com.leclercb.taskunifier.gui.components.toolbar.DefaultToolBarCreator;
import com.leclercb.taskunifier.gui.components.toolbar.MacToolBarCreator;
import com.leclercb.taskunifier.gui.components.toolbar.ToolBarCreator;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.threads.reminder.ReminderThread;
import com.leclercb.taskunifier.gui.threads.scheduledsync.ScheduledSyncThread;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public class MainFrame extends JFrame implements MainView, SavePropertiesListener {
	
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
	
	private JTextField searchField;
	private SearcherPanel searcherPanel;
	private TaskPanel taskPanel;
	
	private MainFrame() {
		this.initialize();
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
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
				this.horizontalSplitPane = ComponentFactory.createThinJScrollPane(JSplitPane.HORIZONTAL_SPLIT);
			} else {
				panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
				
				this.horizontalSplitPane = new JSplitPane(
						JSplitPane.HORIZONTAL_SPLIT);
			}
			
			this.verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			this.verticalSplitPane.setBorder(BorderFactory.createEmptyBorder());
			
			this.horizontalSplitPane.setRightComponent(this.verticalSplitPane);
			
			panel.add(this.horizontalSplitPane, BorderLayout.CENTER);
			
			this.add(panel, BorderLayout.CENTER);
			
			this.loadSplitPaneSettings();
		}
		
		{
			this.initializeSearchField();
			this.initializeSearcherList(this.horizontalSplitPane);
			this.initializeTaskPanel(this.verticalSplitPane);
			this.initializeTaskNote(this.verticalSplitPane);
			this.initializeDefaultTaskSearcher();
			
			this.initializeReminderThread();
			this.initializeScheduledSyncThread();
			
			this.initializeMenuBar();
			this.initializeToolBar();
			this.initializeStatusBar();
		}
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
			this.setSize(640, 480);
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
		if (SystemUtils.IS_OS_MAC) {
			Application application = Application.getApplication();
			MacApplicationAdapter adapter = new MacApplicationAdapter();
			application.setEnabledPreferencesMenu(true);
			application.addApplicationListener(adapter);
		}
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu(Translations.getString("menu.file"));
		menuBar.add(fileMenu);
		
		JMenu importMenu = new JMenu(Translations.getString("general.import"));
		importMenu.setIcon(Images.getResourceImage("download.png", 16, 16));
		
		importMenu.add(new ActionImportModels(16, 16));
		importMenu.add(new ActionImportSearchers(16, 16));
		importMenu.add(new ActionImportSettings(16, 16));
		importMenu.add(new ActionImportTemplates(16, 16));
		fileMenu.add(importMenu);
		
		JMenu exportMenu = new JMenu(Translations.getString("general.export"));
		exportMenu.setIcon(Images.getResourceImage("upload.png", 16, 16));
		
		exportMenu.add(new ActionExportModels(16, 16));
		exportMenu.add(new ActionExportSearchers(16, 16));
		exportMenu.add(new ActionExportSettings(16, 16));
		exportMenu.add(new ActionExportTemplates(16, 16));
		fileMenu.add(exportMenu);
		
		fileMenu.addSeparator();
		fileMenu.add(new ActionConfiguration(16, 16));
		fileMenu.add(new ActionManagePlugins(16, 16));
		fileMenu.add(new ActionManageModels(16, 16));
		fileMenu.add(new ActionManageTemplates(16, 16));
		fileMenu.addSeparator();
		fileMenu.add(new ActionPrint(16, 16));
		fileMenu.addSeparator();
		fileMenu.add(new ActionQuit(16, 16));
		
		JMenu editMenu = new JMenu(Translations.getString("menu.edit"));
		menuBar.add(editMenu);
		
		editMenu.add(new ActionUndo(16, 16));
		editMenu.add(new ActionRedo(16, 16));
		editMenu.addSeparator();
		editMenu.add(new ActionCut(16, 16));
		editMenu.add(new ActionCopy(16, 16));
		editMenu.add(new ActionPaste(16, 16));
		
		JMenu tasksMenu = new JMenu(Translations.getString("menu.tasks"));
		menuBar.add(tasksMenu);
		
		tasksMenu.add(new ActionSynchronize(false, 16, 16));
		tasksMenu.add(new ActionScheduledSync(16, 16));
		tasksMenu.addSeparator();
		tasksMenu.add(new ActionAddTask(16, 16));
		tasksMenu.add(new ActionAddSubTask(this.taskPanel, 16, 16));
		
		// TEMPLATE
		final JMenu templatesMenu = new JMenu(
				Translations.getString("action.name.add_template_task"));
		templatesMenu.setIcon(Images.getResourceImage("duplicate.png", 16, 16));
		tasksMenu.add(templatesMenu);
		
		this.updateTemplateList(templatesMenu, null);
		
		TemplateFactory.getInstance().addListChangeListener(
				new ListChangeListener() {
					
					@Override
					public void listChange(ListChangeEvent event) {
						MainFrame.this.updateTemplateList(templatesMenu, null);
					}
					
				});
		// TEMPLATE
		
		tasksMenu.add(new ActionBatchAddTasks(16, 16));
		tasksMenu.add(new ActionEditTask(this.taskPanel, 16, 16));
		tasksMenu.add(new ActionDelete(16, 16));
		
		JMenu helpMenu = new JMenu(Translations.getString("menu.help"));
		menuBar.add(helpMenu);
		
		helpMenu.add(new ActionCheckVersion(false, 16, 16));
		helpMenu.addSeparator();
		helpMenu.add(new ActionHelp(16, 16));
		helpMenu.add(new ActionAbout(16, 16));
		helpMenu.addSeparator();
		helpMenu.add(new ActionLogBug());
		helpMenu.add(new ActionLogFeatureRequest());
		helpMenu.addSeparator();
		helpMenu.add(new ActionDonate(16, 16));
		helpMenu.add(new ActionReview(16, 16));
		
		this.setJMenuBar(menuBar);
	}
	
	private void initializeToolBar() {
		ToolBarCreator toolBarCreator = null;
		Object[] toolBarObjects = null;
		
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf()) {
			toolBarObjects = this.getToolBarObjects(24, 24);
			toolBarCreator = new MacToolBarCreator();
		} else {
			toolBarObjects = this.getToolBarObjects(24, 24);
			toolBarCreator = new DefaultToolBarCreator();
		}
		
		for (Object toolBarObject : toolBarObjects) {
			if (toolBarObject == null) {
				toolBarCreator.addSeparatorToLeft();
				continue;
			}
			
			if (toolBarObject instanceof Action) {
				JButton button = new JButton((Action) toolBarObject);
				if (SystemUtils.IS_OS_MAC
						&& LookAndFeelUtils.isCurrentLafSystemLaf())
					((MacToolBarCreator) toolBarCreator).optimizeButton(button);
				
				toolBarCreator.addElementToLeft(button);
				continue;
			}
			
			if (toolBarObject instanceof JButton) {
				if (SystemUtils.IS_OS_MAC
						&& LookAndFeelUtils.isCurrentLafSystemLaf())
					((MacToolBarCreator) toolBarCreator).optimizeButton((JButton) toolBarObject);
				
				toolBarCreator.addElementToLeft((JButton) toolBarObject);
				continue;
			}
		}
		
		// SEARCH FIELD
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf())
			toolBarCreator.addElementToRight(ComponentFactory.createSearchField(this.searchField));
		// SEARCH FIELD
		
		this.add(toolBarCreator.getComponent(), BorderLayout.NORTH);
	}
	
	private Object[] getToolBarObjects(final int iconWith, final int iconHeight) {
		// TEMPLATE
		final JPopupMenu popupMenu = new JPopupMenu(
				Translations.getString("action.name.add_template_task"));
		
		this.updateTemplateList(null, popupMenu);
		
		TemplateFactory.getInstance().addListChangeListener(
				new ListChangeListener() {
					
					@Override
					public void listChange(ListChangeEvent event) {
						MainFrame.this.updateTemplateList(null, popupMenu);
					}
					
				});
		
		final JButton addTemplateTaskButton = new JButton();
		
		Action actionAddTemplateTask = new AbstractAction() {
			
			{
				this.putValue(
						NAME,
						Translations.getString("action.name.add_template_task"));
				
				this.putValue(SMALL_ICON, Images.getResourceImage(
						"duplicate.png",
						iconWith,
						iconHeight));
				
				this.putValue(
						SHORT_DESCRIPTION,
						Translations.getString("action.description.add_template_task"));
			}
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				popupMenu.show(
						addTemplateTaskButton,
						addTemplateTaskButton.getX(),
						addTemplateTaskButton.getY());
			}
			
		};
		
		addTemplateTaskButton.setAction(actionAddTemplateTask);
		// TEMPLATE
		
		return new Object[] {
				new ActionAddTask(iconWith, iconHeight),
				addTemplateTaskButton,
				new ActionDelete(iconWith, iconHeight),
				null,
				new ActionSynchronize(false, iconWith, iconHeight),
				new ActionScheduledSync(iconWith, iconHeight),
				null,
				new ActionConfiguration(iconWith, iconHeight) };
	}
	
	private void initializeStatusBar() {
		AbstractStatusBar statusBar = null;
		
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf())
			statusBar = new MacStatusBar();
		else
			statusBar = new DefaultStatusBar();
		
		statusBar.initializeScheduledSyncStatus(this.scheduledSyncThread);
		
		this.add(statusBar, BorderLayout.SOUTH);
	}
	
	private void initializeSearchField() {
		this.searchField = new JTextField(15);
		
		this.searchField.getDocument().addDocumentListener(
				new DocumentListener() {
					
					@Override
					public void removeUpdate(DocumentEvent e) {
						MainFrame.this.searcherPanel.setTitleFilter(MainFrame.this.searchField.getText());
					}
					
					@Override
					public void insertUpdate(DocumentEvent e) {
						MainFrame.this.searcherPanel.setTitleFilter(MainFrame.this.searchField.getText());
					}
					
					@Override
					public void changedUpdate(DocumentEvent e) {
						MainFrame.this.searcherPanel.setTitleFilter(MainFrame.this.searchField.getText());
					}
					
				});
	}
	
	private void initializeSearcherList(JSplitPane horizontalSplitPane) {
		JPanel panel = new JPanel(new BorderLayout());
		
		if (!(SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf())) {
			panel.add(
					ComponentFactory.createSearchField(this.searchField),
					BorderLayout.NORTH);
		}
		
		this.searcherPanel = new SearcherPanel();
		
		this.searcherPanel.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						SearcherPanel.PROP_TITLE_FILTER)) {
					String filter = (String) evt.getNewValue();
					if (!MainFrame.this.searchField.getText().equals(filter))
						MainFrame.this.searchField.setText(filter);
				}
			}
			
		});
		
		panel.add(this.searcherPanel, BorderLayout.CENTER);
		
		horizontalSplitPane.setLeftComponent(panel);
	}
	
	private void initializeTaskPanel(JSplitPane verticalSplitPane) {
		JPanel panel = new JPanel(new BorderLayout());
		
		this.taskPanel = new TaskPanel();
		this.searcherPanel.addTaskSearcherSelectionChangeListener(this.taskPanel);
		
		panel.add(this.taskPanel, BorderLayout.CENTER);
		
		verticalSplitPane.setTopComponent(panel);
	}
	
	private void initializeTaskNote(JSplitPane verticalSplitPane) {
		JPanel panel = new JPanel(new BorderLayout());
		
		TaskNotePanel taskNote = new TaskNotePanel();
		this.taskPanel.addTaskSelectionChangeListener(taskNote);
		
		panel.add(taskNote, BorderLayout.CENTER);
		
		verticalSplitPane.setBottomComponent(panel);
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
	public void dispose() {
		this.reminderThread.interrupt();
		this.scheduledSyncThread.interrupt();
		super.dispose();
	}
	
	private void updateTemplateList(JMenu menu, JPopupMenu popupMenu) {
		if (menu != null)
			menu.removeAll();
		
		if (popupMenu != null)
			popupMenu.removeAll();
		
		List<Template> templates = new ArrayList<Template>(
				TemplateFactory.getInstance().getList());
		Collections.sort(templates, new TemplateComparator());
		
		for (Template template : templates) {
			if (menu != null)
				menu.add(new ActionAddTemplateTask(template, 16, 16));
			
			if (popupMenu != null)
				popupMenu.add(new ActionAddTemplateTask(template, 16, 16));
		}
		
		if (menu != null) {
			menu.addSeparator();
			menu.add(new ActionManageTemplates(16, 16));
		}
		
		if (popupMenu != null) {
			popupMenu.addSeparator();
			popupMenu.add(new ActionManageTemplates(16, 16));
		}
	}
	
}
