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
package com.leclercb.taskunifier.api.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ContactList.ContactItem;
import com.leclercb.taskunifier.api.models.beans.ContactListBean;
import com.leclercb.taskunifier.api.models.beans.ContactListBean.ContactItemBean;

public class ContactList implements Cloneable, Serializable, Iterable<ContactItem>, PropertyChangeListener, ListChangeSupported, PropertyChangeSupported {
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private EventList<ContactItem> contacts;
	
	public ContactList() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		ObservableElementList.Connector<ContactItem> connector = GlazedLists.beanConnector(ContactItem.class);
		
		this.contacts = new ObservableElementList<ContactItem>(
				new BasicEventList<ContactItem>(), 
				connector);
	}
	
	@Override
	protected ContactList clone() {
		ContactList list = new ContactList();
		list.contacts.addAll(this.contacts);
		return list;
	}
	
	@Override
	public Iterator<ContactItem> iterator() {
		return this.contacts.iterator();
	}
	
	public List<ContactItem> getList() {
		return new ArrayList<ContactItem>(this.contacts);
	}
	
	public void add(ContactItem item) {
		CheckUtils.isNotNull(item);
		this.contacts.add(item);
	}
	
	public void addAll(Collection<ContactItem> items) {
		if (items == null)
			return;
		
		for (ContactItem item : items)
			this.add(item);
	}
	
	public void remove(ContactItem item) {
		CheckUtils.isNotNull(item);
		this.contacts.remove(item);
	}
	
	public void clear() {
		for (ContactItem item : this.getList())
			this.remove(item);
	}
	
	public int size() {
		return this.contacts.size();
	}
	
	public int getIndexOf(ContactItem item) {
		return this.contacts.indexOf(item);
	}
	
	public ContactItem get(int index) {
		return this.contacts.get(index);
	}
	
	@Override
	public String toString() {
		List<String> contacts = new ArrayList<String>();
		for (ContactItem contact : this.contacts) {
			if (contact.toString().length() != 0)
				contacts.add(contact.toString());
		}
		
		return StringUtils.join(contacts, ", ");
	}
	
	public ContactListBean toContactGroupBean() {
		ContactListBean list = new ContactListBean();
		
		for (ContactItem item : this)
			list.add(item.toContactItemBean());
		
		return list;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.contacts.addListEventListener(listener);
	}
	
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.contacts.removeListEventListener(listener);
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(
				propertyName,
				listener);
	}
	
	public static class ContactItem implements PropertyChangeSupported, PropertyChangeListener {
		
		public static final String PROP_CONTACT = "contact";
		public static final String PROP_LINK = "link";
		
		private PropertyChangeSupport propertyChangeSupport;
		
		private Contact contact;
		private String link;
		
		public ContactItem() {
			this(null, null);
		}
		
		public ContactItem(Contact contact, String link) {
			this.propertyChangeSupport = new PropertyChangeSupport(this);
			
			this.setContact(contact);
			this.setLink(link);
		}
		
		public Contact getContact() {
			return this.contact;
		}
		
		public void setContact(Contact contact) {
			if (contact != null) {
				if (contact.getModelStatus().equals(ModelStatus.TO_DELETE)
						|| contact.getModelStatus().equals(ModelStatus.DELETED)) {
					ApiLogger.getLogger().severe(
							"You cannot assign a deleted model");
					contact = null;
				}
			}
			
			if (this.contact != null)
				this.contact.removePropertyChangeListener(this);
			
			Contact oldContact = this.contact;
			this.contact = contact;
			
			if (this.contact != null)
				this.contact.addPropertyChangeListener(this);
			
			this.propertyChangeSupport.firePropertyChange(
					PROP_CONTACT,
					oldContact,
					contact);
		}
		
		public String getLink() {
			return this.link;
		}
		
		public void setLink(String link) {
			String oldLink = this.link;
			this.link = link;
			this.propertyChangeSupport.firePropertyChange(
					PROP_LINK,
					oldLink,
					link);
		}
		
		@Override
		public String toString() {
			String contact = (this.contact == null ? "" : this.contact.toString());
			String link = (this.link == null ? "" : this.link);
			
			if (link.length() != 0)
				link = "(" + this.link + ")";
			
			if (contact.length() == 0)
				return link;
			
			if (link.length() == 0)
				return contact;
			
			return contact + " " + link;
		}
		
		public ContactItemBean toContactItemBean() {
			ModelId id = null;
			
			if (this.contact != null)
				id = this.contact.getModelId();
			
			return new ContactItemBean(id, this.link);
		}
		
		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.addPropertyChangeListener(listener);
		}
		
		@Override
		public void addPropertyChangeListener(
				String propertyName,
				PropertyChangeListener listener) {
			this.propertyChangeSupport.addPropertyChangeListener(
					propertyName,
					listener);
		}
		
		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.removePropertyChangeListener(listener);
		}
		
		@Override
		public void removePropertyChangeListener(
				String propertyName,
				PropertyChangeListener listener) {
			this.propertyChangeSupport.removePropertyChangeListener(
					propertyName,
					listener);
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getPropertyName().equals(Model.PROP_MODEL_STATUS)) {
				Contact contact = (Contact) event.getSource();
				
				if (contact.getModelStatus().equals(ModelStatus.TO_DELETE)
						|| contact.getModelStatus().equals(ModelStatus.DELETED))
					this.setContact(null);
			}
		}
		
	}
	
}

