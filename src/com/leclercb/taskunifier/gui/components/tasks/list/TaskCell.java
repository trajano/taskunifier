package com.leclercb.taskunifier.gui.components.tasks.list;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.swing.CurvedBorder;

public class TaskCell extends JPanel {

	private JCheckBox completed;
	private JCheckBox star;
	private JTextField title;

	public TaskCell() {
		this.initialize();
	}

	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new CurvedBorder());

		this.add(panel, BorderLayout.CENTER);

		this.completed = new JCheckBox();

		this.star = new JCheckBox();
		this.star.setIcon(Images.getResourceImage("checkbox_star.gif"));
		this.star.setSelectedIcon(Images.getResourceImage("checkbox_star_selected.gif"));

		this.title = new JTextField();

		GridBagConstraints c = null;

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(this.completed, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 0;
		panel.add(this.star, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 4;
		panel.add(this.title, c);
	}

	public void setTask(Task task) {
		this.completed.setSelected(task.isCompleted());
		this.star.setSelected(task.isStar());
		this.title.setText(task.getTitle());
	}

}
