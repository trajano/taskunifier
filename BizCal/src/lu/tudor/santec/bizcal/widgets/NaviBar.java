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

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * creates a Navigationbar with PatientButtonPanels
 * 
 * @author Johannes Hermen johannes.hermen(at)tudor.lu
 * 
 */
public class NaviBar extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// inner constraints
	public static final int TOP = 1;
	public static final int BOTTOM = 2;
	public static final int FILL = 3;
	
	private CellConstraints cc;
	private FormLayout layout;
	private int pos = 2;
	
	public NaviBar() {
		this.setOpaque(true);
		this.setBackground(new Color(-2695707));
		this.layout = new FormLayout(
				"1dlu, fill:default:grow, 1dlu",
				"1dlu, pref:grow");
		this.setLayout(this.layout);
		this.cc = new CellConstraints();
		
	}
	
	public NaviBar(int width) {
		this.setOpaque(true);
		this.setBackground(new Color(-2695707));
		this.layout = new FormLayout(
				"1dlu, " + width + "dlu, 1dlu",
				"1dlu, pref");
		
		this.setLayout(this.layout);
		this.cc = new CellConstraints();
		
	}
	
	public void addButtonPanel(JComponent buttonPanel, int alignment) {
		/* ================================================== */
		if (alignment == TOP) {
			/* ------------------------------------------------------- */
			this.layout.insertRow(this.pos, new RowSpec("pref"));
			this.add(buttonPanel, this.cc.xy(2, this.pos));
			this.pos++;
			this.layout.insertRow(this.pos, new RowSpec("3dlu"));
			this.pos++;
			/* ------------------------------------------------------- */
		} else if (FILL == alignment) {
			/* ------------------------------------------------------- */
			// this.layout.insertRow(pos, new RowSpec("fill:pref:grow"));
			// this.add(new JLabel("f "+pos), cc.xy(2, pos));
			// pos++;
			this.layout.insertRow(this.pos, new RowSpec("fill:pref:grow"));
			this.add(buttonPanel, this.cc.xy(2, this.pos));
			this.pos++;
			this.layout.insertRow(this.pos, new RowSpec("3dlu"));
			this.pos++;
			/* ------------------------------------------------------- */
		} else {
			this.layout.appendRow(new RowSpec("pref"));
			this.add(buttonPanel, this.cc.xy(2, this.layout.getRowCount()));
			this.layout.appendRow(new RowSpec("3dlu"));
		}
		/* ================================================== */
	}
	
}
