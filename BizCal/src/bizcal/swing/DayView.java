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
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import bizcal.common.DayViewConfig;
import bizcal.common.Event;
import bizcal.swing.util.FrameArea;
import bizcal.util.BizcalException;
import bizcal.util.DateInterval;
import bizcal.util.DateUtil;
import bizcal.util.Interval;
import bizcal.util.TimeOfDay;
import bizcal.util.Tuple;

public class DayView extends CalendarView {
	
	public static int PIXELS_PER_HOUR = 80;
	
	private static final int CAPTION_ROW_HEIGHT0 = 20;
	
	public static final int PREFERRED_DAY_WIDTH = 10;
	
	public static final Integer GRID_LEVEL = Integer.valueOf(1);
	
	private List<List<FrameArea>> frameAreaCols = new ArrayList<List<FrameArea>>();
	
	private List<List<Event>> eventColList = new ArrayList<List<Event>>();
	
	private List<Date> _dateList = new ArrayList<Date>();
	
	private Map<Tuple, JLabel> timeLines = new HashMap<Tuple, JLabel>();
	
	private HashMap<Date, Integer> linePositionMap = new HashMap<Date, Integer>();
	
	private Map<Integer, Date> minuteMapping = Collections.synchronizedMap(new HashMap<Integer, Date>());
	
	private Map hourLabels = new HashMap();
	
	private Map minuteLabels = new HashMap();
	
	private List<JLabel> vLines = new ArrayList<JLabel>();
	
	private List<JPanel> calBackgrounds = new ArrayList<JPanel>();
	
	private ColumnHeaderPanel columnHeader;
	
	private TimeLabelPanel rowHeader;
	
	private int dayCount;
	
	private JScrollPane scrollPane;
	
	private JLayeredPane calPanel;
	
	private boolean firstRefresh = true;
	
	private DayViewConfig config;
	
	private List<JLabel> dateFooters = new ArrayList<JLabel>();
	
	/**
	 * @param desc
	 * @throws Exception
	 */
	public DayView(DayViewConfig desc) throws Exception {
		this(desc, null);
	}
	
