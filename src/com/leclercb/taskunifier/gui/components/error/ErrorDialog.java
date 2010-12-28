package com.leclercb.taskunifier.gui.components.error;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.leclercb.taskunifier.gui.translations.Translations;

public class ErrorDialog extends JDialog {
	
	private Exception exception;
	
	public ErrorDialog(Frame frame, Exception exception) {
		this(frame, Translations.getString("general.error"), exception);
	}
	
	public ErrorDialog(Frame frame, String title, Exception exception) {
		super(frame, title, true);
		
		this.exception = exception;
		this.initialize();
	}
	
	private void initialize() {
		this.setSize(500, 300);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JPanel panel = null;
		
		panel = new JPanel(new BorderLayout(20, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.add(panel, BorderLayout.NORTH);
		
		JLabel icon = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));
		panel.add(icon, BorderLayout.WEST);
		
		JLabel error = new JLabel(this.exception.getMessage());
		panel.add(error, BorderLayout.CENTER);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		this.exception.printStackTrace(printWriter);
		printWriter.close();
		
		try {
			stringWriter.close();
		} catch (IOException e) {}
		
		panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		JTextArea stackTrace = new JTextArea(stringWriter.toString());
		stackTrace.setEditable(false);
		panel.add(new JScrollPane(stackTrace), BorderLayout.CENTER);
		this.add(panel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "CREATE_REPORT") {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int result = fc.showSaveDialog(ErrorDialog.this);
					
					if (result == JFileChooser.APPROVE_OPTION) {
						try {
							File file = fc.getSelectedFile();
							file.createNewFile();
							PrintStream ps = new PrintStream(file);
							ErrorDialog.this.exception.printStackTrace(ps);
							ps.close();
						} catch (Exception exc) {
							JOptionPane.showMessageDialog(
									null,
									exc.getMessage(),
									Translations.getString("general.error"),
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				
				if (event.getActionCommand() == "OK") {
					ErrorDialog.this.dispose();
				}
			}
			
		};
		
		JButton reportButton = new JButton(
				Translations.getString("general.create_report"));
		reportButton.setActionCommand("CREATE_REPORT");
		reportButton.addActionListener(listener);
		buttonsPanel.add(reportButton);
		
		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		buttonsPanel.add(okButton);
		
		this.getRootPane().setDefaultButton(okButton);
	}
}
