/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.searcher.coder;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.SortOrder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.leclercb.taskunifier.api.coder.FactoryCoder;
import com.leclercb.taskunifier.api.coder.exc.FactoryCoderException;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.utils.XMLUtils;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.searcher.TaskFilter;
import com.leclercb.taskunifier.gui.searcher.TaskFilter.CalendarCondition;
import com.leclercb.taskunifier.gui.searcher.TaskFilter.DaysCondition;
import com.leclercb.taskunifier.gui.searcher.TaskFilter.EnumCondition;
import com.leclercb.taskunifier.gui.searcher.TaskFilter.ModelCondition;
import com.leclercb.taskunifier.gui.searcher.TaskFilter.StringCondition;
import com.leclercb.taskunifier.gui.searcher.TaskFilter.TaskFilterElement;
import com.leclercb.taskunifier.gui.searcher.TaskSearcher;
import com.leclercb.taskunifier.gui.searcher.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.searcher.TaskSorter;
import com.leclercb.taskunifier.gui.searcher.TaskSorter.TaskSorterElement;

public class TaskSearcherFactoryXMLCoder implements FactoryCoder {

	private static final String NULL_STRING_VALUE = "{{NULL}}";

	private String rootName;

	public TaskSearcherFactoryXMLCoder() {
		this.rootName = "tasksearch";
	}

