package it.unina.egc.DBInterface.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;


public class DBPediaTablesHandler 
{
	static final String  DBpediaTalesFile1 = "knowledge_base/knowledgeBase/Book.csv";
	static final String  DBpediaTalesFile2 = "knowledge_base/knowledgeBase/Writer.csv";
	static final String  DBpediaTalesFile3 = "knowledge_base/knowledgeBase/Film.csv";
	static final String  DBpediaTalesFile4 = "knowledge_base/knowledgeBase/Person.csv";
	static final String fileRoot = "file:///Users/caldarola/Desktop/workspace/DBPediaInterface/";
	
	static final String File1URL = "export/Book.csv";
	static final String File2URL = "export/Writer.csv";
	
	static TreeMap<String, Long> nodeMap = new TreeMap<String, Long>(); 
	static GraphDatabaseService graphDb;

	static final String against = "http://dbpedia.org/ontology/Book";
	
	static final String dbpediaOntologyFile = "knowledge_base/dbpedia_2015-04.owl";
	
	static final String neo4jDBPath = "/Users/caldarola/Documents/Neo4j/default.graphdb_dbpedia4";
	
	static OntModel ontoModel;
	
	public static void main(String[] args) 
	{
		try
		{
		
			//ontoModel = ModelFactory.createOntologyModel();
			//ontoModel.read(dbpediaOntologyFile, "RDF/XML");
				
			//Access Neo4J running server 
			
			//FileUtils.deleteRecursively( new File( neo4jDBPath ) );
			graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(neo4jDBPath);
			registerShutdownHook(graphDb);
			
			
			loadCSVFileIntoNeo4JGraph(DBpediaTalesFile1);
			//loadAndResolveLinks(DBpediaTalesFile2, "author");
			
		}
		catch(Exception ex)
		{
			registerShutdownHook(graphDb);
			ex.printStackTrace();
			
		}
	}
			
	static String getCypherLoadQuery(String fileURL) throws Exception
	{
		String query = "USING PERIODIC COMMIT 1000\n";
		query += "LOAD CSV FROM " + "\"" + fileRoot + fileURL +"\"" + " AS line\n";
		
		
		BufferedReader br = new BufferedReader(new FileReader(fileURL));
		
		String line1, line2, line3, line4, line;
		
		line1 = br.readLine();
		line2 = br.readLine();
		line3 = br.readLine();
		line4 = br.readLine();
		
		String[] properties_labels = line1.split(","); 			// The first header contains the properties labels.
		String[] properties_uris = line2.split(",");  			// The second header contains the properties URIs
		String[] properties_range_labels = line3.split(",");	 // The third header contains the properties range labels.
		String[] properties_range_URIs = line4.split(",");		 // The fourth header contains the properties range URIs.
		
		// CREATE (:Artist { name: line[1], year: toInt(line[2])})
		String createStr = "CREATE (resource: " + fileURL.substring(fileURL.lastIndexOf('/')+1, fileURL.indexOf('.')) + " { ";  
		for (int i = 0; i < properties_labels.length; i++)
		{
			createStr += properties_labels[i].replaceAll("[\"#-]", "").replaceAll("[0-9]", "") + ": " + "line[" + i +"]";
			if (i < properties_labels.length -1)
				createStr += ", ";
		}
		
		createStr += "})";
		
		query += createStr;
				
				
		return query;
	}
	
