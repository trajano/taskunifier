package com.leclercb.taskunifier.gui.components.modelnote.converters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text2HTML {
	
	public static String convert(String text) {
		if (text == null || text.length() == 0)
			return "<p style=\"margin-top: 0\"></p>";
		
		text = convertTags(text);
		text = convertNlToBr(text);
		text = convertToHtmlUrl(text);
		
		return "<p style=\"margin-top: 0\">" + text + "</p>";
	}
	
	private static String convertTags(String text) {
		StringBuffer buffer = new StringBuffer(text);
		
		int position = 0;
		
		while (true) {
			int index = buffer.indexOf("<", position);
			
			if (index == -1)
				break;
			
			String substring = buffer.substring(index);
			
			// Tags: <b>, <i>, <a>, <ul>, <ol>, <li>
			if (!(substring.startsWith("<b>")
					|| substring.startsWith("<i>")
					|| substring.startsWith("<a ")
					|| substring.startsWith("<ul>")
					|| substring.startsWith("<ol>")
					|| substring.startsWith("<li>")
					|| substring.startsWith("</b>")
					|| substring.startsWith("</i>")
					|| substring.startsWith("</a>")
					|| substring.startsWith("</ul>")
					|| substring.startsWith("</ol>") || substring.startsWith("</li>")))
				buffer.replace(index, index + 1, "&lt;");
			
			position = index + 1;
		}
		
		return buffer.toString();
	}
	
	private static String convertToHtmlUrl(String text) {
		StringBuffer buffer = new StringBuffer(text);
		
		Pattern p = Pattern.compile("(href=['\"]{1})?((https?|ftp|file):((//)|(\\\\))+[\\w\\d:#@%/;$~_?\\+\\-=\\\\.&]*)");
		Matcher m = null;
		int position = 0;
		
		while (true) {
			m = p.matcher(buffer.toString());
			
			if (!m.find(position))
				break;
			
			position = m.end();
			String firstGroup = m.group(1);
			
			if (firstGroup == null)
				firstGroup = "";
			
			if (firstGroup.contains("href"))
				continue;
			
			String url = firstGroup
					+ "<a href=\""
					+ m.group(2)
					+ "\">"
					+ m.group(2)
					+ "</a>";
			
			buffer.replace(m.start(), m.end(), url);
			
			position = m.start() + url.length() - 1;
		}
		
		return buffer.toString();
	}
	
	private static String convertNlToBr(String text) {
		StringBuffer buffer = new StringBuffer();
		
		text = text.replace("\n", "\n ");
		String[] lines = text.split("\n");
		String[] prefixes = new String[] {
				"<ul",
				"<ol",
				"<li",
				"</ul",
				"</ol",
				"</li" };
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			
			line = line.trim();
			buffer.append(line);
			if (startsWith(line, prefixes))
				if (i + 1 < lines.length
						&& startsWith(lines[i + 1].trim(), prefixes))
					continue;
			
			buffer.append("<br />");
		}
		
		return buffer.toString();
	}
	
	private static boolean startsWith(String str, String... prefix) {
		for (String p : prefix) {
			if (str.startsWith(p))
				return true;
		}
		
		return false;
	}
	
}