	@Override
	public void decode(InputStream input) throws FactoryCoderException {
		CheckUtils.isNotNull(input, "Input stream cannot be null");

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			factory.setIgnoringComments(true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(input);

			document.getDocumentElement().normalize();

			if (!document.getChildNodes().item(0).getNodeName().equals(rootName))
				throw new Exception("Root name must be \"" + rootName + "\"");

			Node root = document.getChildNodes().item(0);

			decode(root);
		} catch (FactoryCoderException e) {
			throw e;
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}

	private void decode(Node root) throws FactoryCoderException {
		CheckUtils.isNotNull(root, "Root cannot be null");

		try {
			NodeList nTaskSearchers = root.getChildNodes();

			for (int i=0; i<nTaskSearchers.getLength(); i++) {
				if (!nTaskSearchers.item(i).getNodeName().equals("searcher"))
					continue;

				Node nTaskSearcher = nTaskSearchers.item(i);

				decodeTaskSearcher(nTaskSearcher);
			}
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}

	private void decodeTaskSearcher(Node node) throws FactoryCoderException {
		try {
			NodeList nSearcher = node.getChildNodes();

			String title = null;
			TaskFilter filter = null;
			TaskSorter sorter = null;

			for (int i=0; i<nSearcher.getLength(); i++) {
				if (nSearcher.item(i).getNodeName().equals("title")) {
					title = nSearcher.item(i).getTextContent();
				}

				if (nSearcher.item(i).getNodeName().equals("sorter")) {
					sorter = decodeTaskSorter(nSearcher.item(i));
				}

				if (nSearcher.item(i).getNodeName().equals("filter")) {
					filter = decodeTaskFilter(nSearcher.item(i));
				}
			}

			TaskSearcherFactory.getInstance().create(title, filter, sorter);
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}

	private TaskSorter decodeTaskSorter(Node node) throws FactoryCoderException {
		try {
			NodeList nSorter = node.getChildNodes();
			TaskSorter sorter = new TaskSorter();

			for (int i=0; i<nSorter.getLength(); i++) {
				if (nSorter.item(i).getNodeName().equals("element")) {
					NodeList nElement = nSorter.item(i).getChildNodes();

					int order = 0;
					TaskColumn column = null;
					SortOrder sortOrder = null;

					for (int j=0; j<nElement.getLength(); j++) {
						if (nElement.item(j).getNodeName().equals("order")) {
							order = Integer.parseInt(nElement.item(j).getTextContent());
						}

						if (nElement.item(j).getNodeName().equals("column")) {
							column = TaskColumn.valueOf(nElement.item(j).getTextContent());
						}

						if (nElement.item(j).getNodeName().equals("direction")) {
							sortOrder = SortOrder.valueOf(nElement.item(j).getTextContent());
						}
					}

					sorter.addElement(new TaskSorterElement(order, column, sortOrder));
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
			filter.setLink(TaskFilter.Link.valueOf(XMLUtils.getAttributeValue(node, "link")));

			for (int i=0; i<nFilter.getLength(); i++) {
				if (nFilter.item(i).getNodeName().equals("element")) {
					NodeList nElement = nFilter.item(i).getChildNodes();
					TaskFilterElement element = null;

					TaskColumn column = null;
					String conditionClass = null;
					String enumName = null;
					String valueStr = null;

					for (int j=0; j<nElement.getLength(); j++) {
						if (nElement.item(j).getNodeName().equals("column")) {
							column = TaskColumn.valueOf(nElement.item(j).getTextContent());
						}

						if (nElement.item(j).getNodeName().equals("condition")) {
							String[] values = nElement.item(j).getTextContent().split("\\.");
							conditionClass = values[0];
							enumName = values[1];
						}

						if (nElement.item(j).getNodeName().equals("value")) {
							valueStr = nElement.item(j).getTextContent();

							if (valueStr.equals(NULL_STRING_VALUE))
								valueStr = null;
						}
					}

					if (conditionClass.equals("CalendarCondition")) {
						CalendarCondition condition = CalendarCondition.valueOf(enumName);
						Calendar value = null;

						if (valueStr != null) {
							value = GregorianCalendar.getInstance();
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
					} else if (conditionClass.equals("EnumCondition")) {
						EnumCondition condition = EnumCondition.valueOf(enumName);
						Enum<?> value = null;

						if (valueStr != null) {
							String[] values = valueStr.split("#");
							Object[] enums = Class.forName(values[0]).getEnumConstants();

							for (int j=0; j<enums.length; j++) {
								Enum<?> e = (Enum<?>) enums[j];
								if (e.name().equals(values[1]))
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
							if (column.equals(TaskColumn.CONTEXT))
								value = ContextFactory.getInstance().get(new ModelId(valueStr));
							else if (column.equals(TaskColumn.FOLDER))
								value = FolderFactory.getInstance().get(new ModelId(valueStr));
							else if (column.equals(TaskColumn.GOAL))
								value = GoalFactory.getInstance().get(new ModelId(valueStr));
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
					filter.addFilter(decodeTaskFilter(nFilter.item(i)));
				}
			}

			return filter;
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}

	@Override
	public void encode(OutputStream output) throws FactoryCoderException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation implementation = builder.getDOMImplementation();

			Document document = implementation.createDocument(null, null, null);
			Element root = document.createElement(rootName);
			document.appendChild(root);

			encode(document, root);

			DOMSource domSource = new DOMSource(document);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();

			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			StreamResult sr = new StreamResult(output);
			transformer.transform(domSource, sr);
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}

	private void encode(Document document, Element root) {
		List<TaskSearcher> searchers = TaskSearcherFactory.getInstance().getList();

		for (TaskSearcher taskSearcher : searchers) {
			Element searcher = document.createElement("searcher");
			root.appendChild(searcher);

			Element title = document.createElement("title");
			title.setTextContent(taskSearcher.getTitle());
			searcher.appendChild(title);

			Element sorter = document.createElement("sorter");
			searcher.appendChild(sorter);

			Element filter = document.createElement("filter");
			searcher.appendChild(filter);

			encodeTaskSorter(document, sorter, taskSearcher.getSorter());
			encodeTaskFilter(document, filter, taskSearcher.getFilter());
		}
	}

	private void encodeTaskSorter(Document document, Element root, TaskSorter sorter) {
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

	private void encodeTaskFilter(Document document, Element root, TaskFilter filter) {
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
				condition.setTextContent("CalendarCondition." + e.getCondition().name());

				if (e.getValue() != null)
					value.setTextContent(((Calendar) e.getValue()).getTimeInMillis() + "");
			} else if (e.getCondition() instanceof TaskFilter.DaysCondition) {
				condition.setTextContent("DaysCondition." + e.getCondition().name());

				if (e.getValue() != null)
					value.setTextContent(((Integer) e.getValue()) + "");
			} else if (e.getCondition() instanceof TaskFilter.StringCondition) {
				condition.setTextContent("StringCondition." + e.getCondition().name());

				if (e.getValue() != null)
					value.setTextContent((String) e.getValue());
			} else if (e.getCondition() instanceof TaskFilter.EnumCondition) {
				condition.setTextContent("EnumCondition." + e.getCondition().name());

				if (e.getValue() != null)
					value.setTextContent(e.getValue().getClass().getName() + "#" + ((Enum<?>) e.getValue()).name());
			} else if (e.getCondition() instanceof TaskFilter.ModelCondition) {
				condition.setTextContent("ModelCondition." + e.getCondition().name());

				if (e.getValue() != null)
					value.setTextContent(((Model) e.getValue()).getModelId().getId());
			}

			if (e.getValue() == null)
				value.setTextContent(NULL_STRING_VALUE);
		}

		for (TaskFilter f : filter.getFilters()) {
			Element eF = document.createElement("filter");
			root.appendChild(eF);

			encodeTaskFilter(document, eF, f);
		}
	}

}