	/**
	 * @param desc
	 * @param upperLeftCornerComponent
	 *            component that is displayed in the upper left corner of the
	 *            scrollpaine
	 * @throws Exception
	 */
	public DayView(DayViewConfig desc, Component upperLeftCornerComponent)
			throws Exception {
		/* ================================================== */
		super(desc);
		this.config = desc;
		this.calPanel = new JLayeredPane();
		this.calPanel.setLayout(new Layout());
		ThisMouseListener mouseListener = new ThisMouseListener();
		ThisKeyListener keyListener = new ThisKeyListener();
		this.calPanel.addMouseListener(mouseListener);
		this.calPanel.addMouseMotionListener(mouseListener);
		this.calPanel.addKeyListener(keyListener);
		// calPanel.setPreferredSize(new
		// Dimension(calPanel.getPreferredSize().width,
		// calPanel.getPreferredSize().height+200));
		this.scrollPane = new JScrollPane(
				this.calPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setCursor(Cursor.getDefaultCursor());
		this.scrollPane.getVerticalScrollBar().setUnitIncrement(15);
		
		/* ------------------------------------------------------- */
		if (upperLeftCornerComponent == null) {
			/* ------------------------------------------------------- */
			this.scrollPane.setCorner(
					ScrollPaneConstants.UPPER_LEFT_CORNER,
					this.createCorner(true, true));
			/* ------------------------------------------------------- */
		} else {
			/* ------------------------------------------------------- */
			this.scrollPane.setCorner(
					ScrollPaneConstants.UPPER_LEFT_CORNER,
					upperLeftCornerComponent);
			/* ------------------------------------------------------- */
		}
		
		this.scrollPane.setCorner(
				ScrollPaneConstants.LOWER_LEFT_CORNER,
				this.createCorner(true, false));
		this.scrollPane.setCorner(
				ScrollPaneConstants.UPPER_RIGHT_CORNER,
				this.createCorner(false, true));
		this.columnHeader = new ColumnHeaderPanel(desc);
		this.columnHeader.setShowExtraDateHeaders(desc.isShowExtraDateHeaders());
		this.scrollPane.setColumnHeaderView(this.columnHeader.getComponent());
		/* ------------------------------------------------------- */
		// set the time label at the left side
		this.rowHeader = new TimeLabelPanel(desc, new TimeOfDay(
				this.config.getDayStartHour(),
				0), new TimeOfDay(this.config.getDayEndHour(), 0));
		/* ------------------------------------------------------- */
		this.rowHeader.setFooterHeight(this.getFooterHeight());
		this.scrollPane.setRowHeaderView(this.rowHeader.getComponent());
		
		// scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(),
		// scrollPane.getHeight()+400));
		
		// calPanel.addComponentListener(new ComponentAdapter() {
		// @Override
		// public void componentResized(ComponentEvent e) {
		// /* ====================================================== */
		// try {
		// // DayView.this.refresh();
		// // DayView.this.refresh0();
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		// /* ====================================================== */
		// }
		// });
		/* ================================================== */
	}
	
	@Override
	public void refresh0() throws Exception {
		/* ================================================== */
		// System.out.println("DayView::refresh0");
		// System.out.println("----");
		if (this.calPanel == null || this.getModel() == null)
			return;
		/* ------------------------------------------------------- */
		// remove nealry everything from the panel
		/* ------------------------------------------------------- */
		this.dayCount = (int) (this.getModel().getInterval().getDuration() / (24 * 3600 * 1000));
		this.calPanel.removeAll();
		this.calPanel.setBackground(Color.WHITE);
		this.rowHeader.setStartEnd(new TimeOfDay(
				this.config.getDayStartHour(),
				0), new TimeOfDay(this.config.getDayEndHour(), 0));
		this.rowHeader.setFooterHeight(this.getFooterHeight());
		this.rowHeader.getComponent().revalidate();
		
		this.frameAreaCols.clear();
		this.eventColList.clear();
		this.timeLines.clear();
		this.linePositionMap.clear();
		this.minuteMapping.clear();
		this.hourLabels.clear();
		this.minuteLabels.clear();
		this.calBackgrounds.clear();
		this.vLines.clear();
		this.dateFooters.clear();
		
		this.addDraggingComponents(this.calPanel);
		
		Font hourFont = this.getDayViewConfig().getFont().deriveFont((float) 12);
		hourFont = hourFont.deriveFont(Font.BOLD);
		/* ------------------------------------------------------- */
		// create a color for the lines
		/* ------------------------------------------------------- */
		Color color = this.getDayViewConfig().getLineColor();
		Color hlineColor = new Color(
				color.getRed(),
				color.getGreen(),
				color.getBlue(),
				this.getDayViewConfig().getGridAlpha());
		/* ------------------------------------------------------- */
		// Steps through the time axis and adds hour labels, minute labels
		// and timelines in different maps.
		// key: date, value: label
		/* ------------------------------------------------------- */
		long pos = this.getFirstInterval().getStartDate().getTime();
		while (pos < this.getFirstInterval().getEndDate().getTime()) {
			/* ------------------------------------------------------- */
			// create a date object for the current hour
			/* ------------------------------------------------------- */
			Date currentHour = new Date(pos);
			/* ------------------------------------------------------- */
			// load the number of timeslots per hour from the config
			/* ------------------------------------------------------- */
			int timeSlots = this.config.getNumberOfTimeSlots();
			// do not print more than 6 minute time slots (every 10'')
			// if (PIXELS_PER_HOUR > 120)
			// timeSlots = 6;
			/* ------------------------------------------------------- */
			// keep a maximum of timeslots per hour.
			/* ------------------------------------------------------- */
			// if (timeSlots > 10)
			// timeSlots = 10;
			/* ------------------------------------------------------- */
			// create a horizontal line for each time slot
			/* ------------------------------------------------------- */
			for (int i = 1; i <= timeSlots; i++) {
				/* ------------------------------------------------------- */
				// create a new JLabel for each line
				/* ------------------------------------------------------- */
				JLabel line = new JLabel();
				line.setOpaque(true);
				line.setBackground(hlineColor);
				/* ------------------------------------------------------- */
				// add the label to the panel. Layout will be done later in the
				// layout manager
				/* ------------------------------------------------------- */
				this.calPanel.add(line, GRID_LEVEL);
				/* ------------------------------------------------------- */
				// put a tuple of the current day and the minute that the line
				// is representing
				/* ------------------------------------------------------- */
				this.timeLines.put(new Tuple(currentHour, ""
						+ (60 / timeSlots)
						* i), line);
				this.addHorizontalLine(line);
				/* ------------------------------------------------------- */
			}
			/* ------------------------------------------------------- */
			// increase the position by one hour
			/* ------------------------------------------------------- */
			pos += DateUtil.MILLIS_HOUR;
			/* ------------------------------------------------------- */
		}
		if (this.config.isShowDateFooter()) {
			JLabel line = new JLabel();
			line.setBackground(this.getDayViewConfig().getLineColor());
			line.setOpaque(true);
			this.calPanel.add(line, GRID_LEVEL);
			this.timeLines.put(new Tuple(new Date(pos), "00"), line);
		}
		/* ------------------------------------------------------- */
		// create the columns for each day
		/* ------------------------------------------------------- */
		this.createColumns();
		/* ------------------------------------------------------- */
		// set the background color for each calendar
		/* ------------------------------------------------------- */
		for (Object obj : this.getSelectedCalendars()) {
			/* ------------------------------------------------------- */
			bizcal.common.Calendar cal = (bizcal.common.Calendar) obj;
			JPanel calBackground = new JPanel();
			calBackground.setBackground(cal.getColor());
			this.calBackgrounds.add(calBackground);
			this.calPanel.add(calBackground);
			/* ------------------------------------------------------- */
		}
		
		this.columnHeader.setModel(this.getModel());
		this.columnHeader.setPopupMenuCallback(this.popupMenuCallback);
		this.columnHeader.refresh();
		/* ------------------------------------------------------- */
		// if this is the first refresh, we must initialize the scrollpane
		/* ------------------------------------------------------- */
		if (this.firstRefresh)
			this.initScroll();
		this.firstRefresh = false;
		
		/* ------------------------------------------------------- */
		// do the refresh
		/* ------------------------------------------------------- */
		this.calPanel.validate();
		this.calPanel.repaint();
		/* ------------------------------------------------------- */
		// put the timelines in the background
		/* ------------------------------------------------------- */
		for (JLabel l : this.timeLines.values()) {
			try {
				/* --------------------------------------------- */
				this.calPanel.setComponentZOrder(
						l,
						this.calPanel.getComponents().length - 2);
				/* --------------------------------------------- */
			} catch (Exception e) {
				/* --------------------------------------------- */
				e.printStackTrace();
				/* --------------------------------------------- */
			}
		}
		/* ------------------------------------------------------- */
		this.scrollPane.validate();
		this.scrollPane.repaint();
		
		this.rowHeader.getComponent().updateUI();
		// Hack to make to init scroll work
		// JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		// scrollBar.setValue(scrollBar.getValue()-1);
		
		/* ================================================== */
	}
	
	/**
	 * Returns the number of columns that are to display.
	 * As bizcal can display multiple calendars in parallel, it
	 * multiplies the number of days with the number of displayed calendars.
	 * 
	 * @return
	 * @throws Exception
	 */
	private int getColCount() throws Exception {
		/* ================================================== */
		return this.dayCount * this.getSelectedCalendars().size();
		/* ================================================== */
	}
	
	/**
	 * Returns the first interval to show. Start day plus one.
	 * 
	 * @return
	 * @throws Exception
	 */
	private DateInterval getFirstInterval() throws Exception {
		/* ================================================== */
		Date start = this.getInterval().getStartDate();
		// Date end = DateUtil.getDiffDay(start, +1);
		
		return new DateInterval(DateUtil.round2Hour(
				start,
				this.config.getDayStartHour()), DateUtil.round2Hour(
				start,
				this.config.getDayEndHour()));
		/* ================================================== */
	}
	
	/**
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void createColumns() throws Exception {
		DateInterval interval = this.getFirstInterval();
		int cols = this.getColCount();
		
		this.frameAreaHash.clear();
		List<Event> events = null;
		DateInterval interval2 = null;
		/* ------------------------------------------------------- */
		// iterate over all columns
		/* ------------------------------------------------------- */
		for (int it = 0; it < cols; it++) {
			/* ------------------------------------------------------- */
			int iCal = it / this.dayCount;
			bizcal.common.Calendar cal = (bizcal.common.Calendar) this.getSelectedCalendars().get(
					iCal);
			Object calId = cal.getId();
			// obtain all events for the calendar
			events = this.broker.getEvents(calId);
			Collections.sort(events);
			
			if (it % this.dayCount == 0)
				interval2 = new DateInterval(interval);
			if (interval2 != null)
				this._dateList.add(interval2.getStartDate());
			
			Calendar startdate = DateUtil.newCalendar();
			startdate.setTime(interval2.getStartDate());
			/* ------------------------------------------------------- */
			// create vertical lines
			Color vlColor = this.getDayViewConfig().getLineColor();
			int vlAlpha = this.getDayViewConfig().getGridAlpha() + 50;
			if (vlAlpha > 255)
				vlAlpha = 255;
			/* ------------------------------------------------------- */
			Color vlAlphaColor = new Color(
					vlColor.getRed(),
					vlColor.getGreen(),
					vlColor.getBlue(),
					vlAlpha);
			/* ------------------------------------------------------- */
			if (it > 0) {
				/* ------------------------------------------------------- */
				JLabel verticalLine = new JLabel();
				verticalLine.setOpaque(true);
				verticalLine.setBackground(vlAlphaColor);
				// verticalLine.setBackground(getDesc().getLineColor());
				
				if (startdate.get(Calendar.DAY_OF_WEEK) == startdate.getFirstDayOfWeek())
					verticalLine.setBackground(this.getDescriptor().getLineColor2());
				if (this.getSelectedCalendars().size() > 1
						&& it % this.dayCount == 0)
					verticalLine.setBackground(this.getDescriptor().getLineColor3());
				this.calPanel.add(verticalLine, GRID_LEVEL);
				this.vLines.add(verticalLine);
				/* ------------------------------------------------------- */
			}
			/* ------------------------------------------------------- */
			List<FrameArea> frameAreas = new ArrayList<FrameArea>();
			
			this.frameAreaCols.add(frameAreas);
			
			if (calId == null)
				continue;
			Interval currDayInterval = this.getInterval(it % this.dayCount);
			List<Event> colEvents = new ArrayList<Event>();
			this.eventColList.add(colEvents);
			/* ------------------------------------------------------- */
			int iEvent = 0;
			if (events == null)
				events = new ArrayList();
			
			for (Event event : events) {
				/* ------------------------------------------------------- */
				DateInterval eventInterv = new DateInterval(
						event.getStart(),
						event.getEnd());
				if (!currDayInterval.overlap(eventInterv))
					continue;
				
				// if there are overlapping events
				FrameArea area = this.createFrameArea(calId, event);
				
				area.setBackground(this.config.getPrimaryColor());
				
				frameAreas.add(area);
				colEvents.add(event);
				
				this.calPanel.add(area, Integer.valueOf(event.getLevel()));
				iEvent++;
				
				/* ------------------------------------------------------- */
				if (!this.frameAreaHash.containsKey(event))
					this.frameAreaHash.put(event, area);
				else {
					this.frameAreaHash.get(event).addChild(area);
					
				}
				
			}
			
			if (this.config.isShowDateFooter()) {
				JLabel footer = new JLabel(this.broker.getDateFooter(
						cal.getId(),
						interval2.getStartDate(),
						colEvents));
				footer.setHorizontalAlignment(SwingConstants.CENTER);
				this.dateFooters.add(footer);
				this.calPanel.add(footer);
			}
			
			if (this.dayCount > 1)
				interval2 = this.incDay(interval2);
		}
		
	}
	
