package it.unina.egc.DBInterface.Utils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.jena.ext.com.google.common.base.Stopwatch;


public class DBpediaMappingBasedPropertiesExporter 
{
		
	static Stopwatch watch = Stopwatch.createUnstarted();
	static final String directory = "MyDatabases/DBpedia";
	static final String  DBpediaTypesPropertiesFile = "knowledge_base/instance_types_en.nt";
	static final String DBPediaMappingBasedPropertiesFile = "knowledge_base/mappingbased_properties_en.nt";
	static final String TypePropertiesLinks = "export/TypePropertiesLinks.csv";
	static final String DBpediaRDFResourceDesc = "export/DBpediaRDFResourceDesc.csv";
	
	static TreeMap<String, Integer> resourcesMap = new TreeMap<String, Integer>();
	
	public static void main(String[] args) 
	{
		
		loadDBpedia();
	}
		
	static void loadDBpedia()
	{
		try {
		    
			//FileWriter DBpediaRDFResourceDescWriter = new FileWriter(DBpediaRDFResourceDesc);
			//FileWriter TypePropertiesLinksWriter = new FileWriter(TypePropertiesLinks);
			
			BufferedReader br = new BufferedReader(new FileReader(DBpediaTypesPropertiesFile));
			BufferedReader br2 = new BufferedReader(new FileReader(DBPediaMappingBasedPropertiesFile));
			
			//DBpediaRDFResourceDescWriter.write("id,URI\n");
			//TypePropertiesLinksWriter.write("Sub,Obj\n");
			
			String line;
		    String[] triple = new String[3];
		    
		    int i = 0;
		    
		    while ((line = br2.readLine()) != null)
		    {
		       // process the line.
		    	
		    	if (!line.startsWith("#"))
		    	{
		    	
		    		System.out.println(line);
			    	/*line = line.replaceAll("<", "").replaceAll(">", "");
			    	triple = line.split("\\s");
			    	
			    	Integer s_index =  resourcesMap.get(triple[0]);
			    	Integer o_index =  resourcesMap.get(triple[2]);
			    	
			    	if (s_index == null)
			    	{
			    		resourcesMap.put(triple[0], i);
			    					    		
			    		DBpediaRDFResourceDescWriter.write(i + "," + triple[0] + "\n");
			    		TypePropertiesLinksWriter.write(i + ",");
			    		
			    		i++;
			    		
			    		if (i%100000 == 0)
				    		System.out.println("Mapped " + i + " resources");
			    	}
			    	else
			    	{
			    		TypePropertiesLinksWriter.write(s_index.intValue() + ",");
			    	}
			    	
			    	if (o_index == null)
			    	{
			    		resourcesMap.put(triple[2], i);
			    					    		
			    		DBpediaRDFResourceDescWriter.write(i + "," + triple[2] + "\n");
			    		TypePropertiesLinksWriter.write(i + "\n");
			    		
			    		i++;
			    		if (i%100000 == 0)
				    		System.out.println("Mapped " + i + " resources");
			    	
			    	}	
			    	else
			    	{
			    		TypePropertiesLinksWriter.write(o_index.intValue() + "\n");
			    	}*/
			    }
		    }
		    
		    br2.close();
		    
		    //DBpediaRDFResourceDescWriter.flush();
		    //TypePropertiesLinksWriter.flush();
		    
		    //DBpediaRDFResourceDescWriter.close();
		    //TypePropertiesLinksWriter.close();
		    
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
