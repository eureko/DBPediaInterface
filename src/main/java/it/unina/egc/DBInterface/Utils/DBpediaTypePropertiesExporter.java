package it.unina.egc.DBInterface.Utils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.jena.ext.com.google.common.base.Stopwatch;


public class DBpediaTypePropertiesExporter 
{
	
	static final String rdf_type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	static Stopwatch watch = Stopwatch.createUnstarted();
	static final String directory = "MyDatabases/DBpedia";
	static final String  DBpediaTypesPropertiesFile = "knowledge_base/instance_types_en.nt";
	static final String DBPediaMappingBasedPropertiesFile = "knowledge_base/mappingbased_properties_en.nt";
	static final String DBPediaSpecificMappingBasedPropertiesFile = "knowledge_base/specific_mappingbased_properties_en.nt";
	static final String TypePropertiesLinks = "export/TypePropertiesLinks.csv";
	static final String PropertiesLinks = "export/PropertiesLinks.csv";
	static final String DBpediaRDFResourceDesc = "export/DBpediaRDFResourceDesc.csv";
	
	static TreeMap<String, Integer> resourcesMap = new TreeMap<String, Integer>();
	
	static Vector<String> nameSpaces = new Vector<String>();
	
	public static void main(String[] args) 
	{
		
		loadDBpedia();
	}
		
	static void loadDBpedia()
	{
		try {
		    
			FileWriter DBpediaRDFResourceDescWriter = new FileWriter(DBpediaRDFResourceDesc);
			FileWriter TypePropertiesLinksWriter = new FileWriter(TypePropertiesLinks);
			FileWriter PropertiesLinksWriter = new FileWriter(PropertiesLinks);
			
			BufferedReader br = new BufferedReader(new FileReader(DBpediaTypesPropertiesFile));
			BufferedReader br2 = new BufferedReader(new FileReader(DBPediaMappingBasedPropertiesFile));
			//BufferedReader br3 = new BufferedReader(new FileReader(DBPediaSpecificMappingBasedPropertiesFile));
			
			DBpediaRDFResourceDescWriter.write("id,URI\n");
			TypePropertiesLinksWriter.write("Sub,Pred,Obj\n");
			PropertiesLinksWriter.write("Sub,Pred,Obj\n");
			
			String line;
		    String[] triple = new String[3];
		    
		    int i = 1; // id = 0 is for rdf_type property
		    DBpediaRDFResourceDescWriter.write("0," + rdf_type + "\n");
		    
		    int count = 0;
		    
		    while ((line = br.readLine()) != null)
		    {
		       // process the line.
		    	
		    	if (!line.startsWith("#"))
		    	{
		    	
			    	line = line.replaceAll("<", "").replaceAll(">", "");
			    	triple = line.split("\\s");
			    	
			    	if (triple[2].contains("\""))
			    		triple[2] = triple[2].replaceAll("\"", "");
			    	
			    	Integer s_index =  resourcesMap.get(triple[0]);
			    	//Integer p_index = resourcesMap.get(triple[1]);
			    	Integer o_index =  resourcesMap.get(triple[2]);
			    	
			    	
			    		
			    	
			    	addNameSpace(triple[0]); 
			    	//addNameSpace(triple[2]);
			    	
			    	
			    	int s_id = s_index == null ? i++ : s_index.intValue();
			    	if (s_index == null)
			    	{
			    		resourcesMap.put(triple[0], s_id);
			    		DBpediaRDFResourceDescWriter.write(s_id+","+triple[0] +"\n");	
			    	}
			    	
			    	
			    	int o_id = o_index == null ? i++ : o_index.intValue();
			    	
			    	if (o_index == null)
			    	{
			    		resourcesMap.put(triple[2], o_id);
			    		DBpediaRDFResourceDescWriter.write(o_id+","+triple[2] + "\n");
			    	}
			    	
			    		
			    	TypePropertiesLinksWriter.write(s_id + ",0," + o_id + "\n");
			    	
			    	if (count%100000==0)
			    		System.out.println(count + ": " + triple[0] + " > " +triple[1] + " > " + triple[2]);
			    	
			    	count++;
			    }
		    }
		    
		    while ((line = br2.readLine()) != null)
		    {
		    	// process the line for the DBPedia Mapping-Based Properties
		    	
		    	if (!line.startsWith("#"))
		    	{
		    	
			    	line = line.replaceAll("<", "").replaceAll(">", "");
			    	triple = line.split("\\s");
			    	
			    	if (triple[2].contains("\""))
			    		triple[2] = triple[2].replaceAll("\"", "");
			    	
			    	addNameSpace(triple[0]); 
			    	addNameSpace(triple[1]);
			    	//addNameSpace(triple[2]);
			    	
			    	
			    	Integer s_index =  resourcesMap.get(triple[0]);
			    	Integer p_index = resourcesMap.get(triple[1]);
			    	Integer o_index =  resourcesMap.get(triple[2]);
			    	
			    	
			    	
			    	int s_id = s_index == null ? i++ : s_index.intValue();
			    	if (s_index == null)
			    	{
			    		resourcesMap.put(triple[0], s_id);
			    		DBpediaRDFResourceDescWriter.write(s_id+","+triple[0] +"\n");	
			    	}
			    				
			    	int p_id = p_index == null ? i++ : p_index.intValue();
			    	if (p_index == null)
			    	{
			    		resourcesMap.put(triple[1], p_id);
			    		DBpediaRDFResourceDescWriter.write(p_id+","+triple[1] +"\n");
			    	}
			    	
			    	int o_id = o_index == null ? i++ : o_index.intValue();
			    	if (o_index == null)
			    	{
			    		resourcesMap.put(triple[2], o_id);
			    		DBpediaRDFResourceDescWriter.write(o_id+","+triple[2] + "\n");
			    	}
			    	
			    	PropertiesLinksWriter.write(s_id + "," + p_id + "," + o_id +"\n");
			    	
			    	if (count%100000==0)
			    		System.out.println(count + ": " + triple[0] + " > " +triple[1] + " > " + triple[2]);
			    	
			    	count++;
			    }
		    }
		    
		    for (String s:nameSpaces)
		    {
		    	System.out.println(s);
		    }
		    
		    System.out.println("size: " + nameSpaces.size());
		    
		    br.close();
		    br2.close();
		    
		    DBpediaRDFResourceDescWriter.flush();
		    TypePropertiesLinksWriter.flush();
		    PropertiesLinksWriter.flush();
		    
		    DBpediaRDFResourceDescWriter.close();
		    TypePropertiesLinksWriter.close();
		    PropertiesLinksWriter.close(); 
		    
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void addNameSpace(String uri) 
	{
		try
		{
			URL url = new URL(uri);
			//url.getPath();
			
			String nameSpace = url.getHost();
			
			if (!nameSpaces.contains(nameSpace))
				nameSpaces.add(nameSpace);
		}
		catch(Exception ex){}
				
	}
}
