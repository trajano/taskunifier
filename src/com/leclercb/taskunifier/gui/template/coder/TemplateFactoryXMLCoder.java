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
	
	private static final String NULL_STRING_VALUE = "{{NULL}}";
	
	public TemplateFactoryXMLCoder() {
		super("templates");
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
				
				String title = null;
				
				String taskTitle = null;
				String[] taskTags = null;
				ModelId taskFolder = null;
				ModelId taskContext = null;
				ModelId taskGoal = null;
				ModelId taskLocation = null;
				ModelId taskParent = null;
				Boolean taskCompleted = null;
				Integer taskCompletedOn = null;
				Integer taskDueDate = null;
				Integer taskStartDate = null;
				Integer taskReminder = null;
				String taskRepeat = null;
				TaskRepeatFrom taskRepeatFrom = null;
				TaskStatus taskStatus = null;
				Integer taskLength = null;
				TaskPriority taskPriority = null;
				Boolean taskStar = null;
				String taskNote = null;
				
				for (int j = 0; j < nTemplate.getLength(); j++) {
					Node element = nTemplate.item(j);
					
					if (element.getNodeName().equals("title"))
						title = element.getTextContent();
					
					if (element.getNodeName().equals("task_title"))
						taskTitle = element.getTextContent();
					
					if (element.getNodeName().equals("task_tags"))
						taskTags = element.getTextContent().split(",");
					
					if (element.getNodeName().equals("task_folder"))
						if (element.getTextContent().length() != 0)
							taskFolder = new ModelId(
									XMLUtils.getBooleanAttributeValue(
											element,
											"isnew"), element.getTextContent());
					
					if (element.getNodeName().equals("task_context"))
						if (element.getTextContent().length() != 0)
							taskContext = new ModelId(
									XMLUtils.getBooleanAttributeValue(
											element,
											"isnew"), element.getTextContent());
					
					if (element.getNodeName().equals("task_goal"))
						if (element.getTextContent().length() != 0)
							taskGoal = new ModelId(
									XMLUtils.getBooleanAttributeValue(
											element,
											"isnew"), element.getTextContent());
					
					if (element.getNodeName().equals("task_location"))
						if (element.getTextContent().length() != 0)
							taskLocation = new ModelId(
									XMLUtils.getBooleanAttributeValue(
											element,
											"isnew"), element.getTextContent());
				}
				
				Template template = TemplateFactory.getInstance().create(title);
				
				template.setTaskTitle(taskTitle);
				template.setTaskTags(taskTags);
				template.setTaskFolder(taskFolder);
				template.setTaskContext(taskContext);
				template.setTaskGoal(taskGoal);
				template.setTaskLocation(taskLocation);
				template.setTaskParent(taskParent);
				template.setTaskCompleted(taskCompleted);
				template.setTaskCompletedOn(taskCompletedOn);
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
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void encode(Document document, Element root)
			throws FactoryCoderException {
		// TODO Auto-generated method stub
		
	}
	
}
