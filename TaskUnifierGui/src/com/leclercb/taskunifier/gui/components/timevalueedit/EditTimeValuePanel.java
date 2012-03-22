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
