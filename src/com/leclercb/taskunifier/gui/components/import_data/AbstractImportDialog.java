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
package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.TUFileField;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUCancelButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.FormBuilder;

abstract class AbstractImportDialog extends JDialog {
	
	private TUFileField fileField;
	private JCheckBox deleteExistingValues;
	private String fileExtention;
	private String fileExtentionDescription;
	private String fileProperty;
	
	public AbstractImportDialog(
			String title,
			boolean showDeleteExistingValues,
			String fileExtention,
			String fileExtentionDescription,
			String fileProperty) {
		super(MainFrame.getInstance().getFrame());
		
		CheckUtils.isNotNull(fileExtention);
		CheckUtils.isNotNull(fileExtentionDescription);
		
		this.fileExtention = fileExtention;
		this.fileExtentionDescription = fileExtentionDescription;
		this.fileProperty = fileProperty;
		
		this.initialize(title, showDeleteExistingValues);
	}
	
	private void initialize(String title, boolean showDeleteExistingValues) {
		this.setModal(true);
		this.setTitle(title);
		this.setSize(500, 150);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				AbstractImportDialog.this.fileField.setFile(null);
				AbstractImportDialog.this.deleteExistingValues.setSelected(false);
				AbstractImportDialog.this.setVisible(false);
			}
			
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(panel, BorderLayout.NORTH);
		
		FormBuilder builder = new FormBuilder(
				"right:pref, 4dlu, fill:default:grow");
		
		// Import file
		FileFilter fileFilter = new FileFilter() {
			
			@Override
			public String getDescription() {
				return AbstractImportDialog.this.fileExtentionDescription;
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				
				String extention = FileUtils.getExtention(f.getName());
				
				return AbstractImportDialog.this.fileExtention.equals(extention);
			}
			
		};
		
		String defaultFile = null;
		
		if (this.fileProperty != null)
			defaultFile = Main.getSettings().getStringProperty(
					this.fileProperty);
		
		this.fileField = new TUFileField(
				Translations.getString("general.file"),
				true,
				defaultFile,
				JFileChooser.FILES_ONLY,
				fileFilter,
				null);
		
		builder.appendI15d("import.file_to_import", true, this.fileField);
		
		// Delete existing values
		this.deleteExistingValues = new JCheckBox();
		
		if (showDeleteExistingValues) {
			builder.appendI15d(
					"import.delete_existing_values",
					true,
					this.deleteExistingValues);
		}
		
		// Lay out the panel
		panel.add(builder.getPanel(), BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("IMPORT")) {
					try {
						if (AbstractImportDialog.this.fileProperty != null)
							Main.getSettings().setStringProperty(
									AbstractImportDialog.this.fileProperty,
									AbstractImportDialog.this.fileField.getFile());
						
						if (AbstractImportDialog.this.deleteExistingValues.isSelected())
							AbstractImportDialog.this.deleteExistingValue();
						
						AbstractImportDialog.this.importFromFile(AbstractImportDialog.this.fileField.getFile());
						
						// AbstractImportDialog.this.fileField.setFile(null);
						AbstractImportDialog.this.deleteExistingValues.setSelected(false);
						AbstractImportDialog.this.setVisible(false);
					} catch (Exception e) {
						ErrorInfo info = new ErrorInfo(
								Translations.getString("general.error"),
								e.getMessage(),
								null,
								null,
								e,
								null,
								null);
						
						JXErrorPane.showDialog(
								MainFrame.getInstance().getFrame(),
								info);
					}
				}
				
				if (event.getActionCommand().equals("CANCEL")) {
					AbstractImportDialog.this.fileField.setFile(null);
					AbstractImportDialog.this.deleteExistingValues.setSelected(false);
					AbstractImportDialog.this.setVisible(false);
				}
			}
			
		};
		
		JButton importButton = new JButton(
				Translations.getString("general.import"));
		importButton.setActionCommand("IMPORT");
		importButton.addActionListener(listener);
		
		JButton cancelButton = new TUCancelButton(listener);
		
		JPanel panel = new TUButtonsPanel(importButton, cancelButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(importButton);
	}
	
	protected abstract void deleteExistingValue();
	
	protected abstract void importFromFile(String file) throws Exception;
	
}
