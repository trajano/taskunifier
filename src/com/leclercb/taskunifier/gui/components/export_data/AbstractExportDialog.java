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
package com.leclercb.taskunifier.gui.components.export_data;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;

public abstract class AbstractExportDialog extends JDialog {
	
	private String fileExtention;
	private String fileExtentionDescription;
	private JFileChooser fileChooser;
	private JTextField exportFile;
	
	public AbstractExportDialog(
			String title,
			Frame frame,
			boolean modal,
			String fileExtention,
			String fileExtentionDescription) {
		super(frame, modal);
		
		CheckUtils.isNotNull(fileExtention, "File extention cannot be null");
		CheckUtils.isNotNull(
				fileExtentionDescription,
				"File extention description cannot be null");
		
		this.fileExtention = fileExtention;
		this.fileExtentionDescription = fileExtentionDescription;
		
		this.initialize(title);
	}
	
	private void initialize(String title) {
		this.setTitle(title);
		this.setSize(500, 120);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JPanel panel = null;
		
		panel = new JPanel(new SpringLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(panel, BorderLayout.NORTH);
		
		// Export file
		panel.add(new JLabel(Translations.getString("export.export_to_file")));
		
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.fileChooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return AbstractExportDialog.this.fileExtentionDescription;
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				
				String extention = FileUtils.getExtention(f.getName());
				
				return AbstractExportDialog.this.fileExtention.equals(extention);
			}
			
		});
		
		this.exportFile = new JTextField();
		JButton openFile = new JButton(Translations.getString("general.select"));
		
		openFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = AbstractExportDialog.this.fileChooser.showSaveDialog(AbstractExportDialog.this);
				
				if (result == JFileChooser.APPROVE_OPTION) {
					AbstractExportDialog.this.exportFile.setText(AbstractExportDialog.this.fileChooser.getSelectedFile().getAbsolutePath());
					if (!AbstractExportDialog.this.exportFile.getText().endsWith(
							"." + AbstractExportDialog.this.fileExtention))
						AbstractExportDialog.this.exportFile.setText(AbstractExportDialog.this.exportFile.getText()
								+ "."
								+ AbstractExportDialog.this.fileExtention);
				}
			}
			
		});
		
		JPanel importFilePanel = new JPanel();
		importFilePanel.setLayout(new BorderLayout(5, 0));
		importFilePanel.add(this.exportFile, BorderLayout.CENTER);
		importFilePanel.add(openFile, BorderLayout.EAST);
		
		panel.add(importFilePanel);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(panel, 1, 2, 6, 6, 6, 6);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "EXPORT") {
					try {
						AbstractExportDialog.this.exportToFile(AbstractExportDialog.this.exportFile.getText());
						AbstractExportDialog.this.dispose();
					} catch (Exception e) {
						ErrorDialog errorDialog = new ErrorDialog(
								MainFrame.getInstance().getFrame(),
								null,
								e,
								false);
						errorDialog.setVisible(true);
					}
				}
				
				if (event.getActionCommand() == "CANCEL") {
					AbstractExportDialog.this.dispose();
				}
			}
			
		};
		
		JButton exportButton = new JButton(
				Translations.getString("general.export"));
		exportButton.setActionCommand("EXPORT");
		exportButton.addActionListener(listener);
		buttonsPanel.add(exportButton);
		
		JButton cancelButton = new JButton(
				Translations.getString("general.cancel"));
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(listener);
		buttonsPanel.add(cancelButton);
		
		this.getRootPane().setDefaultButton(exportButton);
	}
	
	protected abstract void exportToFile(String file) throws Exception;
	
}
