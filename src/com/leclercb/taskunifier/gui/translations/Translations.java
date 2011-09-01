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
package com.leclercb.taskunifier.gui.translations;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.leclercb.commons.api.utils.ResourceBundleUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.main.Main;

public final class Translations {
	
	private Translations() {
		
	}
	
	private static final String BUNDLE_FOLDER = Main.RESOURCES_FOLDER
			+ File.separator
			+ "translations";
	
	private static final String BUNDLE_NAME = "Translations";
	
	private static final String DEFAULT_BUNDLE = BUNDLE_FOLDER
			+ File.separator
			+ BUNDLE_NAME
			+ ".properties";
	
	public static final Locale DEFAULT_LOCALE = new Locale("en", "US");
	
	private static Map<Locale, File> locales;
	private static ResourceBundle defaultMessages;
	private static ResourceBundle messages;
	
	static {
		try {
			locales = ResourceBundleUtils.getAvailableLocales(new File(
					BUNDLE_FOLDER), BUNDLE_NAME);
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Cannot load available locales",
					e);
			
			locales = new HashMap<Locale, File>();
		}
		
		try {
			defaultMessages = new PropertyResourceBundle(new FileInputStream(
					DEFAULT_BUNDLE));
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Cannot load default locale",
					e);
			
			defaultMessages = null;
		}
	}
	
	public static Locale[] getAvailableLocales() {
		return locales.keySet().toArray(new Locale[0]);
	}
	
	public static Locale getLocale() {
		return Locale.getDefault();
	}
	
	public static void setLocale(Locale locale) {
		try {
			File file = locales.get(locale);
			
			if (file == null)
				throw new Exception();
			
			messages = new PropertyResourceBundle(new FileInputStream(file));
			Locale.setDefault(locale);
		} catch (Exception e) {
			GuiLogger.getLogger().log(Level.SEVERE, "Cannot load locale", e);
			messages = null;
			Locale.setDefault(DEFAULT_LOCALE);
		}
		
		Tips.setLocale(locale);
	}
	
	public static String getString(String key) {
		if (messages != null && messages.containsKey(key))
			return messages.getString(key);
		
		if (defaultMessages != null && defaultMessages.containsKey(key))
			return defaultMessages.getString(key);
		
		return "#" + key + "#";
	}
	
	public static String getString(String key, Object... args) {
		String value = getString(key);
		return String.format(value, args);
	}
	
}
