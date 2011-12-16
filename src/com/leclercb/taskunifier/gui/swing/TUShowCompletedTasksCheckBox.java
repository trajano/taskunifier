package com.leclercb.taskunifier.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TUShowCompletedTasksCheckBox extends JCheckBox {
	
	public TUShowCompletedTasksCheckBox() {
		super(
				Translations.getString("configuration.general.show_completed_tasks"));
		this.initialize();
	}
	
	private void initialize() {
		this.setOpaque(false);
		this.setFont(this.getFont().deriveFont(10.0f));
		
		this.setSelected(Main.getSettings().getBooleanProperty(
				"tasksearcher.show_completed_tasks"));
		
		Main.getSettings().addPropertyChangeListener(
				"tasksearcher.show_completed_tasks",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						boolean selected = Main.getSettings().getBooleanProperty(
								"tasksearcher.show_completed_tasks");
						TUShowCompletedTasksCheckBox.this.setSelected(selected);
					}
					
				});
		
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.getSettings().setBooleanProperty(
						"tasksearcher.show_completed_tasks",
						TUShowCompletedTasksCheckBox.this.isSelected());
			}
			
		});
	}
	
}
