package net.codealchemists.aem.core.utils;


import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * It's used provide service level authentication and provide resource Resolver to service as per 
 * the area on which the service needs the access. 
 * 
 * <br>
 * This approach is needed as in AEM 6.1 administrative ResourceResolvers and Admin JCR Sessions usage is deprecated.
 * More about this: https://sling.apache.org/documentation/the-sling-engine/service-authentication.html
 * 
 * @author tosheer.kalra
 *
 */
public class ResourceResolverHelper {

	/**
	 * User mapping which has access to Read app content.
	 */
	public static final String APPS_READ_USER = "read-apps-data";

	/**
	 *
	 * @param resourceResolverFactory resource resolver Factory object from caller.
	 * @param user user with whose permission the resource can be resolved.
	 * @return Resource resolver.
	 * @throws LoginException if login of the user failed.
	 */
	private static ResourceResolver getResourceResolver(
			final ResourceResolverFactory resourceResolverFactory, 
			final String user) throws LoginException {
		final Map<String, Object> serviceAuthMap = new HashMap<>();
        serviceAuthMap.put(ResourceResolverFactory.SUBSERVICE, user);
        return resourceResolverFactory.getServiceResourceResolver(serviceAuthMap);
	}

    /**
     * Gives you the Resource Resolver which has Read access to codealchemists folder in apps hierarchy.
     * @param resourceResolverFactory resourceResolverFactory reference passed from calling method.
     * @return Resource Resolver which has Read access to apps data hierarchy.
     * @throws LoginException if login of the user failed.
     */
    public static ResourceResolver getReadResourceResolverOnAppsData(
            final ResourceResolverFactory resourceResolverFactory) throws LoginException {
        return getResourceResolver(resourceResolverFactory, APPS_READ_USER);
    }

}
