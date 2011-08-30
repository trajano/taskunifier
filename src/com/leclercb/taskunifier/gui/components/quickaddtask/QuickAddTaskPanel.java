package com.leclercb.taskunifier.gui.components.quickaddtask;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.leclercb.taskunifier.gui.actions.ActionAddQuickTask;
import com.leclercb.taskunifier.gui.utils.Images;

public class QuickAddTaskPanel extends JPanel {
	
	private JTextField textField;
	private JButton button;
	
	public QuickAddTaskPanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.textField = new JTextField();
		this.textField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					QuickAddTaskPanel.this.addQuickTask();
				}
			}
			
		});
		
		this.button = new JButton(Images.getResourceImage("task.png", 16, 16));
		this.button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				QuickAddTaskPanel.this.addQuickTask();
			}
			
		});
		
		this.setLayout(new BorderLayout(3, 3));
		this.add(this.textField, BorderLayout.CENTER);
		this.add(this.button, BorderLayout.EAST);
	}
	
	private void addQuickTask() {
		String title = this.textField.getText();
		ActionAddQuickTask.addQuickTask(title, false);
		this.textField.setText("");
	}
	
}
