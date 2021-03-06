package it.unina.egc.DBInterface.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.jena.ext.com.google.common.base.Stopwatch;
import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shared.JenaException;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;



public class DBpediaInterface 
{
         
		static FileWriter fileWriter = null;	
		
		static final String  DBpediaTBOXFile = "knowledge_base/dbpedia_2014.owl";
		//static final String  DBpediaTypesPropertiesFile = "knowledge_base/instance_types_en.nt";
		//static final String  DBpediaMappingBasedPropertiesFile = "knowledge_base/mappingbased_properties_en.nt";
		//static final String  DBpediaSpecificMappingBasedPropertiesFile = "knowledge_base/specific_mappingbased_properties_en.nt";
		
		static Stopwatch watch = Stopwatch.createUnstarted();
		
		public static void main(String[] args) 
		{			
			try 
			{
				//File dbpediaFile = new File(DBpediaTypesPropertiesFile);
				//OntModel ontoModel = getOntologyModel(DBpediaTBOXFile);
				//fileWriter = new FileWriter("DBpedia_TBOX_export");
				
				//System.out.println(ontoModel);
				//export(ontoModel);
				//loadDBpedia(dbpediaFile);
				
				//loadRDF(DBpediaSpecificMappingBasedPropertiesFile);
				readDBpedia();
			} 
			
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*finally
			{
				try {
					fileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			
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
			ExtendedIterator<ObjectProperty> objPropsIter = ontoModel.listObjectProperties();
			ExtendedIterator<DatatypeProperty> datapropsIter = ontoModel.listDatatypeProperties();
			ExtendedIterator<AnnotationProperty> annPropsIter = ontoModel.listAnnotationProperties();
			ExtendedIterator<Individual> indivIter = ontoModel.listIndividuals();
			
			ExtendedIterator<OntProperty> allPropsIter = ontoModel.listAllOntProperties();
            
			
			
			int i = 0;
			int j = 0;
			int k = 0;
			int l = 0;
			int m = 0;
			int n = 0;
			
			
           try{
          
				while(classesIter.hasNext())
	            {
	            	OntClass c = classesIter.next();
	            	
	            	fileWriter.write(c.getLocalName() + "\t");
	            	i++;
	            }
				
				fileWriter.write("\n");
				
				
				while(objPropsIter.hasNext())
	            {
					ObjectProperty op = objPropsIter.next();
	            	
	            	fileWriter.write(op.getLocalName() + "\t");
	            	j++;
	            }
				
				fileWriter.write("\n");
				
				while(datapropsIter.hasNext())
	            {
					DatatypeProperty dp = datapropsIter.next();
	            	
	            	fileWriter.write(dp.getLocalName() + "\t");
	            	k++;
	            }
				
				fileWriter.write("\n");
				
				while(annPropsIter.hasNext())
	            {
					AnnotationProperty ap = annPropsIter.next();
	            	System.out.println(ap);
	            	fileWriter.write(ap.getLocalName() + "\t");
	            	l++;
	            }
				
				fileWriter.write("\n");
				
				while(indivIter.hasNext())
	            {
					Individual ind = indivIter.next();
	            	System.out.println(ind);
	            	fileWriter.write(ind.getLocalName());
	            	n++;
	            }
				
				while(allPropsIter.hasNext())
	            {
					OntProperty allp = allPropsIter.next();
	            	
	            	//fileWriter.write(ap.getLocalName() + "\t");
	            	m++;
	            }
				
				System.out.println("Exported " + i + " named classes, " + j + " Object properties, " +
						k + " datatype properties, " + l + " annotation properties, " + n + " individuals."); 
				
				System.out.println("Total properties: " + m);
				
				fileWriter.flush();
           }
           catch(Exception ex)
           {
        	   ex.printStackTrace();
           }
		}
		
		static void loadDBpedia(File file)
		{
			
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				
				int i = 0;
			    String line;
			   
			    while ((line = br.readLine()) != null) {
			        System.out.println(line);
			    	i++;
			    }
			    
			    watch.stop();
			    
			    System.out.println(i + " triples read in " + watch.elapsed(TimeUnit.SECONDS) + " s");
			    
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		static void loadRDF(String fileName)
		{
			 // create an empty model
			 Model model = ModelFactory.createDefaultModel();

			 // use the FileManager to find the input file
			 InputStream in = FileManager.get().open(fileName);
			if (in == null) {
			    throw new IllegalArgumentException(
			                                 "File: " + fileName + " not found");
			}
			watch.start();
			// read the RDF/XML file
			
			RDFDataMgr.read(model, in, Lang.NTRIPLES) ;
			//model.read(in, null, Lang.NT);
			watch.stop();
			System.out.println("Execution time:  " + watch.elapsed(TimeUnit.SECONDS));
			
			StmtIterator stmtIter = model.listStatements();
			
			while(stmtIter.hasNext())
			{
				Statement s = stmtIter.next();
				System.out.print(s.getSubject().getLocalName() + " -> ");
				System.out.print(s.getPredicate().getLocalName() + " -> ");
				
				RDFNode rdfNode = s.getObject();
				
				if (rdfNode.isResource())
					System.out.print(((Resource)s.getObject()).getLocalName());
				else if (rdfNode.isLiteral())
					System.out.print(((Literal)s.getObject()).getString());
				
				System.out.println();
				
			}

			// write it to standard out
			//model.write(System.out);
		}
		
		static void readDBpedia()
		{
			// Make a TDB-backed dataset
			  String directory = "MyDatabases/DBpedia" ;
			  Dataset dataset = TDBFactory.createDataset(directory) ;
			 
			  
			  dataset.begin(ReadWrite.READ) ;
			  // Get model inside the transaction
			  Model model = dataset.getDefaultModel();
			  
			  
			  watch.start();
			  
			  StmtIterator stmtIter = model.listStatements();
			  
			 while(stmtIter.hasNext())
			{
					Statement s = stmtIter.next();
					System.out.print(s.getSubject() + " -> ");
					System.out.print(s.getPredicate() + " -> ");
				
					RDFNode rdfNode = s.getObject();
				
					if (rdfNode.isResource())
						System.out.print(((Resource)s.getObject()));
					else if (rdfNode.isLiteral())
						System.out.print(((Literal)s.getObject()).getString());
					
					System.out.println();
				
			}
			  
			  dataset.end() ;
			  
			  
			 
		}
}
