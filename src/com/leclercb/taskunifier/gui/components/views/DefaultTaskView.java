package com.leclercb.taskunifier.gui.components.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXSearchField;

import com.explodingpixels.macwidgets.SourceListStandardColorScheme;
import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.components.modelnote.ModelNotePanel;
import com.leclercb.taskunifier.gui.components.modelnote.ModelNoteView;
import com.leclercb.taskunifier.gui.components.quickaddtask.QuickAddTaskPanel;
import com.leclercb.taskunifier.gui.components.tasks.TaskTableView;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherPanel;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.swing.TUIndentSubtasksCheckBox;
import com.leclercb.taskunifier.gui.swing.TUShowCompletedTasksCheckBox;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

class DefaultTaskView extends JPanel implements TaskView, SavePropertiesListener {
	
	private JSplitPane horizontalSplitPane;
	private JSplitPane verticalSplitPane;
	
	private JXSearchField searchField;
	private JCheckBox showCompletedTasksCheckBox;
	private JCheckBox indentSubtasksCheckBox;
	
	private TaskSearcherPanel taskSearcherPanel;
	private QuickAddTaskPanel quickAddTaskPanel;
	private TaskTable taskTable;
	private ModelNotePanel taskNote;
	
	public DefaultTaskView(MainView mainView) {
		this.initialize();
	}
	
	@Override
	public ViewType getViewType() {
		return ViewType.TASKS;
	}
	
	@Override
	public JPanel getViewContent() {
		return this;
	}
	
	@Override
	public TaskSearcherView getTaskSearcherView() {
		return this.taskSearcherPanel;
	}
	
	@Override
	public TaskTableView getTaskTableView() {
		return this.taskTable;
	}
	
	@Override
	public ModelNoteView getModelNoteView() {
		return this.taskNote;
	}
	
	private void initialize() {
		Main.SETTINGS.addSavePropertiesListener(this);
		
		this.setLayout(new BorderLayout());
		
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf()) {
			this.horizontalSplitPane = ComponentFactory.createThinJSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		} else {
			this.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
			
			this.horizontalSplitPane = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT);
		}
		
		this.horizontalSplitPane.setOneTouchExpandable(true);
		
		this.verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.verticalSplitPane.setOneTouchExpandable(true);
		this.verticalSplitPane.setBorder(BorderFactory.createEmptyBorder());
		
		JPanel searcherPane = new JPanel();
		searcherPane.setLayout(new BorderLayout());
		
		JPanel middlePane = new JPanel();
		middlePane.setLayout(new BorderLayout(5, 5));
		
		JPanel notePane = new JPanel();
		notePane.setLayout(new BorderLayout());
		
		this.horizontalSplitPane.setLeftComponent(searcherPane);
		this.horizontalSplitPane.setRightComponent(this.verticalSplitPane);
		
		this.verticalSplitPane.setTopComponent(middlePane);
		this.verticalSplitPane.setBottomComponent(notePane);
		
		this.add(this.horizontalSplitPane, BorderLayout.CENTER);
		
		this.loadSplitPaneSettings();
		
		this.initializeSearchField();
		this.showCompletedTasksCheckBox = new TUShowCompletedTasksCheckBox();
		this.indentSubtasksCheckBox = new TUIndentSubtasksCheckBox();
		this.initializeSearcherList(searcherPane);
		this.initializeQuickAddTask(middlePane);
		this.initializeTaskTable(middlePane);
		this.initializeModelNote(notePane);
		
		this.taskSearcherPanel.refreshTaskSearcher();
	}
	
	private void loadSplitPaneSettings() {
		int hSplit = Main.SETTINGS.getIntegerProperty("view.tasks.window.horizontal_split");
		int vSplit = Main.SETTINGS.getIntegerProperty("view.tasks.window.vertical_split");
		
		this.horizontalSplitPane.setDividerLocation(hSplit);
		this.verticalSplitPane.setDividerLocation(vSplit);
	}
	
	@Override
	public void saveProperties() {
		Main.SETTINGS.setIntegerProperty(
				"view.tasks.window.horizontal_split",
				this.horizontalSplitPane.getDividerLocation());
		Main.SETTINGS.setIntegerProperty(
				"view.tasks.window.vertical_split",
				this.verticalSplitPane.getDividerLocation());
	}
	
	private void initializeSearchField() {
		this.searchField = new JXSearchField(
				Translations.getString("general.search"));
		this.searchField.setColumns(15);
		
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTaskView.this.taskSearcherPanel.setSearchFilter(e.getActionCommand());
			}
			
		});
	}
	
	private void initializeSearcherList(JPanel searcherPane) {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(new SourceListStandardColorScheme().getActiveBackgroundColor());
		
		JPanel northPanel = new JPanel(new GridLayout(0, 1));
		northPanel.setBackground(new SourceListStandardColorScheme().getActiveBackgroundColor());
		
		panel.add(northPanel, BorderLayout.NORTH);
		
		northPanel.add(this.searchField);
		northPanel.add(this.showCompletedTasksCheckBox);
		northPanel.add(this.indentSubtasksCheckBox);
		
		searcherPane.add(panel, BorderLayout.CENTER);
		
		this.initializeTaskSearcherList(panel);
	}
	
	private void initializeTaskSearcherList(JPanel searcherPane) {
		this.taskSearcherPanel = new TaskSearcherPanel("tasksearcher.tasks");
		
		this.taskSearcherPanel.addPropertyChangeListener(
				TaskSearcherPanel.PROP_TITLE_FILTER,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						String filter = (String) evt.getNewValue();
						if (!DefaultTaskView.this.searchField.getText().equals(
								filter))
							DefaultTaskView.this.searchField.setText(filter);
					}
					
				});
		
		searcherPane.add(this.taskSearcherPanel);
	}
	
	private void initializeQuickAddTask(JPanel middlePane) {
		this.quickAddTaskPanel = new QuickAddTaskPanel();
		
		JPanel panel = new JPanel(new BorderLayout(3, 3));
		panel.add(this.quickAddTaskPanel, BorderLayout.CENTER);
		panel.add(Help.getHelpButton("task_quick_add"), BorderLayout.EAST);
		
		middlePane.add(panel, BorderLayout.NORTH);
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
						DefaultTaskView.this.taskTable.taskSearcherSelectionChange(new TaskSearcherSelectionChangeEvent(
								evt.getSource(),
								DefaultTaskView.this.taskSearcherPanel.getSelectedTaskSearcher()));
					}
					
				});
		
		middlePane.add(taskPanel, BorderLayout.CENTER);
	}
	
	private void initializeModelNote(JPanel notePane) {
		this.taskNote = new ModelNotePanel();
		this.taskTable.addModelSelectionChangeListener(this.taskNote);
		notePane.add(this.taskNote);
	}
	
}
