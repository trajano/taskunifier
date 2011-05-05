package com.leclercb.taskunifier.gui.components.tasks.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jdesktop.swingx.table.TableColumnExt;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskTableColumn extends TableColumnExt {
	
	private TaskColumn taskColumn;
	
	public TaskTableColumn(TaskColumn taskColumn) {
		super(taskColumn.ordinal());
		
		CheckUtils.isNotNull(taskColumn, "Task column cannot be null");
		
		this.taskColumn = taskColumn;
		
		this.setIdentifier(taskColumn);
		this.setHeaderValue(taskColumn.getLabel());
		this.setPreferredWidth(taskColumn.getWidth());
		this.setVisible(taskColumn.isVisible());
		
		this.taskColumn.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(TaskColumn.PROP_VISIBLE)) {
					TaskTableColumn.this.setVisible((Boolean) evt.getNewValue());
				}

				if (evt.getPropertyName().equals(TaskColumn.PROP_WIDTH)) {
					TaskTableColumn.this.setPreferredWidth((Integer) evt.getNewValue());
				}
			}
		});
	}
	
	@Override
	public void setPreferredWidth(int preferredWidth) {
		this.taskColumn.setWidth(preferredWidth);
		super.setPreferredWidth(preferredWidth);
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.taskColumn.setVisible(visible);
		super.setVisible(visible);
	}

}
