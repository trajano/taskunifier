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
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.gui.api.models.GuiFolder;

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
