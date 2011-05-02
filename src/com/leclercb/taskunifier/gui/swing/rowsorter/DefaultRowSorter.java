/*
 * Copyright 2005-2006 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.leclercb.taskunifier.gui.swing.rowsorter;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

/**
 * An implementation of <code>RowSorter</code> that provides sorting and
 * filtering around a grid-based data model. Beyond creating and installing a
 * <code>RowSorter</code>, you very rarely need to interact with one directly.
 * Refer to {@link javax.swing.table.TableRowSorter TableRowSorter} for a
 * concrete implementation of <code>RowSorter</code> for <code>JTable</code>.
 * <p>
 * Sorting is done based on the current <code>SortKey</code>s, in order. If two
 * objects are equal (the <code>Comparator</code> for the column returns 0) the
 * next <code>SortKey</code> is used. If no <code>SortKey</code>s remain or the
 * order is <code>UNSORTED</code>, then the order of the rows in the model is
 * used.
 * <p>
 * Sorting of each column is done by way of a <code>Comparator</code> that you
 * can specify using the <code>setComparator</code> method. If a
 * <code>Comparator</code> has not been specified, the <code>Comparator</code>
 * returned by <code>Collator.getInstance()</code> is used on the results of
 * calling <code>toString</code> on the underlying objects. The
 * <code>Comparator</code> is never passed <code>null</code>. A
 * <code>null</code> value is treated as occuring before a non-<code>null</code>
 * value, and two <code>null</code> values are considered equal.
 * <p>
 * If you specify a <code>Comparator</code> that casts its argument to a type
 * other than that provided by the model, a <code>ClassCastException</code> will
 * be thrown when the data is sorted.
 * <p>
 * In addition to sorting, <code>DefaultRowSorter</code> provides the ability to
 * filter rows. Filtering is done by way of a <code>RowFilter</code> that is
 * specified using the <code>setRowFilter</code> method. If no filter has been
 * specified all rows are included.
 * <p>
 * By default, rows are in unsorted order (the same as the model) and every
 * column is sortable. The default <code>Comparator</code>s are documented in
 * the subclasses (for example, {@link javax.swing.table.TableRowSorter
 * TableRowSorter}).
 * <p>
 * If the underlying model structure changes (the
 * <code>modelStructureChanged</code> method is invoked) the following are reset
 * to their default values: <code>Comparator</code>s by column, current sort
 * order, and whether each column is sortable. To find the default
 * <code>Comparator</code>s, see the concrete implementation (for example,
 * {@link javax.swing.table.TableRowSorter TableRowSorter}). The default sort
 * order is unsorted (the same as the model), and columns are sortable by
 * default.
 * <p>
 * If the underlying model structure changes (the
 * <code>modelStructureChanged</code> method is invoked) the following are reset
 * to their default values: <code>Comparator</code>s by column, current sort
 * order and whether a column is sortable.
 * <p>
 * <code>DefaultRowSorter</code> is an abstract class. Concrete subclasses must
 * provide access to the underlying data by invoking {@code setModelWrapper}.
 * The {@code setModelWrapper} method <b>must</b> be invoked soon after the
 * constructor is called, ideally from within the subclass's constructor.
 * Undefined behavior will result if you use a {@code DefaultRowSorter} without
 * specifying a {@code ModelWrapper}.
 * <p>
 * <code>DefaultRowSorter</code> has two formal type parameters. The first type
 * parameter corresponds to the class of the model, for example
 * <code>DefaultTableModel</code>. The second type parameter corresponds to the
 * class of the identifier passed to the <code>RowFilter</code>. Refer to
 * <code>TableRowSorter</code> and <code>RowFilter</code> for more details on
 * the type parameters.
 * 
 * @param <M>
 *            the type of the model
 * @param <I>
 *            the type of the identifier passed to the <code>RowFilter</code>
 * @see javax.swing.table.TableRowSorter
 * @see javax.swing.table.DefaultTableModel
 * @see java.text.Collator
 * @since 1.6
 */
@SuppressWarnings({ "rawtypes", "unused" })
public abstract class DefaultRowSorter<M, I> extends RowSorter<M> {
	
	/**
	 * Whether or not we resort on TableModelEvent.UPDATEs.
	 */
	private boolean sortsOnUpdates;
	
	/**
	 * View (JTable) -> model.
	 */
	private Row[] viewToModel;
	
	/**
	 * model -> view (JTable)
	 */
	private int[] modelToView;
	
	/**
	 * Comparators specified by column.
	 */
	private Comparator[] comparators;
	
	/**
	 * Whether or not the specified column is sortable, by column.
	 */
	private boolean[] isSortable;
	
	/**
	 * Cached SortKeys for the current sort.
	 */
	private SortKey[] cachedSortKeys;
	
	/**
	 * Cached comparators for the current sort
	 */
	private Comparator[] sortComparators;
	
	/**
	 * Developer supplied Filter.
	 */
	private RowFilter<? super M, ? super I> filter;
	
	/**
	 * Value passed to the filter. The same instance is passed to the filter for
	 * different rows.
	 */
	private FilterEntry filterEntry;
	
	/**
	 * The sort keys.
	 */
	private List<SortKey> sortKeys;
	
	/**
	 * Whether or not to use getStringValueAt. This is indexed by column.
	 */
	private boolean[] useToString;
	
	/**
	 * Indicates the contents are sorted. This is used if getSortsOnUpdates is
	 * false and an update event is received.
	 */
	private boolean sorted;
	
