package com.leclercb.taskunifier.gui.api.plugins;

import com.leclercb.taskunifier.gui.translations.Translations;

public enum PluginStatus {
	
	TO_INSTALL(Translations.getString("plugin.status.to_install")),
	INSTALLED(Translations.getString("plugin.status.installed")),
	TO_UPDATE(Translations.getString("plugin.status.to_update")),
	DELETED(Translations.getString("plugin.status.deleted"));
	
	private String label;
	
	private PluginStatus(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}

	@Override
	public String toString() {
		return this.label;
	}
	
}
