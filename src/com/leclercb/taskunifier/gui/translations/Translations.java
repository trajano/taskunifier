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

	public static void initialize(Locale locale) {
		messages = ResourceBundle.getBundle(Translations.class.getName(), locale);
	}

	public static Locale getDefaultTranslatedLocale() {
		return new Locale("en");
	}

	public static List<Locale> getTranslatedLocales() {
		List<Locale> locales = new ArrayList<Locale>();

		locales.add(new Locale("en", "US"));
		locales.add(new Locale("fr", "FR"));

		return locales;
	}

	public static String getString(String key) {
		if (messages == null)
			throw new RuntimeException("Translations must be initialized");

		if (!messages.containsKey(key))
			return "#" + key + "#";

		return messages.getString(key);
	}

}
