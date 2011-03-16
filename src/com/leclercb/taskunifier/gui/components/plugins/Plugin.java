package com.leclercb.taskunifier.gui.components.plugins;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.translations.Translations;

public class Plugin {
	
	public static enum PluginStatus {
		
		TO_INSTALL(Translations.getString("plugin.to_install")),
		INSTALLED(Translations.getString("plugin.installed")),
		TO_UPDATE(Translations.getString("plugin.to_update"));
		
		private String label;
		
		private PluginStatus(String label) {
			this.label = label;
		}
		
		@Override
		public String toString() {
			return this.label;
		}
		
	}
	
	private PluginStatus status;
	private String id;
	private String name;
	private String author;
	private String version;
	private String serviceProvider;
	private String downloadUrl;
	
	public Plugin(
			PluginStatus status,
			String id,
			String name,
			String author,
			String version,
			String serviceProvider,
			String downloadUrl) {
		this.setStatus(status);
		this.setId(id);
		this.setName(name);
		this.setAuthor(author);
		this.setVersion(version);
		this.setServiceProvider(serviceProvider);
		this.setDownloadUrl(downloadUrl);
	}
	
	public PluginStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(PluginStatus status) {
		CheckUtils.isNotNull(status, "Status cannot be null");
		this.status = status;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		CheckUtils.isNotNull(id, "Id cannot be null");
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		CheckUtils.isNotNull(name, "Name cannot be null");
		this.name = name;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public void setAuthor(String author) {
		CheckUtils.isNotNull(author, "Author cannot be null");
		this.author = author;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public void setVersion(String version) {
		CheckUtils.isNotNull(version, "Version cannot be null");
		this.version = version;
	}
	
	public String getServiceProvider() {
		return this.serviceProvider;
	}
	
	public void setServiceProvider(String serviceProvider) {
		CheckUtils.isNotNull(serviceProvider, "Service provider cannot be null");
		this.serviceProvider = serviceProvider;
	}
	
	public String getDownloadUrl() {
		return this.downloadUrl;
	}
	
	public void setDownloadUrl(String downloadUrl) {
		CheckUtils.isNotNull(downloadUrl, "Download url cannot be null");
		this.downloadUrl = downloadUrl;
	}
	
}
