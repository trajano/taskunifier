/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
					
					TaskColumn column = null;
					SortOrder sortOrder = null;
					
					for (int j = 0; j < nElement.getLength(); j++) {
						if (nElement.item(j).getNodeName().equals("column")) {
							column = TaskColumn.valueOf(nElement.item(j).getTextContent());
						}
						
						if (nElement.item(j).getNodeName().equals("sortorder")) {
							sortOrder = SortOrder.valueOf(nElement.item(j).getTextContent());
						}
					}
					
					sorter.addElement(new TaskSorterElement(column, sortOrder));
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
			
			Element column = document.createElement("column");
			column.setTextContent(e.getProperty().name());
			element.appendChild(column);
			
			Element sortOrder = document.createElement("sortorder");
			sortOrder.setTextContent(e.getSortOrder().name());
			element.appendChild(sortOrder);
		}
	}
	
}
