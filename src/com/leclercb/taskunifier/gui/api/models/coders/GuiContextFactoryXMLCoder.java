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
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.gui.api.models.GuiContext;

public class GuiContextFactoryXMLCoder extends AbstractFactoryXMLCoder {
	
	public GuiContextFactoryXMLCoder() {
		super("contexts");
	}
	
	@Override
	protected void decode(Node root) throws FactoryCoderException {
		CheckUtils.isNotNull(root, "Root cannot be null");
		
		try {
			NodeList nContexts = root.getChildNodes();
			
			for (int i = 0; i < nContexts.getLength(); i++) {
				if (!nContexts.item(i).getNodeName().equals("context"))
					continue;
				
				NodeList nContext = nContexts.item(i).getChildNodes();
				
				ModelId modelId = null;
				ModelStatus modelStatus = null;
				Calendar modelUpdateDate = null;
				String title = null;
				Color color = null;
				
				for (int j = 0; j < nContext.getLength(); j++) {
					Node element = nContext.item(j);
					
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
					
					if (element.getNodeName().equals("color"))
						if (element.getTextContent().length() != 0)
							color = new Color(
									Integer.parseInt(element.getTextContent()));
				}
				
				GuiContext context = (GuiContext) ContextFactory.getInstance().get(
						modelId);
				
				if (context == null)
					context = (GuiContext) ContextFactory.getInstance().createShell(
							modelId);
				
				context.setTitle(title);
				context.setColor(color);
				
				// After all other setXxx methods
				context.setModelStatus(modelStatus);
				context.setModelUpdateDate(modelUpdateDate);
			}
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void encode(Document document, Element root)
			throws FactoryCoderException {
		List<Context> contexts = ContextFactory.getInstance().getList();
		
		for (Context context : contexts) {
			GuiContext guiContext = (GuiContext) context;
			
			Element nContext = document.createElement("context");
			root.appendChild(nContext);
			
			Element modelId = document.createElement("modelid");
			modelId.setAttribute("isnew", context.getModelId().isNewId() + "");
			modelId.setTextContent(context.getModelId().getId());
			nContext.appendChild(modelId);
			
			Element modelStatus = document.createElement("modelstatus");
			modelStatus.setTextContent(context.getModelStatus().name());
			nContext.appendChild(modelStatus);
			
			Element modelUpdateDate = document.createElement("modelupdatedate");
			modelUpdateDate.setTextContent(context.getModelUpdateDate().getTimeInMillis()
					+ "");
			nContext.appendChild(modelUpdateDate);
			
			Element title = document.createElement("title");
			title.setTextContent(context.getTitle());
			nContext.appendChild(title);
			
			Element color = document.createElement("color");
			color.setTextContent(guiContext.getColor() != null ? guiContext.getColor().getRGB()
					+ "" : "");
			nContext.appendChild(color);
		}
	}
	
}
