package com.leclercb.taskunifier.gui.commons.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultComboBoxModel;

import com.leclercb.commons.api.utils.ArrayUtils;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskPriorityModel extends DefaultComboBoxModel implements PropertyChangeListener {
	
	public TaskPriorityModel(boolean firstNull) {
		super(
				ArrayUtils.concat(
						(firstNull ? new TaskPriority[] { null } : new TaskPriority[0]),
						TaskPriority.values()));
		
		Main.SETTINGS.addPropertyChangeListener(this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().startsWith("theme.color.priority")) {
			this.fireContentsChanged(this, 0, this.getSize() - 1);
		}
	}
	
}
