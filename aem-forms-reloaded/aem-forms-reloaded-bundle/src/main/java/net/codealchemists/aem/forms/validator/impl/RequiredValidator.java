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
 * Validator for required constraint validation.
 * Checks if any data is available in form submit request for the field. If no data is available
 * constraint validation will fail.
 * Created by tosheer.kalra on 01/08/2016.
 */
@Component
@Service
public class RequiredValidator implements ConstraintValidator {

    @Override
    public void validate(final SlingHttpServletRequest slingHttpServletRequest, final Resource constraintResource,
                         final FieldDescription fieldDescription) {
        final ValueMap constraintResourceValueMap = constraintResource.getValueMap();
        if (StringUtils.isEmpty(slingHttpServletRequest.getParameter(fieldDescription.getName()))) {
            fieldDescription.setConstraintMessage(constraintResourceValueMap.get(
                    ConstraintConstants.CONSTRAINT_MESSAGE_KEY, String.class));
            ValidationInfo.addConstraintError(slingHttpServletRequest, fieldDescription);
        }
    }

    @Override
    public String getName() {
        return ConstraintConstants.REQUIRED_CONSTRAINT_NAME;
    }
}
