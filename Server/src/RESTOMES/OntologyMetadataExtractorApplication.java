package RESTOMES;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;


/**
 * This is the Application class for the OntologyMetadataExtractorApplication
 * It contains singleton objects representing the resource classes.
 *
 */
public class OntologyMetadataExtractorApplication extends Application
{
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();

    public OntologyMetadataExtractorApplication() {
        singletons.add(new OntologyResource());
    }

    @Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
