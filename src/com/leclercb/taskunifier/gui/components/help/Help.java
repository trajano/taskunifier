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
package com.leclercb.taskunifier.gui.components.help;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.io.FileUtils;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public final class Help {
	
	private Help() {

	}
	
	public static final String HELP_FILES_FOLDER = Main.RESOURCES_FOLDER
			+ File.separator
			+ "help";
	
	public static String getHelpFile(String fileName) {
		return HELP_FILES_FOLDER + File.separator + fileName;
	}
	
	public static String getContent(String helpFile) {
		CheckUtils.isNotNull(helpFile, "Help file cannot be null");
		
		String content = "";
		
		try {
			File file = new File(helpFile);
			
			if (file.getName().equals("task_repeat.html")) {
				if (SynchronizerUtils.getPlugin().getSynchronizerApi().getRepeatHelpFile() != null) {
					file = new File(
							SynchronizerUtils.getPlugin().getSynchronizerApi().getRepeatHelpFile());
					
					content = FileUtils.readFileToString(file);
				}
			} else {
				content = FileUtils.readFileToString(file);
			}
			
			// Replace parameters
			content = content.replace(
					"{plugin_name}",
					SynchronizerUtils.getPlugin().getName());
			
			content = content.replace("{resources_folder}", new File(
					Main.RESOURCES_FOLDER).getAbsolutePath());
			
			content = content.replace("{help_folder}", new File(
					HELP_FILES_FOLDER).getAbsolutePath());
		} catch (Throwable t) {

		}
		
		return content;
	}
	
	public static void showHelpDialog(final String helpFile) {
		CheckUtils.isNotNull(helpFile, "Help file cannot be null");
		
		HelpDialog.getInstance().setText(getContent(helpFile));
		HelpDialog.getInstance().setVisible(true);
	}
	
	public static JButton getHelpButton(final String helpFile) {
		CheckUtils.isNotNull(helpFile, "Help file cannot be null");
		
		JButton button = new JButton(
				Images.getResourceImage("help.png", 16, 16));
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.addActionListener(new HelpActionListener(helpFile));
		
		return button;
	}
	
	private static class HelpActionListener implements ActionListener {
		
		private String helpFile;
		
		public HelpActionListener(String helpFile) {
			this.helpFile = helpFile;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			HelpDialog.getInstance().setText(getContent(this.helpFile));
			HelpDialog.getInstance().setVisible(true);
		}
		
	}
	
	private static class HelpDialog extends JDialog {
		
		private static HelpDialog INSTANCE;
		
		public static HelpDialog getInstance() {
			if (INSTANCE == null)
				INSTANCE = new HelpDialog();
			
			return INSTANCE;
		}
		
		private JXEditorPane helpEditorPane;
		
		private HelpDialog() {
			super(MainFrame.getInstance().getFrame());
			
			this.initialize();
		}
		
		public void setText(String text) {
			this.helpEditorPane.setText(text);
			this.helpEditorPane.setCaretPosition(0);
		}
		
		private void initialize() {
			this.setModal(true);
			this.setTitle(Translations.getString("general.help"));
			this.setSize(600, 600);
			this.setResizable(true);
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(HIDE_ON_CLOSE);
			
			if (this.getOwner() != null)
				this.setLocationRelativeTo(this.getOwner());
			
			this.helpEditorPane = new JXEditorPane();
			this.helpEditorPane.setContentType("text/html");
			this.helpEditorPane.setEditable(false);
			this.helpEditorPane.setCaretPosition(0);
			this.helpEditorPane.addHyperlinkListener(new HyperlinkListener() {
				
				@Override
				public void hyperlinkUpdate(HyperlinkEvent evt) {
					if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						try {
							HelpDialog.this.helpEditorPane.setText(getContent(evt.getURL().getFile()));
							HelpDialog.this.helpEditorPane.setCaretPosition(0);
						} catch (Exception e) {
							ErrorInfo info = new ErrorInfo(
									Translations.getString("general.error"),
									Translations.getString("error.help_file_not_found"),
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
				}
				
			});
			
			this.add(ComponentFactory.createJScrollPane(
					this.helpEditorPane,
					false), BorderLayout.CENTER);
		}
		
	}
	
}
