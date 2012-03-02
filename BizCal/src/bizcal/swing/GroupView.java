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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import bizcal.common.CalendarModel;
import bizcal.common.CalendarViewConfig;
import bizcal.common.Event;
import bizcal.swing.util.FrameArea;
import bizcal.util.BizcalException;
import bizcal.util.DateInterval;
import bizcal.util.DateUtil;
import bizcal.util.LocaleBroker;
import bizcal.util.TextUtil;
import bizcal.util.TimeOfDay;

/**
 * @author Fredrik Bertilsson
 */
public class GroupView extends CalendarView {
	
	private static final int LABEL_COL_WIDTH = 70;
	public static final int HOUR_RESOLUTION = 2;
	public static final int PREFERRED_HOUR_WIDTH = 10;
	public static final int PREFERRED_ROW_HEIGHT = 40;
	
	private List<List<FrameArea>> frameAreaRows = new ArrayList<List<FrameArea>>();
	private List eventRows = new ArrayList();
	private Map vLines = new HashMap();
	private List hLines = new ArrayList();
	private JLayeredPane calPanel;
	private JScrollPane scrollPane;
	private DaysHoursHeaderPanel columnHeader;
	private CalendarRowHeader rowHeader;
	private List calBackgrounds = new ArrayList();
	private int dayCount;
	
