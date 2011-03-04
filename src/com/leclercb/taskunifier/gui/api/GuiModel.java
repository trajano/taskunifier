package com.leclercb.taskunifier.gui.api;

import java.awt.Color;

import com.leclercb.taskunifier.api.models.Model;

public interface GuiModel extends Model {
	
	public static final String PROP_COLOR = "color";
	
	public abstract Color getColor();
	
	public abstract void setColor(Color color);
	
}
