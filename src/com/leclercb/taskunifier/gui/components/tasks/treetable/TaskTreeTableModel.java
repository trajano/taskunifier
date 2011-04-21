package com.leclercb.taskunifier.gui.components.tasks.treetable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskTreeTableModel extends DefaultTreeTableModel implements ListChangeListener, PropertyChangeListener {
	
	public TaskTreeTableModel(TreeTableNode root) {
		super(root);
	}
	
	@Override
	public int getColumnCount() {
		return TaskColumn.values().length;
	}
	
	@Override
	public String getColumnName(int column) {
		return TaskColumn.values()[column].getLabel();
	}
	
	@Override
	public Class<?> getColumnClass(int column) {
		return TaskColumn.values()[column].getType();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void listChange(ListChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
