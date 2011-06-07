package com.leclercb.taskunifier.gui.utils;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public final class SettingsUtils {
	
	private SettingsUtils() {

	}
	
	public static void resetImportanceColors() {
		Main.SETTINGS.setStringProperty("theme.color.importance.0", "-1");
		Main.SETTINGS.setStringProperty("theme.color.importance.1", "-1");
		Main.SETTINGS.setStringProperty("theme.color.importance.2", "-1");
		Main.SETTINGS.setStringProperty("theme.color.importance.3", "-6684673");
		Main.SETTINGS.setStringProperty("theme.color.importance.4", "-3342337");
		Main.SETTINGS.setStringProperty("theme.color.importance.5", "-6684724");
		Main.SETTINGS.setStringProperty("theme.color.importance.6", "-3342388");
		Main.SETTINGS.setStringProperty("theme.color.importance.7", "-3342439");
		Main.SETTINGS.setStringProperty("theme.color.importance.8", "-52");
		Main.SETTINGS.setStringProperty("theme.color.importance.9", "-103");
		Main.SETTINGS.setStringProperty("theme.color.importance.10", "-13159");
		Main.SETTINGS.setStringProperty("theme.color.importance.11", "-13108");
		Main.SETTINGS.setStringProperty("theme.color.importance.12", "-26215");
	}
	
	public static void resetPriorityColors() {
		Main.SETTINGS.setStringProperty(
				"theme.color.priority.negative",
				"-8355712");
		Main.SETTINGS.setStringProperty("theme.color.priority.low", "-256");
		Main.SETTINGS.setStringProperty(
				"theme.color.priority.medium",
				"-16711936");
		Main.SETTINGS.setStringProperty("theme.color.priority.high", "-14336");
		Main.SETTINGS.setStringProperty("theme.color.priority.top", "-65536");
	}
	
}
