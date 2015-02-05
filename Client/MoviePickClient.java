// This client is using the new JAX-RS 2.0 client interface

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Link;

import javax.ws.rs.client.Entity;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;


// This client is using the new JAX-RS 2.0 client interface
//
public class MoviePickClient 
{
    static final String theater1 = "{\"theaterName\":\"University 16\",\"theaterAddress\":\"Oconee Connector\"}";
    static final String theater2 = "{\"theaterName\":\"Carmike\",\"theaterAddress\":\"Lexington Rd\"}";
    static final String theater3 = "{\"theaterName\":\"BeechWood\",\"theaterAddress\":\"Alps Rd\"}";
    
    static final String movie1 = "{\"movieTitle\":\"American Sniper\",\"genre\":\"Wrong genre!\",\"rate\":1}";
    static final String movie2 = "{\"movieTitle\":\"Paddington\",\"genre\":\"Comedy\",\"rate\":3}";
    static final String movie3 = "{\"movieTitle\":\"Black or White\",\"genre\":\"Drama\",\"rate\":2}";
    static final String movie4 = "{\"movieTitle\":\"The Loft\",\"genre\":\"Thriller\",\"rate\":4}";
    static final String movie5 = "{\"movieTitle\":\"American Sniper\",\"genre\":\"Action\",\"rate\":5}";

    static final String movie1theater1show = "{\"integer\":1,\"stringArray\":[\"Tuesdays 2:30\",\"Thursdays 5:30\"]}";
    static final String movie1theater2show = "{\"integer\":1,\"stringArray\":[\"Fridays 3:00\"]}";
    static final String movie1theater3show = "{\"integer\":1,\"stringArray\":[\"Saturdays 7:30\",\"Sundays 6:00\"]}";
    
    static final String theater1movie2show = "{\"integer\":1,\"stringArray\":[\"Mondays 4:30\"]}";
    static final String theater1movie3show = "{\"integer\":1,\"stringArray\":[\"Sundays 8:30\"]}";
    static final String theater2movie2show = "{\"integer\":2,\"stringArray\":[\"Saturdays 10:00\",\"Wednesdays 11:00\"]}";
    static final String theater3movie3show = "{\"integer\":3,\"stringArray\":[\"Fridays 6:30\"]}";
	
