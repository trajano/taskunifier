package com.leclercb.taskunifier.gui.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.leclercb.taskunifier.gui.translations.Translations;

public class JTextAreaDialog extends JDialog {
	
	private String question;
	private String initialAnswer;
	private String answer;
	
	private JTextArea answerTextArea;
	
	public JTextAreaDialog(Frame frame, String title, String question) {
		this(frame, title, question, "");
	}
	
	public JTextAreaDialog(
			Frame frame,
			String title,
			String question,
			String initialAnswer) {
		super(frame, title, true);
		
		if (initialAnswer == null)
			initialAnswer = "";
		
		this.question = question;
		this.initialAnswer = initialAnswer;
		this.initialize();
	}
	
	public String getAnswer() {
		return this.answer;
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
		
		JLabel icon = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
		panel.add(icon, BorderLayout.WEST);
		
		JLabel question = new JLabel(this.question);
		panel.add(question, BorderLayout.CENTER);
		
		panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		this.answerTextArea = new JTextArea(this.initialAnswer);
		this.answerTextArea.setEditable(true);
		panel.add(new JScrollPane(this.answerTextArea), BorderLayout.CENTER);
		this.add(panel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "OK") {
					JTextAreaDialog.this.answer = JTextAreaDialog.this.answerTextArea.getText();
					JTextAreaDialog.this.dispose();
				}
				
				if (event.getActionCommand() == "CANCEL") {
					JTextAreaDialog.this.answer = null;
					JTextAreaDialog.this.dispose();
				}
			}
			
		};
		
		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		buttonsPanel.add(okButton);
		
		JButton cancelButton = new JButton(
				Translations.getString("general.cancel"));
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(listener);
		buttonsPanel.add(cancelButton);
		
		this.getRootPane().setDefaultButton(okButton);
	}
	
}
