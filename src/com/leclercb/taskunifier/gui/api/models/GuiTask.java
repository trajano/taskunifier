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
package com.leclercb.taskunifier.gui.api.models;

import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.beans.TaskBean;
import com.leclercb.taskunifier.gui.api.models.beans.GuiTaskBean;

public class GuiTask extends Task {
	
	public static final String PROP_SHOW_CHILDREN = "showChildren";
	
	private boolean showChildren;
	
	public GuiTask(TaskBean bean) {
		super(bean);
	}
	
	public GuiTask(String title) {
		super(title);
		this.setShowChildren(true);
	}
	
	public GuiTask(ModelId modelId, String title) {
		super(modelId, title);
		this.setShowChildren(true);
	}
	
	public boolean isShowChildren() {
		return this.showChildren;
	}
	
	public void setShowChildren(boolean showChildren) {
		if (!this.checkBeforeSet(this.isShowChildren(), showChildren))
			return;
		
		boolean oldShowChildren = this.showChildren;
		this.showChildren = showChildren;
		this.updateProperty(
				PROP_SHOW_CHILDREN,
				oldShowChildren,
				showChildren,
				false);
	}
	
	@Override
	public void loadBean(TaskBean bean) {
		if (bean instanceof GuiTaskBean)
			this.setShowChildren(((GuiTaskBean) bean).isShowChildren());
		
		super.loadBean(bean);
	}
	
	@Override
	public void toBean(TaskBean bean) {
		if (bean instanceof GuiTaskBean)
			((GuiTaskBean) bean).setShowChildren(this.isShowChildren());
		
		super.toBean(bean);
	}
	
}
