package com.leclercb.taskunifier.gui.components.synchronize;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.progress.DefaultProgressMessage;
import com.leclercb.commons.api.progress.ProgressMessage;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.ProgressMessageType;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.RetrieveModelsProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizationProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizeModelsProgressMessage;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public abstract class ProgressMessageListener implements ListChangeListener {
	
	public abstract void showMessage(String message);
	
	@Override
	public void listChange(ListChangeEvent event) {
		if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
			ProgressMessage message = (ProgressMessage) event.getValue();
			
			if (message instanceof DefaultProgressMessage) {
				DefaultProgressMessage m = (DefaultProgressMessage) message;
				
				this.showMessage(m.getMessage());
			} else if (message instanceof SynchronizationProgressMessage) {
				SynchronizationProgressMessage m = (SynchronizationProgressMessage) message;
				
				if (m.getType().equals(ProgressMessageType.START))
					this.showMessage(Translations.getString("synchronizer.start_synchronization"));
				else
					this.showMessage(Translations.getString("synchronizer.synchronization_completed"));
			} else if (message instanceof RetrieveModelsProgressMessage) {
				RetrieveModelsProgressMessage m = (RetrieveModelsProgressMessage) message;
				
				if (m.getType().equals(ProgressMessageType.END))
					return;
				
				String type = this.modelTypeToString(m.getModelType(), true);
				
				this.showMessage(Translations.getString(
						"synchronizer.retrieving_models",
						type));
			} else if (message instanceof SynchronizeModelsProgressMessage) {
				SynchronizeModelsProgressMessage m = (SynchronizeModelsProgressMessage) message;
				
				if (m.getType().equals(ProgressMessageType.END)
						|| m.getActionCount() == 0)
					return;
				
				String type = this.modelTypeToString(
						m.getModelType(),
						m.getActionCount() > 1);
				
				this.showMessage(Translations.getString(
						"synchronizer.synchronizing",
						m.getActionCount(),
						type));
			}
		}
	}
	
	private String modelTypeToString(ModelType type, boolean plurial) {
		if (plurial) {
			switch (type) {
				case CONTEXT:
					return Translations.getString("general.contexts");
				case FOLDER:
					return Translations.getString("general.folders");
				case GOAL:
					return Translations.getString("general.goals");
				case LOCATION:
					return Translations.getString("general.locations");
				case NOTE:
					return Translations.getString("general.notes");
				case TASK:
					return Translations.getString("general.tasks");
			}
		}
		
		switch (type) {
			case CONTEXT:
				return Translations.getString("general.context");
			case FOLDER:
				return Translations.getString("general.folder");
			case GOAL:
				return Translations.getString("general.goal");
			case LOCATION:
				return Translations.getString("general.location");
			case NOTE:
				return Translations.getString("general.note");
			case TASK:
				return Translations.getString("general.task");
		}
		
		return null;
	}
	
}
