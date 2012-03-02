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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import bizcal.common.Calendar;
import bizcal.common.CalendarModel;
import bizcal.swing.util.ErrorHandler;
import bizcal.swing.util.GradientArea;
import bizcal.swing.util.TableLayoutPanel;
import bizcal.swing.util.TableLayoutPanel.Cell;
import bizcal.swing.util.TableLayoutPanel.Row;

/**
 * @author Fredrik Bertilsson
 */
public class CalendarList {
	
	private static int GRADIENT_TOP_HEIGHT = 30;
	private CalendarModel _broker;
	private TableLayoutPanel _panel;
	private JSplitPane _splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private Map _checkBoxes;
	private Set _listeners = new HashSet();
	private Object _currCalId;
	private int width;
	private Font font = new Font("Arial", 12, 10);
	private Color primaryColor;
	private Color secondaryColor;
	private PopupMenuCallback popupMenuCallback;
	private JLabel header;
	private JLabel groupName;
	private Object projectId;
	private List calendarList;
	
	public CalendarList(Object projId, CalendarModel broker) throws Exception {
		this.projectId = projId;
		this.primaryColor = new Color(200, 200, 200);
		this.secondaryColor = Color.WHITE;
		this._broker = broker;
		this.init();
	}
	
	public void setBroker(CalendarModel broker) throws Exception {
		this._broker = broker;
		this.init();
	}
	
	private void init() throws Exception {
		if (this._panel != null)
			return;
		this._panel = new TableLayoutPanel();
		this._panel.addComponentListener(new ThisComponentListener());
		
		this.build();
	}
	
	public void refresh() throws Exception {
		this._panel.deleteColumns();
		this._panel.deleteRows();
		this._panel.clear();
		this.build();
		this.fireCalendarsSelected();
	}
	
