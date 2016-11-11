package net.codealchemists.aem.forms.service.impl;

import net.codealchemists.aem.forms.loader.FormLoader;
import net.codealchemists.aem.forms.service.FormLoaderService;
import net.codealchemists.aem.forms.validator.ConstraintValidator;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of {@link FormLoaderService} service interface.
 * @author tosheer.kalra
 */
@Component
@Service
public class FormLoaderServiceImpl implements FormLoaderService {


    @Reference(referenceInterface = FormLoader.class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC, bind = "bindFormLoaders", unbind = "unbindFormLoaders")
    private final Map<String, FormLoader>  loaders = new ConcurrentHashMap<>();

    /**
     * Framework services bind method for {@link ConstraintValidator} service.
     * @param formLoader instance of {@link FormLoader} service.
     */
    protected void bindFormLoaders(final FormLoader formLoader) {
        this.loaders.put(formLoader.getName(), formLoader);

    }

    /**
     * Framework services unbind method for {@link ConstraintValidator} service.
     * @param formLoader instance of {@link FormLoader} service.
     */
    protected void unbindFormLoaders(final FormLoader formLoader) {
        this.loaders.remove(formLoader.getName());
    }

    @Override
    public void load(SlingHttpServletRequest request, String formLoaderName) {
        final FormLoader formLoader = loaders.get(formLoaderName);
        if (formLoader != null) {
            formLoader.load(request);
        }
    }
}
