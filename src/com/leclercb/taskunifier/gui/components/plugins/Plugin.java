package com.leclercb.taskunifier.gui.components.plugins;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.translations.Translations;

public class Plugin implements PropertyChangeSupported {
	
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
	
	public static final String PROP_STATUS = "status";
	public static final String PROP_ID = "id";
	public static final String PROP_NAME = "name";
	public static final String PROP_AUTHOR = "author";
	public static final String PROP_VERSION = "version";
	public static final String PROP_SERVICE_PROVIDER = "serviceProvider";
	public static final String PROP_DOWNLOAD_URL = "downloadUrl";
	
	private PropertyChangeSupport propertyChangeSupport;
	
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
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.setStatus(status);
		this.setId(id);
		this.setName(name);
		this.setAuthor(author);
		this.setVersion(version);
		this.setServiceProvider(serviceProvider);
		this.setDownloadUrl("http://taskunifier.sourceforge.net/TaskUnifierPluginToodledo.jar");
	}
	
	public PluginStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(PluginStatus status) {
		CheckUtils.isNotNull(status, "Status cannot be null");
		PluginStatus oldStatus = this.status;
		this.status = status;
		propertyChangeSupport.firePropertyChange(PROP_STATUS, oldStatus, status);
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		CheckUtils.isNotNull(id, "Id cannot be null");
		String oldId = this.id;
		this.id = id;
		propertyChangeSupport.firePropertyChange(PROP_ID, oldId, id);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		CheckUtils.isNotNull(name, "Name cannot be null");
		String oldName = this.name;
		this.name = name;
		propertyChangeSupport.firePropertyChange(PROP_NAME, oldName, name);
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public void setAuthor(String author) {
		CheckUtils.isNotNull(author, "Author cannot be null");
		String oldAuthor = this.author;
		this.author = author;
		propertyChangeSupport.firePropertyChange(PROP_AUTHOR, oldAuthor, author);
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public void setVersion(String version) {
		CheckUtils.isNotNull(version, "Version cannot be null");
		String oldVersion = this.version;
		this.version = version;
		propertyChangeSupport.firePropertyChange(PROP_VERSION, oldVersion, version);
	}
	
	public String getServiceProvider() {
		return this.serviceProvider;
	}
	
	public void setServiceProvider(String serviceProvider) {
		CheckUtils.isNotNull(serviceProvider, "Service provider cannot be null");
		String oldServiceProvider = this.serviceProvider;
		this.serviceProvider = serviceProvider;
		propertyChangeSupport.firePropertyChange(PROP_SERVICE_PROVIDER, oldServiceProvider, serviceProvider);
	}
	
	public String getDownloadUrl() {
		return this.downloadUrl;
	}
	
	public void setDownloadUrl(String downloadUrl) {
		CheckUtils.isNotNull(downloadUrl, "Download url cannot be null");
		String oldDownloadUrl = this.downloadUrl;
		this.downloadUrl = downloadUrl;
		propertyChangeSupport.firePropertyChange(PROP_DOWNLOAD_URL, oldDownloadUrl, downloadUrl);
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
