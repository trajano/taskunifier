package com.leclercb.taskunifier.gui.swing.macwidgets;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.explodingpixels.macwidgets.IAppWidgetFactory;
import com.explodingpixels.macwidgets.MacWidgetFactory;
import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListClickListener;
import com.explodingpixels.macwidgets.SourceListColorScheme;
import com.explodingpixels.macwidgets.SourceListContextMenuProvider;
import com.explodingpixels.macwidgets.SourceListItem;
import com.explodingpixels.macwidgets.SourceListModel;
import com.explodingpixels.macwidgets.SourceListModelListener;
import com.explodingpixels.macwidgets.SourceListSelectionListener;
import com.explodingpixels.macwidgets.plaf.SourceListTreeUI;
import com.explodingpixels.widgets.TreeUtils;

/**
 * An implementation of an OS X Source List. For a full descrption of what a
 * Source List is, see the
 * <a href=
 * "http://developer.apple.com/documentation/UserExperience/Conceptual/AppleHIGuidelines/XHIGWindows/chapter_18_section_4.html#//apple_ref/doc/uid/20000961-CHDDIGDE"
 * >Source Lists</a>
 * section of Apple's Human Interface Guidelines.
 * <p/>
 * This component provides the two basic sytles of Source List: focusble and
 * non-focusable. As the name implies, focusable Source Lists and recieve
 * keyboard focus, and thus can be navigated using the arrow keys.
 * Non-focusable, cannot receive keyboard focus, and thus cannot be navigated
 * via the arrow keys. The two styles of {@code SourceList} are pictured below:
 * <br>
 * <table>
 * <tr>
 * <td align="center"><img src="../../../../graphics/iTunesSourceList.png"></td>
 * <td align="center"><img src="../../../../graphics/MailSourceList.png"></td>
 * </tr>
 * <tr>
 * <td align="center"><font size="2" face="arial"><b>Focusable
 * SourceList<b></font></td>
 * <td align="center"><font size="2" face="arial"><b>Non-focusable
 * SourceList<b></font></td>
 * </tr>
 * </table>
 * <br>
 * Here's how to create a simple {@code SourceList} with one item:
 * 
 * <pre>
 * SourceListModel model = new SourceListModel();
 * SourceListCategory category = new SourceListCategory(&quot;Category&quot;);
 * model.addCategory(category);
 * model.addItemToCategory(new SourceListItem(&quot;Item&quot;), category);
 * SourceList sourceList = new SourceList(model);
 * </pre>
 * <p>
 * To install a selection listener on the {@code SourceList}, add a
 * {@link SourceListSelectionListener}.
 * </p>
 * <p/>
 * To install a context-menu provider, call
 * {@link #setSourceListContextMenuProvider(SourceListContextMenuProvider)} with
 * an implementation of {@link SourceListContextMenuProvider}.
 */
public class SourceList {
	
	private final SourceListModel fModel;
	
	private final SourceListModelListener fModelListener = this.createSourceListModelListener();
	
	private final List<SourceListSelectionListener> fSourceListSelectionListeners = new ArrayList<SourceListSelectionListener>();
	
	private DefaultMutableTreeNode fRoot = new DefaultMutableTreeNode("root");
	private DefaultTreeModel fTreeModel = new DefaultTreeModel(this.fRoot);
	private JTree fTree = new CustomJTree(this.fTreeModel);
	
	private JScrollPane fScrollPane = MacWidgetFactory.createSourceListScrollPane(this.fTree);
	private final JPanel fComponent = new JPanel(new BorderLayout());
	private TreeSelectionListener fTreeSelectionListener = this.createTreeSelectionListener();
	private MouseListener fMouseListener = this.createMouseListener();
	
	private SourceListControlBar fSourceListControlBar;
	
	private SourceListContextMenuProvider fContextMenuProvider = new EmptySourceListContextMenuProvider();
	private List<SourceListClickListener> fSourceListClickListeners = new ArrayList<SourceListClickListener>();
	private SourceListToolTipProvider fToolTipProvider = new EmptyToolTipProvider();
	
	/**
	 * Creates a {@code SourceList} with an empty {@link SourceListModel}.
	 */
	public SourceList() {
		this(new SourceListModel());
	}
	
