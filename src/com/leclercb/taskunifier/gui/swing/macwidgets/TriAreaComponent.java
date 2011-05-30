package com.leclercb.taskunifier.gui.swing.macwidgets;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.explodingpixels.macwidgets.MacWidgetFactory;
import com.explodingpixels.painter.Painter;
import com.explodingpixels.swingx.EPPanel;
import com.explodingpixels.widgets.WindowDragger;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.leclercb.taskunifier.gui.utils.review.NoReview;

/**
 * A component that has three areas in which it widgets can be added.
 */
@NoReview
public class TriAreaComponent {
	
	private PanelBuilder fLeftPanelBuilder = new PanelBuilder(new FormLayout(
			"",
			"fill:p:grow"), new JPanel());
	private PanelBuilder fCenterPanelBuilder = new PanelBuilder(new FormLayout(
			"",
			"fill:p:grow"), new JPanel());
	private PanelBuilder fRightPanelBuilder = new PanelBuilder(new FormLayout(
			"",
			"fill:p:grow"), new JPanel());
	
	private final EPPanel fPanel = new EPPanel();
	
	private int fSpacer_pixels;
	
	/**
	 * Creates a {@code TriAreaComponent} that uses a padding of 0 pixels
	 * between components.
	 */
	public TriAreaComponent() {
		this(0);
	}
	
	/**
	 * Creates a {@code TriAreaComponent} that uses the given padding between
	 * components.
	 * 
	 * @param spacer_pixels
	 *            the space in pixels to add between components.
	 */
	public TriAreaComponent(int spacer_pixels) {
		this.fSpacer_pixels = spacer_pixels;
		
		// define the FormLayout columns and rows.
		FormLayout layout = new FormLayout(
				"left:p:grow, center:p:grow, right:p:grow",
				"fill:p:grow");
		// layout.setColumnGroups(new int[][]{{1,2,3}});
		// create the cell constraints to use in the layout.
		CellConstraints cc = new CellConstraints();
		// create the builder with our panel as the component to be filled.
		PanelBuilder builder = new PanelBuilder(layout, this.fPanel);
		
		builder.add(this.fLeftPanelBuilder.getPanel(), cc.xy(1, 1));
		builder.add(this.fCenterPanelBuilder.getPanel(), cc.xy(2, 1));
		builder.add(this.fRightPanelBuilder.getPanel(), cc.xy(3, 1));
		
		this.fLeftPanelBuilder.getPanel().setOpaque(false);
		this.fCenterPanelBuilder.getPanel().setOpaque(false);
		this.fRightPanelBuilder.getPanel().setOpaque(false);
		
		// fLeftPanelBuilder.getPanel().setBorder(BorderFactory.createLineBorder(Color.RED));
		// fCenterPanelBuilder.getPanel().setBorder(BorderFactory.createLineBorder(Color.BLUE));
		// fRightPanelBuilder.getPanel().setBorder(BorderFactory.createLineBorder(Color.GREEN));
	}
	
	/**
	 * Forces each of the areas (left, center and right) to have the same widths
	 * regardless of the
	 * size of the items each of the area contains.
	 */
	void forceAreasToHaveTheSameWidth() {
		((FormLayout) this.fPanel.getLayout()).setColumnGroups(new int[][] { {
				1,
				2,
				3 } });
	}
	
	/**
	 * Gets the user interface component representing this {@code SourceList}.
	 * The returned {@link JComponent} should be added to a container that will
	 * be displayed.
	 * 
	 * @return the user interface component representing this {@code SourceList}
	 *         .
	 */
	public JComponent getComponent() {
		return this.fPanel;
	}
	
	/**
	 * Installs a {@link WindowDragger} on the given {@link Window}.
	 * 
	 * @param window
	 *            the {@code Window} to install the {@code WindowDragger} on.
	 */
	public void installWindowDraggerOnWindow(Window window) {
		new WindowDragger(window, this.getComponent());
	}
	
	/**
	 * Adds the given component to the left side of this
	 * {@code TriAreaComponent}.
	 * 
	 * @param toolToAdd
	 *            the tool to add to this {@code TriAreaComponent}.
	 */
	public void addComponentToLeft(JComponent toolToAdd) {
		this.addComponentToLeft(toolToAdd, this.fSpacer_pixels);
	}
	
