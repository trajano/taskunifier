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
package bizcal.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Fredrik Bertilsson
 */
public class LocalizedText {
	
	private String _bundleBaseName;
	private String _text;
	private LocalizedText fallback;
	private String defaultValue;
	
	public LocalizedText(String text, String bundleBaseName) {
		this._text = text;
		this._bundleBaseName = bundleBaseName;
		this.defaultValue = text;
	}
	
	public LocalizedText(String text) {
		this(text, null);
	}
	
	@Override
	public String toString() {
		try {
			if (this._bundleBaseName == null)
				return this._text;
			ResourceBundle bundle = ResourceBundle.getBundle(
					this._bundleBaseName,
					LocaleBroker.getLocale());
			try {
				return bundle.getString(this._text);
			} catch (MissingResourceException mre) {
				if (this.fallback != null)
					return this.fallback.toString();
				return this.defaultValue;
			}
		} catch (Exception e) {
			throw BizcalException.create(e);
		}
	}
	
	public String getKey() {
		return this._text;
	}
	
	public String getDefaultValue() {
		return this.defaultValue;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public void setFallback(LocalizedText fallback) {
		this.fallback = fallback;
	}
	
	public static class Factory {
		
		private String _bundleBaseName;
		
		public Factory(String bundleBaseName) {
			this._bundleBaseName = bundleBaseName;
		}
		
		public LocalizedText createText(String text) {
			return new LocalizedText(text, this._bundleBaseName);
		}
	}
	
}
