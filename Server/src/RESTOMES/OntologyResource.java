package RESTOMES;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

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

import org.jboss.resteasy.spi.NoLogWebApplicationException;

@Path("/ontology")
public class OntologyResource{
  
  public static final Map<Integer, Ontology> ontologyDB = new HashMap<Integer, Ontology>();
  
  /**
     * Register a new ontology entry using a JSON representation.
     * @param ontology the new ontology object data; this will be converted to POJO by a JSON provider (Jackson)
     * @return a response encoding
     */
  @POST
  @Consumes( MediaType.APPLICATION_JSON )
  public Response createEntryJSON( Ontology ontology ){
    System.out.println( "OntologyResource.createEntry" );
    for(Map.Entry<Integer, Vntology> entry : ontologyDB.entrySet()){
      if(entry.getValue().equals(ontology)){
        return Response.seeOther(URI.create("/ontology/" + entry.getKey())).build(); 
      }
    }
    Integer id = ontologyDB.size() + 1;
    ontologyDB.put(id, ontology);
    return Response.created( URI.create("/ontology/" + id) ).build();
  }
  
     /**
     * Retrieve all URI of ontologies and return them as an object, using a JSON representation
     * @return map object of all ontologies; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<Integer, Ontology> returnListJSON(){
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
  public Ontology getEntryJSON(@PathParam("oid") Integer id){
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
  public Map<Integer, OntClass> getEntryJSON(@PathParam("oid") Integer id){
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    }
    Map<Integer, OntClass> classMap = ontology.getontologyClasses();
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
  public Map<Integer, DataProperty> getEntryJSON(@PathParam("oid") Integer id){
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    }
    Map<Integer, DataProperty> dataPropertyMap = ontology.getdataProperties();
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
  public Map<Integer, ObjectProperty> getEntryJSON(@PathParam("oid") Integer id){
    final Ontology ontology = ontologyDB.get(id);
    if(ontology == null){
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    }
    Map<Integer, ObjectProperty> objectPropertyMap = ontology.getobjectProperties();
    return objectPropertyMap;
  }
  
  /**
     * Retrieve dataproperty of a class of an ontology as an object, using a JSON representation
     * @param oid path parameter identifying the ontology entry
     * @param dpid path parameter identifying the dataproperty entry
     * @return a datapropert object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path("{oid: [1-9][0-9]*}/dataproperty/{dpid: [1-9][0-9]*}")
  @Produces(MediaType.APPLICATION_JSON)
  public DataProperty getEntryJSON(@PathParam("oid") Integer oid, @PathParam("dpid") Integer dpid){
    ArrayList<ShowTime> shows = new ArrayList<ShowTime>();
    Movie movie = movieDB.get(movieId);
    if(movie == null)
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    Theater theater = TheaterResource.theaterDB.get(theaterId);
    if(theater == null)
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    for(ShowTime show : TheaterResource.showTimes){
      if(show.getMovie().equals(movie) && show.getTheater().equals(theater))
        shows.add(show);
    }
    return shows;
  }
  
  /**
     * Delete a movie from a theater
     * @param id path parameter identifying the movie resource to delete from the theater
     * @param id1 path parameter identifying the theater resource to deleter from the theaters that are showing the movie
     * @return a response encoding
     */
  @DELETE
  @Path("{id: [1-9][0-9]*}/theater/{id1: [1-9][0-9]*}")
  public Response deleteShowsJSON(@PathParam("id") Integer movieId, @PathParam("id1") Integer theaterId){
    Movie movie = movieDB.get(movieId);
    if(movie == null)
      return Response.status(Response.Status.NOT_FOUND).build();
    Theater theater = TheaterResource.theaterDB.get(theaterId);
    if(theater == null)
      return Response.status(Response.Status.NOT_FOUND).build();
    for(ShowTime time : TheaterResource.showTimes){
      if(time.getMovie().equals(movie) && time.getTheater().equals(theater)){
        TheaterResource.showTimes.remove(time);
      }
    }
    return Response.ok().build();
  }
  
    /**
     * Retrieve a list of movies by genre and return it as an object, using a JSON representation
     * @param genre path parameter identifying a genre
     * @return a list of movies object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path("genre/{genre: [A-Za-z]+}")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<Integer, Movie> getMovieByGenre(@PathParam("genre") String genre){
    Map<Integer, Movie> movies = new HashMap<Integer, Movie>();
    for(Map.Entry<Integer, Movie> entry : movieDB.entrySet()){
      if(entry.getValue().getGenre().toLowerCase().compareTo(genre.toLowerCase()) == 0){
        movies.put(entry.getKey(), entry.getValue());
      }
    }
    return movies;
  }
  
    /**
     * Retrieve a list of movies and return it as an object, using a JSON representation
     * @param rate path parameter identifying a rate value
     * @return a list of movies object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path("rate/{rate: [1-5]}")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<Integer, Movie> getMovieByRate(@PathParam("rate") Integer rate){
    Map<Integer, Movie> movies = new HashMap<Integer, Movie>();
    for(Map.Entry<Integer, Movie> entry : movieDB.entrySet()){
      if(entry.getValue().getRate() == rate){
        movies.put(entry.getKey(), entry.getValue());
      }
    }
    return movies;
  }
}
