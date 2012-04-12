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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.leclercb.taskunifier.gui.utils.FormBuilder;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class TUFileField extends JPanel {
	
	private JFileChooser fileChooser;
	private JTextField fileTextField;
	private JButton selectFile;
	private String appendFileExtention;
	
	public TUFileField(
			String label,
			boolean open,
			String file,
			int fileSelectionMode,
			FileFilter fileFilter,
			String appendFileExtention) {
		this.appendFileExtention = appendFileExtention;
		this.initialize(label, open, file, fileSelectionMode, fileFilter);
	}
	
	public String getFile() {
		return this.fileTextField.getText();
	}
	
	public void setFile(String file) {
		this.fileTextField.setText(file);
	}
	
	private void initialize(
			final String label,
			final boolean open,
			final String file,
			final int fileSelectionMode,
			final FileFilter fileFilter) {
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(fileSelectionMode);
		this.fileChooser.setFileFilter(fileFilter);
		
		this.fileTextField = new JTextField();
		
		if (file != null)
			this.fileTextField.setText(file);
		
		this.selectFile = new JButton(ImageUtils.getResourceImage(
				"folder.png",
				16,
				16));
		this.selectFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TUFileField.this.fileChooser.setCurrentDirectory(new File(
						TUFileField.this.getFile()));
				
				int result;
				
				if (open)
					result = TUFileField.this.fileChooser.showOpenDialog(TUFileField.this);
				else
					result = TUFileField.this.fileChooser.showSaveDialog(TUFileField.this);
				
				if (result == JFileChooser.APPROVE_OPTION) {
					String file = TUFileField.this.fileChooser.getSelectedFile().getAbsolutePath();
					TUFileField.this.fileTextField.setText(file);
					
					if (TUFileField.this.appendFileExtention != null) {
						if (!file.endsWith("."
								+ TUFileField.this.appendFileExtention)) {
							file += "." + TUFileField.this.appendFileExtention;
							TUFileField.this.fileTextField.setText(file);
						}
					}
				}
			}
			
		});
		
		this.setLayout(new BorderLayout(3, 0));
		
		FormBuilder builder = new FormBuilder(
				"right:pref, 4dlu, fill:default:grow, 4dlu, pref");
		
		if (label != null)
			builder.append(new JLabel(label + ": "));
		else
			builder.append(new JLabel());
		
		builder.append(this.fileTextField);
		builder.append(this.selectFile);
		
		this.add(builder.getPanel(), BorderLayout.CENTER);
	}
	
}
