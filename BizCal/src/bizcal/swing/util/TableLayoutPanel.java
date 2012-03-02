/*******************************************************************************
 * Bizcal is a component library for calendar widgets written in java using swing.
 * Copyright (C) 2007  Frederik Bertilsson 
 * Contributors:       Martin Heinemann martin.heinemann(at)tudor.lu
 * 
 * http://sourceforge.net/projects/bizcal/
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 * 
 *******************************************************************************/
package bizcal.swing.util;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;
import info.clearthought.layout.TableLayoutConstraints;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class TableLayoutPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public static final double FILL = TableLayout.FILL;
	public static final double PREFERRED = TableLayout.PREFERRED;
	public static final int RIGHT = TableLayout.RIGHT;
	public static final int LEFT = TableLayout.LEFT;
	public static final int TOP = TableLayout.TOP;
	public static final int BOTTOM = TableLayout.BOTTOM;
	public static final int CENTER = TableLayout.CENTER;
	public static final int FULL = TableLayout.FULL;
	
	private TableLayout _layout;
	private java.util.List<Column> _columns = new ArrayList<Column>();
	private java.util.List<Row> _rows = new ArrayList<Row>();
	
	public TableLayoutPanel() {
		this._layout = new TableLayout();
		this.setLayout(this._layout);
	}
	
	public Column createColumn(double size) {
		Column col = new Column(this, size);
		this._columns.add(col);
		return col;
	}
	
	public Column createSpaceColumn(double size) {
		Column col = new Column(this, size);
		return col;
	}
	
	public Column createColumn() {
		Column col = new Column(this, TableLayoutConstants.PREFERRED);
		this._columns.add(col);
		return col;
	}
	
	public Row createRow(double size) {
		Row row = new Row(this, size);
		this._rows.add(row);
		return row;
	}
	
	public Row createRow() {
		Row row = new Row(this, TableLayoutConstants.PREFERRED);
		this._rows.add(row);
		return row;
	}
	
	/*
	 * public void deleteRow(Row row)
	 * {
	 * int rowNo = row.getRowNumber();
	 * _layout.deleteRow(rowNo);
	 * _rows.remove(row);
	 * int noOfRows = _layout.getRow().length;
	 * for (int i = rowNo; i<noOfRows; i++) {
	 * ((Row)_rows.get(i)).setRowNumber(i);
	 * }
	 * }
	 */
	public void deleteRows() {
		while (this._layout.getRow().length > 0) {
			this._layout.deleteRow(this._layout.getRow().length - 1);
		}
		this._rows.clear();
	}
	
	public void deleteColumns() {
		while (this._layout.getColumn().length > 0) {
			this._layout.deleteColumn(this._layout.getColumn().length - 1);
		}
		this._columns.clear();
	}
	
	public void clear() {
		this.removeAll();
		this.invalidate();
	}
	
	public class Row {
		
		private TableLayoutPanel _table;
		private int _rowNo;
		private List cells = new ArrayList();
		
		private Row(TableLayoutPanel table, double size) {
			this._table = table;
			this._rowNo = table._layout.getNumRow();
			table._layout.insertRow(this._rowNo, size);
		}
		
		public Cell createCell(Component component) {
			Cell cell = this.createCell(component, FULL, LEFT);
			this.cells.add(cell);
			return cell;
		}
		
		public Cell createCell(Component component, int vAlign, int hAlign) {
			Cell cell = new Cell(
					this.getNextColumn(),
					this,
					component,
					vAlign,
					hAlign);
			this.cells.add(cell);
			return cell;
		}
		
		public Cell createCell() {
			Cell cell = new Cell(this.getNextColumn(), this);
			this.cells.add(cell);
			return cell;
		}
		
		public int getRowNumber() {
			return this._rowNo;
		}
		
		public void setRowNumber(int rowNo) {
			this._rowNo = rowNo;
		}
		
		private Column getNextColumn() {
			if (this.cells.isEmpty())
				return TableLayoutPanel.this._columns.get(0);
			Cell cell = (Cell) this.cells.get(this.cells.size() - 1);
			int i = cell.c.col2;
			return TableLayoutPanel.this._columns.get(i + 1);
		}
	}
	
	public class Column {
		
		private int _colNo;
		
		public Column(TableLayoutPanel table, double size) {
			this._colNo = table._layout.getNumColumn();
			table._layout.insertColumn(this._colNo, size);
		}
		
	}
	
	public class Cell {
		
		private TableLayoutConstraints c = new TableLayoutConstraints();
		private TableLayoutPanel _table;
		private Component _comp;
		
		private Cell(
				Column col,
				Row row,
				Component component,
				int vAlign,
				int hAlign) {
			this.c.col1 = this.c.col2 = col._colNo;
			this.c.row1 = this.c.row2 = row._rowNo;
			this.c.vAlign = vAlign;
			this.c.hAlign = hAlign;
			this._table = row._table;
			if (component != null)
				this.put(component);
		}
		
		private Cell(Column col, Row row) {
			this(col, row, null, FULL, LEFT);
		}
		
		public void setVerticalAlignment(int align) {
			this.c.vAlign = align;
		}
		
		public void setHorizontalAlignment(int align) {
			this.c.hAlign = align;
		}
		
		public void setColumnSpan(int span) {
			this.c.col2 = this.c.col1 + span - 1;
		}
		
		public void setRowSpan(int span) {
			this.c.row2 = this.c.row1 + span - 1;
		}
		
		public void put(Component component) {
			if (this._comp != null) {
				this._table.remove(this._comp);
				this._table.revalidate();
			}
			this._comp = component;
			this._table.add(component, this.c);
		}
		
	}
	
}
