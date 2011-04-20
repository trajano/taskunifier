/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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

import com.leclercb.commons.gui.swing.borders.CurvedBorder;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.GoalModel;
import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.commons.renderers.TaskPriorityListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskRepeatFromListCellRenderer;
import com.leclercb.taskunifier.gui.commons.renderers.TaskStatusListCellRenderer;
import com.leclercb.taskunifier.gui.utils.Images;

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
		this.star.setIcon(Images.getResourceImage("checkbox_star.png", 18, 18));
		this.star.setSelectedIcon(Images.getResourceImage(
				"checkbox_star_selected.png",
				18,
				18));
		
		this.title = new JTextField();
		
		this.context = new JComboBox();
		this.context.setModel(new ContextModel(true));
		
		this.folder = new JComboBox();
		this.folder.setModel(new FolderModel(true));
		
		this.goal = new JComboBox();
		this.goal.setModel(new GoalModel(true));
		
		this.location = new JComboBox();
		this.location.setModel(new LocationModel(true));
		
		this.status = new JComboBox();
		this.status.setModel(new DefaultComboBoxModel(TaskStatus.values()));
		this.status.setRenderer(new TaskStatusListCellRenderer());
		
		this.priority = new JComboBox();
		this.priority.setModel(new DefaultComboBoxModel(TaskPriority.values()));
		this.priority.setRenderer(new TaskPriorityListCellRenderer());
		
		this.repeat = new JTextField();
		
		this.repeatFrom = new JComboBox();
		this.repeatFrom.setModel(new DefaultComboBoxModel(
				TaskRepeatFrom.values()));
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
