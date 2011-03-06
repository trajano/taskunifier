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
package com.leclercb.taskunifier.gui.translations;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Translations {
	
	private Translations() {

	}
	
	private static ResourceBundle messages;
	
	static {
		setLocale(getDefaultLocale());
	}
	
	public static Locale getLocale() {
		return Locale.getDefault();
	}
	
	public static void setLocale(Locale locale) {
		Locale.setDefault(locale);
		
		messages = ResourceBundle.getBundle(
				Translations.class.getName(),
				locale);
	}
	
	public static Locale getDefaultLocale() {
		String language = Locale.getDefault().getLanguage();
		
		for (Locale locale : getLocales()) {
			if (locale.getLanguage().equals(language))
				return locale;
		}
		
		return new Locale("en", "US");
	}
	
	public static List<Locale> getLocales() {
		List<Locale> locales = new ArrayList<Locale>();
		
		locales.add(new Locale("de", "DE"));
		locales.add(new Locale("en", "US"));
		locales.add(new Locale("fr", "FR"));
		locales.add(new Locale("zh", "CN"));
		locales.add(new Locale("zh", "TW"));
		
		return locales;
	}
	
	public static String getString(String key) {
		if (!messages.containsKey(key))
			return "#" + key + "#";
		
		return messages.getString(key);
	}
	
	public static String getString(String key, Object... args) {
		String value = getString(key);
		return String.format(value, args);
	}
	
}
