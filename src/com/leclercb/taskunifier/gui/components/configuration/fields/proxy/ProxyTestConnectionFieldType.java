package com.leclercb.taskunifier.gui.components.configuration.fields.proxy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.TUWaitDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.HttpUtils;

public class ProxyTestConnectionFieldType extends ConfigurationFieldType.Button {
	
	public ProxyTestConnectionFieldType(ConfigurationPanel panel) {
		super(
				Translations.getString("configuration.proxy.test_connection"),
				new TestConnection(panel));
	}
	
	private static class TestConnection implements ActionListener {
		
		private ConfigurationPanel panel;
		
		public TestConnection(ConfigurationPanel panel) {
			CheckUtils.isNotNull(panel, "Configuration panel cannot be null");
			this.panel = panel;
		}
		
		@Override
		public void actionPerformed(ActionEvent evt) {
			this.panel.saveAndApplyConfig();
			
			final TUWaitDialog dialog = new TUWaitDialog(
					MainFrame.getInstance().getFrame(),
					Translations.getString("configuration.proxy.test_connection"));
			
			dialog.setWorker(new SwingWorker<Void, Void>() {
				
				@Override
				protected Void doInBackground() throws Exception {
					dialog.appendToProgressStatus(Translations.getString("configuration.proxy.test_connection"));
					
					try {
						HttpUtils.getHttpGetResponse(new URI(
								Constants.TEST_CONNECTION));
						TestConnection.this.showResult(true);
					} catch (Throwable t) {
						TestConnection.this.showResult(false);
					}
					
					return null;
				}
				
				@Override
				protected void done() {
					dialog.dispose();
				}
				
			});
			
			dialog.setVisible(true);
		}
		
		private void showResult(boolean result) {
			if (result) {
				JOptionPane.showMessageDialog(
						MainFrame.getInstance().getFrame(),
						Translations.getString("configuration.proxy.test_connection.success"),
						Translations.getString("general.information"),
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(
						MainFrame.getInstance().getFrame(),
						Translations.getString("configuration.proxy.test_connection.failed"),
						Translations.getString("general.error"),
						JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
}
