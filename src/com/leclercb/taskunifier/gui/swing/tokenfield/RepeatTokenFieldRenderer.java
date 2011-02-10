package com.leclercb.taskunifier.gui.swing.tokenfield;

import com.leclercb.commons.gui.swing.components.tokenfield.DefaultTokenFieldRenderer;
import com.leclercb.taskunifier.gui.swing.tokenfield.repeattokens.AdvancedToken;

public class RepeatTokenFieldRenderer extends DefaultTokenFieldRenderer {
	
	@Override
	public String getText(Object[] tokenValues) {
		if (tokenValues.length == 4)
			if (tokenValues[1] instanceof AdvancedToken
					&& ((AdvancedToken) tokenValues[1]) == AdvancedToken.ON_THE)
				return super.getText(tokenValues) + " of each month";
		
		return super.getText(tokenValues);
	}
	
}
