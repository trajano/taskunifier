package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import com.leclercb.taskunifier.gui.commons.models.TaskReminderModel;
import com.leclercb.taskunifier.gui.commons.renderers.TaskReminderListCellRenderer;

public class ReminderEditor extends DefaultCellEditor {
	
	public ReminderEditor() {
		super(new JComboBox());
		
		JComboBox comboBox = (JComboBox) this.getComponent();
		
		comboBox.setModel(new TaskReminderModel());
		comboBox.setRenderer(new TaskReminderListCellRenderer());
		comboBox.setEditable(true);
	}
	
}
