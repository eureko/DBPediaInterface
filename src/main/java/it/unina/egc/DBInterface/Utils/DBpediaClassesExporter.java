package it.unina.egc.DBInterface.Utils;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.jena.ext.com.google.common.base.Stopwatch;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

public class DBpediaClassesExporter 
{
		static FileWriter fileWriter = null;	
		static final String  DBpediaTBOXFile = "knowledge_base/dbpedia_2014.owl";
		static final String TBOXVocabulary = "export/DBpedia_Classes_export";
		static Stopwatch watch = Stopwatch.createUnstarted();
		
		static final String DBpedia_BaseURI =  "http://dbpedia.org/ontology/";
		
		public static void main(String[] args) 
		{			
			try 
			{
				OntModel ontoModel = getOntologyModel(DBpediaTBOXFile);
				fileWriter = new FileWriter(TBOXVocabulary);
				fileWriter.write("#DBpedia Classes localName export\n");
				
				System.out.println("Exporting DBpedia Schema...");
				
				watch.start();
				export(ontoModel);
				watch.stop();
				System.out.println("Exporting time [ms]: " + watch.elapsed(TimeUnit.MILLISECONDS));
			} 
			
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				try {
					fileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		public static OntModel getOntologyModel(String ontoFile)
		{   
		    OntModel ontoModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
		    try 
		    {
		        InputStream in = FileManager.get().open(ontoFile);
		        try 
		        {
		            ontoModel.read(in, null);
		           
		            
		            System.out.println("Ontology " + ontoFile + " loaded.");
		        } 
		        catch (Exception e) 
		        {
		            e.printStackTrace();
		        }
		       
		    } 
		    catch (JenaException je) 
		    {
		        System.err.println("ERROR" + je.getMessage());
		        je.printStackTrace();
		        System.exit(0);
		    }
		    return ontoModel;
		}
		
		static void export(OntModel ontoModel)
		{
			ExtendedIterator<OntClass> classesIter = ontoModel. listNamedClasses();
			
			int i = 0;
						
			
           try
           {
				while(classesIter.hasNext())
	            {
	            	OntClass c = classesIter.next();
	            	
	            	fileWriter.write(c.getLocalName() +  "\n");
	            	i++;
	            }
				
				System.out.println("Exported " + i + " named classes"); 
				
				fileWriter.flush();
           }
           catch(Exception ex)
           {
        	   ex.printStackTrace();
           }
		}
}