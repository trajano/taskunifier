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
package com.leclercb.taskunifier.gui.images;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.leclercb.taskunifier.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.utils.EqualsBuilder;
import com.leclercb.taskunifier.api.utils.HashCodeBuilder;
import com.leclercb.taskunifier.gui.Main;

public final class Images {

	private Images() {

	}

	private static class ImageInfo {

		private String file;
		private int width;
		private int height;

		public ImageInfo(String file, int width, int height) {
			CheckUtils.isNotNull(file, "File cannot be null");
			this.file = file;
			this.width = width;
			this.height = height;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}

			if (o instanceof ImageInfo) {
				ImageInfo info = (ImageInfo) o;

				return new EqualsBuilder()
				.append(info.file, info.file)
				.append(info.width, info.width)
				.append(info.height, info.height)
				.isEqual();
			}

			return false;
		}

		@Override
		public int hashCode() {
			HashCodeBuilder hashCode = new HashCodeBuilder();
			hashCode.append(this.file);
			hashCode.append(this.width);
			hashCode.append(this.height);

			return hashCode.toHashCode();
		}

	}

	private static Map<ImageInfo, ImageIcon> images = new HashMap<Images.ImageInfo, ImageIcon>();

	private static final String IMAGES_FOLDER = Main.RESOURCE_FOLDER + File.separator + "images";

	public static ImageIcon getImage(String file) {
		ImageInfo info = new ImageInfo(file, -1, -1);

		if (images.containsKey(info))
			return images.get(info);

		System.out.println("Load " + file);
		ImageIcon instance = new ImageIcon(IMAGES_FOLDER + File.separator + file);
		images.put(info, instance);

		return instance;
	}

	public static ImageIcon getImage(String file, int width, int height) {
		ImageInfo info = new ImageInfo(file, width, height);

		if (images.containsKey(info))
			return images.get(info);

		System.out.println("Load " + file + " (" + width + ", " + height + ")");
		ImageIcon instance = new ImageIcon(getImage(file).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		images.put(info, instance);

		return instance;
	}

}
