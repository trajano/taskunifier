package com.leclercb.taskunifier.gui.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.leclercb.commons.api.utils.CheckUtils;

public class Growl {
	
	private static final String GROWL_APPLICATION = "com.Growl.GrowlHelperApp";
	
	private final String applicationName;
	private String[] allNotificationsList;
	private String[] enabledNotificationsList;
	private ScriptEngine appleScriptEngine;
	
	public Growl(
			String applicationName,
			String[] allNotificationsList,
			String[] enabledNotificationsList) {
		CheckUtils.isNotNull(applicationName, "Application name cannot be null");
		CheckUtils.isNotNull(
				allNotificationsList,
				"All notifications list cannot be null");
		CheckUtils.isNotNull(
				enabledNotificationsList,
				"Enable notifications list cannot be null");
		
		this.applicationName = applicationName;
		this.allNotificationsList = allNotificationsList;
		this.enabledNotificationsList = enabledNotificationsList;
		
		this.init();
	}
	
	private void init() {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		this.appleScriptEngine = scriptEngineManager.getEngineByName("AppleScript");
		
		if (this.appleScriptEngine == null) {
			throw new RuntimeException("No AppleScriptEngine available");
		}
		
		if (!this.isGrowlEnabled()) {
			throw new RuntimeException("No Growl process was found");
		}
	}
	
	private boolean isGrowlEnabled() {
		String script = this.script().add("tell application ").quote(
				"System Events").nextRow(
				"return count of (every process whose bundle identifier is ").quote(
				GROWL_APPLICATION).add(") > 0").nextRow("end tell").get();
		long count = this.executeScript(script, 0L);
		return count > 0;
	}
	
	public void registerApplication() throws ScriptException {
		String script = this.script().add("tell application id ").quote(
				GROWL_APPLICATION).nextRow("set the allNotificationsList to ").array(
				this.allNotificationsList).nextRow(
				"set the enabledNotificationsList to ").array(
				this.enabledNotificationsList).nextRow(
				"register as application ").quote(this.applicationName).add(
				" all notifications allNotificationsList default notifications enabledNotificationsList").nextRow(
				"end tell").get();
		this.executeScript(script);
	}
	
	public void notify(String notificationList, String title)
			throws ScriptException {
		this.notify(notificationList, title, "");
	}
	
	public void notify(String notificationList, String title, String description)
			throws ScriptException {
		if (description == null)
			description = "";
		
		String script = this.script().add("tell application id ").quote(
				GROWL_APPLICATION).nextRow("notify with name ").quote(
				notificationList).add(" title ").quote(title).add(
				" description ").quote(description).add(" application name ").quote(
				this.applicationName).nextRow("end tell").get();
		this.executeScript(script);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T executeScript(String script, T defaultValue) {
		try {
			return (T) this.appleScriptEngine.eval(
					script,
					this.appleScriptEngine.getContext());
		} catch (ScriptException e) {
			return defaultValue;
		}
	}
	
	private void executeScript(String script) throws ScriptException {
		this.appleScriptEngine.eval(script, this.appleScriptEngine.getContext());
	}
	
	private ScriptBuilder script() {
		return new ScriptBuilder();
	}
	
	private class ScriptBuilder {
		
		StringBuilder builder = new StringBuilder();
		
		public ScriptBuilder add(String text) {
			this.builder.append(text);
			return this;
		}
		
		public ScriptBuilder quote(String text) {
			this.builder.append("\"");
			this.builder.append(text);
			this.builder.append("\"");
			return this;
		}
		
		public ScriptBuilder nextRow(String text) {
			this.builder.append("\n");
			this.builder.append(text);
			return this;
		}
		
		public String get() {
			return this.builder.toString();
		}
		
		public ScriptBuilder array(String[] array) {
			this.builder.append("{");
			
			for (int i = 0; i < array.length; i++) {
				if (i > 0) {
					this.builder.append(", ");
				}
				
				this.builder.append("\"");
				this.builder.append(array[i]);
				this.builder.append("\"");
			}
			
			this.builder.append("}");
			return this;
		}
		
	}
	
}
