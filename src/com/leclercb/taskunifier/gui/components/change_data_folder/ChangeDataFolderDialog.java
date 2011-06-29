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
package com.leclercb.taskunifier.gui.components.change_data_folder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.FormBuilder;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ChangeDataFolderDialog extends JDialog {
	
	private static ChangeDataFolderDialog INSTANCE;
	
	public static ChangeDataFolderDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ChangeDataFolderDialog();
		
		return INSTANCE;
	}
	
	private JFileChooser fileChooser;
	private JTextField changeLocation;
	
	private ChangeDataFolderDialog() {
		super(MainFrame.getInstance().getFrame());
		
		this.initialize();
	}
	
	@Override
	public void setVisible(boolean b) {
		if (b) {
			File file = new File(Main.DATA_FOLDER);
			this.changeLocation.setText(file.getAbsolutePath());
		}
		
		super.setVisible(b);
	}
	
	private void initialize() {
		this.setModal(true);
		this.setTitle(Translations.getString("action.name.change_data_folder_location"));
		this.setSize(500, 150);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				ChangeDataFolderDialog.this.changeLocation.setText(null);
				ChangeDataFolderDialog.this.setVisible(false);
			}
			
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(panel, BorderLayout.NORTH);
		
		FormBuilder builder = new FormBuilder(
				"right:pref, 4dlu, fill:default:grow");
		
		// Import file
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		this.changeLocation = new JTextField();
		JButton openFile = new JButton(Translations.getString("general.open"));
		
		openFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = ChangeDataFolderDialog.this.fileChooser.showOpenDialog(ChangeDataFolderDialog.this);
				
				if (result == JFileChooser.APPROVE_OPTION)
					ChangeDataFolderDialog.this.changeLocation.setText(ChangeDataFolderDialog.this.fileChooser.getSelectedFile().getAbsolutePath());
			}
			
		});
		
		JPanel changeLocationPanel = new JPanel();
		changeLocationPanel.setLayout(new BorderLayout(5, 0));
		changeLocationPanel.add(this.changeLocation, BorderLayout.CENTER);
		changeLocationPanel.add(openFile, BorderLayout.EAST);
		
		builder.appendI15d("general.folder", true, changeLocationPanel);
		
		// Lay out the panel
		panel.add(builder.getPanel(), BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				boolean stop = false;
				
				if (event.getActionCommand().equals("RESET")) {
					stop = true;
					
					Main.INIT_SETTINGS.remove("com.leclercb.taskunifier.data_folder");
				}
				
				if (event.getActionCommand().equals("CHANGE")) {
					stop = true;
					
					String path = ChangeDataFolderDialog.this.changeLocation.getText();
					
					if (path == null || path.length() == 0)
						;
					
					File file = new File(path);
					
					if (!file.exists() || !file.isDirectory())
						;
					
					Main.INIT_SETTINGS.setStringProperty(
							"com.leclercb.taskunifier.data_folder",
							file.getAbsolutePath());
				}
				
				ChangeDataFolderDialog.this.changeLocation.setText(null);
				ChangeDataFolderDialog.this.setVisible(false);
				
				if (stop)
					Main.stop();
			}
			
		};
		
		JButton resetButton = new JButton(
				Translations.getString("general.reset_default"));
		resetButton.setActionCommand("RESET");
		resetButton.addActionListener(listener);
		
		JButton importButton = new JButton(
				Translations.getString("action.name.change_data_folder_location"));
		importButton.setActionCommand("CHANGE");
		importButton.addActionListener(listener);
		
		JButton cancelButton = ComponentFactory.createButtonCancel(listener);
		
		JPanel panel = ComponentFactory.createButtonsPanel(
				resetButton,
				importButton,
				cancelButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(importButton);
	}
	
}
