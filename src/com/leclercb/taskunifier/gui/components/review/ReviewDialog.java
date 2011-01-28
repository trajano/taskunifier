package com.leclercb.taskunifier.gui.components.review;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.leclercb.taskunifier.gui.translations.Translations;

public class ReviewDialog extends JDialog {
	
	public ReviewDialog(Frame frame, boolean modal) {
		super(frame, modal);
		this.initialize();
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.review"));
		this.setSize(400, 300);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		ReviewPanel reviewPanel = new ReviewPanel();
		reviewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(reviewPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		this.initializeButtons(buttonPanel);
	}
	
	private void initializeButtons(JPanel buttonPanel) {
		ActionListener actionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ReviewDialog.this.setVisible(false);
			}
			
		};
		
		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.addActionListener(actionListener);
		buttonPanel.add(okButton);
		
		this.getRootPane().setDefaultButton(okButton);
	}
	
}
