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
package com.leclercb.taskunifier.gui.template.coder;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.leclercb.commons.api.coder.AbstractFactoryXMLCoder;
import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.XMLUtils;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.template.Template;
import com.leclercb.taskunifier.gui.template.TemplateFactory;

public class TemplateFactoryXMLCoder extends AbstractFactoryXMLCoder {
	
	private boolean createNewTemplates;
	
	public TemplateFactoryXMLCoder(boolean createNewTemplates) {
		super("templates");
		this.createNewTemplates = createNewTemplates;
	}
	
	@Override
	protected void decode(Node root) throws FactoryCoderException {
		CheckUtils.isNotNull(root, "Root cannot be null");
		
		try {
			NodeList nTemplates = root.getChildNodes();
			
			for (int i = 0; i < nTemplates.getLength(); i++) {
				if (!nTemplates.item(i).getNodeName().equals("template"))
					continue;
				
				NodeList nTemplate = nTemplates.item(i).getChildNodes();
				
				String id = null;
				String title = null;
				
				String taskTitle = null;
				String taskTags = null;
				ModelId taskFolder = null;
				ModelId taskContext = null;
				ModelId taskGoal = null;
				ModelId taskLocation = null;
				Boolean taskCompleted = false;
				Integer taskDueDate = null;
				Integer taskStartDate = null;
				Integer taskReminder = 0;
				String taskRepeat = null;
				TaskRepeatFrom taskRepeatFrom = null;
				TaskStatus taskStatus = null;
				Integer taskLength = 0;
				TaskPriority taskPriority = null;
				Boolean taskStar = false;
				String taskNote = null;
				
				for (int j = 0; j < nTemplate.getLength(); j++) {
					Node element = nTemplate.item(j);
					
					if (element.getNodeName().equals("id"))
						id = element.getTextContent();
					
					if (element.getNodeName().equals("title"))
						title = element.getTextContent();
					
					if (element.getNodeName().equals("tasktitle"))
						taskTitle = element.getTextContent();
					
					if (element.getNodeName().equals("tasktags"))
						taskTags = element.getTextContent();
					
					if (element.getNodeName().equals("taskfolder"))
						if (element.getTextContent().length() != 0)
							taskFolder = new ModelId(
									XMLUtils.getBooleanAttributeValue(
											element,
											"isnew"), element.getTextContent());
					
					if (element.getNodeName().equals("taskcontext"))
						if (element.getTextContent().length() != 0)
							taskContext = new ModelId(
									XMLUtils.getBooleanAttributeValue(
											element,
											"isnew"), element.getTextContent());
					
					if (element.getNodeName().equals("taskgoal"))
						if (element.getTextContent().length() != 0)
							taskGoal = new ModelId(
									XMLUtils.getBooleanAttributeValue(
											element,
											"isnew"), element.getTextContent());
					
					if (element.getNodeName().equals("tasklocation"))
						if (element.getTextContent().length() != 0)
							taskLocation = new ModelId(
									XMLUtils.getBooleanAttributeValue(
											element,
											"isnew"), element.getTextContent());
					
					if (element.getNodeName().equals("taskcompleted"))
						if (element.getTextContent().length() != 0)
							taskCompleted = Boolean.parseBoolean(element.getTextContent());
					
					if (element.getNodeName().equals("taskduedate"))
						if (element.getTextContent().length() != 0)
							taskDueDate = Integer.parseInt(element.getTextContent());
					
					if (element.getNodeName().equals("taskstartdate"))
						if (element.getTextContent().length() != 0)
							taskStartDate = Integer.parseInt(element.getTextContent());
					
					if (element.getNodeName().equals("taskreminder"))
						if (element.getTextContent().length() != 0)
							taskReminder = Integer.parseInt(element.getTextContent());
					
					if (element.getNodeName().equals("taskrepeat"))
						taskRepeat = element.getTextContent();
					
					if (element.getNodeName().equals("taskrepeatfrom"))
						if (element.getTextContent().length() != 0)
							taskRepeatFrom = TaskRepeatFrom.valueOf(element.getTextContent());
					
					if (element.getNodeName().equals("taskstatus"))
						if (element.getTextContent().length() != 0)
							taskStatus = TaskStatus.valueOf(element.getTextContent());
					
					if (element.getNodeName().equals("tasklength"))
						if (element.getTextContent().length() != 0)
							taskLength = Integer.parseInt(element.getTextContent());
					
					if (element.getNodeName().equals("taskpriority"))
						if (element.getTextContent().length() != 0)
							taskPriority = TaskPriority.valueOf(element.getTextContent());
					
					if (element.getNodeName().equals("taskstar"))
						if (element.getTextContent().length() != 0)
							taskStar = Boolean.parseBoolean(element.getTextContent());
					
					if (element.getNodeName().equals("tasknote"))
						taskNote = element.getTextContent();
				}
				
				Template template = null;
				
				if (this.createNewTemplates)
					template = TemplateFactory.getInstance().create(title);
				else
					template = TemplateFactory.getInstance().create(id, title);
				
				template.setTaskTitle(taskTitle);
				template.setTaskTags(taskTags);
				template.setTaskFolder(taskFolder);
				template.setTaskContext(taskContext);
				template.setTaskGoal(taskGoal);
				template.setTaskLocation(taskLocation);
				template.setTaskCompleted(taskCompleted);
				template.setTaskDueDate(taskDueDate);
				template.setTaskStartDate(taskStartDate);
				template.setTaskReminder(taskReminder);
				template.setTaskRepeat(taskRepeat);
				template.setTaskRepeatFrom(taskRepeatFrom);
				template.setTaskStatus(taskStatus);
				template.setTaskLength(taskLength);
				template.setTaskPriority(taskPriority);
				template.setTaskStar(taskStar);
				template.setTaskNote(taskNote);
			}
			
			Template defaultTemplate = TemplateFactory.getInstance().get(
					XMLUtils.getAttributeValue(root, "default"));
			
			if (defaultTemplate != null)
				TemplateFactory.getInstance().setDefaultTemplate(
						defaultTemplate);
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void encode(Document document, Element root)
			throws FactoryCoderException {
		List<Template> templates = TemplateFactory.getInstance().getList();
		
		for (Template template : templates) {
			Element nTemplate = document.createElement("template");
			root.appendChild(nTemplate);
			
			Element id = document.createElement("id");
			id.setTextContent(template.getId());
			nTemplate.appendChild(id);
			
			Element title = document.createElement("title");
			title.setTextContent(template.getTitle());
			nTemplate.appendChild(title);
			
			Element taskTitle = document.createElement("tasktitle");
			setTextContext(taskTitle, template.getTaskTitle());
			nTemplate.appendChild(taskTitle);
			
			Element taskTags = document.createElement("tasktags");
			setTextContext(taskTags, template.getTaskTags());
			nTemplate.appendChild(taskTags);
			
			Element taskFolder = document.createElement("taskfolder");
			if (template.getTaskFolder() != null) {
				taskFolder.setAttribute(
						"isnew",
						template.getTaskFolder().isNewId() + "");
				taskFolder.setTextContent(template.getTaskFolder().getId());
			}
			nTemplate.appendChild(taskFolder);
			
			Element taskContext = document.createElement("taskcontext");
			if (template.getTaskContext() != null) {
				taskContext.setAttribute(
						"isnew",
						template.getTaskContext().isNewId() + "");
				taskContext.setTextContent(template.getTaskContext().getId());
			}
			nTemplate.appendChild(taskContext);
			
			Element taskGoal = document.createElement("taskgoal");
			if (template.getTaskGoal() != null) {
				taskGoal.setAttribute("isnew", template.getTaskGoal().isNewId()
						+ "");
				taskGoal.setTextContent(template.getTaskGoal().getId());
			}
			nTemplate.appendChild(taskGoal);
			
			Element taskLocation = document.createElement("tasklocation");
			if (template.getTaskLocation() != null) {
				taskLocation.setAttribute(
						"isnew",
						template.getTaskLocation().isNewId() + "");
				taskLocation.setTextContent(template.getTaskLocation().getId());
			}
			nTemplate.appendChild(taskLocation);
			
			Element taskCompleted = document.createElement("taskcompleted");
			setTextContext(taskCompleted, template.getTaskCompleted());
			nTemplate.appendChild(taskCompleted);
			
			Element taskStartDate = document.createElement("taskstartdate");
			setTextContext(taskStartDate, template.getTaskStartDate());
			nTemplate.appendChild(taskStartDate);
			
			Element taskDueDate = document.createElement("taskduedate");
			setTextContext(taskDueDate, template.getTaskDueDate());
			nTemplate.appendChild(taskDueDate);
			
			Element taskReminder = document.createElement("taskreminder");
			setTextContext(taskReminder, template.getTaskReminder());
			nTemplate.appendChild(taskReminder);
			
			Element taskRepeat = document.createElement("taskrepeat");
			setTextContext(taskRepeat, template.getTaskRepeat());
			nTemplate.appendChild(taskRepeat);
			
			Element taskRepeatFrom = document.createElement("taskrepeatfrom");
			if (template.getTaskRepeatFrom() != null)
				setTextContext(
						taskRepeatFrom,
						template.getTaskRepeatFrom().name());
			nTemplate.appendChild(taskRepeatFrom);
			
			Element taskStatus = document.createElement("taskstatus");
			if (template.getTaskStatus() != null)
				setTextContext(taskStatus, template.getTaskStatus().name());
			nTemplate.appendChild(taskStatus);
			
			Element taskLength = document.createElement("tasklength");
			setTextContext(taskLength, template.getTaskLength());
			nTemplate.appendChild(taskLength);
			
			Element taskPriority = document.createElement("taskpriority");
			if (template.getTaskPriority() != null)
				setTextContext(taskPriority, template.getTaskPriority().name());
			nTemplate.appendChild(taskPriority);
			
			Element taskStar = document.createElement("taskstar");
			setTextContext(taskStar, template.getTaskStar());
			nTemplate.appendChild(taskStar);
			
			Element taskNote = document.createElement("tasknote");
			setTextContext(taskNote, template.getTaskNote());
			nTemplate.appendChild(taskNote);
		}
		
		if (TemplateFactory.getInstance().getDefaultTemplate() != null)
			root.setAttribute(
					"default",
					TemplateFactory.getInstance().getDefaultTemplate().getId());
		else
			root.setAttribute("default", "");
	}
	
	private static void setTextContext(Element element, Object value) {
		element.setTextContent(value == null ? "" : value + "");
	}
	
}
