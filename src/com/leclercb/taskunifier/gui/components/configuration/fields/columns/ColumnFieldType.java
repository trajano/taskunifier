package com.leclercb.taskunifier.gui.components.configuration.fields.columns;

import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;

public class ColumnFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	private TaskColumn taskColumn;
	
	public ColumnFieldType(TaskColumn taskColumn) {
		super(Main.SETTINGS, "taskcolumn."
				+ taskColumn.name().toLowerCase()
				+ ".visible");
		
		this.taskColumn = taskColumn;
	}
	
	@Override
	public Boolean getPropertyValue() {
		return this.taskColumn.isVisible();
	}
	
	@Override
	public void saveAndApplyConfig() {
		this.taskColumn.setVisible(this.getFieldValue());
	}
	
}
