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
package com.leclercb.taskunifier.gui.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelFactory;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.beans.ModelBean;
import com.leclercb.taskunifier.api.models.beans.TaskBean;
import com.leclercb.taskunifier.api.models.utils.ModelFactoryUtils;
import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerPlugin;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizerMainProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizerMainProgressMessage.ProgressMessageType;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizerRetrievedModelsProgressMessage;
import com.leclercb.taskunifier.api.synchronizer.progress.messages.SynchronizerUpdatedModelsProgressMessage;
import com.leclercb.taskunifier.gui.swing.TUSwingUtilities;

public abstract class AbstractSynchronizer implements Synchronizer {
	
	private Connection connection;
	private String keyId;
	
	public AbstractSynchronizer(Connection connection, String keyId) {
		CheckUtils.isNotNull(connection);
		CheckUtils.isNotNull(keyId);
		
		this.connection = connection;
		this.keyId = keyId;
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public abstract SynchronizerPlugin getPlugin();
	
	@Override
	public final void publish() throws SynchronizerException {
		throw new SynchronizerException(true, "Publish is not supported");
	}
	
	@Override
	public final void publish(ProgressMonitor monitor)
			throws SynchronizerException {
		throw new SynchronizerException(true, "Publish is not supported");
	}
	
	@Override
	public final void synchronize() throws SynchronizerException {
		this.synchronize(SynchronizerChoice.KEEP_LAST_UPDATED, null);
	}
	
	@Override
	public final void synchronize(SynchronizerChoice choice)
			throws SynchronizerException {
		this.synchronize(choice, null);
	}
	
	@Override
	public final void synchronize(ProgressMonitor monitor)
			throws SynchronizerException {
		this.synchronize(SynchronizerChoice.KEEP_LAST_UPDATED, monitor);
	}
	
	public final void synchronizeModels(
			SynchronizerChoice choice,
			ProgressMonitor monitor,
			ModelType[] types) throws SynchronizerException {
		if (!this.connection.isConnected())
			throw new SynchronizerException(
					false,
					"Cannot synchronize if not connected");
		
		if (monitor != null)
			monitor.addMessage(new SynchronizerMainProgressMessage(
					this.getPlugin(),
					ProgressMessageType.SYNCHRONIZER_START));
		
		for (ModelType type : types)
			this.synchronizeModels(choice, monitor, type);
		
		for (ModelType type : types)
			this.removeShells(type);
		
		if (monitor != null)
			monitor.addMessage(new SynchronizerMainProgressMessage(
					this.getPlugin(),
					ProgressMessageType.SYNCHRONIZER_END));
	}
	
	private void synchronizeModels(
			final SynchronizerChoice choice,
			final ProgressMonitor monitor,
			final ModelType type) throws SynchronizerException {
		@SuppressWarnings("unchecked")
		final List<Model> models = (List<Model>) ModelFactoryUtils.getFactory(
				type).getList();
		
		final List<ModelBean> updatedModels = new ArrayList<ModelBean>();
		
		if (this.isUpdatedModels(type)) {
			if (monitor != null)
				monitor.addMessage(new SynchronizerRetrievedModelsProgressMessage(
						this.getPlugin(),
						ProgressMessageType.SYNCHRONIZER_START,
						type));
			
			updatedModels.addAll(this.getUpdatedModels(type));
			
			if (monitor != null)
				monitor.addMessage(new SynchronizerRetrievedModelsProgressMessage(
						this.getPlugin(),
						ProgressMessageType.SYNCHRONIZER_END,
						type));
		}
		
		final List<ModelBean> deletedModels = this.getDeletedModels(type);
		
		if (type == ModelType.GOAL) {
			Collections.sort(models, new Comparator<Model>() {
				
				@Override
				public int compare(Model o1, Model o2) {
					Goal g1 = (Goal) o1;
					Goal g2 = (Goal) o2;
					
					if (g1.getContributes() == null
							&& g2.getContributes() == null)
						return 0;
					
					if (g1.getContributes() != null
							&& g2.getContributes() != null)
						return 0;
					
					if (g1.getContributes() == null)
						return -1;
					
					return 1;
				}
				
			});
		}
		
		if (type == ModelType.TASK) {
			Collections.sort(models, new Comparator<Model>() {
				
				@Override
				public int compare(Model o1, Model o2) {
					Task t1 = (Task) o1;
					Task t2 = (Task) o2;
					
					if (t1.getParent() == null && t2.getParent() == null)
						return 0;
					
					if (t1.getParent() != null && t2.getParent() != null)
						return 0;
					
					if (t1.getParent() == null)
						return -1;
					
					return 1;
				}
				
			});
		}
		
		// Compute Action Count
		int actionCount = 0;
		
		for (Model model : models) {
			if (model.getModelStatus().isEndUserStatus())
				if (model.getModelReferenceId(this.keyId) == null)
					actionCount++;
			
			if (model.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
				if (model.getModelReferenceId(this.keyId) != null) {
					ModelBean bean = null;
					for (ModelBean updatedModel : updatedModels) {
						if (EqualsUtils.equalsString(
								updatedModel.getModelReferenceIds().get(
										this.keyId),
								model.getModelReferenceId(this.keyId))) {
							bean = updatedModel;
							break;
						}
					}
					
					if (bean != null) {
						if (!choice.chooseLocalModel(model, bean)) {
							continue;
						}
					}
					
					actionCount++;
				}
			}
			
			if (model.getModelStatus().equals(ModelStatus.TO_DELETE))
				if (model.getModelReferenceId(this.keyId) == null)
					actionCount++;
			
		}
		
		if (monitor != null && actionCount > 0)
			monitor.addMessage(new SynchronizerUpdatedModelsProgressMessage(
					this.getPlugin(),
					ProgressMessageType.SYNCHRONIZER_START,
					type,
					actionCount));
		
		final List<Model> modelsToSync = new ArrayList<Model>();
		final List<Model> modelsToDelete = new ArrayList<Model>();
		
		// Start Delete Action
		for (Model model : models) {
			if (model.getModelStatus().equals(ModelStatus.TO_DELETE)) {
				if (model.getModelReferenceId(this.keyId) != null) {
					ModelBean bean = null;
					for (ModelBean updatedModel : updatedModels) {
						if (EqualsUtils.equalsString(
								updatedModel.getModelReferenceIds().get(
										this.keyId),
								model.getModelReferenceId(this.keyId))) {
							bean = updatedModel;
							break;
						}
					}
					
					if (bean != null) {
						if (!choice.chooseLocalModel(model, bean)) {
							continue;
						}
					}
					
					modelsToDelete.add(model);
					
					PluginLogger.getLogger().info(
							"Delete " + type + ": " + model.getModelId());
				}
			}
		}
		
		this.deleteModels(type, modelsToDelete);
		
		TUSwingUtilities.invokeAndWait(new Runnable() {
			
			@Override
			public void run() {
				for (Model model : modelsToDelete)
					model.setModelStatus(ModelStatus.DELETED);
			}
			
		});
		
		modelsToDelete.clear();
		
		// Remove Deleted Models
		for (ModelBean deletedModel : deletedModels) {
			Model model = ModelFactoryUtils.getFactory(type).get(
					this.keyId,
					deletedModel.getModelReferenceIds().get(this.keyId));
			
			if (model == null)
				continue;
			
			if (choice.chooseLocalModel(model, deletedModel)) {
				if (model.getModelStatus().isEndUserStatus()) {
					modelsToSync.add(model);
					continue;
				}
			}
			
			modelsToDelete.add(model);
		}
		
		TUSwingUtilities.invokeAndWait(new Runnable() {
			
			@Override
			public void run() {
				for (Model model : modelsToSync) {
					model.removeModelReferenceId(AbstractSynchronizer.this.keyId);
					model.setModelStatus(ModelStatus.TO_UPDATE);
					
					if (model instanceof Task) {
						for (Task child : ((Task) model).getAllChildren())
							if (child.getModelStatus().isEndUserStatus())
								child.setModelStatus(ModelStatus.TO_UPDATE);
					}
				}
				
				for (Model model : modelsToDelete)
					model.setModelStatus(ModelStatus.DELETED);
			}
			
		});
		
		modelsToSync.clear();
		modelsToDelete.clear();
		
		// Start Add Action
		if (type == ModelType.TASK) {
			// Update all the children
			for (Model model : models) {
				Task task = (Task) model;
				if (task.getModelStatus().isEndUserStatus()) {
					if (task.getModelReferenceId("toodledo") == null
							|| task.getModelStatus().equals(
									ModelStatus.TO_UPDATE)) {
						for (Task child : task.getAllChildren()) {
							if (child.getModelStatus().isEndUserStatus()) {
								child.setModelStatus(ModelStatus.TO_UPDATE);
							}
						}
					}
				}
			}
		}
		
		for (Model model : models) {
			if (model.getModelStatus().isEndUserStatus()) {
				if (model.getModelReferenceId(this.keyId) == null) {
					modelsToSync.add(model);
					
					PluginLogger.getLogger().info(
							"Add " + type + ": " + model.getModelId());
				}
			}
		}
		
		final List<String> addedIds = this.addModels(type, modelsToSync);
		
		TUSwingUtilities.invokeAndWait(new Runnable() {
			
			@Override
			public void run() {
				for (int i = 0; i < modelsToSync.size(); i++) {
					modelsToSync.get(i).addModelReferenceId(
							AbstractSynchronizer.this.keyId,
							addedIds.get(i));
					modelsToSync.get(i).setModelStatus(ModelStatus.LOADED);
				}
			}
			
		});
		
		modelsToSync.clear();
		
		// Start Update Action
		for (Model model : models) {
			if (model.getModelStatus().equals(ModelStatus.TO_UPDATE)) {
				if (model.getModelReferenceId(this.keyId) != null) {
					ModelBean bean = null;
					for (ModelBean updatedModel : updatedModels) {
						if (EqualsUtils.equalsString(
								updatedModel.getModelReferenceIds().get(
										this.keyId),
								model.getModelReferenceId(this.keyId))) {
							bean = updatedModel;
							break;
						}
					}
					
					if (bean != null) {
						if (!choice.chooseLocalModel(model, bean)) {
							continue;
						}
					}
					
					updatedModels.remove(bean);
					
					modelsToSync.add(model);
					
					PluginLogger.getLogger().info(
							"Update " + type + ": " + model.getModelId());
				}
			}
		}
		
		this.updateModels(type, modelsToSync);
		
		TUSwingUtilities.invokeAndWait(new Runnable() {
			
			@Override
			public void run() {
				for (Model model : modelsToSync)
					model.setModelStatus(ModelStatus.LOADED);
			}
			
		});
		
		modelsToSync.clear();
		
		// Replace Updated Models
		if (type == ModelType.TASK) {
			Collections.sort(updatedModels, new Comparator<ModelBean>() {
				
				@Override
				public int compare(ModelBean o1, ModelBean o2) {
					TaskBean t1 = (TaskBean) o1;
					TaskBean t2 = (TaskBean) o2;
					
					if (t1.getParent() == null && t2.getParent() == null)
						return 0;
					
					if (t1.getParent() != null && t2.getParent() != null)
						return 0;
					
					if (t1.getParent() == null)
						return -1;
					
					return 1;
				}
				
			});
		}
		
		TUSwingUtilities.invokeAndWait(new Runnable() {
			
			@Override
			public void run() {
				for (ModelBean updatedModel : updatedModels) {
					Model model = ModelFactoryUtils.getFactory(type).get(
							AbstractSynchronizer.this.keyId,
							updatedModel.getModelReferenceIds().get(
									AbstractSynchronizer.this.keyId));
					
					if (model != null
							&& model.getModelStatus() == ModelStatus.DELETED)
						continue;
					
					if (model == null)
						model = ModelFactoryUtils.create(
								type,
								updatedModel,
								true);
					else
						model.loadBean(updatedModel, false);
				}
			}
			
		});
		
		if (monitor != null && actionCount > 0)
			monitor.addMessage(new SynchronizerUpdatedModelsProgressMessage(
					this.getPlugin(),
					ProgressMessageType.SYNCHRONIZER_END,
					type,
					actionCount));
	}
	
	private void removeShells(ModelType type) {
		ModelFactory<?, ?, ?, ?> factory = ModelFactoryUtils.getFactory(type);
		
		for (Model model : factory.getList()) {
			if (model.getModelStatus() == ModelStatus.SHELL) {
				factory.markDeleted(model.getModelId());
				
				PluginLogger.getLogger().info(
						"Delete " + type + " shell: " + model.getModelId());
			}
		}
	}
	
	protected abstract boolean isUpdatedModels(ModelType type)
			throws SynchronizerException;
	
	protected abstract List<ModelBean> getUpdatedModels(ModelType type)
			throws SynchronizerException;
	
	protected abstract List<ModelBean> getDeletedModels(ModelType type)
			throws SynchronizerException;
	
	protected abstract List<String> addModels(ModelType type, List<Model> models)
			throws SynchronizerException;
	
	protected abstract void updateModels(ModelType type, List<Model> models)
			throws SynchronizerException;
	
	protected abstract void deleteModels(ModelType type, List<Model> models)
			throws SynchronizerException;
	
}
