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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import bizcal.common.CalendarModel;
import bizcal.swing.util.ErrorHandler;
import bizcal.swing.util.GradientArea;
import bizcal.swing.util.TableLayoutPanel;
import bizcal.swing.util.TableLayoutPanel.Cell;
import bizcal.swing.util.TableLayoutPanel.Row;
import bizcal.util.BizcalException;
import bizcal.util.DateUtil;
import bizcal.util.TextUtil;

/**
 * @author wmjnkal
 * 
 */
public class MonthDateChooser {
	
	private static int WEEK_COLUMN_WIDTH = 20;
	private static int GRADIENT_TOP_HEIGHT = 30;
	private static int WEEKDAY_ROW_HEIGHT = 20;
	private static Color SELECTED_BGCOLOR = new Color(245, 245, 245);
	private static long DAY_INTERVAL = 1;
	private static long WEEK_INTERVAL = 7;
	private static long TWO_WEEK_INTERVAL = 14;
	
	private Font font;
	private Color primaryColor;
	private Color secondaryColor;
	private TableLayoutPanel _panel;
	private int width;
	private int height;
	private JButton previousButton;
	private JButton nextButton;
	private Date navigationDate;
	private CalendarModel _broker;
	private List _listeners = new ArrayList();
	private int selectionInterval = 1;
	private Date _currDate;
	private Map _rowByDayno = new HashMap();
	// private SortedSet redDays;
	private Object projectId;
	
	public MonthDateChooser(Date aDate) throws Exception {
		this.navigationDate = DateUtil.round2Day(aDate);
		this._currDate = DateUtil.round2Day(aDate);
		this._panel = new TableLayoutPanel();
		this._panel.addComponentListener(new ThisComponentListener());
		this._panel.setMinimumSize(new Dimension(180, 150));
		this.primaryColor = new Color(200, 200, 200);
		this.secondaryColor = Color.WHITE;
		this.font = UIManager.getFont("Label.font");
	}
	
