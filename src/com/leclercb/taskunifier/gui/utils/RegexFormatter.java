/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.utils;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.text.DefaultFormatter;

import com.leclercb.taskunifier.api.utils.CheckUtils;

public class RegexFormatter extends DefaultFormatter {
	
	private Pattern pattern;
	
	public RegexFormatter(String regex) throws PatternSyntaxException {
		this(Pattern.compile(regex));
	}
	
	public RegexFormatter(Pattern pattern) {
		CheckUtils.isNotNull(pattern, "Pattern cannot be null");
		this.pattern = pattern;
	}
	
	@Override
	public Object stringToValue(String text) throws ParseException {
		Matcher matcher = this.pattern.matcher(text);
		
		if (matcher.matches())
			return text;
		
		throw new ParseException("Pattern did not match", 0);
	}
	
}
