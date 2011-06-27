package com.leclercb.taskunifier.gui.components.configuration.fields.theme;

import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ShowBadgesFieldType extends ConfigurationFieldTypeExt.CheckBox {
	
	public ShowBadgesFieldType() {
		super(Main.SETTINGS, "searcher.show_badges");
	}
	
	@Override
	public Boolean getPropertyValue() {
		return Main.SETTINGS.getBooleanProperty("searcher.show_badges");
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setBooleanProperty(
				"searcher.show_badges",
				this.getFieldValue());
	}
	
}