	/**
	 * Creates a {@code SourceList} with the given {@link SourceListModel}.
	 * 
	 * @param model
	 *            the {@code SourceListModel} to use.
	 */
	public SourceList(SourceListModel model) {
		if (model == null) {
			throw new IllegalArgumentException("Groups cannot be null.");
		}
		
		this.fModel = model;
		this.fModel.addSourceListModelListener(this.fModelListener);
		
		this.initUi();
		
		// add each category and its sub-items to backing JTree.
		for (int i = 0; i < model.getCategories().size(); i++) {
			this.doAddCategory(model.getCategories().get(i), i);
		}
	}
	
	private void initUi() {
		this.fComponent.add(this.fScrollPane, BorderLayout.CENTER);
		this.fTree.addTreeSelectionListener(this.fTreeSelectionListener);
		this.fTree.addMouseListener(this.fMouseListener);
	}
	
	/**
	 * Installs the given {@link SourceListControlBar} at the base of this
	 * {@code SourceList}. This
	 * method can be called only once, and should generally be called during
	 * creation of the {@code SourceList}.
	 * 
	 * @param sourceListControlBar
	 *            the {@link SourceListControlBar} to add.
	 * @throws IllegalStateException
	 *             if a {@code SourceListControlBar} has already been installed
	 *             on this {@code SourceList}.
	 * @throws IllegalArgumentException
	 *             if the {@code SourceListControlBar} is null.
	 */
	public void installSourceListControlBar(
			SourceListControlBar sourceListControlBar) {
		if (this.fSourceListControlBar != null) {
			throw new IllegalStateException(
					"A SourceListControlBar has already been installed on"
							+ " this SourceList.");
		}
		if (sourceListControlBar == null) {
			throw new IllegalArgumentException(
					"SourceListControlBar cannot be null.");
		}
		this.fSourceListControlBar = sourceListControlBar;
		this.fComponent.add(
				this.fSourceListControlBar.getComponent(),
				BorderLayout.SOUTH);
	}
	
	/**
	 * True if there is a {@link SourceListControlBar} installed on this
	 * {@code SourceList}.
	 * 
	 * @return true if there is a {@link SourceListControlBar} installed on this
	 *         {@code SourceList}.
	 */
	public boolean isSourceListControlBarInstalled() {
		return this.fSourceListControlBar != null;
	}
	
	/**
	 * Sets the {@link SourceListContextMenuProvider} to use for this
	 * {@code SourceList}.
	 * 
	 * @param contextMenuProvider
	 *            the {@link SourceListContextMenuProvider} to use for this
	 *            {@code SourceList}.
	 * @throws IllegalArgumentException
	 *             if the {@code SourceListContextMenuProvider} is null.
	 */
	public void setSourceListContextMenuProvider(
			SourceListContextMenuProvider contextMenuProvider) {
		if (contextMenuProvider == null) {
			throw new IllegalArgumentException(
					"SourceListContextMenuProvider cannot be null.");
		}
		this.fContextMenuProvider = contextMenuProvider;
	}
	
	/**
	 * Uninstalls any listeners that this {@code SourceList} installed on
	 * creation, thereby allowing
	 * it to be garbage collected.
	 */
	public void dispose() {
		this.fModel.removeSourceListModelListener(this.fModelListener);
	}
	
