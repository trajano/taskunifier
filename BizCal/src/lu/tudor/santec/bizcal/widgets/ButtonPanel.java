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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * creates a buttonpanel to show on a PatientNaviBar
 * 
 * @author Johannes Hermen johannes.hermen(at)tudor.lu
 * 
 */
public class ButtonPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JPanel content;
	private ButtonGroup buttonGroup = new ButtonGroup();
	private int columns;
	private HashMap<Action, AbstractButton> map = new HashMap<Action, AbstractButton>();
	
	/**
	 * creates a new buttonpanel
	 * 
	 * @param title
	 *            the title of the buttonpanel
	 * @param color
	 *            the background color of the title of the buttonpanel
	 * @param columns
	 *            the number of button columns
	 * @param buttons
	 *            the buttons to show on the buttonpanel
	 */
	public ButtonPanel(
			String title,
			Color color,
			int columns,
			Vector<AbstractButton> buttons) {
		this.createPanel(title, color, columns, buttons);
	}
	
	/**
	 * creates a new buttonpanel
	 * 
	 * @param title
	 *            the title of the buttonpanel
	 * @param color
	 *            the background color of the title of the buttonpanel
	 * @param columns
	 *            the number of button columns
	 * @param actions
	 *            the actions to show on the buttonpanel
	 * @param toggleActions
	 *            show actions as togglebuttons
	 */
	public ButtonPanel(
			String title,
			Color color,
			int columns,
			Vector<Action> actions,
			boolean toggleActions) {
		Vector<AbstractButton> buttons = new Vector<AbstractButton>();
		for (Iterator iter = actions.iterator(); iter.hasNext();) {
			Action action = (Action) iter.next();
			if (toggleActions) {
				buttons.add(new JToggleButton(action));
			} else {
				buttons.add(new JButton(action));
			}
		}
		this.createPanel(title, color, columns, buttons);
	}
	
	/**
	 * creates a new buttonpanel
	 * 
	 * @param title
	 *            the title of the buttonpanel
	 * @param color
	 *            the background color of the title of the buttonpanel
	 * @param columns
	 *            the number of button columns
	 * @param actions
	 *            the actions to show on the buttonpanel
	 * @param toggleActions
	 *            show actions as togglebuttons
	 * @param captionOnTop
	 *            the label is in the north
	 */
	public ButtonPanel(
			String title,
			Color color,
			int columns,
			Vector<Action> actions,
			boolean toggleActions,
			boolean captionOnTop) {
		Vector<AbstractButton> buttons = new Vector<AbstractButton>();
		for (Iterator iter = actions.iterator(); iter.hasNext();) {
			Action action = (Action) iter.next();
			if (toggleActions) {
				buttons.add(new JToggleButton(action));
			} else {
				buttons.add(new JButton(action));
			}
		}
		this.createPanel(title, color, columns, buttons, captionOnTop);
	}
	
	/**
	 * @param title
	 * @param color
	 * @param columns
	 * @param buttons
	 */
	private void createPanel(
			String title,
			Color color,
			int columns,
			Vector<AbstractButton> buttons) {
		/* ================================================== */
		this.createPanel(title, color, columns, buttons, false);
		/* ================================================== */
	}
	
	/**
	 * creates a new buttonpanel
	 * 
	 * @param title
	 *            the title of the buttonpanel
	 * @param color
	 *            the background color of the title of the buttonpanel
	 * @param columns
	 *            the number of button columns
	 * @param buttons
	 *            the buttons to show on the buttonpanel
	 */
	private void createPanel(
			String title,
			Color color,
			int columns,
			Vector<AbstractButton> buttons,
			boolean captionOnTop) {
		/* ================================================== */
		this.columns = columns;
		this.setLayout(new BorderLayout(0, 2));
		this.setOpaque(false);
		
		if (title != null) {
			JLabel label = new BubbleLabel(" " + title + ":");
			
			if (color != null) {
				color = new Color(
						color.getRed(),
						color.getGreen(),
						color.getBlue(),
						200);
				label.setBackground(color);
			}
			
			label.setPreferredSize(new Dimension(22, 22));
			this.add(label, BorderLayout.NORTH);
		}
		
		this.content = new JPanel(new GridLayout(0, columns, 2, 2));
		this.content.setOpaque(false);
		for (Iterator iter = buttons.iterator(); iter.hasNext();) {
			AbstractButton element = (AbstractButton) iter.next();
			this.addButton(element);
		}
		/* ------------------------------------------------------- */
		if (captionOnTop) {
			/* ------------------------------------------------------- */
			JPanel stretchPanel = new JPanel(new FormLayout(
					"fill:pref",
					"fill:pref:grow,pref"));
			stretchPanel.setOpaque(false);
			CellConstraints cc = new CellConstraints();
			stretchPanel.add(this.content, cc.xy(1, 2));
			this.add(stretchPanel, BorderLayout.CENTER);
			/* ------------------------------------------------------- */
		} else
			this.add(this.content, BorderLayout.CENTER);
		/* ================================================== */
	}
	
	/**
	 * adds a button to the panel
	 * 
	 * @param jb
	 *            the Button to add
	 */
	public void addButton(AbstractButton jb) {
		if (this.columns == 1)
			jb.setHorizontalAlignment(SwingConstants.LEFT);
		else {
			jb.setMargin(new java.awt.Insets(2, 2, 2, 2));
		}
		this.content.add(jb);
	}
	
	/**
	 * adds a button to the panel
	 * 
	 * @param jb
	 *            the Button to add
	 */
	public void addToggleButton(JToggleButton jb) {
		if (this.columns == 1)
			jb.setHorizontalAlignment(SwingConstants.LEFT);
		else {
			jb.setMargin(new java.awt.Insets(2, 2, 2, 2));
		}
		this.content.add(jb);
		this.buttonGroup.add(jb);
	}
	
	public void addComponent(JComponent component) {
		/* ====================================================== */
		this.content.add(component);
		/* ====================================================== */
	}
	
	public void removeComponent(JComponent component) {
		/* ================================================== */
		this.content.remove(component);
		/* ================================================== */
	}
	
	/**
	 * removes a button from the panel
	 * 
	 * @param jb
	 *            the Button to remove
	 */
	public void removeButton(AbstractButton jb) {
		this.content.remove(jb);
		if (jb instanceof JToggleButton) {
			this.buttonGroup.remove(jb);
		}
	}
	
	/**
	 * adds an action to the panel as button
	 * 
	 * @param a
	 *            the action to add
	 */
	public void addAction(Action a) {
		JButton b = new JButton(a);
		this.map.put(a, b);
		this.addButton(b);
	}
	
	/**
	 * adds an action to the panel as togglebutton
	 * 
	 * @param a
	 *            the action to add
	 */
	public void addToggleAction(Action a) {
		JToggleButton b = new JToggleButton(a);
		this.map.put(a, b);
		this.addToggleButton(b);
	}
	
	/**
	 * removes an action from the panel
	 * 
	 * @param a
	 */
	public void removeAction(Action a) {
		AbstractButton b = this.map.get(a);
		this.map.remove(a);
		this.removeButton(b);
	}
	
	/**
	 * Sets the layout of the content panel.
	 * Use carefully!
	 * 
	 * @param layout
	 */
	public void setContentLayout(LayoutManager layout) {
		/* ================================================== */
		this.content.setLayout(layout);
		/* ================================================== */
	}
	
}
