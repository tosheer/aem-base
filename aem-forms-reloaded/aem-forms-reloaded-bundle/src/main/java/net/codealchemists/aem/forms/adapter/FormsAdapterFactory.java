package net.codealchemists.aem.forms.adapter;

/**
 * Created by tosheer.kalra on 19/07/2016.
 */

import net.codealchemists.aem.forms.service.ConstraintValidationService;
import net.codealchemists.aem.forms.service.FormLoaderService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.adapter.AdapterFactory;


/**
 * Adapter factory to return {@link ConstraintValidationService} and {@link FormLoaderService} needed by forms
 * framework for pre loading and validation of the form data.
 * @author tosheer.kalra
 */
@Component
@Service
@Properties({
        @Property(name = "service.description", value = "Form services adapter factory"),
        @Property(name = "adaptables", value = "org.apache.sling.api.SlingHttpServletRequest"),
        @Property(name = "adapters", value = {"net.codealchemists.aem.forms.service.ConstraintValidationService",
                "net.codealchemists.aem.forms.service.FormLoaderService"})
})
public class FormsAdapterFactory implements AdapterFactory {

    @Reference
    private ConstraintValidationService constraintValidationService;

    @Reference
    private FormLoaderService formLoaderService;

    @Override
    public <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> adapterTypeClass) {
        if (adapterTypeClass == FormLoaderService.class) {
            return (AdapterType) formLoaderService;
        } else if (adapterTypeClass == ConstraintValidationService.class) {
            return (AdapterType) constraintValidationService;
        }
        return null;
    }
}
