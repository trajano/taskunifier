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
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import bizcal.common.CalendarModel;
import bizcal.common.CalendarViewConfig;
import bizcal.swing.util.ErrorHandler;
import bizcal.swing.util.GradientArea;
import bizcal.swing.util.ResourceIcon;
import bizcal.swing.util.TrueGridLayout;
import bizcal.util.BizcalException;
import bizcal.util.DateUtil;
import bizcal.util.LocaleBroker;
import bizcal.util.TextUtil;

public class ColumnHeaderPanel {
	
	public static final Color GRADIENT_COLOR = new Color(230, 230, 230);
	private PopupMenuCallback popupMenuCallback;
	private JPanel panel;
	private List calHeaders = new ArrayList();
	private List dateHeaders = new ArrayList();
	private List dateHeaders2 = new ArrayList();
	private List dateList = new ArrayList();
	private List dateLines = new ArrayList();
	private GradientArea gradientArea;
	private JLabel refLabel = new JLabel("AAA");
	private int rowCount;
	private int dayCount;
	private CalendarModel model;
	private Color lineColor = Color.LIGHT_GRAY;
	private int fixedDayCount = -1;
	private CalendarListener listener;
	private boolean showExtraDateHeaders = false;
	private CalendarViewConfig config;
	private boolean isMonthView = false;
	
	// // formater for month view
	// DateFormat monthDateFormat = new SimpleDateFormat("EEEE",
	// LocaleBroker.getLocale());
	// // formater for week view
	// DateFormat weekDateFormat = new SimpleDateFormat("EE - dd.MM.",
	// LocaleBroker.getLocale());
	// // formater for day view
	// DateFormat dayFormat = new SimpleDateFormat("EEEE dd.MM.yyyy",
	// LocaleBroker.getLocale());
	
	public ColumnHeaderPanel(CalendarViewConfig config) {
		this.config = config;
		this.panel = new JPanel();
		this.panel.setLayout(new Layout());
		this.gradientArea = new GradientArea(
				GradientArea.TOP_BOTTOM,
				Color.WHITE,
				GRADIENT_COLOR);
		this.gradientArea.setBorder(false);
	}
	
	public ColumnHeaderPanel(CalendarViewConfig config, int fixedDayCount) {
		this(config);
		this.fixedDayCount = fixedDayCount;
	}
	
