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

import org.jboss.resteasy.spi.NoLogWebApplicationException;

@Path("/movie")
public class MovieResource{
  
  public static final Map<Integer, Movie> movieDB = new HashMap<Integer, Movie>();
  
  /**
     * Create a new movie entry using a JSON representation.
     * @param movie the new movie object data; this will be converted to POJO by a JSON provider (Jackson)
     * @return a response encoding
     */
  @POST
  @Consumes( MediaType.APPLICATION_JSON )
  public Response createEntryJSON( Movie movie ){
    System.out.println( "MovieResource.createEntry" );
    for(Map.Entry<Integer, Movie> entry : movieDB.entrySet()){
      if(entry.getValue().equals(movie)){
        return Response.seeOther(URI.create("/movie/" + entry.getKey())).build(); 
      }
    }
    Integer id = movieDB.size() + 1;
    movieDB.put(id, movie);
    return Response.created( URI.create("/movie/" + id) ).build();
  }
  
     /**
     * Retrieve all movies and return them as an object, using a JSON representation
     * @return map object of all movies; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<Integer, Movie> returnListJSON(){
    return movieDB;
  }
  
     /**
     * Retrieve a movie entry and return it as an object, using a JSON representation
     * @param id path parameter identifying the movie entry
     * @return a movie object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path( "{id: [1-9][0-9]*}" )
  @Produces(MediaType.APPLICATION_JSON)
  public Movie getEntryJSON(@PathParam("id") Integer id){
    final Movie movie = movieDB.get(id);
    if(movie == null){
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    }
    return movie;
  }
  
  /**
     * Update a movie entry using a JSON representation
     * @param movie a new movie object data to be used as an update
     * @param id path parameter identifying the movie resource to update
     * @return a response encoding
     */
  @PUT
  @Path("{id: [1-9][0-9]*}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateMovieJSON(@PathParam("id") Integer id, Movie movie){
    Movie current = movieDB.get(id);
    if(current == null){
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    current.setMovieTitle(movie.getMovieTitle());
    current.setGenre(movie.getGenre());
    current.setRate((current.getRate() + movie.getRate())/2);
    
    return Response.ok().build();
  }
  
  /**
     * Delete a movie entry
     * @param id path parameter identifying the movie resource to delete
     * @return a response encoding
     */
  @DELETE
  @Path("{id: [1-9][0-9]*}")
  public Response deleteMovie(@PathParam("id") Integer id){
    Movie movie = movieDB.get(id);
    if(movie == null)
      return Response.status(Response.Status.NOT_FOUND).build();
    movieDB.remove(id);
    for(ShowTime time : TheaterResource.showTimes){
      if(time.getMovie().equals(movie)){
        TheaterResource.showTimes.remove(time);
      }
    }
    return Response.ok().build();
  }
  
  /**
     * Retrieve a list of theaters that are showing a movie with the showtims and return it as an object, using a JSON representation
     * @param id path parameter identifying the movie entry
     * @return a list of showtims object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path("{id: [1-9][0-9]*}/theater")
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayList<ShowTime> getTheatersShowTimeForMovie(@PathParam("id") Integer id){
    ArrayList<ShowTime> shows = new ArrayList<ShowTime>();
    Movie movie = movieDB.get(id);
    if(movie == null)
      throw new NoLogWebApplicationException( Response.Status.NOT_FOUND );
    for(ShowTime show : TheaterResource.showTimes){
      if(show.getMovie().equals(movie)){
        shows.add(show);
      }
    }
    return shows;
  }
  
  /**
     * Register a movie to be shown in a theater, using a JSON representation
     * @param id path parameter identifying the movie entry
     * @return a response encoding
     */
  @POST
  @Path("{id: [1-9][0-9]*}/theater")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerMovie2Theater(@PathParam("id") Integer movieId, IntStringArray theaterIdShows){
    Movie movie = movieDB.get(movieId);
    if(movie == null)
      return Response.status(Response.Status.NOT_FOUND).build();
    int theaterId = theaterIdShows.getInteger();
    Theater theater = TheaterResource.theaterDB.get(theaterId);
    if(theater == null)
      return Response.status(Response.Status.NOT_FOUND).build();
    ArrayList<String> shows = theaterIdShows.getStringArray();
    for(ShowTime time : TheaterResource.showTimes){
      if(time.getMovie().equals(movie) && time.getTheater().equals(theater)){
        //time.addAllTime(shows);
        return Response.seeOther(URI.create("/movie/" + movieId + "/theater/" + theaterId)).build(); 
      }
    }
    ShowTime newShowTime = new ShowTime(movie, theater, shows);
    TheaterResource.showTimes.add(newShowTime);
    return Response.created( URI.create("/movie/" + movieId + "/theater/" + theaterId) ).build();
  }
  
  /**
     * Retrieve movie showtims in a theater return it as an object, using a JSON representation
     * @param id path parameter identifying the movie entry
     * @param id1 path parameter identifying the theater entry
     * @return a list of showtims object requested; it will be converted to JSON using a JSON provider (Jackson)
     */
  @GET
  @Path("{id: [1-9][0-9]*}/theater/{id1: [1-9][0-9]*}")
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayList<ShowTime> getShowTimesOfMovieInTheaterJSON(@PathParam("id") Integer movieId, @PathParam("id1") Integer theaterId){
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
