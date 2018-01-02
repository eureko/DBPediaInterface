package it.unina.egc.DBInterface.Utils;

import java.io.FileWriter;
import java.io.InputStream;
import java.util.TreeMap;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class DBPediaOntologyIntoNeo4J 
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
			
			//OntModel inferredModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, ontoModel);
			//FileWriter file1writer = new FileWriter(dbpediaOntologyCSVFile);
			
			//Access Neo4J running server 
			
			GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("/Users/caldarola/Documents/Neo4j/dbpedia_ontology3");
			registerShutdownHook( graphDb );
			
			ExtendedIterator<OntClass> classes = ontoModel.listNamedClasses();
			
			try ( Transaction tx = graphDb.beginTx() )
			{
				
				while(classes.hasNext())
				{
					try
					{
						OntClass c = classes.next();
						System.out.println(c);
						
						Node node = graphDb.createNode();
						long nodeId = node.getId();	
						
						System.out.println(nodeId);
						
						nodeMap.put(c.getURI(), nodeId);
												
						node.setProperty("label", c.getLabel("EN"));
						String comment = c.getComment("EN");
						if (comment != null)
							node.setProperty("comment", comment);
						node.setProperty("URI", c.getURI());
						
						
						//Label myLabel = DynamicLabel.label(c.getLocalName());
						//node.addLabel(myLabel);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				
			    tx.success();
			}
			
			ExtendedIterator<ObjectProperty> objProps = ontoModel.listObjectProperties();
			
			try ( Transaction tx = graphDb.beginTx() )
			{
				while(objProps.hasNext())
				{
					try
					{
						ObjectProperty objProp = objProps.next();
						OntResource domain = objProp.getDomain();
						OntResource range = objProp.getRange();
						
						Node srcNode, objNode;
						
						srcNode = graphDb.getNodeById(nodeMap.get(domain.getURI()));
						objNode = graphDb.getNodeById(nodeMap.get(range.getURI()));
						
						Relationship relationship = srcNode.createRelationshipTo(objNode, RelTypes.LINK);
						relationship.setProperty("label", objProp.getLabel("EN"));
						relationship.setProperty("URI", objProp.getURI());
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				
				tx.success();
			}
			
			// Add super-sub classes relationships
			System.out.println("Creating subclassof relationships...");
			
			classes = ontoModel.listNamedClasses();
			
			try ( Transaction tx = graphDb.beginTx() )
			{
				while(classes.hasNext())
				{
					try
					{
						OntClass c = classes.next();
						
						ExtendedIterator<OntClass> subClassesIter = c.listSubClasses();
						
						while (subClassesIter.hasNext())
						{
							OntClass subClass = subClassesIter.next();
							
							Node srcNode, objNode;
							
							srcNode = graphDb.getNodeById(nodeMap.get(c.getURI()));
							objNode = graphDb.getNodeById(nodeMap.get(subClass.getURI()));
							
							Relationship relationship = srcNode.createRelationshipTo(objNode, RelTypes.SUBCLASS);
							
						}
						
						
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				
				tx.success();
			}
			
			
			
			graphDb.shutdown();
			
			//file1writer.close();
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
	    LINK,
	    SUBCLASS
	}
}
