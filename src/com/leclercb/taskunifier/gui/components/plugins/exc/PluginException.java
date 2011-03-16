package com.leclercb.taskunifier.gui.components.plugins.exc;

import com.leclercb.commons.api.utils.CheckUtils;

public class PluginException extends Exception {
	
	public static enum PluginExceptionType {
		
		ERROR_LOADING_PLUGIN,
		NO_VALID_PLUGIN,
		MORE_THAN_ONE_PLUGIN,
		PLUGIN_FOUND;
		
	}
	
	private PluginExceptionType type;
	
	public PluginException(PluginExceptionType type, String message) {
		super(message);
		
		CheckUtils.isNotNull(type, "Type cannot be null");
		
		this.type = type;
	}
	
	public PluginExceptionType getType() {
		return this.type;
	}
	
}
