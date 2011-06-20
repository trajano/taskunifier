package com.leclercb.taskunifier.gui.utils;

import java.net.URI;

import com.leclercb.commons.api.utils.http.HttpResponse;
import com.leclercb.taskunifier.gui.main.Main;

public class HttpUtils {
	
	public static HttpResponse getHttpGetResponse(URI uri) throws Exception {
		Boolean proxyEnabled = Main.SETTINGS.getBooleanProperty("proxy.enabled");
		if (proxyEnabled != null && proxyEnabled) {
			return com.leclercb.commons.api.utils.HttpUtils.getHttpGetResponse(
					uri,
					Main.SETTINGS.getStringProperty("proxy.host"),
					Main.SETTINGS.getIntegerProperty("proxy.port"),
					Main.SETTINGS.getStringProperty("proxy.login"),
					Main.SETTINGS.getStringProperty("proxy.password"));
		} else {
			return com.leclercb.commons.api.utils.HttpUtils.getHttpGetResponse(uri);
		}
	}
	
}
