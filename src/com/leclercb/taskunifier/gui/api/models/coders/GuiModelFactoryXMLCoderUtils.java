package com.leclercb.taskunifier.gui.api.models.coders;

import java.awt.Color;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public final class GuiModelFactoryXMLCoderUtils {
	
	private GuiModelFactoryXMLCoderUtils() {

	}
	
	static void decodeExtended(GuiModel model, Node node)
			throws FactoryCoderException {
		NodeList nModel = node.getChildNodes();
		
		Color color = null;
		
		for (int i = 0; i < nModel.getLength(); i++) {
			Node element = nModel.item(i);
			
			if (element.getNodeName().equals("color"))
				if (element.getTextContent().length() != 0)
					color = new Color(
							Integer.parseInt(element.getTextContent()));
		}
		
		model.setColor(color);
	}
	
	static void encodeExtended(
			GuiModel model,
			Document document,
			Element element) throws FactoryCoderException {
		Element color = document.createElement("color");
		color.setTextContent(model.getColor() != null ? model.getColor().getRGB()
				+ "" : "");
		element.appendChild(color);
	}
	
}
