package com.leclercb.taskunifier.gui.api.models.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Contact;
import com.leclercb.taskunifier.api.models.ContactFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.beans.ContactGroupBean;
import com.leclercb.taskunifier.api.models.beans.ContactGroupBean.ContactItemBean;
import com.leclercb.taskunifier.gui.api.models.beans.ComContactGroupBean.ComContactItemBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ComContactGroupBean implements Cloneable, Serializable, Iterable<ComContactItemBean> {
	
	@XStreamAlias("comcontactlist")
	private List<ComContactItemBean> contacts;
	
	public ComContactGroupBean() {
		this.contacts = new ArrayList<ComContactItemBean>();
	}
	
	public ComContactGroupBean(ContactGroupBean contacts) {
		this.contacts = new ArrayList<ComContactItemBean>();
		for (ContactItemBean contact : contacts) {
			Contact c = ContactFactory.getInstance().get(contact.getContact());
			
			if (c == null)
				continue;
			
			this.contacts.add(new ComContactItemBean(
					c.getTitle(),
					contact.getLink()));
		}
	}
	
	@Override
	protected ComContactGroupBean clone() {
		ComContactGroupBean list = new ComContactGroupBean();
		list.contacts.addAll(this.contacts);
		return list;
	}
	
	@Override
	public Iterator<ComContactItemBean> iterator() {
		return this.contacts.iterator();
	}
	
	public List<ComContactItemBean> getList() {
		return Collections.unmodifiableList(new ArrayList<ComContactItemBean>(
				this.contacts));
	}
	
	public void add(ComContactItemBean item) {
		CheckUtils.isNotNull(item);
		this.contacts.add(item);
	}
	
	public void addAll(Collection<ComContactItemBean> items) {
		if (items == null)
			return;
		
		for (ComContactItemBean item : items)
			this.add(item);
	}
	
	public void remove(ComContactItemBean item) {
		CheckUtils.isNotNull(item);
		this.contacts.remove(item);
	}
	
	public void clear() {
		for (ComContactItemBean item : this.getList())
			this.remove(item);
	}
	
	public int size() {
		return this.contacts.size();
	}
	
	public int getIndexOf(ComContactItemBean item) {
		return this.contacts.indexOf(item);
	}
	
	public ComContactItemBean get(int index) {
		return this.contacts.get(index);
	}
	
	public ContactGroupBean toContactGroupBean() {
		ContactGroupBean list = new ContactGroupBean();
		
		for (ComContactItemBean item : this)
			list.add(item.toContactItemBean());
		
		return list;
	}
	
	@XStreamAlias("comcontactitem")
	public static class ComContactItemBean {
		
		@XStreamAlias("contact")
		private String contact;
		
		@XStreamAlias("link")
		private String link;
		
		public ComContactItemBean() {
			this(null, null);
		}
		
		public ComContactItemBean(String contact, String link) {
			this.setContact(contact);
			this.setLink(link);
		}
		
		public String getContact() {
			return this.contact;
		}
		
		public void setContact(String contact) {
			this.contact = contact;
		}
		
		public String getLink() {
			return this.link;
		}
		
		public void setLink(String link) {
			this.link = link;
		}
		
		public ContactItemBean toContactItemBean() {
			if (this.contact == null)
				return new ContactItemBean(null, this.link);
			
			ModelId modelId = null;
			
			List<Contact> contacts = ContactFactory.getInstance().getList();
			for (Contact contact : contacts) {
				if (contact.getModelStatus().isEndUserStatus()) {
					if (contact.getTitle().equalsIgnoreCase(this.contact)) {
						modelId = contact.getModelId();
					}
				}
			}
			
			return new ContactItemBean(modelId, this.link);
		}
		
	}
	
}
