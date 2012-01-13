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
package com.leclercb.taskunifier.gui.components.notesearcheredit.filter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.searchers.filters.FilterLink;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.ModelCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class NoteFilterPanel extends JPanel {
	
	private NoteFilter filter;
	private NoteFilterTree tree;
	
	private JButton autoFillButton;
	private JButton addElementButton;
	private JButton addFilterButton;
	private JButton removeButton;
	
	public NoteFilterPanel(NoteFilter filter) {
		this.filter = filter;
		
		this.initialize();
	}
	
	public NoteFilterTree getTree() {
		return this.tree;
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.tree = new NoteFilterTree(this.filter);
		this.tree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {
					
					@Override
					public void valueChanged(TreeSelectionEvent event) {
						if (NoteFilterPanel.this.tree.getSelectionCount() != 0) {
							TreeNode node = (TreeNode) NoteFilterPanel.this.tree.getLastSelectedPathComponent();
							
							if (node instanceof NoteFilterTreeNode) {
								if (((NoteFilterTreeNode) node).getFilter().getParent() != null) {
									NoteFilterPanel.this.removeButton.setEnabled(true);
								} else {
									NoteFilterPanel.this.removeButton.setEnabled(false);
								}
								
								NoteFilterPanel.this.addElementButton.setEnabled(true);
								NoteFilterPanel.this.addFilterButton.setEnabled(true);
								return;
							} else if (node instanceof NoteFilterElementTreeNode) {
								NoteFilterPanel.this.addElementButton.setEnabled(false);
								NoteFilterPanel.this.addFilterButton.setEnabled(false);
								NoteFilterPanel.this.removeButton.setEnabled(true);
								return;
							}
						}
						
						NoteFilterPanel.this.addElementButton.setEnabled(false);
						NoteFilterPanel.this.addFilterButton.setEnabled(false);
						NoteFilterPanel.this.removeButton.setEnabled(false);
					}
					
				});
		
		this.add(
				ComponentFactory.createJScrollPane(this.tree, true),
				BorderLayout.CENTER);
		
		this.initializeButtons();
	}
	
	private void initializeButtons() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("AUTO_FILL")) {
					NoteFilterPanel.this.filter.clearElement();
					NoteFilterPanel.this.filter.clearFilters();
					
					NoteFilterPanel.this.filter.setLink(FilterLink.OR);
					
					Note[] notes = ViewType.getNoteView().getNoteTableView().getSelectedNotes();
					for (Note note : notes) {
						NoteFilterPanel.this.filter.addElement(new NoteFilterElement(
								NoteColumn.MODEL,
								ModelCondition.EQUALS,
								note));
					}
				} else if (event.getActionCommand().startsWith("ADD")) {
					TreeNode node = (TreeNode) NoteFilterPanel.this.tree.getLastSelectedPathComponent();
					
					if (node == null || !(node instanceof NoteFilterTreeNode))
						return;
					
					if (event.getActionCommand().equals("ADD_ELEMENT")) {
						NoteFilterElement element = new NoteFilterElement(
								NoteColumn.TITLE,
								StringCondition.EQUALS,
								"");
						
						((NoteFilterTreeNode) node).getFilter().addElement(
								element);
					} else if (event.getActionCommand().equals("ADD_FILTER")) {
						((NoteFilterTreeNode) node).getFilter().addFilter(
								new NoteFilter());
					}
					
					for (int i = 0; i < NoteFilterPanel.this.tree.getRowCount(); i++)
						NoteFilterPanel.this.tree.expandRow(i);
				} else if (event.getActionCommand().equals("REMOVE")) {
					TreeNode node = (TreeNode) NoteFilterPanel.this.tree.getLastSelectedPathComponent();
					
					if (node == null)
						return;
					
					if (node instanceof NoteFilterTreeNode) {
						((NoteFilterTreeNode) node).getFilter().getParent().removeFilter(
								((NoteFilterTreeNode) node).getFilter());
					} else if (node instanceof NoteFilterElementTreeNode) {
						((NoteFilterElementTreeNode) node).getElement().getParent().removeElement(
								((NoteFilterElementTreeNode) node).getElement());
					}
				}
			}
			
		};
		
		this.addElementButton = new JButton(
				Translations.getString("searcheredit.add_element"),
				ImageUtils.getResourceImage("add.png", 16, 16));
		this.addElementButton.setActionCommand("ADD_ELEMENT");
		this.addElementButton.addActionListener(listener);
		this.addElementButton.setEnabled(false);
		
		this.addFilterButton = new JButton(
				Translations.getString("searcheredit.add_filter"),
				ImageUtils.getResourceImage("add.png", 16, 16));
		this.addFilterButton.setActionCommand("ADD_FILTER");
		this.addFilterButton.addActionListener(listener);
		this.addFilterButton.setEnabled(false);
		
		this.removeButton = new JButton(ImageUtils.getResourceImage(
				"remove.png",
				16,
				16));
		this.removeButton.setActionCommand("REMOVE");
		this.removeButton.addActionListener(listener);
		this.removeButton.setEnabled(false);
		
		this.autoFillButton = new JButton(
				Translations.getString("searcheredit.clear_and_auto_fill_with_selected_notes"),
				ImageUtils.getResourceImage("synchronize.png", 16, 16));
		this.autoFillButton.setActionCommand("AUTO_FILL");
		this.autoFillButton.addActionListener(listener);
		this.autoFillButton.setEnabled(true);
		
		// Do not show the auto fill button
		this.autoFillButton.setVisible(false);
		
		JPanel panel = new TUButtonsPanel(
				this.addElementButton,
				this.addFilterButton,
				this.removeButton,
				this.autoFillButton);
		
		this.add(panel, BorderLayout.SOUTH);
	}
	
}
