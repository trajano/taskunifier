package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.properties.events.ReloadPropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.buttons.TUApplyButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUCancelButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUOkButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class PluginConfigurationDialog extends JDialog implements ConfigurationGroup {
	
	private static PluginConfigurationDialog INSTANCE;
	
	public static PluginConfigurationDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PluginConfigurationDialog();
		
		return INSTANCE;
	}
	
	private SynchronizerGuiPlugin plugin;
	
	private JPanel pluginContainerPanel;
	private ConfigurationPanel pluginConfigurationPanel;
	
	private PluginConfigurationDialog() {
		super(ConfigurationDialog.getInstance(), true);
		
		this.initialize();
	}
	
	public void setPlugin(SynchronizerGuiPlugin plugin) {
		CheckUtils.isNotNull(plugin);
		
		if (EqualsUtils.equals(this.plugin, plugin))
			return;
		
		this.plugin = plugin;
		
		this.setTitle(plugin.getName());
		this.initializePluginPanel();
	}
	
	private void initialize() {
		this.setSize(800, 500);
		this.setResizable(true);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JXHeader header = new JXHeader();
		header.setTitle(Translations.getString("header.title.configuration"));
		header.setDescription(Translations.getString("header.description.configuration"));
		header.setIcon(ImageUtils.getResourceImage("settings.png", 32, 32));
		
		this.add(header, BorderLayout.NORTH);
		
		this.pluginContainerPanel = new JPanel();
		this.pluginContainerPanel.setLayout(new BorderLayout());
		
		this.initializeButtonsPanel();
		this.initializePluginPanel();
		
		this.add(this.pluginContainerPanel, BorderLayout.CENTER);
		
		Main.getUserSettings().addReloadPropertiesListener(
				new ReloadPropertiesListener() {
					
					@Override
					public void reloadProperties() {
						PluginConfigurationDialog.this.cancelConfig();
					}
					
				});
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("OK")) {
					PluginConfigurationDialog.this.saveAndApplyConfig();
					PluginConfigurationDialog.this.setVisible(false);
				}
				
				if (event.getActionCommand().equals("CANCEL")) {
					PluginConfigurationDialog.this.cancelConfig();
					PluginConfigurationDialog.this.setVisible(false);
				}
				
				if (event.getActionCommand().equals("APPLY")) {
					PluginConfigurationDialog.this.saveAndApplyConfig();
				}
			}
			
		};
		
		JButton okButton = new TUOkButton(listener);
		JButton cancelButton = new TUCancelButton(listener);
		JButton applyButton = new TUApplyButton(listener);
		
		JPanel panel = new TUButtonsPanel(okButton, cancelButton, applyButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(okButton);
	}
	
	private void initializePluginPanel() {
		if (this.plugin == null)
			return;
		
		this.pluginContainerPanel.removeAll();
		
		this.pluginConfigurationPanel = new PluginConfigurationPanel(
				this,
				false,
				this.plugin);
		
		this.pluginContainerPanel.add(ComponentFactory.createJScrollPane(
				this.pluginConfigurationPanel,
				false), BorderLayout.CENTER);
		
		this.pluginContainerPanel.validate();
	}
	
	@Override
	public void saveAndApplyConfig() {
		try {
			this.pluginConfigurationPanel.saveAndApplyConfig();
			
			Main.saveSettings();
			Main.saveUserSettings();
		} catch (Exception e) {
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					Translations.getString("error.save_settings"),
					null,
					null,
					e,
					null,
					null);
			
			JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
			
			return;
		}
	}
	
	@Override
	public void cancelConfig() {
		try {
			this.pluginConfigurationPanel.cancelConfig();
		} catch (Exception e) {
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					Translations.getString("error.save_settings"),
					null,
					null,
					e,
					null,
					null);
			
			JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
			
			return;
		}
	}
	
}
