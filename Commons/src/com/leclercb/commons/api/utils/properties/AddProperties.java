package com.leclercb.commons.api.utils.properties;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import com.leclercb.commons.api.properties.SortedProperties;

public class AddProperties {
	
	public static void addProperty(
			File file,
			String key,
			String value,
			boolean overwrite) throws Exception {
		SortedProperties p = new SortedProperties();
		p.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		
		if (overwrite || !p.containsKey(key)) {
			System.out.println("Property \""
					+ key
					+ "\" added to: "
					+ file.getName());
			
			p.put(key, value);
			
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			p.store(output, null);
			IOUtils.write(
					output.toString(),
					new FileOutputStream(file),
					"UTF-8");
		}
	}
	
	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);
		String key = args[1];
		String value = args[2];
		boolean overwrite = (args[3].length() > 0);
		
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
			addProperty(f, key, value, overwrite);
		}
	}
	
}