	/**
	 * Maximum number of sort keys.
	 */
	private int maxSortKeys;
	
	/**
	 * Provides access to the data we're sorting/filtering.
	 */
	private ModelWrapper<M, I> modelWrapper;
	
	/**
	 * Size of the model. This is used to enforce error checking within the
	 * table changed notification methods (such as rowsInserted).
	 */
	private int modelRowCount;
	
	/**
	 * Creates an empty <code>DefaultRowSorter</code>.
	 */
	public DefaultRowSorter() {
		this.sortKeys = Collections.emptyList();
		this.maxSortKeys = 3;
	}
	
	/**
	 * Sets the model wrapper providing the data that is being sorted and
	 * filtered.
	 * 
	 * @param modelWrapper
	 *            the model wrapper responsible for providing the data that gets
	 *            sorted and filtered
	 * @throws IllegalArgumentException
	 *             if {@code modelWrapper} is {@code null}
	 */
	protected final void setModelWrapper(ModelWrapper<M, I> modelWrapper) {
		if (modelWrapper == null) {
			throw new IllegalArgumentException("modelWrapper most be non-null");
		}
		ModelWrapper<M, I> last = this.modelWrapper;
		this.modelWrapper = modelWrapper;
		if (last != null) {
			this.modelStructureChanged();
		} else {
			// If last is null, we're in the constructor. If we're in
			// the constructor we don't want to call to overridable methods.
			this.modelRowCount = this.getModelWrapper().getRowCount();
		}
	}
	
	/**
	 * Returns the model wrapper providing the data that is being sorted and
	 * filtered.
	 * 
	 * @return the model wrapper responsible for providing the data that gets
	 *         sorted and filtered
	 */
	protected final ModelWrapper<M, I> getModelWrapper() {
		return this.modelWrapper;
	}
	
	/**
	 * Returns the underlying model.
	 * 
	 * @return the underlying model
	 */
	@Override
	public final M getModel() {
		return this.getModelWrapper().getModel();
	}
	
	/**
	 * Sets whether or not the specified column is sortable. The specified value
	 * is only checked when <code>toggleSortOrder</code> is invoked. It is still
	 * possible to sort on a column that has been marked as unsortable by
	 * directly setting the sort keys. The default is true.
	 * 
	 * @param column
	 *            the column to enable or disable sorting on, in terms of the
	 *            underlying model
	 * @param sortable
	 *            whether or not the specified column is sortable
	 * @throws IndexOutOfBoundsException
	 *             if <code>column</code> is outside the range of the model
	 * @see #toggleSortOrder
	 * @see #setSortKeys
	 */
	public void setSortable(int column, boolean sortable) {
		this.checkColumn(column);
		if (this.isSortable == null) {
			this.isSortable = new boolean[this.getModelWrapper().getColumnCount()];
			for (int i = this.isSortable.length - 1; i >= 0; i--) {
				this.isSortable[i] = true;
			}
		}
		this.isSortable[column] = sortable;
	}
	
	/**
	 * Returns true if the specified column is sortable; otherwise, false.
	 * 
	 * @param column
	 *            the column to check sorting for, in terms of the underlying
	 *            model
	 * @return true if the column is sortable
	 * @throws IndexOutOfBoundsException
	 *             if column is outside the range of the underlying model
	 */
	public boolean isSortable(int column) {
		this.checkColumn(column);
		return (this.isSortable == null) ? true : this.isSortable[column];
	}
	
	/**
	 * Sets the sort keys. This creates a copy of the supplied {@code List};
	 * subsequent changes to the supplied {@code List} do not effect this
	 * {@code DefaultRowSorter}. If the sort keys have changed this triggers a
	 * sort.
	 * 
	 * @param sortKeys
	 *            the new <code>SortKeys</code>; <code>null</code> is a
	 *            shorthand for specifying an empty list, indicating that the
	 *            view should be unsorted
	 * @throws IllegalArgumentException
	 *             if any of the values in <code>sortKeys</code> are null or
	 *             have a column index outside the range of the model
	 */
	@Override
	public void setSortKeys(List<? extends SortKey> sortKeys) {
		List<SortKey> old = this.sortKeys;
		if (sortKeys != null && sortKeys.size() > 0) {
			int max = this.getModelWrapper().getColumnCount();
			for (SortKey key : sortKeys) {
				if (key == null
						|| key.getColumn() < 0
						|| key.getColumn() >= max) {
					throw new IllegalArgumentException("Invalid SortKey");
				}
			}
			this.sortKeys = Collections.unmodifiableList(new ArrayList<SortKey>(
					sortKeys));
		} else {
			this.sortKeys = Collections.emptyList();
		}
		if (!this.sortKeys.equals(old)) {
			this.fireSortOrderChanged();
			if (this.viewToModel == null) {
				// Currently unsorted, use sort so that internal fields
				// are correctly set.
				this.sort();
			} else {
				this.sortExistingData();
			}
		}
	}
	
	/**
	 * Returns the current sort keys. This returns an unmodifiable
	 * {@code non-null List}. If you need to change the sort keys, make a copy
	 * of the returned {@code List}, mutate the copy and invoke
	 * {@code setSortKeys} with the new list.
	 * 
	 * @return the current sort order
	 */
	@Override
	public List<? extends SortKey> getSortKeys() {
		return this.sortKeys;
	}
	
