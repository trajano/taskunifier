package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.translations.Translations;

public abstract class AbstractImportDialog extends JDialog {
	
	private JFileChooser fileChooser;
	private JTextField importFile;
	private JCheckBox replaceValues;
	private String fileExtention;
	private String fileExtentionDescription;
	
	public AbstractImportDialog(
			String title,
			Frame frame,
			boolean modal,
			boolean showReplaceValues,
			String fileExtention,
			String fileExtentionDescription) {
		super(frame, modal);
		
		CheckUtils.isNotNull(fileExtention, "File extention cannot be null");
		CheckUtils.isNotNull(
				fileExtentionDescription,
		"File extention description cannot be null");
		
		this.fileExtention = fileExtention;
		this.fileExtentionDescription = fileExtentionDescription;
		
		this.initialize(title, showReplaceValues);
	}
	
	private void initialize(String title, boolean showReplaceValues) {
		this.setTitle(title);
		this.setSize(500, 150);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JPanel panel = null;
		
		panel = new JPanel(new SpringLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(panel, BorderLayout.NORTH);
		
		// Import file
		panel.add(new JLabel(Translations.getString("import.file_to_import")));
		
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.fileChooser.setFileFilter(new FileFilter() {
			
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
			
		});
		
		this.importFile = new JTextField();
		JButton openFile = new JButton(Translations.getString("general.open"));
		
		openFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = AbstractImportDialog.this.fileChooser.showOpenDialog(AbstractImportDialog.this);
				
				if (result == JFileChooser.APPROVE_OPTION)
					AbstractImportDialog.this.importFile.setText(AbstractImportDialog.this.fileChooser.getSelectedFile().getAbsolutePath());
			}
			
		});
		
		JPanel importFilePanel = new JPanel();
		importFilePanel.setLayout(new BorderLayout(5, 0));
		importFilePanel.add(this.importFile, BorderLayout.CENTER);
		importFilePanel.add(openFile, BorderLayout.EAST);
		
		panel.add(importFilePanel);
		
		// Replace values
		if (showReplaceValues) {
			panel.add(new JLabel(
					Translations.getString("import.delete_existing_values")));
			this.replaceValues = new JCheckBox();
			
			panel.add(this.replaceValues);
		}
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(panel, (showReplaceValues? 2 : 1), 2, 6, 6, 6, 6);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "IMPORT") {
					try {
						if (replaceValues != null && 
								AbstractImportDialog.this.replaceValues.isSelected())
							AbstractImportDialog.this.deleteExistingValue();
						
						AbstractImportDialog.this.importFromFile(AbstractImportDialog.this.importFile.getText());
						
						AbstractImportDialog.this.dispose();
					} catch (Exception e) {
						e.printStackTrace();
						ErrorDialog errorDialog = new ErrorDialog(
								MainFrame.getInstance().getFrame(),
								null,
								e,
								false);
						errorDialog.setVisible(true);
					}
				}
				
				if (event.getActionCommand() == "CANCEL") {
					AbstractImportDialog.this.dispose();
				}
			}
			
		};
		
		JButton importButton = new JButton(
				Translations.getString("general.import"));
		importButton.setActionCommand("IMPORT");
		importButton.addActionListener(listener);
		buttonsPanel.add(importButton);
		
		JButton cancelButton = new JButton(
				Translations.getString("general.cancel"));
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(listener);
		buttonsPanel.add(cancelButton);
		
		this.getRootPane().setDefaultButton(importButton);
	}
	
	protected abstract void deleteExistingValue();
	
	protected abstract void importFromFile(String file) throws Exception;
	
}
