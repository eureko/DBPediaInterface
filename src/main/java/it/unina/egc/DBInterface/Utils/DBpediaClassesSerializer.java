package it.unina.egc.DBInterface.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.apache.jena.ext.com.google.common.base.Stopwatch;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

public class DBpediaClassesSerializer 
{
		static FileReader fileReader = null;	
		static final String DBpedia_Classes_export = "export/DBpedia_Classes_export";
		static Stopwatch watch = Stopwatch.createUnstarted();
		
		static Vector<String> classesVector = new Vector<String>();
		
		public static void main(String[] args) 
		{			
			try (BufferedReader br = new BufferedReader(new FileReader(DBpedia_Classes_export))) {
			    String line;
			    while ((line = br.readLine()) != null) 
			    {
			       
			    	if (!line.startsWith("#"))
			    		classesVector.add(line);
			    }
			    
			    //Serialize classVector to file
			    
			    Collections.sort(classesVector);
			    
			   //Iterator<String> classesIter = classesVector.iterator();
			   
			   //while (classesIter.hasNext())
				 //  System.out.println(classesIter.next());
			   
		        ObjectOutputStream oos = new ObjectOutputStream( 
		                               new FileOutputStream(new File("export/DBpediaOrderedClasses.obj")));

		        oos.writeObject(classesVector);
		        // close the writing.
		        oos.close();
		        
		        ObjectInputStream ois = new ObjectInputStream(                                 
                        new FileInputStream(new File("export/DBpediaOrderedClasses.obj"))) ;
		       
		        Vector<String> classesVectorSerialized = (Vector<String>)ois.readObject();
		        
		      Iterator<String> classesIter = classesVectorSerialized.iterator();
				   
				   while (classesIter.hasNext())
					   System.out.println(classesIter.next());
		        
		      
				   
				   ois.close(); 
			    
			    
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
}