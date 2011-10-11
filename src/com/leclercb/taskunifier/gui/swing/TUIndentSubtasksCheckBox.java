package com.leclercb.taskunifier.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TUIndentSubtasksCheckBox extends JCheckBox {
	
	public TUIndentSubtasksCheckBox() {
		super(Translations.getString("configuration.general.indent_subtasks"));
		this.initialize();
	}
	
	private void initialize() {
		this.setOpaque(false);
		this.setFont(this.getFont().deriveFont(10.0f));
		
		this.setSelected(Main.SETTINGS.getBooleanProperty("task.indent_subtasks"));
		
		Main.SETTINGS.addPropertyChangeListener(
				"task.indent_subtasks",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						boolean selected = Main.SETTINGS.getBooleanProperty("task.indent_subtasks");
						TUIndentSubtasksCheckBox.this.setSelected(selected);
					}
					
				});
		
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.SETTINGS.setBooleanProperty(
						"task.indent_subtasks",
						TUIndentSubtasksCheckBox.this.isSelected());
			}
			
		});
	}
	
}
