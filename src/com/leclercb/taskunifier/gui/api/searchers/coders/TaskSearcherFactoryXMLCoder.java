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

import java.util.Calendar;
import java.util.List;

import javax.swing.SortOrder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.leclercb.commons.api.coder.AbstractFactoryXMLCoder;
import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.XMLUtils;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.CalendarCondition;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.DaysCondition;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.EnumCondition;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.ModelCondition;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.NumberCondition;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.StringCondition;
import com.leclercb.taskunifier.gui.api.searchers.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskSearcherFactoryXMLCoder extends AbstractFactoryXMLCoder {
	
	private static final String NULL_STRING_VALUE = "{{NULL}}";
	
	public TaskSearcherFactoryXMLCoder() {
		super("tasksearchers");
	}
	
	@Override
	public void decode(Node root) throws FactoryCoderException {
		CheckUtils.isNotNull(root, "Root cannot be null");
		
		try {
			NodeList nTaskSearchers = root.getChildNodes();
			
			for (int i = 0; i < nTaskSearchers.getLength(); i++) {
				if (!nTaskSearchers.item(i).getNodeName().equals("searcher"))
					continue;
				
				Node nTaskSearcher = nTaskSearchers.item(i);
				
				this.decodeTaskSearcher(nTaskSearcher);
			}
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	private void decodeTaskSearcher(Node node) throws FactoryCoderException {
		try {
			NodeList nSearcher = node.getChildNodes();
			
			TaskSearcherType type = TaskSearcherType.PERSONAL;
			String title = null;
			String icon = null;
			TaskFilter filter = null;
			TaskSorter sorter = null;
			
			for (int i = 0; i < nSearcher.getLength(); i++) {
				if (nSearcher.item(i).getNodeName().equals("type")) {
					type = TaskSearcherType.valueOf(nSearcher.item(i).getTextContent());
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
			}
			
			TaskSearcherFactory.getInstance().create(
					type,
					title,
					icon,
					filter,
					sorter);
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	private TaskSorter decodeTaskSorter(Node node) throws FactoryCoderException {
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
	
	private TaskFilter decodeTaskFilter(Node node) throws FactoryCoderException {
		try {
			NodeList nFilter = node.getChildNodes();
			TaskFilter filter = new TaskFilter();
			filter.setLink(TaskFilter.Link.valueOf(XMLUtils.getAttributeValue(
					node,
					"link")));
			
			for (int i = 0; i < nFilter.getLength(); i++) {
				if (nFilter.item(i).getNodeName().equals("element")) {
					NodeList nElement = nFilter.item(i).getChildNodes();
					TaskFilterElement element = null;
					
					TaskColumn column = null;
					String conditionClass = null;
					String enumName = null;
					Node valueNode = null;
					String valueStr = null;
					
					for (int j = 0; j < nElement.getLength(); j++) {
						if (nElement.item(j).getNodeName().equals("column")) {
							column = TaskColumn.valueOf(nElement.item(j).getTextContent());
						}
						
						if (nElement.item(j).getNodeName().equals("condition")) {
							String[] values = nElement.item(j).getTextContent().split(
									"\\.");
							conditionClass = values[0];
							enumName = values[1];
						}
						
						if (nElement.item(j).getNodeName().equals("value")) {
							valueNode = nElement.item(j);
							valueStr = nElement.item(j).getTextContent();
							
							if (valueStr.equals(NULL_STRING_VALUE))
								valueStr = null;
						}
					}
					
					if (conditionClass.equals("CalendarCondition")) {
						CalendarCondition condition = CalendarCondition.valueOf(enumName);
						Calendar value = null;
						
						if (valueStr != null) {
							value = Calendar.getInstance();
							value.setTimeInMillis(Long.parseLong(valueStr));
						}
						
						element = new TaskFilterElement(
								column,
								condition,
								value);
					} else if (conditionClass.equals("DaysCondition")) {
						DaysCondition condition = DaysCondition.valueOf(enumName);
						Integer value = null;
						
						if (valueStr != null) {
							value = Integer.parseInt(valueStr);
						}
						
						element = new TaskFilterElement(
								column,
								condition,
								value);
					} else if (conditionClass.equals("StringCondition")) {
						StringCondition condition = StringCondition.valueOf(enumName);
						String value = null;
						
						if (valueStr != null) {
							value = valueStr;
						}
						
						element = new TaskFilterElement(
								column,
								condition,
								value);
					} else if (conditionClass.equals("NumberCondition")) {
						NumberCondition condition = NumberCondition.valueOf(enumName);
						Number value = null;
						
						if (valueStr != null) {
							value = Double.parseDouble(valueStr);
						}
						
						element = new TaskFilterElement(
								column,
								condition,
								value);
					} else if (conditionClass.equals("EnumCondition")) {
						EnumCondition condition = EnumCondition.valueOf(enumName);
						Enum<?> value = null;
						
						if (valueStr != null) {
							String valueClass = valueStr.substring(
									0,
									valueStr.lastIndexOf("#"));
							String valueEnum = valueStr.substring(
									valueStr.lastIndexOf("#") + 1,
									valueStr.length());
							
							Object[] enums = Class.forName(valueClass).getEnumConstants();
							
							for (int j = 0; j < enums.length; j++) {
								Enum<?> e = (Enum<?>) enums[j];
								if (e.name().equals(valueEnum))
									value = e;
							}
						}
						
						element = new TaskFilterElement(
								column,
								condition,
								value);
					} else if (conditionClass.equals("ModelCondition")) {
						ModelCondition condition = ModelCondition.valueOf(enumName);
						Model value = null;
						
						if (valueStr != null) {
							try {
								Boolean newId = XMLUtils.getBooleanAttributeValue(
										valueNode,
										"isnew");
								
								if (newId == null)
									newId = false;
								
								if (column.equals(TaskColumn.CONTEXT))
									value = ContextFactory.getInstance().get(
											new ModelId(newId, valueStr));
								else if (column.equals(TaskColumn.FOLDER))
									value = FolderFactory.getInstance().get(
											new ModelId(newId, valueStr));
								else if (column.equals(TaskColumn.GOAL))
									value = GoalFactory.getInstance().get(
											new ModelId(newId, valueStr));
								else if (column.equals(TaskColumn.LOCATION))
									value = LocationFactory.getInstance().get(
											new ModelId(newId, valueStr));
								else if (column.equals(TaskColumn.PARENT))
									value = TaskFactory.getInstance().get(
											new ModelId(newId, valueStr));
							} catch (Exception e) {
								value = null;
							}
						}
						
						if (valueStr == null || value != null)
							element = new TaskFilterElement(
									column,
									condition,
									value);
					}
					
					if (element != null)
						filter.addElement(element);
				}
				
				if (nFilter.item(i).getNodeName().equals("filter")) {
					filter.addFilter(this.decodeTaskFilter(nFilter.item(i)));
				}
			}
			
			return filter;
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	@Override
	public void encode(Document document, Element root) {
		List<TaskSearcher> searchers = TaskSearcherFactory.getInstance().getList();
		
		for (TaskSearcher taskSearcher : searchers) {
			Element searcher = document.createElement("searcher");
			root.appendChild(searcher);
			
			Element type = document.createElement("type");
			type.setTextContent(taskSearcher.getType().name());
			searcher.appendChild(type);
			
			Element title = document.createElement("title");
			title.setTextContent(taskSearcher.getTitle());
			searcher.appendChild(title);
			
			Element icon = document.createElement("icon");
			icon.setTextContent(taskSearcher.getIcon());
			searcher.appendChild(icon);
			
			Element sorter = document.createElement("sorter");
			searcher.appendChild(sorter);
			
			Element filter = document.createElement("filter");
			searcher.appendChild(filter);
			
			this.encodeTaskSorter(document, sorter, taskSearcher.getSorter());
			this.encodeTaskFilter(document, filter, taskSearcher.getFilter());
		}
	}
	
	private void encodeTaskSorter(
			Document document,
			Element root,
			TaskSorter sorter) {
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
	
	private void encodeTaskFilter(
			Document document,
			Element root,
			TaskFilter filter) {
		root.setAttribute("link", filter.getLink().name());
		
		for (TaskFilterElement e : filter.getElements()) {
			Element element = document.createElement("element");
			root.appendChild(element);
			
			Element column = document.createElement("column");
			column.setTextContent(e.getColumn().name());
			element.appendChild(column);
			
			Element condition = document.createElement("condition");
			element.appendChild(condition);
			
			Element value = document.createElement("value");
			element.appendChild(value);
			
			if (e.getCondition() instanceof TaskFilter.CalendarCondition) {
				condition.setTextContent("CalendarCondition."
						+ e.getCondition().name());
				
				if (e.getValue() != null)
					value.setTextContent(((Calendar) e.getValue()).getTimeInMillis()
							+ "");
			} else if (e.getCondition() instanceof TaskFilter.DaysCondition) {
				condition.setTextContent("DaysCondition."
						+ e.getCondition().name());
				
				if (e.getValue() != null)
					value.setTextContent((e.getValue()) + "");
			} else if (e.getCondition() instanceof TaskFilter.StringCondition) {
				condition.setTextContent("StringCondition."
						+ e.getCondition().name());
				
				if (e.getValue() != null)
					value.setTextContent((String) e.getValue());
			} else if (e.getCondition() instanceof TaskFilter.NumberCondition) {
				condition.setTextContent("NumberCondition."
						+ e.getCondition().name());
				
				if (e.getValue() != null)
					value.setTextContent((e.getValue()) + "");
			} else if (e.getCondition() instanceof TaskFilter.EnumCondition) {
				condition.setTextContent("EnumCondition."
						+ e.getCondition().name());
				
				if (e.getValue() != null)
					value.setTextContent(e.getValue().getClass().getName()
							+ "#"
							+ ((Enum<?>) e.getValue()).name());
			} else if (e.getCondition() instanceof TaskFilter.ModelCondition) {
				condition.setTextContent("ModelCondition."
						+ e.getCondition().name());
				
				if (e.getValue() != null) {
					ModelId id = ((Model) e.getValue()).getModelId();
					
					value.setAttribute("isnew", id.isNewId() + "");
					
					value.setTextContent(id.getId());
				}
			}
			
			if (e.getValue() == null)
				value.setTextContent(NULL_STRING_VALUE);
		}
		
		for (TaskFilter f : filter.getFilters()) {
			Element eF = document.createElement("filter");
			root.appendChild(eF);
			
			this.encodeTaskFilter(document, eF, f);
		}
	}
	
}
