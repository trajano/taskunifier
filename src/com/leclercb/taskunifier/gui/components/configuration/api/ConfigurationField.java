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
package com.leclercb.taskunifier.gui.components.configuration.api;

import com.leclercb.commons.api.utils.CheckUtils;

public class ConfigurationField {
	
	private String id;
	private String label;
	private boolean afterRestart;
	private ConfigurationFieldTypeExt<?, ?> type;
	
	public ConfigurationField(
			String id,
			String label,
			ConfigurationFieldTypeExt<?, ?> type) {
		this(id, label, false, type);
	}
	
	public ConfigurationField(
			String id,
			String label,
			boolean afterRestart,
			ConfigurationFieldTypeExt<?, ?> type) {
		this.setId(id);
		this.setLabel(label);
		this.setAfterRestart(afterRestart);
		this.setType(type);
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		CheckUtils.isNotNull(id, "Id cannot be null");
		this.id = id;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	private void setLabel(String label) {
		this.label = label;
	}
	
	public boolean isAfterRestart() {
		return this.afterRestart;
	}
	
	public void setAfterRestart(boolean afterRestart) {
		this.afterRestart = afterRestart;
	}
	
	public ConfigurationFieldTypeExt<?, ?> getType() {
		return this.type;
	}
	
	private void setType(ConfigurationFieldTypeExt<?, ?> type) {
		CheckUtils.isNotNull(type, "Type cannot be null");
		this.type = type;
	}
	
}
