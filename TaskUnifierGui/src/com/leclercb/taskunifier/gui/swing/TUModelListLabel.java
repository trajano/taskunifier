package com.leclercb.taskunifier.gui.swing;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelList;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;

public class TUModelListLabel extends JPanel {
	
	private ModelList<?> modelList;
	
	public TUModelListLabel() {
		this(null);
	}
	
	public TUModelListLabel(ModelList<?> modelList) {
		this.initialize();
		this.setModelList(modelList);
	}
	
	private void initialize() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		this.setBackground(Color.WHITE);
	}
	
	public ModelList<?> getModelList() {
		return this.modelList;
	}
	
	public void setModelList(ModelList<?> modelList) {
		this.modelList = modelList;
		
		this.removeAll();
		
		if (modelList != null) {
			for (Model model : modelList) {
				JLabel label = new JLabel(
						StringValueModel.INSTANCE.getString(model));
				
				if (model instanceof GuiModel)
					label.setIcon(IconValueModel.INSTANCE.getIcon(model));
				
				this.add(label);
			}
		}
		
		this.revalidate();
	}
	
}
