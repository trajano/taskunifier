package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import java.util.Locale;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.commons.values.StringValueLocale;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public class LocaleFieldType extends ConfigurationFieldTypeExt.ComboBox {
	
	private boolean languageOnly;
	
	public LocaleFieldType(boolean languageOnly) {
		super(
				Translations.getLocales().toArray(),
				Main.SETTINGS,
				"general.locale");
		
		this.languageOnly = languageOnly;
		
		this.setRenderer(new DefaultListRenderer(new StringValueLocale()));
	}
	
	@Override
	public Object getPropertyValue() {
		if (Main.SETTINGS.getLocaleProperty("general.locale") != null)
			return Main.SETTINGS.getLocaleProperty("general.locale");
		else
			return Translations.getDefaultLocale();
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
	
}
