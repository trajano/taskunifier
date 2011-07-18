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
package com.leclercb.taskunifier.gui.components.menubar;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

import com.apple.eawt.Application;
import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.templates.TaskTemplateFactory;
import com.leclercb.taskunifier.gui.actions.ActionAbout;
import com.leclercb.taskunifier.gui.actions.ActionAddNote;
import com.leclercb.taskunifier.gui.actions.ActionAddSubTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTask;
import com.leclercb.taskunifier.gui.actions.ActionAddTaskSearcher;
import com.leclercb.taskunifier.gui.actions.ActionBatchAddTasks;
import com.leclercb.taskunifier.gui.actions.ActionChangeDataFolderLocation;
import com.leclercb.taskunifier.gui.actions.ActionChangeView;
import com.leclercb.taskunifier.gui.actions.ActionCheckPluginVersion;
import com.leclercb.taskunifier.gui.actions.ActionCheckVersion;
import com.leclercb.taskunifier.gui.actions.ActionCollapseAll;
import com.leclercb.taskunifier.gui.actions.ActionConfiguration;
import com.leclercb.taskunifier.gui.actions.ActionCopy;
import com.leclercb.taskunifier.gui.actions.ActionCut;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.actions.ActionDeleteTaskSearcher;
import com.leclercb.taskunifier.gui.actions.ActionDonate;
import com.leclercb.taskunifier.gui.actions.ActionDuplicateNotes;
import com.leclercb.taskunifier.gui.actions.ActionDuplicateTasks;
import com.leclercb.taskunifier.gui.actions.ActionEditTaskSearcher;
import com.leclercb.taskunifier.gui.actions.ActionEditTasks;
import com.leclercb.taskunifier.gui.actions.ActionExpandAll;
import com.leclercb.taskunifier.gui.actions.ActionExportModels;
import com.leclercb.taskunifier.gui.actions.ActionExportSettings;
import com.leclercb.taskunifier.gui.actions.ActionExportTaskSearchers;
import com.leclercb.taskunifier.gui.actions.ActionExportTaskTemplates;
import com.leclercb.taskunifier.gui.actions.ActionHelp;
import com.leclercb.taskunifier.gui.actions.ActionImportModels;
import com.leclercb.taskunifier.gui.actions.ActionImportSettings;
import com.leclercb.taskunifier.gui.actions.ActionImportTaskSearchers;
import com.leclercb.taskunifier.gui.actions.ActionImportTaskTemplates;
import com.leclercb.taskunifier.gui.actions.ActionLogBug;
import com.leclercb.taskunifier.gui.actions.ActionLogFeatureRequest;
import com.leclercb.taskunifier.gui.actions.ActionManageModels;
import com.leclercb.taskunifier.gui.actions.ActionManagePlugins;
import com.leclercb.taskunifier.gui.actions.ActionManageTaskTemplates;
import com.leclercb.taskunifier.gui.actions.ActionPaste;
import com.leclercb.taskunifier.gui.actions.ActionPrint;
import com.leclercb.taskunifier.gui.actions.ActionQuit;
import com.leclercb.taskunifier.gui.actions.ActionRedo;
import com.leclercb.taskunifier.gui.actions.ActionReview;
import com.leclercb.taskunifier.gui.actions.ActionScheduledSync;
import com.leclercb.taskunifier.gui.actions.ActionShowTips;
import com.leclercb.taskunifier.gui.actions.ActionSynchronize;
import com.leclercb.taskunifier.gui.actions.ActionUndo;
import com.leclercb.taskunifier.gui.actions.MacApplicationAdapter;
import com.leclercb.taskunifier.gui.components.tasks.TaskView;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.main.View;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.TemplateUtils;

public class MenuBar extends JMenuBar {
	
	private MainView mainView;
	private TaskView taskView;
	private TaskSearcherView taskSearcherView;
	
	public MenuBar(
			MainView mainView,
			TaskView taskView,
			TaskSearcherView taskSearcherView) {
		CheckUtils.isNotNull(mainView, "Main view cannot be null");
		CheckUtils.isNotNull(taskView, "Task view cannot be null");
		CheckUtils.isNotNull(
				taskSearcherView,
				"Task searcher view cannot be null");
		
		this.mainView = mainView;
		this.taskView = taskView;
		this.taskSearcherView = taskSearcherView;
		
		this.initialize();
	}
	
	@SuppressWarnings("deprecation")
	private void initialize() {
		if (SystemUtils.IS_OS_MAC) {
			Application application = Application.getApplication();
			MacApplicationAdapter adapter = new MacApplicationAdapter();
			application.setEnabledPreferencesMenu(true);
			application.addApplicationListener(adapter);
		}
		
		this.initializeFileMenu();
		this.initializeEditMenu();
		this.initializeViewMenu();
		this.initializeNoteMenu();
		this.initializeTaskMenu();
		this.initializeSynchronizeMenu();
		this.initializeHelpMenu();
	}
	
