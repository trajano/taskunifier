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
package com.leclercb.taskunifier.api.models.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Contact;
import com.leclercb.taskunifier.api.models.ContactFactory;
import com.leclercb.taskunifier.api.models.ContactList;
import com.leclercb.taskunifier.api.models.ContactList.ContactItem;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.beans.ContactListBean.ContactItemBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ContactListBean implements Cloneable, Serializable, Iterable<ContactItemBean> {
	
	@XStreamAlias("contactlist")
	private List<ContactItemBean> contacts;
	
	public ContactListBean() {
		this.contacts = new ArrayList<ContactItemBean>();
	}
	
	@Override
	protected ContactListBean clone() {
		ContactListBean list = new ContactListBean();
		list.contacts.addAll(this.contacts);
		return list;
	}
	
	@Override
	public Iterator<ContactItemBean> iterator() {
		return this.contacts.iterator();
	}
	
	public List<ContactItemBean> getList() {
		return new ArrayList<ContactItemBean>(this.contacts);
	}
	
	public void add(ContactItemBean item) {
		CheckUtils.isNotNull(item);
		this.contacts.add(item);
	}
	
	public void addAll(Collection<ContactItemBean> items) {
		if (items == null)
			return;
		
		for (ContactItemBean item : items)
			this.add(item);
	}
	
	public void remove(ContactItemBean item) {
		CheckUtils.isNotNull(item);
		this.contacts.remove(item);
	}
	
	public void clear() {
		for (ContactItemBean item : this.getList())
			this.remove(item);
	}
	
	public int size() {
		return this.contacts.size();
	}
	
	public int getIndexOf(ContactItemBean item) {
		return this.contacts.indexOf(item);
	}
	
	public ContactItemBean get(int index) {
		return this.contacts.get(index);
	}
	
	public ContactList toContactGroup() {
		ContactList list = new ContactList();
		
		for (ContactItemBean item : this)
			list.add(item.toContactItem());
		
		return list;
	}
	
	@XStreamAlias("contactitem")
	public static class ContactItemBean {
		
		@XStreamAlias("contact")
		private ModelId contact;
		
		@XStreamAlias("link")
		private String link;
		
		public ContactItemBean() {
			this(null, null);
		}
		
		public ContactItemBean(ModelId contact, String link) {
			this.setContact(contact);
			this.setLink(link);
		}
		
		public ModelId getContact() {
			return this.contact;
		}
		
		public void setContact(ModelId contact) {
			this.contact = contact;
		}
		
		public String getLink() {
			return this.link;
		}
		
		public void setLink(String link) {
			this.link = link;
		}
		
		public ContactItem toContactItem() {
			if (this.contact == null)
				return new ContactItem(null, this.link);
			
			Contact contact = ContactFactory.getInstance().get(this.contact);
			if (contact == null)
				contact = ContactFactory.getInstance().createShell(this.contact);
			
			return new ContactItem(contact, this.link);
		}
		
	}
	
}