	private void loadRedDays() throws Exception {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTime(this.navigationDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		// Date start = cal.getTime();
		cal.add(Calendar.MONTH, +1);
	}
	
	public void setBroker(CalendarModel aBroker) throws Exception {
		this._broker = aBroker;
		this.loadRedDays();
	}
	
	public Font getFont() {
		return this.font;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setNavigationDate(Date aDate) throws Exception {
		this.navigationDate = DateUtil.round2Day(aDate);
		this.loadRedDays();
	}
	
	public Date getNavigationDate() throws Exception {
		return this.navigationDate;
	}
	
	public JComponent getComponent() throws Exception {
		this.init();
		return this._panel;
	}
	
	private void init() throws Exception {
		if (this._panel != null)
			return;
		
		this._panel = new TableLayoutPanel();
		this.refresh();
		
	}
	
	public void refresh() throws Exception {
		if (this._panel == null)
			this._panel = new TableLayoutPanel();
		this._panel.deleteRows();
		this._panel.deleteColumns();
		this._panel.clear();
		this._rowByDayno.clear();
		
		this._panel.setBackground(Color.WHITE);
		
		this._panel.createColumn(WEEK_COLUMN_WIDTH);
		for (int i = 0; i < 7; i++)
			this._panel.createColumn(TableLayoutPanel.FILL);
		
		Row stepperRow = this._panel.createRow(GRADIENT_TOP_HEIGHT);
		Cell cell = stepperRow.createCell();
		cell.setColumnSpan(8);
		
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTime(this.navigationDate);
		DateFormat monthFormat = new SimpleDateFormat(
				"MMMM",
				Locale.getDefault());
		DateFormat yearFormat = new SimpleDateFormat(
				"yyyy",
				Locale.getDefault());
		
		ImageIcon previousIcon = this.createImageIcon("util/images/left.gif");
		this.previousButton = new JButton(previousIcon);
		this.previousButton.setBounds(4, 5, 20, 20);
		this.previousButton.setOpaque(false);
		this.previousButton.addActionListener(new StepperListener());
		ImageIcon nextIcon = this.createImageIcon("util/images/right.gif");
		this.nextButton = new JButton(nextIcon);
		this.nextButton.setBounds(this.width - 24, 5, 20, 20);
		this.nextButton.setOpaque(false);
		this.nextButton.addActionListener(new StepperListener());
		GradientArea topGradientArea = new GradientArea(
				GradientArea.TOP_BOTTOM,
				this.secondaryColor,
				this.primaryColor);
		topGradientArea.setGradientLength(0.5);
		JLabel monthYear = new JLabel(
				TextUtil.formatCase(monthFormat.format(cal.getTime()))
						+ " "
						+ yearFormat.format(cal.getTime()),
				SwingConstants.CENTER);
		// Calendar.get(Calendar.MONTH)
		monthYear.setForeground(Color.WHITE);
		monthYear.setFont(this.font.deriveFont(Font.BOLD, 12f));
		monthYear.setBounds(0, 0, this.width, GRADIENT_TOP_HEIGHT);
		topGradientArea.add(this.previousButton);
		topGradientArea.add(this.nextButton);
		topGradientArea.add(monthYear);
		topGradientArea.setBorderWidth(0.2f);
		topGradientArea.setBorderColor(Color.LIGHT_GRAY);
		cell.put(topGradientArea);
		
		Row headerRow = this._panel.createRow(WEEKDAY_ROW_HEIGHT);
		GradientArea emptyHeaderGradientArea = new GradientArea(
				GradientArea.TOP_BOTTOM,
				new Color(255, 255, 255),
				new Color(245, 245, 245));
		emptyHeaderGradientArea.setBorderWidth(0.1f);
		headerRow.createCell(
				emptyHeaderGradientArea,
				TableLayoutPanel.FULL,
				TableLayoutPanel.FULL);
		
		int month = cal.get(Calendar.MONTH);
		// month+=1;
		int weekday = cal.getFirstDayOfWeek();
		
		Calendar cal2 = Calendar.getInstance(Locale.getDefault());
		cal2.setTime(cal.getTime());
		for (int i = 0; i < 7; i++) {
			cal2.set(Calendar.DAY_OF_WEEK, weekday);
			// DateFormat format = new SimpleDateFormat("EEE",
			// LocaleBroker.getLocale());
			String formatedString = StringLengthFormater.formatDateString(
					cal2.getTime(),
					this.font,
					(this.width - WEEK_COLUMN_WIDTH - 10) / 7,
					null);
			JLabel label = new JLabel(formatedString, SwingConstants.CENTER);
			label.setForeground(Color.BLACK);
			label.setBounds(0, 0, (this.width) / 7, WEEKDAY_ROW_HEIGHT);
			label.setFont(this.font.deriveFont(Font.BOLD));
			
			GradientArea headerGradientArea = new GradientArea(
					GradientArea.TOP_BOTTOM,
					new Color(255, 255, 255),
					new Color(240, 240, 240));
			headerGradientArea.add(label);
			headerGradientArea.setGradientLength(0.5);
			headerGradientArea.setBorder(false);
			headerGradientArea.setBorderWidth(0.1f);
			// headerGradientArea.setBorderColor(Color.LIGHT_GRAY);
			
			headerRow.createCell(
					headerGradientArea,
					TableLayoutPanel.FULL,
					TableLayoutPanel.FULL);
			weekday++;
			if (weekday >= 8) {
				weekday -= 7;
			}
		}
		
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int col = cal.get(Calendar.DAY_OF_WEEK);
		col -= cal.getFirstDayOfWeek();
		if (col < 0)
			col += 7;
		
		int lastDayOfWeek = cal.getFirstDayOfWeek();
		lastDayOfWeek--;
		if (lastDayOfWeek < 1)
			lastDayOfWeek += 7;
		
		Row row = this._panel.createRow(TableLayoutPanel.FILL);
		
		GradientArea weekGradientArea = new GradientArea(
				GradientArea.LEFT_RIGHT,
				new Color(255, 255, 255),
				new Color(245, 245, 245));
		weekGradientArea.setBorderWidth(0.1f);
		weekGradientArea.add(this.createCalCell(cal, false));
		row.createCell(
				weekGradientArea,
				TableLayoutPanel.FULL,
				TableLayoutPanel.FULL);
		
		for (int i = 0; i < col; i++) {
			row.createCell();
		}
		
		List cellList = new ArrayList();
		while (cal.get(Calendar.MONTH) == month) {
			JLabel label = this.createCalCell(cal, true);
			cellList.add(label);
			this._rowByDayno.put(label.getText(), cellList);
			row.createCell(label, TableLayoutPanel.FULL, TableLayoutPanel.FULL);
			if (cal.get(Calendar.DAY_OF_WEEK) == lastDayOfWeek) {
				row = this._panel.createRow(TableLayoutPanel.FILL);
				cal.add(Calendar.DAY_OF_MONTH, 1);
				
				weekGradientArea = new GradientArea(
						GradientArea.LEFT_RIGHT,
						new Color(255, 255, 255),
						new Color(245, 245, 245));
				weekGradientArea.setBorderWidth(0.1f);
				weekGradientArea.add(this.createCalCell(cal, false));
				row.createCell(
						weekGradientArea,
						TableLayoutPanel.FULL,
						TableLayoutPanel.FULL);
				
				cellList = new ArrayList();
			} else
				cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		this._panel.updateUI();
		
	}
	
	private ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = MonthDateChooser.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	/**
	 * true f�r dag, false f�r vecka
	 * 
	 * @param cal
	 * @return
	 */
	private JLabel createCalCell(Calendar cal, boolean dag) throws Exception {
		JLabel label = new JLabel("", SwingConstants.CENTER);
		label.setForeground(Color.black);
		
		if (dag) {
			label.addMouseListener(new DayMouseListener());
			label.setFont(this.font);
			label.setOpaque(true);
			Color bgColor = Color.WHITE;
			boolean current = false;
			if (this._currDate != null) {
				if (this.selectionInterval <= DAY_INTERVAL) {
					current = cal.getTime().getTime() == this._currDate.getTime();
					System.err.println("createCalCell: " + this._currDate);
				} else if (this.selectionInterval <= TWO_WEEK_INTERVAL) {
					Calendar cal2 = Calendar.getInstance(Locale.getDefault());
					cal2.setTime(cal.getTime());
					int week = cal2.get(Calendar.WEEK_OF_YEAR);
					cal2.setTime(this._currDate);
					int currWeek = cal2.get(Calendar.WEEK_OF_YEAR);
					current = (week == currWeek);
					if (this.selectionInterval == TWO_WEEK_INTERVAL) {
						if (!current)
							current = (currWeek == (week - 1));
					}
				}
			}
			if (current)
				bgColor = SELECTED_BGCOLOR;
			label.setBackground(bgColor);
			label.setText("" + cal.get(Calendar.DAY_OF_MONTH));
			/*
			 * if(redDays.contains(DateUtil.round2Day(cal.getTime())))
			 * label.setForeground(Color.RED);
			 */
			if (DateUtil.round2Day(cal.getTime()).equals(
					DateUtil.round2Day(new Date())))
				label.setFont(this.font.deriveFont(Font.BOLD));
		} else {
			label.addMouseListener(new WeekMouseListener());
			label.setFont(this.font.deriveFont(Font.BOLD));
			label.setText("" + cal.get(Calendar.WEEK_OF_YEAR));
			label.setBounds(
					0,
					0,
					WEEK_COLUMN_WIDTH,
					(this.height - (GRADIENT_TOP_HEIGHT + WEEKDAY_ROW_HEIGHT)) / 5);// obs
																					// ej
																					// l�st
			GradientArea weekGradientArea = new GradientArea(
					GradientArea.LEFT_RIGHT,
					new Color(255, 255, 255),
					new Color(245, 245, 245));
			weekGradientArea.setBorderWidth(0.1f);
			weekGradientArea.add(label);
		}
		return label;
	}
	
	private class StepperListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				Calendar calendar = Calendar.getInstance(Locale.getDefault());
				calendar.setTime(MonthDateChooser.this.getNavigationDate());
				
				if (event.getSource() == MonthDateChooser.this.previousButton) {
					calendar.add(Calendar.MONTH, -1);
				}
				if (event.getSource() == MonthDateChooser.this.nextButton) {
					calendar.add(Calendar.MONTH, 1);
				}
				MonthDateChooser.this.setNavigationDate(calendar.getTime());
				// fireDateSelected(calendar.getTime());
				MonthDateChooser.this.refresh();
			} catch (Exception e) {
				ErrorHandler.handleError(e);
			}
		}
	}
	
