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
    static final String movie1 = "{\"movieTile\":\"American Sniper\",\"genre\":\"Action\,\"rate\":\"4\"}";
    static final String movie2 = "{\"movieTile\":\"Paddington\",\"genre\":\"Comedy\,\"rate\":\"3\"}";
    static final String movie3 = "{\"movieTile\":\"Black or White\",\"genre\":\"Drama\,\"rate\":\"2\"}";

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
 
	    URI link = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "Theater created; location: " + link.toString() );

    	    // === Create theater2 using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Creating a theater (JSON): " + theater2 );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/theater" );
	    response = target.request().post( Entity.entity( theater2, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI link1 = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "Theater created; location: " + link1.toString() );

    	    // === Create theater3 using POST request (JSON) ===
	    // ===================================================
	    System.out.println( "Creating a theater (JSON): " + theater3 );

	    target = client.target( "http://uml.cs.uga.edu:8080/cs8350_5/rest/theater" );
	    response = target.request().post( Entity.entity( theater3, MediaType.APPLICATION_JSON ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI link2 = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "Theater created; location: " + link2.toString() );

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

	    // === Retrieve the updated theater using a GET request and JSON representation ===
	    // ===============================================================================
	    // perform a GET request, asking for an JSON representation
	    System.out.println();
	    System.out.println( "Retrieving theater (JSON representation): " + link );

	    target = client.target( link );
            response = target.request( MediaType.APPLICATION_JSON ).get();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the theater" );

		String t = response.readEntity( String.class );
		System.out.println( t );
	    }
	    response.close();

	    // === Update theater3 to theater4 using a PUT request and JSON representation ===
	    // =============================================================================
	    System.out.println();
	    System.out.println( "Updating theater: " + link + " : (JSON): " + theater2 );

	    // create a new target
            target = client.target( link );
            response = target.request().put( Entity.entity( theater2, MediaType.APPLICATION_JSON ) );

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "PUT Request failed: HTTP code: " + response.getStatus() );
            }
	    else
		System.out.println( "OK: Updated the theater" );
	    response.close();


	    // === Retrieve the updated theater using a GET request and JSON representation ===
	    // ==============================================================================
	    System.out.println();
	    System.out.println( "Retrieving theater (XML representation): " + link );

	    // create a new target
            target = client.target( link );

	    // perform a GET request, asking for an XML representation
            response = target.request( MediaType.APPLICATION_JSON ).get();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the theater" );

		String t = response.readEntity( String.class );
		System.out.println( t );
	    }
	    response.close();
	


	    // === Delete the theater using a DELETE request ===
	    // ================================================
	    System.out.println( "Deleting the theater: " + link );

	    target = client.target( link );
	    response = target.request().delete();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "DELETE Request failed: HTTP code: " + response.getStatus() );
            }
	    else
		System.out.println( "OK: Deleted the theater" );
	    response.close();


	    // === Attempt to retrieve the deleted theater using a GET request using JSON representation
	    // === This request SHOULD fail
	    System.out.println();
	    System.out.println( "Retrieving theater (JSON representation): " + link );

	    target = client.target( link );
            response = target.request( MediaType.APPLICATION_JSON ).get();

            if( response.getStatus() == 404 ) {
                System.out.println( "GET Request failed: This entry doesn't exist: " + link );
            }
            else if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the theater" );

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
