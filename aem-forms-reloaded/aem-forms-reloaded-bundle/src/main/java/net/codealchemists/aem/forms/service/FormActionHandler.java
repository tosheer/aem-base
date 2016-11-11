package net.codealchemists.aem.forms.service;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 * Service interface which all form Action handler will implement.
 * Created by tosheer.kalra on 19/07/2016.
 */
public interface FormActionHandler {

    /**
     *
     * Processes the form submit request data and generate the response for the form submission.
     * @param request form submit request.
     * @param response form submit response.
     * @return void.
     */
    void doProcess(SlingHttpServletRequest request, SlingHttpServletResponse response);

    /**
     * Returns name with which action handler is going to registered with FormActionController.
     * @return name with which action handler is going to registered with FormActionController.
     */
    String getName();

}
