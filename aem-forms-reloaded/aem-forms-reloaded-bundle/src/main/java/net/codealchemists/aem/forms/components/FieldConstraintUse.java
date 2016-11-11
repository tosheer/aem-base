package net.codealchemists.aem.forms.components;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.foundation.forms.FormsHelper;
import com.day.cq.wcm.foundation.forms.ValidationInfo;
import net.codealchemists.aem.core.utils.ResourceResolverHelper;
import net.codealchemists.aem.forms.constants.ActionHandlerConstants;
import net.codealchemists.aem.forms.constants.ConstraintConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Constrain data from form element dialog will be exposed using this USE class.
 * Created by tosheer.kalra on 17/08/2016.
 */
public class FieldConstraintUse extends WCMUsePojo {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldConstraintUse.class);

    /**
     * White space string constant for adding the space between values.
     */
    private static final String STRING_SPACE = " ";

    /**
     * Prefix for the constraint failure error message key.
     */
    private static final String DATA_ERROR_MSG_PREFIX = "data-msg-";

    /**
     * Prefix for the constraint parameter key.
     */
    private static final String DATA_PARAMETER_PREFIX = "data-param-";

    /**
     *
     */
    private static final String MIN_LENGTH_CONSTRAINT_PATH = "codealchemists/forms/components/constraints/minlength";

    /**
     *
     */
    private static final String REQUIRED_CONSTRAINT_PATH = "codealchemists/forms/components/constraints/required";

    /**
     *
     */
    private static final String MAX_LENGTH_CONSTRAINT_PATH = "codealchemists/forms/components/constraints/maxlength";
    /**
     *
     */
    private static final String REQUIRED_FIELD_LABEL_SUFFIX = "*";

    /**
     * List of constraint classes as a combined string.
     */
    private String constraintClasses = StringUtils.EMPTY;

    /**
     * Map which keep the constraint parameters like error message and parameter for constraint.
     */
    private Map<String, String> dataMap = new HashMap<>();


    private String requiredLabel = StringUtils.EMPTY;

    private List<String> errorMessages = new ArrayList<>();

    /**
     * List of constraint classes as a combined string.
     * @return List of constraint classes as a combined string.
     */
    public String getConstraintClasses() {
        return constraintClasses;
    }

    /**
     * Returns Map which keep the constraint parameters like error message and parameter for constraint.
     * @return Map which keep the constraint parameters like error message and parameter for constraint.
     */
    public Map getDataMap() {
        return dataMap;
    }

    /**
     * Class which needs to added if field is required.
     * @return Class which needs to added if field is required.
     */
    public String getRequiredLabel() {
        return requiredLabel;
    }

    /**
     * Class which needs to added if field is required.
     * @return Class which needs to added if field is required.
     */
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    @Override
    public void activate() throws Exception {
        populateValidationData();
        populateErrorMessages();
    }

    private void populateErrorMessages() {
        final ValidationInfo validationInfo = ValidationInfo.getValidationInfo(getRequest());
        final String parameterName = FormsHelper.getParameterName(getResource());
        if (validationInfo != null && !StringUtils.isEmpty(parameterName)
                && validationInfo.getErrorMessages(parameterName) != null) {
            this.errorMessages = Arrays.asList(validationInfo.getErrorMessages(parameterName));
        }
    }

    private void populateValidationData() {
        try {
            StringBuilder constraintClassesBuilder = new StringBuilder(30);
            if (getResource()
                    .getChild(ActionHandlerConstants.CONSTRAINT_TYPECONFIG_NODE) != null) {
                Iterator<Resource> iteratorResources = getResource().getChild(
                        ActionHandlerConstants.CONSTRAINT_TYPECONFIG_NODE)
                        .listChildren();
                while (iteratorResources.hasNext()) {
                    Resource constraintResource = iteratorResources.next();
                    if (constraintResource != null) {
                        ValueMap prop = constraintResource.getValueMap();
                        final String constraintType = prop.get(ActionHandlerConstants.CONSTRAINT_TYPE, String.class);
                        String constraintName = getConstrainName(constraintType);
                        String constraintMessage =
                                prop.get(ConstraintConstants.CONSTRAINT_MESSAGE_KEY, String.class);
                        String constraintParameter =
                                prop.get(ConstraintConstants.CONSTRAINT_PARAMETER_KEY, String.class);
                        String parameterKey = DATA_PARAMETER_PREFIX + constraintName;

                        if (StringUtils.equalsIgnoreCase(
                                constraintType, REQUIRED_CONSTRAINT_PATH)) {
                            requiredLabel = REQUIRED_FIELD_LABEL_SUFFIX;
                        }
                        if (FormsHelper.doClientValidation(getRequest())) {
                            if (StringUtils.equalsIgnoreCase(
                                    constraintType, MAX_LENGTH_CONSTRAINT_PATH)) {
                                parameterKey = ConstraintConstants.MAX_LENGTH_PARAMETER_NAME;
                            } else if (StringUtils.equalsIgnoreCase(
                                    constraintType, MIN_LENGTH_CONSTRAINT_PATH)) {
                                parameterKey = ConstraintConstants.MIN_LENGTH_PARAMETER_NAME;
                            }
                            constraintClassesBuilder.append(STRING_SPACE).append(constraintName);
                            final String localizedConstraintMessage =
                                    getRequest().getResourceBundle(getRequest().getLocale()).getString(constraintMessage);
                            if (StringUtils.isNotEmpty(localizedConstraintMessage)) {
                                dataMap.put(DATA_ERROR_MSG_PREFIX + constraintName, localizedConstraintMessage);
                            }
                            if (StringUtils.isNotEmpty(constraintParameter)) {
                                dataMap.put(parameterKey, constraintParameter);
                            }
                        }
                    }
                }
            }
            this.constraintClasses = constraintClassesBuilder.toString();
        } catch (Exception exception) {
            LOGGER.error("An exception has occured while populating constraint data for field.", exception);
        }
    }

    private String getConstrainName(final String constrainType) {
        final ResourceResolverFactory resourceResolverFactory =
                getSlingScriptHelper().getService(ResourceResolverFactory.class);
        ResourceResolver resolverOnAppsData = null;
        try {
            resolverOnAppsData = ResourceResolverHelper.getReadResourceResolverOnAppsData(
                    resourceResolverFactory);
            final Resource constrainDataResource =
                    resolverOnAppsData.resolve(ConstraintConstants.APPS_JCR_PATH + constrainType);
            if (constrainDataResource != null) {
                return constrainDataResource.getValueMap().get(
                        ConstraintConstants.CONSTRAINT_NAME_PROPERTY_NAME, String.class);
            }
        } catch (LoginException e) {
            LOGGER.info("An error has occured while getting the the apps data read resolver", e);
        } finally {
            if (resolverOnAppsData != null && resolverOnAppsData.isLive()) {
                resolverOnAppsData.close();
            }
        }
        return null;
    }
}
