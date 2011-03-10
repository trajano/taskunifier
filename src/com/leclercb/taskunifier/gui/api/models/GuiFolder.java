package com.leclercb.taskunifier.gui.api.models;

import java.awt.Color;

import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.ModelId;

public class GuiFolder extends Folder implements GuiModel {
	
	private Color color;
	
	public GuiFolder(ModelId modelId, String title) {
		super(modelId, title);
	}
	
	public GuiFolder(String title) {
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
