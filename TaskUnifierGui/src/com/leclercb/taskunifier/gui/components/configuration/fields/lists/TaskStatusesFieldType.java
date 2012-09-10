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
package com.leclercb.taskunifier.gui.components.configuration.fields.lists;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskStatus;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.buttons.TUAddButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TURemoveButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;
import com.leclercb.taskunifier.gui.utils.TaskStatusList;

public class TaskStatusesFieldType extends ConfigurationFieldType.Panel implements PropertyChangeListener {
	
	private JPanel panel;
	
	private JXList list;
	
	private JXLabel label;
	
	private JButton addButton;
	private JButton removeButton;
	
	public TaskStatusesFieldType() {
		this.initialize();
		this.setPanel(this.panel);
	}
	
	private void initialize() {
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout(10, 10));
		
		this.initializeList();
	}
	
	private void initializeList() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		
		panel.add(
				new JLabel(Translations.getString("general.task.status")),
				BorderLayout.NORTH);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		panel.add(mainPanel, BorderLayout.CENTER);
		
		EventList<String> eventList = new SortedList<String>(
				TaskStatusList.getInstance().getEventList());
		
		this.list = new JXList();
		
		EventListModel<String> model = new EventListModel<String>(eventList);
		
		this.list.setModel(model);
		this.list.setSelectionModel(new EventSelectionModel<String>(eventList));
		this.list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.list.setCellRenderer(new DefaultListRenderer(
				StringValueTaskStatus.INSTANCE));
		
		mainPanel.add(
				ComponentFactory.createJScrollPane(this.list, true),
				BorderLayout.CENTER);
		
		this.label = new JXLabel();
		this.label.setLineWrap(true);
		this.label.setForeground(Color.RED);
		
		mainPanel.add(this.label, BorderLayout.SOUTH);
		
		this.addButton = new TUAddButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				String status = JOptionPane.showInputDialog(
						TaskStatusesFieldType.this.panel,
						Translations.getString("configuration.taskstatuses.enter_status"),
						Translations.getString("general.question"),
						JOptionPane.QUESTION_MESSAGE);
				
				if (status == null)
					return;
				
				TaskStatusList.getInstance().addStatus(status);
				TaskStatusesFieldType.this.list.setSelectedValue(status, true);
			}
			
		});
		
		this.removeButton = new TURemoveButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				for (Object value : TaskStatusesFieldType.this.list.getSelectedValues()) {
					TaskStatusList.getInstance().removeStatus(value.toString());
					
					if (TaskStatusesFieldType.this.list.getModel().getSize() > 0)
						TaskStatusesFieldType.this.list.setSelectedIndex(TaskStatusesFieldType.this.list.getModel().getSize() - 1);
				}
			}
			
		});
		
		panel.add(new TUButtonsPanel(
				true,
				false,
				this.addButton,
				this.removeButton), BorderLayout.SOUTH);
		
		this.panel.add(panel, BorderLayout.WEST);
		
		this.refreshButtons();
		Main.getUserSettings().addPropertyChangeListener(
				"plugin.synchronizer.id",
				new WeakPropertyChangeListener(Main.getUserSettings(), this));
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.refreshButtons();
	}
	
	public void refreshButtons() {
		if (SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().getStatusValues() == null) {
			this.label.setText(null);
		} else {
			this.label.setText(Translations.getString(
					"configuration.list.task_statuses.cannot_modify",
					SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().getApiName()));
		}
		
		boolean enabled = TaskStatusList.getInstance().isEditable();
		this.addButton.setEnabled(enabled);
		this.removeButton.setEnabled(enabled);
	}
	
}
