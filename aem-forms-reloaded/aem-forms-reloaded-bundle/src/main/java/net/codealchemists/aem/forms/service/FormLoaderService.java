package net.codealchemists.aem.forms.service;

import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Service interface which is to be implemented by the service responsible for pre-populating form.
 * Created by tosheer.kalra on 19/07/2016.
 */
public interface FormLoaderService {

    /**
     * Calls the form loader which is registered with the passed form loader name.
     * @param request Form Rendering request.
     * @param formLoaderName loader name with which name is registered with the form FormLoaderService implementation.
     */
    void load(final SlingHttpServletRequest request, String formLoaderName);
}