	private class DayMouseListener extends MouseAdapter {
		
		public JLabel label;
		private Color backgroundw1;
		private Color backgroundw2;
		
		@Override
		public void mouseEntered(MouseEvent e) {
			this.label = (JLabel) e.getSource();
			this.label.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.backgroundw1 = this.label.getBackground();
			if (MonthDateChooser.this.selectionInterval <= DAY_INTERVAL) {
				this.label.setBackground(SELECTED_BGCOLOR);
			} else if (MonthDateChooser.this.selectionInterval <= TWO_WEEK_INTERVAL) {
				int weeks = 0;
				if (MonthDateChooser.this.selectionInterval == TWO_WEEK_INTERVAL)
					weeks = 1;
				for (int iw = 0; iw <= weeks; iw++) {
					Integer intDayNo = new Integer(this.label.getText());
					// intDayNo = new Integer(intDayNo.intValue() + (7*iw));
					intDayNo = Integer.valueOf(intDayNo.intValue() + (7 * iw));
					while (!MonthDateChooser.this._rowByDayno.containsKey(intDayNo.toString()))
						intDayNo = Integer.valueOf(intDayNo.intValue() - 1);
					
					List cellList = (List) MonthDateChooser.this._rowByDayno.get(intDayNo.toString());
					Iterator i = cellList.iterator();
					while (i.hasNext()) {
						JLabel label2 = (JLabel) i.next();
						if (iw == 1)
							this.backgroundw2 = label2.getBackground();
						label2.setBackground(SELECTED_BGCOLOR);
					}
				}
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			this.label = (JLabel) e.getSource();
			this.label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			
			if (MonthDateChooser.this.selectionInterval <= DAY_INTERVAL) {
				this.label.setBackground(this.backgroundw1);
			} else if (MonthDateChooser.this.selectionInterval <= TWO_WEEK_INTERVAL) {
				int weeks = 0;
				if (MonthDateChooser.this.selectionInterval == TWO_WEEK_INTERVAL)
					weeks = 1;
				for (int iw = 0; iw <= weeks; iw++) {
					Integer intDayNo = new Integer(this.label.getText());
					intDayNo = Integer.valueOf(intDayNo.intValue() + (7 * iw));
					while (!MonthDateChooser.this._rowByDayno.containsKey(intDayNo.toString()))
						intDayNo = Integer.valueOf(intDayNo.intValue() - 1);
					
					List cellList = (List) MonthDateChooser.this._rowByDayno.get(intDayNo.toString());
					Iterator i = cellList.iterator();
					while (i.hasNext()) {
						JLabel label2 = (JLabel) i.next();
						if (iw == 1)
							label2.setBackground(this.backgroundw2);
						else
							label2.setBackground(this.backgroundw1);
					}
				}
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				Calendar cal = Calendar.getInstance();
				cal.setTime(MonthDateChooser.this.navigationDate);
				this.label = (JLabel) e.getSource();
				cal.set(
						Calendar.DAY_OF_MONTH,
						Integer.parseInt(this.label.getText()));
				MonthDateChooser.this.selectDate(cal.getTime());
				MonthDateChooser.this.refresh();
			} catch (Exception ex) {
				throw BizcalException.create(ex);
			}
		}
	}
	
