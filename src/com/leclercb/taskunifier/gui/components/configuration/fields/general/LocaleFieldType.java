package com.leclercb.taskunifier.gui.components.configuration.fields.general;

import java.util.Locale;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.commons.values.StringValueLocale;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class LocaleFieldType extends ConfigurationFieldTypeExt.ComboBox {
	
	private boolean languageOnly;
	
	public LocaleFieldType(boolean languageOnly) {
		super(
				Translations.getAvailableLocales(),
				Main.SETTINGS,
				"general.locale");
		
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
	
}
