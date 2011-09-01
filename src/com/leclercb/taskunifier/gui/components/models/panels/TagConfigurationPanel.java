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
package com.leclercb.taskunifier.gui.components.models.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.api.models.utils.TaskTagList;
import com.leclercb.taskunifier.gui.commons.models.TaskTagModel;
import com.leclercb.taskunifier.gui.components.models.lists.ITagList;
import com.leclercb.taskunifier.gui.components.models.lists.TagList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.FormBuilder;

public class TagConfigurationPanel extends JSplitPane implements ITagList {
	
	private TagList tagList;
	
	public TagConfigurationPanel() {
		this.initialize();
	}
	
	@Override
	public Tag getSelectedTag() {
		return this.tagList.getSelectedTag();
	}
	
	@Override
	public void setSelectedTag(Tag tag) {
		this.tagList.setSelectedTag(tag);
	}
	
	private void initialize() {
		this.setBorder(null);
		
		// Initialize Fields
		final JTextField tagTitle = new JTextField();
		final JButton tagSave = new JButton(
				Translations.getString("general.save"));
		
		// Set Disabled
		tagTitle.setEnabled(false);
		
		// Initialize Model List
		this.tagList = new TagList(new TaskTagModel(false)) {
			
			@Override
			public void removeTag(Tag tag) {
				TaskTagList.getInstance().removeTag(tag);
			}
			
			@Override
			public void tagSelected(Tag tag) {
				tagTitle.setText(tag != null ? tag.toString() : null);
				tagTitle.setEnabled(tag != null);
				tagSave.setEnabled(tag != null);
			}
			
		};
		
		this.setLeftComponent(this.tagList);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new BorderLayout());
		this.setRightComponent(ComponentFactory.createJScrollPane(
				rightPanel,
				false));
		
		FormBuilder builder = new FormBuilder(
				"right:pref, 4dlu, fill:default:grow");
		
		// Tag Title
		builder.appendI15d("general.task.tag", true, tagTitle);
		
		// Tag Save
		tagSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					Tag tag = new Tag(tagTitle.getText());
					
					TaskTagList.getInstance().editTag(
							TagConfigurationPanel.this.tagList.getSelectedTag(),
							tag);
					
					ViewType.getTaskView().getTaskSearcherView().selectTag(tag);
				} catch (Exception e) {
					
				}
			}
			
		});
		
		builder.append("", tagSave);
		
		// Lay out the panel
		rightPanel.add(builder.getPanel(), BorderLayout.CENTER);
		
		this.setDividerLocation(200);
	}
	
}
