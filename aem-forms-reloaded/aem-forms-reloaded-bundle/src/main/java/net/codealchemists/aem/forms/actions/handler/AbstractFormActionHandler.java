package net.codealchemists.aem.forms.actions.handler;

import com.day.cq.wcm.foundation.forms.ValidationInfo;
import net.codealchemists.aem.forms.constants.ActionHandlerConstants;
import net.codealchemists.aem.forms.service.FormActionHandler;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Abstract class for doing the common task in {@link FormActionHandler}.
 * Created by tosheer.kalra on 25/10/2016.
 */
public abstract class AbstractFormActionHandler implements FormActionHandler {

    /**
     * Method for setting the error message when an exception happened while request processing by
     * {@link FormActionHandler}.
     *
     * @param slingHttpServletRequest form submit request.
     * @param errorMessage error message to be displayed.
     */
    public void addErrorMessage(final SlingHttpServletRequest slingHttpServletRequest, final String errorMessage) {
        final ValidationInfo validationInfo = ValidationInfo.createValidationInfo(slingHttpServletRequest);
        validationInfo.addErrorMessage(null, errorMessage);
    }

    /**
     * Method for setting the redirect page path when a form is successfully submitted by
     * {@link FormActionHandler}.
     *
     * Thank You Page configured in the form start component will take priority if configured.
     *
     * @param slingHttpServletRequest form submit request.
     * @param redirectPagePath redirect page path where form should redirect after successful submission.
     */
    public void setRedirectPath(final SlingHttpServletRequest slingHttpServletRequest, final String redirectPagePath) {
        slingHttpServletRequest.setAttribute(ActionHandlerConstants.MAISON_REDIRECT_PAGE_PATH_KEY, redirectPagePath);
    }

    /**
     * Method for setting the ajax success message when successful submission of ajax form submit happens
     * {@link FormActionHandler}.
     *
     * Should only be used if it is a Ajax form submit.
     *
     * Ajax success message configured in the form start component will take priority if configured.
     *
     * @param slingHttpServletRequest form submit request.
     * @param successMessage success message to be displayed.
     */
    public void setAjaxSuccessMessage(final SlingHttpServletRequest slingHttpServletRequest,
                                      final String successMessage) {
        slingHttpServletRequest.setAttribute(ActionHandlerConstants.MAISON_AJAX_SUCCESS_MESSAGE_KEY, successMessage);
    }

}
