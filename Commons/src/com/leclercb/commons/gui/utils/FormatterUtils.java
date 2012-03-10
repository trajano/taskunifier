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
package com.leclercb.commons.gui.utils;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.DefaultFormatter;

public final class FormatterUtils {
	
	private FormatterUtils() {
		
	}
	
	public static AbstractFormatter getIntegerFormatter() {
		AbstractFormatter formatter = new AbstractFormatter() {
			
			@Override
			public String valueToString(Object value) throws ParseException {
				if (value == null) {
					return "";
				} else {
					return value + "";
				}
			}
			
			@Override
			public Object stringToValue(String text) throws ParseException {
				if ("".equals(text)) {
					return null;
				}
				
				try {
					return Integer.parseInt(text);
				} catch (Exception exc) {
					throw new ParseException(exc.getMessage(), 0);
				}
			}
			
		};
		
		return formatter;
	}
	
	public static DefaultFormatter getRegexFormatter(String regex) {
		final Pattern pattern = Pattern.compile(regex);
		
		DefaultFormatter formatter = new DefaultFormatter() {
			
			@Override
			public Object stringToValue(String text) throws ParseException {
				Matcher matcher = pattern.matcher(text);
				
				if (matcher.matches())
					return text;
				
				throw new ParseException("Pattern did not match", 0);
			}
			
		};
		
		return formatter;
	}
	
}
