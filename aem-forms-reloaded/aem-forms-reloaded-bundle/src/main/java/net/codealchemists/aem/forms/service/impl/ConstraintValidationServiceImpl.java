package net.codealchemists.aem.forms.service.impl;

import com.day.cq.wcm.foundation.forms.FieldDescription;
import net.codealchemists.aem.forms.service.ConstraintValidationService;
import net.codealchemists.aem.forms.validator.ConstraintValidator;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Implementation of {@link ConstraintValidationService} for service side validation of field data in request.
 * Created by tosheer.kalra on 19/07/2016.
 */
@Component
@Service
public class ConstraintValidationServiceImpl implements ConstraintValidationService {


    @Reference(referenceInterface = ConstraintValidator.class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC, bind = "bindConstraintValidators", unbind = "unbindConstraintValidators")
    private final Map<String, ConstraintValidator> validators = new ConcurrentHashMap<>();


    /**
     * Framework services bind method for {@link ConstraintValidator} service.
     * @param constraintValidator instance of {@link ConstraintValidator} service.
     */
    protected void bindConstraintValidators(final ConstraintValidator constraintValidator) {
        this.validators.put(constraintValidator.getName(), constraintValidator);

    }

    /**
     * Framework services unbind method for {@link ConstraintValidator} service.
     * @param constraintValidator instance of {@link ConstraintValidator} service.
     */
    protected void unbindConstraintValidators(final ConstraintValidator constraintValidator) {
        this.validators.remove(constraintValidator.getName());
    }


    @Override
    public void validate(final SlingHttpServletRequest slingHttpServletRequest, final Resource constraintResource,
                         final FieldDescription fieldDescription, String validationName) {
        final ConstraintValidator constraintValidator = this.validators.get(validationName);
        if (constraintValidator != null) {
            constraintValidator.validate(slingHttpServletRequest, constraintResource, fieldDescription);
        }
    }
}
