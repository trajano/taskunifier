package com.leclercb.taskunifier.gui.utils;

import com.leclercb.taskunifier.api.models.Contact;
import com.leclercb.taskunifier.api.models.ContactFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.gui.main.Main;

public final class ContactUtils {
	
	public static final Contact getCurrentUser() {
		try {
			ModelId modelId = Main.getUserSettings().getObjectProperty(
					"general.contact.me",
					ModelId.class);
			
			return ContactFactory.getInstance().get(modelId);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final void setCurrentUser(Contact contact) {
		if (contact == null) {
			Main.getUserSettings().remove("general.contact.me");
			return;
		}
		
		Main.getUserSettings().setObjectProperty(
				"general.contact.me",
				ModelId.class,
				contact.getModelId());
	}
	
}