	// F�r in ett events start- eller slutdatum, h�jden p� f�nstret samt
	// intervallet som positionen ska ber�knas utifr�n
	/**
	 * Returns the y position for the date and the day
	 * 
	 * @param aDate
	 * @param dayNo
	 * @return
	 * @throws Exception
	 */
	private int getYPos(Date aDate, int dayNo) throws Exception {
		long time = aDate.getTime();
		return this.getYPos(time, dayNo);
	}
	
	/**
	 * @param time
	 * @param dayNo
	 * @return
	 * @throws Exception
	 */
	private int getYPos(long time, int dayNo) throws Exception {
		/* ================================================== */
		DateInterval interval = this.getInterval(dayNo);
		time -= interval.getStartDate().getTime();
		
		double viewPortHeight = this.getHeight()
				- this.getCaptionRowHeight()
				- this.getFooterHeight();
		// double timeSpan = (double) getTimeSpan();
		// double timeSpan = 24 * 3600 * 1000;
		double timeSpan = this.config.getHours() * 3600 * 1000;
		
		double dblTime = time;
		int ypos = (int) (dblTime / timeSpan * viewPortHeight);
		ypos += this.getCaptionRowHeight();
		return ypos;
		/* ================================================== */
	}
	
	/*
	 * private long getTimeSpan() throws Exception { return
	 * getDesc().getViewEndTime().getValue() -
	 * getDesc().getViewStartTime().getValue(); }
	 */
	
