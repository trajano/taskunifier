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
package com.leclercb.taskunifier.gui.components.models.lists;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;

import com.leclercb.commons.api.utils.IgnoreCaseString;
import com.leclercb.taskunifier.gui.commons.comparators.TaskTagComparator;
import com.leclercb.taskunifier.gui.commons.highlighters.AlternateHighlighter;
import com.leclercb.taskunifier.gui.commons.models.TaskTagModel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public abstract class TagList extends JPanel implements ITagList {
	
	private JXSearchField searchField;
	
	private JXList tagList;
	private TagRowFilter rowFilter;
	
	private JButton removeButton;
	
	public TagList(TaskTagModel model) {
		this.initialize(model);
	}
	
	private void initialize(TaskTagModel model) {
		this.setLayout(new BorderLayout(0, 3));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.tagList = new JXList();
		this.tagList.setModel(model);
		this.tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.tagList.setAutoCreateRowSorter(true);
		this.tagList.setComparator(new TaskTagComparator());
		this.tagList.setSortOrder(SortOrder.ASCENDING);
		this.tagList.setSortsOnUpdates(true);
		
		this.rowFilter = new TagRowFilter();
		this.tagList.setRowFilter(this.rowFilter);
		
		this.tagList.setHighlighters(new AlternateHighlighter());
		
		this.tagList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting())
					return;
				
				if (TagList.this.tagList.getSelectedValue() == null) {
					TagList.this.tagSelected(null);
					TagList.this.removeButton.setEnabled(false);
				} else {
					TagList.this.tagSelected(TagList.this.getSelectedTag());
					TagList.this.removeButton.setEnabled(true);
				}
			}
			
		});
		
		this.add(
				ComponentFactory.createJScrollPane(this.tagList, true),
				BorderLayout.CENTER);
		
		this.searchField = new JXSearchField(
				Translations.getString("general.search"));
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TagList.this.rowFilter.setTitle(e.getActionCommand());
			}
			
		});
		
		this.rowFilter.addPropertyChangeListener(
				ModelRowFilter.PROP_TITLE,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						TagList.this.searchField.setText((String) evt.getNewValue());
						TagList.this.tagList.setRowFilter((TagRowFilter) evt.getSource());
					}
					
				});
		
		this.add(this.searchField, BorderLayout.NORTH);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("REMOVE")) {
					IgnoreCaseString tag = TagList.this.getSelectedTag();
					TagList.this.removeTag(tag);
				}
			}
			
		};
		
		this.removeButton = ComponentFactory.createButtonRemove(listener);
		this.removeButton.setEnabled(false);
		
		JPanel panel = ComponentFactory.createButtonsPanel(this.removeButton);
		
		this.add(panel, BorderLayout.SOUTH);
	}
	
	@Override
	public IgnoreCaseString getSelectedTag() {
		return (IgnoreCaseString) this.tagList.getSelectedValue();
	}
	
	@Override
	public void setSelectedTag(IgnoreCaseString tag) {
		this.tagList.setSelectedValue(tag, true);
	}
	
	public abstract void removeTag(IgnoreCaseString tag);
	
	public abstract void tagSelected(IgnoreCaseString tag);
	
}
