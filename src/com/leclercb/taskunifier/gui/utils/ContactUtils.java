package com.leclercb.taskunifier.gui.utils;

import com.leclercb.taskunifier.api.models.Contact;
import com.leclercb.taskunifier.api.models.ContactFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.gui.main.Main;

public final class ContactUtils {
	
	public static final Contact getCurrentUser() {
		try {
			ModelId modelId = Main.getSettings().getObjectProperty(
					"general.current_user",
					ModelId.class);
			
			return ContactFactory.getInstance().get(modelId);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final void setCurrentUser(Contact contact) {
		if (contact == null) {
			Main.getSettings().remove("general.current_user");
			return;
		}
		
		Main.getSettings().setObjectProperty(
				"general.current_user",
				ModelId.class,
				contact.getModelId());
	}
	
}
