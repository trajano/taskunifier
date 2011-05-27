package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.commons.models.TaskRepeatFromModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeatFrom;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class RepeatFromEditor extends ComboBoxCellEditor {
	
	public RepeatFromEditor() {
		super(new JComboBox());
		
		JComboBox comboBox = (JComboBox) this.getComponent();
		
		comboBox.setModel(new TaskRepeatFromModel(false));
		comboBox.setRenderer(new DefaultListRenderer(
				new StringValueTaskRepeatFrom()));
	}
	
}