	static void loadCSVFileIntoNeo4JGraph(String file)
	{
		try
		{
			String dbpedia_class = file.substring(file.lastIndexOf('/')+ 1, file.lastIndexOf('.'));
			
			BufferedReader br1 = new BufferedReader(new FileReader(file));
			
			String line1, line2, line3, line4, line;
			
			line1 = br1.readLine();
			line2 = br1.readLine();
			line3 = br1.readLine();
			line4 = br1.readLine();
			
			String[] properties_labels = line1.split(","); 			// The first header contains the properties labels.
			String[] properties_uris = line2.split(",");  			// The second header contains the properties URIs
			String[] properties_range_labels = line3.split(",");	// The third header contains the properties range labels.
			String[] properties_range_URIs = line4.split(",");		// The fourth header contains the properties range URIs.
			
			Transaction tx = graphDb.beginTx();
			try
			{
				while ((line = br1.readLine()) != null)
				{
					String[] fields = line.split(","); // "URI","rdf-schema#label","rdf-schema#comment","author_label","author", ...
					 
					Node node = graphDb.createNode();
					long nodeId = node.getId();	
					
					nodeMap.put(fields[0], nodeId);
						
					 for (int i = 0; i < 5/*properties_labels.length*/; i++) // takes all properties
					 {
						System.out.println(properties_labels[i].replace("\"", "") +", " + fields[i].replace("\"", "") + " " + dbpedia_class);
						node.setProperty(properties_labels[i].replace("\"", ""), fields[i].replace("\"", ""));
						Label myLabel = DynamicLabel.label(dbpedia_class);
						node.addLabel(myLabel);
					 }
				}
				
				tx.success();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			finally
			{
			    tx.close();
			    br1.close();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	//TODO
	static void loadAndResolveLinks(String file, String link) throws Exception
	{
		
		// load the new csv file
		String dbpedia_class = file.substring(file.lastIndexOf('/')+ 1, file.lastIndexOf('.'));
		BufferedReader br1 = new BufferedReader(new FileReader(file));
		
		String line1, line2, line3, line4, line;
		
		line1 = br1.readLine();
		line2 = br1.readLine();
		line3 = br1.readLine();
		line4 = br1.readLine();
		
		String[] properties_labels = line1.split(","); 			// The first header contains the properties labels.
		String[] properties_uris = line2.split(",");  			// The second header contains the properties URIs
		String[] properties_range_labels = line3.split(",");	// The third header contains the properties range labels.
		String[] properties_range_URIs = line4.split(",");		// The fourth header contains the properties range URIs.
	
		RelationshipType rel_type = DynamicRelationshipType.withName(link);
		
		Transaction tx = graphDb.beginTx();
		try
		{
			while ((line = br1.readLine()) != null)
			{
				String[] fields = line.split(","); // "URI","rdf-schema#label","rdf-schema#comment","author_label","author", ...
				 
				Node node = graphDb.createNode();
				long nodeId = node.getId();	
				
				nodeMap.put(fields[0].replace("\"", ""), nodeId);
					
				 for (int i = 0; i < /*properties_labels.length*/3; i++) // takes all properties
				 {
					System.out.println(properties_labels[i].replace("\"", "") +", " + fields[i].replace("\"", "") + " " + dbpedia_class);
					node.setProperty(properties_labels[i].replace("\"", ""), fields[i].replace("\"", ""));
					Label myLabel = DynamicLabel.label(dbpedia_class);
					node.addLabel(myLabel);
				 }
			}
			
			tx.success();
		}
		finally
		{
		     tx.close();
		}
		
		
		// Set links
		Transaction tx1 = graphDb.beginTx();
		try
		{
			Iterator<Node> iterator = graphDb.getAllNodes().iterator();
			
			while (iterator.hasNext())
			{
				try
				{
					Node n = iterator.next();
					//System.out.println(n);
					
					String author_uri = "";
					if (n.hasProperty("author"))
					{
						 author_uri = (String)n.getProperty("author");
						 System.out.println(author_uri);
						 
						 long target_node_id = nodeMap.get(author_uri);
						 
						 Node target_node = graphDb.getNodeById(target_node_id);
						 
						n.createRelationshipTo(target_node, rel_type);
						
						System.out.println("Created relationship between: " + n + " and " + target_node);
					}
				}
				catch(Exception ex)
				{
					//ex.printStackTrace();
					System.out.println("Not found");
				}
				
				//System.out.println(n + ": author: " +  author_str);
			}
			tx1.success();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		 {
		     tx1.close();
		     br1.close();
		 }
	}
	
	int someClassinHierarchyHasURIEqualTo(OntClass c, String against)
	{
		int flag = 0;
		if (c.getURI().compareTo(against) == 0)
			flag++;
		else
		{
			if (c.hasSuperClass())
			{
				ExtendedIterator<OntClass> superClasses = c.listSuperClasses(true);
				while (superClasses.hasNext())
				{
					OntClass sup = superClasses.next();
					flag += someClassinHierarchyHasURIEqualTo(sup, against);
				}
			}	
		}
		return flag;	
	}
	
	void linkEachToEachOther(String label1, String label2)
	{
		Iterable<Node> nodes = graphDb.getAllNodes();
		Iterator<Node> nodes_iter = nodes.iterator();
		
		while(nodes_iter.hasNext())
		{ 
			try
			{
				Node n = nodes_iter.next();
				String prop_uri = (String)n.getProperty("author");
				
				long prop_node_id = nodeMap.get(prop_uri);
				
				
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			
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
	    
	}
	
	
}