	/**
	 * Adds the given component to the left side of this
	 * {@code TriAreaComponent} followed by the
	 * given an empty space of the given pixel width.
	 * 
	 * @param toolToAdd
	 *            the tool to add to this {@code TriAreaComponent}.
	 * @param spacer_pixels
	 *            the amount of space to post-pend the added component with.
	 */
	public void addComponentToLeft(JComponent toolToAdd, int spacer_pixels) {
		this.fLeftPanelBuilder.appendColumn("p");
		this.fLeftPanelBuilder.add(toolToAdd);
		this.fLeftPanelBuilder.nextColumn();
		this.fLeftPanelBuilder.appendColumn("p");
		this.fLeftPanelBuilder.add(MacWidgetFactory.createSpacer(
				spacer_pixels,
				0));
		this.fLeftPanelBuilder.nextColumn();
	}
	
	/**
	 * Adds the given component to the center of this {@code TriAreaComponent}.
	 * 
	 * @param toolToAdd
	 *            the tool to add to this {@code TriAreaComponent}.
	 */
	public void addComponentToCenter(JComponent toolToAdd) {
		this.addComponentToCenter(toolToAdd, this.fSpacer_pixels);
	}
	
	/**
	 * Adds the given component to the center of this {@code TriAreaComponent}.
	 * If this is not the
	 * first component to be added to the center, then the given component will
	 * be preceeded by a
	 * space of the given width.
	 * 
	 * @param toolToAdd
	 *            the tool to add to this {@code TriAreaComponent}.
	 * @param spacer_pixels
	 *            the amount of space to pre-pend the added component with *if*
	 *            the given
	 *            component is *not* the first component to be added to the
	 *            center.
	 */
	public void addComponentToCenter(JComponent toolToAdd, int spacer_pixels) {
		if (this.getCenterComponentCount() > 0) {
			this.fCenterPanelBuilder.appendColumn("p");
			this.fCenterPanelBuilder.add(MacWidgetFactory.createSpacer(
					spacer_pixels,
					0));
			this.fCenterPanelBuilder.nextColumn();
		}
		
		this.fCenterPanelBuilder.appendColumn("p");
		this.fCenterPanelBuilder.add(toolToAdd);
		this.fCenterPanelBuilder.nextColumn();
	}
	
	/**
	 * Adds the given component to the right side of this
	 * {@code TriAreaComponent}.
	 * 
	 * @param toolToAdd
	 *            the tool to add to this {@code TriAreaComponent}.
	 */
	public void addComponentToRight(JComponent toolToAdd) {
		this.addComponentToRight(toolToAdd, this.fSpacer_pixels);
	}
	
	/**
	 * Adds the given component to the right side of this
	 * {@code TriAreaComponent}. If this is not
	 * the first component to be added to the right, then the given component
	 * will be followed by a
	 * space of the given width.
	 * 
	 * @param toolToAdd
	 *            the tool to add to this {@code TriAreaComponent}.
	 * @param spacer_pixels
	 *            the amount of space to post-pend the added component with *if*
	 *            the given
	 *            component is *not* the first component to be added to the
	 *            center.
	 */
	public void addComponentToRight(JComponent toolToAdd, int spacer_pixels) {
		// first, add a spacer if there is already an component on the right.
		if (this.getRightComponentCount() > 0) {
			this.fRightPanelBuilder.appendColumn("p");
			this.fRightPanelBuilder.add(MacWidgetFactory.createSpacer(
					spacer_pixels,
					0));
			this.fRightPanelBuilder.nextColumn();
		}
		
		// next, add the given component.
		this.fRightPanelBuilder.appendColumn("p");
		this.fRightPanelBuilder.add(toolToAdd);
		this.fRightPanelBuilder.nextColumn();
	}
	
	/**
	 * Set's the background {@link Painter} that this {@code TriAreaComponent}
	 * uses.
	 * 
	 * @param backgroundPainter
	 *            the background {@link Painter} that this
	 *            {@code TriAreaComponent} uses.
	 */
	public void setBackgroundPainter(Painter<Component> backgroundPainter) {
		this.fPanel.setBackgroundPainter(backgroundPainter);
	}
	
	protected final int getLeftComponentCount() {
		return this.fLeftPanelBuilder.getPanel().getComponentCount();
	}
	
	protected final int getCenterComponentCount() {
		return this.fCenterPanelBuilder.getPanel().getComponentCount();
	}
	
	protected final int getRightComponentCount() {
		return this.fRightPanelBuilder.getPanel().getComponentCount();
	}
}