	private void initializeFileMenu() {
		JMenu fileMenu = new JMenu(Translations.getString("menu.file"));
		this.add(fileMenu);
		
		fileMenu.add(new ActionChangeDataFolderLocation(16, 16));
		fileMenu.addSeparator();
		
		JMenu importMenu = new JMenu(Translations.getString("general.import"));
		importMenu.setIcon(Images.getResourceImage("download.png", 16, 16));
		
		importMenu.add(new ActionImportModels(16, 16));
		importMenu.add(new ActionImportSettings(16, 16));
		importMenu.add(new ActionImportTaskSearchers(16, 16));
		importMenu.add(new ActionImportTaskTemplates(16, 16));
		fileMenu.add(importMenu);
		
		JMenu exportMenu = new JMenu(Translations.getString("general.export"));
		exportMenu.setIcon(Images.getResourceImage("upload.png", 16, 16));
		
		exportMenu.add(new ActionExportModels(16, 16));
		exportMenu.add(new ActionExportSettings(16, 16));
		exportMenu.add(new ActionExportTaskSearchers(16, 16));
		exportMenu.add(new ActionExportTaskTemplates(16, 16));
		fileMenu.add(exportMenu);
		
		fileMenu.addSeparator();
		fileMenu.add(new ActionConfiguration(16, 16));
		fileMenu.add(new ActionManagePlugins(16, 16));
		fileMenu.add(new ActionManageModels(16, 16));
		fileMenu.add(new ActionManageTaskTemplates(16, 16));
		fileMenu.addSeparator();
		fileMenu.add(new ActionPrint(16, 16));
		fileMenu.addSeparator();
		fileMenu.add(new ActionQuit(16, 16));
	}
	
	private void initializeEditMenu() {
		JMenu editMenu = new JMenu(Translations.getString("menu.edit"));
		this.add(editMenu);
		
		editMenu.add(new ActionUndo(16, 16));
		editMenu.add(new ActionRedo(16, 16));
		editMenu.addSeparator();
		editMenu.add(new ActionCut(16, 16));
		editMenu.add(new ActionCopy(16, 16));
		editMenu.add(new ActionPaste(16, 16));
	}
	
	private void initializeViewMenu() {
		JMenu viewMenu = new JMenu(Translations.getString("menu.view"));
		this.add(viewMenu);
		
		viewMenu.add(new ActionChangeView(this.mainView, 16, 16));
		viewMenu.addSeparator();
		
		ButtonGroup viewGroup = new ButtonGroup();
		
		for (View view : View.values()) {
			final View v = view;
			final JRadioButtonMenuItem item = new JRadioButtonMenuItem(
					v.getLabel());
			viewGroup.add(item);
			viewMenu.add(item);
			
			if (this.mainView.getSelectedView() == view)
				item.setSelected(true);
			
			item.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent evt) {
					MenuBar.this.mainView.setSelectedView(v);
				}
				
			});
			
			this.mainView.addPropertyChangeListener(
					MainView.PROP_SELECTED_VIEW,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							if (EqualsUtils.equals(evt.getNewValue(), v))
								item.setSelected(true);
						}
						
					});
		}
	}
	
	private void initializeNoteMenu() {
		JMenu notesMenu = new JMenu(Translations.getString("menu.notes"));
		this.add(notesMenu);
		
		notesMenu.add(new ActionAddNote(16, 16));
		notesMenu.add(new ActionDuplicateNotes(16, 16));
		notesMenu.add(new ActionDelete(16, 16));
	}
	
	private void initializeTaskMenu() {
		JMenu tasksMenu = new JMenu(Translations.getString("menu.tasks"));
		this.add(tasksMenu);
		
		tasksMenu.add(new ActionAddTask(16, 16));
		tasksMenu.add(new ActionAddSubTask(this.taskView, 16, 16));
		
		this.initializeTemplateMenu(tasksMenu);
		
		tasksMenu.add(new ActionBatchAddTasks(16, 16));
		tasksMenu.add(new ActionEditTasks(this.taskView, 16, 16));
		tasksMenu.add(new ActionDuplicateTasks(16, 16));
		tasksMenu.add(new ActionDelete(16, 16));
		
		tasksMenu.addSeparator();
		
		tasksMenu.add(new ActionCollapseAll());
		tasksMenu.add(new ActionExpandAll());
		
		tasksMenu.addSeparator();
		
		tasksMenu.add(new ActionAddTaskSearcher(16, 16));
		tasksMenu.add(new ActionEditTaskSearcher(this.taskSearcherView, 16, 16));
		tasksMenu.add(new ActionDeleteTaskSearcher(
				this.taskSearcherView,
				16,
				16));
	}
	
	private void initializeSynchronizeMenu() {
		JMenu synchronizeMenu = new JMenu(
				Translations.getString("menu.synchronize"));
		this.add(synchronizeMenu);
		
		synchronizeMenu.add(new ActionSynchronize(false, 16, 16));
		synchronizeMenu.add(new ActionScheduledSync(16, 16));
	}
	
	private void initializeTemplateMenu(JMenu tasksMenu) {
		final JMenu templatesMenu = new JMenu(
				Translations.getString("action.add_template_task"));
		
		templatesMenu.setToolTipText(Translations.getString("action.add_template_task"));
		
		templatesMenu.setIcon(Images.getResourceImage("duplicate.png", 16, 16));
		tasksMenu.add(templatesMenu);
		
		TemplateUtils.updateTemplateList(templatesMenu, null);
		
		TaskTemplateFactory.getInstance().addListChangeListener(
				new ListChangeListener() {
					
					@Override
					public void listChange(ListChangeEvent event) {
						TemplateUtils.updateTemplateList(templatesMenu, null);
					}
					
				});
	}
	
	private void initializeHelpMenu() {
		JMenu helpMenu = new JMenu(Translations.getString("menu.help"));
		this.add(helpMenu);
		
		helpMenu.add(new ActionCheckVersion(false, 16, 16));
		helpMenu.add(new ActionCheckPluginVersion(false, 16, 16));
		helpMenu.addSeparator();
		helpMenu.add(new ActionHelp(16, 16));
		helpMenu.add(new ActionShowTips(16, 16));
		helpMenu.add(new ActionAbout(16, 16));
		helpMenu.addSeparator();
		helpMenu.add(new ActionLogBug());
		helpMenu.add(new ActionLogFeatureRequest());
		helpMenu.addSeparator();
		helpMenu.add(new ActionDonate(16, 16));
		helpMenu.add(new ActionReview(16, 16));
	}
	
}
