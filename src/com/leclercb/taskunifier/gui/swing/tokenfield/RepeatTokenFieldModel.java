package com.leclercb.taskunifier.gui.swing.tokenfield;

import com.leclercb.commons.gui.swing.components.tokenfield.AbstractTokenFieldModel;
import com.leclercb.commons.gui.swing.components.tokenfield.ComboBoxTokenType;
import com.leclercb.commons.gui.swing.components.tokenfield.TokenType;
import com.leclercb.taskunifier.gui.swing.tokenfield.repeattokens.AdvancedToken;
import com.leclercb.taskunifier.gui.swing.tokenfield.repeattokens.RepeatToken;

public class RepeatTokenFieldModel extends AbstractTokenFieldModel {
	
	private TokenType repeatTokenType;
	private TokenType advancedTokenType;
	
	public RepeatTokenFieldModel() {
		this.repeatTokenType = new ComboBoxTokenType(RepeatToken.values());
		this.advancedTokenType = new ComboBoxTokenType(AdvancedToken.values());
	}
	
	@Override
	public TokenType nextTokenType(TokenType currentToken) {
		if (currentToken == null)
			return this.repeatTokenType;
		
		if (currentToken == this.repeatTokenType)
			if (currentToken.getTokenValue() == RepeatToken.ADVANCED)
				return this.advancedTokenType;
			else
				return null;
		
		return null;
	}
	
}
