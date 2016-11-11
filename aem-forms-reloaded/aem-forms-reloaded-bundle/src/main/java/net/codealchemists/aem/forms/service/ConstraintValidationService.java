package net.codealchemists.aem.forms.service;

import com.day.cq.wcm.foundation.forms.FieldDescription;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

/**
 * Service interface for the service which registers various field data validators and validate the form field
 * submit request data against registered constraint validators.
 * Created by tosheer.kalra on 19/07/2016.
 */
public interface ConstraintValidationService {

    /**
     * Validates the constraint against the form field data in request.
     * @param slingHttpServletRequest form submit request.
     * @param constraintResource resource which contained various constraint parameter authored in the field.
     * @param fieldDescription field description of the form field which is getting validated.
     * @param validationName validator name with which it is registered with ConstraintValidationService implementation.
     */
    void validate(final SlingHttpServletRequest slingHttpServletRequest, final Resource constraintResource,
                  final FieldDescription fieldDescription, final String validationName);

}
