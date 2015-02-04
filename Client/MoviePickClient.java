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
    
    static final String movie1 = "{\"movieTile\":\"American Sniper\",\"genre\":\"Wrong genre\",\"rate\":\"5\"}";
    static final String movie2 = "{\"movieTile\":\"Paddington\",\"genre\":\"Comedy\",\"rate\":\"3\"}";
    static final String movie3 = "{\"movieTile\":\"Black or White\",\"genre\":\"Drama\",\"rate\":\"2\"}";
    static final String movie4 = "{\"movieTile\":\"The Loft\",\"genre\":\"Thriller\",\"rate\":\"4\"}";
    static final String movie5 = "{\"movieTile\":\"American Sniper\",\"genre\":\"Action\",\"rate\":\"4\"}";

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
	    System.out.println( "Creating a movie (JSON): " + movie1 );

	    ResteasyClient client = new ResteasyClientBuilder().build();
	    ResteasyWebTarget target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/movie" );
	    Response response = target.request().post( Entity.entity( movie1, MediaType.APPLICATION_JSON ) );

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
	    response = target.request().post( Entity.entity( movie1, MediaType.APPLICATION_JSON ) );

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
	    System.out.println( "Updating movie: " + movielink + " : (JSON): " + movie5 );

	    // create a new target
            target = client.target( movielink );
            response = target.request().put( Entity.entity( movie4, MediaType.APPLICATION_JSON ) );

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

	} 
	catch( Exception e ) {
	    System.out.println( e );
	    e.printStackTrace();
	}
    }
}
