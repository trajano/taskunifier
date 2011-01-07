package com.leclercb.taskunifier.gui.utils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public final class TreeUtils {
	
	private TreeUtils() {

	}
	
	public static TreePath getPath(TreeNode treeNode) {
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		
		while (treeNode != null) {
			nodes.add(0, treeNode);
			treeNode = treeNode.getParent();
		}
		
		return nodes.isEmpty() ? null : new TreePath(nodes.toArray());
	}
	
}
