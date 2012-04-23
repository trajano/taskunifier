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
package com.leclercb.taskunifier.gui.commons.models;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.WeakListChangeListener;
import com.leclercb.commons.gui.swing.models.AbstractComboBoxModel;
import com.leclercb.taskunifier.api.models.utils.TaskContactLinkList;

public class TaskContactLinkModel extends AbstractComboBoxModel implements ListChangeListener {
	
	private boolean firstNull;
	
	public TaskContactLinkModel(boolean firstNull) {
		this.firstNull = firstNull;
		
		TaskContactLinkList.getInstance().addListChangeListener(
				new WeakListChangeListener(
						TaskContactLinkList.getInstance(),
						this));
	}
	
	@Override
	public Object getElementAt(int index) {
		if (this.firstNull) {
			if (index == 0)
				return null;
			
			return TaskContactLinkList.getInstance().getLink(index - 1);
		}
		
		return TaskContactLinkList.getInstance().getLink(index);
	}
	
	@Override
	public int getSize() {
		if (this.firstNull)
			return TaskContactLinkList.getInstance().getLinkCount() + 1;
		
		return TaskContactLinkList.getInstance().getLinkCount();
	}
	
	@Override
	public void listChange(ListChangeEvent evt) {
		int index = evt.getIndex();
		
		if (this.firstNull)
			index++;
		
		if (evt.getChangeType() == ListChangeEvent.VALUE_ADDED)
			this.fireIntervalAdded(this, index, index);
		else if (evt.getChangeType() == ListChangeEvent.VALUE_REMOVED)
			this.fireIntervalRemoved(this, index, index);
		else if (evt.getChangeType() == ListChangeEvent.VALUE_CHANGED)
			this.fireContentsChanged(this, index, index);
	}
	
}
