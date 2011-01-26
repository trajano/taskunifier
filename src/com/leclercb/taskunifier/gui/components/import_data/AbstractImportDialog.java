package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;

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

import com.leclercb.commons.api.coder.FactoryCoder;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.translations.Translations;

public abstract class AbstractImportDialog extends JDialog {
	
	private FactoryCoder coder;
	private JFileChooser fileChooser;
	private JTextField importFile;
	private JCheckBox replaceValues;
	
	public AbstractImportDialog(
			FactoryCoder coder,
			String title,
			Frame frame,
			boolean modal) {
		super(frame, modal);
		
		CheckUtils.isNotNull(coder, "Coder cannot be null");
		
		this.coder = coder;
		
		this.initialize(title);
	}
	
	private void initialize(String title) {
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
				return Translations.getString("general.xml_files");
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				
				String extention = FileUtils.getExtention(f.getName());
				
				return "xml".equals(extention);
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
		panel.add(new JLabel(
				Translations.getString("import.delete_existing_values")));
		this.replaceValues = new JCheckBox();
		
		panel.add(this.replaceValues);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(panel, 2, 2, 6, 6, 6, 6);
		
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
						FileInputStream input = new FileInputStream(
								AbstractImportDialog.this.importFile.getText());
						coder.decode(input);
						
						deleteExistingValue();
						
						AbstractImportDialog.this.dispose();
					} catch (Exception e) {
						e.printStackTrace();
						ErrorDialog errorDialog = new ErrorDialog(
								MainFrame.getInstance().getFrame(),
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
	
	public abstract void deleteExistingValue();
	
}
