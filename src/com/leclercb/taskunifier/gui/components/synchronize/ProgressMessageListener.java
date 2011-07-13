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
