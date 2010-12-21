package com.leclercb.taskunifier.gui.components.tasks;

import java.awt.CardLayout;
import java.awt.HeadlessException;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable.PrintMode;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.settings.SaveSettingsListener;
import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.tasks.list.TaskList;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public class TaskPanel extends JPanel implements TaskView, SaveSettingsListener {
	
	public static enum View {
		
		TABLE;
		// LIST;
		
	}
	
	private View currentView;
	
	private TaskTable taskTable;
	private TaskList taskList;
	
	public TaskPanel() {
		this.initialize();
	}
	
	private void initialize() {
		Settings.addSaveSettingsListener(this);
		this.loadTaskColumnSettings();
		
		this.setLayout(new CardLayout());
		
		this.taskTable = new TaskTable();
		this.taskList = new TaskList();
		
		this.add(new JScrollPane(this.taskTable), View.TABLE.name());
		// this.add(new JScrollPane(this.taskList), View.LIST.name());
		
		this.setView(View.TABLE);
	}
	
	private void loadTaskColumnSettings() {
		TaskColumn[] columns = TaskColumn.values();
		for (int i = 0; i < columns.length; i++) {
			Integer order = Settings.getIntegerProperty("taskcolumn."
					+ columns[i].name().toLowerCase()
					+ ".order");
			Integer width = Settings.getIntegerProperty("taskcolumn."
					+ columns[i].name().toLowerCase()
					+ ".width");
			Boolean visible = Settings.getBooleanProperty("taskcolumn."
					+ columns[i].name().toLowerCase()
					+ ".visible");
			
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
		TaskColumn[] taskColumns = TaskColumn.getValues(false);
		for (int i = 0; i < taskColumns.length; i++) {
			Settings.setBooleanProperty("taskcolumn."
					+ taskColumns[i].name().toLowerCase()
					+ ".visible", false);
		}
		
		int i = 0;
		Enumeration<TableColumn> columns = this.taskTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();
			TaskColumn taskColumn = (TaskColumn) column.getIdentifier();
			
			Settings.setIntegerProperty("taskcolumn."
					+ taskColumn.name().toLowerCase()
					+ ".order", i);
			Settings.setIntegerProperty("taskcolumn."
					+ taskColumn.name().toLowerCase()
					+ ".width", column.getWidth());
			Settings.setBooleanProperty("taskcolumn."
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
	
	public void addListSelectionListener(ListSelectionListener listener) {
		this.taskList.getSelectionModel().addListSelectionListener(listener);
		this.taskTable.getSelectionModel().addListSelectionListener(listener);
	}
	
	public void setTaskSearcher(TaskSearcher searcher) {
		this.taskTable.setTaskSearcher(searcher);
	}
	
	@Override
	public void setSelectedTask(Task task) {
		this.taskTable.setSelectedTask(task);
	}
	
	@Override
	public void showColumn(TaskColumn taskColumn, boolean show) {
		this.taskTable.showColumn(taskColumn, show);
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
	public int getTaskCount() {
		List<Task> tasks = TaskFactory.getInstance().getList();
		int count = tasks.size();
		
		for (Task task : tasks) {
			if (!task.getModelStatus().equals(ModelStatus.LOADED)
					&& !task.getModelStatus().equals(ModelStatus.TO_UPDATE))
				count--;
		}
		
		return count;
	}
	
	@Override
	public int getDisplayedTaskCount() {
		return this.taskTable.getRowCount();
	}
	
}
