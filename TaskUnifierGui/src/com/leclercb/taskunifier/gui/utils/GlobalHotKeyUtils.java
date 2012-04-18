package com.leclercb.taskunifier.gui.utils;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.Main;

public final class GlobalHotKeyUtils {
	
	private GlobalHotKeyUtils() {
		
	}
	
	public static GlobalHotKey getGlobalHotKey(String propertyName) {
		String value = Main.getSettings().getStringProperty(propertyName);
		
		if (value == null || value.length() == 0)
			return null;
		
		String[] split = value.split(";");
		
		if (split.length < 2)
			return null;
		
		int character = 0;
		int[] modifiers = new int[split.length];
		
		try {
			for (int i = 0; i < split.length; i++) {
				int v = Integer.parseInt(split[i]);
				
				if (i == 0)
					character = v;
				else
					modifiers[i - 1] = v;
			}
		} catch (NumberFormatException e) {
			return null;
		}
		
		return new GlobalHotKey(character, modifiers);
	}
	
	public static void setGlobalHotKey(
			String propertyName,
			GlobalHotKey globalHotKey) {
		String value = globalHotKey.getCharacter() + "";
		
		for (int modifier : globalHotKey.getModifiers())
			value += ";" + modifier;
		
		Main.getSettings().setStringProperty(propertyName, value);
	}
	
	public static class GlobalHotKey {
		
		private int character;
		private int[] modifiers;
		
		public GlobalHotKey(int character, int... modifiers) {
			CheckUtils.isNotNull(modifiers);
			
			if (modifiers.length <= 0)
				throw new IllegalArgumentException(
						"At least one modifier must be provided");
			
			this.character = character;
			this.modifiers = modifiers;
		}
		
		public int getCharacter() {
			return this.character;
		}
		
		public int[] getModifiers() {
			return this.modifiers;
		}
		
		public int getModifierSum() {
			int sum = 0;
			
			for (int modifier : this.modifiers) {
				sum += modifier;
			}
			
			return sum;
		}
		
	}
	
}
