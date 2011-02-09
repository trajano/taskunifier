package com.leclercb.taskunifier.gui.components.export_data;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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

public class DefaultExportDialog extends JDialog {
	
	private FactoryCoder coder;
	private JFileChooser fileChooser;
	private JTextField exportFile;
	
	public DefaultExportDialog(
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
		
		this.exportFile = new JTextField();
		JButton openFile = new JButton(Translations.getString("general.select"));
		
		openFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = DefaultExportDialog.this.fileChooser.showSaveDialog(DefaultExportDialog.this);
				
				if (result == JFileChooser.APPROVE_OPTION)
					DefaultExportDialog.this.exportFile.setText(DefaultExportDialog.this.fileChooser.getSelectedFile().getAbsolutePath());
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
						FileOutputStream output = new FileOutputStream(
								DefaultExportDialog.this.exportFile.getText());
						DefaultExportDialog.this.coder.encode(output);
						
						DefaultExportDialog.this.dispose();
					} catch (Exception e) {
						ErrorDialog errorDialog = new ErrorDialog(
								MainFrame.getInstance().getFrame(),
								e,
								false);
						errorDialog.setVisible(true);
					}
				}
				
				if (event.getActionCommand() == "CANCEL") {
					DefaultExportDialog.this.dispose();
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
	
}
