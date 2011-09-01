package com.leclercb.taskunifier.gui.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.leclercb.taskunifier.gui.translations.Translations;
import com.toedter.calendar.JCalendar;

public class JExtendedCalendar extends JCalendar {
	
	public JExtendedCalendar() {
		super();
		this.initialize();
	}
	
	public JExtendedCalendar(boolean monthSpinner) {
		super(monthSpinner);
		this.initialize();
	}
	
	public JExtendedCalendar(Calendar calendar) {
		super(calendar);
		this.initialize();
	}
	
	public JExtendedCalendar(Date date, boolean monthSpinner) {
		super(date, monthSpinner);
		this.initialize();
	}
	
	public JExtendedCalendar(
			Date date,
			Locale locale,
			boolean monthSpinner,
			boolean weekOfYearVisible) {
		super(date, locale, monthSpinner, weekOfYearVisible);
		this.initialize();
	}
	
	public JExtendedCalendar(Date date, Locale locale) {
		super(date, locale);
		this.initialize();
	}
	
	public JExtendedCalendar(Date date) {
		super(date);
		this.initialize();
	}
	
	public JExtendedCalendar(Locale locale, boolean monthSpinner) {
		super(locale, monthSpinner);
		this.initialize();
	}
	
	public JExtendedCalendar(Locale locale) {
		super(locale);
		this.initialize();
	}
	
	private void initialize() {
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout(3, 3));
		main.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(new JQuickDateButton(
				Translations.getString("postpone.today"),
				Calendar.DAY_OF_MONTH,
				0));
		panel.add(new JQuickDateButton(
				Translations.getString("postpone.tomorrow"),
				Calendar.DAY_OF_MONTH,
				1));
		panel.add(new JQuickDateButton(
				Translations.getString(
						"postpone.in",
						Translations.getString("postpone.x_days", 2)),
				Calendar.DAY_OF_MONTH,
				2));
		panel.add(new JQuickDateButton(
				Translations.getString(
						"postpone.in",
						Translations.getString("postpone.x_days", 3)),
				Calendar.DAY_OF_MONTH,
				3));
		panel.add(new JQuickDateButton(
				Translations.getString(
						"postpone.in",
						Translations.getString("postpone.1_week")),
				Calendar.WEEK_OF_YEAR,
				1));
		panel.add(new JQuickDateButton(
				Translations.getString(
						"postpone.in",
						Translations.getString("postpone.x_weeks", 2)),
				Calendar.WEEK_OF_YEAR,
				2));
		panel.add(new JQuickDateButton(Translations.getString(
				"postpone.in",
				Translations.getString("postpone.1_month")), Calendar.MONTH, 1));
		panel.add(new JQuickDateButton(Translations.getString(
				"postpone.in",
				Translations.getString("postpone.1_year")), Calendar.YEAR, 1));
		
		main.add(new JSeparator(SwingConstants.VERTICAL), BorderLayout.WEST);
		main.add(panel, BorderLayout.CENTER);
		
		this.add(main, BorderLayout.EAST);
	}
	
	private class JQuickDateButton extends JButton {
		
		public JQuickDateButton(
				final String title,
				final int field,
				final int amount) {
			super(title);
			
			this.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent evt) {
					Calendar calendar = Calendar.getInstance();
					calendar.add(field, amount);
					
					Calendar currentCalendar = JExtendedCalendar.this.getCalendar();
					if (currentCalendar != null) {
						calendar.set(
								Calendar.HOUR_OF_DAY,
								currentCalendar.get(Calendar.HOUR_OF_DAY));
						calendar.set(
								Calendar.MINUTE,
								currentCalendar.get(Calendar.MINUTE));
						calendar.set(
								Calendar.SECOND,
								currentCalendar.get(Calendar.SECOND));
					}
					
					JExtendedCalendar.this.setCalendar(calendar);
				}
				
			});
		}
		
	}
	
}
