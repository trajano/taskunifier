package com.leclercb.taskunifier.gui.components.configuration.fields.proxy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

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
			CheckUtils.isNotNull(panel);
			this.panel = panel;
		}
		
		@Override
		public void actionPerformed(ActionEvent evt) {
			this.panel.saveAndApplyConfig();
			
			final TUWaitDialog dialog = new TUWaitDialog(
					MainFrame.getInstance().getFrame(),
					Translations.getString("configuration.proxy.test_connection"));
			
			dialog.setWorker(new SwingWorker<Void, String>() {
				
				@Override
				protected void process(List<String> messages) {
					for (String message : messages) {
						dialog.appendToProgressStatus(message);
					}
				}
				
				@Override
				protected Void doInBackground() throws Exception {
					this.publish(Translations.getString("configuration.proxy.test_connection"));
					
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
				ErrorInfo info = new ErrorInfo(
						Translations.getString("general.error"),
						Translations.getString("configuration.proxy.test_connection.failed"),
						null,
						null,
						null,
						null,
						null);
				
				JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
			}
		}
		
	}
	
}
