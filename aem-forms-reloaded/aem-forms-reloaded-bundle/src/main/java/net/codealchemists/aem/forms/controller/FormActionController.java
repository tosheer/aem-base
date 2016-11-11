package net.codealchemists.aem.forms.controller;

import com.day.cq.wcm.foundation.forms.FormsHelper;
import com.day.cq.wcm.foundation.forms.ValidationInfo;
import net.codealchemists.aem.forms.constants.ActionHandlerConstants;
import net.codealchemists.aem.forms.service.FormActionHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.apache.sling.api.wrappers.SlingHttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This ActionController will act as submit request controller and maps various form
 * submit request to form action handlers.
 * Created by tosheer.kalra on 19/07/2016.
 */
@Service
@Component
@SlingServlet(
        generateComponent = false,
        resourceTypes = "foundation/components/form/start",
        methods = HttpPost.METHOD_NAME,
        extensions = "html",
        selectors = "formActionHandler")
public class FormActionController extends SlingAllMethodsServlet {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormActionController.class);

    private static final String ATTR_RESOURCE = "com.day.cq.wcm.foundation.forms.impl.FormsHandlingServlet/resource";

    @Reference(referenceInterface = FormActionHandler.class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC, bind = "bindFormActionHandlers", unbind = "unbindFormActionHandlers")
    private final Map<String, FormActionHandler> actionHandlers = new ConcurrentHashMap<>();

    /**
     * OSGI framework service reference bind method.
     * @param formActionHandler form action handler service reference.
     */
    protected void bindFormActionHandlers(final FormActionHandler formActionHandler) {
        this.actionHandlers.put(formActionHandler.getName(), formActionHandler);

    }

    /**
     * OSGI framework service reference unbind method.
     * @param formActionHandler form action handler service reference.
     */
    protected void unbindFormActionHandlers(final FormActionHandler formActionHandler) {
        this.actionHandlers.remove(formActionHandler.getName());
    }

    /**
     *
     * Method act as an controller for all the form submit request which has successfully passed the server side
     * validation and has Action Type selected as "CMS Base Form Submit Action" in form start component.
     *
     * It does the following tasks.
     * 1. Get the action handler name from request and execute that action handler doProcess method.
     * 2. Depending upon if it is normal form submit request or Ajax submitted request prepare the response.
     *
     * @param request form submit request.
     * @param response form submit response.
     * @throws ServletException for exception happen while request processing.
     * @throws IOException for exception happen while request processing.
     */
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws ServletException, IOException {

        final String actionName = (String)request.getAttribute(ActionHandlerConstants.SUBMIT_ACTION_NAME);
        LOGGER.debug("Submit request is received for {}", actionName);
        final FormActionHandler formActionHandler = actionHandlers.get(actionName);
        if (formActionHandler != null) {
            formActionHandler.doProcess(request, new NoRedirectSlingHttpServletResponseWrapper(response));
        }

        final ValueMap formStartValueMap = request.getResource().getValueMap();
        if (formStartValueMap.get(ActionHandlerConstants.AJAX_CALL_FLAG_PROPERTY_NAME, false)) {
            handleAjaxFormSubmission(request, response, formStartValueMap);
        } else {
            handleNormalFormSubmission(request, response, formStartValueMap);
        }
    }

    /**
     * Method creates ajax form submit response.
     * In case of the ajax form request response is always forward to the same page from where request came in.
     *
     * @param request form submit request
     * @param response form submit response.
     * @param formStartValueMap value map of form start component.
     * @throws IOException for exception happen while request processing.
     * @throws ServletException for exception happen while request processing.
     */
    private void handleAjaxFormSubmission(final SlingHttpServletRequest request,
                                          final SlingHttpServletResponse response,
                                          final ValueMap formStartValueMap) throws ServletException, IOException {
        final Resource formSubmitOriginalResource = (Resource) request.getAttribute(ATTR_RESOURCE);

        String redirectPagePath = getRedirectPage(request, formStartValueMap);
        if (StringUtils.isNotEmpty(redirectPagePath)) {
            request.setAttribute(ActionHandlerConstants.REDIRECT_PATH_KEY, redirectPagePath);
        }
        String successMessage = getSuccessMessage(request, formStartValueMap);
        if (StringUtils.isNotEmpty(successMessage))
            request.setAttribute(
                    ActionHandlerConstants.SUCCESS_MESSAGE_KEY, getSuccessMessage(request, formStartValueMap));

        request.removeAttribute(ATTR_RESOURCE);
        request.getRequestDispatcher(formSubmitOriginalResource).forward(new NonPostRequestWrapper(request), response);
        request.removeAttribute(ActionHandlerConstants.SUCCESS_MESSAGE_KEY);
        request.removeAttribute(ActionHandlerConstants.REDIRECT_PATH_KEY);
    }

    /**
     *
     * Method creates normal form submit response by doing following tasks.
     *
     * 1. It checks if any error message is set by action handler in ValidationInfo for current request.
     * 2. If it found no ValidationInfo exists for the request it redirect to Thank You Page path configured. If no
     * Path is configured. Redirect to the referrer page is done.
     * 3. If Validation Info exists for request it forwards the request to the same page.
     *
     * @param request form submit request
     * @param response form submit response.
     * @param formStartValueMap value map of form start component.
     * @throws IOException for exception happen while request processing.
     * @throws ServletException for exception happen while request processing.
     */
    private void handleNormalFormSubmission(final SlingHttpServletRequest request,
                                            final SlingHttpServletResponse response,
                                            final ValueMap formStartValueMap) throws IOException, ServletException {
        final ValidationInfo validationInfo = ValidationInfo.getValidationInfo(request);
        if (validationInfo == null) {
            final String redirectPagePath = getRedirectPage(request, formStartValueMap);
            if (StringUtils.isNotEmpty(redirectPagePath)) {
                response.sendRedirect(request.getResourceResolver().map(request, redirectPagePath));
            }
            else {
                response.sendRedirect(request.getResourceResolver().map(request, FormsHelper.getReferrer(request)));
            }
        } else {
            final Resource formSubmitOriginalResource = (Resource) request.getAttribute(ATTR_RESOURCE);
            request.removeAttribute(ATTR_RESOURCE);
            request.getRequestDispatcher(formSubmitOriginalResource).forward(
                    new NonPostRequestWrapper(request), response);
        }
    }

    private String getRedirectPage(SlingHttpServletRequest request, ValueMap formStartValueMap) {
        String redirectUrl = null;
        final Object maisonRedirectPagePath =
                request.getAttribute(ActionHandlerConstants.MAISON_REDIRECT_PAGE_PATH_KEY);
        if(maisonRedirectPagePath != null) {
            redirectUrl = (String) maisonRedirectPagePath;
        }

        final String thankYouPagePath = formStartValueMap
                .get(ActionHandlerConstants.FORM_REDIRECT_PATH_KEY, String.class);
        if (StringUtils.isNotEmpty(thankYouPagePath)) {
            redirectUrl = thankYouPagePath;
        }
        if (StringUtils.isNotEmpty(redirectUrl) && !redirectUrl.contains(ActionHandlerConstants.HTML_EXTENSION)) {
            redirectUrl += ActionHandlerConstants.HTML_EXTENSION;
        }
        return redirectUrl;
    }

    private String getSuccessMessage(SlingHttpServletRequest request, ValueMap formStartValueMap) {

        String successMessage = null;
        final Object maisonSuccessMessage =
                request.getAttribute(ActionHandlerConstants.MAISON_AJAX_SUCCESS_MESSAGE_KEY);
        if(maisonSuccessMessage != null) {
            successMessage = (String) maisonSuccessMessage;
        }

        final String ajaxSuccessMessage = formStartValueMap
                .get(ActionHandlerConstants.AJAX_SUCCESS_MESSAGE_PROPERTY_NAME, String.class);
        if (StringUtils.isNotEmpty(ajaxSuccessMessage)) {
            successMessage = ajaxSuccessMessage;
        }
        return successMessage;
    }
    /**
     * Trick to prevent SlingPostServlet from capturing our forward request.
     * @author fransisco.chicharro
     *
     */
    private final class NonPostRequestWrapper extends SlingHttpServletRequestWrapper {

        public NonPostRequestWrapper(SlingHttpServletRequest wrappedRequest) {
            super(wrappedRequest);
        }
        /**
         * Validation includes always assume GET
         */
        @Override
        public String getMethod() {
            return HttpGet.METHOD_NAME;
        }
    }

    /**
     * Response wrapper to prevent redirecting.
     * @author tosheer.kalra
     */
    private final class NoRedirectSlingHttpServletResponseWrapper extends SlingHttpServletResponseWrapper {


        private static final String SEND_REDIRECT_UNSUPPORTED_ERROR_MESSAGE = "Response redirect is not supported.";

        /**
         * Constructor for initializing the Response wrapper.
         *
         * @param response response.
         */
        public NoRedirectSlingHttpServletResponseWrapper(final SlingHttpServletResponse response) {
            super(response);
        }

        @Override
        public void sendRedirect(String location) throws IOException {
            throw new UnsupportedOperationException(SEND_REDIRECT_UNSUPPORTED_ERROR_MESSAGE);
        }
    }
}

