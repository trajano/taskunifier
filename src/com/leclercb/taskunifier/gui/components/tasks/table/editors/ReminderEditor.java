package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.commons.models.TaskReminderModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskReminder;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ReminderEditor extends ComboBoxCellEditor {
	
	public ReminderEditor() {
		super(new JComboBox());
		
		JComboBox comboBox = (JComboBox) this.getComponent();
		
		comboBox.setModel(new TaskReminderModel());
		comboBox.setRenderer(new DefaultListRenderer(
				new StringValueTaskReminder()));
		comboBox.setEditable(true);
	}
	
}
