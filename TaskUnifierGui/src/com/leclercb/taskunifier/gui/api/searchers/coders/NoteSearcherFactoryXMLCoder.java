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

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.leclercb.commons.api.coder.AbstractFactoryXMLCoder;
import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.templates.NoteTemplate;
import com.leclercb.taskunifier.api.models.templates.NoteTemplateFactory;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilter;
import com.leclercb.taskunifier.gui.api.searchers.sorters.NoteSorter;

public class NoteSearcherFactoryXMLCoder extends AbstractFactoryXMLCoder {
	
	private static final NoteSorterXMLCoder NOTE_SORTER_XML_CODER = new NoteSorterXMLCoder();
	private static final NoteFilterXMLCoder NOTE_FILTER_XML_CODER = new NoteFilterXMLCoder();
	
	public NoteSearcherFactoryXMLCoder() {
		super("notesearchers");
	}
	
	@Override
	public void decode(Node root) throws FactoryCoderException {
		CheckUtils.isNotNull(root);
		
		try {
			NodeList nNoteSearchers = root.getChildNodes();
			
			for (int i = 0; i < nNoteSearchers.getLength(); i++) {
				if (!nNoteSearchers.item(i).getNodeName().equals("searcher"))
					continue;
				
				Node nNoteSearcher = nNoteSearchers.item(i);
				
				this.decodeNoteSearcher(nNoteSearcher);
			}
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	private void decodeNoteSearcher(Node node) throws FactoryCoderException {
		try {
			NodeList nSearcher = node.getChildNodes();
			
			NoteSearcherType type = NoteSearcherType.PERSONAL;
			int order = 0;
			String title = null;
			String icon = null;
			NoteFilter filter = null;
			NoteSorter sorter = null;
			NoteTemplate template = null;
			
			for (int i = 0; i < nSearcher.getLength(); i++) {
				if (nSearcher.item(i).getNodeName().equals("type")) {
					type = NoteSearcherType.valueOf(nSearcher.item(i).getTextContent());
				}
				
				if (nSearcher.item(i).getNodeName().equals("order")) {
					order = Integer.parseInt(nSearcher.item(i).getTextContent());
				}
				
				if (nSearcher.item(i).getNodeName().equals("title")) {
					title = nSearcher.item(i).getTextContent();
				}
				
				if (nSearcher.item(i).getNodeName().equals("icon")) {
					if (nSearcher.item(i).getTextContent().length() != 0)
						icon = nSearcher.item(i).getTextContent();
				}
				
				if (nSearcher.item(i).getNodeName().equals("sorter")) {
					sorter = this.decodeNoteSorter(nSearcher.item(i));
				}
				
				if (nSearcher.item(i).getNodeName().equals("filter")) {
					filter = this.decodeNoteFilter(nSearcher.item(i));
				}
				
				if (nSearcher.item(i).getNodeName().equals("template")) {
					template = NoteTemplateFactory.getInstance().get(
							new ModelId(nSearcher.item(i).getTextContent()));
				}
			}
			
			NoteSearcherFactory.getInstance().create(
					type,
					order,
					title,
					icon,
					filter,
					sorter,
					template);
		} catch (Exception e) {
			throw new FactoryCoderException(e.getMessage(), e);
		}
	}
	
	private NoteSorter decodeNoteSorter(Node node) throws FactoryCoderException {
		return NOTE_SORTER_XML_CODER.decode(node);
	}
	
	private NoteFilter decodeNoteFilter(Node node) throws FactoryCoderException {
		return NOTE_FILTER_XML_CODER.decode(node);
	}
	
	@Override
	public void encode(Document document, Element root) {
		List<NoteSearcher> searchers = NoteSearcherFactory.getInstance().getList();
		
		for (NoteSearcher noteSearcher : searchers) {
			Element searcher = document.createElement("searcher");
			root.appendChild(searcher);
			
			Element type = document.createElement("type");
			type.setTextContent(noteSearcher.getType().name());
			searcher.appendChild(type);
			
			Element order = document.createElement("order");
			order.setTextContent(noteSearcher.getOrder() + "");
			searcher.appendChild(order);
			
			Element title = document.createElement("title");
			title.setTextContent(noteSearcher.getTitle());
			searcher.appendChild(title);
			
			Element icon = document.createElement("icon");
			icon.setTextContent(noteSearcher.getIcon());
			searcher.appendChild(icon);
			
			Element sorter = document.createElement("sorter");
			searcher.appendChild(sorter);
			
			Element filter = document.createElement("filter");
			searcher.appendChild(filter);
			
			if (noteSearcher.getTemplate() != null) {
				Element template = document.createElement("template");
				template.setTextContent(noteSearcher.getTemplate().getModelId().getId());
				searcher.appendChild(template);
			}
			
			this.encodeNoteSorter(document, sorter, noteSearcher.getSorter());
			this.encodeNoteFilter(document, filter, noteSearcher.getFilter());
		}
	}
	
	private void encodeNoteSorter(
			Document document,
			Element root,
			NoteSorter sorter) {
		NOTE_SORTER_XML_CODER.encode(document, root, sorter);
	}
	
	private void encodeNoteFilter(
			Document document,
			Element root,
			NoteFilter filter) {
		NOTE_FILTER_XML_CODER.encode(document, root, filter);
	}
	
}
