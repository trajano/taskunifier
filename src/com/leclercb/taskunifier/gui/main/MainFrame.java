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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXSearchField;

import com.apple.eawt.Application;
import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.actions.ActionAbout;
import com.leclercb.taskunifier.gui.actions.ActionAddNote;
import com.leclercb.taskunifier.gui.actions.ActionAddSubTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTemplateTask;
import com.leclercb.taskunifier.gui.actions.ActionBatchAddTasks;
import com.leclercb.taskunifier.gui.actions.ActionChangeView;
import com.leclercb.taskunifier.gui.actions.ActionCheckPluginVersion;
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
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.modelnote.ModelNotePanel;
import com.leclercb.taskunifier.gui.components.notes.NotePanel;
import com.leclercb.taskunifier.gui.components.notes.NoteView;
import com.leclercb.taskunifier.gui.components.searcherlist.SearcherPanel;
import com.leclercb.taskunifier.gui.components.searcherlist.SearcherView;
import com.leclercb.taskunifier.gui.components.statusbar.AbstractStatusBar;
import com.leclercb.taskunifier.gui.components.statusbar.DefaultStatusBar;
import com.leclercb.taskunifier.gui.components.statusbar.MacStatusBar;
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

public class MainFrame extends JFrame implements MainView, SavePropertiesListener, PropertyChangeSupported {
	
	public static final String PROP_SELECTED_VIEW = "selectedView";
	
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
	private JPanel middlePane;
	
	private JXSearchField searchField;
	private SearcherPanel searcherPanel;
	
	private NotePanel notePanel;
	private TaskPanel taskPanel;
	
	private ModelNotePanel modelNote;
	
	private View selectedView;
	
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
			this.initializeSearcherList(this.horizontalSplitPane);
			this.initializeNotePanel(this.middlePane);
			this.initializeTaskPanel(this.middlePane);
			this.initializeModelNote(this.verticalSplitPane);
			
			this.setSelectedView(View.TASKS);
			
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
	public View getSelectedView() {
		return this.selectedView;
	}
	
	@Override
	public void setSelectedView(View view) {
		CheckUtils.isNotNull(view, "View cannot be null");
		
		if (this.selectedView == view)
			return;
		
		CardLayout layout = (CardLayout) this.middlePane.getLayout();
		layout.show(this.middlePane, view.name());
		
		this.searchField.setText(null);
		
		if (view == View.NOTES)
			this.modelNote.modelSelectionChange(new ModelSelectionChangeEvent(
					this,
					this.notePanel.getSelectedNotes()));
		else if (view == View.TASKS)
			this.modelNote.modelSelectionChange(new ModelSelectionChangeEvent(
					this,
					this.taskPanel.getSelectedTasks()));
		
		View oldSelectedView = this.selectedView;
		this.selectedView = view;
		
		this.firePropertyChange(PROP_SELECTED_VIEW, oldSelectedView, view);
	}
	
	@Override
	public NoteView getNoteView() {
		return this.notePanel;
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
		
		JMenu viewMenu = new JMenu(Translations.getString("menu.view"));
		menuBar.add(viewMenu);
		
		viewMenu.add(new ActionChangeView(16, 16));
		viewMenu.addSeparator();
		
		ButtonGroup viewGroup = new ButtonGroup();
		
		for (View view : View.values()) {
			final View v = view;
			final JRadioButtonMenuItem item = new JRadioButtonMenuItem(
					v.getLabel());
			viewGroup.add(item);
			viewMenu.add(item);
			
			if (this.getSelectedView() == view)
				item.setSelected(true);
			
			item.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent evt) {
					MainFrame.this.setSelectedView(v);
				}
				
			});
			
			this.addPropertyChangeListener(
					PROP_SELECTED_VIEW,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							if (EqualsUtils.equals(evt.getNewValue(), v))
								item.setSelected(true);
						}
						
					});
		}
		
		JMenu notesMenu = new JMenu(Translations.getString("menu.notes"));
		menuBar.add(notesMenu);
		
		notesMenu.add(new ActionAddNote(16, 16));
		notesMenu.add(new ActionDelete(16, 16));
		
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
		helpMenu.add(new ActionCheckPluginVersion(false, 16, 16));
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
			toolBarCreator.addElementToRight(this.searchField);
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
				new ActionAddNote(iconWith, iconHeight),
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
		this.searchField = new JXSearchField("Search");
		
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (MainFrame.this.getSelectedView() == View.NOTES)
					MainFrame.this.notePanel.setTitleFilter(e.getActionCommand());
				else
					MainFrame.this.searcherPanel.setTitleFilter(e.getActionCommand());
			}
			
		});
	}
	
	private void initializeSearcherList(JSplitPane horizontalSplitPane) {
		JPanel panel = new JPanel(new BorderLayout());
		
		if (!(SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf())) {
			panel.add(this.searchField, BorderLayout.NORTH);
		}
		
		this.searcherPanel = new SearcherPanel();
		
		this.searcherPanel.addTaskSearcherSelectionChangeListener(new TaskSearcherSelectionListener() {
			
			@Override
			public void taskSearcherSelectionChange(
					TaskSearcherSelectionChangeEvent event) {
				MainFrame.this.setSelectedView(View.TASKS);
			}
			
		});
		
		this.searcherPanel.addPropertyChangeListener(
				SearcherPanel.PROP_TITLE_FILTER,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						String filter = (String) evt.getNewValue();
						if (!MainFrame.this.searchField.getText().equals(filter))
							MainFrame.this.searchField.setText(filter);
					}
					
				});
		
		panel.add(this.searcherPanel, BorderLayout.CENTER);
		
		horizontalSplitPane.setLeftComponent(panel);
	}
	
	private void initializeNotePanel(JPanel middlePane) {
		this.notePanel = new NotePanel();
		
		middlePane.add(this.notePanel, View.NOTES.name());
	}
	
	private void initializeTaskPanel(JPanel middlePane) {
		this.taskPanel = new TaskPanel();
		this.searcherPanel.addTaskSearcherSelectionChangeListener(this.taskPanel);
		this.searcherPanel.addPropertyChangeListener(
				SearcherPanel.PROP_TITLE_FILTER,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						MainFrame.this.taskPanel.taskSearcherSelectionChange(new TaskSearcherSelectionChangeEvent(
								evt.getSource(),
								MainFrame.this.searcherPanel.getSelectedTaskSearcher()));
					}
					
				});
		
		middlePane.add(this.taskPanel, View.TASKS.name());
	}
	
	private void initializeModelNote(JSplitPane verticalSplitPane) {
		JPanel panel = new JPanel(new BorderLayout());
		
		this.modelNote = new ModelNotePanel();
		
		this.notePanel.addModelSelectionChangeListener(new ModelSelectionListener() {
			
			@Override
			public void modelSelectionChange(ModelSelectionChangeEvent event) {
				if (MainFrame.this.getSelectedView() == View.NOTES)
					MainFrame.this.modelNote.modelSelectionChange(event);
			}
			
		});
		
		this.taskPanel.addModelSelectionChangeListener(new ModelSelectionListener() {
			
			@Override
			public void modelSelectionChange(ModelSelectionChangeEvent event) {
				if (MainFrame.this.getSelectedView() == View.TASKS)
					MainFrame.this.modelNote.modelSelectionChange(event);
			}
			
		});
		
		panel.add(this.modelNote, BorderLayout.CENTER);
		
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
