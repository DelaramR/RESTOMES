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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



@Path( "/theater" )
public class TheaterResource
{
    // the 'data source'
    //private final Map<Integer, Person> phoneDB = new HashMap<Integer, Person>();

    @POST
    @Consumes( "application/xml" )
    public Response createEntry( InputStream is ) 
    {
       System.out.println( "TheaterResource.createEntry" );
        Theater theater = readTheater(is);
        // create a new id for the theater
        //Integer id = phoneDB.size() + 1;
        //phoneDB.put(id, person);

        return Response.created( URI.create("/theater/" + id) ).build();
    }

    @GET
    @Path( "{id}" )
    @Produces( "application/xml" )
    public StreamingOutput getEntry(@PathParam("id") Integer id) 
    {
        //final Theatre theater = phoneDB.get(id);

        if (theater == null) {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }

        return new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                outputPerson(output, theater);
            }
        };
    }

    @PUT
    @Path( "{id}" )
    @Consumes( "application/xml" )
    public Response updatePerson( @PathParam("id") Integer id, InputStream is ) 
    {
        Theater update = readTheater(is);
        //Theater current = phoneDB.get(id);

        if (current == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        current.setTheaterName( update.getTheaterName() );
        current.setTheaterAdress( update.getTheaterAddress() ); 
        
        return Response.ok().build();
    }

    @DELETE
    @Path( "{id}" )
    public Response deletePerson( @PathParam("id") Integer id ) 
    {

        phoneDB.remove( id );

        return Response.ok().build();
    }

    private Person readTheater( InputStream is ) 
    {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(is);
            
            // Get the document's root XML node
            NodeList root = doc.getChildNodes();
            Node theaterNode = getNode( "theater", root );
           
            NodeList nodes = personNode.getChildNodes();
            
            String theaterName = getNodeValue("theaterName", nodes);
            String theaterAdress = getNodeValue("theaterAddress", nodes);
            
            System.out.println( "Theater name: " + theaterName );
            System.out.println( "Theater address: " + theaterAddress );

            Theater theater = new Theater( theaterName, theaterAddress );

            return theater;
        }
        catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
        }
    }
    
    private void outputTheater( OutputStream os, Theater theater ) 
            throws IOException 
    {
        PrintStream writer = new PrintStream(os);
        writer.println("<theater>");
        writer.println("   <theaterName>" + theater.getTheaterName() + "</theaterName>");
        writer.println("   <theaterAddress>" + theater.getTheaterAddress() + "</theaterAddress>");
        writer.println("</theater>");
        writer.close();
    }

    private Node getNode(String tagName, NodeList nodes) 
    {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }

        return null;
    }
    
    public String getNodeValue(String tagName, NodeList nodes ) 
    {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++ ) {
                    Node data = childNodes.item(y);
                    if ( data.getNodeType() == Node.TEXT_NODE )
                        return data.getNodeValue();
                }
            }
        }
        return "";
    }
}
