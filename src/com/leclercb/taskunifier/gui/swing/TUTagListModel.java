package com.leclercb.taskunifier.gui.swing;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.api.models.TagList;
import com.leclercb.taskunifier.api.models.utils.TaskTagList;

public class TUTagListModel extends DefaultListModel implements ListChangeSupported, ListChangeListener, ItemListener {
	
	private ListChangeSupport listChangeSupport;
	
	public TUTagListModel() {
		this.listChangeSupport = new ListChangeSupport(this);
		
		TagList tags = TaskTagList.getInstance().getTags();
		
		for (Tag tag : tags) {
			JCheckBox cb = new JCheckBox(tag.toString());
			this.addElement(cb);
			cb.addItemListener(this);
		}
		
		TaskTagList.getInstance().addListChangeListener(this);
	}
	
	public void updateCheckBoxStates(String[] tags) {
		for (int i = 0; i < this.size(); i++) {
			JCheckBox checkBox = (JCheckBox) this.getElementAt(i);
			
			boolean selected = false;
			
			for (String tag : tags) {
				if (checkBox.getText().equalsIgnoreCase(tag)) {
					selected = true;
					break;
				}
			}
			
			checkBox.setSelected(selected);
		}
		
		this.fireContentsChanged(this, 0, this.size());
	}
	
	private JCheckBox findCheckBox(Tag tag) {
		for (int i = 0; i < this.size(); i++) {
			JCheckBox checkBox = (JCheckBox) this.getElementAt(i);
			
			if (new Tag(checkBox.getText()).equals(tag)) {
				return checkBox;
			}
		}
		
		return null;
	}
	
	@Override
	public void listChange(ListChangeEvent evt) {
		Tag tag = (Tag) evt.getValue();
		
		if (evt.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			JCheckBox cb = new JCheckBox(tag.toString());
			this.insertElementAt(cb, evt.getIndex());
			cb.addItemListener(this);
		} else if (evt.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
			JCheckBox cb = this.findCheckBox(tag);
			
			if (cb == null)
				return;
			
			cb.removeItemListener(this);
			this.removeElement(cb);
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent event) {
		JCheckBox cb = (JCheckBox) event.getSource();
		int changeType = (cb.isSelected() ? ListChangeEvent.VALUE_ADDED : ListChangeEvent.VALUE_REMOVED);
		
		this.listChangeSupport.fireListChange(
				changeType,
				this.indexOf(cb),
				new Tag(cb.getText()));
	}
	
	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
}
