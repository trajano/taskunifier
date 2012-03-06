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
package com.leclercb.taskunifier.api.models.beans.converters;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelList;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.beans.ModelBeanList;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public abstract class ModelListConverter<M extends Model> extends ReflectionConverter {
	
	private ModelType modelType;
	
	public ModelListConverter(
			ModelType modelType,
			Mapper mapper,
			ReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
		
		CheckUtils.isNotNull(modelType);
		this.modelType = modelType;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return ModelList.class.isAssignableFrom(type);
	}
	
	@Override
	public Object unmarshal(
			HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		ModelBeanList modelBeanList = (ModelBeanList) super.unmarshal(
				reader,
				context);
		
		if (modelBeanList == null)
			return null;
		
		return modelBeanList.toModelList(new ModelList<M>(), this.modelType);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void marshal(
			Object source,
			HierarchicalStreamWriter writer,
			MarshallingContext context) {
		if (source == null)
			return;
		
		ModelList modelList = (ModelList) source;
		
		super.marshal(modelList.toModelBeanList(), writer, context);
	}
	
}
