package com.leclercb.taskunifier.gui.plugins;

import java.awt.Component;
import java.util.Locale;

import com.leclercb.commons.api.properties.PropertyMap;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class PluginApi {
	
	public static Component getMainFrame() {
		return MainFrame.getInstance().getFrame();
	}
	
	public static PropertyMap getSettings() {
		return Main.SETTINGS;
	}
	
	public static void initializeProxy() {
		SynchronizerUtils.initializeProxy();
	}
	
	public static Locale getLocale() {
		return Translations.getLocale();
	}
	
	public static String getTranslation(String key) {
		return Translations.getString(key);
	}
	
	public static String getTranslation(String key, Object... args) {
		return Translations.getString(key, args);
	}
	
}
