package com.leclercb.taskunifier.gui.components.tasks.list;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.models.ContextComboBoxModel;
import com.leclercb.taskunifier.gui.models.FolderComboBoxModel;
import com.leclercb.taskunifier.gui.models.GoalComboBoxModel;
import com.leclercb.taskunifier.gui.models.LocationComboBoxModel;
import com.leclercb.taskunifier.gui.renderers.TaskPriorityListCellRenderer;
import com.leclercb.taskunifier.gui.renderers.TaskRepeatFromListCellRenderer;
import com.leclercb.taskunifier.gui.renderers.TaskStatusListCellRenderer;
import com.leclercb.taskunifier.gui.swing.CurvedBorder;

public class TaskCell extends JPanel {

	private JCheckBox completed;
	private JCheckBox star;
	private JTextField title;

	private JComboBox context;
	private JComboBox folder;
	private JComboBox goal;
	private JComboBox location;

	private JComboBox status;
	private JComboBox priority;
	private JTextField repeat;
	private JComboBox repeatFrom;

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

		this.context = new JComboBox();
		this.context.setModel(new ContextComboBoxModel());

		this.folder = new JComboBox();
		this.folder.setModel(new FolderComboBoxModel());

		this.goal = new JComboBox();
		this.goal.setModel(new GoalComboBoxModel());

		this.location = new JComboBox();
		this.location.setModel(new LocationComboBoxModel());

		this.status = new JComboBox();
		this.status.setModel(new DefaultComboBoxModel(TaskStatus.values()));
		this.status.setRenderer(new TaskStatusListCellRenderer());

		this.priority = new JComboBox();
		this.priority.setModel(new DefaultComboBoxModel(TaskPriority.values()));
		this.priority.setRenderer(new TaskPriorityListCellRenderer());

		this.repeat = new JTextField();

		this.repeatFrom = new JComboBox();
		this.repeatFrom.setModel(new DefaultComboBoxModel(TaskRepeatFrom.values()));
		this.repeatFrom.setRenderer(new TaskRepeatFromListCellRenderer());

		GridBagConstraints c = null;

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2, 2, 2, 2);
		panel.add(this.completed, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(2, 2, 2, 2);
		panel.add(this.star, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 14;
		c.insets = new Insets(2, 2, 2, 2);
		panel.add(this.title, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		c.insets = new Insets(2, 2, 2, 2);
		panel.add(this.context, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 1;
		c.gridwidth = 4;
		c.insets = new Insets(2, 2, 2, 2);
		panel.add(this.folder, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 8;
		c.gridy = 1;
		c.gridwidth = 4;
		c.insets = new Insets(2, 2, 2, 2);
		panel.add(this.goal, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 12;
		c.gridy = 1;
		c.gridwidth = 4;
		c.insets = new Insets(2, 2, 2, 2);
		panel.add(this.location, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		c.insets = new Insets(2, 2, 2, 2);
		panel.add(this.status, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 2;
		c.gridwidth = 4;
		c.insets = new Insets(2, 2, 2, 2);
		panel.add(this.priority, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 8;
		c.gridy = 2;
		c.gridwidth = 4;
		c.insets = new Insets(2, 2, 2, 2);
		panel.add(this.repeat, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 12;
		c.gridy = 2;
		c.gridwidth = 4;
		c.insets = new Insets(2, 2, 2, 2);
		panel.add(this.repeatFrom, c);
	}

	public void setTask(Task task) {
		this.completed.setSelected(task.isCompleted());
		this.star.setSelected(task.isStar());
		this.title.setText(task.getTitle());

		this.context.setSelectedItem(task.getContext());
		this.folder.setSelectedItem(task.getFolder());
		this.goal.setSelectedItem(task.getGoal());
		this.location.setSelectedItem(task.getLocation());

		this.status.setSelectedItem(task.getStatus());
		this.priority.setSelectedItem(task.getPriority());
		this.repeat.setText(task.getRepeat());
		this.repeatFrom.setSelectedItem(task.getRepeatFrom());
	}

}
