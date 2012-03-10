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
package com.leclercb.commons.api.utils;

import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URI;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public final class HttpUtils {
	
	private HttpUtils() {
		
	}
	
	public static HttpResponse getHttpGetResponse(URI uri) throws Exception {
		return getHttpGetResponse(uri, null, 0, null, null);
	}
	
	public static HttpResponse getHttpGetResponse(
			URI uri,
			final String host,
			final int port,
			final String username,
			final String password) throws Exception {
		return getHttpResponse(true, uri, null, host, port, username, password);
	}
	
	public static HttpResponse getHttpPostResponse(
			URI uri,
			List<NameValuePair> parameters) throws Exception {
		return getHttpPostResponse(uri, parameters, null, 0, null, null);
	}
	
	public static HttpResponse getHttpPostResponse(
			URI uri,
			List<NameValuePair> parameters,
			final String host,
			final int port,
			final String username,
			final String password) throws Exception {
		return getHttpResponse(
				false,
				uri,
				parameters,
				host,
				port,
				username,
				password);
	}
	
	private static HttpResponse getHttpResponse(
			boolean get,
			URI uri,
			List<NameValuePair> parameters,
			final String host,
			final int port,
			final String username,
			final String password) throws Exception {
		HttpURLConnection connection = null;
		
		if (host == null || host.length() == 0) {
			connection = (HttpURLConnection) uri.toURL().openConnection();
		} else {
			if (username != null && username.length() != 0 && password != null) {
				Authenticator.setDefault(new Authenticator() {
					
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								username,
								password.toCharArray());
					}
					
				});
			}
			
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					host,
					port));
			connection = (HttpURLConnection) uri.toURL().openConnection(proxy);
		}
		
		if (!get && parameters != null && parameters.size() != 0) {
			connection.setDoOutput(true);
			IOUtils.write(
					URLEncodedUtils.format(parameters, "UTF-8"),
					connection.getOutputStream());
		}
		
		InputStream inputStream = null;
		
		if (connection.getResponseCode() == 200)
			inputStream = connection.getInputStream();
		else
			inputStream = connection.getErrorStream();
		
		if (inputStream == null)
			throw new Exception("HTTP error: "
					+ connection.getResponseCode()
					+ " - "
					+ connection.getResponseMessage());
		
		byte[] bytes = null;
		
		if (connection.getResponseCode() == 200)
			bytes = IOUtils.toByteArray(inputStream);
		
		return new HttpResponse(
				connection.getResponseCode(),
				connection.getResponseMessage(),
				bytes);
	}
	
}
