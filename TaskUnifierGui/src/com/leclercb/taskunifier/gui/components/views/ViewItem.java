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
package com.leclercb.taskunifier.gui.components.views;

import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.Icon;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.event.action.ActionSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.logger.GuiLogger;

public class ViewItem implements ActionSupported {
	
	public static final String ACTION_VIEW_LOADED = "ACTION_VIEW_LOADED";
	
	private ActionSupport actionSupport;
	
	private ViewType viewType;
	private View view;
	private String label;
	private Icon icon;
	private int frameId;
	
	public ViewItem(ViewType viewType, String label, Icon icon, int frameId) {
		CheckUtils.isNotNull(viewType);
		CheckUtils.isNotNull(label);
		CheckUtils.isNotNull(icon);
		
		this.actionSupport = new ActionSupport(this);
		
		this.viewType = viewType;
		this.view = null;
		this.label = label;
		this.icon = icon;
		this.frameId = frameId;
	}
	
	public boolean isLoaded() {
		return this.view != null;
	}
	
	public ViewType getViewType() {
		return this.viewType;
	}
	
	public View getView() {
		if (!this.isLoaded())
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"View \"" + this.label + "\" is not loaded yet",
					new Throwable());
		
		return this.view;
	}
	
	public void setView(View view) {
		CheckUtils.isNotNull(view);
		
		if (!this.viewType.match(view))
			throw new RuntimeException("View \""
					+ this.label
					+ "\" has an incorrect type");
		
		if (this.view != null)
			throw new RuntimeException("View \""
					+ this.label
					+ "\" is already loaded");
		
		this.view = view;
		this.actionSupport.fireActionPerformed(0, ACTION_VIEW_LOADED);
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public Icon getIcon() {
		return this.icon;
	}
	
	public int getFrameId() {
		return this.frameId;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
	@Override
	public void addActionListener(ActionListener listener) {
		this.actionSupport.addActionListener(listener);
	}
	
	@Override
	public void removeActionListener(ActionListener listener) {
		this.actionSupport.removeActionListener(listener);
	}
	
}