	/**
	 * Sets the maximum number of sort keys. The number of sort keys determines
	 * how equal values are resolved when sorting. For example, assume a table
	 * row sorter is created and <code>setMaxSortKeys(2)</code> is invoked on
	 * it. The user clicks the header for column 1, causing the table rows to be
	 * sorted based on the items in column 1. Next, the user clicks the header
	 * for column 2, causing the table to be sorted based on the items in column
	 * 2; if any items in column 2 are equal, then those particular rows are
	 * ordered based on the items in column 1. In this case, we say that the
	 * rows are primarily sorted on column 2, and secondarily on column 1. If
	 * the user then clicks the header for column 3, then the items are
	 * primarily sorted on column 3 and secondarily sorted on column 2. Because
	 * the maximum number of sort keys has been set to 2 with
	 * <code>setMaxSortKeys</code>, column 1 no longer has an effect on the
	 * order.
	 * <p>
	 * The maximum number of sort keys is enforced by
	 * <code>toggleSortOrder</code>. You can specify more sort keys by invoking
	 * <code>setSortKeys</code> directly and they will all be honored. However
	 * if <code>toggleSortOrder</code> is subsequently invoked the maximum
	 * number of sort keys will be enforced. The default value is 3.
	 * 
	 * @param max
	 *            the maximum number of sort keys
	 * @throws IllegalArgumentException
	 *             if <code>max</code> &lt; 1
	 */
	public void setMaxSortKeys(int max) {
		if (max < 1) {
			throw new IllegalArgumentException("Invalid max");
		}
		this.maxSortKeys = max;
	}
	
	/**
	 * Returns the maximum number of sort keys.
	 * 
	 * @return the maximum number of sort keys
	 */
	public int getMaxSortKeys() {
		return this.maxSortKeys;
	}
	
	/**
	 * If true, specifies that a sort should happen when the underlying model is
	 * updated (<code>rowsUpdated</code> is invoked). For example, if this is
	 * true and the user edits an entry the location of that item in the view
	 * may change. The default is false.
	 * 
	 * @param sortsOnUpdates
	 *            whether or not to sort on update events
	 */
	public void setSortsOnUpdates(boolean sortsOnUpdates) {
		this.sortsOnUpdates = sortsOnUpdates;
	}
	
	/**
	 * Returns true if a sort should happen when the underlying model is
	 * updated; otherwise, returns false.
	 * 
	 * @return whether or not to sort when the model is updated
	 */
	public boolean getSortsOnUpdates() {
		return this.sortsOnUpdates;
	}
	
	/**
	 * Sets the filter that determines which rows, if any, should be hidden from
	 * the view. The filter is applied before sorting. A value of
	 * <code>null</code> indicates all values from the model should be included.
	 * <p>
	 * <code>RowFilter</code>'s <code>include</code> method is passed an
	 * <code>Entry</code> that wraps the underlying model. The number of columns
	 * in the <code>Entry</code> corresponds to the number of columns in the
	 * <code>ModelWrapper</code>. The identifier comes from the
	 * <code>ModelWrapper</code> as well.
	 * <p>
	 * This method triggers a sort.
	 * 
	 * @param filter
	 *            the filter used to determine what entries should be included
	 */
	public void setRowFilter(RowFilter<? super M, ? super I> filter) {
		this.filter = filter;
		this.sort();
	}
	
	/**
	 * Returns the filter that determines which rows, if any, should be hidden
	 * from view.
	 * 
	 * @return the filter
	 */
	public RowFilter<? super M, ? super I> getRowFilter() {
		return this.filter;
	}
	
	/**
	 * Reverses the sort order from ascending to descending (or descending to
	 * ascending) if the specified column is already the primary sorted column;
	 * otherwise, makes the specified column the primary sorted column, with an
	 * ascending sort order. If the specified column is not sortable, this
	 * method has no effect.
	 * 
	 * @param column
	 *            index of the column to make the primary sorted column, in
	 *            terms of the underlying model
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 * @see #setSortable(int,boolean)
	 * @see #setMaxSortKeys(int)
	 */
	@Override
	public void toggleSortOrder(int column) {
		this.checkColumn(column);
		if (this.isSortable(column)) {
			List<SortKey> keys = new ArrayList<SortKey>(this.getSortKeys());
			SortKey sortKey;
			int sortIndex;
			for (sortIndex = keys.size() - 1; sortIndex >= 0; sortIndex--) {
				if (keys.get(sortIndex).getColumn() == column) {
					break;
				}
			}
			if (sortIndex == -1) {
				// Key doesn't exist
				sortKey = new SortKey(column, SortOrder.ASCENDING);
				keys.add(0, sortKey);
			} else if (sortIndex == 0) {
				// It's the primary sorting key, toggle it
				keys.set(0, this.toggle(keys.get(0)));
			} else {
				// It's not the first, but was sorted on, remove old
				// entry, insert as first with ascending.
				keys.remove(sortIndex);
				keys.add(0, new SortKey(column, SortOrder.ASCENDING));
			}
			if (keys.size() > this.getMaxSortKeys()) {
				keys = keys.subList(0, this.getMaxSortKeys());
			}
			this.setSortKeys(keys);
		}
	}
	
