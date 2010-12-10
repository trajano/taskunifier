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
package com.leclercb.taskunifier.gui.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class GuiLogger {

	private GuiLogger() {

	}

	private static final Logger LOGGER;

	static {
		try {
			LOGGER = Logger.getLogger(GuiLogger.class.getPackage().getName());
			LOGGER.setLevel(Level.ALL);
		} catch (SecurityException e) {
			throw new RuntimeException("Cannot create API logger", e);
		}
	}

	public static Logger getLogger() {
		return LOGGER;
	}

}
