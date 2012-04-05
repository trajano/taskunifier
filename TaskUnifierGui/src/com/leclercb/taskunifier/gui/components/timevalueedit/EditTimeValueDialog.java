package com.leclercb.taskunifier.gui.components.timevalueedit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;

import com.leclercb.taskunifier.gui.main.frames.FrameUtils;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.TimeValue;

public class EditTimeValueDialog extends JDialog {
	
	private static EditTimeValueDialog INSTANCE;
	
	public static EditTimeValueDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new EditTimeValueDialog();
		
		return INSTANCE;
	}
	
	private EditTimeValuePanel editTimeValuePanel;
	
	private EditTimeValueDialog() {
		this.initialize();
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			this.setLocationRelativeTo(FrameUtils.getCurrentFrameView().getFrame());
		}
		
		super.setVisible(visible);
	}
	
	private void initialize() {
		this.setModal(true);
		this.setTitle(Translations.getString("general.edit_postpone_item"));
		this.setSize(400, 150);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		this.editTimeValuePanel = new EditTimeValuePanel();
		this.editTimeValuePanel.setBorder(BorderFactory.createEmptyBorder(
				10,
				10,
				0,
				10));
		
		this.add(this.editTimeValuePanel, BorderLayout.CENTER);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				EditTimeValueDialog.this.editTimeValuePanel.actionCancel();
			}
			
		});
		
		this.editTimeValuePanel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EditTimeValueDialog.this.setVisible(false);
			}
			
		});
		
		this.getRootPane().setDefaultButton(
				this.editTimeValuePanel.getOkButton());
	}
	
	public TimeValue getTimeValue() {
		return this.editTimeValuePanel.getTimeValue();
	}
	
	public void setTimeValue(TimeValue timeValue) {
		this.editTimeValuePanel.setTimeValue(timeValue);
	}
	
}
