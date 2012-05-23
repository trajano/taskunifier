/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.timevalueedit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.event.action.ActionSupported;
import com.leclercb.commons.gui.utils.FormatterUtils;
import com.leclercb.taskunifier.gui.commons.values.StringValueCalendarField;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUCancelButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUOkButton;
import com.leclercb.taskunifier.gui.utils.FormBuilder;
import com.leclercb.taskunifier.gui.utils.TimeValue;

public class EditTimeValuePanel extends JPanel implements ActionSupported {
	
	public static final String ACTION_OK = "ACTION_OK";
	public static final String ACTION_CANCEL = "ACTION_CANCEL";
	
	private ActionSupport actionSupport;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private TimeValue timeValue;
	
	private JComboBox fieldField;
	private JFormattedTextField amountField;
	
	public EditTimeValuePanel() {
		this.actionSupport = new ActionSupport(this);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		FormBuilder builder = new FormBuilder(
				"right:pref, 4dlu, fill:default:grow");
		
		this.fieldField = new JComboBox(new Integer[] {
				Calendar.MINUTE,
				Calendar.HOUR_OF_DAY,
				Calendar.DAY_OF_MONTH,
				Calendar.WEEK_OF_YEAR,
				Calendar.MONTH,
				Calendar.YEAR });
		
		this.fieldField.setRenderer(new DefaultListRenderer(
				StringValueCalendarField.INSTANCE));
		
		this.amountField = new JFormattedTextField(
				FormatterUtils.getRegexFormatter("^[0-9]{1,4}$"));
		
		builder.appendI15d("general.postponeitem.field", true, this.fieldField);
		builder.appendI15d(
				"general.postponeitem.amount",
				true,
				this.amountField);
		
		this.add(builder.getPanel(), BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("OK")) {
					EditTimeValuePanel.this.actionOk();
				} else {
					EditTimeValuePanel.this.actionCancel();
				}
			}
			
		};
		
		this.okButton = new TUOkButton(listener);
		this.cancelButton = new TUCancelButton(listener);
		
		JPanel panel = new TUButtonsPanel(this.okButton, this.cancelButton);
		
		this.add(panel, BorderLayout.SOUTH);
	}
	
	public void actionOk() {
		if (this.timeValue != null) {
			this.timeValue.setField((Integer) this.fieldField.getSelectedItem());
			this.timeValue.setAmount(Integer.parseInt(this.amountField.getValue().toString()));
		}
		
		this.actionSupport.fireActionPerformed(0, ACTION_OK);
	}
	
	public void actionCancel() {
		this.actionSupport.fireActionPerformed(0, ACTION_CANCEL);
	}
	
	public JButton getOkButton() {
		return this.okButton;
	}
	
	public JButton getCancelButton() {
		return this.cancelButton;
	}
	
	public TimeValue getTimeValue() {
		return this.timeValue;
	}
	
	public void setTimeValue(TimeValue timeValue) {
		this.timeValue = timeValue;
		
		if (this.timeValue == null) {
			this.fieldField.setSelectedItem(new Integer(Calendar.MINUTE));
			this.amountField.setValue("0");
		} else {
			this.fieldField.setSelectedItem(new Integer(
					this.timeValue.getField()));
			this.amountField.setValue("" + this.timeValue.getAmount());
		}
	}
	
	@Override
	public void addActionListener(ActionListener listener) {
		this.actionSupport.addActionListener(listener);
	}
	
	@Override
	public void removeActionListener(ActionListener listener) {
		this.actionSupport.removeActionListener(listener);
	}
	
}