	/***
	 * Try to get a date fitting to the given position
	 * 
	 */
	@Override
	protected synchronized Date getDate(int xPos, int yPos) throws Exception {
		/* ================================================== */
		
		int colNo = this.getColumn(xPos);
		int dayNo = 0;
		/* ------------------------------------------------------- */
		// try to find the day in which the xPointer is located
		/* ------------------------------------------------------- */
		if (this.dayCount != 0)
			dayNo = colNo % this.dayCount;
		/* ------------------------------------------------------- */
		// get the DateInterval for the day
		/* ------------------------------------------------------- */
		DateInterval interval = this.getInterval(dayNo);
		/* ------------------------------------------------------- */
		// now we have the day. Next step is to find the time
		/* ------------------------------------------------------- */
		yPos -= this.getCaptionRowHeight();
		
		// BigDecimal bg_Pos = new BigDecimal (yPos);
		// BigDecimal bg_timeHeight = new BigDecimal (getTimeHeight());
		// BigDecimal bg_ratio = bg_Pos.divide
		// (bg_timeHeight,5,RoundingMode.DOWN);
		// BigDecimal bg_time = new BigDecimal
		// (interval.getDuration()).multiply(bg_ratio);
		// long time = bg_time.longValue();
		
		// Date duration = DateUtil.round2Minute(new
		// Date(interval.getDuration()));
		
		// double ratio = ((double) yPos) / ((double) getTimeHeight());
		
		// long time = (long) Math.round((double)(interval.getDuration()/60000 *
		// yPos) / (double)getTimeHeight());
		//
		// BigDecimal b = new BigDecimal(interval.getDuration());
		//
		// b.round(new MathContext(60000));
		// time *= 60000;
		// time += interval.getStartDate().getTime();
		/* ------------------------------------------------------- */
		Date foundDate = null;
		while (foundDate == null) {
			/* ------------------------------------------------------- */
			foundDate = this.minuteMapping.get(yPos);
			yPos++;
			if (yPos < 0)
				break;
			if (yPos >= this.getHeight())
				yPos = this.getHeight();
			// break;
			/* ------------------------------------------------------- */
		}
		/* ------------------------------------------------------- */
		// return new Date(time);
		if (foundDate != null) {
			TimeOfDay td = DateUtil.getTimeOfDay(foundDate);
			Date d = td.getDate(interval.getStartDate());
			return d;
		}
		return null;
		// return foundDate;
		
		/* ================================================== */
	}
	
	// private static long normalize(long time) {
	// /* ================================================== */
	// // return DateUtil.round2Minute(new Date(time)).getTime();
	// // int mod = 1000;
	// BigDecimal b = new BigDecimal(time);
	// // BigDecimal rounded = b.round(new MathContext(60000,
	// RoundingMode.HALF_UP));
	// BigDecimal rounded = b.setScale(60000, RoundingMode.HALF_DOWN);
	//
	//
	// System.out.println("Normalizing " + time + " to " + rounded.longValue());
	//
	// // return rounded.longValue();
	// return time;
	//
	// // return time/60000;
	// /* ================================================== */
	// }
	
	/**
	 * Returns the DateInterval object for the given day
	 * 
	 * @param dayNo
	 * @return
	 * @throws Exception
	 */
	private DateInterval getInterval(int dayNo) throws Exception {
		/* ================================================== */
		// get the first interval
		/* ------------------------------------------------------- */
		DateInterval interval = this.getFirstInterval();
		/* ------------------------------------------------------- */
		// cycle through the days until we have reached the desired one
		/* ------------------------------------------------------- */
		for (int i = 0; i < dayNo; i++)
			interval = this.incDay(interval);
		return interval;
		/* ================================================== */
	}
	
	private int getColumn(int xPos) throws Exception {
		xPos -= this.getXOffset();
		int width = this.getWidth() - this.getXOffset();
		double ratio = ((double) xPos) / ((double) width);
		return (int) (ratio * this.getColCount());
	}
	
	private Object getCalendarId(int colNo) throws Exception {
		int pos = 0;
		// dayCount = 1;
		if (this.dayCount != 0)
			pos = colNo / this.dayCount;
		bizcal.common.Calendar cal = (bizcal.common.Calendar) this.getSelectedCalendars().get(
				pos);
		return cal.getId();
	}
	
	@Override
	protected int getXOffset() {
		// return LABEL_COL_WIDTH;
		return 0;
	}
	
	private int getXPos(int colno) throws Exception {
		double x = this.getWidth();
		x = x - this.getXOffset();
		double ratio = ((double) colno) / ((double) this.getColCount());
		return ((int) (x * ratio)) + this.getXOffset();
		/*
		 * BigDecimal xPos = new BigDecimal((x * ratio) + getXOffset()); return
		 * xPos.setScale(0,BigDecimal.ROUND_CEILING).intValue();
		 */
	}
	
