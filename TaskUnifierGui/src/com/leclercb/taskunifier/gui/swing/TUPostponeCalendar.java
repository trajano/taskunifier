/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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

public class TUPostponeCalendar extends JCalendar implements ActionSupported {
	
	public static final String NO_DATE_COMMAND = "NO_DATE";
	
	private ActionSupport actionSupport;
	
	public TUPostponeCalendar(boolean showNoDateButton) {
		super();
		this.initialize(showNoDateButton);
	}
	
	public TUPostponeCalendar(boolean showNoDateButton, boolean monthSpinner) {
		super(monthSpinner);
		this.initialize(showNoDateButton);
	}
	
	public TUPostponeCalendar(boolean showNoDateButton, Calendar calendar) {
		super(calendar);
		this.initialize(showNoDateButton);
	}
	
	public TUPostponeCalendar(
			boolean showNoDateButton,
			Date date,
			boolean monthSpinner) {
		super(date, monthSpinner);
		this.initialize(showNoDateButton);
	}
	
	public TUPostponeCalendar(
			boolean showNoDateButton,
			Date date,
			Locale locale,
			boolean monthSpinner,
			boolean weekOfYearVisible) {
		super(date, locale, monthSpinner, weekOfYearVisible);
		this.initialize(showNoDateButton);
	}
	
	public TUPostponeCalendar(boolean showNoDateButton, Date date, Locale locale) {
		super(date, locale);
		this.initialize(showNoDateButton);
	}
	
	public TUPostponeCalendar(boolean showNoDateButton, Date date) {
		super(date);
		this.initialize(showNoDateButton);
	}
	
	public TUPostponeCalendar(
			boolean showNoDateButton,
			Locale locale,
			boolean monthSpinner) {
		super(locale, monthSpinner);
		this.initialize(showNoDateButton);
	}
	
	public TUPostponeCalendar(boolean showNoDateButton, Locale locale) {
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
					TUPostponeCalendar.this.setCalendar(Calendar.getInstance());
					TUPostponeCalendar.this.actionSupport.fireActionPerformed(
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
					
					Calendar currentCalendar = TUPostponeCalendar.this.getCalendar();
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
					
					TUPostponeCalendar.this.setCalendar(calendar);
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
