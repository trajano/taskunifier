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
package com.leclercb.taskunifier.gui.api.models.coders;

import java.awt.Color;
import java.util.Calendar;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.leclercb.commons.api.coder.AbstractFactoryXMLCoder;
import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.XMLUtils;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.enums.GoalLevel;
import com.leclercb.taskunifier.gui.api.models.GuiGoal;

public class GuiGoalFactoryXMLCoder extends AbstractFactoryXMLCoder {
	
	public GuiGoalFactoryXMLCoder() {
		super("goals");
	}
	
	@Override
	protected void decode(Node root) throws FactoryCoderException {
		CheckUtils.isNotNull(root, "Root cannot be null");
		
		try {
			NodeList nGoals = root.getChildNodes();
			
			for (int i = 0; i < nGoals.getLength(); i++) {
				if (!nGoals.item(i).getNodeName().equals("goal"))
					continue;
				
				NodeList nGoal = nGoals.item(i).getChildNodes();
				
				ModelId modelId = null;
				ModelStatus modelStatus = null;
				Calendar modelUpdateDate = null;
				String title = null;
				GoalLevel level = null;
				Goal contributes = null;
				Color color = null;
				
				for (int j = 0; j < nGoal.getLength(); j++) {
					Node element = nGoal.item(j);
					
					if (element.getNodeName().equals("modelid"))
						if (element.getTextContent().length() != 0)
							modelId = new ModelId(
									XMLUtils.getBooleanAttributeValue(
											element,
											"isnew"), element.getTextContent());
					
					if (element.getNodeName().equals("modelstatus"))
						modelStatus = ModelStatus.valueOf(element.getTextContent());
					
					if (element.getNodeName().equals("modelupdatedate")) {
						modelUpdateDate = Calendar.getInstance();
						modelUpdateDate.setTimeInMillis(Long.parseLong(element.getTextContent()));
					}
					
					if (element.getNodeName().equals("title"))
						title = element.getTextContent();
					
					if (element.getNodeName().equals("level"))
						level = GoalLevel.valueOf(element.getTextContent());
					
					if (element.getNodeName().equals("contributes")) {
						if (element.getTextContent().length() != 0) {
							ModelId id = new ModelId(
									XMLUtils.getBooleanAttributeValue(
											element,
											"isnew"), element.getTextContent());
							contributes = GoalFactory.getInstance().get(id);
							
							if (contributes == null) {
								contributes = GoalFactory.getInstance().createShell(
										id);
								
								contributes.setLevel(GoalLevel.LIFE_TIME);
								contributes.setModelStatus(ModelStatus.SHELL);
							}
						}
					}
					
					if (element.getNodeName().equals("color"))
						if (element.getTextContent().length() != 0)
							color = new Color(
									Integer.parseInt(element.getTextContent()));
				}
				
				GuiGoal goal = (GuiGoal) GoalFactory.getInstance().get(modelId);
				
				if (goal == null)
					goal = (GuiGoal) GoalFactory.getInstance().createShell(
							modelId);
				
				goal.setTitle(title);
				goal.setLevel(level);
				goal.setContributes(contributes);
				goal.setColor(color);
				
				// After all other setXxx methods
				goal.setModelStatus(modelStatus);
				goal.setModelUpdateDate(modelUpdateDate);
			}
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void encode(Document document, Element root)
			throws FactoryCoderException {
		List<Goal> goals = GoalFactory.getInstance().getList();
		
		for (Goal goal : goals) {
			GuiGoal guiGoal = (GuiGoal) goal;
			
			Element nGoal = document.createElement("goal");
			root.appendChild(nGoal);
			
			Element modelId = document.createElement("modelid");
			modelId.setAttribute("isnew", goal.getModelId().isNewId() + "");
			modelId.setTextContent(goal.getModelId().getId());
			nGoal.appendChild(modelId);
			
			Element modelStatus = document.createElement("modelstatus");
			modelStatus.setTextContent(goal.getModelStatus().name());
			nGoal.appendChild(modelStatus);
			
			Element modelUpdateDate = document.createElement("modelupdatedate");
			modelUpdateDate.setTextContent(goal.getModelUpdateDate().getTimeInMillis()
					+ "");
			nGoal.appendChild(modelUpdateDate);
			
			Element title = document.createElement("title");
			title.setTextContent(goal.getTitle());
			nGoal.appendChild(title);
			
			Element level = document.createElement("level");
			level.setTextContent(goal.getLevel().name());
			nGoal.appendChild(level);
			
			Element contributes = document.createElement("contributes");
			if (goal.getContributes() != null) {
				contributes.setAttribute(
						"isnew",
						goal.getContributes().getModelId().isNewId() + "");
				contributes.setTextContent(goal.getContributes().getModelId().getId());
			}
			nGoal.appendChild(contributes);
			
			Element color = document.createElement("color");
			color.setTextContent(guiGoal.getColor() != null ? guiGoal.getColor().getRGB()
					+ "" : "");
			nGoal.appendChild(color);
		}
	}
	
}
