package it.unina.egc.DBInterface.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

public class MainPanel extends JPanel 
{
	private String[] columnNames = {"Selection", "properties_labels", "properties_uris", "properties_range_labels", "properties_range_URIs"};
	private String[] csvColumnNames;
	private Object[][] headersData;
	private String[][] csvData;
	
	private boolean DEBUG = false;
	
	public static File  DBpediaTablesFile;
	
	JTable headersTable, csvTable;
	JScrollPane headersScrollPane, csvScrollPane;
	
	int base_count = 0;
	
	boolean tablesAlreadyCreated = false;
	public MainPanel() 
	{
		 super(new GridLayout(2,0));
		 		
	}
	
	public void createTable()
	{
		if (tablesAlreadyCreated)
		{
			removeAll();
			repaint();
			revalidate();
		}
		
		tablesAlreadyCreated = true;
		
		headersTable = new JTable(new HeadersTableModel());
	        headersTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
	        headersTable.setFillsViewportHeight(true);
	        
	        //Create the scroll pane and add the table to it.
	        headersScrollPane = new JScrollPane(headersTable);
	        headersScrollPane.setBorder(new TitledBorder(DBpediaTablesFile + " headers: "));
	        
	     
	    
	     JTable rowTable = new RowNumberTable(headersTable);
	     headersScrollPane.setRowHeaderView(rowTable);
	     headersScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
	         rowTable.getTableHeader());
	     
	     
	     ///////////////////////////////////////////
	     
	     csvTable = new JTable(new CSVFileTableModel());
	     csvTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	     csvTable.setPreferredScrollableViewportSize(new Dimension(50, 70));
	     //csvTable.setFillsViewportHeight(true);
	        
	        //Create the scroll pane and add the table to it.
	        csvScrollPane = new JScrollPane(csvTable);
	        csvScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	        csvScrollPane.setBorder(new TitledBorder(DBpediaTablesFile + " content: "));
	        
	     
	    
	     JTable rowTable1 = new RowNumberTable(csvTable);
	     csvScrollPane.setRowHeaderView(rowTable1);
	     csvScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
	    		 rowTable1.getTableHeader());
	     
	      
	 
	        //Add the scroll pane to this panel.
	        add(headersScrollPane);
	        add(csvScrollPane);
	}
	
	public void update() throws Exception
	{
		headersTable.repaint();
		csvTable.repaint();
		 csvScrollPane.setBorder(new TitledBorder(DBpediaTablesFile + " content: "));
		 headersScrollPane.setBorder(new TitledBorder(DBpediaTablesFile + " headers: "));
		 
		 
	}
	
	public void setColumnsAndData() throws Exception
	{
		BufferedReader br1 = new BufferedReader(new FileReader(DBpediaTablesFile));
		
		String line1, line2, line3, line4, line;
		
		line1 = br1.readLine();
		line2 = br1.readLine();
		line3 = br1.readLine();
		line4 = br1.readLine();
		
		System.out.println(line1);
		System.out.println(line2);
		System.out.println(line3);
		System.out.println(line4);
		
		String[] properties_labels = line1.split(","); 			// The first header contains the properties labels.
		String[] properties_uris = line2.split(",");  			// The second header contains the properties URIs
		String[] properties_range_labels = line3.split(",");	 // The third header contains the properties range labels.
		String[] properties_range_URIs = line4.split(",");		 // The fourth header contains the properties range URIs.
		
		
		headersData = new Object[properties_labels.length][5];
		csvData = new String[1000][properties_labels.length];
		csvColumnNames = new String[properties_labels.length];
		
		for (int i = 0; i < properties_labels.length; i++)
		{
			headersData[i][0] = new Boolean(false);
			headersData[i][1] = properties_labels[i];
			headersData[i][2] = properties_uris[i];
			headersData[i][3] = properties_range_labels[i];
			headersData[i][4] = properties_range_URIs[i];
			
			csvColumnNames[i] = properties_labels[i];
		}
		
	    for (int i = 0; i < 1000; i++)
		{
	    	line = br1.readLine();
	    	csvData[i] = line.split(","); 
	    }
		br1.close();
	}
	
	class HeadersTableModel extends AbstractTableModel {

        public int getColumnCount() {
            return columnNames.length;
        }
 
        public int getRowCount() {
            return headersData.length;
        }
 
        public String getColumnName(int col) {
            return columnNames[col];
        }
 
        public Object getValueAt(int row, int col) {
            return headersData[row][col];
        }
 
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
 
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col == 0) {
                return true;
            } else {
                return false;
            }
        }
 
        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            }
 
            headersData[row][col] = value;
            fireTableCellUpdated(row, col);
 
            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }
 
        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
 
            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + headersData[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }
	
	class CSVFileTableModel extends AbstractTableModel {

        public int getColumnCount() {
            return csvColumnNames.length;
        }
 
        public int getRowCount() {
            return csvData.length;
        }
 
        public String getColumnName(int col) {
            return csvColumnNames[col];
        }
 
        public Object getValueAt(int row, int col) {
            return csvData[row][col];
        }
 
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
 
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col == 0) {
                return true;
            } else {
                return false;
            }
        }
 
        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            }
 
            headersData[row][col] = value;
            fireTableCellUpdated(row, col);
 
            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }
 
        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
 
            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + headersData[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }
}
