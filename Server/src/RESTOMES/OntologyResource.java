package RESTOMES;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.ontology.*;

import org.jboss.resteasy.spi.NoLogWebApplicationException;


@Path("/ontology")
public class OntologyResource{
  
  public static final Map<Integer, Ontology> ontologyDB = new HashMap<Integer, Ontology>();
  public static final String QUERY_NAMESPACES = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\nPREFIX owl: <http://www.w3.org/2002/07/owl#>\r\nPREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\nPREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\nPREFIX fn: <http://www.w3.org/2005/xpath-functions#>\r\n";
  
  @Context
  UriInfo uri;

  /**
     * Register a new ontology entry using a JSON representation.
     * @param ontology the new ontology object data; this will be converted to POJO by a JSON provider (Jackson)
     * @return a response encoding
     */
  @POST
  @Consumes( MediaType.APPLICATION_JSON )
  public Response createOntologyEntryJSON( OntologyJsonObject object ){ //JsonFile file ){
    System.out.println( "OntologyResource.createEntry" );
    // String ontologyFileName = file.getName();
    // String ontologyContent = file.getContent();
    String ontologyName = object.getName();
    String ontologyUrl = object.getUrl();
    // try{
      //byte[] bytes = ontologyContent.getBytes();
      // File f = new File(ontologyFileName);
      // FileOutputStream fop = new FileOutputStream(f);
      //fop.write(bytes);
      //fop.flush();
      // fop.close();
      //File ontologyFile = new File(ontologyFileName);
      
      Ontology ontology = new Ontology();
      ontology.setUrl(ontologyUrl);
      ontology.setName(ontologyName);

      for(Map.Entry<Integer, Ontology> entry : ontologyDB.entrySet()){
        if(entry.getValue().getUrl().equals(ontology.getUrl())){
          return Response.seeOther(URI.create("/ontology/" + entry.getKey())).build(); 
        }
      }
      Map<Integer, OntologyClass> ontologyClasses = new HashMap<Integer, OntologyClass>();
      Map<String, Integer> classNameIDMap = new HashMap<String, Integer>();
      Map<String, Integer> objectPropertyIDMap = new HashMap<String, Integer>();
      Map<String, Integer> dataPropertyIDMap = new HashMap<String, Integer>();
      String queryString = QUERY_NAMESPACES + "select distinct ?class where { ?class a owl:Class.}";
      Map<Integer, ObjectProperty> objectProperties = new HashMap<Integer, ObjectProperty>();
      String queryString1 = QUERY_NAMESPACES + "SELECT distinct ?ObjectProperty ?ranges ?domains" +
	" WHERE { " + 
		" ?ObjectProperty a owl:ObjectProperty." +
		" optional { ?ObjectProperty rdfs:domain ?domains. }" +
		" optional { ?ObjectProperty rdfs:range ?ranges. }" +		
		" } " ; 

      Map<Integer, DataProperty> dataProperties = new HashMap<Integer, DataProperty>();
      String queryString2 = QUERY_NAMESPACES + "SELECT distinct ?DatatypeProperty ?ranges ?domains " +
	" WHERE { " + 
		" ?DatatypeProperty a owl:DatatypeProperty." +
		" optional { ?DatatypeProperty rdfs:domain ?domains. }" +
		" optional { ?DatatypeProperty rdfs:range ?ranges. }" +		
		" } " ; 	
      
      try{
      Model model = ModelFactory.createDefaultModel();
      model = model.read(ontologyUrl);
      ///Building metadata of Classes
      Query query = QueryFactory.create(queryString);
      QueryExecution queryExec = QueryExecutionFactory.create(query, model);
      ResultSet classes = queryExec.execSelect();
      while(classes.hasNext()){
		 QuerySolution entity = classes.next();
		 String value = entity.get("class").toString();
		 value = value.substring(value.lastIndexOf("/") + 1);
		 int key = ontologyClasses.size() + 1;
		 OntologyClass ontologyClass = new OntologyClass(value);
		 ontologyClasses.put(key, ontologyClass);
		 classNameIDMap.put(value, key);
	    }
      ///Building metadata of Object Properties	
      Query query1 = QueryFactory.create(queryString1);
      QueryExecution queryExec1 = QueryExecutionFactory.create(query1, model);
      ResultSet objectProperties_result = queryExec1.execSelect();
      while(objectProperties_result.hasNext()){
		 QuerySolution entity = objectProperties_result.next();
		 
		 String property_name = entity.get("?ObjectProperty").toString();
		 property_name = property_name.substring(property_name.lastIndexOf("/") + 1);
		 RDFNode domains_node = entity.get("domains");
		 RDFNode ranges_node = entity.get("ranges");
		if(objectPropertyIDMap.get(property_name) != null){
			Integer id = objectPropertyIDMap.get(property_name);
		 	if(domains_node != null){
		 		String domains = domains_node.toString();	
			 	domains = domains.substring(domains.lastIndexOf("/") + 1);
			 	
			 	Integer cid;
			 	if((cid = classNameIDMap.get(domains)) != null){
			 		OntologyClass oc;
			 		if((oc = ontologyClasses.get(cid)) != null){
			 			if(!objectProperties.get(id).getDomain().contains(oc))
			 				objectProperties.get(id).getDomain().add(oc);
			 		}
			 	}
		 	}
		 	if(ranges_node != null){
			 	String ranges = ranges_node.toString();	
			 	ranges = ranges.substring(ranges.lastIndexOf("/") + 1);
			 	
			 	Integer cid;
			 	if((cid = classNameIDMap.get(ranges)) != null){
			 		OntologyClass oc;
			 		if((oc = ontologyClasses.get(cid)) != null){
			 			if(!objectProperties.get(id).getRange().contains(oc))
			 				objectProperties.get(id).getRange().add(oc);
			 		}
			 	}
		 	}
		}else{
			ArrayList<OntologyClass> domain_list = new ArrayList<OntologyClass>();
			ArrayList<OntologyClass> range_list = new ArrayList<OntologyClass>();
			if(domains_node != null){
			 	String domains = domains_node.toString();	
			 	domains = domains.substring(domains.lastIndexOf("/") + 1);
			 	Integer cid;
			 	if((cid = classNameIDMap.get(domains)) != null){
			 		OntologyClass oc;
			 		if((oc = ontologyClasses.get(cid)) != null){
			 			domain_list.add(oc);
			 		}
			 	}
			}
			if(ranges_node != null){
			 	String ranges = ranges_node.toString();	
			 	ranges = ranges.substring(ranges.lastIndexOf("/") + 1);
			 	
			 	Integer cid;
			 	if((cid = classNameIDMap.get(ranges)) != null){
			 		OntologyClass oc;
			 		if((oc = ontologyClasses.get(cid)) != null){
			 			range_list.add(oc);
			 		}
			 	}
			}
			 	
			int key = objectProperties.size() + 1;
			ObjectProperty objectProperty = new ObjectProperty(domain_list, range_list, property_name);
			objectProperties.put(key, objectProperty);
			objectPropertyIDMap.put(property_name, key);
		}
      }

      ///Building metadata of Data Properties	    
      Query query2 = QueryFactory.create(queryString2);
      QueryExecution queryExec2 = QueryExecutionFactory.create(query2, model);
      ResultSet dataProperties_result = queryExec2.execSelect();
      while(dataProperties_result.hasNext()){
		 QuerySolution entity = dataProperties_result.next();
		 
		 String property_name = entity.get("?DatatypeProperty").toString();
		 property_name = property_name.substring(property_name.lastIndexOf("/") + 1);
		 RDFNode domains_node = entity.get("domains");
		 RDFNode ranges_node = entity.get("ranges");
		 if(dataPropertyIDMap.get(property_name) != null){
		 	Integer id = dataPropertyIDMap.get(property_name);
		 	if(domains_node != null){
		 		String domains = domains_node.toString();	
		 		domains = domains.substring(domains.lastIndexOf("/") + 1);
		 	 	Integer cid;
		 		if((cid = classNameIDMap.get(domains)) != null){
		 			OntologyClass oc;
		 			if((oc = ontologyClasses.get(cid)) != null){
		 				if(!dataProperties.get(id).getDomain().contains(oc))
		 					dataProperties.get(id).getDomain().add(oc);
		 			}
		 		}
		 	}
		 	if(ranges_nodes != null){
		 		String ranges = ranges_node.toString();	
		 		ranges = ranges.substring(ranges.lastIndexOf("/") + 1);
		 		
		 		if(!dataProperties.get(id).getRange().contains(ranges))
		 			dataProperties.get(id).getRange().add(ranges);
		 	}
		 }else{
		 	ArrayList<OntologyClass> domain_list = new ArrayList<OntologyClass>();
		 	ArrayList<String> range_list = new ArrayList<String>();
		 	
		 	if(domains_node != null){
		 		String domains = domains_node.toString();	
		 		domains = domains.substring(domains.lastIndexOf("/") + 1);
		 		Integer cid;
		 		if((cid = classNameIDMap.get(domains)) != null){
		 			OntologyClass oc;
		 			if((oc = ontologyClasses.get(cid)) != null){
		 				domain_list.add(oc);
		 			}
		 		}	
		 	}
		 	if(ranges_node != null){
		 		String ranges = ranges_node.toString();	
		 		ranges = ranges.substring(ranges.lastIndexOf("/") + 1);
		 	
		 		range_list.add(ranges);
		 	}
		 	
		 	int key = objectProperties.size() + 1;
		 	DataProperty dataProperty = new DataProperty(domain_list, range_list, property_name);
		 	dataProperties.put(key, dataProperty);
		 	dataPropertyIDMap.put(property_name, key);
		 }
	    }
	    
      }catch(Exception ex){
	// System.out.println(ex.toString());
	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Resource not found: " + ontologyUrl).build();
      }
      System.out.println("HERE");
      ontology.setOntologyClasses(ontologyClasses);
      ontology.setDataProperties(dataProperties);
      ontology.setObjectProperties(objectProperties);

      Integer id = ontologyDB.size() + 1;
      ontologyDB.put(id, ontology);
      return Response.created( URI.create("/ontology/" + id) ).build();
    // }catch(IOException ex){
      // return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for: " + ontologyFileName).build();
    // }
  }
  
