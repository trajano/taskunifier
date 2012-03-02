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
package bizcal.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import bizcal.common.Calendar;
import bizcal.common.CalendarModel;
import bizcal.common.CalendarViewConfig;
import bizcal.swing.util.GradientArea;
import bizcal.util.BizcalException;

public class CalendarRowHeader {
	
	private JLayeredPane panel;
	private List<JLabel> calLabels = new ArrayList<JLabel>();
	private List<JLabel> calLines = new ArrayList<JLabel>();
	private GradientArea gradientArea;
	private int width = 100;
	private int footerHeight = 0;
	private CalendarModel model;
	private CalendarViewConfig config;
	
	public CalendarRowHeader(CalendarModel model, CalendarViewConfig config)
			throws Exception {
		this.model = model;
		this.config = config;
		this.panel = new JLayeredPane();
		this.panel.setLayout(new Layout());
	}
	
	public void refresh() throws Exception {
		this.panel.removeAll();
		this.calLabels.clear();
		this.calLines.clear();
		
		JLabel line = new JLabel();
		line.setBackground(this.config.getLineColor());
		line.setOpaque(true);
		this.calLines.add(line);
		this.panel.add(line, Integer.valueOf(2));
		
		Iterator i = this.model.getSelectedCalendars().iterator();
		while (i.hasNext()) {
			Calendar cal = (Calendar) i.next();
			JLabel label = new JLabel(cal.getSummary());
			label.setVerticalTextPosition(SwingConstants.CENTER);
			this.panel.add(label, Integer.valueOf(2));
			this.calLabels.add(label);
			line = new JLabel();
			line.setBackground(this.config.getLineColor());
			line.setOpaque(true);
			this.calLines.add(line);
			this.panel.add(line, Integer.valueOf(2));
		}
		this.gradientArea = new GradientArea(
				GradientArea.LEFT_RIGHT,
				Color.WHITE,
				ColumnHeaderPanel.GRADIENT_COLOR);
		this.gradientArea.setOpaque(true);
		this.gradientArea.setBorder(false);
		this.panel.add(this.gradientArea, Integer.valueOf(1));
	}
	
	private class Layout implements LayoutManager {
		
		@Override
		public void addLayoutComponent(String name, Component comp) {}
		
		@Override
		public void removeLayoutComponent(Component comp) {}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			try {
				return new Dimension(
						CalendarRowHeader.this.width,
						GroupView.PREFERRED_ROW_HEIGHT
								* CalendarRowHeader.this.model.getSelectedCalendars().size()
								+ CalendarRowHeader.this.footerHeight);
			} catch (Exception e) {
				throw BizcalException.create(e);
			}
		}
		
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(50, 50);
		}
		
		@Override
		public void layoutContainer(Container parent) {
			try {
				double rowHeight = GroupView.PREFERRED_ROW_HEIGHT;
				JLabel calLine = CalendarRowHeader.this.calLines.get(0);
				calLine.setBounds(0, 0, CalendarRowHeader.this.width, 1);
				for (int i = 0; i < CalendarRowHeader.this.calLabels.size(); i++) {
					JLabel calLabel = CalendarRowHeader.this.calLabels.get(i);
					calLabel.setBounds(
							5,
							(int) (i * rowHeight),
							CalendarRowHeader.this.width - 5,
							(int) rowHeight);
					calLine = CalendarRowHeader.this.calLines.get(i + 1);
					calLine.setBounds(
							0,
							(int) ((i + 1) * rowHeight),
							CalendarRowHeader.this.width,
							1);
				}
				CalendarRowHeader.this.gradientArea.setBounds(
						0,
						0,
						parent.getWidth(),
						parent.getHeight());
			} catch (Exception e) {
				throw BizcalException.create(e);
			}
		}
	}
	
	public JComponent getComponent() {
		return this.panel;
	}
	
	public void setFooterHeight(int footerHeight) {
		this.footerHeight = footerHeight;
	}
}
