package com.leclercb.commons.api.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.leclercb.commons.api.properties.SortedProperties;

public class AddProperties {
	
	public static void addProperty(File file, String key, String value)
			throws Exception {
		SortedProperties p = new SortedProperties();
		p.load(new FileInputStream(file));
		
		if (!p.contains(key)) {
			System.out.println("Property \""
					+ key
					+ "\" added to: "
					+ file.getName());
			
			p.put(key, value);
			p.store(new FileOutputStream(file), null);
		}
	}
	
	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);
		String key = args[1];
		String value = args[2];
		
		if (!file.exists() || !file.isFile())
			throw new IllegalArgumentException();
		
		final String bundle = file.getName().substring(
				0,
				(file.getName().indexOf('_') == -1 ? file.getName().indexOf('.') : file.getName().indexOf(
						'_')));
		
		File[] files = file.getParentFile().listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				return file.getName().startsWith(bundle);
			}
			
		});
		
		for (File f : files) {
			addProperty(f, key, value);
		}
	}
	
}
