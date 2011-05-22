package com.leclercb.taskunifier.gui.components.models.lists;

import java.beans.PropertyChangeListener;

import javax.swing.ListModel;
import javax.swing.RowFilter;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.commons.models.ModelListModel;

public class ModelRowFilter extends RowFilter<ListModel, Integer> implements PropertyChangeSupported {
	
	public static final String PROP_TITLE = "title";
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private String title;
	
	public ModelRowFilter() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		String oldTitle = this.title;
		this.title = title;
		this.propertyChangeSupport.firePropertyChange(
				PROP_TITLE,
				oldTitle,
				title);
	}
	
	@Override
	public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
		ModelListModel modelListModel = (ModelListModel) entry.getModel();
		Model model = (Model) modelListModel.getElementAt(entry.getIdentifier());
		
		if (this.title == null)
			return true;
		
		return model.getTitle().toLowerCase().contains(this.title.toLowerCase());
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
