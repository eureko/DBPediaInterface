package it.unina.egc.DBInterface.Utils;

import java.util.TreeMap;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.RelationshipType;

import scala.collection.immutable.List;

public class ProgramaticDBPediaOntologyIntoNeo4J 
{
	static final String dbpediaOntologyFile = "knowledge_base/dbpedia_2015-04.owl";
	static final String dbpediaOntologyCSVFile = "export/dbpediaOntologyCSVFile.csv";
	
	static TreeMap<String, Long> nodeMap = new TreeMap<String, Long>(); 
		
	public static void main(String[] args) 
	{
		try
		{
			OntModel ontoModel = ModelFactory.createOntologyModel();
			ontoModel.read(dbpediaOntologyFile, "RDF/XML");
			
			//OntModel inferedModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MINI_RULE_INF, ontoModel);
			
			System.out.println("begin");
			
			ExtendedIterator<OntClass> classes_iter = ontoModel.listNamedClasses();
			
			while (classes_iter.hasNext())
			{
				System.out.println(classes_iter.next().getURI());
			}
			
			OntClass c = ontoModel.getOntClass("http://dbpedia.org/ontology/Book");
			
			ExtendedIterator<OntProperty> props_iter = c.listDeclaredProperties();
						
			System.out.println("************************************************");
			
			
			while(props_iter.hasNext())
			{
				try
				{
					OntProperty ontProperty = props_iter.next();
					System.out.println(ontProperty.getDomain().getURI() + " -> " + ontProperty.getURI() + " -> " + ontProperty.getRange().toString());
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
	private static enum RelTypes implements RelationshipType
	{
	    LINK
	}
}
