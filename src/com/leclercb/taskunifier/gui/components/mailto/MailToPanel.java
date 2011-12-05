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
package com.leclercb.taskunifier.gui.components.mailto;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.taskunifier.api.models.Contact;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.DesktopUtils;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class MailToPanel extends JPanel {
	
	public static final String ACTION_SNOOZE = "ACTION_MAIL";
	
	private ActionSupport actionSupport;
	
	private ContactList contactList;
	
	public MailToPanel() {
		this.actionSupport = new ActionSupport(this);
		
		this.initialize();
	}
	
	public ContactList getContactList() {
		return this.contactList;
	}
	
	public void addActionListener(ActionListener listener) {
		this.actionSupport.addActionListener(listener);
	}
	
	public void removeActionListener(ActionListener listener) {
		this.actionSupport.removeActionListener(listener);
	}
	
	public void mail() {
		Contact[] contacts = this.contactList.getSelectedContacts();
		
		List<String> toList = new ArrayList<String>();
		for (Contact contact : contacts) {
			if (contact.getEmail() != null)
				toList.add(contact.getEmail());
		}
		
		String[] to = toList.toArray(new String[0]);
		String[] cc = null;
		String subject = null;
		String body = null;
		
		ViewType viewType = MainFrame.getInstance().getSelectedViewType();
		if (viewType == ViewType.TASKS || viewType == ViewType.CALENDAR) {
			Task[] tasks = ViewType.getSelectedTasks();
			
		} else if (viewType == ViewType.NOTES) {
			Note[] notes = ViewType.getSelectedNotes();
			
		}
		
		DesktopUtils.mail(to, cc, subject, body);
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(0, 10));
		
		this.contactList = new ContactList();
		this.add(this.contactList, BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		JButton mailButton = new JButton(new MailAction());
		JPanel panel = new TUButtonsPanel(mailButton);
		
		this.add(panel, BorderLayout.SOUTH);
	}
	
	private class MailAction extends AbstractAction {
		
		public MailAction() {
			super(
					Translations.getString("general.send_mail"),
					ImageUtils.getResourceImage("mail.png", 24, 24));
			
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			MailToPanel.this.mail();
		}
		
	}
	
}
