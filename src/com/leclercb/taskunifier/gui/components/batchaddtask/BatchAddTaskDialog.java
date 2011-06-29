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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.actions.ActionBatchAddTasks;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.commons.models.TemplateModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueTemplateTitle;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class BatchAddTaskDialog extends JDialog {
	
	private static BatchAddTaskDialog INSTANCE;
	
	public static BatchAddTaskDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new BatchAddTaskDialog();
		
		return INSTANCE;
	}
	
	private JTextArea answerTextArea;
	private JComboBox templateComboBox;
	
	private BatchAddTaskDialog() {
		super(MainFrame.getInstance().getFrame());
		this.initialize();
	}
	
	private void initialize() {
		this.setModal(true);
		this.setTitle(Translations.getString("general.batch_add_tasks"));
		this.setSize(600, 350);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				BatchAddTaskDialog.this.answerTextArea.setText(null);
				BatchAddTaskDialog.this.templateComboBox.setSelectedItem(null);
				
				BatchAddTaskDialog.this.setVisible(false);
			}
			
		});
		
		JPanel panel = null;
		
		panel = new JPanel(new BorderLayout(20, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
		this.add(panel, BorderLayout.NORTH);
		
		JLabel icon = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
		panel.add(icon, BorderLayout.WEST);
		
		JLabel question = new JLabel(
				Translations.getString("batch_add_tasks.insert_task_titles"));
		panel.add(question, BorderLayout.CENTER);
		
		panel = new JPanel(new BorderLayout(0, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		
		this.answerTextArea = new JTextArea();
		this.answerTextArea.setEditable(true);
		
		JPanel templatePanel = new JPanel();
		templatePanel.setLayout(new BorderLayout());
		
		this.templateComboBox = new JComboBox();
		this.templateComboBox.setModel(new TemplateModel(true));
		this.templateComboBox.setRenderer(new DefaultListRenderer(
				StringValueTemplateTitle.INSTANCE));
		
		templatePanel.add(new JLabel(Translations.getString("general.template")
				+ ": "), BorderLayout.WEST);
		templatePanel.add(this.templateComboBox, BorderLayout.CENTER);
		
		panel.add(
				ComponentFactory.createJScrollPane(this.answerTextArea, true),
				BorderLayout.CENTER);
		panel.add(templatePanel, BorderLayout.SOUTH);
		
		this.add(panel, BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("OK")) {
					String answer = BatchAddTaskDialog.this.answerTextArea.getText();
					Template template = (Template) BatchAddTaskDialog.this.templateComboBox.getSelectedItem();
					
					if (answer == null)
						return;
					
					String[] titles = answer.split("\n");
					
					ActionBatchAddTasks.batchAddTasks(template, titles);
				}
				
				BatchAddTaskDialog.this.answerTextArea.setText(null);
				BatchAddTaskDialog.this.templateComboBox.setSelectedItem(null);
				
				BatchAddTaskDialog.this.setVisible(false);
			}
			
		};
		
		JButton okButton = ComponentFactory.createButtonOk(listener);
		JButton cancelButton = ComponentFactory.createButtonCancel(listener);
		
		JPanel panel = ComponentFactory.createButtonsPanel(
				okButton,
				cancelButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(okButton);
	}
	
}
