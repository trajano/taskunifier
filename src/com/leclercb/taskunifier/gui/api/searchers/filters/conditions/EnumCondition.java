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

public enum EnumCondition implements Condition<Enum<?>, Enum<?>> {
	
	EQUALS,
	GREATER_THAN,
	GREATER_THAN_OR_EQUALS,
	LESS_THAN,
	LESS_THAN_OR_EQUALS,
	NOT_EQUALS;
	
	private EnumCondition() {
		
	}
	
	@Override
	public Class<?> getValueType() {
		return Enum.class;
	}
	
	@Override
	public Class<?> getModelValueType() {
		return Enum.class;
	}
	
	@Override
	public boolean include(Enum<?> value, Enum<?> taskValue) {
		switch (this) {
			case EQUALS:
				return taskValue.equals(value);
			case GREATER_THAN:
				return taskValue.ordinal() > value.ordinal();
			case GREATER_THAN_OR_EQUALS:
				return taskValue.ordinal() >= value.ordinal();
			case LESS_THAN:
				return taskValue.ordinal() < value.ordinal();
			case LESS_THAN_OR_EQUALS:
				return taskValue.ordinal() <= value.ordinal();
			case NOT_EQUALS:
				return !(taskValue.equals(value));
		}
		
		return false;
	}
	
}
