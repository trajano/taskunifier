/*******************************************************************************
 * Bizcal is a component library for calendar widgets written in java using swing.
 * Copyright (C) 2007  Frederik Bertilsson 
 * Contributors:       Martin Heinemann martin.heinemann(at)tudor.lu
 * 
 * http://sourceforge.net/projects/bizcal/
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 * 
 *******************************************************************************/
package bizcal.common;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * @author Fredrik Bertilsson
 */
public class Calendar {
	
	private Object id;
	private String summary;
	private Color color = Color.WHITE;
	private BufferedImage image;
	private boolean enabled = true;
	private boolean blankIsAvailable = true;
	private boolean closeable = false;
	
	public Object getId() {
		return this.id;
	}
	
	public void setId(Object id) {
		this.id = id;
	}
	
	public String getSummary() {
		return this.summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isBlankIsAvailable() {
		return this.blankIsAvailable;
	}
	
	public void setBlankIsAvailable(boolean blankIsAvailable) {
		this.blankIsAvailable = blankIsAvailable;
	}
	
	public boolean isCloseable() {
		return this.closeable;
	}
	
	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}
}
