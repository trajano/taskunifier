/*******************************************************************************
 * Bizcal is a component library for calendar widgets written in java using swing.
 * Copyright (C) 2007  Frederik Bertilsson 
 * Contributors:       Martin Heinemann martin.heinemann(at)tudor.lu
 * 
 * http://sourceforge.net/projects/bizcal/
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 * 
 *******************************************************************************/
package bizcal.swing.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Creates an area which fade between two given colors.
 * 
 * Example use:
 * GradientArea gradientArea = new GradientArea(GradientArea.TOP_BOTTOM, white,
 * grey);
 * gradientArea.setOpaque(true);
 * gradientArea.setBounds(0, 0, x, y);
 * panel.add(gradientArea);
 * 
 * @author Jonas Karlsson
 */
public class GradientArea extends JComponent {
	
	private static final long serialVersionUID = 1L;
	
	public final static String TOP_BOTTOM = "TOP_BOTTOM";
	public final static String BOTTOM_TOP = "BOTTOM_TOP";
	public final static String LEFT_RIGHT = "LEFT_RIGHT";
	public final static String RIGHT_LEFT = "RIGHT_LEFT";
	public final static String TOPLEFT_BOTTOMRIGHT = "TOPLEFT_BOTTOMRIGHT";
	public final static String IN_OUT = "IN_OUT";
	public final static String BORDER_RECTANGLE = "BORDER_RECTANGLE";
	public final static String BORDER_TOP = "BORDER_TOP";
	
	private String itsFadeDirection;
	private double itsGradientLength;
	private Color itsStartColor;
	private Color itsEndColor;
	private float itsBorderWidth;
	private Color itsBorderColor;
	private boolean itsBorder;
	private String itsBorderType;
	// private String text;
	// private JComponent component;
	private JLabel _label;
	
	/**
	 * Constructs a default GradientArea which fades in top-to-bottom
	 * direction from white to a bright grey tone
	 * 
	 */
	public GradientArea() {
		this(GradientArea.TOP_BOTTOM, new Color(255, 255, 255), new Color(
				245,
				245,
				245));
	}
	
	public GradientArea(
			String aFadeDirection,
			Color aStartColor,
			Color aEndColor) {
		this.itsFadeDirection = aFadeDirection;
		this.itsStartColor = aStartColor;
		this.itsEndColor = aEndColor;
		this.itsBorderWidth = 4.0f;
		this.itsBorderColor = this.itsEndColor;
		this.itsGradientLength = 1;
		this.itsBorder = true;
		this.itsBorderType = GradientArea.BORDER_RECTANGLE;
	}
	
	/**
	 * Sets position of the break point of the two colors
	 * 
	 * @param length
	 */
	public void setGradientLength(double length) {
		this.itsGradientLength = length;
	}
	
	/**
	 * Sets width of this component border
	 * 
	 * @param aWidth
	 */
	public void setBorderWidth(float aWidth) {
		this.itsBorderWidth = aWidth;
	}
	
	/**
	 * Sets color of the border of this component
	 * 
	 * @param aColor
	 */
	public void setBorderColor(Color aColor) {
		this.itsBorderColor = aColor;
	}
	
	public void setBorder(boolean aBorder) {
		this.itsBorder = aBorder;
	}
	
	public void setColors(Color startColor, Color endColor) {
		this.itsStartColor = startColor;
		this.itsEndColor = endColor;
	}
	
	public void setBorderType(String aType) {
		this.itsBorderType = aType;
	}
	
	/**
	 * The toolkit will invoke this method when it's time to paint
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		GradientPaint gradient;
		if (this.itsFadeDirection.equals(GradientArea.TOP_BOTTOM)) {
			gradient = new GradientPaint(
					0,
					0,
					this.itsStartColor,
					0,
					(int) (height * this.itsGradientLength),
					this.itsEndColor);
			g2.setPaint(gradient);
		}
		if (this.itsFadeDirection.equals(GradientArea.BOTTOM_TOP)) {
			gradient = new GradientPaint(
					0,
					0,
					this.itsEndColor,
					0,
					(int) (height * this.itsGradientLength),
					this.itsStartColor);
			g2.setPaint(gradient);
		}
		if (this.itsFadeDirection.equals(GradientArea.LEFT_RIGHT)) {
			gradient = new GradientPaint(
					0,
					0,
					this.itsStartColor,
					(int) (width * this.itsGradientLength),
					0,
					this.itsEndColor);
			g2.setPaint(gradient);
		}
		if (this.itsFadeDirection.equals(GradientArea.RIGHT_LEFT)) {
			gradient = new GradientPaint(
					(int) (width * this.itsGradientLength),
					0,
					this.itsStartColor,
					0,
					0,
					this.itsEndColor);
			g2.setPaint(gradient);
		}
		if (this.itsFadeDirection.equals(GradientArea.TOPLEFT_BOTTOMRIGHT)) {
			gradient = new GradientPaint(
					0,
					0,
					this.itsStartColor,
					(int) (width * this.itsGradientLength),
					(int) (height * this.itsGradientLength),
					this.itsEndColor);
			g2.setPaint(gradient);
		}
		if (this.itsFadeDirection.equals(GradientArea.IN_OUT)) {
			gradient = new GradientPaint(
					0,
					10,
					Color.RED,
					(int) (width * this.itsGradientLength),
					0,
					this.itsEndColor);
			g2.setPaint(gradient);
			gradient = new GradientPaint(
					0,
					0,
					Color.BLUE,
					(int) (width * this.itsGradientLength),
					0,
					this.itsEndColor);
			g2.setPaint(gradient);
		}
		
		g2.fill(new Rectangle2D.Double(0, 0, width, height));
		
		if (this.itsBorder) {
			g2.setStroke(new BasicStroke(this.itsBorderWidth));
			g2.setPaint(this.itsBorderColor);
			if (this.itsBorderType.equals(GradientArea.BORDER_RECTANGLE))
				g2.draw(new Rectangle2D.Double(0, 0, width - 1, height - 1));
			if (this.itsBorderType.equals(GradientArea.BORDER_TOP))
				g2.draw(new Line2D.Double(new Point(0, 0), new Point(width, 0)));
		}
		
		super.paintComponent(g2);
	}
	
	public void setText(String text, boolean center) {
		this.initLabel();
		this._label.setText(text);
		if (center)
			this._label.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	private void initLabel() {
		if (this._label == null) {
			this._label = new JLabel();
			this.addComponentListener(new ThisComponentListner());
			this.add(this._label);
		}
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		this.initLabel();
		this._label.setFont(font);
	}
	
	@Override
	public void setForeground(Color color) {
		this.initLabel();
		this._label.setForeground(color);
		super.setForeground(color);
	}
	
	public void setHorizontalAlignment(int align) {
		this._label.setHorizontalAlignment(align);
	}
	
	private class ThisComponentListner extends ComponentAdapter {
		
		@Override
		public void componentResized(final ComponentEvent e) {
			// SwingUtilities.invokeLater(new Runnable() {
			// public void run() {
			GradientArea.this._label.setBounds(
					10,
					0,
					e.getComponent().getWidth(),
					e.getComponent().getHeight());
			// }
			// });
		}
	}
	
	public ComponentListener getComponentListener() {
		return new ThisComponentListner();
	}
	
}
