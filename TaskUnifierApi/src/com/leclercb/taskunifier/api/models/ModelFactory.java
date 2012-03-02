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
package com.leclercb.taskunifier.api.models;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.leclercb.taskunifier.api.models.beans.ModelBean;

public interface ModelFactory<OM extends Model, OMB extends ModelBean, M extends Model, MB extends ModelBean> {
	
	public abstract boolean contains(ModelId id);
	
	public abstract int size();
	
	public abstract List<M> getList();
	
	public abstract M get(int index);
	
	public abstract M get(ModelId id);
	
	public abstract M get(String key, String referenceId);
	
	public abstract int getIndexOf(M model);
	
	public abstract boolean markToDelete(ModelId modelId);
	
	public abstract void markToDelete(M model);
	
	public abstract boolean markDeleted(ModelId modelId);
	
	public abstract void markDeleted(M model);
	
	public abstract void deleteAll();
	
	public abstract OMB createOriginalBean();
	
	public abstract OMB createOriginalBean(ModelId id);
	
	public abstract MB createBean();
	
	public abstract MB createBean(ModelId id);
	
	public abstract M createShell(ModelId id);
	
	public abstract M create(MB bean, boolean loadReferenceIds);
	
	public abstract M create(M model);
	
	public abstract M create(ModelId id, M model);
	
	public abstract M create(String title);
	
	public abstract M create(ModelId id, String title);
	
	public abstract MB[] decodeBeansFromXML(InputStream input);
	
	public abstract void encodeBeansToXML(OutputStream output, MB[] beans);
	
	public abstract void decodeFromXML(InputStream input);
	
	public abstract void encodeToXML(OutputStream output);
	
}
