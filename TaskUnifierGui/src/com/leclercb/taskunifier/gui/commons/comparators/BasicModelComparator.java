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
package com.leclercb.taskunifier.gui.commons.comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.taskunifier.api.models.BasicModel;
import com.leclercb.taskunifier.api.models.ModelParent;

public class BasicModelComparator implements Comparator<BasicModel> {
	
	public static final BasicModelComparator INSTANCE = new BasicModelComparator();
	
	private BasicModelComparator() {
		
	}
	
	@Override
	public int compare(BasicModel m1, BasicModel m2) {
		if (m1 instanceof ModelParent<?> && m2 instanceof ModelParent<?>)
			return this.compareIndented(
					(ModelParent<?>) m1,
					(ModelParent<?>) m2);
		
		return this.compareTitle(m1, m2);
	}
	
	public int compareTitle(BasicModel m1, BasicModel m2) {
		if (m1 == null && m2 == null)
			return 0;
		
		if (m1 == null)
			return 1;
		
		if (m2 == null)
			return -1;
		
		String s1 = (m1 == null ? null : m1.getTitle().toLowerCase());
		String s2 = (m2 == null ? null : m2.getTitle().toLowerCase());
		
		int result = CompareUtils.compare(s1, s2);
		
		if (result != 0)
			return result;
		
		return CompareUtils.compare(m1, m2);
	}
	
	private int compareIndented(ModelParent<?> model1, ModelParent<?> model2) {
		int result = 0;
		
		List<ModelParent<?>> parents1 = new ArrayList<ModelParent<?>>(
				model1.getAllParents());
		List<ModelParent<?>> parents2 = new ArrayList<ModelParent<?>>(
				model2.getAllParents());
		
		if (model1.getParent() == null && model2.getParent() == null) {
			// If both tasks are parents, compare them
			result = this.compareTitle(model1, model2);
		} else if (model1.getParent() != null
				&& model2.getParent() != null
				&& model1.getParent().equals(model2.getParent())) {
			// If both tasks have the same parent, compare them
			result = this.compareTitle(model1, model2);
		} else if (parents1.contains(model2)) {
			// If a task is the child of the other task
			result = 1;
		} else if (parents2.contains(model1)) {
			// If a task is the child of the other task
			result = -1;
		} else {
			// Else, compare tasks with parent
			parents1.add(0, model1);
			parents2.add(0, model2);
			
			Collections.reverse(parents1);
			Collections.reverse(parents2);
			
			int max = Math.max(parents1.size(), parents2.size());
			for (int i = 0; i < max; i++) {
				if (i < parents1.size())
					model1 = parents1.get(i);
				
				if (i < parents2.size())
					model2 = parents2.get(i);
				
				if (model1.equals(model2))
					continue;
				
				result = this.compareTitle(model1, model2);
				
				break;
			}
		}
		
		return result;
	}
	
}
