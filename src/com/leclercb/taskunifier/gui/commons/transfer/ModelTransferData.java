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
package com.leclercb.taskunifier.gui.commons.transfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

public class ModelTransferData implements Serializable {
	
	private ModelType type;
	private ModelId[] ids;
	
	public ModelTransferData(ModelType type, ModelId id) {
		CheckUtils.isNotNull(type, "Type cannot be null");
		CheckUtils.isNotNull(id, "ID cannot be null");
		
		this.type = type;
		this.ids = new ModelId[] { id };
	}
	
	public ModelTransferData(ModelType type, ModelId[] ids) {
		CheckUtils.isNotNull(type, "Type cannot be null");
		CheckUtils.isNotNull(ids, "IDs cannot be null");
		
		this.type = type;
		this.ids = Arrays.copyOf(ids, ids.length);
	}
	
	public ModelType getType() {
		return this.type;
	}
	
	public ModelId[] getIds() {
		return this.ids;
	}
	
	public String getPlainData() {
		return null;
	}
	
	public String getHtmlData() {
		if (this.type == ModelType.TASK) {
			List<Task> tasks = new ArrayList<Task>();
			for (ModelId id : this.ids) {
				Task task = TaskFactory.getInstance().get(id);
				if (task != null)
					tasks.add(task);
			}
			
			return TaskUtils.toHtml(
					tasks.toArray(new Task[0]),
					TaskColumn.values());
		}
		
		return null;
	}
	
}
