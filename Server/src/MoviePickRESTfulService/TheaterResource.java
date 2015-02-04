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
    @Path( "{id}" )
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
    @Path( "{id}" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response updateTheaterJSON( @PathParam("id") Integer id, Theater theater ) 
    {
        Theater current = theaterDB.get(id);
        if (current == null)
            throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
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
    @Path( "{id}" )
    public Response deleteTheater( @PathParam("id") Integer id ) 
    {
        theaterDB.remove( id );
        return Response.ok().build();
    }

    /**
     * Add an existing movie to the particular theater using a JSON representation.
     * @param movieId the id of a particualt movie
     * @param showTime the list of show times for a particualr movie to be added to the particular theater
     * @return a response encoding
     */
    @POST
    @Path( "{id}/movie" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response registerTheaterForMovieJSON( @PathParam("id") Integer theaterId, Integer movieId, ArrayList<String> showTime ) 
    {
        Theater theater = theaterDB.get(theaterId);
        if (theater == null)
            throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
        Movie movie = MovieResource.movieDB.get(movieId);
        if (movie == null)
            throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
        
        ShowTime showTimeObj = new ShowTime();
        showTimeObj.setTime(showTime);
        showTimeObj.setTheater(theater);
        showTimeObj.setMovie(movie);
        
        showTimes.add(showTimeObj);
        
        return Response.created( URI.create("/theater/" + theaterId + "/movie/") ).build();
    }
    
    /**
     * Retrieve list of movies from a particular theater and return it as a streaming output, using a JSON representation
     * @return a movie object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
    @GET
    @Path( "{id}/movie/" )
    @Produces( MediaType.APPLICATION_JSON )
    public ArrayList<ShowTime> getMovieShowTimesInTheaterJSON( @PathParam("id") Integer theaterId) 
    {
        Theater theater = theaterDB.get(theaterId);
        if (theater == null)
            throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
         
    ArrayList<ShowTime> theaterShows = new ArrayList<ShowTime>();     
    for (ShowTime st : showTimes){
        if (st.getTheater().equals(theater))    
            theaterShows.add(st);
    }
        return theatershows;
    }
    
     /**
     * Retrieve a particualr movie from a particular theater and return it as a streaming output, using a JSON representation
     * @return a movie object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
    @GET
    @Path( "{theaterId}/movie/{movieId}" )
    @Produces( MediaType.APPLICATION_JSON )
    public ShowTime getShowTimesOfTheaterForMovieJSON( @PathParam("theaterId") Integer theaterId, @PathParam("movieId") Integer movieId) 
    {
        Theater theater = theaterDB.get(theaterId);
        if (theater == null)
            throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
            Movie movie = movieDB.get(movieId);
        if (movie == null)
            throw new NoLogWebApplicationException(Response.Status.NOT_FOUND);
         
    ShowTime movieShows = new ShowTime();     
    for (ShowTime st : showTimes){
        if (st.getTheater().equals(theater) && st.getMovie().equals(movie))    
            movieShows = st;
    }
        return movieShows;
    }
}
