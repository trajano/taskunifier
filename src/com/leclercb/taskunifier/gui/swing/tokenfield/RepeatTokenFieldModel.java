package com.leclercb.taskunifier.gui.swing.tokenfield;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.leclercb.commons.api.utils.ArrayUtils;
import com.leclercb.commons.gui.swing.components.JTokenField;
import com.leclercb.commons.gui.swing.components.tokenfield.AbstractTokenFieldModel;
import com.leclercb.commons.gui.swing.components.tokenfield.ComboBoxTokenType;
import com.leclercb.commons.gui.swing.components.tokenfield.TokenType;
import com.leclercb.taskunifier.gui.swing.tokenfield.repeattokens.AdvancedToken;
import com.leclercb.taskunifier.gui.swing.tokenfield.repeattokens.DayToken;
import com.leclercb.taskunifier.gui.swing.tokenfield.repeattokens.NumericalWordToken;
import com.leclercb.taskunifier.gui.swing.tokenfield.repeattokens.RepeatToken;
import com.leclercb.taskunifier.gui.swing.tokenfield.repeattokens.SpecialDayToken;
import com.leclercb.taskunifier.gui.swing.tokenfield.repeattokens.TimeToken;

public class RepeatTokenFieldModel extends AbstractTokenFieldModel {
	
	private TokenType advancedTokenType;
	private TokenType dayTokenType;
	private TokenType numericalWordTokenType;
	private TokenType repeatTokenType;
	private TokenType specialDay100TokenType;
	private TokenType timeTokenType;
	
	public RepeatTokenFieldModel() {
		Integer[] _100 = new Integer[100];
		for (int i = 0; i < _100.length; i++)
			_100[i] = i + 1;
		
		this.advancedTokenType = new ComboBoxTokenType(AdvancedToken.values());
		this.dayTokenType = new ComboBoxTokenType(DayToken.values());
		this.numericalWordTokenType = new ComboBoxTokenType(
				NumericalWordToken.values());
		this.repeatTokenType = new ComboBoxTokenType(RepeatToken.values());
		this.specialDay100TokenType = new ComboBoxTokenType(
				ArrayUtils.concatObjectArrays(SpecialDayToken.values(), _100));
		this.timeTokenType = new ComboBoxTokenType(TimeToken.values());
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
		
		if (currentToken == this.advancedTokenType)
			if (currentToken.getTokenValue() == AdvancedToken.EVERY)
				return this.specialDay100TokenType;
			else if (currentToken.getTokenValue() == AdvancedToken.ON_THE)
				return this.numericalWordTokenType;
		
		if (currentToken == this.specialDay100TokenType)
			if (this.specialDay100TokenType.getTokenValue() instanceof Integer)
				return this.timeTokenType;
			else
				return null;
		
		if (currentToken == this.numericalWordTokenType)
			return this.dayTokenType;
		
		return null;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("test");
		frame.setSize(640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLayout(new BorderLayout());
		
		JTokenField tokenField = new JTokenField(new RepeatTokenFieldModel());
		tokenField.setRenderer(new RepeatTokenFieldRenderer());
		frame.add(tokenField);
		
		frame.setVisible(true);
	}
	
}
