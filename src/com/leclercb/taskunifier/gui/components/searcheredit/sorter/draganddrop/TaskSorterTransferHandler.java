package com.leclercb.taskunifier.gui.components.searcheredit.sorter.draganddrop;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.commons.comparators.TaskSorterElementComparator;
import com.leclercb.taskunifier.gui.commons.transfer.TaskSorterTransferData;
import com.leclercb.taskunifier.gui.commons.transfer.TaskSorterTransferable;
import com.leclercb.taskunifier.gui.components.searcheredit.sorter.TaskSorterTable;

public class TaskSorterTransferHandler extends TransferHandler {
	
	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDataFlavorSupported(TaskSorterTransferable.SORTER_FLAVOR))
			return false;
		
		return true;
	}
	
	@Override
	protected Transferable createTransferable(JComponent c) {
		TaskSorterTable table = (TaskSorterTable) c;
		TaskSorterElement[] elements = table.getSelectedTaskSorterElements();
		
		int[] indexes = new int[elements.length];
		for (int i = 0; i < elements.length; i++)
			indexes[i] = table.getTaskSorter().getIndexOf(elements[i]);
		
		return new TaskSorterTransferable(new TaskSorterTransferData(indexes));
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.MOVE;
	}
	
	@Override
	public boolean importData(TransferSupport support) {
		if (!this.canImport(support)) {
			return false;
		}
		
		Transferable t = support.getTransferable();
		TaskSorterTransferData data = null;
		
		try {
			data = (TaskSorterTransferData) t.getTransferData(TaskSorterTransferable.SORTER_FLAVOR);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		if (support.isDrop()) {
			TaskSorterTable table = (TaskSorterTable) support.getComponent();
			JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
			
			// Import : If insert row
			if (dl.isInsertRow()) {
				List<TaskSorterElement> dragElements = new ArrayList<TaskSorterElement>();
				for (int i : data.getElementIndexes()) {
					dragElements.add(table.getTaskSorter().getElement(i));
				}
				
				TaskSorterElement dropElement = table.getTaskSorterElement(table.rowAtPoint(dl.getDropPoint()));
				
				List<TaskSorterElement> elements = new ArrayList<TaskSorterElement>(
						table.getTaskSorter().getElements());
				Collections.sort(elements, new TaskSorterElementComparator());
				
				int index = elements.indexOf(dropElement);
				
				elements.removeAll(dragElements);
				
				elements.addAll(index, dragElements);
				
				int order = 1;
				for (TaskSorterElement element : elements) {
					element.setOrder(order++);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {

	}
	
}
