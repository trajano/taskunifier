package com.leclercb.taskunifier.gui.swing;

import javax.swing.SpinnerNumberModel;

public class SpinnerTimeModel extends SpinnerNumberModel {
	
	public SpinnerTimeModel() {
		this(0);
	}
	
	public SpinnerTimeModel(int value) {
		super(value, 0, 5760, 1);
	}
	
}
