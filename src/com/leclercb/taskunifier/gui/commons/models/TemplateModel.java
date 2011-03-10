package com.leclercb.taskunifier.gui.commons.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.gui.swing.models.DefaultSortedComboBoxModel;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.api.templates.TemplateFactory;

public class TemplateModel extends DefaultSortedComboBoxModel implements ListChangeListener, PropertyChangeListener {
	
	public TemplateModel() {
		super(new TemplateComparator());
		this.initialize();
	}
	
	private void initialize() {
		List<Template> templates = TemplateFactory.getInstance().getList();
		for (Template template : templates)
			this.addElement(template);
		
		TemplateFactory.getInstance().addListChangeListener(this);
		TemplateFactory.getInstance().addPropertyChangeListener(this);
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			this.addElement(event.getValue());
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.removeElement(event.getValue());
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource() instanceof TemplateFactory) {
			if (event.getPropertyName().equals(
					TemplateFactory.PROP_DEFAULT_TEMPLATE))
				this.fireContentsChanged(
						this,
						0,
						TemplateFactory.getInstance().size() - 1);
			
			return;
		}
		
		int index = this.getIndexOf(event.getSource());
		this.fireContentsChanged(this, index, index);
	}
	
}
