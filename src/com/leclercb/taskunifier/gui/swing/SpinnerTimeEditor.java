package com.leclercb.taskunifier.gui.swing;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

import com.leclercb.taskunifier.gui.commons.values.StringValueTaskLength;

public class SpinnerTimeEditor extends JSpinner.DefaultEditor {
	
	public SpinnerTimeEditor(JSpinner spinner) {
		super(spinner);
		
		if (!(spinner.getModel() instanceof SpinnerNumberModel)) {
			throw new IllegalArgumentException();
		}
		
		final Pattern pattern = Pattern.compile("([0-9]{1,2}):([0-5]?[0-9])");
		DefaultFormatter formatter = new DefaultFormatter() {
			
			@Override
			public String valueToString(Object value) throws ParseException {
				return StringValueTaskLength.INSTANCE.getString(value);
			}
			
			@Override
			public Object stringToValue(String text) throws ParseException {
				Matcher matcher = pattern.matcher(text);
				
				if (!matcher.find()) {
					throw new ParseException("Pattern did not match", 0);
				}
				
				int hour = Integer.parseInt(matcher.group(1));
				int minute = Integer.parseInt(matcher.group(2));
				
				return (hour * 60) + minute;
			}
			
		};
		
		DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
		
		JFormattedTextField ftf = this.getTextField();
		ftf.setEditable(true);
		ftf.setFormatterFactory(factory);
	}
	
}
