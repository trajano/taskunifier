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
package com.leclercb.taskunifier.gui.plugins;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.api.utils.HttpResponse;
import com.leclercb.taskunifier.gui.api.synchronizer.exc.SynchronizerLicenseException;
import com.leclercb.taskunifier.gui.utils.HttpUtils;

public class PluginLicense {
	
	private String pluginId;
	private String email;
	private String serial;
	private String cipher;
	
	public PluginLicense(
			String pluginId,
			String email,
			String serial,
			String cipher) {
		this.setPluginId(pluginId);
		this.setEmail(email);
		this.setSerial(serial);
		this.setCipher(cipher);
	}
	
	public String getPluginId() {
		return this.pluginId;
	}
	
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		if (email != null && email.length() != 0)
			email = email.trim();
		
		this.email = email;
	}
	
	public String getSerial() {
		return this.serial;
	}
	
	public void setSerial(String serial) {
		if (serial != null && serial.length() != 0)
			serial = serial.trim().toUpperCase();
		
		this.serial = serial;
	}
	
	public String getCipher() {
		return this.cipher;
	}
	
	public void setCipher(String cipher) {
		this.cipher = cipher;
	}
	
	public boolean checkLicense() throws SynchronizerLicenseException {
		return this.checkLicense(true);
	}
	
	public boolean checkLicense(boolean getLicense)
			throws SynchronizerLicenseException {
		if (this.email == null || this.email.length() == 0)
			return false;
		
		if (getLicense
				&& (this.serial == null || this.serial.length() == 0 || !this.checkValidLicense()))
			this.serial = this.getLicense();
		
		if (this.serial == null || this.serial.length() == 0)
			return false;
		
		if (this.checkValidLicense()) {
			this.setSerial(this.serial);
			return true;
		}
		
		return false;
	}
	
	private String getLicense() throws SynchronizerLicenseException {
		if (this.email == null || this.email.length() == 0)
			return null;
		
		try {
			HttpResponse response = HttpUtils.getHttpGetResponse(this.getCheckSerialUri());
			
			if (!response.isSuccessfull())
				throw new Exception("Error while retrieving license key");
			
			return response.getContent().trim();
		} catch (SynchronizerLicenseException e) {
			throw e;
		} catch (Exception e) {
			throw new SynchronizerLicenseException(e.getMessage(), e);
		}
	}
	
	private boolean checkValidLicense() {
		if (this.email == null || this.email.length() == 0)
			return false;
		
		if (this.serial == null || this.serial.length() == 0)
			return false;
		
		String data = this.email + this.cipher;
		String validSerial = DigestUtils.shaHex(data).toUpperCase();
		
		return EqualsUtils.equals(this.serial, validSerial);
	}
	
	private URI getCheckSerialUri() throws Exception {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("plugin", this.pluginId));
		parameters.add(new BasicNameValuePair("email", this.email));
		
		return URIUtils.createURI(
				"http",
				"www.taskunifier.com",
				-1,
				"/plugins/paypal/checklicense.php",
				URLEncodedUtils.format(parameters, "UTF-8"),
				null);
	}
	
}