	/**
	 * Gets the selected {@link SourceListItem}.
	 * 
	 * @return the selected {@code SourceListItem}.
	 */
	public SourceListItem getSelectedItem() {
		SourceListItem selectedItem = null;
		if (this.fTree.getSelectionPath() != null
				&& this.fTree.getSelectionPath().getLastPathComponent() != null) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.fTree.getSelectionPath().getLastPathComponent();
			assert selectedNode.getUserObject() instanceof SourceListItem : "Only SourceListItems can be selected.";
			selectedItem = (SourceListItem) selectedNode.getUserObject();
		}
		return selectedItem;
	}
	
	/**
	 * Selects the given {@link SourceListItem} in the list.
	 * 
	 * @param item
	 *            the item to select.
	 * @throws IllegalArgumentException
	 *             if the given item is not in the list.
	 */
	public void setSelectedItem(SourceListItem item) {
		this.getModel().validateItemIsInModel(item);
		DefaultMutableTreeNode treeNode = getNodeForObject(this.fRoot, item);
		this.fTree.setSelectionPath(new TreePath(treeNode.getPath()));
	}
	
	/**
	 * Clears the current selection, if there is one.
	 */
	public void clearSelection() {
		this.fTree.clearSelection();
	}
	
	/**
	 * Sets whether this {@code SourceList} can have focus. When focusable and
	 * this {@code SourceList} has focus, the keyboard can be used for
	 * navigation.
	 * 
	 * @param focusable
	 *            true if this {@code SourceList} should be focusable.
	 */
	public void setFocusable(boolean focusable) {
		this.fTree.setFocusable(focusable);
	}
	
	/**
	 * Installs iApp style scroll bars on this {@code SourceList}.
	 * 
	 * @see IAppWidgetFactory#makeIAppScrollPane
	 */
	public void useIAppStyleScrollBars() {
		IAppWidgetFactory.makeIAppScrollPane(this.fScrollPane);
	}
	
	/**
	 * Gets the {@link SourceListColorScheme} that this {@code SourceList} uses.
	 * 
	 * @return the {@link SourceListColorScheme} that this {@code SourceList}
	 *         uses.
	 */
	public SourceListColorScheme getColorScheme() {
		return ((SourceListTreeUI) this.fTree.getUI()).getColorScheme();
	}
	
	/**
	 * Sets the {@link SourceListColorScheme} that this {@code SourceList} uses.
	 * 
	 * @param colorScheme
	 *            the {@link SourceListColorScheme} that this {@code SourceList}
	 *            uses.
	 */
	public void setColorScheme(SourceListColorScheme colorScheme) {
		((SourceListTreeUI) this.fTree.getUI()).setColorScheme(colorScheme);
	}
	
	/**
	 * Set's the {@link TransferHandler} that this {@code SourceList} should use
	 * during drag and drop operations. If the given handler not null, then
	 * dragging will be turned on for the {@code SourceList}. If the handler is
	 * null, then dragging will be turned off.
	 * 
	 * @param transferHandler
	 *            the {@code TransferHandler} for this {@code SourceList}. Can
	 *            be null.
	 */
	public void setTransferHandler(TransferHandler transferHandler) {
		this.fTree.setDragEnabled(transferHandler != null);
		this.fTree.setTransferHandler(transferHandler);
	}
	
	/**
	 * Scrolls the given {@link SourceListItem} to be visible.
	 * 
	 * @param item
	 *            the {@code SourceListItem} to scroll to visible.
	 */
	public void scrollItemToVisible(SourceListItem item) {
		this.getModel().validateItemIsInModel(item);
		DefaultMutableTreeNode treeNode = getNodeForObject(this.fRoot, item);
		this.fTree.scrollPathToVisible(new TreePath(treeNode.getPath()));
	}
	
	public boolean isExpanded(SourceListCategory category) {
		DefaultMutableTreeNode categoryNode = this.getNodeForObject(category);
		checkCategoryNodeNotNull(categoryNode);
		return this.fTree.isExpanded(new TreePath(categoryNode.getPath()));
	}
	
	/**
	 * Sets the expanded state of the given {@link SourceListCategory}.
	 * 
	 * @param category
	 *            the category to set the expanded state on.
	 * @param expanded
	 *            true if the given category should be expanded, false if it
	 *            should be
	 *            collapsed.
	 * @throws IllegalArgumentException
	 *             if the given {@code SourceListCategory} is not part of the
	 *             associated {@link SourceListModel}.
	 */
	public void setExpanded(SourceListCategory category, boolean expanded) {
		DefaultMutableTreeNode categoryNode = this.getNodeForObject(category);
		checkCategoryNodeNotNull(categoryNode);
		TreeUtils.setExpandedOnEdt(
				this.fTree,
				new TreePath(categoryNode.getPath()),
				expanded);
	}
	
	/**
	 * Sets the expanded state of the given {@link SourceListItem}.
	 * 
	 * @param item
	 *            the item to set the expanded state on.
	 * @param expanded
	 *            true if the given item should be expanded, false if it should
	 *            be
	 *            collapsed.
	 * @throws IllegalArgumentException
	 *             if the given {@code SourceListItem} is not part of the
	 *             associated {@link SourceListModel}.
	 */
	public void setExpanded(SourceListItem item, boolean expanded) {
		DefaultMutableTreeNode itemNode = this.getNodeForObject(item);
		checkItemNodeNotNull(itemNode);
		TreeUtils.setExpandedOnEdt(
				this.fTree,
				new TreePath(itemNode.getPath()),
				expanded);
	}
	
	private DefaultMutableTreeNode getNodeForObject(Object userObject) {
		return getNodeForObject(this.fRoot, userObject);
	}
	
	private static DefaultMutableTreeNode getNodeForObject(
			DefaultMutableTreeNode parentNode,
			Object userObject) {
		if (parentNode.getUserObject().equals(userObject)) {
			return parentNode;
		} else if (parentNode.children().hasMoreElements()) {
			for (int i = 0; i < parentNode.getChildCount(); i++) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
				DefaultMutableTreeNode retVal = getNodeForObject(
						childNode,
						userObject);
				if (retVal != null) {
					return retVal;
				}
			}
		} else {
			return null;
		}
		
		return null;
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
		return this.fComponent;
	}
	
	/**
	 * Gets the {@link SourceListModel} backing this {@code SourceList}.
	 * 
	 * @return the {@code SourceListModel} backing this {@code SourceList}.
	 */
	public SourceListModel getModel() {
		return this.fModel;
	}
	
	/**
	 * Sets the {@link SourceListToolTipProvider} to use.
	 * 
	 * @param toolTipProvider
	 *            the {@code SourceListToolTipProvider to use.

	 */
	public void setToolTipProvider(SourceListToolTipProvider toolTipProvider) {
		if (toolTipProvider == null) {
			throw new IllegalArgumentException(
					"SourceListToolTipProvider cannot be null.");
		}
		this.fToolTipProvider = toolTipProvider;
	}
	
	private void doAddCategory(SourceListCategory category, int index) {
		DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(
				category);
		this.fTreeModel.insertNodeInto(categoryNode, this.fRoot, index);
		// add each of the categories child items to the tree.
		for (int i = 0; i < category.getItems().size(); i++) {
			this.doAddItemToCategory(category.getItems().get(i), category, i);
		}
		
		TreeUtils.expandPathOnEdt(
				this.fTree,
				new TreePath(categoryNode.getPath()));
	}
	
	private void doRemoveCategory(SourceListCategory category) {
		DefaultMutableTreeNode categoryNode = getNodeForObject(
				this.fRoot,
				category);
		checkCategoryNodeNotNull(categoryNode);
		this.fTreeModel.removeNodeFromParent(categoryNode);
	}
	
	private void doAddItemToCategory(
			SourceListItem itemToAdd,
			SourceListCategory category,
			int index) {
		DefaultMutableTreeNode categoryNode = getNodeForObject(
				this.fRoot,
				category);
		checkCategoryNodeNotNull(categoryNode);
		this.doAddItemToNode(itemToAdd, categoryNode, index);
	}
	
	private void doRemoveItemFromCategory(
			SourceListItem itemToRemove,
			SourceListCategory category) {
		DefaultMutableTreeNode categoryNode = getNodeForObject(
				this.fRoot,
				category);
		checkCategoryNodeNotNull(categoryNode);
		DefaultMutableTreeNode itemNode = getNodeForObject(
				categoryNode,
				itemToRemove);
		checkCategoryNodeNotNull(itemNode);
		this.fTreeModel.removeNodeFromParent(itemNode);
	}
	
	private void doAddItemToItem(
			SourceListItem itemToAdd,
			SourceListItem parentItem,
			int index) {
		DefaultMutableTreeNode parentItemNode = getNodeForObject(
				this.fRoot,
				parentItem);
		checkCategoryNodeNotNull(parentItemNode);
		this.doAddItemToNode(itemToAdd, parentItemNode, index);
	}
	
	private void doRemoveItemFromItem(
			SourceListItem itemToRemove,
			SourceListItem parentItem) {
		DefaultMutableTreeNode parentNode = getNodeForObject(
				this.fRoot,
				parentItem);
		checkCategoryNodeNotNull(parentNode);
		DefaultMutableTreeNode itemNode = getNodeForObject(
				parentNode,
				itemToRemove);
		checkCategoryNodeNotNull(itemNode);
		this.fTreeModel.removeNodeFromParent(itemNode);
	}
	
	private void doAddItemToNode(
			SourceListItem itemToAdd,
			DefaultMutableTreeNode parentNode,
			int index) {
		DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode(itemToAdd);
		this.fTreeModel.insertNodeInto(itemNode, parentNode, index);
		// add each of the newly added item's children nodes.
		for (int i = 0; i < itemToAdd.getChildItems().size(); i++) {
			this.doAddItemToItem(itemToAdd.getChildItems().get(i), itemToAdd, i);
		}
		// if the parent node is a new node, expand it. thus the default
		// behavior is to expand a
		// parent node.
		if (parentNode.getChildCount() == 1) {
			TreeUtils.expandPathOnEdt(
					this.fTree,
					new TreePath(parentNode.getPath()));
		}
	}
	
	private void doItemChanged(SourceListItem item) {
		DefaultMutableTreeNode itemNode = getNodeForObject(this.fRoot, item);
		checkItemNodeNotNull(itemNode);
		this.fTreeModel.nodeChanged(itemNode);
	}
	
	private void doShowContextMenu(MouseEvent event) {
		// grab the item or category under the mouse events point if there is
		// there is an item or category under this point.
		Object itemOrCategory = this.getItemOrCategoryUnderPoint(event.getPoint());
		
		// if there was no item under the click, then call the generic
		// contribution method.
		// else if there was a SourceListItem under the click, call the
		// corresponding contribution
		// method.
		// else if there was a SourceListCategory under the click, call the
		// corresponding contribution
		// method.
		JPopupMenu popup = null;
		if (itemOrCategory == null) {
			popup = this.fContextMenuProvider.createContextMenu();
		} else if (itemOrCategory instanceof SourceListItem) {
			popup = this.fContextMenuProvider.createContextMenu((SourceListItem) itemOrCategory);
		} else if (itemOrCategory instanceof SourceListCategory) {
			popup = this.fContextMenuProvider.createContextMenu((SourceListCategory) itemOrCategory);
		}
		
		// only show the context-menu if menu items have been added to it.
		if (popup != null && popup.getComponentCount() > 0) {
			popup.show(this.fTree, event.getX(), event.getY());
		}
	}
	
	private void doSourceListClicked(MouseEvent event) {
		// grab the item or category under the mouse events point if there is
		// there is an item or category under this point.
		Object itemOrCategory = this.getItemOrCategoryUnderPoint(event.getPoint());
		
		SourceListClickListener.Button button = SourceListClickListener.Button.getButton(event.getButton());
		int clickCount = event.getClickCount();
		
		if (itemOrCategory == null) {
			// do nothing.
		} else if (itemOrCategory instanceof SourceListItem) {
			this.fireSourceListItemClicked(
					(SourceListItem) itemOrCategory,
					button,
					clickCount);
		} else if (itemOrCategory instanceof SourceListCategory) {
			this.fireSourceListCategoryClicked(
					(SourceListCategory) itemOrCategory,
					button,
					clickCount);
		}
	}
	
	private Object getItemOrCategoryUnderPoint(Point point) {
		// grab the path under the given point.
		TreePath path = this.fTree.getPathForLocation(point.x, point.y);
		// if there is a tree item under that point, cast it to a
		// DefaultMutableTreeNode and grab
		// the user object which will either be a SourceListItem or
		// SourceListCategory.
		return path == null ? null : ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
	}
	
	private TreeSelectionListener createTreeSelectionListener() {
		return new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				SourceList.this.fireSourceListItemSelected(SourceList.this.getSelectedItem());
			}
		};
	}
	
	private SourceListModelListener createSourceListModelListener() {
		return new SourceListModelListener() {
			
			@Override
			public void categoryAdded(SourceListCategory category, int index) {
				SourceList.this.doAddCategory(category, index);
			}
			
			@Override
			public void categoryRemoved(SourceListCategory category) {
				SourceList.this.doRemoveCategory(category);
			}
			
			@Override
			public void itemAddedToCategory(
					SourceListItem item,
					SourceListCategory category,
					int index) {
				SourceList.this.doAddItemToCategory(item, category, index);
			}
			
			@Override
			public void itemRemovedFromCategory(
					SourceListItem item,
					SourceListCategory category) {
				SourceList.this.doRemoveItemFromCategory(item, category);
			}
			
			@Override
			public void itemAddedToItem(
					SourceListItem item,
					SourceListItem parentItem,
					int index) {
				SourceList.this.doAddItemToItem(item, parentItem, index);
			}
			
			@Override
			public void itemRemovedFromItem(
					SourceListItem item,
					SourceListItem parentItem) {
				SourceList.this.doRemoveItemFromItem(item, parentItem);
			}
			
			@Override
			public void itemChanged(SourceListItem item) {
				SourceList.this.doItemChanged(item);
			}
		};
	}
	
	private MouseListener createMouseListener() {
		return new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					SourceList.this.doShowContextMenu(e);
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					SourceList.this.doShowContextMenu(e);
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				SourceList.this.doSourceListClicked(e);
			}
		};
	}
	
	// SourceListClickListener support. ///////////////////////////////////////
	
	private void fireSourceListItemClicked(
			SourceListItem item,
			SourceListClickListener.Button button,
			int clickCount) {
		for (SourceListClickListener listener : this.fSourceListClickListeners) {
			listener.sourceListItemClicked(item, button, clickCount);
		}
	}
	
	private void fireSourceListCategoryClicked(
			SourceListCategory category,
			SourceListClickListener.Button button,
			int clickCount) {
		for (SourceListClickListener listener : this.fSourceListClickListeners) {
			listener.sourceListCategoryClicked(category, button, clickCount);
		}
	}
	
	/**
	 * Adds the {@link SourceListClickListener} to the list of listeners.
	 * 
	 * @param listener
	 *            the {@code SourceListClickListener} to add.
	 */
	public void addSourceListClickListener(SourceListClickListener listener) {
		this.fSourceListClickListeners.add(listener);
	}
	
	/**
	 * Removes the {@link SourceListClickListener} to the list of listeners.
	 * 
	 * @param listener
	 *            the {@code SourceListClickListener} to remove.
	 */
	public void removeSourceListClickListener(SourceListClickListener listener) {
		this.fSourceListClickListeners.remove(listener);
	}
	
	// SourceListSelectionListener support. ///////////////////////////////////
	
	private void fireSourceListItemSelected(SourceListItem item) {
		for (SourceListSelectionListener listener : this.fSourceListSelectionListeners) {
			listener.sourceListItemSelected(item);
		}
	}
	
	/**
	 * Adds the {@link SourceListSelectionListener} to the list of listeners.
	 * 
	 * @param listener
	 *            the {@code SourceListSelectionListener} to add.
	 */
	public void addSourceListSelectionListener(
			SourceListSelectionListener listener) {
		this.fSourceListSelectionListeners.add(listener);
	}
	
	/**
	 * Removes the {@link SourceListSelectionListener} from the list of
	 * listeners.
	 * 
	 * @param listener
	 *            the {@code SourceListSelectionListener} to remove.
	 */
	public void removeSourceListSelectionListener(
			SourceListSelectionListener listener) {
		this.fSourceListSelectionListeners.remove(listener);
	}
	
	// Utility methods.
	// ///////////////////////////////////////////////////////////////////////////
	
	private static void checkCategoryNodeNotNull(MutableTreeNode node) {
		if (node == null) {
			throw new IllegalArgumentException("The given SourceListCategory "
					+ "does not exist in this SourceList.");
		}
	}
	
	private static void checkItemNodeNotNull(MutableTreeNode node) {
		if (node == null) {
			throw new IllegalArgumentException("The given SourceListItem "
					+ "does not exist in this SourceList.");
		}
	}
	
	// EmptySourceListContextMenuProvider implementation.
	// /////////////////////////////////////////
	
	private static class EmptySourceListContextMenuProvider implements SourceListContextMenuProvider {
		
		@Override
		public JPopupMenu createContextMenu() {
			return null;
		}
		
		@Override
		public JPopupMenu createContextMenu(SourceListItem item) {
			return null;
		}
		
		@Override
		public JPopupMenu createContextMenu(SourceListCategory category) {
			return null;
		}
	}
	
	// Custom JTree implementation that always returns SourceListTreeUI
	// delegate. /////////////////
	
	private class CustomJTree extends JTree {
		
		public CustomJTree(TreeModel newModel) {
			super(newModel);
			ToolTipManager.sharedInstance().registerComponent(this);
		}
		
		@Override
		public void updateUI() {
			this.setUI(new SourceListTreeUI());
			this.invalidate();
		}
		
		@Override
		public String getToolTipText(MouseEvent event) {
			TreePath path = this.getPathForLocation(event.getX(), event.getY());
			Object userObject = path == null ? null : ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
			String toolTipText = null;
			if (userObject instanceof SourceListCategory) {
				toolTipText = SourceList.this.fToolTipProvider.getTooltip((SourceListCategory) userObject);
			} else if (userObject instanceof SourceListItem) {
				toolTipText = SourceList.this.fToolTipProvider.getTooltip((SourceListItem) userObject);
			}
			return toolTipText;
		}
	}
	
	// Empty SourceListTooltipProvider.
	// ///////////////////////////////////////////////////////////
	
	private static class EmptyToolTipProvider implements SourceListToolTipProvider {
		
		@Override
		public String getTooltip(SourceListCategory category) {
			return null;
		}
		
		@Override
		public String getTooltip(SourceListItem item) {
			return null;
		}
	}
}
