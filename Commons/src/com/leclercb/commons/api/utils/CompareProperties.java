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
package com.leclercb.commons.api.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.leclercb.commons.api.properties.SortedProperties;

public final class CompareProperties {
	
	private CompareProperties() {
		
	}
	
	public static void showDifferences(
			Properties p1,
			Properties p2,
			boolean edit) {
		List<String> p1Keys = new ArrayList<String>();
		List<String> p2Keys = new ArrayList<String>();
		
		List<String> p1KeysClone = new ArrayList<String>();
		List<String> p2KeysClone = new ArrayList<String>();
		
		for (Object key : p1.keySet()) {
			p1Keys.add(key.toString());
			p1KeysClone.add(key.toString());
		}
		
		for (Object key : p2.keySet()) {
			p2Keys.add(key.toString());
			p2KeysClone.add(key.toString());
		}
		
		p1Keys.removeAll(p2KeysClone);
		p2Keys.removeAll(p1KeysClone);
		
		if (p2Keys.size() != 0) {
			System.out.println("P1 doesn't contain the following P2 keys:");
			
			for (String key : p2Keys) {
				if (!edit) {
					System.out.println("\t" + key);
					continue;
				}
				
				System.out.println("\tREMOVED: " + key);
				p2.remove(key);
			}
		}
		
		if (p1Keys.size() != 0) {
			System.out.println("P2 doesn't contain the following P1 keys:");
			
			for (String key : p1Keys) {
				if (!edit) {
					System.out.println("\t" + key);
					continue;
				}
				
				System.out.println("\tADDED: " + key);
				p2.put(key, p1.get(key));
			}
		}
		
		if (p1Keys.size() == 0 && p2Keys.size() == 0)
			System.out.println("Files contain the same keys");
	}
	
	public static void main(String[] args) throws Exception {
		SortedProperties p1 = new SortedProperties();
		SortedProperties p2 = new SortedProperties();
		boolean edit = false;
		
		p1.load(new FileInputStream(args[0]));
		p2.load(new FileInputStream(args[1]));
		edit = (args[2].length() > 0);
		
		showDifferences(p1, p2, edit);
		
		if (edit)
			p2.store(new FileOutputStream(args[1]), null);
	}
	
}
