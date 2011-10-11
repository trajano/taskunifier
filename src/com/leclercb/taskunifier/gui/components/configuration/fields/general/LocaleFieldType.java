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
package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.commons.values.StringValueLocale;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public class LocaleFieldType extends ConfigurationFieldType.ComboBox {
	
	private boolean languageOnly;
	
	public LocaleFieldType(boolean languageOnly) {
		super(getAvailableLocales(), "general.locale");
		
		this.languageOnly = languageOnly;
		
		this.setRenderer(new DefaultListRenderer(StringValueLocale.INSTANCE));
	}
	
	@Override
	public Object getPropertyValue() {
		return Main.SETTINGS.getLocaleProperty("general.locale");
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setLocaleProperty(
				"general.locale",
				(Locale) this.getFieldValue());
		
		if (this.languageOnly) {
			Translations.setLocale(Main.SETTINGS.getLocaleProperty("general.locale"));
		}
	}
	
	private static Locale[] getAvailableLocales() {
		Locale[] locales = Translations.getAvailableLocales();
		Arrays.sort(locales, new Comparator<Locale>() {
			
			@Override
			public int compare(Locale l1, Locale l2) {
				return l1.getDisplayName().compareTo(l2.getDisplayName());
			}
			
		});
		
		return locales;
	}
	
}
