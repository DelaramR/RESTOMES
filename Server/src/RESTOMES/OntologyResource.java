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
      String queryString = QUERY_NAMESPACES + "select distinct ?class where { ?class a owl:Class.}";      
      try{
      Model model = ModelFactory.createDefaultModel();
      model = model.read(ontologyUrl);
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
	    }
      }catch(Exception ex){
	System.out.println(ex.toString());
	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Resource not found: " + ontologyUrl).build();
      }
      System.out.println("HERE");
      ontology.setOntologyClasses(ontologyClasses);	

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
  @Produces(MediaType.APPLICATION_JSON)
  public Map<Integer, Ontology> returnOntologyListJSON(){
    return ontologyDB;
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
