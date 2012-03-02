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

import java.util.Calendar;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.beans.ContactBean;
import com.leclercb.taskunifier.api.models.beans.ModelBean;

public class Contact extends AbstractModel implements Model {
	
	public static final String PROP_FIRSTNAME = "firstName";
	public static final String PROP_LASTNAME = "lastName";
	public static final String PROP_EMAIL = "email";
	
	private String firstName;
	private String lastName;
	private String email;
	
	protected Contact(ContactBean bean, boolean loadReferenceIds) {
		this(bean.getModelId(), bean.getTitle());
		this.loadBean(bean, loadReferenceIds);
	}
	
	protected Contact(String title) {
		this(new ModelId(), title);
	}
	
	protected Contact(ModelId modelId, String title) {
		super(modelId, title);
		
		this.setFirstName(null);
		this.setLastName(null);
		this.setEmail(null);
		
		this.getFactory().register(this);
	}
	
	@Override
	public Contact clone(ModelId modelId) {
		Contact contact = this.getFactory().create(modelId, this.getTitle());
		
		contact.setFirstName(this.getFirstName());
		contact.setLastName(this.getLastName());
		contact.setEmail(this.getEmail());
		
		// After all other setXxx methods
		contact.setOrder(this.getOrder());
		contact.addProperties(this.getProperties());
		contact.setModelStatus(this.getModelStatus());
		contact.setModelCreationDate(Calendar.getInstance());
		contact.setModelUpdateDate(Calendar.getInstance());
		
		return contact;
	}
	
	@Override
	public ContactFactory<Contact, ContactBean> getFactory() {
		return ContactFactory.getInstance();
	}
	
	@Override
	public ModelType getModelType() {
		return ModelType.CONTACT;
	}
	
	@Override
	public void loadBean(ModelBean b, boolean loadReferenceIds) {
		CheckUtils.isNotNull(b);
		CheckUtils.isInstanceOf(b, ContactBean.class);
		
		ContactBean bean = (ContactBean) b;
		
		this.setFirstName(bean.getFirstName());
		this.setLastName(bean.getLastName());
		this.setEmail(bean.getEmail());
		
		super.loadBean(bean, loadReferenceIds);
	}
	
	@Override
	public ContactBean toBean() {
		ContactBean bean = (ContactBean) super.toBean();
		
		bean.setFirstName(this.getFirstName());
		bean.setLastName(this.getLastName());
		bean.setEmail(this.getEmail());
		
		return bean;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String firstName) {
		if (!this.checkBeforeSet(this.getFirstName(), firstName))
			return;
		
		String oldFirstName = this.firstName;
		this.firstName = firstName;
		this.updateProperty(PROP_FIRSTNAME, oldFirstName, firstName);
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		if (!this.checkBeforeSet(this.getLastName(), lastName))
			return;
		
		String oldLastName = this.lastName;
		this.lastName = lastName;
		this.updateProperty(PROP_LASTNAME, oldLastName, lastName);
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		if (!this.checkBeforeSet(this.getEmail(), email))
			return;
		
		String oldEmail = this.email;
		this.email = email;
		this.updateProperty(PROP_EMAIL, oldEmail, email);
	}
	
}
