/*******************************************************************************
 * Copyright (c) 2007 by CRP Henri TUDOR - SANTEC LUXEMBOURG 
 * check http://www.santec.tudor.lu for more information
 *  
 * Contributor(s):
 * Johannes Hermen  johannes.hermen(at)tudor.lu                            
 * Martin Heinemann martin.heinemann(at)tudor.lu  
 *  
 * This library is free software; you can redistribute it and/or modify it  
 * under the terms of the GNU Lesser General Public License (version 2.1)
 * as published by the Free Software Foundation.
 * 
 * This software is distributed in the hope that it will be useful, but     
 * WITHOUT ANY WARRANTY; without even the implied warranty of               
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU        
 * Lesser General Public License for more details.                          
 * 
 * You should have received a copy of the GNU Lesser General Public         
 * License along with this library; if not, write to the Free Software      
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *******************************************************************************/
package lu.tudor.santec.bizcal;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import lu.tudor.santec.bizcal.listeners.CalendarManagementListener;
import lu.tudor.santec.bizcal.listeners.DateListener;
import lu.tudor.santec.bizcal.listeners.NamedCalendarListener;
import lu.tudor.santec.bizcal.resources.BizCalTranslations;
import lu.tudor.santec.bizcal.views.DayViewPanel;
import lu.tudor.santec.bizcal.views.ListViewPanel;
import lu.tudor.santec.bizcal.views.MonthViewPanel;
import lu.tudor.santec.bizcal.widgets.BubbleLabel;
import lu.tudor.santec.bizcal.widgets.ButtonPanel;
import lu.tudor.santec.bizcal.widgets.CheckBoxPanel;
import lu.tudor.santec.bizcal.widgets.NaviBar;

import org.jdesktop.swingx.JXMonthView;

import bizcal.common.Event;
import bizcal.swing.DayView;
import bizcal.swing.PopupMenuCallback;
import bizcal.util.DateUtil;

