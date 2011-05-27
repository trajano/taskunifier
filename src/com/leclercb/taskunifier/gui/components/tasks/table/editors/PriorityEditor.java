package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.commons.models.TaskPriorityModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskPriority;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class PriorityEditor extends ComboBoxCellEditor {
	
	public PriorityEditor() {
		super(new JComboBox());
		
		JComboBox comboBox = (JComboBox) this.getComponent();
		
		comboBox.setModel(new TaskPriorityModel(false));
		comboBox.setRenderer(new DefaultListRenderer(
				new StringValueTaskPriority(),
				new IconValueTaskPriority()));
	}
	
}