	@SuppressWarnings("unchecked")
	public void refresh() throws Exception {
		this.calHeaders.clear();
		this.dateHeaders.clear();
		this.dateHeaders2.clear();
		this.dateList.clear();
		this.dateLines.clear();
		this.panel.removeAll();
		
		Calendar calendar = DateUtil.newCalendar();
		this.dayCount = DateUtil.getDateDiff(
				this.model.getInterval().getEndDate(),
				this.model.getInterval().getStartDate());
		if (this.fixedDayCount > 0)
			this.dayCount = this.fixedDayCount;
		
		int calCount = this.model.getSelectedCalendars().size();
		if (this.dayCount >= 1 || calCount > 1) {
			if (this.dayCount > 1 && calCount > 1)
				this.rowCount = 2;
			else
				this.rowCount = 1;
			DateFormat toolTipFormat = new SimpleDateFormat(
					"EEEE d MMMM",
					LocaleBroker.getLocale());
			
			// DateFormat dateFormat = new SimpleDateFormat("EE - d MMMM yyyy",
			// LocaleBroker.getLocale());
			// DateFormat longDateFormat = new
			// SimpleDateFormat("EEEE - d MMMM yyyy",
			// LocaleBroker.getLocale());
			//
			// DateFormat shortDateFormat =
			// DateFormat.getDateInstance(DateFormat.SHORT,
			// LocaleBroker.getLocale());
			
			// TODO ??????
			// if (dayCount == 5 || dayCount == 7) {
			// }
			for (int j = 0; j < calCount; j++) {
				bizcal.common.Calendar cal = (bizcal.common.Calendar) this.model.getSelectedCalendars().get(
						j);
				if (calCount >= 1) {
					JLabel headerLabel = new JLabel(
							cal.getSummary(),
							SwingConstants.CENTER);
					headerLabel.addMouseListener(new CalHeaderMouseListener(
							cal.getId()));
					headerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
					JComponent header = headerLabel;
					if (cal.isCloseable()) {
						JPanel panel = new JPanel();
						panel.setOpaque(false);
						panel.setLayout(new BorderLayout());
						panel.add(headerLabel, BorderLayout.CENTER);
						JLabel iconLabel = new JLabel(new ResourceIcon(
								"/bizcal/res/cancel.gif"));
						iconLabel.addMouseListener(new CloseListener(
								cal.getId()));
						panel.add(iconLabel, BorderLayout.EAST);
						header = panel;
					}
					this.calHeaders.add(header);
					this.panel.add(header);
				}
				JPanel dateHeaderPanel = new JPanel();
				dateHeaderPanel.setLayout(new TrueGridLayout(1, this.dayCount));
				dateHeaderPanel.setOpaque(false);
				Date date = this.model.getInterval().getStartDate();
				if (this.fixedDayCount > 0)
					date = DateUtil.round2Week(date);
				for (int i = 0; i < this.dayCount; i++) {
					/* ------------------------------------------------------- */
					// ==========================================================
					// Bullshit here.
					// the final text of the column is set in the method
					// resizeDates(int width) !!!!!!!!!!!!!
					// ===========================================================
					//
					String dateStr = "";
					if (this.dayCount == 1)
						dateStr = TextUtil.formatCase(this.config.getDayFormat().format(
								date));
					else if (this.isMonthView)
						dateStr = this.config.getMonthDateFormat().format(date);
					else
						dateStr = this.config.getWeekDateFormat().format(date);
					/* ------------------------------------------------------- */
					JLabel header = new JLabel(dateStr, SwingConstants.CENTER);
					header.setAlignmentY(2);
					// header.setFont(font);
					header.setToolTipText(toolTipFormat.format(date));
					
					if (this.model.isRedDay(date))
						header.setForeground(Color.RED);
					this.dateHeaders.add(header);
					this.panel.add(header);
					if (this.showExtraDateHeaders) {
						JLabel header2 = new JLabel(this.model.getDateHeader(
								cal.getId(),
								date), SwingConstants.CENTER);
						this.dateHeaders2.add(header2);
						this.panel.add(header2);
					}
					this.dateList.add(date);
					if (i > 0 || j > 0) {
						JLabel line = new JLabel();
						line.setBackground(this.lineColor);
						line.setOpaque(true);
						line.setBackground(this.lineColor);
						if (DateUtil.getDayOfWeek(date) == calendar.getFirstDayOfWeek())
							line.setBackground(this.config.getLineColor2());
						if (this.model.getSelectedCalendars().size() > 1
								&& i == 0)
							line.setBackground(this.config.getLineColor3());
						
						this.panel.add(line);
						this.dateLines.add(line);
					}
					date = DateUtil.getDiffDay(date, +1);
				}
			}
		} else
			this.rowCount = 0;
		
		if (this.showExtraDateHeaders)
			this.rowCount++;
		
		this.panel.add(this.gradientArea);
		this.panel.updateUI();
	}
	
	public JComponent getComponent() {
		return this.panel;
	}
	
	protected class CalHeaderMouseListener extends MouseAdapter {
		
		private Object calId;
		
