package com.leclercb.taskunifier.gui.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.commons.gui.swing.models.DefaultSortedComboBoxModel;
import com.leclercb.taskunifier.gui.template.Template;
import com.leclercb.taskunifier.gui.template.TemplateFactory;

public class TemplateModel extends DefaultSortedComboBoxModel<Template> implements ListChangeListener, PropertyChangeListener {
	
	public TemplateModel() {
		super(new Comparator<Template>() {
			
			@Override
			public int compare(Template t1, Template t2) {
				String s1 = t1 == null ? null : t1.getTitle();
				String s2 = t2 == null ? null : t2.getTitle();
				
				int result = CompareUtils.compare(s1, s2);
				
				if (result == 0) {
					s1 = t1 == null ? null : t1.getId();
					s2 = t2 == null ? null : t2.getId();
					
					result = CompareUtils.compare(s1, s2);
				}
				
				return result;
			}
			
		});
		
		List<Template> templates = TemplateFactory.getInstance().getList();
		for (Template template : templates)
			this.addElement(template);
		
		TemplateFactory.getInstance().addListChangeListener(this);
		TemplateFactory.getInstance().addPropertyChangeListener(this);
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			this.addElement((Template) event.getValue());
		} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			this.removeElement((Template) event.getValue());
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
		
		int index = this.getIndexOf((Template) event.getSource());
		this.fireContentsChanged(this, index, index);
	}
	
}
