package net.codealchemists.aem.forms.loader;

import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Form loader service interface which is to be implemented by various form loader.
 * Created by tosheer.kalra on 19/07/2016.
 */
public interface FormLoader {

    /**
     * Do various processes needed for form data and add request parameters in the request passed.
     * @param request form request.
     */
    void load(SlingHttpServletRequest request);

    /**
     * Name with which form loader get registered with form Loader service.
     * @return name of the loader.
     */
    String getName();

}
