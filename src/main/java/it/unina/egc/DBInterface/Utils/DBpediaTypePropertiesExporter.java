package it.unina.egc.DBInterface.Utils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.base.Stopwatch;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.tdb.TDBFactory;


public class DBpediaTypePropertiesExporter 
{
		
	static Stopwatch watch = Stopwatch.createUnstarted();
	static final String directory = "MyDatabases/DBpedia";
	static final String  DBpediaTypesPropertiesFile = "knowledge_base/instance_types_en.nt";
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
		    
			FileWriter DBpediaRDFResourceDescWriter = new FileWriter(DBpediaRDFResourceDesc);
			FileWriter TypePropertiesLinksWriter = new FileWriter(TypePropertiesLinks);
			
			BufferedReader br = new BufferedReader(new FileReader(DBpediaTypesPropertiesFile));
			
			DBpediaRDFResourceDescWriter.write("id,URI\n");
			TypePropertiesLinksWriter.write("Sub,Obj\n");
			
			String line;
		    String[] triple = new String[3];
		    
		    int i = 0;
		    
		    while ((line = br.readLine()) != null)
		    {
		       // process the line.
		    	
		    	if (!line.startsWith("#"))
		    	{
		    	
			    	line = line.replaceAll("<", "").replaceAll(">", "");
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
			    	}
			    }
		    }
		    
		    br.close();
		    
		    DBpediaRDFResourceDescWriter.flush();
		    TypePropertiesLinksWriter.flush();
		    
		    DBpediaRDFResourceDescWriter.close();
		    TypePropertiesLinksWriter.close();
		    
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
