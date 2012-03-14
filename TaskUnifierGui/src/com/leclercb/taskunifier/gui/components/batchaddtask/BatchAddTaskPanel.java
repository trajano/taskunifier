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
package com.leclercb.taskunifier.gui.components.batchaddtask;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.event.action.ActionSupported;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.gui.actions.ActionBatchAddTasks;
import com.leclercb.taskunifier.gui.actions.ActionManageTaskTemplates;
import com.leclercb.taskunifier.gui.commons.models.TaskTemplateModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskTemplateTitle;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUCancelButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUOkButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class BatchAddTaskPanel extends JPanel implements ActionSupported {
	
	public static final String ACTION_OK = "OK";
	public static final String ACTION_CANCEL = "CANCEL";
	
	private ActionSupport actionSupport;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private JTextArea titlesTextArea;
	private JComboBox templateComboBox;
	
	public BatchAddTaskPanel() {
		this.actionSupport = new ActionSupport(this);
		this.initialize();
	}
	
	public JButton getOkButton() {
		return this.okButton;
	}
	
	public JButton getCancelButton() {
		return this.cancelButton;
	}
	
	@Override
	public void addActionListener(ActionListener listener) {
		this.actionSupport.addActionListener(listener);
	}
	
	@Override
	public void removeActionListener(ActionListener listener) {
		this.actionSupport.removeActionListener(listener);
	}
	
	private void initialize() {
		this.setLayout(new MigLayout());
		
		this.titlesTextArea = new JTextArea();
		this.titlesTextArea.setEditable(true);
		
		this.templateComboBox = new JComboBox();
		this.templateComboBox.setModel(new TaskTemplateModel(true));
		this.templateComboBox.setRenderer(new DefaultListRenderer(
				StringValueTaskTemplateTitle.INSTANCE));
		
		JLabel templateLabel = new JLabel(
				Translations.getString("general.template") + ": ");
		
		this.add(
				ComponentFactory.createJScrollPane(this.titlesTextArea, true),
				"grow, push, wrap 10px");
		this.add(templateLabel, "split");
		this.add(this.templateComboBox, "growx, pushx");
		
		JButton manageTemplates = new JButton(new ActionManageTaskTemplates(
				16,
				16));
		manageTemplates.setText(null);
		
		this.add(manageTemplates, "wrap");
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("OK")) {
					BatchAddTaskPanel.this.actionOk();
				} else {
					BatchAddTaskPanel.this.actionCancel();
				}
			}
			
		};
		
		this.okButton = new TUOkButton(listener);
		this.cancelButton = new TUCancelButton(listener);
		
		JPanel panel = new TUButtonsPanel(this.okButton, this.cancelButton);
		
		this.add(panel, "growx, pushx");
	}
	
	public void actionOk() {
		String titles = this.titlesTextArea.getText();
		TaskTemplate template = (TaskTemplate) this.templateComboBox.getSelectedItem();
		
		if (titles == null || titles.length() == 0)
			return;
		
		String[] titleArray = titles.split("\n");
		
		ActionBatchAddTasks.batchAddTasks(template, titleArray);
		
		BatchAddTaskPanel.this.titlesTextArea.setText(null);
		BatchAddTaskPanel.this.templateComboBox.setSelectedItem(null);
		this.actionSupport.fireActionPerformed(0, ACTION_OK);
	}
	
	public void actionCancel() {
		BatchAddTaskPanel.this.titlesTextArea.setText(null);
		BatchAddTaskPanel.this.templateComboBox.setSelectedItem(null);
		this.actionSupport.fireActionPerformed(0, ACTION_CANCEL);
	}
	
}