    public static void main( String[] args )
    {
	BufferedReader br = null;
	String output = null;

	try {

	    // === Create theater1 using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Creating a theater (JSON): " + theater1 );

	    ResteasyClient client = new ResteasyClientBuilder().build();
	    ResteasyWebTarget target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/theater" );
	    Response response = target.request().post( Entity.entity( theater1, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI theaterlink = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "Theater created; location: " + theaterlink.toString() );

    	    // === Create theater2 using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Creating a theater (JSON): " + theater2 );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/theater" );
	    response = target.request().post( Entity.entity( theater2, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI theaterlink1 = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "Theater created; location: " + theaterlink1.toString() );

    	    // === Create theater3 using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Creating a theater (JSON): " + theater3 );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/theater" );
	    response = target.request().post( Entity.entity( theater3, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI theaterlink2 = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "Theater created; location: " + theaterlink2.toString() );

	    // === Retrieve theaters using a GET request and JSON representation ===
	    // ===============================================================================
	    // perform a GET request, asking for an JSON representation
	    System.out.println();
	    System.out.println( "Retrieving theaters (JSON representation): " );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/theater" );
            response = target.request( MediaType.APPLICATION_JSON ).get();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the theaters" );

		String t = response.readEntity( String.class );
		System.out.println( t );
	    }
	    response.close();
	    
	    // === Create movie1 using POST request (JSON) ===
	    // ===================================================
	    System.out.println();
	    System.out.println( "Creating a movie (JSON): " + movie1 );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/movie" );
	    response = target.request().post( Entity.entity( movie1, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI movielink = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "Movie created; location: " + movielink.toString() );

    	    // === Create movie2 using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Creating a movie (JSON): " + movie2 );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/movie" );
	    response = target.request().post( Entity.entity( movie2, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI movielink1 = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "Movie created; location: " + movielink1.toString() );

    	    // === Create movie3 using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Creating a movie (JSON): " + movie3 );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/movie" );
	    response = target.request().post( Entity.entity( movie3, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI movielink2 = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "Movie created; location: " + movielink2.toString() );

	    // === Create movie4 using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Creating a movie (JSON): " + movie4 );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/movie" );
	    response = target.request().post( Entity.entity( movie4, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI movielink3 = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "Movie created; location: " + movielink3.toString() );

	    // === Update movie1 to movie5 using a PUT request and JSON representation ===
	    // =============================================================================
	    System.out.println();
	    System.out.println( "Updating movie: " + movielink + " : (JSON): " + movie1 );

	    // create a new target
            target = client.target( movielink );
            response = target.request().put( Entity.entity( movie5, MediaType.APPLICATION_JSON ) );

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "PUT Request failed: HTTP code: " + response.getStatus() );
            }
	    else
		System.out.println( "OK: Updated the movie" );
	    response.close();

	    // === Retrieve the updated movie using a GET request and JSON representation ===
	    // ===============================================================================
	    // perform a GET request, asking for an JSON representation
	    System.out.println();
	    System.out.println( "Retrieving movie (JSON representation): " + movielink );

	    target = client.target( movielink );
            response = target.request( MediaType.APPLICATION_JSON ).get();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the movie" );

		String m = response.readEntity( String.class );
		System.out.println( m );
	    }
	    response.close();

	    // === Delete the movie using a DELETE request ===
	    // ================================================
	    System.out.println();
	    System.out.println( "Deleting the movie: " + movielink3 );

	    target = client.target( movielink3 );
	    response = target.request().delete();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "DELETE Request failed: HTTP code: " + response.getStatus() );
            }
	    else
		System.out.println( "OK: Deleted the movie" );
	    response.close();

	    // === Attempt to retrieve the deleted theater using a GET request using JSON representation
	    // === This request SHOULD fail
	    System.out.println();
	    System.out.println( "Retrieving theater (JSON representation): " + movielink3 );

	    target = client.target( movielink3 );
            response = target.request( MediaType.APPLICATION_JSON ).get();

            if( response.getStatus() == 404 ) {
                System.out.println( "GET Request failed: This entry doesn't exist: " + movielink3 );
            }
            else if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the movie" );

		String t = response.readEntity( String.class );
		System.out.println( t );
	    }
	    response.close();

    	    // === Add movie1theater1show using POST request (JSON) ===
	    // ===================================================
	    System.out.println();
	    System.out.println( "Adding a movie and its showtimes to theater (JSON): " + movie1theater1show );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/theater/1/movie" );
	    response = target.request().post( Entity.entity( movie1theater1show, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI movie1theater1link = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "movie and its showtimes added; location: " + movie1theater1link.toString() );
	    
	    // === Add movie1theater2show using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Adding a movie and its showtimes to theater (JSON): " + movie1theater2show );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/theater/2/movie" );
	    response = target.request().post( Entity.entity( movie1theater2show, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI movie1theater2link = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "movie and its showtimes added; location: " + movie1theater2link.toString() );
	    
	    // === Add movie1theater3show using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Adding a movie and its showtimes to theater (JSON): " + movie1theater3show );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/theater/3/movie" );
	    response = target.request().post( Entity.entity( movie1theater3show, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
	     
	    URI movie1theater3link = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "movie and its showtimes added; location: " + movie1theater3link.toString() );
 
 	    // === Add theater1movie2show using POST request (JSON) ===
	    // ===================================================
	    System.out.println();
	    System.out.println( "Adding a theater and the showtimes to theaters that show a movie (JSON): " + theater1movie2show );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/movie/2/theater" );
	    response = target.request().post( Entity.entity( theater1movie2show, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI theater1movie2link = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "theater and the showtimes for a movie added; location: " + theater1movie2link.toString() );
	    
	    // === Add theater1movie3show using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Adding a theater and the showtimes to theaters that show a movie (JSON): " + theater1movie3show );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/movie/3/theater" );
	    response = target.request().post( Entity.entity( theater1movie3show, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI theater1movie3link = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "theater and the showtimes for a movie added; location: " + theater1movie3link.toString() );
	    
	    // === Add theater2movie2show using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Adding a theater and the showtimes to theaters that show a movie (JSON): " + theater2movie2show );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/movie/2/theater" );
	    response = target.request().post( Entity.entity( theater2movie2show, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI theater2movie2link = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "theater and the showtimes for a movie added; location: " + theater2movie2link.toString() );
	    
	    // === Add theater3movie3show using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Adding a theater and the showtimes to theaters that show a movie (JSON): " + theater3movie3show );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/movie/3/theater" );
	    response = target.request().post( Entity.entity( theater3movie3show, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI theater3movie3link = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "theater and the showtimes for a movie added; location: " + theater3movie3link.toString() );
	    
	    // === Retrieve movies that are shown in theater1 using a GET request and JSON representation ===
	    // ===============================================================================
	    // perform a GET request, asking for an JSON representation
	    System.out.println();
	    System.out.println( "Retrieving movies (JSON representation): " );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/theater/1/movie" );
            response = target.request( MediaType.APPLICATION_JSON ).get();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the movies of the theater" );

		String m = response.readEntity( String.class );
		System.out.println( m );
	    }
	    response.close();
	    
	    // === Retrieve theaters that are showing movie3 using a GET request and JSON representation ===
	    // ===============================================================================
	    // perform a GET request, asking for an JSON representation
	    System.out.println();
	    System.out.println( "Retrieving theaters (JSON representation): " );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/movie/3/theater" );
            response = target.request( MediaType.APPLICATION_JSON ).get();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the theaters that are showing the movie" );

		String t = response.readEntity( String.class );
		System.out.println( t );
	    }
	    response.close();
	    
	    // === Retrieve movie1 from movies that are shown in theater1 using a GET request and JSON representation ===
	    // ===============================================================================
	    // perform a GET request, asking for an JSON representation
	    System.out.println();
	    System.out.println( "Retrieving a movie from a theater (JSON representation): " );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/theater/1/movie/1" );
            response = target.request( MediaType.APPLICATION_JSON ).get();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the movie and its show time from a theater" );

		String m = response.readEntity( String.class );
		System.out.println( m );
	    }
	    response.close();
	    
	} 
	catch( Exception e ) {
	    System.out.println( e );
	    e.printStackTrace();
	}
    }
}
