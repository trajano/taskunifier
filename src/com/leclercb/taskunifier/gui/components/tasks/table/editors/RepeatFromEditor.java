package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import com.leclercb.taskunifier.gui.commons.models.TaskRepeatFromModel;
import com.leclercb.taskunifier.gui.commons.renderers.TaskRepeatFromListCellRenderer;

public class RepeatFromEditor extends DefaultCellEditor {

	public RepeatFromEditor() {
		super(new JComboBox());

		JComboBox comboBox = (JComboBox) this.getComponent();

		comboBox.setModel(new TaskRepeatFromModel());
		comboBox.setRenderer(new TaskRepeatFromListCellRenderer());
	}

}
