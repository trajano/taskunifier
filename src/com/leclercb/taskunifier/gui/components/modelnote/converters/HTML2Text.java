package com.leclercb.taskunifier.gui.components.modelnote.converters;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import com.leclercb.commons.gui.logger.GuiLogger;

public class HTML2Text extends HTMLEditorKit.ParserCallback {
	
	private List<HTML.Tag> keepTags;
	private StringBuffer stringBuffer;
	
	private HTML2Text() {
		this.keepTags = new ArrayList<HTML.Tag>();
		this.keepTags.add(HTML.Tag.OL);
		this.keepTags.add(HTML.Tag.UL);
		this.keepTags.add(HTML.Tag.LI);
		this.keepTags.add(HTML.Tag.B);
		this.keepTags.add(HTML.Tag.I);
		this.keepTags.add(HTML.Tag.U);
		
		this.stringBuffer = new StringBuffer();
	}
	
	public void parse(Reader in) throws IOException {
		ParserDelegator delegator = new ParserDelegator();
		delegator.parse(in, this, Boolean.TRUE);
	}
	
	@Override
	public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
		if (this.keepTags.contains(t))
			this.stringBuffer.append("<" + t + ">");
	}
	
	@Override
	public void handleEndTag(HTML.Tag t, int pos) {
		if (this.keepTags.contains(t))
			this.stringBuffer.append("</" + t + ">");
	}
	
	@Override
	public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
		if (t.equals(HTML.Tag.BR) || t.equals(HTML.Tag.P))
			this.stringBuffer.append("\n");
	}
	
	@Override
	public void handleText(char[] text, int pos) {
		this.stringBuffer.append(text);
	}
	
	public String getText() {
		return this.stringBuffer.toString();
	}
	
	public static String convert(String html) {
		HTML2Text parser = new HTML2Text();
		Reader in = new StringReader(html);
		
		try {
			parser.parse(in);
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.WARNING,
					"Error while parsing html to text",
					e);
		} finally {
			try {
				in.close();
			} catch (IOException ioe) {
				
			}
		}
		
		return parser.getText();
	}
	
}
