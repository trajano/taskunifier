package com.leclercb.taskunifier.gui.properties;

import com.leclercb.commons.api.properties.PropertiesCoder;
import com.leclercb.taskunifier.gui.main.frames.GlobalHotKey;

public class GlobalHotKeyCoder extends PropertiesCoder<GlobalHotKey> {
	
	@Override
	public GlobalHotKey decode(String value) throws Exception {
		if (value == null || value.length() == 0)
			return null;
		
		String[] split = value.split(";");
		
		if (split.length != 2)
			return null;
		
		try {
			int character = Integer.parseInt(split[0]);
			int modifiers = Integer.parseInt(split[1]);
			
			return new GlobalHotKey(character, modifiers);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public String encode(GlobalHotKey value) throws Exception {
		if (value == null)
			return null;
		
		return value.getKeyCode() + ";" + value.getModifiers();
	}
	
}
