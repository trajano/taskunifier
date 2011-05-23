package com.leclercb.taskunifier.gui.components.configuration.fields.theme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldTypeExt;
import com.leclercb.taskunifier.gui.main.Main;

public class LookAndFeelFieldType extends ConfigurationFieldTypeExt.ComboBox {
	
	public LookAndFeelFieldType() {
		super(createModel(), Main.SETTINGS, "theme.lookandfeel");
	}
	
	@Override
	public Object getPropertyValue() {
		if (Main.SETTINGS.getStringProperty("theme.lookandfeel") != null)
			return LookAndFeelUtils.getLookAndFeel(Main.SETTINGS.getStringProperty("theme.lookandfeel"));
		else
			return null;
	}
	
	@Override
	public void saveAndApplyConfig() {
		Main.SETTINGS.setStringProperty(
				"theme.lookandfeel",
				((LookAndFeelDescriptor) this.getFieldValue()).getIdentifier());
	}
	
	private static DefaultComboBoxModel createModel() {
		List<LookAndFeelDescriptor> lookAndFeels = new ArrayList<LookAndFeelDescriptor>(
				LookAndFeelUtils.getLookAndFeels());
		Collections.sort(lookAndFeels, new Comparator<LookAndFeelDescriptor>() {
			
			@Override
			public int compare(
					LookAndFeelDescriptor laf1,
					LookAndFeelDescriptor laf2) {
				return laf1.getName().compareTo(laf2.getName());
			}
			
		});
		
		return new DefaultComboBoxModel(lookAndFeels.toArray());
	}
	
}