	private void build() throws Exception {
		this._panel.setBackground(Color.WHITE);
		this._panel.createColumn();
		this._panel.createColumn(TableLayoutPanel.FILL);
		this._panel.createColumn(5);
		Row ro = this._panel.createRow(30);
		Cell cell = ro.createCell();
		cell.setColumnSpan(3);
		
		GradientArea headerGradientArea = new GradientArea(
				GradientArea.TOP_BOTTOM,
				this.secondaryColor,
				this.primaryColor);
		headerGradientArea.setGradientLength(0.5);
		if (this.header == null) {
			this.header = new JLabel("Planeringsgrupp", SwingConstants.CENTER);// (monthFormat.format(cal.getTime()).toUpperCase()
																				// +
																				// " "
																				// +
																				// yearFormat.format(cal.getTime()).toUpperCase(),
																				// JLabel.CENTER);
			this.header.setFont(this.font.deriveFont(Font.BOLD, 12f));
			this.header.setForeground(Color.WHITE);
		}
		headerGradientArea.add(this.header);
		headerGradientArea.setBorderWidth(0.2f);
		headerGradientArea.setBorderColor(Color.LIGHT_GRAY);
		cell.put(headerGradientArea);
		
		ro = this._panel.createRow(20);
		cell = ro.createCell();
		cell.setColumnSpan(3);
		GradientArea topGradientArea = new GradientArea(
				GradientArea.TOP_BOTTOM,
				new Color(255, 255, 255),
				new Color(245, 245, 245));
		topGradientArea.setOpaque(true);
		cell.put(topGradientArea);
		String projectName = "Unknown";
		if (this.projectId != null)
			projectName = "Calendars";
		if (this.groupName == null)
			this.groupName = new JLabel(projectName);
		
		this.groupName.addMouseListener(new ProjectLabelMouseListener(
				this.projectId));
		
		this.groupName.setText(projectName);
		this.groupName.setFont(this.font);
		this.groupName.setForeground(Color.BLACK);
		
		topGradientArea.add(this.groupName);
		
		Map oldCheckBoxes = this._checkBoxes;
		this._checkBoxes = new HashMap();
		TableLayoutPanel checkboxPanel = new TableLayoutPanel();
		checkboxPanel.setBackground(Color.WHITE);
		checkboxPanel.createColumn(TableLayoutPanel.FILL);
		checkboxPanel.createColumn(15);
		this.calendarList = this._broker.getCalendars();
		Iterator i = this.calendarList.iterator();
		boolean first = true;
		while (i.hasNext()) {
			Calendar cal = (Calendar) i.next();
			Object id = cal.getId();
			String name = cal.getSummary();
			Row row;
			row = checkboxPanel.createRow();
			JCheckBox checkBox = new JCheckBox(name);
			checkBox.addMouseListener(new CalMouseListener(id));
			checkBox.setFont(this.font);
			checkBox.setOpaque(false);
			checkBox.setActionCommand("select");
			CheckBoxListener listener = new CheckBoxListener(
					checkBox,
					cal.getId());
			checkBox.addActionListener(listener);
			this._checkBoxes.put(id, checkBox);
			if (first && checkBox.isEnabled()) {
				checkBox.setSelected(true);
				first = false;
			}
			if (oldCheckBoxes != null) {
				JCheckBox oldCheckBox = (JCheckBox) oldCheckBoxes.get(id);
				if (oldCheckBox != null)
					checkBox.setSelected(oldCheckBox.isSelected());
			}
			row.createCell(checkBox);
			if (!cal.isEnabled()) {
				checkBox.setEnabled(cal.isEnabled());
				checkBox.setFont(this.font.deriveFont(Font.ITALIC));
			}
		}
		JScrollPane scrollPanel = new JScrollPane(
				checkboxPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setBorder(null);
		
		this._splitPanel.setBackground(Color.WHITE);
		this._splitPanel.setBorder(null);
		this._splitPanel.setDividerSize(0);
		this._splitPanel.setTopComponent(this._panel);
		this._splitPanel.setBottomComponent(scrollPanel);
	}
	
	public JComponent getComponent() {
		return this._splitPanel;
	}
	
	@SuppressWarnings("unchecked")
	public void addListener(CalendarSelectionListener listener) {
		this._listeners.add(listener);
	}
	
	private class CheckBoxListener implements ActionListener {
		
		public CheckBoxListener(JCheckBox checkBox, Object id) {}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				CalendarList.this.fireCalendarsSelected();
			} catch (Exception e) {
				ErrorHandler.handleError(e);
			}
		}
	}
	
	private void fireCalendarsSelected() throws Exception {
		List selectedCals = this.getSelectedCalendars();
		Iterator i = this._listeners.iterator();
		while (i.hasNext()) {
			CalendarSelectionListener listener = (CalendarSelectionListener) i.next();
			listener.calendarSelected(selectedCals);
		}
	}
	
	public List getSelectedCalendars() throws Exception {
		List selectedCals = new ArrayList();
		Iterator i = this.calendarList.iterator();
		while (i.hasNext()) {
			Calendar cal = (Calendar) i.next();
			JCheckBox checkBox = (JCheckBox) this._checkBoxes.get(cal.getId());
			if (checkBox != null && checkBox.isSelected())
				selectedCals.add(cal);
		}
		return selectedCals;
	}
	
	private class CalMouseListener extends MouseAdapter {
		
		private Object _calId;
		
		public CalMouseListener(Object id) {
			this._calId = id;
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
					CalendarList.this._currCalId = this._calId;
					JPopupMenu popup;
					if (CalendarList.this.popupMenuCallback != null)
						popup = CalendarList.this.popupMenuCallback.getCalendarPopupMenu(this._calId);
					else {
						popup = new JPopupMenu();
						JMenuItem item = new JMenuItem("Ta bort");
						popup.add(item);
						item.addActionListener(new DeleteListener());
					}
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			} catch (Exception exc) {
				ErrorHandler.handleError(exc);
			}
		}
	}
	
	private class ProjectLabelMouseListener extends MouseAdapter {
		
		public ProjectLabelMouseListener(Object id) {}
		
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
					JPopupMenu popup;
					if (CalendarList.this.popupMenuCallback != null)
						popup = CalendarList.this.popupMenuCallback.getProjectPopupMenu(CalendarList.this.projectId);
					else {
						popup = new JPopupMenu();
					}
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			} catch (Exception exc) {
				ErrorHandler.handleError(exc);
			}
		}
	}
	
	private class DeleteListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				CalendarList.this._broker.deleteCalendar(CalendarList.this._currCalId);
				CalendarList.this.refresh();
			} catch (Exception e) {
				ErrorHandler.handleError(e);
			}
			
		}
	}
	
	private class ThisComponentListener extends ComponentAdapter {
		
		@Override
		public void componentResized(ComponentEvent e) {
			// SwingUtilities.invokeLater(new Runnable() {
			// public void run() {
			// /* ================================================== */
			try {
				CalendarList.this.width = CalendarList.this._panel.getWidth();
				CalendarList.this.header.setBounds(
						0,
						0,
						CalendarList.this.width,
						GRADIENT_TOP_HEIGHT);
				CalendarList.this.groupName.setBounds(
						5,
						0,
						CalendarList.this.width - 5,
						20);
				// refresh();
			} catch (Exception exc) {
				exc.printStackTrace();
				ErrorHandler.handleError(exc);
			}
			/* ================================================== */
			// }
			// });
		}
	}
	
	public void setPopupMenuCallback(PopupMenuCallback popupMenuCallback) {
		this.popupMenuCallback = popupMenuCallback;
	}
	
	public void setProjectId(Object projectId) {
		this.projectId = projectId;
	}
}