     /**
     * Retrieve all URI of ontologies and return them as an object, using a JSON representation
     * @return map object of all ontologies; it will be converted to JSON using a JSON provider (Jackson)
     */

  @GET
  @Produces(MediaType.TEXT_HTML)
  public String returnOntologyListJSON(){
  	String html = "<html>\r\n" +
  		"<head>\r\n" + 
  		"</head>\r\n" + 
  		"<body>" + 
  		"<table>\r\n" +
  		"<tr>\r\n" +
  		"<td><h4>RESTful Ontology Metadata Extractor/Storage System</h4></td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"<table>\r\n" +
		"<tr>\r\n" + 
		"<td>\r\n" +
		"<div>\r\n" +
		"Ontology URI: <input type=\"text\" id=\"uri\"><br>\r\n" +
		"<br>\r\n" +
		"<button id=\"btnLogin\">Upload</button>\r\n" +
		"</div>\r\n" +
		"<div align=\"center\" id=\"result\">\r\n";
	for (Map.Entry<Integer, Ontology> entry : ontologyDB.entrySet()){
		UriBuilder ub = uri.getAbsolutePathBuilder();
            	URI userUri = ub.path(entry.getKey().toString()).build();
		String value = userUri.toString();
		html += "<a href=" + value + ">" + value + "</a>\r\n";
	}
	html += "</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
	
	
	return html;
  }
     /**
     * Retrieve an ontology entry and return it as an object, using a JSON representation
     * @param id path parameter identifying the ontology entry
     * @return an ontology object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path( "{oid: [1-9][0-9]*}" )
  @Produces(MediaType.APPLICATION_JSON)
  public Ontology getOntologyEntryJSON(@PathParam("oid") Integer id){
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    }
    return ontology;
  }
  
  
     /**
     * Retrieve all URIs of classes of an ontology and return them as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @return map object of all classes; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path( "{oid: [1-9][0-9]*}/class" )
  @Produces(MediaType.APPLICATION_JSON)
  public Map<Integer, OntologyClass> getClassListJSON(@PathParam("oid") Integer id){
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    }
    Map<Integer, OntologyClass> classMap = ontology.getOntologyClasses();
    return classMap;
  }
  
     /**
     * Retrieve all URIs of dataproperties of an ontology and return them as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @return map object of all dataproperties; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path( "{oid: [1-9][0-9]*}/dataproperty" )
  @Produces(MediaType.APPLICATION_JSON)
  public Map<Integer, DataProperty> getDataPropertyListJSON(@PathParam("oid") Integer id){
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    }
    Map<Integer, DataProperty> dataPropertyMap = ontology.getDataProperties();
    return dataPropertyMap;
  }
    
     /**
     * Retrieve all URIs of object properties of an ontology and return them as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @return map object of all dataproperties; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path( "{oid: [1-9][0-9]*}/objectproperty" )
  @Produces(MediaType.APPLICATION_JSON)
  public Map<Integer, ObjectProperty> getObjectPropertyListJSON(@PathParam("oid") Integer id){
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    }
    Map<Integer, ObjectProperty> objectPropertyMap = ontology.getObjectProperties();
    return objectPropertyMap;
  }
  
  /**
     * Retrieve class of an ontology as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @param cid path parameter identifying the class entry
     * @return a class object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path( "{oid: [1-9][0-9]*}/class/{cid: [1-9][0-9]*}" )
  @Produces(MediaType.APPLICATION_JSON)
  public OntologyClass getClassJSON(@PathParam("oid") Integer id, @PathParam("cid") Integer cid){
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    }
    OntologyClass ontologyClass = ontology.getOntologyClasses().get(cid);
    if(ontologyClass == null)
    	throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
    return ontologyClass;
  }
  
  /**
     * Retrieve dataproperty of an ontology as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @param dpid path parameter identifying the dataproperty entry
     * @return a datapropert object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path("{oid: [1-9][0-9]*}/dataproperty/{dpid: [1-9][0-9]*}")
  @Produces(MediaType.APPLICATION_JSON)
  public DataProperty getDataPropertyEntryJSON(@PathParam("oid") Integer oid, @PathParam("dpid") Integer dpid){
    Ontology ontology = ontologyDB.get(oid);
    if(ontology == null)
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    DataProperty dataProperty = ontology.getDataProperties().get(dpid);
    if(dataProperty == null)
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    return dataProperty;
  }
  
  
  /**
     * Retrieve objectproperty of an ontology as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @param opid path parameter identifying the dataproperty entry
     * @return a objectpropert object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path("{oid: [1-9][0-9]*}/objectproperty/{opid: [1-9][0-9]*}")
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectProperty getObjectPropertyEntryJSON(@PathParam("oid") Integer oid, @PathParam("opid") Integer opid){
    Ontology ontology = ontologyDB.get(oid);
    if(ontology == null)
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    ObjectProperty objectProperty = ontology.getObjectProperties().get(opid);
    if(objectProperty == null)
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    return objectProperty;
  }
}
