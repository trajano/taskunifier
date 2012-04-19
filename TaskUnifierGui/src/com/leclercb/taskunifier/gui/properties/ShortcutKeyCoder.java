package com.leclercb.taskunifier.gui.properties;

import com.leclercb.commons.api.properties.PropertiesCoder;
import com.leclercb.taskunifier.gui.main.frames.ShortcutKey;

public class ShortcutKeyCoder extends PropertiesCoder<ShortcutKey> {
	
	@Override
	public ShortcutKey decode(String value) throws Exception {
		if (value == null || value.length() == 0)
			return null;
		
		String[] split = value.split(";");
		
		if (split.length != 2)
			return null;
		
		try {
			int character = Integer.parseInt(split[0]);
			int modifiers = Integer.parseInt(split[1]);
			
			return new ShortcutKey(character, modifiers);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public String encode(ShortcutKey value) throws Exception {
		if (value == null)
			return null;
		
		return value.getKeyCode() + ";" + value.getModifiers();
	}
	
}
