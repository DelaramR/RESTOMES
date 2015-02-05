package MoviePickRESTfulService;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

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

// if you'd like to log this exception in JBoss log file, use WebApplicationException
// otherwise, use NoLogWebApplicationException, which will not log the exception
//import javax.ws.rs.WebApplicationException;
import org.jboss.resteasy.spi.NoLogWebApplicationException;

/**
 * Theater service implementation class
 * The main path is /phonebook.
 */
@Path( "/theater" )
public class TheaterResource
{
    // the 'data source' -- in reality, the data should be in a database
    public static final Map<Integer, Theater> theaterDB = new HashMap<Integer, Theater>();
    public static final ArrayList<ShowTime> showTimes = new ArrayList<ShowTime>();

    /**
     * Create a new theater entry using a JSON representation.
     * @param theater the new theater object data; this will be converted to POJO by a JSON provider (Jackson)
     * @return a response encoding
     */
    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    public Response createEntryJSON( Theater theater ) 
    {
        for(Map.Entry<Integer, Theater> entry : theaterDB.entrySet()){
            if(entry.getValue().equals(theater)){
                return Response.status(Response.Status.FOUND).build(); 
            }
        }
        Integer id = theaterDB.size() + 1;
        theaterDB.put(id, theater);
        return Response.created( URI.create("/theater/" + id) ).build();
    }
    
        /**
     * Retrieve a theater entry and return it as a streaming output, using a JSON representation
     * @param id path parameter identifying the theater entry
     * @return a theater object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Map<Integer, Theater> getEntryJSON() 
    {
        return theaterDB;
    }

    /**
     * Retrieve a theater entry and return it as a streaming output, using a JSON representation
     * @param id path parameter identifying the theater entry
     * @return a theater object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
    @GET
    @Path( "{id: [1-9][0-9]*}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Theater getEntryJSON(@PathParam("id") Integer id) 
    {
        final Theater theater = theaterDB.get(id);
        if (theater == null)
            throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
        return theater;
    }
    
    /**
     * Update a theater entry using a JSON representation
     * @param theater a new Theater object data to be used as an update
     * @param id path parameter identifying the theater resource to update
     * @return a response encoding
     */
    @PUT
    @Path( "{id: [1-9][0-9]*}" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response updateTheaterJSON( @PathParam("id") Integer id, Theater theater ) 
    {
        Theater current = theaterDB.get(id);
        if (current == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        current.setTheaterName( theater.getTheaterName() );
        current.setTheaterAddress( theater.getTheaterAddress() ); 
        return Response.ok().build();
    }
    
    /**
     * Delete a theater entry
     * @param id path parameter identifying the theater resource to delete
     * @return a response encoding
     */
    @DELETE
    @Path( "{id: [1-9][0-9]*}" )
    public Response deleteTheater( @PathParam("id") Integer id ) 
    {
        Theater theater = theaterDB.get(id);
        if(theater == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        theaterDB.remove( id );
        for(ShowTime time : showTimes){
            if(time.getTheater().equals(theater)){
                showTimes.remove(time);
            }
        }
        return Response.ok().build();
    }

    /**
     * Add an existing movie to the particular theater using a JSON representation.
     * @param movieId the id of a particualt movie
     * @param showTime the list of show times for a particualr movie to be added to the particular theater
     * @return a response encoding
     */
    @POST
    @Path( "{id: [1-9][0-9]*}/movie" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response registerTheaterForMovieJSON( @PathParam("id") Integer theaterId, IntStringArray movieIdShows ) 
    {
        Theater theater = theaterDB.get(theaterId);
        if (theater == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        int movieId = movieIdShows.getInteger();
        Movie movie = MovieResource.movieDB.get(movieId);
        if (movie == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        ArrayList<String> shows = movieIdShows.getStringArray();
        
        for(ShowTime time : showTimes){
            if(time.getMovie().equals(movie) && time.getTheater().equals(theater)){
                //time.addAllTime(shows);
                return Response.status(Response.Status.FOUND).build(); 
            }
        }
        ShowTime showTimeObj = new ShowTime(movie, theater, shows);
        showTimes.add(showTimeObj);
        return Response.created( URI.create("/theater/" + theaterId + "/movie/" + movieId) ).build();
    }
    
    /**
     * Retrieve list of movies from a particular theater and return it as a streaming output, using a JSON representation
     * @return a movie object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
    @GET
    @Path( "{id: [1-9][0-9]*}/movie/" )
    @Produces( MediaType.APPLICATION_JSON )
    public ArrayList<ShowTime> getMovieShowTimesInTheaterJSON( @PathParam("id") Integer theaterId) 
    {
        Theater theater = theaterDB.get(theaterId);
        if (theater == null)
            throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
         
        ArrayList<ShowTime> theaterShows = new ArrayList<ShowTime>();     
        for (ShowTime st : showTimes){
            if (st.getTheater().equals(theater))    
                theaterShows.add(st);
        }
        return theaterShows;
    }
    
     /**
     * Retrieve a particualr movie from a particular theater and return it as a streaming output, using a JSON representation
     * @return a movie object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
    @GET
    @Path( "{theaterId: [1-9][0-9]*}/movie/{movieId: [1-9][0-9]*}" )
    @Produces( MediaType.APPLICATION_JSON )
    public ArrayList<ShowTime> getShowTimesOfTheaterForMovieJSON( @PathParam("theaterId") Integer theaterId, @PathParam("movieId") Integer movieId) 
    {
        ArrayList<ShowTime> shows = new ArrayList<ShowTime>();
        Theater theater = theaterDB.get(theaterId);
        if (theater == null)
            throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
            Movie movie = MovieResource.movieDB.get(movieId);
        if (movie == null)
            throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
        for (ShowTime show : showTimes){
            if (show.getTheater().equals(theater) && show.getMovie().equals(movie))    
              shows.add(show);
        }
        return shows;
    }
    
    @DELETE
    @Path("{id: [1-9][0-9]*}/movie/{id1: [1-9][0-9]*}")
    public Response deleteShowsJSON(@PathParam("id") Integer theaterId, @PathParam("id1") Integer movieId){
        Movie movie = MovieResource.movieDB.get(movieId);
        if(movie == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        Theater theater = theaterDB.get(theaterId);
        if(theater == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        for(ShowTime time : showTimes){
            if(time.getMovie().equals(movie) && time.getTheater().equals(theater)){
                TheaterResource.showTimes.remove(time);
            }
        }
    return Response.ok().build();
  }
}
