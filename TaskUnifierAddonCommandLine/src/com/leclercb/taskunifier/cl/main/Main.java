package com.leclercb.taskunifier.cl.main;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0 || args.length > 2) {
			exitAndPrintUsage();
			return;
		}
		
		String title = args[0];
		int port = 4576;
		
		if (args.length == 2) {
			if (args[1].matches("[0-9]{1,4}")) {
				port = Integer.parseInt(args[1]);
			} else {
				System.err.println("Port is invalid: [0-9]{1,4}");
				exitAndPrintUsage();
			}
		}
		
		String xml = null;
		
		try {
			xml = createXml(title);
		} catch (Exception e) {
			System.err.println("An error occured during the creation of the xml");
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			sendXml(xml, port);
		} catch (Exception e) {
			System.err.println("Please check that TaskUnifier is started");
			System.exit(1);
		}
		
		System.exit(0);
	}
	
	private static void exitAndPrintUsage() {
		System.err.println("Usage: \"quick task\" [port]");
		System.exit(1);
	}
	
	private static void sendXml(String xml, int port) throws Exception {
		Socket socket = new Socket("127.0.0.1", port);
		BufferedOutputStream stream = new BufferedOutputStream(
				socket.getOutputStream());
		stream.write(xml.getBytes());
		
		stream.close();
		socket.close();
	}
	
	private static String createXml(String title) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation implementation = builder.getDOMImplementation();
		
		Document document = implementation.createDocument(null, null, null);
		Element root = document.createElement("com");
		document.appendChild(root);
		
		Element applicationNameElement = document.createElement("applicationName");
		applicationNameElement.setTextContent("TaskUnifier");
		
		Element quickTasksElement = document.createElement("quicktasks");
		
		Element quickTaskElement = document.createElement("quicktask");
		
		Element titleElement = document.createElement("title");
		titleElement.setTextContent(title);
		
		root.appendChild(applicationNameElement);
		root.appendChild(quickTasksElement);
		quickTasksElement.appendChild(quickTaskElement);
		quickTaskElement.appendChild(titleElement);
		
		DOMSource domSource = new DOMSource(document);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount",
				"4");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		StreamResult sr = new StreamResult(output);
		transformer.transform(domSource, sr);
		
		return output.toString("UTF-8");
	}
	
}