	private class WeekMouseListener extends MouseAdapter {
		
		public JLabel label;
		
		@Override
		public void mouseEntered(MouseEvent e) {
			this.label = (JLabel) e.getSource();
			this.label.setCursor(new Cursor(Cursor.HAND_CURSOR));
			
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			this.label = (JLabel) e.getSource();
			this.label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				this.label = (JLabel) e.getSource();
				
			}
			
			catch (Exception ex) {
				throw BizcalException.create(ex);
			}
		}
	}
	
	private class ThisComponentListener extends ComponentAdapter {
		
		@Override
		public void componentResized(ComponentEvent e) {
			// SwingUtilities.invokeLater(new Runnable() {
			// public void run() {
			/* ================================================== */
			try {
				
				MonthDateChooser.this.width = MonthDateChooser.this._panel.getWidth();
				MonthDateChooser.this.height = MonthDateChooser.this._panel.getHeight();
				MonthDateChooser.this.refresh();
				
			} catch (Exception exc) {
				exc.printStackTrace();
				ErrorHandler.handleError(exc);
			}
			/* ================================================== */
			// }
			// });
		}
	}
	
	/*
	 * public void addDateListener(DateListener l)
	 * {
	 * _listeners.add(l);
	 * }
	 */
	
	private void selectDate(Date date) throws Exception {
		this._currDate = DateUtil.round2Day(date);
		// fireDateSelected(date);
	}
	
	/*
	 * private void fireDateSelected(Date date)
	 * throws Exception
	 * {
	 * Iterator i = _listeners.iterator();
	 * while (i.hasNext()) {
	 * DateListener l = (DateListener) i.next();
	 * l.dateSelected(date);
	 * }
	 * }
	 */
	
	public void setSelectionInterval(int dayCount) throws Exception {
		this.selectionInterval = dayCount;
		this.refresh();
	}
	
	public void setProjectId(Object projectId) {
		this.projectId = projectId;
	}
	
}
