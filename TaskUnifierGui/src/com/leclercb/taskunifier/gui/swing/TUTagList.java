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
package com.leclercb.taskunifier.gui.swing;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.api.models.TagList;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class TUTagList extends JPanel {
	
	private JTextField text;
	private JButton button;
	private JPopupMenu popup;
	private TUCheckBoxList list;
	private TUTagListModel model;
	
	public TUTagList() {
		this.initialize();
	}
	
	public TagList getTags() {
		return TagList.fromString(this.text.getText());
	}
	
	public void setTags(String tags) {
		this.text.setText(tags);
	}
	
	public void setTags(TagList tags) {
		if (tags == null)
			this.text.setText("");
		else
			this.text.setText(tags.toString());
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.text.setEnabled(enabled);
		this.button.setEnabled(enabled);
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.popup = new JPopupMenu();
		
		this.text = new JTextField();
		
		this.button = new JButton(ImageUtils.getResourceImage(
				"edit.png",
				16,
				16));
		this.button.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				String text = TUTagList.this.text.getText();
				String[] tags = text.split(",");
				for (int i = 0; i < tags.length; i++) {
					tags[i] = tags[i].trim();
				}
				
				TUTagList.this.model.updateCheckBoxStates(tags);
				
				TUTagList.this.popup.show(
						TUTagList.this.button,
						e.getX(),
						e.getY());
			}
			
		});
		
		JPanel popupPanel = new JPanel(new BorderLayout());
		popupPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.list = new TUCheckBoxList();
		this.model = new TUTagListModel();
		this.list.setModel(this.model);
		
		this.model.addListChangeListener(new ListChangeListener() {
			
			@Override
			public void listChange(ListChangeEvent event) {
				Tag tag = (Tag) event.getValue();
				TagList tags = TagList.fromString(TUTagList.this.text.getText());
				
				if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
					tags.addTag(tag);
				} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
					tags.removeTag(tag);
				}
				
				TUTagList.this.text.setText(tags.toString());
			}
			
		});
		
		popupPanel.add(ComponentFactory.createJScrollPane(this.list, false));
		
		this.popup.add(popupPanel);
		
		this.add(this.text, BorderLayout.CENTER);
		this.add(this.button, BorderLayout.EAST);
	}
	
}
