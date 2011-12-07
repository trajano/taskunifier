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
package com.leclercb.taskunifier.gui.utils;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.Main;

public final class ImageUtils {
	
	private ImageUtils() {
		
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
				
				return new EqualsBuilder().append(this.file, info.file).append(
						this.width,
						info.width).append(this.height, info.height).isEquals();
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
	
	private static Map<ImageInfo, ImageIcon> images = new HashMap<ImageUtils.ImageInfo, ImageIcon>();
	
	private static final String IMAGES_FOLDER = Main.RESOURCES_FOLDER
			+ File.separator
			+ "images";
	
	public static String getResourceFile(String file) {
		return IMAGES_FOLDER + File.separator + file;
	}
	
	public static ImageIcon getResourceImage(String file) {
		return getImage(IMAGES_FOLDER + File.separator + file);
	}
	
	public static ImageIcon getResourceImage(String file, int width, int height) {
		return getImage(IMAGES_FOLDER + File.separator + file, width, height);
	}
	
	public static ImageIcon getImage(String file) {
		ImageInfo info = new ImageInfo(file, -1, -1);
		
		if (images.containsKey(info))
			return images.get(info);
		
		ImageIcon instance = new ImageIcon(file);
		images.put(info, instance);
		
		return instance;
	}
	
	public static ImageIcon getImage(String file, int width, int height) {
		ImageInfo info = new ImageInfo(file, width, height);
		
		if (images.containsKey(info))
			return images.get(info);
		
		ImageIcon instance = new ImageIcon(
				getImage(file).getImage().getScaledInstance(
						width,
						height,
						Image.SCALE_SMOOTH));
		images.put(info, instance);
		
		return instance;
	}
	
}