		public CalHeaderMouseListener(Object calId) {
			this.calId = calId;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			this.maybeShowPopup(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			this.maybeShowPopup(e);
		}
		
		private void maybeShowPopup(MouseEvent e) {
			try {
				if (e.isPopupTrigger()) {
					JPopupMenu popup = ColumnHeaderPanel.this.popupMenuCallback.getCalendarPopupMenu(this.calId);
					if (popup == null)
						return;
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			} catch (Exception exc) {
				throw BizcalException.create(exc);
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// rootPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// rootPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private class Layout implements LayoutManager {
		
		@Override
		public void addLayoutComponent(String name, Component comp) {}
		
		@Override
		public void removeLayoutComponent(Component comp) {}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			try {
				int height = ColumnHeaderPanel.this.refLabel.getPreferredSize().height;
				height = ColumnHeaderPanel.this.rowCount * height;
				int calenderSize = 1;
				if (ColumnHeaderPanel.this.model != null
						&& ColumnHeaderPanel.this.model.getSelectedCalendars() != null)
					calenderSize = ColumnHeaderPanel.this.model.getSelectedCalendars().size();
				/* ---------------------------------------------------- */
				int width = ColumnHeaderPanel.this.dayCount
						* calenderSize
						* DayView.PREFERRED_DAY_WIDTH;
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
		public void layoutContainer(Container parent) {
			try {
				if (ColumnHeaderPanel.this.rowCount == 0)
					return;
				double totWidth = parent.getWidth();
				double dateColWidth = totWidth
						/ ColumnHeaderPanel.this.dateHeaders.size();
				double calColWidth = totWidth
						/ ColumnHeaderPanel.this.calHeaders.size();
				double rowHeight = parent.getHeight()
						/ ColumnHeaderPanel.this.rowCount;
				double dateYPos = 0;
				if (ColumnHeaderPanel.this.calHeaders.size() > 0)
					dateYPos = rowHeight;
				int dateI = 0;
				int dateLineI = 0;
				int dayRowCount = ColumnHeaderPanel.this.showExtraDateHeaders ? 2 : 1;
				for (int i = 0; i < ColumnHeaderPanel.this.model.getSelectedCalendars().size(); i++) {
					if (ColumnHeaderPanel.this.calHeaders.size() > 0) {
						JComponent label = (JComponent) ColumnHeaderPanel.this.calHeaders.get(i);
						label.setBounds(
								(int) (i * calColWidth),
								0,
								(int) calColWidth,
								(int) rowHeight);
					}
					if (ColumnHeaderPanel.this.dayCount >= 1) {
						for (int j = 0; j < ColumnHeaderPanel.this.dayCount; j++) {
							JLabel dateLabel = (JLabel) ColumnHeaderPanel.this.dateHeaders.get(dateI);
							int xpos = (int) (dateI * dateColWidth);
							dateLabel.setBounds(
									xpos,
									(int) dateYPos,
									(int) dateColWidth,
									(int) rowHeight);
							if (ColumnHeaderPanel.this.showExtraDateHeaders) {
								dateLabel = (JLabel) ColumnHeaderPanel.this.dateHeaders2.get(dateI);
								dateLabel.setBounds(
										xpos,
										(int) (dateYPos + rowHeight),
										(int) dateColWidth,
										(int) rowHeight);
							}
							if (j > 0 || i > 0) {
								JLabel line = (JLabel) ColumnHeaderPanel.this.dateLines.get(dateLineI);
								int ypos = (int) dateYPos;
								int height = (int) rowHeight * dayRowCount;
								if (j == 0) {
									ypos = 0;
									height = (int) (rowHeight * (dayRowCount + 1));
								}
								line.setBounds(xpos, ypos, 1, height);
								dateLineI++;
							}
							dateI++;
						}
					}
				}
				ColumnHeaderPanel.this.gradientArea.setBounds(
						0,
						0,
						parent.getWidth(),
						parent.getHeight());
			} catch (Exception e) {
				// throw BizcalException.create(e);
			}
		}
	}
	
	public void setModel(CalendarModel model) {
		this.model = model;
	}
	
	public void setPopupMenuCallback(PopupMenuCallback popupMenuCallback) {
		this.popupMenuCallback = popupMenuCallback;
	}
	
	public void addCalendarListener(CalendarListener listener) {
		this.listener = listener;
	}
	
	private class CloseListener extends MouseAdapter {
		
		private Object calId;
		
		public CloseListener(Object calId) {
			this.calId = calId;
		}
		
		@Override
		public void mouseClicked(MouseEvent event) {
			try {
				ColumnHeaderPanel.this.listener.closeCalendar(this.calId);
			} catch (Exception e) {
				ErrorHandler.handleError(e);
			}
		}
	}
	
	public void setShowExtraDateHeaders(boolean showExtraDateHeaders) {
		this.showExtraDateHeaders = showExtraDateHeaders;
	}
	
	/**
	 * @return the isMonthView
	 */
	public boolean isMonthView() {
		return this.isMonthView;
	}
	
	/**
	 * @param isMonthView
	 *            the isMonthView to set
	 */
	public void setMonthView(boolean isMonthView) {
		this.isMonthView = isMonthView;
	}
	
}
