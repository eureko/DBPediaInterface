package it.unina.egc.DBInterface.core;



import it.unina.egc.DBInterface.gui.MainPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class DBPediaInterfaceGUI {

	static MainPanel mainPanel = new MainPanel();
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() 
		{
		      public void run() {
		        createGUI();
		      }
		});

	}
	
protected static void createGUI() {
		
		
	final JFileChooser fc = new JFileChooser();
	JFrame frame = new JFrame();
		
		JMenu fileMenu = new JMenu("File");
		JMenu infoMenu = new JMenu("?");
		
		
		JMenuItem openItem = new JMenuItem("Open...");
		JMenuItem compareItem = new JMenuItem("Match...");
		fileMenu.add(openItem);
		fileMenu.add(compareItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(infoMenu);
		 
		frame.setJMenuBar(menuBar);
		
		frame.setContentPane(mainPanel);
		frame.setTitle("DBpedia csv file GUI interface");
		
		
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setVisible(true);
		frame.repaint();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		openItem.addActionListener(new ActionListener() 
		{
			
			public void actionPerformed(ActionEvent actionEvent) {
	        	//Handle open button action.
	           int returnVal = fc.showOpenDialog(mainPanel);
	           
	           if (returnVal == JFileChooser.APPROVE_OPTION) {
	              
	        	  try
	        	  {
		        	  mainPanel.DBpediaTablesFile = fc.getSelectedFile();
		        	  mainPanel.setColumnsAndData();
		              mainPanel.createTable();
		              mainPanel.update();
		              mainPanel.repaint();
		              mainPanel.revalidate();
	        	  }
	        	  catch(Exception ex)
	        	  {
	        		  ex.printStackTrace();
	        	  }
	               
	           } 
	        }
	    });
				
	}

}
