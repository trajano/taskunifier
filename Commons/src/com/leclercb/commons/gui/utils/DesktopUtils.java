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
package com.leclercb.commons.gui.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.leclercb.commons.api.utils.CheckUtils;

public final class DesktopUtils {
	
	private DesktopUtils() {
		
	}
	
	private static void isSupported(Desktop.Action action) throws Exception {
		if (!Desktop.isDesktopSupported())
			throw new Exception("Desktop is not supported");
		
		Desktop desktop = Desktop.getDesktop();
		
		if (!desktop.isSupported(action))
			throw new Exception("Desktop doesn't support the action \""
					+ action.toString()
					+ "\"");
	}
	
	public static void browse(String url) throws Exception {
		isSupported(Desktop.Action.BROWSE);
		Desktop.getDesktop().browse(new URI(url));
	}
	
	public static void edit(File file) throws Exception {
		isSupported(Desktop.Action.EDIT);
		Desktop.getDesktop().edit(file);
	}
	
	public static void open(File file) throws Exception {
		isSupported(Desktop.Action.OPEN);
		Desktop.getDesktop().open(file);
	}
	
	public static void mail(
			String[] to,
			String[] cc,
			String subject,
			String body) throws Exception {
		isSupported(Desktop.Action.MAIL);
		CheckUtils.isNotNull(to);
		
		String toStr = encode(StringUtils.join(to, ";"), null) + "?";
		
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		
		if (cc != null)
			parameters.add(new BasicNameValuePair("cc", StringUtils.join(
					cc,
					";")));
		
		if (subject != null && subject.length() != 0)
			parameters.add(new BasicNameValuePair("subject", subject));
		
		if (body != null && body.length() != 0)
			parameters.add(new BasicNameValuePair("body", body));
		
		String url = "mailto:"
				+ toStr
				+ URLEncodedUtils.format(parameters, (String) null);
		
		url = url.replaceAll("\\+", "%20");
		
		Desktop.getDesktop().mail(new URI(url));
	}
	
	private static String encode(final String content, final String encoding) {
		try {
			return URLEncoder.encode(
					content,
					encoding != null ? encoding : HTTP.DEF_CONTENT_CHARSET.name());
		} catch (UnsupportedEncodingException problem) {
			throw new IllegalArgumentException(problem);
		}
	}
	
}
