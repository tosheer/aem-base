package net.codealchemists.aem.forms.validator.impl;

import com.day.cq.wcm.foundation.forms.FieldDescription;
import com.day.cq.wcm.foundation.forms.ValidationInfo;
import net.codealchemists.aem.forms.constants.ConstraintConstants;
import net.codealchemists.aem.forms.validator.ConstraintValidator;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator for checking the field data in the form submit request against pattern added as a parameter for constraint.
 * Created by tosheer.kalra on 01/08/2016.
 */
@Component
@Service
public class PatternValidator implements ConstraintValidator {

    @Override
    public void validate(final SlingHttpServletRequest slingHttpServletRequest, final Resource constraintResource,
                         final FieldDescription fieldDescription) {
        final ValueMap constraintResourceValueMap = constraintResource.getValueMap();
        final String patternParameter = constraintResourceValueMap.get(
                ConstraintConstants.CONSTRAINT_PARAMETER_KEY, String.class);
        final Pattern pattern = Pattern.compile(patternParameter);
        final String value = slingHttpServletRequest.getParameter(fieldDescription.getName());
        if (value != null) {
            final Matcher m = pattern.matcher(value);
            if (!m.matches()) {
                fieldDescription.setConstraintMessage(
                        constraintResourceValueMap.get(ConstraintConstants.CONSTRAINT_MESSAGE_KEY, String.class));
                ValidationInfo.addConstraintError(slingHttpServletRequest, fieldDescription);
            }
        }
    }

    @Override
    public String getName() {
        return ConstraintConstants.PATTERN_CONSTRAINT_NAME;
    }
}
