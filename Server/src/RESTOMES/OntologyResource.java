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
import javax.ws.rs.FormParam;
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
  @Produces(MediaType.TEXT_HTML)
  public String createOntologyEntry(@FormParam("uri") String ont){ //( OntologyJsonObject object ){ //JsonFile file ){
    System.out.println( "OntologyResource.createEntry   " + ont );
    String ontologyUrl = ont;
    String ontologyName = ontologyUrl.substring(ontologyUrl.lastIndexOf("/") + 1);
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
		
		"<br>\r\n" +
		"<form method=\"POST\" action=\"\">" +
		"Please enter a valid ontology URI</br></br>\r\n" +
		"Ontology URI: <input type=\"text\" name=\"uri\" size=\"80\"> " +
		"<input type=\"submit\" value=\"Submit\">\r\n" +
		"</from>" +
		"</div>\r\n" +
		"<div id=\"result\">\r\n" + 
		"Existing Ontologies:</br>\r\n";
      
      Ontology ontology = new Ontology();
      ontology.setUrl(ontologyUrl);
      ontology.setName(ontologyName);

      for(Map.Entry<Integer, Ontology> entry : ontologyDB.entrySet()){
        if(entry.getValue().getUrl().equals(ontology.getUrl())){
          //return Response.seeOther(URI.create("/ontology/" + entry.getKey())).build(); 
          for (Map.Entry<Integer, Ontology> entry1 : ontologyDB.entrySet()){
		UriBuilder ub = uri.getAbsolutePathBuilder();
            	URI userUri = ub.path(entry1.getKey().toString()).build();
		String value = userUri.toString();
		html += "<a href=" + value + ">" + ontologyUrl + "</a></br>\r\n";
	}
	html += "</div>\r\n" +
		"</br><div style=\"color:red\">\r\n" +
		"Duplicate Request: Ontology already exists" +
		"</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
		
	return html;
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
		 	if(ranges_node != null){
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
	// return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Resource not found: " + ontologyUrl).build();
	for (Map.Entry<Integer, Ontology> entry : ontologyDB.entrySet()){
		UriBuilder ub = uri.getAbsolutePathBuilder();
            	URI userUri = ub.path(entry.getKey().toString()).build();
		String value = userUri.toString();
		html += "<a href=" + value + ">" + entry.getValue().getUrl() + "</a></br>\r\n";
	}
	html += "</div>\r\n" +
		"</br><div style=\"color:red\">\r\n" +
		"Server Internal Error" +
		"</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
		
	return html;
      }
      ontology.setOntologyClasses(ontologyClasses);
      ontology.setDataProperties(dataProperties);
      ontology.setObjectProperties(objectProperties);

      Integer id = ontologyDB.size() + 1;
      ontologyDB.put(id, ontology);
      URI location = URI.create("/ontology/" + id);
      
      for (Map.Entry<Integer, Ontology> entry : ontologyDB.entrySet()){
		UriBuilder ub = uri.getAbsolutePathBuilder();
            	URI userUri = ub.path(entry.getKey().toString()).build();
		String value = userUri.toString();
		html += "<a href=" + value + ">" + entry.getValue().getUrl() + "</a></br>\r\n";
	}
	html += "</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
		
	return html;
      //return Response.created( location ).build();
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
		
		"<br>\r\n" +
		"<form method=\"POST\" action=\"\">" +
		"Please enter a valid ontology URI</br></br>\r\n" +
		"Ontology URI: <input type=\"text\" name=\"uri\" size=\"80\"> " +
		"<input type=\"submit\" value=\"Submit\">\r\n" +
		"</from>" +
		"</div>\r\n" +
		"<div id=\"result\">\r\n" +
		"Existing Ontologies:</br>\r\n";
	for (Map.Entry<Integer, Ontology> entry : ontologyDB.entrySet()){
		UriBuilder ub = uri.getAbsolutePathBuilder();
            	URI userUri = ub.path(entry.getKey().toString()).build();
		String value = userUri.toString();
		html += "<a href=" + value + ">" + entry.getValue().getUrl() + "</a><br>\r\n";
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
  @Produces(MediaType.TEXT_HTML)
  public String getOntologyEntryJSON(@PathParam("oid") Integer id){
    String html = "<html>\r\n" +
  		"<head>\r\n" + 
  		"</head>\r\n" + 
  		"<body>" + 
		"<table>\r\n" +
		"<tr>\r\n" + 
		"<td>\r\n" +
		"<div id=\"result\">\r\n";
	
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      //throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
      html += "</div></br>\r\n" +
      		"</br><div style=\"color:red\">\r\n" +
      		"Ontology not found" +
      		"</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
		
	return html;
    }
    
    html += "Ontology URI: " + ontology.getUrl() + "</br>\r\n";
    UriBuilder ub = uri.getAbsolutePathBuilder();
    URI userUri = ub.path("class").build();
    String value = userUri.toString();
    html += "<a href=" + value + ">" + "Ontology Classes" + "</a><br>\r\n";
    UriBuilder ub1 = uri.getAbsolutePathBuilder();
    URI userUri1 = ub1.path("objectproperty").build();
    value = userUri1.toString();
    html += "<a href=" + value + ">" + "Ontology ObjectProperties" + "</a><br>\r\n";
    UriBuilder ub2 = uri.getAbsolutePathBuilder();
    URI userUri2 = ub2.path("dataproperty").build();
    value = userUri2.toString();
    html += "<a href=" + value + ">" + "Ontology DataTypeProperties" + "</a><br>\r\n";
    html += "</div></br>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
    return html;
  }
  
  
     /**
     * Retrieve all URIs of classes of an ontology and return them as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @return map object of all classes; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path( "{oid: [1-9][0-9]*}/class" )
  @Produces(MediaType.TEXT_HTML)
  public String getClassListJSON(@PathParam("oid") Integer id){
    String html = "<html>\r\n" +
  		"<head>\r\n" + 
  		"</head>\r\n" + 
  		"<body>" + 
		"<table>\r\n" +
		"<tr>\r\n" + 
		"<td>\r\n" +
		"<div id=\"result\">\r\n";
		
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      //throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
      html += "</div></br>\r\n" +
      		"</br><div style=\"color:red\">\r\n" +
      		"Ontology not found" +
      		"</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
		
	return html;
    }
    html += "Ontology URI: " + ontology.getUrl() + "</br>\r\n" +
    	"</br>Ontology Classes:</br>";
    for (Map.Entry<Integer, OntologyClass> entry : ontology.getOntologyClasses().entrySet()){
		UriBuilder ub = uri.getAbsolutePathBuilder();
            	URI userUri = ub.path(entry.getKey().toString()).build();
		String value = userUri.toString();
		html += "<a href=" + value + ">" + entry.getValue().getClassName() + "</a><br>\r\n";
	}
    html += "</div></br>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
    return html;
  }
  
     /**
     * Retrieve all URIs of dataproperties of an ontology and return them as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @return map object of all dataproperties; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path( "{oid: [1-9][0-9]*}/dataproperty" )
  @Produces(MediaType.TEXT_HTML)
  public String getDataPropertyListJSON(@PathParam("oid") Integer id){
    String html = "<html>\r\n" +
  		"<head>\r\n" + 
  		"</head>\r\n" + 
  		"<body>" + 
		"<table>\r\n" +
		"<tr>\r\n" + 
		"<td>\r\n" +
		"<div id=\"result\">\r\n";
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      //throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
      html += "</div></br>\r\n" +
      		"</br><div style=\"color:red\">\r\n" +
      		"Ontology not found" +
      		"</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
		
	return html;
    }
    html += "Ontology URI: " + ontology.getUrl() + "</br>\r\n";
    html += "</br>Ontology DataTypeProperties:</br>";
    for (Map.Entry<Integer, DataProperty> entry : ontology.getDataProperties().entrySet()){
		UriBuilder ub = uri.getAbsolutePathBuilder();
            	URI userUri = ub.path(entry.getKey().toString()).build();
		String value = userUri.toString();
		html += "<a href=" + value + ">" + entry.getValue().getProperty() + "</a><br>\r\n";
	}
    html += "</div></br>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
    return html;
  }
    
     /**
     * Retrieve all URIs of object properties of an ontology and return them as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @return map object of all dataproperties; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path( "{oid: [1-9][0-9]*}/objectproperty" )
  @Produces(MediaType.TEXT_HTML)
  public String getObjectPropertyListJSON(@PathParam("oid") Integer id){
  	String html = "<html>\r\n" +
  		"<head>\r\n" + 
  		"</head>\r\n" + 
  		"<body>" + 
		"<table>\r\n" +
		"<tr>\r\n" + 
		"<td>\r\n" +
		"<div id=\"result\">\r\n";
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      //throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
      html += "</div></br>\r\n" +
      		"</br><div style=\"color:red\">\r\n" +
      		"Ontology not found" +
      		"</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
		
	return html;
    }
    
    html += "Ontology URI: " + ontology.getUrl() + "</br>\r\n";
    html += "</br>Ontology ObjectProperties:</br>";
    for (Map.Entry<Integer, ObjectProperty> entry : ontology.getObjectProperties().entrySet()){
		UriBuilder ub = uri.getAbsolutePathBuilder();
            	URI userUri = ub.path(entry.getKey().toString()).build();
		String value = userUri.toString();
		html += "<a href=" + value + ">" + entry.getValue().getProperty() + "</a><br>\r\n";
	}
    html += "</div></br>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
    return html;
  }
  
  /**
     * Retrieve class of an ontology as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @param cid path parameter identifying the class entry
     * @return a class object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path( "{oid: [1-9][0-9]*}/class/{cid: [1-9][0-9]*}" )
  @Produces(MediaType.TEXT_HTML)
  public String getClassJSON(@PathParam("oid") Integer id, @PathParam("cid") Integer cid){
    String html = "<html>\r\n" +
  		"<head>\r\n" + 
  		"</head>\r\n" + 
  		"<body>" + 
		"<table>\r\n" +
		"<tr>\r\n" + 
		"<td>\r\n" +
		"<div id=\"result\">\r\n";
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      //throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
      html += "</div></br>\r\n" +
      		"</br><div style=\"color:red\">\r\n" +
      		"Ontology not found" +
      		"</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
		
	return html;
    }
    OntologyClass ontologyClass = ontology.getOntologyClasses().get(cid);
    if(ontologyClass == null){
    	// throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
    	html += "</div></br>\r\n" +
      		"</br><div style=\"color:red\">\r\n" +
      		"Resource not found" +
      		"</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
		
	return html;
    }
    html += "Class Name: " + ontologyClass.getClassName() + "</br>\r\n";
    html += "</div></br>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
    return html;
  }
  
  /**
     * Retrieve dataproperty of an ontology as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @param dpid path parameter identifying the dataproperty entry
     * @return a datapropert object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path("{oid: [1-9][0-9]*}/dataproperty/{dpid: [1-9][0-9]*}")
  @Produces(MediaType.TEXT_HTML)
  public String getDataPropertyEntryJSON(@PathParam("oid") Integer oid, @PathParam("dpid") Integer dpid){
    String html = "<html>\r\n" +
  		"<head>\r\n" + 
  		"</head>\r\n" + 
  		"<body>" + 
		"<table>\r\n" +
		"<tr>\r\n" + 
		"<td>\r\n" +
		"<div id=\"result\">\r\n";
    Ontology ontology = ontologyDB.get(oid);
    if(ontology == null){
      //throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
      html += "</div></br>\r\n" +
      		"</br><div style=\"color:red\">\r\n" +
      		"Ontology not found" +
      		"</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
		
	return html;
    }
    DataProperty dataProperty = ontology.getDataProperties().get(dpid);
    if(dataProperty == null){
      //throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
      html += "</div></br>\r\n" +
      		"</br><div style=\"color:red\">\r\n" +
      		"Resource not found" +
      		"</div>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
		
	return html;
    }
    html += "Property Name: " + dataProperty.getProperty() + "</br>\r\n";
    for(OntologyClass entry : dataProperty.getDomain()){
    	for (Map.Entry<Integer, OntologyClass> entry1 : ontology.getOntologyClasses().entrySet()){
    		if(entry1.getValue().getClassName().compareTo(entry.getClassName()) == 0){
    			UriBuilder ub = uri.getBaseUri();
    			URI userUri = ub.path("class/" + entry1.getKey()).build();
    			String value = userUri.toString();
    			html += "<a href=" + value + ">" + value + "</a><br>\r\n";
    			break;
    		}
	}
    }
    html += "</div></br>\r\n" +
		"</td>\r\n" +
		"</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
		"</html>";
    return html;
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
