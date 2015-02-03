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
public class MoviePickRESTfulService 
{
    static final String person1 = "<?xml version=\"1.0\"?><person><name>Bill Watson</name><number>123-7856</number></person>";
    static final String person2 = "<?xml version=\"1.0\"?><person><name>Bill Watson</name><number>445-1234</number></person>";
    static final String person3 = "{\"theaterName\":\"University 16\",\"phoneNumber\":\"667-9876\"}";

    public static void main( String[] args )
    {
	BufferedReader br = null;
	String         output = null;

	try {

	    // === Create person1 a using POST request (XML) ===
	    // =================================================
	    System.out.println( "Creating a person (XML): " + person1 );

	    ResteasyClient client = new ResteasyClientBuilder().build();
	    ResteasyWebTarget target = client.target( "http://uml.cs.uga.edu:8080/phonebook/rest/phonebook" );
	    Response response = target.request().post( Entity.entity( person1, MediaType.APPLICATION_XML ) );

	    if( response.getStatus() != 201 ) {
                throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
	    }
 
	    URI link = response.getLocation();
	    response.close();	// this response must be closed before we can reuse the client object
	    System.out.println( "Person created; location: " + link.toString() );


	    // === Retrieve the updated person using a GET request and JSON representation ===
	    // ===============================================================================
	    // perform a GET request, asking for an XML representation
	    System.out.println();
	    System.out.println( "Retrieving person (JSON representation): " + link );

	    target = client.target( link );
            response = target.request( MediaType.APPLICATION_JSON ).get();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the person" );

		String p = response.readEntity( String.class );
		System.out.println( p );
	    }
	    response.close();

	
	    // === Update person1 to person2 data using a PUT request (XML) ===
	    // ================================================================
	    System.out.println();
	    System.out.println( "Updating person: " + link + " : (XML): " + person2 );

	    // create a new target
            target = client.target( link );
            response = target.request().put( Entity.entity( person2, MediaType.APPLICATION_XML ) );

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "PUT Request failed: HTTP code: " + response.getStatus() );
            }
	    else
		System.out.println( "OK: Updated the person" );
	    response.close();


	    // === Retrieve the updated person using a GET request and XML representation ===
	    // ==============================================================================
	    System.out.println();
	    System.out.println( "Retrieving person (XML representation): " + link );

	    // create a new target
            target = client.target( link );

	    // perform a GET request, asking for an XML representation
            response = target.request( MediaType.APPLICATION_XML ).get();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the person" );

		String p = response.readEntity( String.class );
		System.out.println( p );
	    }
	    response.close();
	

	    // === Update person1 to person3 using a PUT request and JSON representation ===
	    // =============================================================================
	    System.out.println();
	    System.out.println( "Updating person: " + link + " : (JSON): " + person3 );

	    // create a new target
            target = client.target( link );
            response = target.request().put( Entity.entity( person3, MediaType.APPLICATION_JSON ) );

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "PUT Request failed: HTTP code: " + response.getStatus() );
            }
	    else
		System.out.println( "OK: Updated the person" );
	    response.close();


	    // === Retrieve the updated person using a GET request and XML representation ===
	    // ==============================================================================
	    System.out.println();
	    System.out.println( "Retrieving person (XML representation): " + link );

	    // create a new target
            target = client.target( link );

	    // perform a GET request, asking for an XML representation
            response = target.request( MediaType.APPLICATION_XML ).get();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the person" );

		String p = response.readEntity( String.class );
		System.out.println( p );
	    }
	    response.close();
	


	    // === Delete the person using a DELETE request ===
	    // ================================================
	    System.out.println( "Deleting the person: " + link );

	    target = client.target( link );
	    response = target.request().delete();

            if( response.getStatus() != 200 ) {
                throw new RuntimeException( "DELETE Request failed: HTTP code: " + response.getStatus() );
            }
	    else
		System.out.println( "OK: Deleted the person" );
	    response.close();


	    // === Attempt to retrieve the deleted person using a GET request using JSON representation
	    // === This request SHOULD fail
	    System.out.println();
	    System.out.println( "Retrieving person (JSON representation): " + link );

	    target = client.target( link );
            response = target.request( MediaType.APPLICATION_JSON ).get();

            if( response.getStatus() == 404 ) {
                System.out.println( "GET Request failed: This entry doesn't exist: " + link );
            }
            else if( response.getStatus() != 200 ) {
                throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
            }
	    else {
		System.out.println( "OK: Retrieved the person" );

		String p = response.readEntity( String.class );
		System.out.println( p );
	    }
	    response.close();

	} 
	catch( Exception e ) {
	    System.out.println( e );
	    e.printStackTrace();
	}
    }
}
