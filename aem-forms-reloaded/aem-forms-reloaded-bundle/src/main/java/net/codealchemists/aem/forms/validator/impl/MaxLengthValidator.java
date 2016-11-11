package net.codealchemists.aem.forms.validator.impl;

import com.day.cq.wcm.foundation.forms.FieldDescription;
import com.day.cq.wcm.foundation.forms.ValidationInfo;
import net.codealchemists.aem.forms.constants.ConstraintConstants;
import net.codealchemists.aem.forms.validator.ConstraintValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

/**
 * Validator for checking the field value length of the field data in the form submit request
 * against the field length passed as constraint parameter.
 * Created by tosheer.kalra on 01/08/2016.
 */
@Component
@Service
public class MaxLengthValidator implements ConstraintValidator {

    @Override
    public void validate(final SlingHttpServletRequest slingHttpServletRequest, final Resource constraintResource,
                         final FieldDescription fieldDescription) {
        int length = slingHttpServletRequest.getParameter(fieldDescription.getName()).length();
        final ValueMap constraintResourceValueMap = constraintResource.getValueMap();
        final String parameter = constraintResourceValueMap.get(
                ConstraintConstants.CONSTRAINT_PARAMETER_KEY, String.class);
        if (StringUtils.isNotEmpty(parameter)) {
            if (length > Integer.parseInt(parameter)) {
                fieldDescription.setConstraintMessage(constraintResourceValueMap.get(
                        ConstraintConstants.CONSTRAINT_MESSAGE_KEY, String.class));
                ValidationInfo.addConstraintError(slingHttpServletRequest, fieldDescription);
            }
        }
    }

    @Override
    public String getName() {
        return ConstraintConstants.MAX_LENGTH_CONSTRAINT_NAME;
    }
}
