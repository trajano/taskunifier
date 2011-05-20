package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.commons.models.TaskPriorityModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskPriority;

public class PriorityEditor extends DefaultCellEditor {
	
	public PriorityEditor() {
		super(new JComboBox());
		
		JComboBox comboBox = (JComboBox) this.getComponent();
		
		comboBox.setModel(new TaskPriorityModel(false));
		comboBox.setRenderer(new DefaultListRenderer(
				new StringValueTaskPriority(),
				new IconValueTaskPriority()));
	}
	
}
