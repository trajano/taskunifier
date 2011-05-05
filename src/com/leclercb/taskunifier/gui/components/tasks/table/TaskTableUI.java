package com.leclercb.taskunifier.gui.components.tasks.table;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;

import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.ContextEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.DateEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.FolderEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.GoalEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.LengthEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.LocationEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.PriorityEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.ReminderEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.RepeatEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.RepeatFromEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.editors.StatusEditor;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.CalendarRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.CheckBoxRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.DefaultRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.LengthRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.ModelRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.ReminderRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.RepeatRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.StarRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskPriorityRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskRepeatFromRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskStatusRenderer;
import com.leclercb.taskunifier.gui.components.tasks.table.renderers.TaskTitleRenderer;

public class TaskTableUI extends JXTable {

	private static final TableCellRenderer CHECK_BOX_RENDERER;
	private static final TableCellRenderer DATE_RENDERER;
	private static final TableCellRenderer DEFAULT_RENDERER;
	private static final TableCellRenderer LENGTH_RENDERER;
	private static final TableCellRenderer MODEL_RENDERER;
	private static final TableCellRenderer REMINDER_RENDERER;
	private static final TableCellRenderer REPEAT_RENDERER;
	private static final TableCellRenderer STAR_RENDERER;
	private static final TableCellRenderer TASK_PRIORITY_RENDERER;
	private static final TableCellRenderer TASK_REPEAT_FROM_RENDERER;
	private static final TableCellRenderer TASK_STATUS_RENDERER;
	private static final TableCellRenderer TASK_TITLE_RENDERER;

	private static final TableCellEditor BOOLEAN_EDITOR;
	private static final TableCellEditor CONTEXT_EDITOR;
	private static final TableCellEditor DATE_EDITOR;
	private static final TableCellEditor FOLDER_EDITOR;
	private static final TableCellEditor GENERIC_EDITOR;
	private static final TableCellEditor GOAL_EDITOR;
	private static final TableCellEditor LENGTH_EDITOR;
	private static final TableCellEditor LOCATION_EDITOR;
	private static final TableCellEditor REMINDER_EDITOR;
	private static final TableCellEditor REPEAT_EDITOR;
	private static final TableCellEditor TASK_PRIORITY_EDITOR;
	private static final TableCellEditor TASK_REPEAT_FROM_EDITOR;
	private static final TableCellEditor TASK_STATUS_EDITOR;

	static {
		CHECK_BOX_RENDERER = new CheckBoxRenderer();
		DATE_RENDERER = new CalendarRenderer();
		DEFAULT_RENDERER = new DefaultRenderer();
		LENGTH_RENDERER = new LengthRenderer();
		MODEL_RENDERER = new ModelRenderer();
		REMINDER_RENDERER = new ReminderRenderer();
		REPEAT_RENDERER = new RepeatRenderer();
		STAR_RENDERER = new StarRenderer();
		TASK_PRIORITY_RENDERER = new TaskPriorityRenderer();
		TASK_REPEAT_FROM_RENDERER = new TaskRepeatFromRenderer();
		TASK_STATUS_RENDERER = new TaskStatusRenderer();
		TASK_TITLE_RENDERER = new TaskTitleRenderer();

		BOOLEAN_EDITOR = new JXTable.BooleanEditor();
		CONTEXT_EDITOR = new ContextEditor();
		DATE_EDITOR = new DateEditor();
		FOLDER_EDITOR = new FolderEditor();
		GENERIC_EDITOR = new JXTable.GenericEditor();
		GOAL_EDITOR = new GoalEditor();
		LENGTH_EDITOR = new LengthEditor();
		LOCATION_EDITOR = new LocationEditor();
		REMINDER_EDITOR = new ReminderEditor();
		REPEAT_EDITOR = new RepeatEditor();
		TASK_PRIORITY_EDITOR = new PriorityEditor();
		TASK_REPEAT_FROM_EDITOR = new RepeatFromEditor();
		TASK_STATUS_EDITOR = new StatusEditor();
	}

	@Override
	public TableCellEditor getCellEditor(int row, int col) {
		TaskColumn column = ((TaskTableColumnModel) this.getColumnModel()).getTaskColumn(col);

		switch (column) {
		case TITLE:
			return GENERIC_EDITOR;
		case TAGS:
			return GENERIC_EDITOR;
		case FOLDER:
			return FOLDER_EDITOR;
		case CONTEXT:
			return CONTEXT_EDITOR;
		case GOAL:
			return GOAL_EDITOR;
		case LOCATION:
			return LOCATION_EDITOR;
		case COMPLETED:
			return BOOLEAN_EDITOR;
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
			return BOOLEAN_EDITOR;
		default:
			return super.getCellEditor(row, col);
		}
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int col) {
		TaskColumn column = ((TaskTableColumnModel) this.getColumnModel()).getTaskColumn(col);

		switch (column) {
		case TITLE:
			return TASK_TITLE_RENDERER;
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
