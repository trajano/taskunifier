package com.leclercb.taskunifier.gui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class CurvedBorder extends AbstractBorder {

	private Color wallColor = Color.gray;
	private int sinkLevel = 10;

	public CurvedBorder() {

	}

	public CurvedBorder(int sinkLevel) {
		this.sinkLevel = sinkLevel;
	}

	public CurvedBorder(Color wallColor) {
		this.wallColor = wallColor;
	}

	public CurvedBorder(int sinkLevel, Color wallColor) {
		this.sinkLevel = sinkLevel;
		this.wallColor = wallColor;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		g.setColor(this.getWallColor());

		for (int i = 0; i < this.sinkLevel; i++) {
			g.drawRoundRect(x + i, y + i, w - i - 1, h - i - 1, this.sinkLevel - i, this.sinkLevel);
			g.drawRoundRect(x + i, y + i, w - i - 1, h - i - 1, this.sinkLevel, this.sinkLevel - i);
			g.drawRoundRect(x + i, y, w - i - 1, h - 1, this.sinkLevel - i, this.sinkLevel);
			g.drawRoundRect(x, y + i, w - 1, h - i - 1, this.sinkLevel, this.sinkLevel - i);
		}
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(this.sinkLevel, this.sinkLevel, this.sinkLevel, this.sinkLevel);
	}

	@Override
	public Insets getBorderInsets(Component c, Insets i) {
		i.left = i.right = i.bottom = i.top = this.sinkLevel;
		return i;
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

	public int getSinkLevel() {
		return this.sinkLevel;
	}

	public Color getWallColor() {
		return this.wallColor;
	}

}
