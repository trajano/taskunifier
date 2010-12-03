/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.lookandfeel;

import java.awt.Window;

import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.utils.EqualsBuilder;
import com.leclercb.taskunifier.api.utils.HashCodeBuilder;
import com.leclercb.taskunifier.gui.lookandfeel.exc.LookAndFeelException;

public abstract class LookAndFeelDescriptor {

	private String name;
	private String identifier;

	public LookAndFeelDescriptor(String name, String identifier) {
		CheckUtils.isNotNull(name, "Name cannot be null");
		CheckUtils.isNotNull(identifier, "Identifier cannot be null");

		this.name = name;
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setLookAndFeel() throws LookAndFeelException {
		setLookAndFeel(null);
	}

	public abstract void setLookAndFeel(Window window) throws LookAndFeelException;

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof LookAndFeelDescriptor) {
			LookAndFeelDescriptor model = (LookAndFeelDescriptor) o;

			return new EqualsBuilder()
			.append(this.identifier, model.identifier)
			.isEqual();
		}

		return false;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCode = new HashCodeBuilder();
		hashCode.append(this.identifier);

		return hashCode.toHashCode();
	}

}
