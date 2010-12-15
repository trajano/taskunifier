package com.leclercb.taskunifier.gui.utils;

/*
 *	This programs uses the information found in the UIManager
 *  to create a table of key/value pairs for each Swing component.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class UIManagerDefaults implements ActionListener, ItemListener {

	private final static String[] COLUMN_NAMES = { "Key", "Value", "Sample" };
	private static String selectedItem;

	private JComponent contentPane;
	private JMenuBar menuBar;
	private JComboBox comboBox;
	private JRadioButton byComponent;
	private JTable table;
	private TreeMap<String, TreeMap<String, Object>> items;
	private HashMap<String, DefaultTableModel> models;

	/*
	 * Constructor
	 */
	public UIManagerDefaults() {
		this.items = new TreeMap<String, TreeMap<String, Object>>();
		this.models = new HashMap<String, DefaultTableModel>();

		this.contentPane = new JPanel(new BorderLayout());
		this.contentPane.add(this.buildNorthComponent(), BorderLayout.NORTH);
		this.contentPane.add(this.buildCenterComponent(), BorderLayout.CENTER);

		this.resetComponents();
	}

	/*
	 * The content pane should be added to a high level container
	 */
	public JComponent getContentPane() {
		return this.contentPane;
	}

	/*
	 * A menu can also be added which provides the ability to switch between
	 * different LAF's.
	 */
	public JMenuBar getMenuBar() {
		if (this.menuBar == null)
			this.menuBar = this.createMenuBar();

		return this.menuBar;
	}

	/*
	 * This panel is added to the North of the content pane
	 */
	private JComponent buildNorthComponent() {
		this.comboBox = new JComboBox();

		JLabel label = new JLabel("Select Item:");
		label.setDisplayedMnemonic('S');
		label.setLabelFor(this.comboBox);

		this.byComponent = new JRadioButton("By Component", true);
		this.byComponent.setMnemonic('C');
		this.byComponent.addActionListener(this);

		JRadioButton byValueType = new JRadioButton("By Value Type");
		byValueType.setMnemonic('V');
		byValueType.addActionListener(this);

		ButtonGroup group = new ButtonGroup();
		group.add(this.byComponent);
		group.add(byValueType);

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(15, 0, 15, 0));
		panel.add(label);
		panel.add(this.comboBox);
		panel.add(this.byComponent);
		panel.add(byValueType);
		return panel;
	}

	/*
	 * This panel is added to the Center of the content pane
	 */
	private JComponent buildCenterComponent() {
		DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0);
		this.table = new JTable(model);
		this.table.setAutoCreateColumnsFromModel(false);
		this.table.getColumnModel().getColumn(0).setPreferredWidth(250);
		this.table.getColumnModel().getColumn(1).setPreferredWidth(500);
		this.table.getColumnModel().getColumn(2).setPreferredWidth(100);
		this.table.getColumnModel().getColumn(2).setCellRenderer(new SampleRenderer());
		Dimension d = this.table.getPreferredSize();
		d.height = 350;
		this.table.setPreferredScrollableViewportSize(d);

		return new JScrollPane(this.table);
	}

	/*
	 * When the LAF is changed we need to reset the content pane
	 */
	public void resetComponents() {
		this.items.clear();
		this.models.clear();
		((DefaultTableModel) this.table.getModel()).setRowCount(0);

		this.buildItemsMap();

		Vector<String> comboBoxItems = new Vector<String>(50);
		Iterator keys = this.items.keySet().iterator();

		while (keys.hasNext()) {
			Object key = keys.next();
			comboBoxItems.add((String) key);
		}

		this.comboBox.removeItemListener(this);
		this.comboBox.setModel(new DefaultComboBoxModel(comboBoxItems));
		this.comboBox.setSelectedIndex(-1);
		this.comboBox.addItemListener(this);
		this.comboBox.requestFocusInWindow();

		if (selectedItem != null)
			this.comboBox.setSelectedItem(selectedItem);
	}

	/*
	 * The item map will contain items for each component or items for each
	 * attribute type.
	 */
	private TreeMap buildItemsMap() {
		UIDefaults defaults = UIManager.getLookAndFeelDefaults();

		// Build of Map of items and a Map of attributes for each item

		for (Enumeration enumm = defaults.keys(); enumm.hasMoreElements();) {
			Object key = enumm.nextElement();
			Object value = defaults.get(key);

			String itemName = this.getItemName(key.toString(), value);

			if (itemName == null)
				continue;

			// Get the attribute map for this componenent, or
			// create a map when one is not found

			TreeMap<String, Object> attributeMap = this.items.get(itemName);

			if (attributeMap == null) {
				attributeMap = new TreeMap<String, Object>();
				this.items.put(itemName, attributeMap);
			}

			// Add the attribute to the map for this componenent

			attributeMap.put(key.toString(), value);
		}

		return this.items;
	}

	/*
	 * Parse the key to determine the item name to use
	 */
	private String getItemName(String key, Object value) {
		// Seems like this is an old check required for JDK1.4.2

		if (key.startsWith("class") || key.startsWith("javax"))
			return null;

		if (this.byComponent.isSelected())
			return this.getComponentName(key, value);
		else
			return this.getValueName(key, value);
	}

	private String getComponentName(String key, Object value) {
		// The key is of the form:
		// "componentName.componentProperty", or
		// "componentNameUI", or
		// "someOtherString"

		String componentName;

		int pos = key.indexOf(".");

		if (pos != -1) {
			componentName = key.substring(0, pos);
		} else if (key.endsWith("UI")) {
			componentName = key.substring(0, key.length() - 2);
		} else if (value instanceof ColorUIResource) {
			componentName = "System Colors";
		} else {
			componentName = "Miscellaneous";
		}

		// Fix inconsistency

		if (componentName.equals("Checkbox"))
			componentName = "CheckBox";

		return componentName;
	}

	private String getValueName(String key, Object value) {
		if (value instanceof Icon)
			return "Icon";
		else if (value instanceof Font)
			return "Font";
		else if (value instanceof Border)
			return "Border";
		else if (value instanceof Color)
			return "Color";
		else if (value instanceof Insets)
			return "Insets";
		else if (value instanceof Boolean)
			return "Boolean";
		else if (value instanceof Dimension)
			return "Dimension";
		else if (value instanceof Number)
			return "Number";
		else if (key.endsWith("UI"))
			return "UI";
		else if (key.endsWith("InputMap"))
			return "InputMap";
		else if (key.endsWith("RightToLeft"))
			return "InputMap";
		else if (key.endsWith("radient"))
			return "Gradient";
		else {
			return "The Rest";
		}
	}

	/**
	 * Create menu bar
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		menuBar.add(this.createFileMenu());
		menuBar.add(this.createLAFMenu());

		return menuBar;
	}

	/**
	 * Create menu items for the Application menu
	 */
	private JMenu createFileMenu() {
		JMenu menu = new JMenu("Application");
		menu.setMnemonic('A');

		menu.addSeparator();
		menu.add(new ExitAction());

		return menu;
	}

	/**
	 * Create menu items for the Look & Feel menu
	 */
	private JMenu createLAFMenu() {
		ButtonGroup bg = new ButtonGroup();

		JMenu menu = new JMenu("Look & Feel");
		menu.setMnemonic('L');

		String lafId = UIManager.getLookAndFeel().getID();
		UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();

		for (int i = 0; i < lafInfo.length; i++) {
			String laf = lafInfo[i].getClassName();
			String name = lafInfo[i].getName();

			Action action = new ChangeLookAndFeelAction(this, laf, name);
			JRadioButtonMenuItem mi = new JRadioButtonMenuItem(action);
			menu.add(mi);
			bg.add(mi);

			if (name.equals(lafId)) {
				mi.setSelected(true);
			}
		}

		return menu;
	}

	/*
	 * Implement the ActionListener interface
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		selectedItem = null;
		this.resetComponents();
		this.comboBox.requestFocusInWindow();
	}

	/*
	 * Implement the ItemListener interface
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		String itemName = (String) e.getItem();
		this.changeTableModel(itemName);
		this.updateRowHeights();
		selectedItem = itemName;
	}

	/*
	 * Change the TabelModel in the table for the selected item
	 */
	private void changeTableModel(String itemName) {
		// The model has been created previously so just use it

		DefaultTableModel model = this.models.get(itemName);

		if (model != null) {
			this.table.setModel(model);
			return;
		}

		// Create a new model for the requested item
		// and add the attributes of the item to the model

		model = new DefaultTableModel(COLUMN_NAMES, 0);
		Map attributes = this.items.get(itemName);

		Iterator ai = attributes.keySet().iterator();

		while (ai.hasNext()) {
			String attribute = (String) ai.next();
			Object value = attributes.get(attribute);

			Vector<Object> row = new Vector<Object>(3);
			row.add(attribute);

			if (value != null) {
				row.add(value.toString());

				if (value instanceof Icon)
					value = new SafeIcon((Icon) value);

				row.add(value);
			} else {
				row.add("null");
				row.add("");
			}

			model.addRow(row);
		}

		this.table.setModel(model);
		this.models.put(itemName, model);
	}

	/*
	 * Some rows containing icons, may need to be sized taller to fully display
	 * the icon.
	 */
	private void updateRowHeights() {
		try {
			for (int row = 0; row < this.table.getRowCount(); row++) {
				int rowHeight = this.table.getRowHeight();

				for (int column = 0; column < this.table.getColumnCount(); column++) {
					Component comp = this.table.prepareRenderer(this.table.getCellRenderer(row, column), row, column);
					rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
				}

				this.table.setRowHeight(row, rowHeight);
			}
		} catch (ClassCastException e) {
		}
	}

	/**
	 * Thanks to Jeanette for the use of this code found at:
	 * 
	 * https://jdnc-incubator.dev.java.net/source/browse/jdnc-incubator/src/
	 * kleopatra
	 * /java/org/jdesktop/swingx/renderer/UIPropertiesViewer.java?rev=1.2
	 * &view=markup
	 * 
	 * Some ui-icons misbehave in that they unconditionally class-cast to the
	 * component type they are mostly painted on. Consequently they blow up if
	 * we are trying to paint them anywhere else (f.i. in a renderer).
	 * 
	 * This Icon is an adaption of a cool trick by Darryl Burke found at
	 * http://tips4java.wordpress.com/2008/12/18/icon-table-cell-renderer
	 * 
	 * The base idea is to instantiate a component of the type expected by the
	 * icon, let it paint into the graphics of a bufferedImage and create an
	 * ImageIcon from it. In subsequent calls the ImageIcon is used.
	 * 
	 */
	public static class SafeIcon implements Icon {

		private Icon wrappee;
		private Icon standIn;

		public SafeIcon(Icon wrappee) {
			this.wrappee = wrappee;
		}

		@Override
		public int getIconHeight() {
			return this.wrappee.getIconHeight();
		}

		@Override
		public int getIconWidth() {
			return this.wrappee.getIconWidth();
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			if (this.standIn == this) {
				this.paintFallback(c, g, x, y);
			} else if (this.standIn != null) {
				this.standIn.paintIcon(c, g, x, y);
			} else {
				try {
					this.wrappee.paintIcon(c, g, x, y);
				} catch (ClassCastException e) {
					this.createStandIn(e, x, y);
					this.standIn.paintIcon(c, g, x, y);
				}
			}
		}

		/**
		 * @param e
		 */
		private void createStandIn(ClassCastException e, int x, int y) {
			try {
				Class<?> clazz = this.getClass(e);
				JComponent standInComponent = this.getSubstitute(clazz);
				this.standIn = this.createImageIcon(standInComponent, x, y);
			} catch (Exception e1) {
				// something went wrong - fallback to this painting
				this.standIn = this;
			}
		}

		private Icon createImageIcon(JComponent standInComponent, int x, int y) {
			BufferedImage image = new BufferedImage(this.getIconWidth(), this.getIconHeight(),
					BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.createGraphics();
			try {
				this.wrappee.paintIcon(standInComponent, g, 0, 0);
				return new ImageIcon(image);
			} finally {
				g.dispose();
			}
		}

		/**
		 * @param clazz
		 * @throws IllegalAccessException
		 */
		private JComponent getSubstitute(Class<?> clazz) throws IllegalAccessException {
			JComponent standInComponent;

			try {
				standInComponent = (JComponent) clazz.newInstance();
			} catch (InstantiationException e) {
				standInComponent = new AbstractButton() {
				};
				((AbstractButton) standInComponent).setModel(new DefaultButtonModel());
			}
			return standInComponent;
		}

		private Class<?> getClass(ClassCastException e) throws ClassNotFoundException {
			String className = e.getMessage();
			className = className.substring(className.lastIndexOf(" ") + 1);
			return Class.forName(className);

		}

		private void paintFallback(Component c, Graphics g, int x, int y) {
			g.drawRect(x, y, this.getIconWidth(), this.getIconHeight());
			g.drawLine(x, y, x + this.getIconWidth(), y + this.getIconHeight());
			g.drawLine(x + this.getIconWidth(), y, x, y + this.getIconHeight());
		}

	}

	/*
	 * Render the value based on its class.
	 */
	class SampleRenderer extends JLabel implements TableCellRenderer {

		public SampleRenderer() {
			super();
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object sample, boolean isSelected,
				boolean hasFocus, int row, int column) {
			this.setBackground(null);
			this.setBorder(null);
			this.setIcon(null);
			this.setText("");

			if (sample instanceof Color) {
				this.setBackground((Color) sample);
			} else if (sample instanceof Border) {
				this.setBorder((Border) sample);
			} else if (sample instanceof Font) {
				this.setText("Sample");
				this.setFont((Font) sample);
			} else if (sample instanceof Icon) {
				this.setIcon((Icon) sample);
			}

			return this;
		}

		/*
		 * Some icons are painted using inner classes and are not meant to be
		 * shared by other items. This code will catch the ClassCastException
		 * that is thrown.
		 */
		@Override
		public void paint(Graphics g) {
			try {
				super.paint(g);
			} catch (Exception e) {
				// System.out.println(e);
				// System.out.println(e.getStackTrace()[0]);
			}
		}
	}

	/*
	 * Change the LAF and recreate the UIManagerDefaults so that the properties
	 * of the new LAF are correctly displayed.
	 */
	class ChangeLookAndFeelAction extends AbstractAction {

		private UIManagerDefaults defaults;
		private String laf;

		protected ChangeLookAndFeelAction(UIManagerDefaults defaults, String laf, String name) {
			this.defaults = defaults;
			this.laf = laf;
			this.putValue(Action.NAME, name);
			this.putValue(Action.SHORT_DESCRIPTION, this.getValue(Action.NAME));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				UIManager.setLookAndFeel(this.laf);
				this.defaults.resetComponents();

				JMenuItem mi = (JMenuItem) e.getSource();
				JPopupMenu popup = (JPopupMenu) mi.getParent();
				JRootPane rootPane = SwingUtilities.getRootPane(popup.getInvoker());
				SwingUtilities.updateComponentTreeUI(rootPane);

				// Use custom decorations when supported by the LAF

				JFrame frame = (JFrame) SwingUtilities.windowForComponent(rootPane);
				frame.dispose();

				if (UIManager.getLookAndFeel().getSupportsWindowDecorations()) {
					frame.setUndecorated(true);
					frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
				} else {
					frame.setUndecorated(false);
				}

				frame.setVisible(true);
			} catch (Exception ex) {
				System.out.println("Failed loading L&F: " + this.laf);
				System.out.println(ex);
			}
		}
	}

	/*
	 * Close the frame
	 */
	class ExitAction extends AbstractAction {

		public ExitAction() {
			this.putValue(Action.NAME, "Exit");
			this.putValue(Action.SHORT_DESCRIPTION, this.getValue(Action.NAME));
			this.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	/*
	 * Build a GUI using the content pane and menu bar of UIManagerDefaults
	 */
	private static void createAndShowGUI() {
		UIManagerDefaults application = new UIManagerDefaults();

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("UIManager Defaults");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(application.getMenuBar());
		frame.getContentPane().add(application.getContentPane());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/*
	 * UIManagerDefaults Main. Called only if we're an application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
