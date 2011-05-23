package com.leclercb.taskunifier.gui.utils.review;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

@Reviewed
public final class ReviewScanner {
	
	private ReviewScanner() {
		
	}
	
	public static void main(String[] args) {
		File folder = new File("src");
		
		ArrayList<File> files = new ArrayList<File>();
		FileUtils.listFiles(folder, new String[] { "java" }, true);
		
		System.out.println("To Review :");
		System.out.println("-----------");
		
		for (File file : files) {
			try {
				String path = file.getCanonicalPath();
				path = path.substring(folder.getCanonicalPath().length());
				path = toReference(path);
				
				Class<?> cls = Class.forName(path, false, Thread.currentThread().getContextClassLoader());
				
				if (cls.equals(Reviewed.class) || cls.equals(NoReview.class))
					continue;
				
				if (cls.getAnnotation(Reviewed.class) == null && 
						cls.getAnnotation(NoReview.class) == null)
					System.out.println(path);
			} catch (Exception e) {}
		}
	}
	
	private static String toReference(String path) {
		if (path.charAt(0) == File.separatorChar)
			path = path.substring(1);
		
		path = path.substring(0, path.length() - 5);
		path = path.replace(File.separatorChar, '.');
		
		return path;
	}
	
}

