package MoviePickRESTfulService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.resteasy.spi.NoLogWebApplicationException;

@Path("/movie")
public class MovieResource{
  
  private static final Map<Integer, Movie> movieDB = new HashMap<Integer, Movie>();
  
  @POST
  @Consumes( MediaType.APPLICATION_JSON )
  public Response createEntryJSON( Movie movie ){
    System.out.println( "MovieResource.createEntry" );
    Integer id = movieDB.size() + 1;
    movieDB.put(id, movie);
    return Response.created( URI.create("/movie/" + id) ).build();
  }
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<Integer, Movie> returnListJSON(){
    return movieDB;
  }
  
  @GET
  @Path( "{id}" )
  @Produces(MediaType.APPLICATION_JSON)
  public Movie getEntryJSON(@PathParam("id") Integer id){
    final Movie movie = movieDB.get(id);
    if(movie == null){
      throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
    }
    return movie;
  }
  
  @PUT
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateMovieJSON(@PathParam("id") Integer id, Movie movie){
    Movie current = movieDB.get(id);
    if(current == null){
      throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
    }
    current.setMovieTitle(movie.getMovieTitle());
    current.setGenre(movie.getGenre());
    current.setRate((current.getRate() + movie.getRate())/2);
    
    return Response.ok().build();
  }
  
  @DELETE
  @Path("{id}")
  public Response deleteMovie(@PathParam("id") Integer id){
     movieDB.remove(id);
     return Response.ok().build();
  }
}