	private int getWidth() {
		return this.calPanel.getWidth();
	}
	
	private int getHeight() {
		return this.calPanel.getHeight();
	}
	
	private int getTimeHeight() throws Exception {
		return this.getHeight()
				- this.getCaptionRowHeight()
				- this.getFooterHeight();
	}
	
	private int getFooterHeight() {
		if (this.config.isShowDateFooter())
			return PIXELS_PER_HOUR / 2;
		return 0;
	}
	
	/**
	 * 
	 * 05.06.2007 11:31:56
	 * 
	 * 
	 * @version <br>
	 *          $Log: DayView.java,v $
	 *          Revision 1.38 2008/12/12 16:20:11 heine_
	 *          *** empty log message ***
	 * 
	 *          Revision 1.37 2008/08/12 12:47:27 heine_
	 *          fixed some bugs and made code improvements
	 * 
	 *          Revision 1.36 2008/06/10 13:16:36 heine_
	 *          *** empty log message ***
	 * 
	 *          Revision 1.35 2008/06/09 14:10:09 heine_
	 *          *** empty log message ***
	 * 
	 *          Revision 1.34 2008/05/30 11:36:48 heine_
	 *          *** empty log message ***
	 * 
	 *          Revision 1.33 2008/04/24 14:17:37 heine_
	 *          Improved timeslot search when clicking and moving
	 * 
	 *          Revision 1.32 2008/04/08 13:17:53 heine_
	 *          *** empty log message ***
	 * 
	 *          Revision 1.31 2008/03/28 08:45:11 heine_
	 *          *** empty log message ***
	 * 
	 *          Revision 1.30 2008/03/21 15:02:35 heine_
	 *          fixed problem when selecting lasso area in a region that was in
	 *          the bottom of the panel.
	 * 
	 *          Removed all the evil getBounds() statements. Should run fast now
	 *          and use lesser heap.
	 * 
	 *          Revision 1.29 2008/01/21 14:13:55 heine_
	 *          fixed nullpointer problem when refreshing without a model.
	 *          The refresh method just returns in case of this
	 * 
	 *          Revision 1.24 2008-01-21 14:06:11 heinemann
	 *          fixed nullpointer problem when refreshing without a model.
	 *          The refresh method just returns in case of this.
	 * 
	 *          Revision 1.23 2007-09-18 12:39:57 heinemann
	 *          *** empty log message ***
	 * 
	 *          Revision 1.22 2007/07/09 07:30:08 heinemann
	 *          *** empty log message ***
	 * 
	 *          Revision 1.21 2007/07/09 07:16:47 heinemann
	 *          *** empty log message ***
	 * 
	 *          Revision 1.20 2007/06/20 12:08:08 heinemann
	 *          *** empty log message ***
	 * 
	 *          Revision 1.19 2007/06/18 11:41:32 heinemann
	 *          bug fixes and alpha optimations
	 * 
	 *          Revision 1.18 2007/06/15 07:00:38 hermen
	 *          changed translatrix keys
	 * 
	 *          Revision 1.17 2007/06/14 13:31:25 heinemann
	 *          *** empty log message ***
	 * 
	 *          Revision 1.16 2007/06/12 11:58:03 heinemann
	 *          *** empty log message ***
	 * 
	 *          Revision 1.15 2007/06/11 13:23:39 heinemann
	 *          *** empty log message ***
	 * 
	 *          Revision 1.14 2007/06/08 12:21:10 heinemann
	 *          *** empty log message ***
	 * 
	 *          Revision 1.13 2007/06/07 12:12:50 heinemann
	 *          Events that lasts longer than a day and have at least one
	 *          overlapping, will now have the same width for all FrameAreas in
	 *          the columns <br>
	 *          Revision 1.12 2007/06/06 11:23:01 heinemann <br>
	 *          *** empty log message *** <br>
	 * 
	 */
	public class Layout implements LayoutManager {
		
		@Override
		public void addLayoutComponent(String name, Component comp) {}
		
