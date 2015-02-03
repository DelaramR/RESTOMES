package MoviePickRESTfulService;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;


/**
 * This is the Application class for the MoviePickRESTfulService
 * It contains singleton objects representing the resource classes.
 *
 */
public class MoviePickRESTfulServiceApplication extends Application
{
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();

    public MoviePickRESTfulServiceApplication() {
        singletons.add(new MoviePickRESTfulServiceResource());
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
