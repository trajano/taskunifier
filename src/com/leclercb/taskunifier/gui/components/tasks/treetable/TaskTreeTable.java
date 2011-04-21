package com.leclercb.taskunifier.gui.components.tasks.treetable;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.commons.renderers.TaskPriorityListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskReminderListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskRepeatFromListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskStatusListCellRenderer;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTableColumnModel;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.DateEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.LengthEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.RepeatEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.TagsEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.TitleEditor;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.CalendarRenderer;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.CheckBoxRenderer;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.DefaultRenderer;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.LengthRenderer;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.ModelRenderer;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.ReminderRenderer;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.RepeatRenderer;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.StarRenderer;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.TaskPriorityRenderer;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.TaskRepeatFromRenderer;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.TaskStatusRenderer;
import com.leclercb.taskunifier.gui.components.tasks.treetable.renderers.TaskTitleRenderer;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class TaskTreeTable extends JXTreeTable {
	
	private static final DefaultRenderer DEFAULT_RENDERER;
	private static final CheckBoxRenderer CHECK_BOX_RENDERER;
	private static final ModelRenderer MODEL_RENDERER;
	private static final CalendarRenderer DATE_RENDERER;
	private static final RepeatRenderer REPEAT_RENDERER;
	private static final LengthRenderer LENGTH_RENDERER;
	private static final TaskTitleRenderer TASK_TITLE_RENDERER;
	private static final StarRenderer STAR_RENDERER;
	private static final ReminderRenderer REMINDER_RENDERER;
	
	private static final TaskPriorityRenderer TASK_PRIORITY_RENDERER;
	private static final TaskRepeatFromRenderer TASK_REPEAT_FROM_RENDERER;
	private static final TaskStatusRenderer TASK_STATUS_RENDERER;
	
	private static final TableCellEditor TITLE_EDITOR;
	private static final TableCellEditor TAGS_EDITOR;
	
	private static final TableCellEditor CHECK_BOX_EDITOR;
	private static final TableCellEditor DATE_EDITOR;
	private static final TableCellEditor LENGTH_EDITOR;
	private static final TableCellEditor STAR_EDITOR;
	
	private static final TableCellEditor CONTEXT_EDITOR;
	private static final TableCellEditor FOLDER_EDITOR;
	private static final TableCellEditor GOAL_EDITOR;
	private static final TableCellEditor LOCATION_EDITOR;
	
	private static final TableCellEditor REPEAT_EDITOR;
	
	private static final TableCellEditor REMINDER_EDITOR;
	private static final TableCellEditor TASK_PRIORITY_EDITOR;
	private static final TableCellEditor TASK_REPEAT_FROM_EDITOR;
	private static final TableCellEditor TASK_STATUS_EDITOR;
	
	static {
		// RENDERERS
		DEFAULT_RENDERER = new DefaultRenderer();
		CHECK_BOX_RENDERER = new CheckBoxRenderer();
		MODEL_RENDERER = new ModelRenderer();
		DATE_RENDERER = new CalendarRenderer(new SimpleDateFormat(
				Main.SETTINGS.getStringProperty("date.date_format")
						+ " "
						+ Main.SETTINGS.getStringProperty("date.time_format")));
		REPEAT_RENDERER = new RepeatRenderer();
		LENGTH_RENDERER = new LengthRenderer(
				Main.SETTINGS.getSimpleDateFormatProperty("date.time_format"));
		TASK_TITLE_RENDERER = new TaskTitleRenderer();
		STAR_RENDERER = new StarRenderer();
		REMINDER_RENDERER = new ReminderRenderer();
		
		TASK_PRIORITY_RENDERER = new TaskPriorityRenderer();
		TASK_REPEAT_FROM_RENDERER = new TaskRepeatFromRenderer();
		TASK_STATUS_RENDERER = new TaskStatusRenderer();
		
		// EDITORS
		TITLE_EDITOR = new TitleEditor();
		TAGS_EDITOR = new TagsEditor();
		
		JCheckBox checkBox = null;
		
		checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(SwingConstants.CENTER);
		
		CHECK_BOX_EDITOR = new DefaultCellEditor(checkBox);
		LENGTH_EDITOR = new LengthEditor();
		DATE_EDITOR = new DateEditor();
		
		checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(SwingConstants.CENTER);
		checkBox.setIcon(new ImageIcon("images/blank_star.gif"));
		checkBox.setSelectedIcon(new ImageIcon("images/star.gif"));
		
		JComboBox comboBox = null;
		
		STAR_EDITOR = new DefaultCellEditor(checkBox);
		
		CONTEXT_EDITOR = new ComboBoxCellEditor(
				ComponentFactory.createModelComboBox(new ContextModel(true)));
		
		FOLDER_EDITOR = new ComboBoxCellEditor(
				ComponentFactory.createModelComboBox(new FolderModel(true)));
		
		GOAL_EDITOR = new ComboBoxCellEditor(
				ComponentFactory.createModelComboBox(new GoalModel(true)));
		
		LOCATION_EDITOR = new ComboBoxCellEditor(
				ComponentFactory.createModelComboBox(new LocationModel(true)));
		
		REPEAT_EDITOR = new RepeatEditor();
		
		comboBox = new JComboBox(new Integer[] {
				0,
				5,
				15,
				30,
				60,
				60 * 24,
				60 * 24 * 7 });
		comboBox.setRenderer(new TaskReminderListCellRenderer());
		comboBox.setEditable(true);
		
		REMINDER_EDITOR = new DefaultCellEditor(comboBox);
		
		comboBox = new JComboBox(TaskPriority.values());
		comboBox.setRenderer(new TaskPriorityListCellRenderer());
		
		TASK_PRIORITY_EDITOR = new DefaultCellEditor(comboBox);
		
		comboBox = new JComboBox(TaskRepeatFrom.values());
		comboBox.setRenderer(new TaskRepeatFromListCellRenderer());
		
		TASK_REPEAT_FROM_EDITOR = new DefaultCellEditor(comboBox);
		
		comboBox = new JComboBox(TaskStatus.values());
		comboBox.setRenderer(new TaskStatusListCellRenderer());
		
		TASK_STATUS_EDITOR = new DefaultCellEditor(comboBox);
	}
	
	public TaskTreeTable() {
		this.initialize();
	}
	
	private void initialize() {
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		TaskTableColumnModel columnModel = new TaskTableColumnModel();
		TaskTreeTableModel treeTableModel = new TaskTreeTableModel(
				this.createRoot());
		
		this.setTreeTableModel(treeTableModel);
		this.setColumnModel(columnModel);
		this.setRootVisible(false);
	}
	
	private TreeTableNode createRoot() {
		DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
		
		List<Task> tasks = TaskFactory.getInstance().getList();
		for (Task task : tasks) {
			if (task.getParent() == null)
				root.add(new TaskTreeTableNode(root, task));
		}
		
		return root;
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int col) {
		TaskColumn column = ((TaskTableColumnModel) this.getColumnModel()).getTaskColumn(col);
		
		switch (column) {
			case TITLE:
				return TITLE_EDITOR;
			case TAGS:
				return TAGS_EDITOR;
			case FOLDER:
				return FOLDER_EDITOR;
			case CONTEXT:
				return CONTEXT_EDITOR;
			case GOAL:
				return GOAL_EDITOR;
			case LOCATION:
				return LOCATION_EDITOR;
			case COMPLETED:
				return CHECK_BOX_EDITOR;
			case DUE_DATE:
				return DATE_EDITOR;
			case START_DATE:
				return DATE_EDITOR;
			case REPEAT:
				return REPEAT_EDITOR;
			case REMINDER:
				return REMINDER_EDITOR;
			case REPEAT_FROM:
				return TASK_REPEAT_FROM_EDITOR;
			case STATUS:
				return TASK_STATUS_EDITOR;
			case LENGTH:
				return LENGTH_EDITOR;
			case PRIORITY:
				return TASK_PRIORITY_EDITOR;
			case STAR:
				return STAR_EDITOR;
			default:
				return super.getCellEditor(row, col);
		}
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int col) {
		TaskColumn column = ((TaskTableColumnModel) this.getColumnModel()).getTaskColumn(col);
		
		switch (column) {
			case TITLE:
				return super.getCellRenderer(row, col);
			case COMPLETED:
				return CHECK_BOX_RENDERER;
			case CONTEXT:
			case FOLDER:
			case GOAL:
			case LOCATION:
				return MODEL_RENDERER;
			case COMPLETED_ON:
			case DUE_DATE:
			case START_DATE:
				return DATE_RENDERER;
			case REMINDER:
				return REMINDER_RENDERER;
			case LENGTH:
				return LENGTH_RENDERER;
			case STAR:
				return STAR_RENDERER;
			case PRIORITY:
				return TASK_PRIORITY_RENDERER;
			case REPEAT:
				return REPEAT_RENDERER;
			case REPEAT_FROM:
				return TASK_REPEAT_FROM_RENDERER;
			case STATUS:
				return TASK_STATUS_RENDERER;
			default:
				return DEFAULT_RENDERER;
		}
	}
	
}
