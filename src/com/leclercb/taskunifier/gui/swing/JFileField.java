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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.leclercb.taskunifier.gui.translations.Translations;

public class JFileField extends JPanel {
	
	private JFileChooser fileChooser;
	private JTextField fileTextField;
	private JButton selectFile;
	private String appendFileExtention;
	
	public JFileField(
			String file,
			int fileSelectionMode,
			FileFilter fileFilter,
			String appendFileExtention) {
		this.appendFileExtention = appendFileExtention;
		this.initialize(file, fileSelectionMode, fileFilter);
	}
	
	public String getFile() {
		return this.fileTextField.getText();
	}
	
	public void setFile(String file) {
		this.fileTextField.setText(file);
	}
	
	private void initialize(
			String file,
			int fileSelectionMode,
			FileFilter fileFilter) {
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(fileSelectionMode);
		this.fileChooser.setFileFilter(fileFilter);
		
		this.fileTextField = new JTextField();
		
		if (file != null)
			this.fileTextField.setText(file);
		
		this.selectFile = new JButton(Translations.getString("general.select"));
		this.selectFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileField.this.fileChooser.setCurrentDirectory(new File(
						JFileField.this.getFile()));
				
				int result = JFileField.this.fileChooser.showOpenDialog(JFileField.this);
				
				if (result == JFileChooser.APPROVE_OPTION) {
					String file = JFileField.this.fileChooser.getSelectedFile().getAbsolutePath();
					JFileField.this.fileTextField.setText(file);
					
					if (JFileField.this.appendFileExtention != null) {
						if (!file.endsWith("."
								+ JFileField.this.appendFileExtention)) {
							file += "." + JFileField.this.appendFileExtention;
							JFileField.this.fileTextField.setText(file);
						}
					}
				}
			}
			
		});
		
		this.setLayout(new BorderLayout(5, 0));
		this.add(this.fileTextField, BorderLayout.CENTER);
		this.add(this.selectFile, BorderLayout.EAST);
	}
	
}
