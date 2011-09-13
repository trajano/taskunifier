package com.leclercb.taskunifier.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
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

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.event.action.ActionSupported;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.toedter.calendar.JCalendar;

public class JExtendedCalendar extends JCalendar implements ActionSupported {
	
	public static final String NO_DATE_COMMAND = "NO_DATE";
	
	private ActionSupport actionSupport;
	
	public JExtendedCalendar(boolean showNoDateButton) {
		super();
		this.initialize(showNoDateButton);
	}
	
	public JExtendedCalendar(boolean showNoDateButton, boolean monthSpinner) {
		super(monthSpinner);
		this.initialize(showNoDateButton);
	}
	
	public JExtendedCalendar(boolean showNoDateButton, Calendar calendar) {
		super(calendar);
		this.initialize(showNoDateButton);
	}
	
	public JExtendedCalendar(
			boolean showNoDateButton,
			Date date,
			boolean monthSpinner) {
		super(date, monthSpinner);
		this.initialize(showNoDateButton);
	}
	
	public JExtendedCalendar(
			boolean showNoDateButton,
			Date date,
			Locale locale,
			boolean monthSpinner,
			boolean weekOfYearVisible) {
		super(date, locale, monthSpinner, weekOfYearVisible);
		this.initialize(showNoDateButton);
	}
	
	public JExtendedCalendar(boolean showNoDateButton, Date date, Locale locale) {
		super(date, locale);
		this.initialize(showNoDateButton);
	}
	
	public JExtendedCalendar(boolean showNoDateButton, Date date) {
		super(date);
		this.initialize(showNoDateButton);
	}
	
	public JExtendedCalendar(
			boolean showNoDateButton,
			Locale locale,
			boolean monthSpinner) {
		super(locale, monthSpinner);
		this.initialize(showNoDateButton);
	}
	
	public JExtendedCalendar(boolean showNoDateButton, Locale locale) {
		super(locale);
		this.initialize(showNoDateButton);
	}
	
	private void initialize(boolean showNoDateButton) {
		this.actionSupport = new ActionSupport(this);
		
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
		
		if (showNoDateButton) {
			JButton noDateButton = new JButton(
					Translations.getString("postpone.no_date"));
			noDateButton.setForeground(Color.RED);
			noDateButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent evt) {
					JExtendedCalendar.this.setCalendar(Calendar.getInstance());
					JExtendedCalendar.this.actionSupport.fireActionPerformed(
							0,
							NO_DATE_COMMAND);
				}
				
			});
			
			panel.add(noDateButton);
		}
		
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
	
	@Override
	public void addActionListener(ActionListener listener) {
		this.actionSupport.addActionListener(listener);
	}
	
	@Override
	public void removeActionListener(ActionListener listener) {
		this.actionSupport.removeActionListener(listener);
	}
	
}