public class CalendarPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	Vector<Action> functionsActionsVector = new Vector<Action>();
	
	LinkedHashMap<String, AbstractCalendarView> calendarViews = new LinkedHashMap<String, AbstractCalendarView>();
	
	private NaviBar naviBar;
	private JXMonthView dayChooser;
	
	private ButtonPanel viewsButtonPanel;
	
	private ButtonPanel calendarButtonPanel;
	
	private JPanel viewsPanel;
	
	private CardLayout viewsCardLayout;
	
	private transient ActionListener viewListener;
	
	private Vector<DateListener> dateListeners = new Vector<DateListener>();
	
	private Vector<NamedCalendarListener> calendarListeners = new Vector<NamedCalendarListener>();
	
	private List<CalendarManagementListener> calendarManagementListeners = new ArrayList<CalendarManagementListener>();
	
	private Date date = new Date();
	
	private LinkedHashMap<NamedCalendar, CheckBoxPanel> namedCalendars = new LinkedHashMap<NamedCalendar, CheckBoxPanel>();
	
	private JPopupMenu popup;
	
	private AbstractAction modifyCalendarAction;
	
	private AbstractAction newCalendarAction;
	
	private AbstractAction deleteCalendarAction;
	
	private ButtonPanel functionsButtonPanel;
	
	protected AbstractCalendarView currentView;
	
	private JSlider slider;
	
	private NamedCalendar lastShowingCalendarBeforeShowAll = null;
	
	private static Color headerColor = new Color(138, 155, 190);
	
	private ButtonGroup calendarButtonGroup = new ButtonGroup();
	
	private JPanel leftPanel;
	
	public CalendarPanel() {
		BizCalTranslations.setLocale(Locale.getDefault());
		
		this.setLayout(new BorderLayout(3, 3));
		this.setBackground(Color.WHITE);
		
		this.createMainPanel();
		this.add(this.viewsPanel, BorderLayout.CENTER);
		
		this.createNaviBar();
		
		this.leftPanel = new JPanel(new BorderLayout());
		
		JScrollPane naviBarPanel = new JScrollPane(this.naviBar);
		naviBarPanel.setBorder(null);
		
		this.leftPanel.add(naviBarPanel);
		
		this.add(this.leftPanel, BorderLayout.WEST);
		
		this.initPopup();
		
	}
	
	public void setTopLeftPanel(JPanel panel) {
		this.leftPanel.add(panel, BorderLayout.NORTH);
	}
	
	public void setBottomLeftPanel(JPanel panel) {
		this.leftPanel.add(panel, BorderLayout.SOUTH);
	}
	
	/**
	 * creates the main panel with the differen view
	 */
	private void createMainPanel() {
		this.viewsCardLayout = new CardLayout();
		this.viewsPanel = new JPanel(this.viewsCardLayout);
		this.viewsPanel.setOpaque(false);
	}
	
	/**
	 * creates the right toolbar
	 */
	private void createNaviBar() {
		this.naviBar = new NaviBar();
		/* ================================================== */
		
		// create the view buttons
		this.viewsButtonPanel = new ButtonPanel(
				BizCalTranslations.getString("bizcal.views"),
				headerColor,
				4,
				new Vector<AbstractButton>());
		this.naviBar.addButtonPanel(this.viewsButtonPanel, NaviBar.TOP);
		/* ------------------------------------------------------- */
		this.viewListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CalendarPanel.this.viewsCardLayout.show(
						CalendarPanel.this.viewsPanel,
						e.getActionCommand());
				CalendarPanel.this.currentView = CalendarPanel.this.calendarViews.get(e.getActionCommand());
			}
		};
		
		// create the functions buttons
		this.functionsButtonPanel = new ButtonPanel(
				BizCalTranslations.getString("bizcal.functions"),
				headerColor,
				1,
				this.functionsActionsVector,
				false,
				true);
		this.functionsButtonPanel.setContentLayout(new BorderLayout());
		this.naviBar.addButtonPanel(this.functionsButtonPanel, NaviBar.FILL);
		
		// create the calendar buttons
		this.calendarButtonPanel = new ButtonPanel(
				BizCalTranslations.getString("bizcal.calendar"),
				headerColor,
				1,
				new Vector<AbstractButton>());
		
		this.naviBar.addButtonPanel(this.calendarButtonPanel, NaviBar.BOTTOM);
		
		// this.calendarButtonPanel.addComponent(new CheckBoxPanel("text",
		// Color.RED));
		
		// create the datechooser
		JPanel daychooserPanel = new JPanel();
		daychooserPanel.setLayout(new BorderLayout(0, 2));
		daychooserPanel.setOpaque(false);
		daychooserPanel.setBackground(Color.WHITE);
		JLabel label = new BubbleLabel(" "
				+ BizCalTranslations.getString("bizcal.chooseDay")
				+ ":");
		label.setBackground(headerColor);
		label.setPreferredSize(new Dimension(22, 22));
		daychooserPanel.add(label, BorderLayout.NORTH);
		
		this.dayChooser = new JXMonthView(this.date);
		this.dayChooser.setBackground(new Color(-2695707));
		this.dayChooser.setTraversable(true);
		this.setDate(this.date);
		
		// manage date changes on daychooser and fire dateChanged
		this.dayChooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				CalendarPanel.this.date = CalendarPanel.this.dayChooser.getSelection().first();
				for (Iterator iter = CalendarPanel.this.dateListeners.iterator(); iter.hasNext();) {
					DateListener listener = (DateListener) iter.next();
					listener.dateChanged(CalendarPanel.this.date);
				}
			}
		});
		
		daychooserPanel.add(this.dayChooser, BorderLayout.CENTER);
		
		JButton todayButton = new JButton(
				BizCalTranslations.getString("bizcal.gotoToday"),
				CalendarIcons.getMediumIcon(CalendarIcons.TODAY));
		todayButton.setHorizontalAlignment(SwingConstants.LEFT);
		todayButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CalendarPanel.this.setDate(new Date());
			}
		});
		daychooserPanel.add(todayButton, BorderLayout.SOUTH);
		
		this.naviBar.addButtonPanel(daychooserPanel, NaviBar.BOTTOM);
		
		/* ------------------------------------------------------- */
		// slider for zoom
		this.slider = new JSlider();
		try {
			/* --------------------------------------------- */
			this.slider.setMinimum(30);
			this.slider.setMaximum(500);
			this.slider.setValue(DayView.PIXELS_PER_HOUR);
			/* --------------------------------------------- */
		} catch (Exception e) {
			/* --------------------------------------------- */
			e.printStackTrace();
			/* --------------------------------------------- */
		}
		
		this.slider.setOpaque(false);
		
		this.slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				/* ====================================================== */
				int pos = (((JSlider) (e.getSource())).getValue());
				for (AbstractCalendarView acv : CalendarPanel.this.calendarViews.values()) {
					/* ------------------------------------------------------- */
					if (acv instanceof DayViewPanel) {
						DayViewPanel dvp = (DayViewPanel) acv;
						dvp.setZoomFactor(pos);
					}
					/* ------------------------------------------------------- */
				}
				/* ====================================================== */
			}
		});
		
		this.slider.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				/* ====================================================== */
				CalendarPanel.this.slider.setValue(CalendarPanel.this.slider.getValue()
						+ e.getWheelRotation()
						* 6);
				/* ====================================================== */
			}
		});
		
		this.naviBar.addButtonPanel(this.slider, NaviBar.BOTTOM);
	}
	
	public JXMonthView getDayChooser() {
		return this.dayChooser;
	}
	
	/**
	 * show the specified view
	 * 
	 * @param panelName
	 */
	public void showView(String panelName) {
		if (panelName == null)
			return;
		for (Iterator iter = this.calendarViews.values().iterator(); iter.hasNext();) {
			AbstractCalendarView panel = (AbstractCalendarView) iter.next();
			if (panelName.equals(panel.getButton().getActionCommand())) {
				panel.getButton().doClick();
				return;
			}
		}
	}
	
	/**
	 * adds a new CalendarView to the CalendarPanel
	 * 
	 * @param calendarView
	 *            a CalendarView Object
	 */
	public void addCalendarView(AbstractCalendarView calendarView) {
		// add the panel
		this.viewsPanel.add(calendarView, calendarView.getViewName());
		// add the button for the panel
		this.viewsButtonPanel.addToggleButton(calendarView.getButton());
		calendarView.getButton().setActionCommand(calendarView.getViewName());
		calendarView.getButton().addActionListener(this.viewListener);
		// add a datelistener to the panel
		this.addDateListener(calendarView);
		this.addNamedCalendarListener(calendarView);
		this.calendarViews.put(calendarView.getViewName(), calendarView);
		
		// calendarView.getView().setPopupMenuCallback(popupCallBack);
		
		// select the first Panel
		if (this.calendarViews.size() == 1) {
			calendarView.getButton().doClick();
		}
	}
	
	/**
	 * sets the date of the CalendarPanel
	 * 
	 * @param date
	 *            the new Date
	 */
	public void setDate(Date date) {
		this.dayChooser.setSelectionDate(date);
		this.dayChooser.commitSelection();
		this.dayChooser.ensureDateVisible(date);
	}
	
	/**
	 * gets the date of the CalendarPanel
	 * 
	 * @return the current Date
	 */
	public Date getDate() {
		return this.date;
	}
	
	/**
	 * adds a DateListener to the Panel
	 * 
	 * @param dateListener
	 *            the DateListener
	 */
	public void addDateListener(DateListener dateListener) {
		this.dateListeners.add(dateListener);
	}
	
	/**
	 * removes a DateListener to the Panel
	 * 
	 * @param dateListener
	 *            the DateListener
	 */
	public void removeDateListener(DateListener dateListener) {
		this.dateListeners.remove(dateListener);
	}
	
	/**
	 * adds a CalendarListener to the Panel
	 * 
	 * @param calendarListener
	 *            the CalendarListener
	 */
	public void addNamedCalendarListener(NamedCalendarListener calendarListener) {
		this.calendarListeners.add(calendarListener);
	}
	
	/**
	 * removes a CalendarListener to the Panel
	 * 
	 * @param calendarListener
	 *            the CalendarListener
	 */
	public void removeNamedCalendarListener(
			NamedCalendarListener calendarListener) {
		this.calendarListeners.remove(calendarListener);
	}
	
	/**
	 * Add a CalendarManagementListener
	 * 
	 * @param listener
	 */
	public void addCalendarManagementListener(
			CalendarManagementListener listener) {
		/* ================================================== */
		this.calendarManagementListeners.add(listener);
		/* ================================================== */
	}
	
	/**
	 * Remove a CalendarManagementListener
	 * 
	 * @param listener
	 */
	public void removeCalendarManagementListener(
			CalendarManagementListener listener) {
		/* ================================================== */
		this.calendarManagementListeners.remove(listener);
		/* ================================================== */
	}
	
	/**
	 * Remove a NamedCalendar.
	 * 
	 * @param namedCalendar
	 */
	public void removeNamedCalendar(NamedCalendar namedCalendar) {
		/* ================================================== */
		if (namedCalendar == null)
			return;
		/* ------------------------------------------------------- */
		// remove from hash
		this.namedCalendars.remove(namedCalendar);
		/* ------------------------------------------------------- */
		// remove button
		try {
			this.calendarButtonPanel.removeComponent(namedCalendar.getCheckBox());
			this.calendarButtonPanel.validate();
			this.calendarButtonPanel.updateUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* ================================================== */
	}
	
	/**
	 * Add a calendar.
	 * 
	 * @param namedCalendar
	 */
	public void addNamedCalendar(final NamedCalendar namedCalendar) {
		/* ================================================== */
		if (!this.namedCalendars.containsKey(namedCalendar)) {
			
			final CheckBoxPanel calendarToggler = new CheckBoxPanel(
					namedCalendar.getName(),
					namedCalendar.getColor(),
					this.calendarButtonGroup);
			
			calendarToggler.setToolTipText(namedCalendar.getDescription());
			calendarToggler.setActive(namedCalendar.isActive());
			namedCalendar.setCheckBox(calendarToggler);
			/* ------------------------------------------------------- */
			// mouselistener for contextmenu
			calendarToggler.addMouseListener(this);
			/* ------------------------------------------------------- */
			// actionlistener for the calender itself
			calendarToggler.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					/* ------------------------------------------------------- */
					// notify calendar listeners if a calendar has been
					// de/activated
					if (namedCalendar.isActive() != calendarToggler.isActive()) {
						/*
						 * ------------------------------------------------------
						 * -
						 */
						// save the new state of the calendar
						namedCalendar.setActive(calendarToggler.isActive());
						/*
						 * ------------------------------------------------------
						 * -
						 */
						// inform the listeners
						for (Iterator iter = CalendarPanel.this.calendarListeners.iterator(); iter.hasNext();) {
							NamedCalendarListener listener = (NamedCalendarListener) iter.next();
							listener.activeCalendarsChanged(CalendarPanel.this.namedCalendars.keySet());
						}
						/*
						 * ------------------------------------------------------
						 * -
						 */
					} else {
						/*
						 * ------------------------------------------------------
						 * -
						 */
						// =========================================================
						// set the namedcalendar to the selected state of its
						// calendar toggle button.
						// =========================================================
						namedCalendar.setSelected(calendarToggler.isSelected());
						// =========================================================
						// the namedcalendar that is in charge of this
						// togglebutton
						// is set as the current calendar. There can be only one
						// selected calendar to work on!
						// Unselect the rest.
						// =========================================================
						if (calendarToggler.isSelected()) {
							/*
							 * --------------------------------------------------
							 * -----
							 */
							// deselect all calendars, except the current
							// selected
							for (NamedCalendar cal : CalendarPanel.this.namedCalendars.keySet()) {
								if (!CalendarPanel.this.namedCalendars.get(cal).equals(
										calendarToggler)) {
									// namedCalendars.get(cal).setSelected(false);
									cal.setSelected(false);
								}
							}
							/*
							 * --------------------------------------------------
							 * -----
							 */
						}
						// =========================================================
						// send the current selected calendar to the listeners
						// =========================================================
						for (Iterator iter = CalendarPanel.this.calendarListeners.iterator(); iter.hasNext();) {
							NamedCalendarListener listener = (NamedCalendarListener) iter.next();
							listener.selectedCalendarChanged(CalendarPanel.this.getSelectedCalendar());
						}
						/*
						 * ------------------------------------------------------
						 * -
						 */
					}
				}
			});
			// end of actionlistener
			/* ------------------------------------------------------- */
			this.calendarButtonPanel.addComponent(calendarToggler);
			this.namedCalendars.put(namedCalendar, calendarToggler);
		}
		// =========================================================
		// inform all listeners of the new calendar
		// =========================================================
		// for (Iterator iter = calendarListeners.iterator(); iter.hasNext();) {
		// /* ------------------------------------------------------- */
		// NamedCalendarListener listener = (NamedCalendarListener) iter.next();
		// listener.activeCalendarsChanged(namedCalendars.keySet());
		// /* ------------------------------------------------------- */
		// }
		/* ================================================== */
	}
	
	/**
	 * Triggers update of calendars and calendar buttons
	 */
	public void triggerUpdate() {
		/* ================================================== */
		for (Iterator iter = this.calendarListeners.iterator(); iter.hasNext();) {
			/* ------------------------------------------------------- */
			NamedCalendarListener listener = (NamedCalendarListener) iter.next();
			listener.activeCalendarsChanged(this.namedCalendars.keySet());
			/* ------------------------------------------------------- */
		}
		
		this.calendarButtonPanel.validate();
		this.calendarButtonPanel.updateUI();
		/* ================================================== */
	}
	
	/**
	 * step to next date according of the current view
	 */
	public void next() {
		/* ================================================== */
		this.stepDate(true);
		/* ================================================== */
	}
	
	/**
	 * step to previous date according of the current view
	 */
	public void pevious() {
		/* ================================================== */
		this.stepDate(false);
		/* ================================================== */
	}
	
	/**
	 * @param forward
	 */
	private void stepDate(boolean forward) {
		/* ================================================== */
		try {
			String currentName = this.getCurrentView().getViewName();
			
			int step = 0;
			
			if (MonthViewPanel.VIEW_NAME.equals(currentName)) {
				/* ------------------------------------------------------- */
				step = 31;
				/* ------------------------------------------------------- */
			} else if (DayViewPanel.VIEW_NAME_DAY.equals(currentName)) {
				step = 1;
			} else if (DayViewPanel.VIEW_NAME_WEEK.equals(currentName)) {
				step = 7;
			} else if (ListViewPanel.VIEW_NAME.equals(currentName)) {
				try {
					ListViewPanel listView = (ListViewPanel) this.calendarViews.get(ListViewPanel.VIEW_NAME);
					step = listView.listView.getShowDays();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (!forward)
				step = step * (-1);
			/* ------------------------------------------------------- */
			this.dayChooser.setSelectionDate(DateUtil.getDiffDay(
					this.dayChooser.getSelection().first(),
					step));
			this.date = this.dayChooser.getSelection().first();
			
			for (Iterator iter = this.dateListeners.iterator(); iter.hasNext();) {
				DateListener listener = (DateListener) iter.next();
				listener.dateChanged(this.date);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// FICKEN
		}
		/* ================================================== */
	}
	
	/**
	 * Returns the current selected calendar.
	 * 
	 * @return
	 */
	public NamedCalendar getSelectedCalendar() {
		/* ================================================== */
		if (this.namedCalendars.keySet() != null) {
			for (NamedCalendar nc : this.namedCalendars.keySet()) {
				/* ------------------------------------------------------- */
				if (nc.isSelected())
					return nc;
				/* ------------------------------------------------------- */
			}
		}
		/* ------------------------------------------------------- */
		// if we are here, no calendar is selected
		// if there are active calendars, we will select the first
		if (this.getActiveCalendars() != null
				&& this.getActiveCalendars().size() > 0) {
			NamedCalendar nc = (NamedCalendar) this.getActiveCalendars().toArray()[0];
			nc.setSelected(true);
			// triggerUpdate();
			
			// inform listeners
			this.informListeners();
			
			// setSelectedCalendar(nc);
			return nc;
		}
		/* ------------------------------------------------------- */
		// if there are no selected and no active calendars,
		// we will select and activate the first in the list
		if (this.getActiveCalendars() == null
				|| this.getActiveCalendars().size() < 1) {
			/* ------------------------------------------------------- */
			List<NamedCalendar> allCals = this.getCalendars();
			if (allCals != null && allCals.size() > 0) {
				/* ------------------------------------------------------- */
				NamedCalendar nc = allCals.get(0);
				nc.setActive(true);
				nc.getCheckBox().setSelected(true);
				nc.setSelected(true);
				
				// triggerUpdate();
				// inform listeners
				this.informListeners();
				
				// setSelectedCalendar(nc);
				return nc;
				/* ------------------------------------------------------- */
			}
			/* ------------------------------------------------------- */
		}
		/* ------------------------------------------------------- */
		
		return null;
		/* ================================================== */
	}
	
	/**
	 * Sets the selected calendar
	 * 
	 * @param cal
	 */
	public void setSelectedCalendar(NamedCalendar cal) {
		/* ================================================== */
		// if the to selected calendar is not active, do so
		//
		// ... hmm, some kind of weird....
		/* ------------------------------------------------------- */
		if (!cal.isActive()) {
			cal.setActive(true);
			cal.getCheckBox().setActive(true);
		}
		/* ------------------------------------------------------- */
		// try to set selected
		/* ------------------------------------------------------- */
		if (!cal.isSelected()) {
			/* ------------------------------------------------------- */
			// select the new one
			/* ------------------------------------------------------- */
			cal.setSelected(true);
			cal.getCheckBox().setSelected(true);
			if (this.lastShowingCalendarBeforeShowAll != null)
				this.lastShowingCalendarBeforeShowAll = null;
			/* ------------------------------------------------------- */
		}
		/* ================================================== */
	}
	
	/**
	 * Selects the next calendar in the list
	 */
	public void selectNextCalendar() {
		/* ================================================== */
		NamedCalendar nc = this.getSelectedCalendar();
		/* ------------------------------------------------------- */
		if (nc == null)
			return;
		/* ------------------------------------------------------- */
		ArrayList<NamedCalendar> list = new ArrayList<NamedCalendar>(
				this.namedCalendars.keySet());
		/* ------------------------------------------------------- */
		int size = list.size();
		int pos = list.indexOf(nc);
		pos++;
		/* ------------------------------------------------------- */
		// if this is the last element, take the first
		if (pos > size - 1)
			pos = 0;
		/* ------------------------------------------------------- */
		NamedCalendar currCal = list.get(pos);
		currCal.setActive(true);
		this.namedCalendars.get(currCal).setActive(true);
		
		this.setSelectedCalendar(list.get(pos));
		this.deactivateOthers(currCal);
		
		this.informListeners();
		/* ================================================== */
	}
	
	/**
	 * Selects the next calendar in the list
	 */
	public void selectPreviousCalendar() {
		/* ================================================== */
		NamedCalendar nc = this.getSelectedCalendar();
		/* ------------------------------------------------------- */
		if (nc == null)
			return;
		/* ------------------------------------------------------- */
		ArrayList<NamedCalendar> list = new ArrayList<NamedCalendar>(
				this.namedCalendars.keySet());
		/* ------------------------------------------------------- */
		int size = list.size();
		int pos = list.indexOf(nc);
		pos--;
		/* ------------------------------------------------------- */
		// if this is the last element, take the first
		if (pos < 0)
			pos = size - 1;
		/* ------------------------------------------------------- */
		NamedCalendar currCal = list.get(pos);
		currCal.setActive(true);
		// activate the button
		this.namedCalendars.get(currCal).setActive(true);
		
		this.setSelectedCalendar(list.get(pos));
		
		this.deactivateOthers(currCal);
		this.informListeners();
		/* ================================================== */
	}
	
	/**
	 * Deactivates all but the given calendar
	 * 
	 * @param activeCal
	 */
	private void deactivateOthers(NamedCalendar activeCal) {
		/* ================================================== */
		ArrayList<NamedCalendar> list = new ArrayList<NamedCalendar>(
				this.namedCalendars.keySet());
		
		for (NamedCalendar nc : list) {
			/* ------------------------------------------------------- */
			if (!activeCal.equals(nc)) {
				nc.setActive(false);
				// activate the button
				this.namedCalendars.get(nc).setActive(false);
			}
			/* ------------------------------------------------------- */
		}
		this.lastShowingCalendarBeforeShowAll = null;
		/* ================================================== */
	}
	
	/**
	 * Shows all calendars or hides all but the last selected
	 */
	public void toggleShowAllCalendars() {
		/* ================================================== */
		// =====================================================================
		// show all calendars
		//
		// =====================================================================
		if (this.lastShowingCalendarBeforeShowAll == null) {
			/* ------------------------------------------------------- */
			this.lastShowingCalendarBeforeShowAll = this.getSelectedCalendar();
			/* ------------------------------------------------------- */
			ArrayList<NamedCalendar> list = new ArrayList<NamedCalendar>(
					this.namedCalendars.keySet());
			
			for (NamedCalendar nc : list) {
				/* ------------------------------------------------------- */
				nc.setActive(true);
				// activate the button
				this.namedCalendars.get(nc).setActive(true);
			}
			/* ------------------------------------------------------- */
			/* ------------------------------------------------------- */
		} else {
			// ===================================================================
			// hide all but the last selected
			//
			// ===================================================================
			NamedCalendar cal = this.lastShowingCalendarBeforeShowAll;
			/* ------------------------------------------------------- */
			cal.setSelected(true);
			cal.setActive(true);
			this.namedCalendars.get(cal).setActive(true);
			
			ArrayList<NamedCalendar> list = new ArrayList<NamedCalendar>(
					this.namedCalendars.keySet());
			
			for (NamedCalendar nc : list) {
				/* ------------------------------------------------------- */
				if (!cal.equals(nc)) {
					nc.setActive(false);
					// activate the button
					this.namedCalendars.get(nc).setActive(false);
				}
				/* ------------------------------------------------------- */
			}
			this.lastShowingCalendarBeforeShowAll = null;
			/* ------------------------------------------------------- */
		}
		
		this.informListeners();
		/* ================================================== */
	}
	
	/**
	 * Returns all active calendars
	 * 
	 * @return
	 */
	public Collection<NamedCalendar> getActiveCalendars() {
		/* ================================================== */
		Collection<NamedCalendar> activeCalendars = new ArrayList<NamedCalendar>(
				0);
		/* ------------------------------------------------------- */
		if (this.namedCalendars != null) {
			for (NamedCalendar nc : this.namedCalendars.keySet()) {
				/* ------------------------------------------------------- */
				if (nc.isActive())
					activeCalendars.add(nc);
				/* ------------------------------------------------------- */
			}
		}
		return activeCalendars;
		/* ================================================== */
	}
	
	/**
	 * inform active calendar listeners
	 */
	private void informListeners() {
		/* ================================================== */
		// System.out.println("called update");
		for (Iterator iter = this.calendarListeners.iterator(); iter.hasNext();) {
			NamedCalendarListener listener = (NamedCalendarListener) iter.next();
			listener.activeCalendarsChanged(this.namedCalendars.keySet());
		}
		/* ================================================== */
	}
	
	/**
	 * Returns all calendars, selected and unselected, acitve and inactive
	 * 
	 * @return
	 */
	public List<NamedCalendar> getCalendars() {
		/* ================================================== */
		return new ArrayList<NamedCalendar>(this.namedCalendars.keySet());
		/* ================================================== */
	}
	
	/**
	 * Init the popup menu
	 */
	private void initPopup() {
		/* ================================================== */
		this.popup = new JPopupMenu();
		/* ------------------------------------------------------- */
		// ===============================================
		// N E W Action
		// ===============================================
		this.newCalendarAction = new AbstractAction(
				BizCalTranslations.getString("calendar.core.new"),
				CalendarIcons.getMediumIcon(CalendarIcons.NEW)) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/* ====================================================== */
				// inform the listeners
				for (CalendarManagementListener lis : CalendarPanel.this.calendarManagementListeners)
					lis.newCalendar();
				/* ====================================================== */
			}
		};
		
		// ===============================================
		// M O D I F Y Action
		// ===============================================
		this.modifyCalendarAction = new AbstractAction(
				BizCalTranslations.getString("calendar.core.modify"),
				CalendarIcons.getMediumIcon(CalendarIcons.EDIT)) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/* ====================================================== */
				// find the named calendar for the clicked toggleButton
				// the clicked calendar should be the currentSelectedCalendar
				// =======================================================
				// inform the listeners
				for (CalendarManagementListener lis : CalendarPanel.this.calendarManagementListeners)
					lis.modifyCalendar(CalendarPanel.this.getSelectedCalendar());
				/* ====================================================== */
			}
		};
		// ===============================================
		// D E L E T E Action
		// ===============================================
		this.deleteCalendarAction = new AbstractAction(
				BizCalTranslations.getString("calendar.core.remove"),
				CalendarIcons.getMediumIcon(CalendarIcons.DELETE)) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/* ====================================================== */
				// find the named calendar for the clicked toggleButton
				// the clicked calendar should be the currentSelectedCalendar
				// =======================================================
				// inform the listeners
				for (CalendarManagementListener lis : CalendarPanel.this.calendarManagementListeners)
					lis.deleteCalendar(CalendarPanel.this.getSelectedCalendar());
				/* ====================================================== */
			}
		};
		/* ------------------------------------------------------- */
		// ==================================================
		// assemble the popup menu
		// ==================================================
		this.popup.add(this.newCalendarAction);
		/* ------------------------------------------------------- */
		this.popup.add(new JSeparator());
		/* ------------------------------------------------------- */
		this.popup.add(this.modifyCalendarAction);
		/* ------------------------------------------------------- */
		this.popup.add(new JSeparator());
		/* ------------------------------------------------------- */
		this.popup.add(this.deleteCalendarAction);
		/* ================================================== */
	}
	
	// /**
	// * Tries to find the owner of the toggler button
	// *
	// * @param toggler
	// * @return
	// */
	// private NamedCalendar findNamedCalendarByButton(CheckBoxPanel toggler) {
	// /* ================================================== */
	// if (namedCalendars.keySet() != null) {
	// for(NamedCalendar nc : namedCalendars.keySet()) {
	// if (nc.getCheckBox().equals(toggler))
	// return nc;
	// }
	// }
	// return null;
	// /* ================================================== */
	// }
	
	// ============================================================
	// MouseListener
	// ============================================================
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		/* ====================================================== */
		this.showContextMenu(e);
		/* ====================================================== */
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		/* ====================================================== */
		this.showContextMenu(e);
		/* ====================================================== */
	}
	
	/**
	 * Show the pop menu
	 * 
	 * @param e
	 */
	private void showContextMenu(MouseEvent e) {
		/* ================================================== */
		if (e.isPopupTrigger()) {
			/* ------------------------------------------------------- */
			try {
				((CheckBoxPanel) ((JToggleButton) e.getSource()).getParent()).setSelected(true);
			} catch (Exception ee) {}
			/* ------------------------------------------------------- */
			this.popup.show((Component) e.getSource(), e.getX(), e.getY());
			/* ------------------------------------------------------- */
		}
		/* ================================================== */
	}
	
	/**
	 * @return the functionsButtonPanel
	 */
	public ButtonPanel getFunctionsButtonPanel() {
		return this.functionsButtonPanel;
	}
	
	/**
	 * @return the currentView
	 */
	public AbstractCalendarView getCurrentView() {
		return this.currentView;
	}
	
	// public void removeNamedCalendar(NamedCalendar namedCalendar) {
	// this.calendarButtonPanel.remove(this.namedCalendars .get(namedCalendar));
	// this.namedCalendars .remove(namedCalendar);
	// for (Iterator iter = calendarListeners.iterator(); iter.hasNext();) {
	// NamedCalendarListener listener = (NamedCalendarListener) iter.next();
	// listener.calendarsChanged(namedCalendars.keySet());
	// }
	// }
	
	class PopupCallBack implements PopupMenuCallback {
		
		@Override
		public JPopupMenu getCalendarPopupMenu(Object calId) throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			return null;
			/* ====================================================== */
		}
		
		@Override
		public JPopupMenu getEmptyPopupMenu(Object calId, Date date)
				throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			return null;
			/* ====================================================== */
		}
		
		@Override
		public JPopupMenu getEventPopupMenu(Object calId, Event event)
				throws Exception {
			/* ====================================================== */
			return CalendarPanel.this.popup;
			/* ====================================================== */
		}
		
		@Override
		public JPopupMenu getProjectPopupMenu(Object calId) throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			return null;
			/* ====================================================== */
		}
		
	}
	
}
