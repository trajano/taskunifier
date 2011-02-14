package com.leclercb.taskunifier.gui.components.tasknote;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.events.TaskSelectionChangeEvent;
import com.leclercb.taskunifier.gui.events.TaskSelectionListener;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TaskNotePanel extends JTextArea implements TaskSelectionListener {
	
	private Task previousSelectedTask;
	
	public TaskNotePanel() {
		this.previousSelectedTask = null;
		this.initialize();
	}
	
	public String getTaskNote() {
		return this.getText();
	}
	
	private void initialize() {
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setText(Translations.getString("error.select_one_task"));
		this.setEnabled(false);
		this.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if (previousSelectedTask != null) {
					if (!EqualsUtils.equals(
							previousSelectedTask.getNote(),
							getText()))
						previousSelectedTask.setNote(getText());
				}
			}
			
		});
	}
	
	@Override
	public void taskSelectionChange(TaskSelectionChangeEvent event) {
		if (this.previousSelectedTask != null) {
			if (!EqualsUtils.equals(
					this.previousSelectedTask.getNote(),
					this.getText()))
				this.previousSelectedTask.setNote(this.getText());
		}
		
		Task[] tasks = event.getSelectedTasks();
		
		if (tasks.length != 1) {
			this.previousSelectedTask = null;
			
			this.setText(Translations.getString("error.select_one_task"));
			this.setEnabled(false);
		} else {
			this.previousSelectedTask = tasks[0];
			
			this.setText((tasks[0].getNote() == null ? "" : tasks[0].getNote()));
			this.setEnabled(true);
		}
	}
	
}
