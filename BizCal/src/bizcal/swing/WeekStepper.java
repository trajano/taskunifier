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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bizcal.common.Bundle;
import bizcal.swing.util.ErrorHandler;
import bizcal.swing.util.TableLayoutPanel;
import bizcal.swing.util.TableLayoutPanel.Row;
import bizcal.util.BizcalException;
import bizcal.util.DateUtil;
import bizcal.util.LocaleBroker;
import bizcal.util.StreamCopier;
import bizcal.util.TextUtil;

public class WeekStepper {
	
	private TableLayoutPanel panel;
	private Calendar cal;
	private JComboBox yearCombo;
	private JComboBox weekCombo;
	private JLabel textLabel;
	private List listeners = new ArrayList();
	private String fastRewindArrow = "/bizcal/res/go_fb.gif";
	private String prevArrow = "/bizcal/res/go_back.gif";
	private String nextArrow = "/bizcal/res/go_forward.gif";
	private String fastForwardArrow = "/bizcal/res/go_ff.gif";
	
	public WeekStepper() throws Exception {
		this.cal = Calendar.getInstance(LocaleBroker.getLocale());
		this.cal.setTime(DateUtil.round2Week(new Date()));
		this.panel = new TableLayoutPanel();
		this.panel.createColumn();
		this.panel.createColumn();
		this.panel.createColumn(10);
		this.panel.createColumn();
		this.panel.createColumn(10);
		this.panel.createColumn();
		this.panel.createColumn(10);
		this.panel.createColumn(TableLayoutPanel.FILL);
		this.panel.createColumn();
		this.panel.createColumn();
		Row row = this.panel.createRow();
		ActionListener listener;
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					WeekStepper.this.previousYear();
				} catch (Exception e) {
					ErrorHandler.handleError(e);
				}
			}
		};
		row.createCell(this.createButton(this.fastRewindArrow, listener));
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					WeekStepper.this.previous();
				} catch (Exception e) {
					ErrorHandler.handleError(e);
				}
				
			}
		};
		row.createCell(this.createButton(this.prevArrow, listener));
		row.createCell();
		this.initYearCombo();
		this.initWeekCombo();
		this.textLabel = new JLabel();
		this.setCombos();
		row.createCell(this.yearCombo);
		row.createCell();
		row.createCell(this.weekCombo);
		row.createCell();
		row.createCell(this.textLabel);
		
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					WeekStepper.this.next();
				} catch (Exception e) {
					ErrorHandler.handleError(e);
				}
			}
		};
		row.createCell(this.createButton(this.nextArrow, listener));
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					WeekStepper.this.nextYear();
				} catch (Exception e) {
					ErrorHandler.handleError(e);
				}
			}
		};
		row.createCell(this.createButton(this.fastForwardArrow, listener));
	}
	
	private void initYearCombo() throws Exception {
		this.yearCombo = new JComboBox();
		int year = this.cal.get(Calendar.YEAR);
		for (int i = year - 1; i < year + 4; i++)
			// yearCombo.addItem(new Integer(i));
			this.yearCombo.addItem(Integer.valueOf(i));
		this.yearCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					int year = ((Integer) WeekStepper.this.yearCombo.getSelectedItem()).intValue();
					WeekStepper.this.cal.set(Calendar.YEAR, year);
					WeekStepper.this.refreshWeekCombo();
					WeekStepper.this.setLabel();
				} catch (Exception e) {
					throw BizcalException.create(e);
				}
			}
		});
	}
	
	private void initWeekCombo() throws Exception {
		this.weekCombo = new JComboBox();
		int currWeek = this.cal.get(Calendar.WEEK_OF_YEAR);
		this.refreshWeekCombo();
		this.weekCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					Integer selectedItem = (Integer) WeekStepper.this.weekCombo.getSelectedItem();
					if (selectedItem == null)
						return;
					int week = ((Integer) WeekStepper.this.weekCombo.getSelectedItem()).intValue();
					WeekStepper.this.cal.set(Calendar.WEEK_OF_YEAR, week);
					WeekStepper.this.setLabel();
				} catch (Exception e) {
					throw BizcalException.create(e);
				}
			}
		});
		this.cal.set(Calendar.WEEK_OF_YEAR, currWeek);
	}
	
	private void setCombos() throws Exception {
		int year = this.cal.get(Calendar.YEAR);
		this.yearCombo.setSelectedItem(Integer.valueOf(year));
		int week = this.cal.get(Calendar.WEEK_OF_YEAR);
		if (this.weekCombo.getItemCount() < week)
			week = this.weekCombo.getItemCount();
		this.weekCombo.setSelectedIndex(week - 1);
		this.setLabel();
	}
	
	private void setLabel() throws Exception {
		this.textLabel.setText(this.getText());
	}
	
	private void refreshWeekCombo() {
		int currWeek = this.cal.get(Calendar.WEEK_OF_YEAR);
		this.weekCombo.removeAllItems();
		int maxWeekNo = this.cal.getActualMaximum(Calendar.WEEK_OF_YEAR);
		for (int i = 1; i <= maxWeekNo; i++) {
			this.weekCombo.addItem(Integer.valueOf(i));
		}
		this.cal.set(Calendar.WEEK_OF_YEAR, currWeek);
	}
	
	public JComponent getComponent() {
		return this.panel;
	}
	
	public Date getDate() {
		return this.cal.getTime();
	}
	
	private void next() throws Exception {
		this.cal.add(Calendar.WEEK_OF_YEAR, +1);
		this.setCombos();
		this.fireStateChanged();
	}
	
	private void previous() throws Exception {
		this.cal.add(Calendar.WEEK_OF_YEAR, -1);
		this.setCombos();
		this.fireStateChanged();
	}
	
	private void nextYear() throws Exception {
		this.cal.add(Calendar.YEAR, +1);
		this.cal.set(Calendar.DAY_OF_WEEK, this.cal.getFirstDayOfWeek());
		this.setCombos();
		this.fireStateChanged();
	}
	
	private void previousYear() throws Exception {
		this.cal.add(Calendar.YEAR, -1);
		this.cal.set(Calendar.DAY_OF_WEEK, this.cal.getFirstDayOfWeek());
		this.setCombos();
		this.fireStateChanged();
	}
	
	public void addChangeListener(ChangeListener listener) {
		this.listeners.add(listener);
	}
	
	private void fireStateChanged() throws Exception {
		ChangeEvent event = new ChangeEvent(this);
		Iterator i = this.listeners.iterator();
		while (i.hasNext()) {
			ChangeListener l = (ChangeListener) i.next();
			l.stateChanged(event);
		}
	}
	
	private Icon getIcon(String filename) throws Exception {
		byte[] bytes = StreamCopier.copyToByteArray(this.getClass().getResourceAsStream(
				filename));
		return new ImageIcon(bytes);
	}
	
	private JComponent createButton(String filename, ActionListener listener)
			throws Exception {
		JButton button = new JButton(this.getIcon(filename));
		button.addActionListener(listener);
		return button;
	}
	
	public void setDate(Date date) throws Exception {
		this.cal.setTime(DateUtil.round2Week(date));
		this.setCombos();
		this.fireStateChanged();
	}
	
	private String getText() throws Exception {
		StringBuffer str = new StringBuffer();
		str.append(Bundle.translate("Week")
				+ " "
				+ this.cal.get(Calendar.WEEK_OF_YEAR)
				+ ": ");
		Calendar cal2 = Calendar.getInstance(LocaleBroker.getLocale());
		int currYear = cal2.get(Calendar.YEAR);
		int year = this.cal.get(Calendar.YEAR);
		if (currYear != year)
			str.append(year + " ");
		int month = this.cal.get(Calendar.MONTH);
		DateFormat monthFormat = new SimpleDateFormat("MMM");
		str.append(TextUtil.formatCase(monthFormat.format(this.cal.getTime()))
				+ " ");
		int day = this.cal.get(Calendar.DAY_OF_MONTH);
		str.append(day);
		str.append(" - ");
		cal2.setTime(this.cal.getTime());
		cal2.add(Calendar.DAY_OF_WEEK, +6);
		int month2 = cal2.get(Calendar.MONTH);
		if (month != month2)
			str.append(TextUtil.formatCase(monthFormat.format(cal2.getTime()))
					+ " ");
		day = cal2.get(Calendar.DAY_OF_MONTH);
		str.append(day);
		return str.toString();
	}
}
