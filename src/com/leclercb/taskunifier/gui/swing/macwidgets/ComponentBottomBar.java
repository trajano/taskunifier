package com.leclercb.taskunifier.gui.swing.macwidgets;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ComponentBottomBar extends TriAreaComponent {
	
	ComponentBottomBar() {
		super();
		this.setBackgroundPainter(MacButtonFactory.GRADIENT_BUTTON_IMAGE_PAINTER);
		this.getComponent().setBorder(
				BorderFactory.createMatteBorder(
						1,
						0,
						0,
						0,
						MacButtonFactory.GRADIENT_BUTTON_BORDER_COLOR));
	}
	
	public void addComponentToLeftWithBorder(JComponent toolToAdd) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createMatteBorder(
				0,
				0,
				0,
				1,
				MacButtonFactory.GRADIENT_BUTTON_BORDER_COLOR));
		panel.add(toolToAdd, BorderLayout.CENTER);
		super.addComponentToLeft(panel);
	}
	
	@SuppressWarnings("unused")
	public void addComponentToCenterWithBorder(JComponent toolToAdd) {
		Border matteBorder = this.getCenterComponentCount() == 0 ? BorderFactory.createMatteBorder(
				0,
				1,
				0,
				1,
				MacButtonFactory.GRADIENT_BUTTON_BORDER_COLOR) : BorderFactory.createMatteBorder(
				0,
				0,
				0,
				1,
				MacButtonFactory.GRADIENT_BUTTON_BORDER_COLOR);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createMatteBorder(
				0,
				0,
				0,
				1,
				MacButtonFactory.GRADIENT_BUTTON_BORDER_COLOR));
		panel.add(toolToAdd, BorderLayout.CENTER);
		super.addComponentToCenter(panel);
	}
	
	public void addComponentToRightWithBorder(JComponent toolToAdd) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createMatteBorder(
				0,
				0,
				0,
				1,
				MacButtonFactory.GRADIENT_BUTTON_BORDER_COLOR));
		panel.add(toolToAdd, BorderLayout.CENTER);
		super.addComponentToRight(panel);
	}
	
}
