/*******************************************************************************
 * Copyright (c) 2007 by CRP Henri TUDOR - SANTEC LUXEMBOURG 
 * check http://www.santec.tudor.lu for more information
 *  
 * Contributor(s):
 * Johannes Hermen  johannes.hermen(at)tudor.lu                            
 * Martin Heinemann martin.heinemann(at)tudor.lu  
 *  
 * This library is free software; you can redistribute it and/or modify it  
 * under the terms of the GNU Lesser General Public License (version 2.1)
 * as published by the Free Software Foundation.
 * 
 * This software is distributed in the hope that it will be useful, but     
 * WITHOUT ANY WARRANTY; without even the implied warranty of               
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU        
 * Lesser General Public License for more details.                          
 * 
 * You should have received a copy of the GNU Lesser General Public         
 * License along with this library; if not, write to the Free Software      
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *******************************************************************************/
package lu.tudor.santec.bizcal.widgets;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;

import com.explodingpixels.macwidgets.SourceListStandardColorScheme;

public class BubbleLabel extends JLabel {
	
	private static final long serialVersionUID = 1L;
	
	private static final Font categoryFont = UIManager.getFont("Label.font").deriveFont(
			Font.BOLD);
	
	private static final SourceListStandardColorScheme colorScheme = new SourceListStandardColorScheme();
	
	public BubbleLabel(Icon image) {
		super(image);
		this.setOpaque(false);
		this.setForeground(colorScheme.getSelectedItemTextColor());
		this.setFont(categoryFont);
	}
	
	public BubbleLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		this.setOpaque(false);
		this.setForeground(colorScheme.getSelectedItemTextColor());
		this.setFont(categoryFont);
	}
	
	public BubbleLabel(String text) {
		super(text);
		this.setOpaque(false);
		this.setForeground(colorScheme.getSelectedItemTextColor());
		this.setFont(categoryFont);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		colorScheme.getActiveUnfocusedSelectedItemPainter().paint(
				(Graphics2D) g,
				this,
				this.getWidth(),
				this.getHeight());
		super.paintComponent(g);
	}
	
}
