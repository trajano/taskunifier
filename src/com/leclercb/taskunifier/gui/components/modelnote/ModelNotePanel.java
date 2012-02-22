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
package com.leclercb.taskunifier.gui.components.modelnote;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelNote;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.components.modelnote.editors.WysiwygHTMLEditorPane;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ModelNotePanel extends JPanel implements ModelNoteView, ModelSelectionListener {
	
	private HTMLEditorInterface htmlEditorPane;
	private ModelNote previousSelectedModel;
	
	public ModelNotePanel(String propertyName) {
		this.previousSelectedModel = null;
		this.initialize(propertyName);
	}
	
	@Override
	public boolean edit() {
		return this.htmlEditorPane.edit();
	}
	
	@Override
	public void view() {
		this.htmlEditorPane.view();
	}
	
	private void initialize(String propertyName) {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		
		this.htmlEditorPane = new WysiwygHTMLEditorPane(
				Translations.getString("error.select_one_row"),
				false,
				propertyName);
		
		this.htmlEditorPane.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				if (ModelNotePanel.this.previousSelectedModel != null)
					ModelNotePanel.this.previousSelectedModel.setNote(htmlEditorPane.getText());
			}
		});
		
		this.add(this.htmlEditorPane.getComponent(), BorderLayout.CENTER);
	}
	
	@Override
	public void modelSelectionChange(ModelSelectionChangeEvent event) {
		Model[] models = event.getSelectedModels();
		
		if (models.length == 1 && models[0] instanceof ModelNote) {
			if (EqualsUtils.equals(models[0], this.previousSelectedModel))
				return;
		}
		
		if (models.length != 1 || !(models[0] instanceof ModelNote)) {
			this.previousSelectedModel = null;
			this.htmlEditorPane.setText(
					Translations.getString("error.select_one_row"),
					false,
					true);
		} else {
			this.previousSelectedModel = (ModelNote) models[0];
			this.htmlEditorPane.setText(
					this.previousSelectedModel.getNote(),
					true,
					true);
		}
	}
	
}
