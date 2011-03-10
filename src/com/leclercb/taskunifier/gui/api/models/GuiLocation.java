package com.leclercb.taskunifier.gui.api.models;

import java.awt.Color;

import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.ModelId;

public class GuiLocation extends Location implements GuiModel {
	
	private Color color;
	
	public GuiLocation(ModelId modelId, String title) {
		super(modelId, title);
	}
	
	public GuiLocation(String title) {
		super(title);
	}
	
	@Override
	public Color getColor() {
		return this.color;
	}
	
	@Override
	public void setColor(Color color) {
		this.checkBeforeSet();
		Color oldColor = this.color;
		this.color = color;
		this.updateProperty(PROP_COLOR, oldColor, color);
	}
	
}
