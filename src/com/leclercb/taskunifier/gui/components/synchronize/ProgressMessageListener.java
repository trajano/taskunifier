package com.leclercb.taskunifier.gui.components.synchronize;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.progress.DefaultProgressMessage;
import com.leclercb.commons.api.progress.ProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.ProgressMessageType;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.RetrieveModelsProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizationProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizeModelsProgressMessage;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public abstract class ProgressMessageListener implements ListChangeListener {
	
	public abstract void showMessage(ProgressMessage message, String content);
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			ProgressMessage message = (ProgressMessage) event.getValue();
			
			if (message instanceof DefaultProgressMessage) {
				DefaultProgressMessage m = (DefaultProgressMessage) message;
				
				this.showMessage(m, m.getMessage());
			} else if (message instanceof SynchronizationProgressMessage) {
				SynchronizationProgressMessage m = (SynchronizationProgressMessage) message;
				
				if (m.getType().equals(ProgressMessageType.START))
					this.showMessage(
							m,
							Translations.getString("synchronizer.start_synchronization"));
				else if (m.getType().equals(ProgressMessageType.END))
					this.showMessage(
							m,
							Translations.getString("synchronizer.synchronization_completed"));
			} else if (message instanceof RetrieveModelsProgressMessage) {
				RetrieveModelsProgressMessage m = (RetrieveModelsProgressMessage) message;
				
				if (m.getType().equals(ProgressMessageType.END))
					return;
				
				String type = TranslationsUtils.translateModelType(
						m.getModelType(),
						true);
				
				this.showMessage(m, Translations.getString(
						"synchronizer.retrieving_models",
						type));
			} else if (message instanceof SynchronizeModelsProgressMessage) {
				SynchronizeModelsProgressMessage m = (SynchronizeModelsProgressMessage) message;
				
				if (m.getType().equals(ProgressMessageType.END)
						|| m.getActionCount() == 0)
					return;
				
				String type = TranslationsUtils.translateModelType(
						m.getModelType(),
						m.getActionCount() > 1);
				
				this.showMessage(
						m,
						Translations.getString(
								"synchronizer.synchronizing",
								m.getActionCount(),
								type));
			}
		}
	}
	
}
