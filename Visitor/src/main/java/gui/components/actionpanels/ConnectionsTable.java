package gui.components.actionpanels;

import gui.common.EnhancedTable;
import gui.common.EnhancedTableModel;

import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;


public class ConnectionsTable extends EnhancedTable {
	

	private static final long serialVersionUID = 8361602787427930500L;

	public ConnectionsTable(){
		setGridColor(Color.LIGHT_GRAY);
		setShowHorizontalLines(true);
		setShowVerticalLines(true);
		JTableHeader header = getTableHeader();
		header.setBackground(Color.LIGHT_GRAY);
		setTableHeader(header);
		ArrayList<String> nameOfColums = new ArrayList<String> ();
		nameOfColums.add("Name");
		nameOfColums.add("Adress");
		EnhancedTableModel tableModel = new EnhancedTableModel(nameOfColums);
		setModel(tableModel);
	}
}