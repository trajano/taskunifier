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

import com.leclercb.commons.api.utils.ResourceBundleUtils;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public final class Translations {
	
	private Translations() {

	}
	
	private static final String bundleFolder = Main.RESOURCES_FOLDER
			+ File.separator
			+ "translations";
	
	private static final String bundleName = "Translations";
	
	private static final String defaultBundle = bundleFolder
			+ File.separator
			+ bundleName
			+ ".properties";
	
	private static Map<Locale, File> locales;
	private static ResourceBundle defaultMessages;
	private static ResourceBundle messages;
	
	static {
		try {
			locales = ResourceBundleUtils.getAvailableLocales(new File(
					bundleFolder), bundleName);
		} catch (Exception e) {
			e.printStackTrace();
			locales = new HashMap<Locale, File>();
		}
		
		try {
			defaultMessages = new PropertyResourceBundle(new FileInputStream(
					defaultBundle));
		} catch (Exception e) {
			e.printStackTrace();
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
		Locale.setDefault(locale);
		
		File file = locales.get(locale);
		
		if (file == null) {
			messages = null;
			return;
		}
		
		try {
			messages = new PropertyResourceBundle(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
			messages = null;
		}
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
