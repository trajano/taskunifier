package com.leclercb.taskunifier.gui.api.models;

import java.awt.Color;

import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.enums.GoalLevel;

public class GuiGoal extends Goal implements GuiModel {
	
	private Color color;
	
	public GuiGoal(ModelId modelId, String title) {
		super(modelId, title);
	}
	
	public GuiGoal(String title) {
		super(title);
	}
	
	public GuiGoal(ModelId modelId, String title, GoalLevel level) {
		super(modelId, title, level);
	}
	
	public GuiGoal(String title, GoalLevel level) {
		super(title, level);
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
