package gui.components.actionpanels;

import gui.common.EnhancedTable;
import gui.common.EnhancedTableModel;

import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;


public class SeriesTable extends EnhancedTable{
	
	private static final long serialVersionUID = 1L;

	public SeriesTable(){
		
		setGridColor(Color.LIGHT_GRAY);
		setShowHorizontalLines(true);
		setShowVerticalLines(true);
		JTableHeader header = getTableHeader();
		header.setBackground(Color.LIGHT_GRAY);
		setTableHeader(header);
		ArrayList<String> nameOfColums = new ArrayList<String> ();
		nameOfColums.add("#");
		nameOfColums.add("Host");
		EnhancedTableModel tableModel = new EnhancedTableModel(nameOfColums);
		setModel(tableModel);
	}
}