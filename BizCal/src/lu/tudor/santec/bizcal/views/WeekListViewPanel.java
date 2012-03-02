package lu.tudor.santec.bizcal.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JToggleButton;

import lu.tudor.santec.bizcal.AbstractCalendarView;
import lu.tudor.santec.bizcal.CalendarIcons;
import lu.tudor.santec.bizcal.EventModel;
import lu.tudor.santec.bizcal.NamedCalendar;
import lu.tudor.santec.bizcal.print.PrintUtilities;
import lu.tudor.santec.bizcal.resources.BizCalTranslations;
import lu.tudor.santec.bizcal.views.weeklist.WeekListView;
import bizcal.common.CalendarViewConfig;
import bizcal.swing.CalendarListener;
import bizcal.swing.CalendarView;
import bizcal.swing.util.GradientArea;

public class WeekListViewPanel extends AbstractCalendarView {
	
	private static final long serialVersionUID = 1L;
	private CalendarViewConfig config;
	private JToggleButton button;
	private EventModel eventModel;
	public static final String VIEW_NAME = "LIST_VIEW";
	public WeekListView listView;
	Color primaryColor = new Color(230, 230, 230);
	Color secondaryColor = Color.WHITE;
	private GradientArea gp;
	
	private static Logger logger = Logger.getLogger(WeekListViewPanel.class.getName());
	
	public WeekListViewPanel(EventModel model) {
		this(model, new CalendarViewConfig());
	}
	
	public WeekListViewPanel(EventModel model, CalendarViewConfig config) {
		this.config = config;
		this.eventModel = model;
		
		this.button = new JToggleButton(
				CalendarIcons.getMediumIcon(CalendarIcons.LISTVIEW));
		this.button.setToolTipText(BizCalTranslations.getString("bizcal.LIST_VIEW"));
		
		this.setLayout(new BorderLayout());
		
		this.gp = new GradientArea(
				GradientArea.TOP_BOTTOM,
				this.secondaryColor,
				this.primaryColor);
		this.gp.setPreferredSize(new Dimension(30, 30));
		
		this.add(this.gp, BorderLayout.NORTH);
		
		try {
			this.listView = new WeekListView(config, this);
			this.listView.setModel(this.eventModel);
			this.eventModel.addCalendarView(this.listView);
			
			this.listView.refresh();
			this.listView.refresh0();
			this.add(this.listView.getComponent(), BorderLayout.CENTER);
			
		} catch (Exception e) {
			logger.log(Level.WARNING, "listView creation failed", e);
		}
		
	}
	
	@Override
	public JToggleButton getButton() {
		return this.button;
	}
	
	@Override
	public String getViewName() {
		return VIEW_NAME;
	}
	
	@Override
	public void dateChanged(Date date) {
		this.eventModel.setDate(date);
		this.listView.setDate(date);
		try {
			this.eventModel.refresh();
		} catch (Exception e) {
			logger.log(Level.WARNING, "updating listView failed", e);
		}
	}
	
	public void setTitle(Date start, Date end) {
		try {
			this.gp.setText(
					"<html><center>"
							+ BizCalTranslations.getString("bizcal.LIST_VIEW")
							+ "<br>"
							+ this.config.getDayFormat().format(start)
							+ " - "
							+ this.config.getDayFormat().format(end),
					true);
		} catch (Exception e) {
			logger.log(Level.WARNING, "setTitle failed", e);
		}
	}
	
	@Override
	public void activeCalendarsChanged(Collection<NamedCalendar> calendars) {
		
	}
	
	@Override
	public void selectedCalendarChanged(NamedCalendar selectedCalendar) {
		
	}
	
	public void addCalendarListener(CalendarListener listener) {
		this.listView.addListener(listener);
	}
	
	@Override
	public List getEvents() {
		try {
			return this.listView.getEvents();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public void print(boolean showPrinterDialog) {
		PrintUtilities.printComponent(this, showPrinterDialog, false);
	}
	
	@Override
	public CalendarView getView() {
		return this.listView;
	}
	
}
