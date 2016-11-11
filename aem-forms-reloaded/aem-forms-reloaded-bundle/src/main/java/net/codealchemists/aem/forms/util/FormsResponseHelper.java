package net.codealchemists.aem.forms.util;

import com.day.cq.wcm.foundation.forms.ValidationInfo;
import net.codealchemists.aem.forms.constants.ActionHandlerConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.io.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

/**
 * Util class for doing additional stuff in forms framework.
 * Created by tosheer.kalra on 15/08/2016.
 */
public final class FormsResponseHelper {

    private static final String RESULT = "result_";
    private static final String VALIDATION_ERRORS_KEY = "validationErrors";
    private static final String FIELD_KEY = "field";
    private static final String FIELD_ERROR_MSG_KEY = "fieldErrorMsg";
    private static final String ERROR_MSG_KEY = "errorMsg";
    private static final String SUCCESS_FLAG_JSON_KEY = "success";
    /**
     * Private constructor to deny direct instantiation.
     */
    private FormsResponseHelper() { }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormsResponseHelper.class);


    /**
     *
     * Method create the ajax form submission json response which is to added as part of form start component.
     * result_<formid> = {
            "success":"true|false",
            "successMessage":"<nice thank you message>"
            "redirectPath": "<Page to where request should be redirected>"
            "validationErrors": [
            {
                "field": "<field_id_1>",
                "errorMsg" : ["<validation error 1_1>", "<validation error 1_2>", ..."<validation error 1_X>"]
            },
            {
                "field": "<field_id_2>",
                "errorMsg" : ["<validation error 2_1>", "<validation error 2_2>", ..."<validation error 2_X>"]
            }],
     };
     *
     * Putting a check on validation object is initialized doesn't says that it has error,
     * but we have already looked deep into it and this the way Adobe is checking if form has an error or not.
     * So we have kept it like this.
     *
     * @param request Form rendering request.
     * @param response Form rendering response.
     */
    public static void buildFormSubmitJsonResponse(final SlingHttpServletRequest request,
                                                   final SlingHttpServletResponse response) {
            try {
                JSONWriter jsonWriter = new JSONWriter(response.getWriter());
                jsonWriter.setTidy(true);
                jsonWriter.object().key(RESULT + com.day.cq.wcm.foundation.forms.FormsHelper.getFormId(request)).object();

                final ValidationInfo validationInfo = ValidationInfo.getValidationInfo(request);
                final boolean noErrorInRequest = validationInfo == null;
                jsonWriter.key(SUCCESS_FLAG_JSON_KEY).value(noErrorInRequest);

                if (noErrorInRequest) {
                    buildSuccessPartJson(request, jsonWriter);

                } else {
                    buildErrorPartJson(request, jsonWriter, validationInfo);
                }

                jsonWriter.endObject().endObject();
            } catch (IOException | JSONException e) {
                LOGGER.error("An error has occured while processing the form submission response.", e);
            }

    }

    private static void buildSuccessPartJson(final SlingHttpServletRequest request,
                                             final JSONWriter jsonWriter) throws JSONException {
        final Object successMessage = request.getAttribute(ActionHandlerConstants.SUCCESS_MESSAGE_KEY);
        if (successMessage != null)
            jsonWriter.key(ActionHandlerConstants.SUCCESS_MESSAGE_KEY).value(successMessage);

        final Object redirectPath = request.getAttribute(ActionHandlerConstants.REDIRECT_PATH_KEY);
        if (redirectPath != null)
            jsonWriter.key(ActionHandlerConstants.REDIRECT_PATH_KEY).value(redirectPath);

    }

    private static void buildErrorPartJson(final SlingHttpServletRequest request,
                                           final JSONWriter jsonWriter,
                                           final ValidationInfo validationInfo) throws JSONException {
        jsonWriter.key(VALIDATION_ERRORS_KEY).array();
        final Iterator<Resource> formElements = com.day.cq.wcm.foundation.forms.FormsHelper.getFormElements(request.getResource());

        while (formElements.hasNext()) {
            final Resource resource = formElements.next();
            final String parameterName = com.day.cq.wcm.foundation.forms.FormsHelper.getParameterName(resource);
            final String[] errorMessages = validationInfo.getErrorMessages(parameterName);

            if (!StringUtils.isEmpty(parameterName)
                    && errorMessages != null) {
                jsonWriter.object().key(FIELD_KEY).value(com.day.cq.wcm.foundation.forms.FormsHelper.getFieldId(request, resource))
                        .key(FIELD_ERROR_MSG_KEY).array();
                for (String errorMessage : errorMessages) {
                    jsonWriter.value(errorMessage);
                }
                jsonWriter.endArray().endObject();
            }
        }
        jsonWriter.endArray();

        final String[] errorMessages = validationInfo.getErrorMessages(null);
        if (errorMessages != null) {
            for (String errorMessage : errorMessages) {
                jsonWriter.key(ERROR_MSG_KEY).value(errorMessage);
            }
        }
    }
}
