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
package com.leclercb.taskunifier.gui.components.import_data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import a_vcard.android.syncml.pim.PropertyNode;
import a_vcard.android.syncml.pim.VDataBuilder;
import a_vcard.android.syncml.pim.VNode;
import a_vcard.android.syncml.pim.vcard.VCardException;
import a_vcard.android.syncml.pim.vcard.VCardParser;

import com.leclercb.taskunifier.api.models.Contact;
import com.leclercb.taskunifier.api.models.ContactFactory;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ImportVCardDialog extends AbstractImportDialog {
	
	private static ImportVCardDialog INSTANCE;
	
	public static ImportVCardDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ImportVCardDialog();
		
		return INSTANCE;
	}
	
	private ImportVCardDialog() {
		super(
				Translations.getString("action.import_vcard"),
				false,
				"vcf",
				Translations.getString("general.vcard_files"));
	}
	
	@Override
	public void deleteExistingValue() {
		
	}
	
	@Override
	protected void importFromFile(String file) throws Exception {
		VCardParser parser = new VCardParser();
		VDataBuilder builder = new VDataBuilder();
		
		String vcard = FileUtils.readFileToString(new File(file), "UTF-8");
		
		boolean parsed = parser.parse(vcard, "UTF-8", builder);
		if (!parsed)
			throw new VCardException("Could not parse vCard file: " + file);
		
		List<VNode> contacts = builder.vNodeList;
		for (VNode c : contacts) {
			ArrayList<PropertyNode> props = c.propList;
			
			String fullName = null;
			String firstName = null;
			String lastName = null;
			String email = null;
			
			for (PropertyNode prop : props) {
				if (prop.propValue == null)
					continue;
				
				if ("FN".equals(prop.propName)) {
					fullName = prop.propValue;
					continue;
				}
				
				if ("N".equals(prop.propName)) {
					String[] array = prop.propValue.split(";");
					
					if (array.length >= 1)
						lastName = array[0];
					
					if (array.length >= 2)
						firstName = array[1];
					
					continue;
				}
				
				if ("EMAIL".equals(prop.propName)) {
					email = prop.propValue;
					continue;
				}
			}
			
			if (fullName == null)
				fullName = lastName + " " + firstName;
			
			Contact contact = ContactFactory.getInstance().create(fullName);
			contact.setFirstName(firstName);
			contact.setLastName(lastName);
			contact.setEmail(email);
		}
		
	}
	
}
