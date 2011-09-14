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
		
		final Pattern pattern = Pattern.compile("([0-9]+):([0-5]?[0-9])");
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
