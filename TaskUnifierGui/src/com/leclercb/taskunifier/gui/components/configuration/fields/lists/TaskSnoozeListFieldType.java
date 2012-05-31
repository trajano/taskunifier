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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

import com.leclercb.taskunifier.gui.commons.comparators.TimeValueComparator;
import com.leclercb.taskunifier.gui.commons.values.StringValueTimeValue;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.timevalueedit.EditTimeValueDialog;
import com.leclercb.taskunifier.gui.swing.buttons.TUAddButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUEditButton;
import com.leclercb.taskunifier.gui.swing.buttons.TURemoveButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.TaskSnoozeList;
import com.leclercb.taskunifier.gui.utils.TaskSnoozeList.SnoozeItem;

public class TaskSnoozeListFieldType extends ConfigurationFieldType.Panel {
	
	private JPanel panel;
	
	private JXList list;
	
	private JButton addButton;
	private JButton editButton;
	private JButton removeButton;
	
	public TaskSnoozeListFieldType() {
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
				new JLabel(Translations.getString("general.tasksnoozelist")),
				BorderLayout.NORTH);
		
		EventList<SnoozeItem> eventList = new SortedList<SnoozeItem>(
				TaskSnoozeList.getInstance().getEventList(),
				TimeValueComparator.INSTANCE);
		
		this.list = new JXList();
		
		EventListModel<SnoozeItem> model = new EventListModel<SnoozeItem>(
				eventList);
		
		this.list.setModel(model);
		this.list.setSelectionModel(new EventSelectionModel<SnoozeItem>(
				eventList));
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setCellRenderer(new DefaultListRenderer(
				StringValueTimeValue.INSTANCE));
		
		panel.add(
				ComponentFactory.createJScrollPane(this.list, true),
				BorderLayout.CENTER);
		
		this.addButton = new TUAddButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				SnoozeItem item = new SnoozeItem();
				TaskSnoozeList.getInstance().add(item);
				EditTimeValueDialog.getInstance().setTimeValue(item);
				EditTimeValueDialog.getInstance().setVisible(true);
			}
			
		});
		
		this.editButton = new TUEditButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (TaskSnoozeListFieldType.this.list.getSelectedValue() == null)
					return;
				
				SnoozeItem item = (SnoozeItem) TaskSnoozeListFieldType.this.list.getSelectedValue();
				EditTimeValueDialog.getInstance().setTimeValue(item);
				EditTimeValueDialog.getInstance().setVisible(true);
				TaskSnoozeListFieldType.this.list.setSelectedValue(item, true);
			}
			
		});
		
		this.removeButton = new TURemoveButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				for (Object value : TaskSnoozeListFieldType.this.list.getSelectedValues()) {
					TaskSnoozeList.getInstance().remove((SnoozeItem) value);
					
					if (TaskSnoozeListFieldType.this.list.getModel().getSize() > 0)
						TaskSnoozeListFieldType.this.list.setSelectedIndex(TaskSnoozeListFieldType.this.list.getModel().getSize() - 1);
				}
			}
			
		});
		
		panel.add(new TUButtonsPanel(
				true,
				this.addButton,
				this.editButton,
				this.removeButton), BorderLayout.SOUTH);
		
		this.panel.add(panel, BorderLayout.CENTER);
	}
	
}
