package gui.components.actionpanels;

import com.influxdb.query.FluxColumn;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import gui.common.EnhancedTable;
import gui.common.TableRecord;


import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class QueryResultTable extends EnhancedTable {

	private static final long serialVersionUID = 1L;
	private QueryResultTableModel tableModel;
	private int numOfrows;
	private ArrayList<String> colNameList = new ArrayList<>();

	/**
	 * One FluxTable is one table
	 * @param serie
	 */
	public QueryResultTable(FluxTable serie) {
		setUpUI();
		numOfrows = 1;
		List<FluxColumn> columNames = serie.getColumns();
		for(FluxColumn col:columNames)
			colNameList.add(col.getLabel());

		tableModel = new QueryResultTableModel(columNames);
		for(FluxRecord record:serie.getRecords()){
			QueryResultTableRecord tableRecord = new QueryResultTableRecord(numOfrows, record);
			tableModel.addTableRecord(tableRecord);
			numOfrows++;
		}
		setModel(tableModel);
	}

	private void setUpUI() {
		setGridColor(Color.LIGHT_GRAY);
		setShowHorizontalLines(true);
		setShowVerticalLines(true);
		JTableHeader header = getTableHeader();
		header.setBackground(Color.LIGHT_GRAY);
		setTableHeader(header);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		Component comp = super.prepareRenderer(renderer, row, col);
		((JLabel) comp).setHorizontalAlignment(JLabel.LEFT);
		return comp;
	}

	public int getNumOfRows() {
		return numOfrows - 1;
	}

	class QueryResultTableRecord implements TableRecord {

		private FluxRecord tabelRecordData;

		public QueryResultTableRecord(int recordNumber, FluxRecord data) {
			tabelRecordData = data;
		}

		@Override
		public Object getFieldAt(int fieldIndex) {
			return tabelRecordData.getValueByIndex(fieldIndex);
		}

		public Object getFiledbyKey(String key){
			return tabelRecordData.getValueByKey(key);
		}

		@Override
		public int getNumberOfField() {
			return tabelRecordData.getValues().size();
		}

		@Override
		public void setFieldAt(int fieldIndex, Object field) {
			//tabelRecordData.set(fieldIndex, field);
			//tabelRecordData.get(fieldIndex)
			tabelRecordData.getValues().values().toArray()[fieldIndex] = field;
		}
	}

	class QueryResultTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 8919352997347026280L;
		private Vector<Object> rowsVector;
		private ArrayList<String> columnNames;

		public QueryResultTableModel(List<FluxColumn> nameOfColums) {
			rowsVector = new Vector<Object>();
			this.columnNames = new ArrayList<String>();
			this.columnNames.add("#");
			for (FluxColumn value : nameOfColums) {
				this.columnNames.add(value.getLabel());
			}
		}

		public String getColumnName(int column) {

			return (String) columnNames.get(column);
		}

		public int getColumnCount() {
			return columnNames.size();
		}

		public void addTableRecord(TableRecord record) {
			rowsVector.add(record);
		}

		public TableRecord getValueAtRow(int rowIndex) {
			return (TableRecord) rowsVector.get(rowIndex);
		}

		public void removeRow(int row) {
			if (rowsVector.isEmpty()) {
				return;
			} else if (rowsVector.size() >= row) {
				rowsVector.remove(row);
			}
		}

		public boolean hasTableRecord(TableRecord record) {
			if (rowsVector.contains(record))
				return true;
			else
				return false;
		}

		public void removeAllRows() {
			if (rowsVector.isEmpty()) {
				return;
			} else {
				rowsVector.removeAllElements();
			}
		}

		public Vector<Object> getAllRowsVector() {
			return rowsVector;
		}

		public void addAllRows(Vector<Object> alllRows) {
			if (alllRows != null) {
				rowsVector = alllRows;
			} else
				removeAllRows();
		}

		public void removeRow(TableRecord record) {
			if (rowsVector.isEmpty()) {
				return;
			} else if (rowsVector.contains(record)) {
				rowsVector.remove(record);
			}
		}

		public void changeTableRecord(TableRecord record, int rowIndex) {
			rowsVector.setElementAt(record, rowIndex);
		}

		public int getRowCount() {
			return rowsVector.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (((TableRecord) rowsVector.get(rowIndex)) != null) {
				String key = columnNames.get(columnIndex);
				return ((TableRecord) rowsVector.get(rowIndex)).getFiledbyKey(key);
			} else {
				return "";
			}
		}
	}
}
