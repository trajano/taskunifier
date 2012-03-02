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
import java.awt.Font;
import java.awt.LayoutManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import bizcal.common.CalendarViewConfig;
import bizcal.swing.util.GradientArea;
import bizcal.util.TimeOfDay;

/**
 * 
 * Class to paint the time labels on the border of the calendar grid.
 * 
 * @author martin.heinemann@tudor.lu
 *         27.03.2008
 *         10:45:06
 * 
 * 
 * @version
 * <br>
 *          $Log: TimeLabelPanel.java,v $ <br>
 *          Revision 1.7 2008/06/12 13:04:18 heine_ <br>
 *          *** empty log message *** <br>
 * <br>
 *          Revision 1.6 2008/03/28 08:45:11 heine_ <br>
 *          *** empty log message *** <br>
 * 
 */
public class TimeLabelPanel {
	
	private JPanel panel;
	private List<JLabel> hourLabels = new ArrayList<JLabel>();
	private List<JLabel> minuteLabels = new ArrayList<JLabel>();
	private List<JLabel> hourLines = new ArrayList<JLabel>();
	private List<JLabel> minuteLines = new ArrayList<JLabel>();
	private Font font = UIManager.getFont("Label.font");
	private GradientArea gradientArea;
	private int width = 40;
	private int hourCount;
	private int footerHeight = 0;
	private CalendarViewConfig config;
	private TimeOfDay start;
	private TimeOfDay end;
	private SimpleDateFormat hourFormat;
	private Font hourFont;
	
	public TimeLabelPanel(
			CalendarViewConfig config,
			TimeOfDay start,
			TimeOfDay end) throws Exception {
		/* ================================================== */
		this.config = config;
		this.start = start;
		this.end = end;
		/* ------------------------------------------------------- */
		this.panel = new JPanel();
		this.panel.setLayout(new Layout());
		
		this.hourFormat = new SimpleDateFormat("HH");
		this.hourFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		this.hourFont = this.font.deriveFont((float) 12);
		this.hourFont = this.hourFont.deriveFont(Font.BOLD);
		/* ------------------------------------------------------- */
		this.refresh();
		/* ================================================== */
	}
	
	public void refresh() {
		/* ================================================== */
		try {
			this.hourCount = this.end.getHour() - this.start.getHour();
			
			if (this.hourCount < 0 && this.end.getDay() > 1)
				this.hourCount = 24 - this.start.getHour();
			
			if (this.hourCount == 0)
				this.hourCount = 24;
			/* ------------------------------------------------------- */
			// clear arrays
			this.hourLabels.clear();
			this.minuteLabels.clear();
			this.hourLines.clear();
			this.minuteLines.clear();
			/* ------------------------------------------------------- */
			// remove all elemtns from panel
			this.panel.removeAll();
			/* ------------------------------------------------------- */
			long pos = this.start.getValue();
			while (pos < this.end.getValue()) {
				Date date = new Date(pos);
				String timeTxt = this.hourFormat.format(date);
				JLabel timeLabel = new JLabel(timeTxt);
				timeLabel.setVerticalTextPosition(SwingConstants.CENTER);
				timeLabel.setFont(this.hourFont);
				this.panel.add(timeLabel);
				this.hourLabels.add(timeLabel);
				JLabel line = new JLabel();
				line.setBackground(this.config.getLineColor());
				line.setOpaque(true);
				this.hourLines.add(line);
				this.panel.add(line);
				
				timeTxt = "15";
				timeLabel = new JLabel(timeTxt);
				timeLabel.setFont(this.font);
				this.panel.add(timeLabel);
				this.minuteLabels.add(timeLabel);
				// line = new JLabel();
				// line.setBackground(this.config.getLineColor());
				// line.setOpaque(true);
				// minuteLines.add(line);
				// panel.add(line);
				this.createMinuteLine();
				this.createMinuteLine();
				
				timeTxt = "45";
				timeLabel = new JLabel(timeTxt);
				timeLabel.setFont(this.font);
				this.panel.add(timeLabel);
				this.minuteLabels.add(timeLabel);
				// line = new JLabel();
				// // line.setBackground(this.config.getLineColor());
				// line.setBackground(Color.RED);
				// line.setOpaque(true);
				// minuteLines.add(line);
				// panel.add(line);
				this.createMinuteLine();
				this.createMinuteLine();
				
				pos += 3600 * 1000;
			}
			this.gradientArea = new GradientArea(
					GradientArea.LEFT_RIGHT,
					Color.WHITE,
					ColumnHeaderPanel.GRADIENT_COLOR);
			this.gradientArea.setOpaque(true);
			this.gradientArea.setBorder(false);
			this.panel.add(this.gradientArea);
			
			this.panel.validate();
			this.panel.updateUI();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* ================================================== */
	}
	
	/**
	 * Creates a new JLabel for a line and adds it to the panel
	 */
	private void createMinuteLine() {
		/* ================================================== */
		JLabel line = new JLabel();
		line.setBackground(this.config.getLineColor());
		line.setOpaque(true);
		this.minuteLines.add(line);
		this.panel.add(line);
		/* ================================================== */
	}
	
	/**
	 * Sets the start end end interval.
	 * A refresh is made automatically
	 * 
	 * @param start
	 * @param end
	 */
	public void setStartEnd(TimeOfDay start, TimeOfDay end) {
		/* ================================================== */
		this.start = start;
		this.end = end;
		this.refresh();
		/* ================================================== */
	}
	
	private int getPreferredHeight() {
		return DayView.PIXELS_PER_HOUR * this.hourCount + this.footerHeight;
	}
	
	private class Layout implements LayoutManager {
		
		@Override
		public void addLayoutComponent(String name, Component comp) {}
		
		@Override
		public void removeLayoutComponent(Component comp) {}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(
					TimeLabelPanel.this.width,
					TimeLabelPanel.this.getPreferredHeight());
		}
		
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(50, 50);
		}
		
