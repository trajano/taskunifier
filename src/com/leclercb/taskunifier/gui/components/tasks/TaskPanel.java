package com.leclercb.taskunifier.gui.components.tasks;

import java.awt.CardLayout;
import java.awt.HeadlessException;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JTable.PrintMode;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.leclercb.commons.api.properties.SavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionListener;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class TaskPanel extends JPanel implements TaskView, SavePropertiesListener, TaskSearcherSelectionListener {
	
	public static enum View {
		
		TABLE;
		
	}
	
	private TaskSelectionChangeSupport taskSelectionChangeSupport;
	
	private View currentView;
	
	private TaskTable taskTable;
	
	public TaskPanel() {
		this.taskSelectionChangeSupport = new TaskSelectionChangeSupport(this);
		this.initialize();
	}
	
	private void initialize() {
		Main.SETTINGS.addSavePropertiesListener(this);
		
		this.setLayout(new CardLayout());
		
		this.taskTable = new TaskTable();
		this.taskTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent e) {
						TaskPanel.this.taskSelectionChangeSupport.fireTaskSelectionChange(TaskPanel.this.getSelectedTasks());
					}
					
				});
		
		this.add(
				ComponentFactory.createJScrollPane(this.taskTable, false),
				View.TABLE.name());
		
		this.setView(View.TABLE);
	}
	
	@Override
	public void saveSettings() {
		// TODO: add listeners in task table and put this in TaskColumn
		int i = 0;
		Enumeration<TableColumn> columns = this.taskTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();
			TaskColumn taskColumn = (TaskColumn) column.getIdentifier();
			
			Main.SETTINGS.setIntegerProperty("taskcolumn."
					+ taskColumn.name().toLowerCase()
					+ ".order", i);
			Main.SETTINGS.setIntegerProperty("taskcolumn."
					+ taskColumn.name().toLowerCase()
					+ ".width", column.getWidth());
			Main.SETTINGS.setBooleanProperty("taskcolumn."
					+ taskColumn.name().toLowerCase()
					+ ".visible", taskColumn.isVisible());
			
			i++;
		}
	}
	
	public View getView() {
		return this.currentView;
	}
	
	public void setView(View view) {
		CheckUtils.isNotNull(view, "View cannot be null");
		
		this.currentView = view;
		
		CardLayout layout = (CardLayout) (this.getLayout());
		layout.show(this, view.name());
	}
	
	@Override
	public Task[] getSelectedTasks() {
		return this.taskTable.getSelectedTasks();
	}
	
	@Override
	public void setSelectedTaskAndStartEdit(Task task) {
		this.taskTable.setSelectedTaskAndStartEdit(task);
	}
	
	@Override
	public void setSelectedTasks(Task[] tasks) {
		this.taskTable.setSelectedTasks(tasks);
	}
	
	@Override
	public void refreshTasks() {
		this.taskTable.refreshTasks();
	}
	
	@Override
	public void printTasks() throws HeadlessException, PrinterException {
		this.taskTable.print(
				PrintMode.FIT_WIDTH,
				new MessageFormat(Constants.TITLE
						+ " - "
						+ this.taskTable.getTaskSearcher().getTitle()),
				new MessageFormat(this.taskTable.getRowCount()
						+ " tasks | Page - {0}"),
				true,
				null,
				true);
	}
	
	@Override
	public void addTaskSelectionChangeListener(TaskSelectionListener listener) {
		this.taskSelectionChangeSupport.addTaskSelectionChangeListener(listener);
	}
	
	@Override
	public void removeTaskSelectionChangeListener(TaskSelectionListener listener) {
		this.taskSelectionChangeSupport.removeTaskSelectionChangeListener(listener);
	}
	
	@Override
	public void taskSearcherSelectionChange(
			TaskSearcherSelectionChangeEvent event) {
		if (event.getSelectedTaskSearcher() != null)
			this.taskTable.setTaskSearcher(event.getSelectedTaskSearcher());
	}
	
}
