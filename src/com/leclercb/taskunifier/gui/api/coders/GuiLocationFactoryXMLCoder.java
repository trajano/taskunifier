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
package com.leclercb.taskunifier.gui.api.coders;

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
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.gui.api.GuiLocation;

public class GuiLocationFactoryXMLCoder extends AbstractFactoryXMLCoder {
	
	public GuiLocationFactoryXMLCoder() {
		super("locations");
	}
	
	@Override
	protected void decode(Node root) throws FactoryCoderException {
		CheckUtils.isNotNull(root, "Root cannot be null");
		
		try {
			NodeList nLocations = root.getChildNodes();
			
			for (int i = 0; i < nLocations.getLength(); i++) {
				if (!nLocations.item(i).getNodeName().equals("location"))
					continue;
				
				NodeList nLocation = nLocations.item(i).getChildNodes();
				
				ModelId modelId = null;
				ModelStatus modelStatus = null;
				Calendar modelUpdateDate = null;
				String title = null;
				String description = null;
				double latitude = 0;
				double longitude = 0;
				Color color = null;
				
				for (int j = 0; j < nLocation.getLength(); j++) {
					Node element = nLocation.item(j);
					
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
					
					if (element.getNodeName().equals("description"))
						description = element.getTextContent();
					
					if (element.getNodeName().equals("latitude"))
						latitude = Double.parseDouble(element.getTextContent());
					
					if (element.getNodeName().equals("longitude"))
						longitude = Double.parseDouble(element.getTextContent());
					
					if (element.getNodeName().equals("color"))
						if (element.getTextContent().length() != 0)
							color = new Color(
									Integer.parseInt(element.getTextContent()));
				}
				
				GuiLocation location = (GuiLocation) LocationFactory.getInstance().get(
						modelId);
				
				if (location == null)
					location = (GuiLocation) LocationFactory.getInstance().createShell(
							modelId);
				
				location.setTitle(title);
				location.setDescription(description);
				location.setLatitude(latitude);
				location.setLongitude(longitude);
				location.setColor(color);
				
				// After all other setXxx methods
				location.setModelStatus(modelStatus);
				location.setModelUpdateDate(modelUpdateDate);
			}
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void encode(Document document, Element root)
			throws FactoryCoderException {
		List<Location> locations = LocationFactory.getInstance().getList();
		
		for (Location location : locations) {
			GuiLocation guiLocation = (GuiLocation) location;
			
			Element nLocation = document.createElement("location");
			root.appendChild(nLocation);
			
			Element modelId = document.createElement("modelid");
			modelId.setAttribute("isnew", location.getModelId().isNewId() + "");
			modelId.setTextContent(location.getModelId().getId());
			nLocation.appendChild(modelId);
			
			Element modelStatus = document.createElement("modelstatus");
			modelStatus.setTextContent(location.getModelStatus().name());
			nLocation.appendChild(modelStatus);
			
			Element modelUpdateDate = document.createElement("modelupdatedate");
			modelUpdateDate.setTextContent(location.getModelUpdateDate().getTimeInMillis()
					+ "");
			nLocation.appendChild(modelUpdateDate);
			
			Element title = document.createElement("title");
			title.setTextContent(location.getTitle());
			nLocation.appendChild(title);
			
			Element description = document.createElement("description");
			description.setTextContent(location.getDescription());
			nLocation.appendChild(description);
			
			Element latitude = document.createElement("latitude");
			latitude.setTextContent(location.getLatitude() + "");
			nLocation.appendChild(latitude);
			
			Element longitude = document.createElement("longitude");
			longitude.setTextContent(location.getLongitude() + "");
			nLocation.appendChild(longitude);
			
			Element color = document.createElement("color");
			color.setTextContent(guiLocation.getColor() != null ? guiLocation.getColor().getRGB()
					+ "" : "");
			nLocation.appendChild(color);
		}
	}
	
}
