// This client is using the new JAX-RS 2.0 client interface

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

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
public class RESTOMESClient{
	public static String CreateJsonFile(String filePath, String fileName) throws FileNotFoundException, IOException{ 
		File file = new File(filePath);
		FileInputStream fstream = new FileInputStream(file);
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for(int n; (n = fstream.read(buffer)) != -1;){
			ostream.write(buffer, 0, n);
		}
		
		byte[] bytes = ostream.toByteArray();
        String fileString = new String(bytes);
		String object = "{\"name\":\"" + fileName + "\",\"content\":\"" + fileString + "\"}";
		return object;
	}
	
	public static void main( String[] args ){
		BufferedReader br = null;
		String output = null;
		try {
			String ontologyName = args[0].substring(args[0].lastIndexOf("/") + 1);
			String ontology = RESTOMESClient.CreateJsonFile(args[0], ontologyName);
			System.out.println( "Creating an ontology (JSON): " + ontologyName );
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget target = client.target( "http://uml.cs.uga.edu:8080/RESTOMES/rest/ontology" );
			Response response = target.request().post( Entity.entity( ontology, MediaType.APPLICATION_JSON ) );
			
			if( response.getStatus() != 201 ) {
				if(response.getStatus() == 303)
					System.out.println(ontologyName + " is already available");
				else
					throw new RuntimeException( "POST Request failed: HTTP code: " + response.getStatus() );
			}
			
			URI ontologyLink = response.getLocation();
			response.close();	// this response must be closed before we can reuse the client object
			System.out.println( "Ontology created; location: " + ontologyLink.toString() );
			
			// === Retrieve ontologies using a GET request and JSON representation ===
			// ===============================================================================
			// perform a GET request, asking for an JSON representation
			
			System.out.println();
			System.out.println( "Retrieving ontologies (JSON representation): " );
			target = client.target( "http://uml.cs.uga.edu:8080/RESTOMES/rest/ontology" );
			response = target.request( MediaType.APPLICATION_JSON ).get();
			
			if( response.getStatus() != 200 ) {
				throw new RuntimeException( "GET Request failed: HTTP code: " + response.getStatus() );
			}
			else {
				System.out.println( "OK: Retrieved the ontologies" );
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
