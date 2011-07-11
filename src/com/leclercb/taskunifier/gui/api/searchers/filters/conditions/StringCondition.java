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
package com.leclercb.taskunifier.gui.api.searchers.filters.conditions;

import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public enum StringCondition implements Condition<String, Object> {
	
	EQUALS,
	CONTAINS,
	STARTS_WITH,
	ENDS_WITH,
	NOT_EQUALS;
	
	private StringCondition() {

	}
	
	@Override
	public Class<?> getValueType() {
		return String.class;
	}
	
	@Override
	public Class<?> getTaskValueType() {
		return Object.class;
	}
	
	@Override
	public boolean include(String value, Object taskValue) {
		if (this == EQUALS) {
			return taskValue.toString().equalsIgnoreCase(value);
		}
		
		if (this == CONTAINS) {
			return taskValue.toString().toLowerCase().contains(
					value.toLowerCase());
		}
		
		if (this == STARTS_WITH) {
			return taskValue.toString().toLowerCase().startsWith(
					value.toLowerCase());
		}
		
		if (this == ENDS_WITH) {
			return taskValue.toString().toLowerCase().endsWith(
					value.toLowerCase());
		}
		
		if (this == NOT_EQUALS) {
			return !(taskValue.toString().equalsIgnoreCase(value));
		}
		
		return false;
	}
	
}
