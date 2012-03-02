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
package com.leclercb.taskunifier.gui.components.notesearcheredit.sorter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.taskunifier.gui.api.searchers.sorters.NoteSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.NoteSorterElement;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class NoteSorterPanel extends JPanel {
	
	private NoteSorter sorter;
	private NoteSorterTable table;
	
	private JButton addButton;
	private JButton removeButton;
	
	public NoteSorterPanel(NoteSorter sorter) {
		this.sorter = sorter;
		
		this.initialize();
	}
	
	public NoteSorter getSorter() {
		return this.sorter;
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.table = new NoteSorterTable(this.sorter);
		this.table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent event) {
						if (event.getValueIsAdjusting())
							return;
						
						if (NoteSorterPanel.this.table.getSelectedRow() == -1) {
							NoteSorterPanel.this.removeButton.setEnabled(false);
						} else {
							NoteSorterPanel.this.removeButton.setEnabled(true);
						}
					}
					
				});
		
		panel.add(this.table.getTableHeader(), BorderLayout.NORTH);
		
		panel.add(this.table, BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.CENTER);
		
		this.initializeButtons();
	}
	
	private void initializeButtons() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("ADD")) {
					NoteSorterElement element = new NoteSorterElement(
							NoteColumn.TITLE,
							SortOrder.ASCENDING);
					
					NoteSorterPanel.this.sorter.addElement(element);
				} else {
					if (NoteSorterPanel.this.table.getCellEditor() != null)
						NoteSorterPanel.this.table.getCellEditor().stopCellEditing();
					
					int[] selectedRows = NoteSorterPanel.this.table.getSelectedRows();
					
					List<NoteSorterElement> elements = new ArrayList<NoteSorterElement>();
					for (int selectedRow : selectedRows) {
						NoteSorterElement element = NoteSorterPanel.this.table.getNoteSorterElement(selectedRow);
						if (element != null)
							elements.add(element);
					}
					
					for (NoteSorterElement element : elements) {
						NoteSorterPanel.this.sorter.removeElement(element);
					}
				}
			}
			
		};
		
		this.addButton = new JButton(ImageUtils.getResourceImage(
				"add.png",
				16,
				16));
		this.addButton.setActionCommand("ADD");
		this.addButton.addActionListener(listener);
		
		this.removeButton = new JButton(ImageUtils.getResourceImage(
				"remove.png",
				16,
				16));
		this.removeButton.setActionCommand("REMOVE");
		this.removeButton.addActionListener(listener);
		this.removeButton.setEnabled(false);
		
		JPanel buttonsPanel = new TUButtonsPanel(
				this.addButton,
				this.removeButton);
		
		this.add(buttonsPanel, BorderLayout.SOUTH);
	}
	
}
