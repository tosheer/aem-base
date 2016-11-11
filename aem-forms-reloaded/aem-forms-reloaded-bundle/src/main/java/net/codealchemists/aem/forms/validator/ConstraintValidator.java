package net.codealchemists.aem.forms.validator;

import com.day.cq.wcm.foundation.forms.FieldDescription;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

/**
 * Interface which every custom form data validator will implement.
 * Created by tosheer.kalra on 19/07/2016.
 */
public interface ConstraintValidator {


    /**
     * Validate form data  for the constraint.
     * @param slingHttpServletRequest form request.
     * @param constraintResource constraint resource.
     * @param fieldDescription field description,
     */
    void validate(final SlingHttpServletRequest slingHttpServletRequest, final Resource constraintResource,
                  final FieldDescription fieldDescription);

    /**
     * Returns name with which form constrain validator get registered with form constrain service.
     * @return Name with which form constrain validator get registered with form constrain service.
     */
    String getName();

}