	private SortKey toggle(SortKey key) {
		if (key.getSortOrder() == SortOrder.ASCENDING) {
			return new SortKey(key.getColumn(), SortOrder.DESCENDING);
		}
		return new SortKey(key.getColumn(), SortOrder.ASCENDING);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	@Override
	public int convertRowIndexToView(int index) {
		if (this.modelToView == null) {
			if (index < 0 || index >= this.getModelWrapper().getRowCount()) {
				throw new IndexOutOfBoundsException("Invalid index");
			}
			return index;
		}
		return this.modelToView[index];
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	@Override
	public int convertRowIndexToModel(int index) {
		if (this.viewToModel == null) {
			if (index < 0 || index >= this.getModelWrapper().getRowCount()) {
				throw new IndexOutOfBoundsException("Invalid index");
			}
			return index;
		}
		
		return this.viewToModel[index].modelIndex;
	}
	
	private boolean isUnsorted() {
		List<? extends SortKey> keys = this.getSortKeys();
		int keySize = keys.size();
		return (keySize == 0 || keys.get(0).getSortOrder() == SortOrder.UNSORTED);
	}
	
	/**
	 * Sorts the existing filtered data. This should only be used if the filter
	 * hasn't changed.
	 */
	private void sortExistingData() {
		int[] lastViewToModel = this.getViewToModelAsInts(this.viewToModel);
		
		this.updateUseToString();
		this.cacheSortKeys(this.getSortKeys());
		
		if (this.isUnsorted()) {
			if (this.getRowFilter() == null) {
				this.viewToModel = null;
				this.modelToView = null;
			} else {
				int included = 0;
				for (int i = 0; i < this.modelToView.length; i++) {
					if (this.modelToView[i] != -1) {
						this.viewToModel[included].modelIndex = i;
						this.modelToView[i] = included++;
					}
				}
			}
		} else {
			// sort the data
			Arrays.sort(this.viewToModel);
			
			// Update the modelToView array
			this.setModelToViewFromViewToModel(false);
		}
		this.fireRowSorterChanged(lastViewToModel);
	}
	
	/**
	 * Sorts and filters the rows in the view based on the sort keys of the
	 * columns currently being sorted and the filter, if any, associated with
	 * this sorter. An empty <code>sortKeys</code> list indicates that the view
	 * should unsorted, the same as the model.
	 * 
	 * @see #setRowFilter
	 * @see #setSortKeys
	 */
	public void sort() {
		this.sorted = true;
		int[] lastViewToModel = this.getViewToModelAsInts(this.viewToModel);
		this.updateUseToString();
		if (this.isUnsorted()) {
			// Unsorted
			this.cachedSortKeys = new SortKey[0];
			if (this.getRowFilter() == null) {
				// No filter & unsorted
				if (this.viewToModel != null) {
					// sorted -> unsorted
					this.viewToModel = null;
					this.modelToView = null;
				} else {
					// unsorted -> unsorted
					// No need to do anything.
					return;
				}
			} else {
				// There is filter, reset mappings
				this.initializeFilteredMapping();
			}
		} else {
			this.cacheSortKeys(this.getSortKeys());
			
			if (this.getRowFilter() != null) {
				this.initializeFilteredMapping();
			} else {
				this.createModelToView(this.getModelWrapper().getRowCount());
				this.createViewToModel(this.getModelWrapper().getRowCount());
			}
			
			// sort them
			Arrays.sort(this.viewToModel);
			
			// Update the modelToView array
			this.setModelToViewFromViewToModel(false);
		}
		this.fireRowSorterChanged(lastViewToModel);
	}
	
	/**
	 * Updates the useToString mapping before a sort.
	 */
	private void updateUseToString() {
		int i = this.getModelWrapper().getColumnCount();
		if (this.useToString == null || this.useToString.length != i) {
			this.useToString = new boolean[i];
		}
		for (--i; i >= 0; i--) {
			this.useToString[i] = this.useToString(i);
		}
	}
	
	/**
	 * Resets the viewToModel and modelToView mappings based on the current
	 * Filter.
	 */
	private void initializeFilteredMapping() {
		int rowCount = this.getModelWrapper().getRowCount();
		int i, j;
		int excludedCount = 0;
		
		// Update model -> view
		this.createModelToView(rowCount);
		for (i = 0; i < rowCount; i++) {
			if (this.include(i)) {
				this.modelToView[i] = i - excludedCount;
			} else {
				this.modelToView[i] = -1;
				excludedCount++;
			}
		}
		
		// Update view -> model
		this.createViewToModel(rowCount - excludedCount);
		for (i = 0, j = 0; i < rowCount; i++) {
			if (this.modelToView[i] != -1) {
				this.viewToModel[j++].modelIndex = i;
			}
		}
	}
	
	/**
	 * Makes sure the modelToView array is of size rowCount.
	 */
	private void createModelToView(int rowCount) {
		if (this.modelToView == null || this.modelToView.length != rowCount) {
			this.modelToView = new int[rowCount];
		}
	}
	
	/**
	 * Resets the viewToModel array to be of size rowCount.
	 */
	private void createViewToModel(int rowCount) {
		int recreateFrom = 0;
		if (this.viewToModel != null) {
			recreateFrom = Math.min(rowCount, this.viewToModel.length);
			if (this.viewToModel.length != rowCount) {
				Row[] oldViewToModel = this.viewToModel;
				this.viewToModel = new Row[rowCount];
				System.arraycopy(
						oldViewToModel,
						0,
						this.viewToModel,
						0,
						recreateFrom);
			}
		} else {
			this.viewToModel = new Row[rowCount];
		}
		int i;
		for (i = 0; i < recreateFrom; i++) {
			this.viewToModel[i].modelIndex = i;
		}
		for (i = recreateFrom; i < rowCount; i++) {
			this.viewToModel[i] = new Row(this, i);
		}
	}
	
	/**
	 * Caches the sort keys before a sort.
	 */
	private void cacheSortKeys(List<? extends SortKey> keys) {
		int keySize = keys.size();
		this.sortComparators = new Comparator[keySize];
		for (int i = 0; i < keySize; i++) {
			this.sortComparators[i] = this.getComparator0(keys.get(i).getColumn());
		}
		this.cachedSortKeys = keys.toArray(new SortKey[keySize]);
	}
	
	/**
	 * Returns whether or not to convert the value to a string before doing
	 * comparisons when sorting. If true
	 * <code>ModelWrapper.getStringValueAt</code> will be used, otherwise
	 * <code>ModelWrapper.getValueAt</code> will be used. It is up to
	 * subclasses, such as <code>TableRowSorter</code>, to honor this value in
	 * their <code>ModelWrapper</code> implementation.
	 * 
	 * @param column
	 *            the index of the column to test, in terms of the underlying
	 *            model
	 * @throws IndexOutOfBoundsException
	 *             if <code>column</code> is not valid
	 */
	protected boolean useToString(int column) {
		return (this.getComparator(column) == null);
	}
	
	/**
	 * Refreshes the modelToView mapping from that of viewToModel. If
	 * <code>unsetFirst</code> is true, all indices in modelToView are first set
	 * to -1.
	 */
	private void setModelToViewFromViewToModel(boolean unsetFirst) {
		int i;
		if (unsetFirst) {
			for (i = this.modelToView.length - 1; i >= 0; i--) {
				this.modelToView[i] = -1;
			}
		}
		for (i = this.viewToModel.length - 1; i >= 0; i--) {
			this.modelToView[this.viewToModel[i].modelIndex] = i;
		}
	}
	
	private int[] getViewToModelAsInts(Row[] viewToModel) {
		if (viewToModel != null) {
			int[] viewToModelI = new int[viewToModel.length];
			for (int i = viewToModel.length - 1; i >= 0; i--) {
				viewToModelI[i] = viewToModel[i].modelIndex;
			}
			return viewToModelI;
		}
		return new int[0];
	}
	
	/**
	 * Sets the <code>Comparator</code> to use when sorting the specified
	 * column. This does not trigger a sort. If you want to sort after setting
	 * the comparator you need to explicitly invoke <code>sort</code>.
	 * 
	 * @param column
	 *            the index of the column the <code>Comparator</code> is to be
	 *            used for, in terms of the underlying model
	 * @param comparator
	 *            the <code>Comparator</code> to use
	 * @throws IndexOutOfBoundsException
	 *             if <code>column</code> is outside the range of the underlying
	 *             model
	 */
	public void setComparator(int column, Comparator<?> comparator) {
		this.checkColumn(column);
		if (this.comparators == null) {
			this.comparators = new Comparator[this.getModelWrapper().getColumnCount()];
		}
		this.comparators[column] = comparator;
	}
	
	/**
	 * Returns the <code>Comparator</code> for the specified column. This will
	 * return <code>null</code> if a <code>Comparator</code> has not been
	 * specified for the column.
	 * 
	 * @param column
	 *            the column to fetch the <code>Comparator</code> for, in terms
	 *            of the underlying model
	 * @return the <code>Comparator</code> for the specified column
	 * @throws IndexOutOfBoundsException
	 *             if column is outside the range of the underlying model
	 */
	public Comparator<?> getComparator(int column) {
		this.checkColumn(column);
		if (this.comparators != null) {
			return this.comparators[column];
		}
		return null;
	}
	
	// Returns the Comparator to use during sorting. Where as
	// getComparator() may return null, this will never return null.
	private Comparator getComparator0(int column) {
		Comparator comparator = this.getComparator(column);
		if (comparator != null) {
			return comparator;
		}
		// This should be ok as useToString(column) should have returned
		// true in this case.
		return Collator.getInstance();
	}
	
	private RowFilter.Entry<M, I> getFilterEntry(int modelIndex) {
		if (this.filterEntry == null) {
			this.filterEntry = new FilterEntry();
		}
		this.filterEntry.modelIndex = modelIndex;
		return this.filterEntry;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getViewRowCount() {
		if (this.viewToModel != null) {
			// When filtering this may differ from
			// getModelWrapper().getRowCount()
			return this.viewToModel.length;
		}
		return this.getModelWrapper().getRowCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getModelRowCount() {
		return this.getModelWrapper().getRowCount();
	}
	
	private void allChanged() {
		this.modelToView = null;
		this.viewToModel = null;
		this.comparators = null;
		this.isSortable = null;
		if (this.isUnsorted()) {
			// Keys are already empty, to force a resort we have to
			// call sort
			this.sort();
		} else {
			this.setSortKeys(null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void modelStructureChanged() {
		this.allChanged();
		this.modelRowCount = this.getModelWrapper().getRowCount();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void allRowsChanged() {
		this.modelRowCount = this.getModelWrapper().getRowCount();
		this.sort();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	@Override
	public void rowsInserted(int firstRow, int endRow) {
		this.checkAgainstModel(firstRow, endRow);
		int newModelRowCount = this.getModelWrapper().getRowCount();
		if (endRow >= newModelRowCount) {
			throw new IndexOutOfBoundsException("Invalid range");
		}
		this.modelRowCount = newModelRowCount;
		if (this.shouldOptimizeChange(firstRow, endRow)) {
			this.rowsInserted0(firstRow, endRow);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	@Override
	public void rowsDeleted(int firstRow, int endRow) {
		this.checkAgainstModel(firstRow, endRow);
		if (firstRow >= this.modelRowCount || endRow >= this.modelRowCount) {
			throw new IndexOutOfBoundsException("Invalid range");
		}
		this.modelRowCount = this.getModelWrapper().getRowCount();
		if (this.shouldOptimizeChange(firstRow, endRow)) {
			this.rowsDeleted0(firstRow, endRow);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	@Override
	public void rowsUpdated(int firstRow, int endRow) {
		this.checkAgainstModel(firstRow, endRow);
		if (firstRow >= this.modelRowCount || endRow >= this.modelRowCount) {
			throw new IndexOutOfBoundsException("Invalid range");
		}
		if (this.getSortsOnUpdates()) {
			if (this.shouldOptimizeChange(firstRow, endRow)) {
				this.rowsUpdated0(firstRow, endRow);
			}
		} else {
			this.sorted = false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	@Override
	public void rowsUpdated(int firstRow, int endRow, int column) {
		this.checkColumn(column);
		this.rowsUpdated(firstRow, endRow);
	}
	
	private void checkAgainstModel(int firstRow, int endRow) {
		if (firstRow > endRow
				|| firstRow < 0
				|| endRow < 0
				|| firstRow > this.modelRowCount) {
			throw new IndexOutOfBoundsException("Invalid range");
		}
	}
	
	/**
	 * Returns true if the specified row should be included.
	 */
	private boolean include(int row) {
		RowFilter<? super M, ? super I> filter = this.getRowFilter();
		if (filter != null) {
			return filter.include(this.getFilterEntry(row));
		}
		// null filter, always include the row.
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private int compare(int model1, int model2) {
		int column;
		SortOrder sortOrder;
		Object v1, v2;
		int result;
		
		for (int counter = 0; counter < this.cachedSortKeys.length; counter++) {
			column = this.cachedSortKeys[counter].getColumn();
			sortOrder = this.cachedSortKeys[counter].getSortOrder();
			if (sortOrder == SortOrder.UNSORTED) {
				result = model1 - model2;
			} else {
				if (this.sortComparators[counter] instanceof RowComparator<?>) {
					result = ((RowComparator<?>) this.sortComparators[counter]).compare(
							model1,
							model2,
							column,
							sortOrder);
				} else {
					// v1 != null && v2 != null
					if (this.useToString[column]) {
						v1 = this.getModelWrapper().getStringValueAt(
								model1,
								column);
						v2 = this.getModelWrapper().getStringValueAt(
								model2,
								column);
					} else {
						v1 = this.getModelWrapper().getValueAt(model1, column);
						v2 = this.getModelWrapper().getValueAt(model2, column);
					}
					
					// Treat nulls as < then non-null
					if (v1 == null) {
						if (v2 == null) {
							result = 0;
						} else {
							result = -1;
						}
					} else if (v2 == null) {
						result = 1;
					} else {
						result = this.sortComparators[counter].compare(v1, v2);
					}
					if (sortOrder == SortOrder.DESCENDING) {
						result *= -1;
					}
				}
			}
			if (result != 0) {
				return result;
			}
		}
		
		if (this.sortComparators[this.sortComparators.length - 1] instanceof RowComparator<?>) {
			result = ((RowComparator<?>) this.sortComparators[this.sortComparators.length - 1]).compareIfEquals(
					model1,
					model2);
			
			if (result != 0) {
				return result;
			}
		}
		
		// If we get here, they're equal. Fallback to model order.
		return model1 - model2;
	}
	
	/**
	 * Whether not we are filtering/sorting.
	 */
	private boolean isTransformed() {
		return (this.viewToModel != null);
	}
	
	/**
	 * Insets new set of entries.
	 * 
	 * @param toAdd
	 *            the Rows to add, sorted
	 * @param current
	 *            the array to insert the items into
	 */
	private void insertInOrder(List<Row> toAdd, Row[] current) {
		int last = 0;
		int index;
		int max = toAdd.size();
		for (int i = 0; i < max; i++) {
			index = Arrays.binarySearch(current, toAdd.get(i));
			if (index < 0) {
				index = -1 - index;
			}
			System.arraycopy(current, last, this.viewToModel, last + i, index
					- last);
			this.viewToModel[index + i] = toAdd.get(i);
			last = index;
		}
		System.arraycopy(
				current,
				last,
				this.viewToModel,
				last + max,
				current.length - last);
	}
	
	/**
	 * Returns true if we should try and optimize the processing of the
	 * <code>TableModelEvent</code>. If this returns false, assume the event was
	 * dealt with and no further processing needs to happen.
	 */
	private boolean shouldOptimizeChange(int firstRow, int lastRow) {
		if (!this.isTransformed()) {
			// Not transformed, nothing to do.
			return false;
		}
		if (!this.sorted || (lastRow - firstRow) > this.viewToModel.length / 10) {
			// We either weren't sorted, or to much changed, sort it all
			this.sort();
			return false;
		}
		return true;
	}
	
	private void rowsInserted0(int firstRow, int lastRow) {
		int[] oldViewToModel = this.getViewToModelAsInts(this.viewToModel);
		int i;
		int delta = (lastRow - firstRow) + 1;
		List<Row> added = new ArrayList<Row>(delta);
		
		// Build the list of Rows to add into added
		for (i = firstRow; i <= lastRow; i++) {
			if (this.include(i)) {
				added.add(new Row(this, i));
			}
		}
		
		// Adjust the model index of rows after the effected region
		int viewIndex;
		for (i = this.modelToView.length - 1; i >= firstRow; i--) {
			viewIndex = this.modelToView[i];
			if (viewIndex != -1) {
				this.viewToModel[viewIndex].modelIndex += delta;
			}
		}
		
		// Insert newly added rows into viewToModel
		if (added.size() > 0) {
			Collections.sort(added);
			Row[] lastViewToModel = this.viewToModel;
			this.viewToModel = new Row[this.viewToModel.length + added.size()];
			this.insertInOrder(added, lastViewToModel);
		}
		
		// Update modelToView
		this.createModelToView(this.getModelWrapper().getRowCount());
		this.setModelToViewFromViewToModel(true);
		
		// Notify of change
		this.fireRowSorterChanged(oldViewToModel);
	}
	
	private void rowsDeleted0(int firstRow, int lastRow) {
		int[] oldViewToModel = this.getViewToModelAsInts(this.viewToModel);
		int removedFromView = 0;
		int i;
		int viewIndex;
		
		// Figure out how many visible rows are going to be effected.
		for (i = firstRow; i <= lastRow; i++) {
			viewIndex = this.modelToView[i];
			if (viewIndex != -1) {
				removedFromView++;
				this.viewToModel[viewIndex] = null;
			}
		}
		
		// Update the model index of rows after the effected region
		int delta = lastRow - firstRow + 1;
		for (i = this.modelToView.length - 1; i > lastRow; i--) {
			viewIndex = this.modelToView[i];
			if (viewIndex != -1) {
				this.viewToModel[viewIndex].modelIndex -= delta;
			}
		}
		
		// Then patch up the viewToModel array
		if (removedFromView > 0) {
			Row[] newViewToModel = new Row[this.viewToModel.length
					- removedFromView];
			int newIndex = 0;
			int last = 0;
			for (i = 0; i < this.viewToModel.length; i++) {
				if (this.viewToModel[i] == null) {
					System.arraycopy(
							this.viewToModel,
							last,
							newViewToModel,
							newIndex,
							i - last);
					newIndex += (i - last);
					last = i + 1;
				}
			}
			System.arraycopy(
					this.viewToModel,
					last,
					newViewToModel,
					newIndex,
					this.viewToModel.length - last);
			this.viewToModel = newViewToModel;
		}
		
		// Update the modelToView mapping
		this.createModelToView(this.getModelWrapper().getRowCount());
		this.setModelToViewFromViewToModel(true);
		
		// And notify of change
		this.fireRowSorterChanged(oldViewToModel);
	}
	
	private void rowsUpdated0(int firstRow, int lastRow) {
		int[] oldViewToModel = this.getViewToModelAsInts(this.viewToModel);
		int i, j;
		int delta = lastRow - firstRow + 1;
		int modelIndex;
		int last;
		int index;
		
		if (this.getRowFilter() == null) {
			// Sorting only:
			
			// Remove the effected rows
			Row[] updated = new Row[delta];
			for (j = 0, i = firstRow; i <= lastRow; i++, j++) {
				updated[j] = this.viewToModel[this.modelToView[i]];
			}
			
			// Sort the update rows
			Arrays.sort(updated);
			
			// Build the intermediary array: the array of
			// viewToModel without the effected rows.
			Row[] intermediary = new Row[this.viewToModel.length - delta];
			for (i = 0, j = 0; i < this.viewToModel.length; i++) {
				modelIndex = this.viewToModel[i].modelIndex;
				if (modelIndex < firstRow || modelIndex > lastRow) {
					intermediary[j++] = this.viewToModel[i];
				}
			}
			
			// Build the new viewToModel
			this.insertInOrder(Arrays.asList(updated), intermediary);
			
			// Update modelToView
			this.setModelToViewFromViewToModel(false);
		} else {
			// Sorting & filtering.
			
			// Remove the effected rows, adding them to updated and setting
			// modelToView to -2 for any rows that were not filtered out
			List<Row> updated = new ArrayList<Row>(delta);
			int newlyVisible = 0;
			int newlyHidden = 0;
			int effected = 0;
			for (i = firstRow; i <= lastRow; i++) {
				if (this.modelToView[i] == -1) {
					// This row was filtered out
					if (this.include(i)) {
						// No longer filtered
						updated.add(new Row(this, i));
						newlyVisible++;
					}
				} else {
					// This row was visible, make sure it should still be
					// visible.
					if (!this.include(i)) {
						newlyHidden++;
					} else {
						updated.add(this.viewToModel[this.modelToView[i]]);
					}
					this.modelToView[i] = -2;
					effected++;
				}
			}
			
			// Sort the updated rows
			Collections.sort(updated);
			
			// Build the intermediary array: the array of
			// viewToModel without the updated rows.
			Row[] intermediary = new Row[this.viewToModel.length - effected];
			for (i = 0, j = 0; i < this.viewToModel.length; i++) {
				modelIndex = this.viewToModel[i].modelIndex;
				if (this.modelToView[modelIndex] != -2) {
					intermediary[j++] = this.viewToModel[i];
				}
			}
			
			// Recreate viewToModel, if necessary
			if (newlyVisible != newlyHidden) {
				this.viewToModel = new Row[this.viewToModel.length
						+ newlyVisible
						- newlyHidden];
			}
			
			// Rebuild the new viewToModel array
			this.insertInOrder(updated, intermediary);
			
			// Update modelToView
			this.setModelToViewFromViewToModel(true);
		}
		// And finally fire a sort event.
		this.fireRowSorterChanged(oldViewToModel);
	}
	
	private void checkColumn(int column) {
		if (column < 0 || column >= this.getModelWrapper().getColumnCount()) {
			throw new IndexOutOfBoundsException(
					"column beyond range of TableModel");
		}
	}
	
	/**
	 * <code>DefaultRowSorter.ModelWrapper</code> is responsible for providing
	 * the data that gets sorted by <code>DefaultRowSorter</code>. You normally
	 * do not interact directly with <code>ModelWrapper</code>. Subclasses of
	 * <code>DefaultRowSorter</code> provide an implementation of
	 * <code>ModelWrapper</code> wrapping another model. For example,
	 * <code>TableRowSorter</code> provides a <code>ModelWrapper</code> that
	 * wraps a <code>TableModel</code>.
	 * <p>
	 * <code>ModelWrapper</code> makes a distinction between values as
	 * <code>Object</code>s and <code>String</code>s. This allows
	 * implementations to provide a custom string converter to be used instead
	 * of invoking <code>toString</code> on the object.
	 * 
	 * @param <M>
	 *            the type of the underlying model
	 * @param <I>
	 *            the identifier supplied to the filter
	 * @since 1.6
	 * @see RowFilter
	 * @see RowFilter.Entry
	 */
	protected abstract static class ModelWrapper<M, I> {
		
		/**
		 * Creates a new <code>ModelWrapper</code>.
		 */
		protected ModelWrapper() {}
		
		/**
		 * Returns the underlying model that this <code>Model</code> is
		 * wrapping.
		 * 
		 * @return the underlying model
		 */
		public abstract M getModel();
		
		/**
		 * Returns the number of columns in the model.
		 * 
		 * @return the number of columns in the model
		 */
		public abstract int getColumnCount();
		
		/**
		 * Returns the number of rows in the model.
		 * 
		 * @return the number of rows in the model
		 */
		public abstract int getRowCount();
		
		/**
		 * Returns the value at the specified index.
		 * 
		 * @param row
		 *            the row index
		 * @param column
		 *            the column index
		 * @return the value at the specified index
		 * @throws IndexOutOfBoundsException
		 *             if the indices are outside the range of the model
		 */
		public abstract Object getValueAt(int row, int column);
		
		/**
		 * Returns the value as a <code>String</code> at the specified index.
		 * This implementation uses <code>toString</code> on the result from
		 * <code>getValueAt</code> (making sure to return an empty string for
		 * null values). Subclasses that override this method should never
		 * return null.
		 * 
		 * @param row
		 *            the row index
		 * @param column
		 *            the column index
		 * @return the value at the specified index as a <code>String</code>
		 * @throws IndexOutOfBoundsException
		 *             if the indices are outside the range of the model
		 */
		public String getStringValueAt(int row, int column) {
			Object o = this.getValueAt(row, column);
			if (o == null) {
				return "";
			}
			String string = o.toString();
			if (string == null) {
				return "";
			}
			return string;
		}
		
		/**
		 * Returns the identifier for the specified row. The return value of
		 * this is used as the identifier for the <code>RowFilter.Entry</code>
		 * that is passed to the <code>RowFilter</code>.
		 * 
		 * @param row
		 *            the row to return the identifier for, in terms of the
		 *            underlying model
		 * @return the identifier
		 * @see RowFilter.Entry#getIdentifier
		 */
		public abstract I getIdentifier(int row);
	}
	
	/**
	 * RowFilter.Entry implementation that delegates to the ModelWrapper.
	 * getFilterEntry(int) creates the single instance of this that is passed to
	 * the Filter. Only call getFilterEntry(int) to get the instance.
	 */
	private class FilterEntry extends RowFilter.Entry<M, I> {
		
		/**
		 * The index into the model, set in getFilterEntry
		 */
		int modelIndex;
		
		@Override
		public M getModel() {
			return DefaultRowSorter.this.getModelWrapper().getModel();
		}
		
		@Override
		public int getValueCount() {
			return DefaultRowSorter.this.getModelWrapper().getColumnCount();
		}
		
		@Override
		public Object getValue(int index) {
			return DefaultRowSorter.this.getModelWrapper().getValueAt(
					this.modelIndex,
					index);
		}
		
		@Override
		public String getStringValue(int index) {
			return DefaultRowSorter.this.getModelWrapper().getStringValueAt(
					this.modelIndex,
					index);
		}
		
		@Override
		public I getIdentifier() {
			return DefaultRowSorter.this.getModelWrapper().getIdentifier(
					this.modelIndex);
		}
	}
	
	/**
	 * Row is used to handle the actual sorting by way of Comparable. It will
	 * use the sortKeys to do the actual comparison.
	 */
	// NOTE: this class is static so that it can be placed in an array
	private static class Row implements Comparable<Row> {
		
		private DefaultRowSorter sorter;
		int modelIndex;
		
		public Row(DefaultRowSorter sorter, int index) {
			this.sorter = sorter;
			this.modelIndex = index;
		}
		
		@Override
		public int compareTo(Row o) {
			return this.sorter.compare(this.modelIndex, o.modelIndex);
		}
	}
}