	public GroupView(CalendarViewConfig config, CalendarModel model)
			throws Exception {
		super(config);
		this.setModel(model);
		this.font = UIManager.getFont("Label.font");
		this.calPanel = new JLayeredPane();
		this.calPanel.setLayout(new Layout());
		ThisMouseListener mouseListener = new ThisMouseListener();
		this.calPanel.addMouseListener(mouseListener);
		this.calPanel.addMouseMotionListener(mouseListener);
		this.scrollPane = new JScrollPane(
				this.calPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setCursor(Cursor.getDefaultCursor());
		this.scrollPane.getVerticalScrollBar().setUnitIncrement(15);
		this.scrollPane.setCorner(
				ScrollPaneConstants.UPPER_LEFT_CORNER,
				this.createCorner(true, true));
		this.scrollPane.setCorner(
				ScrollPaneConstants.LOWER_LEFT_CORNER,
				this.createCorner(true, false));
		this.scrollPane.setCorner(
				ScrollPaneConstants.UPPER_RIGHT_CORNER,
				this.createCorner(false, true));
		this.columnHeader = new DaysHoursHeaderPanel(config, model);
		this.scrollPane.setColumnHeaderView(this.columnHeader.getComponent());
		this.rowHeader = new CalendarRowHeader(model, config);
		this.rowHeader.setFooterHeight(0);
		this.scrollPane.setRowHeaderView(this.rowHeader.getComponent());
		
	}
	
	@Override
	public void refresh0() throws Exception {
		this.calPanel.removeAll();
		this.calPanel.setBackground(Color.WHITE);
		
		this.frameAreaRows.clear();
		this.eventRows.clear();
		this.hLines.clear();
		this.vLines.clear();
		this.calBackgrounds.clear();
		
		this.dayCount = DateUtil.getDateDiff(
				this.getModel().getInterval().getEndDate(),
				this.getModel().getInterval().getStartDate());
		
		this.addDraggingComponents(this.calPanel);
		
		JLabel hLine = new JLabel();
		hLine.setBackground(this.getDescriptor().getLineColor());
		hLine.setOpaque(true);
		this.calPanel.add(hLine, Integer.valueOf(1));
		this.hLines.add(hLine);
		
		Iterator i = this.getModel().getSelectedCalendars().iterator();
		while (i.hasNext()) {
			bizcal.common.Calendar cal = (bizcal.common.Calendar) i.next();
			Object calId = cal.getId();
			String calHeader = cal.getSummary();
			calHeader = StringLengthFormater.formatNameString(
					calHeader,
					this.font,
					LABEL_COL_WIDTH - 5);
			
			hLine = new JLabel();
			hLine.setBackground(this.getDescriptor().getLineColor());
			hLine.setOpaque(true);
			this.calPanel.add(hLine, Integer.valueOf(1));
			this.hLines.add(hLine);
			
			List<FrameArea> frameAreas = new ArrayList<FrameArea>();
			this.frameAreaRows.add(frameAreas);
			
			List events = this.getModel().getEvents(calId);
			Collections.sort(events);
			
			this.eventRows.add(events);
			Iterator j = events.iterator();
			while (j.hasNext()) {
				Event event = (Event) j.next();
				FrameArea area = this.createFrameArea(calId, event);
				frameAreas.add(area);
				this.calPanel.add(area, Integer.valueOf(event.getLevel()));
			}
		}
		
		Calendar cal = Calendar.getInstance(LocaleBroker.getLocale());
		cal.setTime(this.getInterval().getStartDate());
		while (cal.getTime().getTime() < this.getInterval().getEndDate().getTime()) {
			Date date = cal.getTime();
			
			// Day line
			JLabel line = new JLabel();
			line.setBackground(this.getDescriptor().getLineColor2());
			line.setOpaque(true);
			this.calPanel.add(line, Integer.valueOf(2));
			this.vLines.put(date, line);
			
			if (this.dayCount <= 7) {
				TimeOfDay startTime = this.getDescriptor().getStartView();
				cal.set(Calendar.HOUR_OF_DAY, startTime.getHour());
				cal.set(Calendar.MINUTE, startTime.getMinute());
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				TimeOfDay endTime = this.getDescriptor().getEndView();
				while (true) {
					TimeOfDay timeOfDay = new TimeOfDay(cal.getTime());
					if (timeOfDay.getValue() >= endTime.getValue())
						break;
					
					line = new JLabel();
					line.setBackground(this.getDescriptor().getLineColor());
					line.setOpaque(true);
					this.calPanel.add(line, Integer.valueOf(2));
					this.vLines.put(cal.getTime(), line);
					cal.add(Calendar.HOUR, +1 * HOUR_RESOLUTION);
				}
			}
			
			cal.add(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			// pos += 24 * 3600 * 1000;
			
		}
		
		i = this.getSelectedCalendars().iterator();
		while (i.hasNext()) {
			bizcal.common.Calendar calendar = (bizcal.common.Calendar) i.next();
			JPanel calBackground = new JPanel();
			calBackground.setBackground(calendar.getColor());
			this.calBackgrounds.add(calBackground);
			this.calPanel.add(calBackground, Integer.valueOf(1));
		}
		
		this.calPanel.validate();
		this.calPanel.repaint();
		
		this.columnHeader.setModel(this.getModel());
		this.columnHeader.refresh();
		this.rowHeader.refresh();
		
	}
	
	private int getWidth() {
		return this.calPanel.getWidth();
	}
	
	private int getHeight() {
		return this.calPanel.getHeight();
	}
	
	@Override
	protected int getCaptionRowHeight() {
		// return CAPTION_ROW_HEIGHT0 * 2;
		return 0;
	}
	
	@Override
	protected int getXOffset() {
		// return LABEL_COL_WIDTH;
		return 0;
	}
	
	private int getTimeHeight() {
		return this.getHeight() - this.getCaptionRowHeight();
	}
	
	private int getTimeWidth() {
		return this.getWidth() - this.getXOffset();
	}
	
	private int getRowHeight() throws Exception {
		// return getTimeHeight() / getModel().getSelectedCalendars().size();
		return PREFERRED_ROW_HEIGHT;
	}
	
	private int getXPos(Date date) throws Exception {
		TimeOfDay time = new TimeOfDay(date);
		long x = time.getValue()
				- this.getDescriptor().getStartView().getValue();
		if (x < 0)
			x = 0;
		long dayViewDuration = this.getDescriptor().getEndView().getValue()
				- this.getDescriptor().getStartView().getValue();
		double ratio = ((double) x) / ((double) dayViewDuration);
		double dayWidth = this.getDayWidth();
		int datediff = DateUtil.getDateDiff(
				date,
				this.getInterval().getStartDate());
		return (int) (this.getXOffset() + datediff * dayWidth + ratio
				* dayWidth);
	}
	
	private double getDayWidth() throws Exception {
		long duration = this.getInterval().getDuration();
		duration = duration / 24 / 3600 / 1000;
		return ((double) this.getTimeWidth() / (double) duration);
	}
	
	@Override
	protected LayoutManager getLayout() {
		return new Layout();
	}
	
	private class Layout implements LayoutManager {
		
		@Override
		public void addLayoutComponent(String name, Component comp) {}
		
		@Override
		public void removeLayoutComponent(Component comp) {}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			try {
				DateInterval interval = GroupView.this.getModel().getInterval();
				int dayCount = DateUtil.getDateDiff(
						interval.getEndDate(),
						interval.getStartDate());
				int width = dayCount
						* GroupView.this.getHourCount()
						* PREFERRED_HOUR_WIDTH;
				// int height = getModel().getSelectedCalendars().size() *
				// PREFERRED_ROW_HEIGHT;
				int height = 10 * PREFERRED_ROW_HEIGHT;
				return new Dimension(width, height);
			} catch (Exception e) {
				throw BizcalException.create(e);
			}
		}
		
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(50, 100);
		}
		
		@Override
		public void layoutContainer(Container parent0) {
			try {
				int width = GroupView.this.getWidth();
				int height = GroupView.this.getHeight();
				int yoffset = GroupView.this.getCaptionRowHeight();
				int rowHeight = GroupView.this.getRowHeight();
				
				JLabel hLine = (JLabel) GroupView.this.hLines.get(0);
				hLine.setBounds(0, 0, width, 1);
				
				int yPos = yoffset;
				for (int i = 0; i < GroupView.this.eventRows.size(); i++) {
					List areas = GroupView.this.frameAreaRows.get(i);
					List events = (List) GroupView.this.eventRows.get(i);
					
					FrameArea prevArea = null;
					int overlapCol = 0;
					int overlapColCount = 0;
					int overlapCols[] = new int[events.size()];
					
					for (int j = 0; j < areas.size(); j++) {
						FrameArea area = (FrameArea) areas.get(j);
						Event event = (Event) events.get(j);
						int x1 = GroupView.this.getXPos(event.getStart());
						int x2 = GroupView.this.getXPos(event.getEnd());
						area.setBounds(x1, yPos, x2 - x1, rowHeight);
						
						// Overlap logic
						if (!event.isBackground()) {
							if (prevArea != null) {
								Rectangle r = prevArea.getBounds();
								int prevX2 = r.x + r.width;
								if (prevX2 > x1) {
									// Previous event overlap
									overlapCol++;
									if (prevX2 < x2) {
										// This events finish later than
										// previous
										prevArea = area;
									}
								} else {
									overlapCol = 0;
									prevArea = area;
								}
							} else
								prevArea = area;
							overlapCols[j] = overlapCol;
							if (overlapCol > overlapColCount)
								overlapColCount = overlapCol;
						} else
							overlapCols[j] = 0;
					}
					// Overlap logic. Loop the events/frameareas a second
					// time and set the xpos and widths
					if (overlapColCount > 0) {
						int slotHeight = rowHeight / (overlapColCount + 1);
						for (int j = 0; j < areas.size(); j++) {
							Event event = (Event) events.get(j);
							if (event.isBackground())
								continue;
							FrameArea area = (FrameArea) areas.get(j);
							int index = overlapCols[j];
							Rectangle r = area.getBounds();
							area.setBounds(
									r.x,
									r.y + index * slotHeight,
									r.width,
									slotHeight);
						}
					}
					
					hLine = (JLabel) GroupView.this.hLines.get(i + 1);
					hLine.setBounds(0, yPos + rowHeight, width, 1);
					
					yPos += rowHeight;
				}
				
				int captionHeight = GroupView.this.getCaptionRowHeight() / 2;
				// long pos = getInterval().getStartDate().getTime();
				Calendar cal = Calendar.getInstance(LocaleBroker.getLocale());
				cal.setTime(GroupView.this.getInterval().getStartDate());
				while (cal.getTime().getTime() < GroupView.this.getInterval().getEndDate().getTime()) {
					Date date = cal.getTime();
					int xpos = GroupView.this.getXPos(date);
					
					JLabel line = (JLabel) GroupView.this.vLines.get(date);
					line.setBounds(xpos, 0, 1, height);
					
					if (GroupView.this.dayCount <= 7) {
						TimeOfDay startTime = GroupView.this.getDescriptor().getStartView();
						cal.set(Calendar.HOUR_OF_DAY, startTime.getHour());
						cal.set(Calendar.MINUTE, startTime.getMinute());
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);
						TimeOfDay endTime = GroupView.this.getDescriptor().getEndView();
						while (true) {
							TimeOfDay timeOfDay = new TimeOfDay(cal.getTime());
							if (timeOfDay.getValue() >= endTime.getValue())
								break;
							
							xpos = GroupView.this.getXPos(cal.getTime());
							line = (JLabel) GroupView.this.vLines.get(cal.getTime());
							line.setBounds(xpos, captionHeight, 1, height
									- captionHeight);
							cal.add(Calendar.HOUR, +1 * HOUR_RESOLUTION);
						}
					}
					
					cal.add(Calendar.DAY_OF_MONTH, 1);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
				}
				
				yPos = yoffset;
				for (int iCal = 0; iCal < GroupView.this.calBackgrounds.size(); iCal++) {
					int x1 = GroupView.this.getXOffset();
					int x2 = GroupView.this.getWidth();
					JPanel calBackground = (JPanel) GroupView.this.calBackgrounds.get(iCal);
					calBackground.setBounds(x1, yPos, x2 - x1, rowHeight);
					yPos += rowHeight;
				}
				
			} catch (Exception e) {
				throw BizcalException.create(e);
			}
		}
	}
	
	@Override
	public JComponent getComponent() {
		return this.scrollPane;
	}
	
	@Override
	protected Date getDate(int xPos, int yPos) throws Exception {
		double daywidth = this.getDayWidth();
		xPos -= this.getXOffset();
		int dayno = (int) (xPos / daywidth);
		xPos -= dayno * daywidth;
		double ratio = xPos / daywidth;
		Date date = this.getInterval().getStartDate();
		date = DateUtil.getDiffDay(date, dayno);
		long dayViewDuration = this.getDescriptor().getEndView().getValue()
				- this.getDescriptor().getStartView().getValue();
		long startTime = this.getDescriptor().getStartView().getValue();
		long passedTime = (long) (ratio * dayViewDuration);
		TimeOfDay timeOfDay = new TimeOfDay(startTime + passedTime);
		date = timeOfDay.getDate(date);
		return date;
	}
	
	@Override
	protected Object getCalendarId(int x, int y) throws Exception {
		int pos = y / this.getRowHeight();
		if (pos >= this.getSelectedCalendars().size())
			return null;
		bizcal.common.Calendar cal = (bizcal.common.Calendar) this.getSelectedCalendars().get(
				pos);
		return cal.getId();
	}
	
	protected String getHeaderText() throws Exception {
		Date from = this.getInterval().getStartDate();
		Calendar date = Calendar.getInstance(LocaleBroker.getLocale());
		date.setTime(this.getInterval().getEndDate());
		date.add(Calendar.DATE, -1);
		DateFormat format = new SimpleDateFormat(
				"MMMM yyyy",
				LocaleBroker.getLocale());
		return TextUtil.formatCase(format.format(from));
	}
	
	private int getHourCount() throws Exception {
		return this.getDescriptor().getEndView().getHour()
				- this.getDescriptor().getStartView().getHour();
	}
	
}
