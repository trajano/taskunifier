package com.leclercb.taskunifier.gui.commons.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultComboBoxModel;

import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.gui.main.Main;

public class SynchronizerChoiceModel extends DefaultComboBoxModel {
	
	public SynchronizerChoiceModel() {
		super(SynchronizerChoice.values());
		
		Main.SETTINGS.addPropertyChangeListener(
				"api.id",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						int end = SynchronizerChoiceModel.this.getSize() - 1;
						
						if (end < 0)
							end = 0;
						
						SynchronizerChoiceModel.this.fireContentsChanged(
								SynchronizerChoiceModel.this,
								0,
								end);
					}
					
				});
	}
	
}
