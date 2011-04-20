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
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.gui.api.models.GuiLocation;

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
