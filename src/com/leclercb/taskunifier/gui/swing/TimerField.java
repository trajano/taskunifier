package com.leclercb.taskunifier.gui.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Timer;
import com.leclercb.taskunifier.gui.utils.Images;

public class TimerField extends JPanel {
	
	private boolean showButton;
	private Timer timer;
	
	private JSpinner timeSpinner;
	private JToggleButton button;
	
	public TimerField(boolean showButton) {
		this(showButton, new Timer());
	}
	
	public TimerField(boolean showButton, Timer timer) {
		this.showButton = showButton;
		this.initialize();
		this.setTimer(timer);
	}
	
	public void commitEdit() {
		try {
			this.timeSpinner.commitEdit();
		} catch (ParseException e) {
			
		}
	}
	
	public Timer getTimer() {
		return this.timer;
	}
	
	public void setTimer(Timer timer) {
		CheckUtils.isNotNull(timer, "Timer cannot be null");
		this.timer = timer;
		
		this.timeSpinner.setValue((int) (this.timer.getTimerValue() / 60));
		this.button.setSelected(this.timer.isStarted());
	}
	
	private void initialize() {
		this.timeSpinner = new JSpinner();
		this.timeSpinner.setModel(new SpinnerTimeModel());
		this.timeSpinner.setEditor(new SpinnerTimeEditor(this.timeSpinner));
		
		this.button = new JToggleButton();
		this.button.setIcon(Images.getResourceImage("pause.png", 16, 16));
		this.button.setSelectedIcon(Images.getResourceImage("play.png", 16, 16));
		
		this.button.setBorderPainted(false);
		this.button.setContentAreaFilled(false);
		
		this.setLayout(new BorderLayout());
		this.add(this.timeSpinner, BorderLayout.CENTER);
		
		if (this.showButton)
			this.add(this.button, BorderLayout.WEST);
		
		this.timeSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent evt) {
				TimerField.this.timer.setValue(((Number) TimerField.this.timeSpinner.getValue()).longValue() * 60);
			}
			
		});
		
		this.button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				if (TimerField.this.button.isSelected())
					TimerField.this.timer.start();
				else
					TimerField.this.timer.stop();
			}
			
		});
	}
	
}
