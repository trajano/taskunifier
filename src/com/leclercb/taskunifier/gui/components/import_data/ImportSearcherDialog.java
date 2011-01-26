package com.leclercb.taskunifier.gui.components.import_data;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

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

import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.searchers.coder.TaskSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ImportSearcherDialog extends JDialog {
	
	private JFileChooser fileChooser;
	private JTextField importFile;
	private JCheckBox replaceSearchers;
	
	public ImportSearcherDialog(Frame frame, boolean modal) {
		super(frame, modal);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.import_searchers"));
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
		panel.add(new JLabel(
				Translations.getString("import_searcher.file_to_import")));
		
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
				int result = ImportSearcherDialog.this.fileChooser.showOpenDialog(ImportSearcherDialog.this);
				
				if (result == JFileChooser.APPROVE_OPTION)
					ImportSearcherDialog.this.importFile.setText(ImportSearcherDialog.this.fileChooser.getSelectedFile().getAbsolutePath());
			}
			
		});
		
		JPanel importFilePanel = new JPanel();
		importFilePanel.setLayout(new BorderLayout(5, 0));
		importFilePanel.add(this.importFile, BorderLayout.CENTER);
		importFilePanel.add(openFile, BorderLayout.EAST);
		
		panel.add(importFilePanel);
		
		// Replace searchers
		panel.add(new JLabel(
				Translations.getString("import_searcher.delete_existing_searchers")));
		this.replaceSearchers = new JCheckBox();
		
		panel.add(this.replaceSearchers);
		
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
						List<TaskSearcher> existingSearchers = TaskSearcherFactory.getInstance().getList();
						
						FileInputStream input = new FileInputStream(
								ImportSearcherDialog.this.importFile.getText());
						new TaskSearcherFactoryXMLCoder().decode(input);
						
						for (TaskSearcher searcher : existingSearchers) {
							TaskSearcherFactory.getInstance().delete(searcher);
						}
						
						ImportSearcherDialog.this.dispose();
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
					ImportSearcherDialog.this.dispose();
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
	
}
