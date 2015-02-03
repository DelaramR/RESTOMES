package MoviePickRESTfulService;

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

@Path("/movie")
public class MovieResource{
  @POST
  @Consumes( MediaType.APPLICATION_XML )
  public Response createEntryXML( InputStream is )
  {
    System.out.println( "MovieResource.createEntry" );
    return Response.created( URI.create("/movie/" + id) ).build();
  }
}
