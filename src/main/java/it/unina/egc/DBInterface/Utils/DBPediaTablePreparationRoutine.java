package it.unina.egc.DBInterface.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.TreeMap;

public class DBPediaTablePreparationRoutine 
{
	static final String  DBpediaTalesFile1 = "knowledge_base/knowledgeBase/Person.csv";
	
	static final String File1URL = "export/Book.csv";
	
	TreeMap<String, Integer> map = new TreeMap<String, Integer>();
	
	public static void main(String[] args) 
	{
		try
		{
									
			
			System.out.println("QUI");
			BufferedReader br1 = new BufferedReader(new FileReader(DBpediaTalesFile1));
						
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
			
			while ((line = br1.readLine()) != null)
		    {
				//System.out.println(line);
		    }
			
			br1.close();
			
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
