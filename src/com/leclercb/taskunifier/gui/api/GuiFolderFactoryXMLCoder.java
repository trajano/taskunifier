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
package com.leclercb.taskunifier.gui.api;

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
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelStatus;

public class GuiFolderFactoryXMLCoder extends AbstractFactoryXMLCoder {
	
	public GuiFolderFactoryXMLCoder() {
		super("folders");
	}
	
	@Override
	protected void decode(Node root) throws FactoryCoderException {
		CheckUtils.isNotNull(root, "Root cannot be null");
		
		try {
			NodeList nFolders = root.getChildNodes();
			
			for (int i = 0; i < nFolders.getLength(); i++) {
				if (!nFolders.item(i).getNodeName().equals("folder"))
					continue;
				
				NodeList nFolder = nFolders.item(i).getChildNodes();
				
				ModelId modelId = null;
				ModelStatus modelStatus = null;
				Calendar modelUpdateDate = null;
				String title = null;
				Color color = null;
				
				for (int j = 0; j < nFolder.getLength(); j++) {
					Node element = nFolder.item(j);
					
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
				
				GuiFolder folder = (GuiFolder) FolderFactory.getInstance().get(
						modelId);
				
				if (folder == null)
					folder = (GuiFolder) FolderFactory.getInstance().createShell(
							modelId);
				
				folder.setTitle(title);
				folder.setColor(color);
				
				// After all other setXxx methods
				folder.setModelStatus(modelStatus);
				folder.setModelUpdateDate(modelUpdateDate);
			}
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	@Override
	protected void encode(Document document, Element root)
			throws FactoryCoderException {
		List<Folder> folders = FolderFactory.getInstance().getList();
		
		for (Folder folder : folders) {
			GuiFolder guiFolder = (GuiFolder) folder;
			
			Element nFolder = document.createElement("folder");
			root.appendChild(nFolder);
			
			Element modelId = document.createElement("modelid");
			modelId.setAttribute("isnew", folder.getModelId().isNewId() + "");
			modelId.setTextContent(folder.getModelId().getId());
			nFolder.appendChild(modelId);
			
			Element modelStatus = document.createElement("modelstatus");
			modelStatus.setTextContent(folder.getModelStatus().name());
			nFolder.appendChild(modelStatus);
			
			Element modelUpdateDate = document.createElement("modelupdatedate");
			modelUpdateDate.setTextContent(folder.getModelUpdateDate().getTimeInMillis()
					+ "");
			nFolder.appendChild(modelUpdateDate);
			
			Element title = document.createElement("title");
			title.setTextContent(folder.getTitle());
			nFolder.appendChild(title);
			
			Element color = document.createElement("color");
			color.setTextContent(guiFolder.getColor() != null ? guiFolder.getColor().getRGB()
					+ "" : "");
			nFolder.appendChild(color);
		}
	}
	
}
