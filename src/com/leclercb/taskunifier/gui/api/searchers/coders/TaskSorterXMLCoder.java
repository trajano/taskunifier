package com.leclercb.taskunifier.gui.api.searchers.coders;

import javax.swing.SortOrder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.leclercb.commons.api.coder.AbstractXMLCoder;
import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskSorterXMLCoder extends AbstractXMLCoder<TaskSorter> {
	
	public TaskSorterXMLCoder() {
		super("sorter");
	}
	
	@Override
	protected TaskSorter decode(Node node) throws FactoryCoderException {
		try {
			NodeList nSorter = node.getChildNodes();
			TaskSorter sorter = new TaskSorter();
			
			for (int i = 0; i < nSorter.getLength(); i++) {
				if (nSorter.item(i).getNodeName().equals("element")) {
					NodeList nElement = nSorter.item(i).getChildNodes();
					
					int order = 0;
					TaskColumn column = null;
					SortOrder sortOrder = null;
					
					for (int j = 0; j < nElement.getLength(); j++) {
						if (nElement.item(j).getNodeName().equals("order")) {
							order = Integer.parseInt(nElement.item(j).getTextContent());
						}
						
						if (nElement.item(j).getNodeName().equals("column")) {
							column = TaskColumn.valueOf(nElement.item(j).getTextContent());
						}
						
						if (nElement.item(j).getNodeName().equals("sortorder")) {
							sortOrder = SortOrder.valueOf(nElement.item(j).getTextContent());
						}
					}
					
					sorter.addElement(new TaskSorterElement(
							order,
							column,
							sortOrder));
				}
			}
			
			return sorter;
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void encode(Document document, Element root, TaskSorter sorter) {
		for (TaskSorterElement e : sorter.getElements()) {
			Element element = document.createElement("element");
			root.appendChild(element);
			
			Element order = document.createElement("order");
			order.setTextContent(e.getOrder() + "");
			element.appendChild(order);
			
			Element column = document.createElement("column");
			column.setTextContent(e.getColumn().name());
			element.appendChild(column);
			
			Element sortOrder = document.createElement("sortorder");
			sortOrder.setTextContent(e.getSortOrder().name());
			element.appendChild(sortOrder);
		}
	}
	
}
