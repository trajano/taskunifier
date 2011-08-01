package com.leclercb.taskunifier.gui.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Growl {
	
	private static final String GROWL_APPLICATION = "GrowlHelperApp";
	
	private final String applicationName;
	private String[] availableNotifications;
	private String[] enabledNotifications;
	private ScriptEngine appleScriptEngine;
	
	public Growl(String applicationName) {
		this(
				applicationName,
				new String[] { "system" },
				new String[] { "system" });
	}
	
	public Growl(
			String applicationName,
			String[] availableNotifications,
			String[] enabledNotifications) {
		this.applicationName = applicationName;
		this.availableNotifications = availableNotifications;
		this.enabledNotifications = enabledNotifications;
	}
	
	public void init() {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		this.appleScriptEngine = scriptEngineManager.getEngineByName("AppleScript");
		
		if (this.appleScriptEngine == null) {
			throw new RuntimeException("No AppleScriptEngine available");
		}
		
		if (!this.isGrowlEnabled()) {
			throw new RuntimeException("No Growl process was found.");
		}
	}
	
	public void registerApplication() {
		String script = this.script().add("tell application ").quote(
				GROWL_APPLICATION).nextRow("set the availableList to ").array(
				this.availableNotifications).nextRow("set the enabledList to ").array(
				this.enabledNotifications).nextRow("register as application ").quote(
				this.applicationName).add(
				" all notifications availableList default notifications enabledList").nextRow(
				"end tell").get();
		this.executeScript(script);
	}
	
	public void notify(String title, String message) {
		this.notify("system", title, message);
	}
	
	public void notify(String notificationName, String title, String message) {
		String script = this.script().add("tell application ").quote(
				GROWL_APPLICATION).nextRow("notify with name ").quote(
				notificationName).add(" title ").quote(title).add(
				" description ").quote(message).add(" application name ").quote(
				this.applicationName).nextRow("end tell").get();
		this.executeScript(script);
	}
	
	private boolean isGrowlEnabled() {
		String script = this.script().add("tell application ").quote(
				"System Events").nextRow(
				"return count of (every process whose name is ").quote(
				GROWL_APPLICATION).add(") > 0").nextRow("end tell").get();
		long count = this.executeScript(script, 0L);
		return count > 0;
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
	
	private void executeScript(String script) {
		try {
			this.appleScriptEngine.eval(
					script,
					this.appleScriptEngine.getContext());
		} catch (ScriptException e) {
			// log.error("Problem executing script, e);
		}
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
