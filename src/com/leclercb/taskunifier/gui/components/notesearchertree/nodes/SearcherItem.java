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
package com.leclercb.taskunifier.gui.components.notesearchertree.nodes;

import java.util.List;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.NoteUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class SearcherItem extends DefaultMutableTreeNode implements SearcherNode {
	
	private BadgeCount badgeCount;
	
	public SearcherItem(NoteSearcher searcher) {
		super(searcher);
		
		CheckUtils.isNotNull(searcher, "Searcher cannot be null");
		
		this.updateBadgeCount();
	}
	
	@Override
	public NoteSearcher getNoteSearcher() {
		return (NoteSearcher) this.getUserObject();
	}
	
	@Override
	public Icon getIcon() {
		if (this.getNoteSearcher().getIcon() == null)
			return Images.getResourceImage("transparent.png", 16, 16);
		else
			return Images.getImage(this.getNoteSearcher().getIcon(), 16, 16);
	}
	
	@Override
	public String getText() {
		return this.getNoteSearcher().getTitle();
	}
	
	@Override
	public void updateBadgeCount() {
		if (!Main.SETTINGS.getBooleanProperty("searcher.show_badges")) {
			this.badgeCount = null;
		}
		
		List<Note> notes = NoteFactory.getInstance().getList();
		NoteSearcher searcher = this.getNoteSearcher();
		
		int count = 0;
		for (Note note : notes) {
			if (NoteUtils.showNote(note, searcher.getFilter())) {
				count++;
			}
		}
		
		this.badgeCount = new BadgeCount(count);
	}
	
	@Override
	public BadgeCount getBadgeCount() {
		return this.badgeCount;
	}
	
	@Override
	public boolean getAllowsChildren() {
		return false;
	}
	
}
