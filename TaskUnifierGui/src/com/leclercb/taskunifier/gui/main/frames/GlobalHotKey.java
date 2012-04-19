package com.leclercb.taskunifier.gui.main.frames;

import com.leclercb.commons.api.utils.CheckUtils;

public class GlobalHotKey {
	
	private int keyCode;
	private int modifiers;
	
	public GlobalHotKey(int keyCode, int modifiers) {
		CheckUtils.isNotNull(modifiers);
		
		if (modifiers == 0)
			throw new IllegalArgumentException(
					"At least one modifier must be provided");
		
		this.keyCode = keyCode;
		this.modifiers = modifiers;
	}
	
	public int getKeyCode() {
		return this.keyCode;
	}
	
	public int getModifiers() {
		return this.modifiers;
	}
	
}
