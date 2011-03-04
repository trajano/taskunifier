package com.leclercb.taskunifier.gui.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class ColorBadgeIcon implements Icon {
	
	private Color color;
	private int width;
	private int height;
	
	public ColorBadgeIcon(Color color, int width, int height) {
		this.color = (color == null ? Color.GRAY : color);
		this.width = width;
		this.height = height;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	@Override
	public int getIconHeight() {
		return this.height;
	}
	
	@Override
	public int getIconWidth() {
		return this.width;
	}
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(this.color);
		g2.fillOval(x, y, this.width, this.height);
	}
	
}