		@Override
		public void layoutContainer(Container parent) {
			try {
				double totHeight = parent.getHeight()
						- TimeLabelPanel.this.footerHeight;
				double rowHeight = totHeight / TimeLabelPanel.this.hourCount;
				double minuteRowHeight = rowHeight / 2;
				int colWidth = TimeLabelPanel.this.width / 2;
				int iMinute = 0;
				int iLine = 0;
				for (int i = 0; i < TimeLabelPanel.this.hourLabels.size(); i++) {
					/* ------------------------------------------------------- */
					// layout the hour labels
					/* ------------------------------------------------------- */
					JLabel hourLabel = TimeLabelPanel.this.hourLabels.get(i);
					hourLabel.setBounds(
							0,
							(int) (i * rowHeight),
							colWidth,
							(int) rowHeight);
					/* ------------------------------------------------------- */
					// layout the hour lines
					/* ------------------------------------------------------- */
					JLabel hourLine = TimeLabelPanel.this.hourLines.get(i);
					hourLine.setBounds(
							0,
							(int) ((i + 1) * rowHeight),
							TimeLabelPanel.this.width,
							1);
					/* ------------------------------------------------------- */
					// layout the first minute label
					/* ------------------------------------------------------- */
					JLabel minuteLabel = TimeLabelPanel.this.minuteLabels.get(iMinute);
					minuteLabel.setBounds(
							colWidth,
							(int) (i * rowHeight),
							colWidth,
							(int) (minuteRowHeight));
					iMinute++;
					/* ------------------------------------------------------- */
					// the minute line for the 30 min
					/* ------------------------------------------------------- */
					JLabel minuteLine = TimeLabelPanel.this.minuteLines.get(iLine);
					//
					minuteLine.setBounds(
							colWidth,
							(int) (i * rowHeight + minuteRowHeight),
							colWidth,
							1);
					iLine++;
					/* ------------------------------------------------------- */
					// line for 15
					/* ------------------------------------------------------- */
					JLabel minuteLine2 = TimeLabelPanel.this.minuteLines.get(iLine);
					
					minuteLine2.setBounds(colWidth * 2 - 4, (int) (i
							* rowHeight + minuteRowHeight / 2), colWidth, 1);
					
					iLine++;
					/* ------------------------------------------------------- */
					// the minute label for 45 min
					/* ------------------------------------------------------- */
					minuteLabel = TimeLabelPanel.this.minuteLabels.get(iMinute);
					minuteLabel.setBounds(
							colWidth,
							(int) (i * rowHeight + minuteRowHeight),
							colWidth,
							(int) minuteRowHeight);
					iMinute++;
					/* ------------------------------------------------------- */
					// line for 45
					/* ------------------------------------------------------- */
					JLabel minuteLine3 = TimeLabelPanel.this.minuteLines.get(iLine);
					
					minuteLine3.setBounds(
							colWidth * 2 - 4,
							(int) (i * rowHeight + minuteRowHeight + minuteRowHeight / 2),
							colWidth,
							1);
					
					iLine++;
					/* ------------------------------------------------------- */
				}
				TimeLabelPanel.this.gradientArea.setBounds(
						0,
						0,
						parent.getWidth(),
						parent.getHeight());
			} catch (Exception e) {
				// throw BizcalException.create(e);
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
