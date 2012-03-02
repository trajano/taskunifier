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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bizcal.swing.util.ErrorHandler;
import bizcal.swing.util.TableLayoutPanel;
import bizcal.swing.util.TableLayoutPanel.Row;
import bizcal.util.BizcalException;
import bizcal.util.DateUtil;
import bizcal.util.LocaleBroker;
import bizcal.util.StreamCopier;
import bizcal.util.TextUtil;

public class DayStepper {
	
	private TableLayoutPanel panel;
	private Calendar cal;
	private JComboBox yearCombo;
	private JComboBox monthCombo;
	private JComboBox dayCombo;
	private List listeners = new ArrayList();
	private String fastRewindArrow = "/bizcal/res/go_fb.gif";
	private String prevArrow = "/bizcal/res/go_back.gif";
	private String nextArrow = "/bizcal/res/go_forward.gif";
	private String fastForwardArrow = "/bizcal/res/go_ff.gif";
	
	public DayStepper() throws Exception {
		this.cal = Calendar.getInstance(LocaleBroker.getLocale());
		this.cal.setTime(DateUtil.round2Day(new Date()));
		this.panel = new TableLayoutPanel();
		this.panel.createColumn();
		this.panel.createColumn();
		this.panel.createColumn(TableLayoutPanel.FILL);
		this.panel.createColumn();
		this.panel.createColumn(10);
		this.panel.createColumn();
		this.panel.createColumn(10);
		this.panel.createColumn();
		this.panel.createColumn(TableLayoutPanel.FILL);
		this.panel.createColumn();
		this.panel.createColumn();
		Row row = this.panel.createRow();
		ActionListener listener;
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					DayStepper.this.previousMonth();
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
					DayStepper.this.previous();
				} catch (Exception e) {
					ErrorHandler.handleError(e);
				}
				
			}
		};
		row.createCell(this.createButton(this.prevArrow, listener));
		row.createCell();
		this.initYearCombo();
		this.initMonthCombo();
		this.initDayCombo();
		this.setCombos();
		row.createCell(this.yearCombo);
		row.createCell();
		row.createCell(this.monthCombo);
		row.createCell();
		row.createCell(this.dayCombo);
		row.createCell();
		
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					DayStepper.this.next();
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
					DayStepper.this.nextMonth();
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
			this.yearCombo.addItem(Integer.valueOf(i));
		this.yearCombo.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent event) {
				try {
					int year = ((Integer) DayStepper.this.yearCombo.getSelectedItem()).intValue();
					DayStepper.this.cal.set(Calendar.YEAR, year);
				} catch (Exception e) {
					throw BizcalException.create(e);
				}
			}
		});
	}
	
	private void initMonthCombo() throws Exception {
		this.monthCombo = new JComboBox();
		DateFormat format = new SimpleDateFormat(
				"MMMMM",
				LocaleBroker.getLocale());
		int orgMonth = this.cal.get(Calendar.MONTH);
		for (int i = 0; i < 12; i++) {
			this.cal.set(Calendar.MONTH, i);
			MonthWrapper wrapper = new MonthWrapper(
					i,
					TextUtil.formatCase(format.format(this.cal.getTime())));
			this.monthCombo.addItem(wrapper);
		}
		this.cal.set(Calendar.MONTH, orgMonth);
		this.monthCombo.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent event) {
				try {
					int month = ((MonthWrapper) DayStepper.this.monthCombo.getSelectedItem()).getValue();
					DayStepper.this.cal.set(Calendar.MONTH, month);
					DayStepper.this.refreshDayCombo();
				} catch (Exception e) {
					throw BizcalException.create(e);
				}
			}
		});
	}
	
	private void initDayCombo() throws Exception {
		this.dayCombo = new JComboBox();
		int currDay = this.cal.get(Calendar.DAY_OF_MONTH);
		this.refreshDayCombo();
		this.dayCombo.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent event) {
				Integer selectedItem = (Integer) DayStepper.this.dayCombo.getSelectedItem();
				if (selectedItem == null)
					return;
				int day = ((Integer) DayStepper.this.dayCombo.getSelectedItem()).intValue();
				DayStepper.this.cal.set(Calendar.DAY_OF_MONTH, day);
			}
		});
		this.cal.set(Calendar.DAY_OF_MONTH, currDay);
	}
	
	private void setCombos() {
		int year = this.cal.get(Calendar.YEAR);
		this.yearCombo.setSelectedItem(Integer.valueOf(year));
		int month = this.cal.get(Calendar.MONTH);
		this.monthCombo.setSelectedIndex(month);
		int day = this.cal.get(Calendar.DAY_OF_MONTH);
		this.dayCombo.setSelectedItem(Integer.valueOf(day));
	}
	
	private void refreshDayCombo() {
		Integer selectedItem = (Integer) this.dayCombo.getSelectedItem();
		int dayNo = 1;
		if (selectedItem != null)
			dayNo = selectedItem.intValue();
		this.dayCombo.removeAllItems();
		int maxDayNo = this.cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for (int i = 1; i <= maxDayNo; i++) {
			this.dayCombo.addItem(Integer.valueOf(i));
		}
		this.dayCombo.setSelectedItem(Integer.valueOf(dayNo));
	}
	
	public JComponent getComponent() {
		return this.panel;
	}
	
	public Date getDate() {
		return this.cal.getTime();
	}
	
	private void next() throws Exception {
		this.cal.add(Calendar.DAY_OF_MONTH, +1);
		this.setCombos();
		this.fireStateChanged();
	}
	
	private void previous() throws Exception {
		this.cal.add(Calendar.DAY_OF_MONTH, -1);
		this.setCombos();
		this.fireStateChanged();
	}
	
	private void nextMonth() throws Exception {
		this.cal.add(Calendar.MONTH, +1);
		this.setCombos();
		this.fireStateChanged();
	}
	
	private void previousMonth() throws Exception {
		this.cal.add(Calendar.MONTH, -1);
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
		this.cal.setTime(DateUtil.round2Day(date));
		this.setCombos();
		this.fireStateChanged();
	}
	
	private class MonthWrapper {
		
		private int value;
		private String caption;
		
		public MonthWrapper(int value, String caption) {
			this.value = value;
			this.caption = caption;
		}
		
		@Override
		public String toString() {
			return this.caption;
		}
		
		public int getValue() {
			return this.value;
		}
	}
}
