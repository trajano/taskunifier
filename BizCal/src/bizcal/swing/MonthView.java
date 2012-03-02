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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import bizcal.common.CalendarViewConfig;
import bizcal.common.Event;
import bizcal.swing.util.ErrorHandler;
import bizcal.swing.util.FrameArea;
import bizcal.swing.util.TableLayoutPanel;
import bizcal.swing.util.TableLayoutPanel.Row;
import bizcal.util.BizcalException;
import bizcal.util.DateUtil;
import bizcal.util.TextUtil;

/**
 * 
 * @version
 * <br>
 *          $Log: MonthView.java,v $ <br>
 *          Revision 1.19 2008/06/19 12:20:00 heine_ <br>
 *          *** empty log message *** <br>
 * 
 */
public class MonthView extends CalendarView {
	
	private CalendarViewConfig config;
	private ColumnHeaderPanel columnHeader;
	private List<List> cells = new ArrayList<List>();
	private List hLines = new ArrayList();
	private List vLines = new ArrayList();
	private JScrollPane scrollPane;
	private JPanel calPanel;
	
	public MonthView(CalendarViewConfig config) throws Exception {
		/* ================================================== */
		super(config);
		this.config = config;
		this.calPanel = new JPanel();
		this.calPanel.setLayout(new Layout());
		
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
		this.columnHeader = new ColumnHeaderPanel(config, 7);
		
		this.columnHeader.setShowExtraDateHeaders(true);
		this.columnHeader.setMonthView(true);
		this.scrollPane.setColumnHeaderView(this.columnHeader.getComponent());
		/* ================================================== */
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see bizcal.swing.CalendarView#refresh0()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void refresh0() throws Exception {
		/* ================================================== */
		// clear all containers
		/* ------------------------------------------------------- */
		this.calPanel.removeAll();
		this.cells.clear();
		this.hLines.clear();
		this.vLines.clear();
		/* ------------------------------------------------------- */
		Calendar cal = DateUtil.newCalendar();
		/* ------------------------------------------------------- */
		// get the current date constraint for the month
		/* ------------------------------------------------------- */
		// create a new calendar object for this month
		cal.setTime(this.getInterval().getStartDate());
		/* ------------------------------------------------------- */
		// add 15 days to the start of the month, to get nealry
		// the middle of the month
		/* ------------------------------------------------------- */
		cal.add(Calendar.DAY_OF_YEAR, 15);
		int month = cal.get(Calendar.MONTH);
		/* ------------------------------------------------------- */
		// do something for the start of the week
		/* ------------------------------------------------------- */
		int lastDayOfWeek = cal.getFirstDayOfWeek();
		lastDayOfWeek--;
		if (lastDayOfWeek < 1)
			lastDayOfWeek += 7;
		
		/* ------------------------------------------------------- */
		// iterate over all selected calendars
		/* ------------------------------------------------------- */
		Iterator j = this.getModel().getSelectedCalendars().iterator();
		while (j.hasNext()) {
			/* ------------------------------------------------------- */
			bizcal.common.Calendar calInfo = (bizcal.common.Calendar) j.next();
			
			cal.setTime(this.getInterval().getStartDate());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
			/* ------------------------------------------------------- */
			// get the events of this calendar
			/* ------------------------------------------------------- */
			Map<Date, List<Event>> eventMap = this.createEventsPerDay(
					calInfo.getId(),
					true);
			
			int rowno = 0;
			
			while (true) {
				/* ------------------------------------------------------- */
				List<JComponent> row;
				if (this.cells.size() <= rowno) {
					/* ------------------------------------------------------- */
					row = new ArrayList<JComponent>();
					this.cells.add(row);
					/* ------------------------------------------------------- */
				} else
					row = this.cells.get(rowno);
				
				JComponent cell = this.createDayCell(
						cal,
						eventMap,
						month,
						calInfo.getId());
				this.calPanel.add(cell);
				row.add(cell);
				if (cal.get(Calendar.DAY_OF_WEEK) == lastDayOfWeek) {
					/* ------------------------------------------------------- */
					if (cal.get(Calendar.MONTH) != month)
						break;
					rowno++;
					/* ------------------------------------------------------- */
				}
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		
		int colCount = this.getModel().getSelectedCalendars().size() * 7;
		for (int i = 0; i < colCount - 1; i++) {
			JLabel line = new JLabel();
			line.setBackground(Color.LIGHT_GRAY);
			line.setOpaque(true);
			if ((i + 1) % 7 == 0)
				line.setBackground(this.getDescriptor().getLineColor3());
			this.calPanel.add(line);
			this.vLines.add(line);
		}
		
		int rowCount = this.cells.size() - 1;
		for (int i = 0; i < rowCount; i++) {
			JLabel line = new JLabel();
			line.setBackground(Color.LIGHT_GRAY);
			line.setOpaque(true);
			this.calPanel.add(line);
			this.hLines.add(line);
		}
		
		this.columnHeader.setModel(this.getModel());
		this.columnHeader.setPopupMenuCallback(this.popupMenuCallback);
		this.columnHeader.refresh();
		/* ================================================== */
	}
	
	/**
	 * @param cal
	 * @param eventMap
	 * @param month
	 * @param calId
	 * @return
	 * @throws Exception
	 */
	private JComponent createDayCell(
			Calendar cal,
			Map<Date, List<Event>> eventMap,
			int month,
			Object calId) throws Exception {
		/* ================================================== */
		Font eventFont = this.font;
		TableLayoutPanel panel = new TableLayoutPanel();
		
		if (cal.get(Calendar.MONTH) == month) {
			panel.setBackground(Color.WHITE);
		} else
			panel.setBackground(new Color(230, 230, 230));
		panel.createColumn(TableLayoutPanel.FILL);
		// panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		// panel.setBorder(BasicBorders.getRadioButtonBorder());
		int dayno = cal.get(Calendar.DAY_OF_MONTH);
		String text = "" + dayno;
		Row row = panel.createRow();
		JLabel label = new JLabel(text);
		// label.setOpaque(true);
		// label.setBackground(getDescriptor().getPrimaryColor());
		// label.setForeground(Color.black);
		label.setFont(this.font.deriveFont(Font.BOLD));
		row.createCell(label);
		panel.createRow(TableLayoutPanel.FILL);
		
		DateFormat format = DateFormat.getTimeInstance(
				DateFormat.SHORT,
				Locale.getDefault());
		List<Event> events = eventMap.get(DateUtil.round2Day(cal.getTime()));
		
		if (events != null) {
			/* ------------------------------------------------------- */
			for (Event event : events) {
				/* ------------------------------------------------------- */
				row = panel.createRow();
				String time = format.format(event.getStart());
				String summary = "";
				if (event.getSummary() != null)
					summary = event.getSummary();
				
				JPanel p = new JPanel(new BorderLayout());
				
				JLabel titleLabel = new JLabel(event.getDescription());
				titleLabel.setToolTipText(event.getToolTip());
				titleLabel.setOpaque(true);
				titleLabel.setBackground(event.getColor());
				titleLabel.setForeground(FrameArea.computeForeground(event.getColor()));
				titleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				titleLabel.addMouseListener(new EventMouseListener(event, calId));
				p.add(titleLabel, BorderLayout.CENTER);
				
				if (this.config.isShowTime()) {
					JLabel eventLabel = new JLabel(time + " " + summary);
					eventLabel.setFont(eventFont);
					time += "-" + format.format(event.getEnd());
					eventLabel.setToolTipText(event.getToolTip());
					eventLabel.setOpaque(true);
					eventLabel.setBackground(event.getColor());
					/* ------------------------------------------------------- */
					// set foreground color
					eventLabel.setForeground(FrameArea.computeForeground(event.getColor()));
					/* ------------------------------------------------------- */
					eventLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
					if (event.getIcon() != null)
						eventLabel.setIcon(event.getIcon());
					eventLabel.addMouseListener(new EventMouseListener(
							event,
							calId));
					p.add(eventLabel, BorderLayout.NORTH);
				} else {
					if (event.getIcon() != null)
						titleLabel.setIcon(event.getIcon());
				}
				
				row.createCell(p, TableLayoutPanel.TOP, TableLayoutPanel.FULL);
				/* ------------------------------------------------------- */
			}
			/* ------------------------------------------------------- */
		}
		panel.addMouseListener(new DayMouseListener(calId, cal.getTime()));
		JScrollPane scrollPanel = new JScrollPane(
				panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setBorder(BorderFactory.createEmptyBorder());
		scrollPanel.setPreferredSize(new Dimension(100, 100));
		return scrollPanel;
		// return panel;
		/* ================================================== */
	}
	
	/**
	 * @author martin.heinemann@tudor.lu
	 *         15.06.2007
	 *         15:41:10
	 * 
	 * 
	 * @version
	 * <br>
	 *          $Log: MonthView.java,v $ <br>
	 *          Revision 1.19 2008/06/19 12:20:00 heine_ <br>
	 *          *** empty log message *** <br>
	 * <br>
	 *          Revision 1.18 2008/01/21 14:13:26 heine_ <br>
	 *          *** empty log message *** <br>
	 * <br>
	 *          Revision 1.9 2007/06/22 13:14:49 heinemann <br>
	 *          *** empty log message *** <br>
	 * <br>
	 *          Revision 1.8 2007/06/20 12:08:08 heinemann <br>
	 *          *** empty log message *** <br>
	 * <br>
	 *          Revision 1.7 2007/06/18 11:41:32 heinemann <br>
	 *          bug fixes and alpha optimations <br>
	 * 
	 */
	private class EventMouseListener extends MouseAdapter {
		
		private Event event;
		
		private Object calId;
		
		/**
		 * @param event
		 * @param calId
		 */
		public EventMouseListener(Event event, Object calId) {
			/* ================================================== */
			this.calId = calId;
			this.event = event;
			/* ================================================== */
		}
		
		@Override
		public void mouseClicked(MouseEvent mevent) {
			/* ================================================== */
			try {
				if (mevent.getClickCount() == 2)
					MonthView.this.listener.showEvent(this.calId, this.event);
			} catch (Exception e) {
				ErrorHandler.handleError(e);
			}
			/* ================================================== */
		}
	}
	
	private class DayMouseListener extends MouseAdapter {
		
		private Object calId;
		private Date date;
		
		public DayMouseListener(Object calId, Date date) {
			this.calId = calId;
			this.date = date;
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			JPanel label = (JPanel) e.getSource();
			label.setCursor(new Cursor(Cursor.HAND_CURSOR));
			label.setBackground(label.getBackground().darker());
			label.setForeground(Color.LIGHT_GRAY);
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			JPanel label = (JPanel) e.getSource();
			label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			label.setBackground(label.getBackground().brighter());
			label.setForeground(Color.BLACK);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				/* ------------------------------------------------------- */
				if (MonthView.this.listener == null)
					return;
				/* ------------------------------------------------------- */
				if (e.getClickCount() < 2) {
					MonthView.this.listener.dateSelected(this.date);
					return;
				}
				/* ------------------------------------------------------- */
				if (!MonthView.this.getModel().isInsertable(
						this.calId,
						this.date))
					return;
				MonthView.this.listener.newEvent(this.calId, this.date);
				/* ------------------------------------------------------- */
			} catch (Exception exc) {
				ErrorHandler.handleError(exc);
			}
		}
	}
	
	@Override
	protected Date getDate(int xPos, int yPos) throws Exception {
		/* ================================================== */
		return null;
		/* ================================================== */
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public long getTimeInterval() throws Exception {
		/* ================================================== */
		// return 24*3600*1000*30;
		return DateUtil.MILLIS_DAY * 30;
		/* ================================================== */
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	protected String getHeaderText() throws Exception {
		/* ================================================== */
		Calendar cal = DateUtil.newCalendar();
		cal.setTime(this.getInterval().getStartDate());
		DateFormat format = new SimpleDateFormat(
				"MMMM yyyy",
				Locale.getDefault());
		return TextUtil.formatCase(format.format(cal.getTime()));
		/* ================================================== */
	}
	
	// protected JComponent createCalendarPanel()
	// throws Exception
	// {
	// calPanel = new JPanel();
	// calPanel.setLayout(new Layout());
	// calPanel.setBackground(Color.WHITE);
	// return calPanel;
	// }
	
	@Override
	protected boolean supportsDrag() {
		return false;
	}
	
	/**
	 * @author martin.heinemann@tudor.lu
	 *         19.06.2008
	 *         10:34:43
	 * 
	 * 
	 * @version
	 * <br>
	 *          $Log: MonthView.java,v $ <br>
	 *          Revision 1.19 2008/06/19 12:20:00 heine_ <br>
	 *          *** empty log message *** <br>
	 * 
	 */
	private class Layout implements LayoutManager {
		
		@Override
		public void addLayoutComponent(String name, Component comp) {}
		
		@Override
		public void removeLayoutComponent(Component comp) {}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			try {
				int width = 7
						* MonthView.this.getModel().getSelectedCalendars().size()
						* DayView.PREFERRED_DAY_WIDTH;
				return new Dimension(width, MonthView.this.getPreferredHeight());
			} catch (Exception e) {
				throw BizcalException.create(e);
			}
		}
		
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(50, 100);
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
		 */
		@Override
		public void layoutContainer(Container parent) {
			/* ================================================== */
			try {
				double width = parent.getWidth();
				width = width
						/ MonthView.this.getModel().getSelectedCalendars().size();
				width = width / 7;
				double height = parent.getHeight();
				height = height / MonthView.this.cells.size();
				for (int row = 0; row < MonthView.this.cells.size(); row++) {
					/* ------------------------------------------------------- */
					List rowList = MonthView.this.cells.get(row);
					for (int col = 0; col < rowList.size(); col++) {
						/*
						 * ------------------------------------------------------
						 * -
						 */
						JComponent cell = (JComponent) rowList.get(col);
						cell.setBounds(
								(int) (col * width + 1),
								(int) (row * height + 1),
								(int) width - 1,
								(int) height - 1);
						/*
						 * ------------------------------------------------------
						 * -
						 */
					}
					/* ------------------------------------------------------- */
				}
				
				int colCount = MonthView.this.getModel().getSelectedCalendars().size() * 7;
				for (int i = 0; i < colCount - 1; i++) {
					/* ------------------------------------------------------- */
					try {
						/*
						 * ------------------------------------------------------
						 * -
						 */
						JLabel line = (JLabel) MonthView.this.vLines.get(i);
						line.setBounds(
								(int) ((i + 1) * width),
								0,
								1,
								parent.getHeight());
						/*
						 * ------------------------------------------------------
						 * -
						 */
					} catch (Exception e) {
						// e.printStackTrace();
					}
					/* ------------------------------------------------------- */
				}
				int rowCount = MonthView.this.cells.size() - 1;
				for (int i = 0; i < rowCount; i++) {
					/* ------------------------------------------------------- */
					try {
						JLabel line = (JLabel) MonthView.this.hLines.get(i);
						line.setBounds(
								0,
								(int) ((i + 1) * height),
								parent.getWidth(),
								1);
					} catch (Exception e) {
						
					}
					/* ------------------------------------------------------- */
				}
				
			} catch (Exception e) {
				throw BizcalException.create(e);
			}
		}
		/* ================================================== */
	}
	
	/**
	 * @return
	 */
	private int getPreferredHeight() {
		return this.cells.size() * 40;
	}
	
	@Override
	public JComponent getComponent() {
		return this.scrollPane;
	}
	
	@Override
	public void addListener(CalendarListener listener) {
		super.addListener(listener);
		this.columnHeader.addCalendarListener(listener);
	}
	
}
