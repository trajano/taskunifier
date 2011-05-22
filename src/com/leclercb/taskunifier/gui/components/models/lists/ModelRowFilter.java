package com.leclercb.taskunifier.gui.components.models.lists;

import javax.swing.ListModel;
import javax.swing.RowFilter;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.commons.models.ModelListModel;

public class ModelRowFilter extends RowFilter<ListModel, Integer> {
	
	private String title;
	
	public ModelRowFilter() {
		
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
		ModelListModel modelListModel = (ModelListModel) entry.getModel();
		Model model = (Model) modelListModel.getElementAt(entry.getIdentifier());
		
		if (title == null)
			return true;
		
		return model.getTitle().toLowerCase().contains(title.toLowerCase());
	}
	
}
