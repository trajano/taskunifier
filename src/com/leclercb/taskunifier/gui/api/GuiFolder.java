package com.leclercb.taskunifier.gui.api;

import java.awt.Color;

import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.ModelId;

public class GuiFolder extends Folder {
	
	public static final String PROP_COLOR = "color";
	
	private Color color;
	
	public GuiFolder(ModelId modelId, String title) {
		super(modelId, title);
	}
	
	public GuiFolder(String title) {
		super(title);
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.checkBeforeSet();
		Color oldColor = this.color;
		this.color = color;
		this.updateProperty(PROP_COLOR, oldColor, color);
	}
	
}
