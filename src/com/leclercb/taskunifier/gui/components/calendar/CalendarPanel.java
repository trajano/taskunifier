package com.leclercb.taskunifier.gui.components.calendar;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import lu.tudor.santec.bizcal.AbstractCalendarView;
import lu.tudor.santec.bizcal.NamedCalendar;
import lu.tudor.santec.bizcal.listeners.DateListener;
import lu.tudor.santec.bizcal.listeners.NamedCalendarListener;
import lu.tudor.santec.bizcal.views.DayViewPanel;
import lu.tudor.santec.bizcal.widgets.ButtonPanel;
import lu.tudor.santec.bizcal.widgets.CheckBoxPanel;

import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.JXSearchField;

import bizcal.swing.DayView;

import com.explodingpixels.macwidgets.SourceListStandardColorScheme;
import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.components.quickaddtask.QuickAddTaskPanel;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherPanel;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.TUShowCompletedTasksCheckBox;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class CalendarPanel extends JPanel implements SavePropertiesListener {
	
	private LinkedHashMap<String, AbstractCalendarView> calendarViews = new LinkedHashMap<String, AbstractCalendarView>();
	private JXMonthView dayChooser;
	private ButtonPanel viewsButtonPanel;
	private ButtonPanel calendarButtonPanel;
	private JPanel viewsPanel;
	private CardLayout viewsCardLayout;
	private transient ActionListener viewListener;
	private Vector<DateListener> dateListeners = new Vector<DateListener>();
	private Vector<NamedCalendarListener> calendarListeners = new Vector<NamedCalendarListener>();
	private Date date = new Date();
	private LinkedHashMap<NamedCalendar, CheckBoxPanel> namedCalendars = new LinkedHashMap<NamedCalendar, CheckBoxPanel>();
	protected AbstractCalendarView currentView;
	private JSlider zoomSlider;
	private NamedCalendar lastShowingCalendarBeforeShowAll = null;
	private ButtonGroup calendarButtonGroup = new ButtonGroup();
	
	private JSplitPane horizontalSplitPane;
	
	private JXSearchField searchField;
	private JCheckBox showCompletedTasksCheckBox;
	private TaskSearcherPanel taskSearcherPanel;
	
	public CalendarPanel() {
		Main.getSettings().addSavePropertiesListener(this);
		
		this.setLayout(new BorderLayout(3, 3));
		
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf()) {
			this.horizontalSplitPane = ComponentFactory.createThinJSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		} else {
			this.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
			
			this.horizontalSplitPane = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT);
		}
		
		this.horizontalSplitPane.setOneTouchExpandable(true);
		
		this.add(this.horizontalSplitPane, BorderLayout.CENTER);
		
		this.createMainPanel();
		this.createMenuPanel();
		
		this.loadSplitPaneSettings();
	}
	
	public JSlider getZoomSlider() {
		return this.zoomSlider;
	}
	
	public TaskSearcherPanel getTaskSearcherPanel() {
		return this.taskSearcherPanel;
	}
	
	private void loadSplitPaneSettings() {
		int hSplit = Main.getSettings().getIntegerProperty(
				"view.calendar.window.horizontal_split");
		this.horizontalSplitPane.setDividerLocation(hSplit);
	}
	
	@Override
	public void saveProperties() {
		Main.getSettings().setIntegerProperty(
				"view.calendar.window.horizontal_split",
				this.horizontalSplitPane.getDividerLocation());
	}
	
	private void createMainPanel() {
		this.viewsCardLayout = new CardLayout();
		this.viewsPanel = new JPanel(this.viewsCardLayout);
		this.viewsPanel.setOpaque(false);
		
		this.viewListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CalendarPanel.this.viewsCardLayout.show(
						CalendarPanel.this.viewsPanel,
						e.getActionCommand());
				CalendarPanel.this.currentView = CalendarPanel.this.calendarViews.get(e.getActionCommand());
			}
		};
		
		JPanel quickAddTaskPanel = new JPanel(new BorderLayout(3, 3));
		quickAddTaskPanel.add(new QuickAddTaskPanel(), BorderLayout.CENTER);
		quickAddTaskPanel.add(
				Help.getHelpButton("task_quick_add"),
				BorderLayout.EAST);
		
		JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
		mainPanel.add(quickAddTaskPanel, BorderLayout.NORTH);
		mainPanel.add(this.viewsPanel, BorderLayout.CENTER);
		
		this.horizontalSplitPane.setRightComponent(mainPanel);
	}
	
	private void createMenuPanel() {
		JPanel bottomPanel = new JPanel(new BorderLayout(3, 3));
		bottomPanel.setOpaque(false);
		
		this.viewsButtonPanel = new ButtonPanel(
				null,
				null,
				4,
				new Vector<AbstractButton>());
		this.viewsButtonPanel.setBorder(BorderFactory.createEmptyBorder(
				3,
				3,
				3,
				3));
		
		bottomPanel.add(this.viewsButtonPanel, BorderLayout.NORTH);
		
		this.calendarButtonPanel = new ButtonPanel(
				null,
				null,
				1,
				new Vector<AbstractButton>());
		this.calendarButtonPanel.setBorder(BorderFactory.createEmptyBorder(
				0,
				0,
				0,
				5));
		
		this.dayChooser = new JXMonthView(this.date);
		this.dayChooser.setBackground(new Color(-2695707));
		this.dayChooser.setTraversable(true);
		this.setDate(this.date);
		
		this.dayChooser.addActionListener(new ActionListener() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void actionPerformed(ActionEvent evt) {
				CalendarPanel.this.date = CalendarPanel.this.dayChooser.getSelection().first();
				for (Iterator iter = CalendarPanel.this.dateListeners.iterator(); iter.hasNext();) {
					DateListener listener = (DateListener) iter.next();
					listener.dateChanged(CalendarPanel.this.date);
				}
			}
		});
		
		bottomPanel.add(this.dayChooser, BorderLayout.CENTER);
		
		this.zoomSlider = new JSlider();
		try {
			this.zoomSlider.setMinimum(30);
			this.zoomSlider.setMaximum(500);
			this.zoomSlider.setValue(DayView.PIXELS_PER_HOUR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.zoomSlider.setOpaque(false);
		
		this.zoomSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Date date = CalendarPanel.this.getDate();
				int pos = (((JSlider) (e.getSource())).getValue());
				for (AbstractCalendarView acv : CalendarPanel.this.calendarViews.values()) {
					if (acv instanceof DayViewPanel) {
						DayViewPanel dvp = (DayViewPanel) acv;
						dvp.setZoomFactor(pos);
						CalendarPanel.this.setDate(date);
					}
				}
			}
		});
		
		this.zoomSlider.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				CalendarPanel.this.zoomSlider.setValue(CalendarPanel.this.zoomSlider.getValue()
						+ e.getWheelRotation()
						* 6);
			}
		});
		
		bottomPanel.add(this.zoomSlider, BorderLayout.SOUTH);
		
		JPanel searcherPane = new JPanel();
		searcherPane.setLayout(new BorderLayout());
		searcherPane.setBackground(new SourceListStandardColorScheme().getActiveBackgroundColor());
		
		this.initializeSearchField();
		this.showCompletedTasksCheckBox = new TUShowCompletedTasksCheckBox();
		this.initializeSearcherList(searcherPane);
		
		searcherPane.add(bottomPanel, BorderLayout.SOUTH);
		
		this.horizontalSplitPane.setLeftComponent(searcherPane);
	}
	
	private void initializeSearchField() {
		this.searchField = new JXSearchField(
				Translations.getString("general.search"));
		this.searchField.setColumns(15);
		
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CalendarPanel.this.taskSearcherPanel.setSearchFilter(e.getActionCommand());
			}
			
		});
	}
	
	private void initializeSearcherList(JPanel searcherPane) {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(new SourceListStandardColorScheme().getActiveBackgroundColor());
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.setBackground(new SourceListStandardColorScheme().getActiveBackgroundColor());
		
		panel.add(northPanel, BorderLayout.NORTH);
		
		JPanel showComletedTasksPanel = new JPanel(new BorderLayout());
		showComletedTasksPanel.setOpaque(false);
		showComletedTasksPanel.add(this.showCompletedTasksCheckBox);
		
		northPanel.add(this.searchField);
		northPanel.add(showComletedTasksPanel);
		northPanel.add(this.calendarButtonPanel);
		
		searcherPane.add(panel, BorderLayout.CENTER);
		
		panel.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);
		
		this.initializeTaskSearcherList(panel);
	}
	
	private void initializeTaskSearcherList(JPanel searcherPane) {
		this.taskSearcherPanel = new TaskSearcherPanel("tasksearcher.calendar");
		
		this.taskSearcherPanel.addPropertyChangeListener(
				TaskSearcherPanel.PROP_TITLE_FILTER,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						String filter = (String) evt.getNewValue();
						if (!CalendarPanel.this.searchField.getText().equals(
								filter))
							CalendarPanel.this.searchField.setText(filter);
					}
					
				});
		
		searcherPane.add(this.taskSearcherPanel);
	}
	
	public JXMonthView getDayChooser() {
		return this.dayChooser;
	}
	
	@SuppressWarnings("rawtypes")
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
	
	public void addCalendarView(AbstractCalendarView calendarView) {
		this.viewsPanel.add(calendarView, calendarView.getViewName());
		this.viewsButtonPanel.addToggleButton(calendarView.getButton());
		calendarView.getButton().setActionCommand(calendarView.getViewName());
		calendarView.getButton().addActionListener(this.viewListener);
		this.addDateListener(calendarView);
		this.addNamedCalendarListener(calendarView);
		this.calendarViews.put(calendarView.getViewName(), calendarView);
		
		if (this.calendarViews.size() == 1) {
			calendarView.getButton().doClick();
		}
	}
	
	public void setDate(Date date) {
		this.dayChooser.setSelectionDate(date);
		this.dayChooser.commitSelection();
		this.dayChooser.ensureDateVisible(date);
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public void addDateListener(DateListener dateListener) {
		this.dateListeners.add(dateListener);
	}
	
	public void removeDateListener(DateListener dateListener) {
		this.dateListeners.remove(dateListener);
	}
	
	public void addNamedCalendarListener(NamedCalendarListener calendarListener) {
		this.calendarListeners.add(calendarListener);
	}
	
	public void removeNamedCalendarListener(
			NamedCalendarListener calendarListener) {
		this.calendarListeners.remove(calendarListener);
	}
	
	public void removeNamedCalendar(NamedCalendar namedCalendar) {
		if (namedCalendar == null)
			return;
		
		this.namedCalendars.remove(namedCalendar);
		
		try {
			this.calendarButtonPanel.removeComponent(namedCalendar.getCheckBox());
			this.calendarButtonPanel.validate();
			this.calendarButtonPanel.updateUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addNamedCalendar(final NamedCalendar namedCalendar) {
		if (!this.namedCalendars.containsKey(namedCalendar)) {
			
			final CheckBoxPanel calendarToggler = new CheckBoxPanel(
					namedCalendar.getName(),
					namedCalendar.getColor(),
					this.calendarButtonGroup);
			
			calendarToggler.setToolTipText(namedCalendar.getDescription());
			calendarToggler.setActive(namedCalendar.isActive());
			namedCalendar.setCheckBox(calendarToggler);
			
			calendarToggler.addActionListener(new ActionListener() {
				
				@SuppressWarnings("rawtypes")
				@Override
				public void actionPerformed(ActionEvent e) {
					if (namedCalendar.isActive() != calendarToggler.isActive()) {
						namedCalendar.setActive(calendarToggler.isActive());
						
						for (Iterator iter = CalendarPanel.this.calendarListeners.iterator(); iter.hasNext();) {
							NamedCalendarListener listener = (NamedCalendarListener) iter.next();
							listener.activeCalendarsChanged(CalendarPanel.this.namedCalendars.keySet());
						}
					} else {
						namedCalendar.setSelected(calendarToggler.isSelected());
						
						if (calendarToggler.isSelected()) {
							for (NamedCalendar cal : CalendarPanel.this.namedCalendars.keySet()) {
								if (!CalendarPanel.this.namedCalendars.get(cal).equals(
										calendarToggler)) {
									cal.setSelected(false);
								}
							}
						}
						
						for (Iterator iter = CalendarPanel.this.calendarListeners.iterator(); iter.hasNext();) {
							NamedCalendarListener listener = (NamedCalendarListener) iter.next();
							listener.selectedCalendarChanged(CalendarPanel.this.getSelectedCalendar());
						}
					}
				}
			});
			this.calendarButtonPanel.addComponent(calendarToggler);
			this.namedCalendars.put(namedCalendar, calendarToggler);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void triggerUpdate() {
		for (Iterator iter = this.calendarListeners.iterator(); iter.hasNext();) {
			NamedCalendarListener listener = (NamedCalendarListener) iter.next();
			listener.activeCalendarsChanged(this.namedCalendars.keySet());
		}
		
		this.calendarButtonPanel.validate();
		this.calendarButtonPanel.updateUI();
	}
	
	public NamedCalendar getSelectedCalendar() {
		if (this.namedCalendars.keySet() != null) {
			for (NamedCalendar nc : this.namedCalendars.keySet()) {
				if (nc.isSelected())
					return nc;
			}
		}
		
		if (this.getActiveCalendars() != null
				&& this.getActiveCalendars().size() > 0) {
			NamedCalendar nc = (NamedCalendar) this.getActiveCalendars().toArray()[0];
			nc.setSelected(true);
			
			this.informListeners();
			
			return nc;
		}
		
		if (this.getActiveCalendars() == null
				|| this.getActiveCalendars().size() < 1) {
			List<NamedCalendar> allCals = this.getCalendars();
			if (allCals != null && allCals.size() > 0) {
				NamedCalendar nc = allCals.get(0);
				nc.setActive(true);
				nc.getCheckBox().setSelected(true);
				nc.setSelected(true);
				
				this.informListeners();
				
				return nc;
			}
		}
		
		return null;
	}
	
	public void setSelectedCalendar(NamedCalendar cal) {
		if (!cal.isActive()) {
			cal.setActive(true);
			cal.getCheckBox().setActive(true);
		}
		
		if (!cal.isSelected()) {
			cal.setSelected(true);
			cal.getCheckBox().setSelected(true);
			if (this.lastShowingCalendarBeforeShowAll != null)
				this.lastShowingCalendarBeforeShowAll = null;
		}
	}
	
	public void toggleShowAllCalendars() {
		if (this.lastShowingCalendarBeforeShowAll == null) {
			this.lastShowingCalendarBeforeShowAll = this.getSelectedCalendar();
			
			ArrayList<NamedCalendar> list = new ArrayList<NamedCalendar>(
					this.namedCalendars.keySet());
			
			for (NamedCalendar nc : list) {
				nc.setActive(true);
				this.namedCalendars.get(nc).setActive(true);
			}
		} else {
			NamedCalendar cal = this.lastShowingCalendarBeforeShowAll;
			
			cal.setSelected(true);
			cal.setActive(true);
			this.namedCalendars.get(cal).setActive(true);
			
			ArrayList<NamedCalendar> list = new ArrayList<NamedCalendar>(
					this.namedCalendars.keySet());
			
			for (NamedCalendar nc : list) {
				if (!cal.equals(nc)) {
					nc.setActive(false);
					this.namedCalendars.get(nc).setActive(false);
				}
			}
			
			this.lastShowingCalendarBeforeShowAll = null;
		}
		
		this.informListeners();
	}
	
	public Collection<NamedCalendar> getActiveCalendars() {
		Collection<NamedCalendar> activeCalendars = new ArrayList<NamedCalendar>(
				0);
		
		if (this.namedCalendars != null) {
			for (NamedCalendar nc : this.namedCalendars.keySet()) {
				if (nc.isActive())
					activeCalendars.add(nc);
			}
		}
		
		return activeCalendars;
	}
	
	@SuppressWarnings("rawtypes")
	private void informListeners() {
		for (Iterator iter = this.calendarListeners.iterator(); iter.hasNext();) {
			NamedCalendarListener listener = (NamedCalendarListener) iter.next();
			listener.activeCalendarsChanged(this.namedCalendars.keySet());
		}
	}
	
	public List<NamedCalendar> getCalendars() {
		return new ArrayList<NamedCalendar>(this.namedCalendars.keySet());
	}
	
	public AbstractCalendarView getCurrentView() {
		return this.currentView;
	}
	
}
