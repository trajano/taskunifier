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
package com.leclercb.taskunifier.gui.utils;

import com.leclercb.taskunifier.api.utils.CheckUtils;

public final class FileUtils {

	private FileUtils() {

	}

	public static String getExtention(String fileName) {
		CheckUtils.isNotNull(fileName, "File name cannot be null");

		int lastIndexOfDot = fileName.lastIndexOf('.');

		if (lastIndexOfDot < 0)
			return "";

		return fileName.substring(lastIndexOfDot + 1, fileName.length());
	}

	public static boolean hasExtention(String fileName, String... extentions) {
		CheckUtils.isNotNull(fileName, "File name cannot be null");
		CheckUtils.isNotNull(extentions, "Extentions cannot be null");

		String extention = getExtention(fileName);

		for (int i=0; i<extentions.length; i++)
			if (extention.equals(extentions[i]))
				return true;

		return false;
	}

	public static String removeExtention(String fileName) {
		CheckUtils.isNotNull(fileName, "File name cannot be null");

		int lastIndexOfDot = fileName.lastIndexOf('.');

		if (lastIndexOfDot < 0)
			return fileName;

		return fileName.substring(0, lastIndexOfDot);
	}

}
