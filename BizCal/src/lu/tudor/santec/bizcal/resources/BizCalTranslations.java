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
package lu.tudor.santec.bizcal.resources;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public final class BizCalTranslations {
	
	private BizCalTranslations() {
		
	}
	
	private static final String bundleName = "BizCalTranslations";
	private static final String packageName = BizCalTranslations.class.getName();
	private static final String defaultBundle = bundleName + ".properties";
	
	private static ResourceBundle defaultMessages;
	private static ResourceBundle messages;
	
	static {
		try {
			defaultMessages = new PropertyResourceBundle(
					BizCalTranslations.class.getResourceAsStream(defaultBundle));
		} catch (Exception e) {
			e.printStackTrace();
			defaultMessages = null;
		}
	}
	
	public static Locale getLocale() {
		return Locale.getDefault();
	}
	
	public static void setLocale(Locale locale) {
		try {
			messages = ResourceBundle.getBundle(
					packageName,
					Locale.getDefault());
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
