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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.leclercb.commons.api.coder.AbstractXMLCoder;
import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.api.models.templates.TaskTemplateFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.TaskSorter;

public class TaskSearcherXMLCoder extends AbstractXMLCoder<TaskSearcher> {
	
	private static final TaskSorterXMLCoder TASK_SORTER_XML_CODER = new TaskSorterXMLCoder();
	private static final TaskFilterXMLCoder TASK_FILTER_XML_CODER = new TaskFilterXMLCoder();
	
	public TaskSearcherXMLCoder() {
		super("searcher");
	}
	
	@Override
	public TaskSearcher decode(Node node) throws FactoryCoderException {
		try {
			NodeList nSearcher = node.getChildNodes();
			
			TaskSearcherType type = TaskSearcherType.PERSONAL;
			int order = 0;
			String title = null;
			String icon = null;
			TaskFilter filter = null;
			TaskSorter sorter = null;
			TaskTemplate template = null;
			
			for (int i = 0; i < nSearcher.getLength(); i++) {
				if (nSearcher.item(i).getNodeName().equals("type")) {
					type = TaskSearcherType.valueOf(nSearcher.item(i).getTextContent());
				}
				
				if (nSearcher.item(i).getNodeName().equals("order")) {
					order = Integer.parseInt(nSearcher.item(i).getTextContent());
				}
				
				if (nSearcher.item(i).getNodeName().equals("title")) {
					title = nSearcher.item(i).getTextContent();
				}
				
				if (nSearcher.item(i).getNodeName().equals("icon")) {
					if (nSearcher.item(i).getTextContent().length() != 0)
						icon = nSearcher.item(i).getTextContent();
				}
				
				if (nSearcher.item(i).getNodeName().equals("sorter")) {
					sorter = this.decodeTaskSorter(nSearcher.item(i));
				}
				
				if (nSearcher.item(i).getNodeName().equals("filter")) {
					filter = this.decodeTaskFilter(nSearcher.item(i));
				}
				
				if (nSearcher.item(i).getNodeName().equals("template")) {
					template = TaskTemplateFactory.getInstance().get(
							new ModelId(nSearcher.item(i).getTextContent()));
				}
			}
			
			return new TaskSearcher(
					type,
					order,
					title,
					icon,
					filter,
					sorter,
					template);
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	private TaskSorter decodeTaskSorter(Node node) throws FactoryCoderException {
		return TASK_SORTER_XML_CODER.decode(node);
	}
	
	private TaskFilter decodeTaskFilter(Node node) throws FactoryCoderException {
		return TASK_FILTER_XML_CODER.decode(node);
	}
	
	@Override
	public void encode(
			Document document,
			Element root,
			TaskSearcher taskSearcher) {
		Element type = document.createElement("type");
		type.setTextContent(taskSearcher.getType().name());
		root.appendChild(type);
		
		Element order = document.createElement("order");
		order.setTextContent(taskSearcher.getOrder() + "");
		root.appendChild(order);
		
		Element title = document.createElement("title");
		title.setTextContent(taskSearcher.getTitle());
		root.appendChild(title);
		
		Element icon = document.createElement("icon");
		icon.setTextContent(taskSearcher.getIcon());
		root.appendChild(icon);
		
		Element sorter = document.createElement("sorter");
		root.appendChild(sorter);
		
		Element filter = document.createElement("filter");
		root.appendChild(filter);
		
		if (taskSearcher.getTemplate() != null) {
			Element template = document.createElement("template");
			template.setTextContent(taskSearcher.getTemplate().getModelId().getId());
			root.appendChild(template);
		}
		
		this.encodeTaskSorter(document, sorter, taskSearcher.getSorter());
		this.encodeTaskFilter(document, filter, taskSearcher.getFilter());
	}
	
	private void encodeTaskSorter(
			Document document,
			Element root,
			TaskSorter sorter) {
		TASK_SORTER_XML_CODER.encode(document, root, sorter);
	}
	
	private void encodeTaskFilter(
			Document document,
			Element root,
			TaskFilter filter) {
		TASK_FILTER_XML_CODER.encode(document, root, filter);
	}
	
}