		@Override
		public void removeLayoutComponent(Component comp) {}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			try {
				int width = DayView.this.dayCount
						* DayView.this.getModel().getSelectedCalendars().size()
						* PREFERRED_DAY_WIDTH;
				return new Dimension(width, DayView.this.getPreferredHeight());
			} catch (Exception e) {
				throw BizcalException.create(e);
			}
		}
		
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(50, 100);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public void layoutContainer(Container parent0) {
			/* ================================================== */
			try {
				DayView.this.resetVerticalLines();
				int width = DayView.this.getWidth();
				int height = DayView.this.getHeight();
				DateInterval day = DayView.this.getFirstInterval();
				
				int numberOfCols = DayView.this.getColCount();
				if (numberOfCols == 0)
					numberOfCols = 1;
				
				/* ------------------------------------------------------- */
				// iterate over all columns (a column per day)
				/* ------------------------------------------------------- */
				for (int i = 0; i < DayView.this.eventColList.size(); i++) {
					/* ------------------------------------------------------- */
					int dayNo = i % DayView.this.dayCount;
					int xpos = DayView.this.getXPos(i);
					int captionYOffset = DayView.this.getCaptionRowHeight()
							- CAPTION_ROW_HEIGHT0;
					int colWidth = DayView.this.getXPos(i + 1)
							- DayView.this.getXPos(i);
					// Obs. tempor�r l�sning med korrigering med +2. L�gg
					// till
					// korrigeringen p� r�tt st�lle
					// kan h�ra ihop synkning av tidsaxel och muslyssnare
					int vLineTop = captionYOffset + CAPTION_ROW_HEIGHT0 + 2;
					if (dayNo == 0
							&& (DayView.this.getSelectedCalendars().size() > 1)) {
						vLineTop = 0;
						day = DayView.this.getFirstInterval();
					}
					
					// Calendar startinterv =
					// Calendar.getInstance(Locale.getDefault());
					// startinterv.setTime(day.getStartDate());
					
					/* ------------------------------------------------------- */
					//
					/* ------------------------------------------------------- */
					if (i > 0) {
						JLabel verticalLine = DayView.this.vLines.get(i - 1);
						int vLineHeight = height - vLineTop;
						verticalLine.setBounds(xpos, vLineTop, 1, vLineHeight);
						// add the line position to the list
						DayView.this.addVerticalLine(verticalLine);
					}
					/* ------------------------------------------------------- */
					// show a footer. Haven't seen it working....
					/* ------------------------------------------------------- */
					if (DayView.this.config.isShowDateFooter()) {
						JLabel dayFooter = DayView.this.dateFooters.get(i);
						dayFooter.setBounds(
								xpos,
								DayView.this.getTimeHeight(),
								colWidth,
								DayView.this.getFooterHeight());
					}
					/* ------------------------------------------------------- */
					// get the date interval for the current day
					/* ------------------------------------------------------- */
					DateInterval currIntervall = DayView.this.getInterval(dayNo);
					FrameArea previousArea = null;
					/* ------------------------------------------------------- */
					// this indicates the position of the event inside the
					// day-column
					// if it is overlapping with other events
					/* ------------------------------------------------------- */
					int overlapCol = 0;
					/* ------------------------------------------------------- */
					// this is the total amount of columns inside a day-column.
					// Overlapping events will be painted in columns inside the
					// day-column
					/* ------------------------------------------------------- */
					int overlapColCount = 0;
					// ======================================================
					// eventColList contains a list of ArrayLists that holds the
					// events per day
					// the same with the frameAreaCols
					// =======================================================
					List<Event> events = DayView.this.eventColList.get(i);
					List<FrameArea> areas = DayView.this.frameAreaCols.get(i);
					/* ------------------------------------------------------- */
					int overlapCols[] = new int[events.size()];
					// for each event of the day
					for (int j = 0; j < events.size(); j++) {
						/*
						 * ------------------------------------------------------
						 * -
						 */
						FrameArea area = areas.get(j);
						Event event = events.get(j);
						// adapt the FrameArea according the appropriate event
						// data
						Date startTime = event.getStart();
						// if the starttime is before the displayable time, we
						// take the first displayable time
						if (startTime.before(currIntervall.getStartDate()))
							startTime = currIntervall.getStartDate();
						/*
						 * ------------------------------------------------------
						 * -
						 */
						Date endTime = event.getEnd();
						// if the events lasts longer than the current day, set
						// 23:59 as end
						if (endTime.after(currIntervall.getEndDate()))
							endTime = currIntervall.getEndDate();
						/*
						 * ------------------------------------------------------
						 * -
						 */
						// compute the new bounds of the framearea
						/*
						 * ------------------------------------------------------
						 * -
						 */
						// get the ypos for the start time
						int y1 = DayView.this.getYPos(startTime, dayNo);
						if (y1 < DayView.this.getCaptionRowHeight())
							y1 = DayView.this.getCaptionRowHeight();
						// get the y position for the end time
						int y2 = DayView.this.getYPos(endTime, dayNo);
						
						int dHeight = y2 - y1;
						int x1 = xpos;
						area.setBounds(x1, y1, colWidth, dHeight);
						/*
						 * ------------------------------------------------------
						 * -
						 */
						// Overlap logic
						//
						// overlapping works only for events that are not
						// in the background
						/*
						 * ------------------------------------------------------
						 * -
						 */
						if (!event.isBackground()) {
							/*
							 * --------------------------------------------------
							 * -----
							 */
							if (previousArea != null) {
								/*
								 * ----------------------------------------------
								 * ---------
								 */
								int previousY2 = previousArea.getY()
										+ previousArea.getHeight();
								// if the previous ends after the current starts
								if (previousY2 > y1) {
									// Previous event overlap
									overlapCol++;
									if (previousY2 < y2) {
										/*
										 * --------------------------------------
										 * -----------------
										 */
										// This events ends after the previous
										/*
										 * --------------------------------------
										 * -----------------
										 */
										previousArea = area;
										/*
										 * --------------------------------------
										 * -----------------
										 */
									}
								} else {
									/*
									 * ------------------------------------------
									 * -------------
									 */
									// set the overlap column to 0. this is the
									// column in which the
									// overlap event will be painted afterwards.
									/*
									 * ------------------------------------------
									 * -------------
									 */
									overlapCol = 0;
									previousArea = area;
									/*
									 * ------------------------------------------
									 * -------------
									 */
								}
								/*
								 * ----------------------------------------------
								 * ---------
								 */
							} else {
								previousArea = area;
								// overlapCols[j] = 0;
							}
							// store the column position for the overlapping
							// event
							overlapCols[j] = overlapCol;
							
							if (overlapCol > overlapColCount)
								overlapColCount = overlapCol;
							/*
							 * --------------------------------------------------
							 * -----
							 */
						} else
							overlapCols[j] = -1;
					}
					/* ------------------------------------------------------- */
					// Overlap logic. Loop the events/frameareas a second
					// time and set the xpos and widths
					/* ------------------------------------------------------- */
					if (overlapColCount > 0) {
						/*
						 * ------------------------------------------------------
						 * -
						 */
						int currWidth = colWidth;
						for (int k = 0; k < areas.size(); k++) {
							/*
							 * --------------------------------------------------
							 * -----
							 */
							Event event = events.get(k);
							/*
							 * --------------------------------------------------
							 * -----
							 */
							if (event.isBackground())
								continue;
							/*
							 * --------------------------------------------------
							 * -----
							 */
							FrameArea area = areas.get(k);
							int overlapIndex = overlapCols[k];
							if (overlapIndex == 0)
								currWidth = colWidth;
							/*
							 * --------------------------------------------------
							 * -----
							 */
							try {
								/*
								 * ----------------------------------------------
								 * ---------
								 */
								int kOffset = 1;
								while (events.get(k + kOffset).isBackground())
									kOffset++;
								
								if (overlapCols[k + kOffset] > 0) {
									// find biggest in line
									int curr = overlapIndex;
									for (int a = k + 1; a < areas.size(); a++) {
										/*
										 * --------------------------------------
										 * -----------------
										 */
										if (overlapCols[a] == 0)
											// break;
											continue;
										if (overlapCols[a] > curr)
											curr = overlapCols[a];
										/*
										 * --------------------------------------
										 * -----------------
										 */
									}
									currWidth = colWidth / (curr + 1);
								}
							} catch (Exception e) {}
							/*
							 * --------------------------------------------------
							 * -----
							 */
							area.setBounds(
									area.getX() + overlapIndex * currWidth,
									area.getY(),
									currWidth,
									area.getHeight());
						}
					}
				}
				/* ------------------------------------------------------- */
				// Loop the frameareas a third time
				// and set areas that belong to an event to the same width
				/* ------------------------------------------------------- */
				for (List<FrameArea> fAreas : DayView.this.frameAreaCols) {
					/* ------------------------------------------------------- */
					if (fAreas != null)
						for (FrameArea fa : fAreas) {
							/*
							 * --------------------------------------------------
							 * -----
							 */
							int sw = DayView.this.findSmallestFrameArea(fa);
							int baseFAWidth;
							try {
								baseFAWidth = DayView.this.getBaseFrameArea(
										fa.getEvent()).getWidth();
							} catch (Exception e) {
								continue;
							}
							if (sw > baseFAWidth) {
								sw = baseFAWidth;
							}
							fa.setBounds(
									fa.getX(),
									fa.getY(),
									sw,
									fa.getHeight());
							/*
							 * --------------------------------------------------
							 * -----
							 */
							// ensure, that the background events are really
							// painted in the background!
							/*
							 * --------------------------------------------------
							 * -----
							 */
							try {
								/* --------------------------------------------- */
								if (fa.getEvent().isBackground())
									DayView.this.calPanel.setComponentZOrder(
											fa,
											DayView.this.calPanel.getComponents().length - 5);
								/* --------------------------------------------- */
							} catch (Exception e) {
								// e.printStackTrace();
							}
							/*
							 * --------------------------------------------------
							 * -----
							 */
						}
					/* ------------------------------------------------------- */
				}
				
				// old obsolete
				// // Overlap logic. Loop the events/frameareas a second
				// // time and set the xpos and widths
				// if (overlapColCount > 0) {
				// int slotWidth = colWidth / (overlapColCount+1);
				// for (int j = 0; j < areas.size(); j++) {
				// Event event = (Event) events.get(j);
				// if (event.isBackground())
				// continue;
				// FrameArea area = (FrameArea) areas.get(j);
				// int index = overlapCols[j];
				// Rectangle r = area.getBounds();
				// area.setBounds(r.x + index*slotWidth, r.y, slotWidth,
				// r.height);
				// }
				// }
				/* ================================================== */
				// set up the line to minute mapping hashmap.
				// we create a hashmap of pixel to minute mapping to
				// have a fixed resource for resolving the explicit time
				// for a position on the calendar panel
				/* ================================================== */
				if (DayView.this.dayCount > 1)
					day = DayView.this.incDay(day);
				
				/* ------------------------------------------------------- */
				// iterate through all time lines
				/* ------------------------------------------------------- */
				for (Tuple key : DayView.this.timeLines.keySet()) {
					/* ------------------------------------------------------- */
					// get the day of the line
					/* ------------------------------------------------------- */
					Date date = (Date) key.elementAt(0);
					/* ------------------------------------------------------- */
					// extract the minutes from the string
					/* ------------------------------------------------------- */
					int minutes = Integer.parseInt((String) key.elementAt(1));
					/* ------------------------------------------------------- */
					JLabel line = DayView.this.timeLines.get(key);
					Date date1 = new Date(date.getTime()
							+ ((long) minutes)
							* 60
							* 1000);
					
					int y1 = DayView.this.getYPos(date1, 0);
					
					DayView.this.linePositionMap.put(date1, y1);
					
					int x1 = 0;
					int lineheight = 1;
					if (minutes > 0) {
						// x1 = 25;
						lineheight = 1;
					}
					line.setBounds(x1, y1, width, lineheight);
					/* ------------------------------------------------------- */
				}
				/* ------------------------------------------------------- */
				// build up the hash for minute to pixel mapping
				/* ------------------------------------------------------- */
				// get the dates of the lines and sort them
				/* ------------------------------------------------------- */
				List<Date> lines = new ArrayList<Date>(
						DayView.this.linePositionMap.keySet());
				/* ------------------------------------------------------- */
				// add the first, there is no line!
				/* ------------------------------------------------------- */
				// minuteMapping.put(0, getFirstInterval().getStartDate());
				DayView.this.linePositionMap.put(
						DayView.this.getFirstInterval().getStartDate(),
						0);
				Collections.sort(lines);
				/* ------------------------------------------------------- */
				int linesPerHour = DayView.this.config.getNumberOfTimeSlots();
				for (int i = 0; i < lines.size(); i++) {
					/* ------------------------------------------------------- */
					// get the date for the position
					/* ------------------------------------------------------- */
					Date currDate = lines.get(i);
					/* ------------------------------------------------------- */
					// get the position for that date
					/* ------------------------------------------------------- */
					int currPos = DayView.this.linePositionMap.get(currDate);
					/* ------------------------------------------------------- */
					// get the position of the next date
					/* ------------------------------------------------------- */
					int nextPos = 0;
					if (i + 1 < lines.size()) {
						Date nextDate = lines.get(i + 1);
						nextPos = DayView.this.linePositionMap.get(nextDate);
					} else
						nextPos = DayView.this.getTimeHeight();
					/* ------------------------------------------------------- */
					// div the height of one timeslot
					/* ------------------------------------------------------- */
					int slotHeight = nextPos - currPos;
					int numberOfMinutesPerSlot = 60 / linesPerHour;
					/* ------------------------------------------------------- */
					// compute the number of pixels for one minute
					/* ------------------------------------------------------- */
					int pixelsPerMinute = slotHeight / numberOfMinutesPerSlot;
					/* ------------------------------------------------------- */
					// add the minute->pixel mapping
					/* ------------------------------------------------------- */
					DayView.this.minuteMapping.put(currPos, currDate);
					int startMinute = DateUtil.getMinuteOfHour(currDate);
					
					for (int k = 1; k < numberOfMinutesPerSlot; k++) {
						
						DayView.this.minuteMapping.put(
								currPos + k * pixelsPerMinute,
								DateUtil.round2Minute(currDate, startMinute + k));
					}
					/* ------------------------------------------------------- */
				}
				/* ------------------------------------------------------- */
				// DEBUG print minuteMapping
				/* ------------------------------------------------------- */
				// List<Integer> minList = new
				// ArrayList<Integer>(minuteMapping.keySet());
				// Collections.sort(minList);
				// for (Integer in : minList)
				// System.out.println("Key: " + in + " => " +
				// minuteMapping.get(in));
				
				/* ------------------------------------------------------- */
				for (int iCal = 0; iCal < DayView.this.calBackgrounds.size(); iCal++) {
					/* ------------------------------------------------------- */
					int x1 = DayView.this.getXPos(iCal * DayView.this.dayCount);
					int x2 = DayView.this.getXPos((iCal + 1)
							* DayView.this.dayCount);
					JPanel calBackground = DayView.this.calBackgrounds.get(iCal);
					calBackground.setBounds(
							x1,
							DayView.this.getCaptionRowHeight(),
							x2 - x1,
							DayView.this.getHeight());
					/* ------------------------------------------------------- */
				}
			} catch (Exception e) {
				throw BizcalException.create(e);
			}
		}
		/* ================================================== */
	}
	
	private void layoutNew(List<Event> eventList, List<FrameArea> areaList) {
		/* ================================================== */
		// currCol
		// colsInRow
		// currWidth=width/colsInRow
		/* ------------------------------------------------------- */
		// Map to store the column position for each event
		HashMap<Event, Integer> colPositionMap = new HashMap<Event, Integer>();
		// Map to store the amount of events, that are painted in a row
		// neccessary to get the right width of each event
		HashMap<Event, Integer> colsInRowMap = new HashMap<Event, Integer>();
		/* ------------------------------------------------------- */
		
		/* ================================================== */
	}
	
	/**
	 * Finds the smallest width of a framearea and its children
	 * 
	 * @param fa
	 * @return
	 */
	private int findSmallestFrameArea(FrameArea fa) {
		/* ================================================== */
		if (fa.getChildren() == null || fa.getChildren().size() < 1)
			return fa.getWidth();
		
		int smallest = fa.getWidth();
		for (FrameArea child : fa.getChildren()) {
			if (child.getWidth() < smallest)
				smallest = child.getWidth();
		}
		return smallest;
		
		/* ================================================== */
	}
	
	@Override
	protected Object getCalendarId(int x, int y) throws Exception {
		return this.getCalendarId(this.getColumn(x));
	}
	
	/**
	 * returns the day view config object.
	 * If none is specified, it will create a default.
	 * 
	 * @return
	 * @throws Exception
	 */
	private DayViewConfig getDayViewConfig() throws Exception {
		/* ================================================== */
		DayViewConfig result = (DayViewConfig) this.getDescriptor();
		if (result == null) {
			result = new DayViewConfig();
			this.setDescriptor(result);
		}
		return result;
		/* ================================================== */
	}
	
	// public DayViewConfig getDayViewConfig() throws Exception {
	// return getDesc();
	// }
	
	@Override
	protected int getInitYPos() throws Exception {
		double viewStart = this.getModel().getViewStart().getValue();
		double ratio = viewStart / (24 * 3600 * 1000);
		return (int) (ratio * this.config.getHours() * PIXELS_PER_HOUR);
		
		// double viewStart = getModel().getViewStart().getValue();
		// double ratio = viewStart / (24 * 3600 * 1000);
		// return (int) (ratio * 24 * PIXELS_PER_HOUR);
		
	}
	
	private int getPreferredHeight() {
		
		return this.config.getHours()
				* PIXELS_PER_HOUR
				+ this.getFooterHeight();
	}
	
	@Override
	public JComponent getComponent() {
		return this.scrollPane;
	}
	
	public void initScroll() throws Exception {
		this.scrollPane.getViewport().setViewPosition(
				new Point(0, this.getInitYPos()));
	}
	
	@Override
	public void addListener(CalendarListener listener) {
		super.addListener(listener);
		this.columnHeader.addCalendarListener(listener);
	}
	
}
