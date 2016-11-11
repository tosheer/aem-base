package net.codealchemists.aem.forms.loader.impl;


import net.codealchemists.aem.forms.constants.FormLoaderConstants;
import net.codealchemists.aem.forms.loader.FormLoader;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Sample form add
 * Created by tosheer.kalra on 27/07/2016.
 */
@Component
@Service
public class SampleFormLoader implements FormLoader {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleFormLoader.class);

    /**
     * Do various processes needed for form data and add request parameters in the request passed.
     *
     * @param request form request.
     */
    @Override
    public void load(SlingHttpServletRequest request) {
        LOGGER.info("Form loading Request received");
        request.setAttribute("text", "Loader value");
    }

    @Override
    public String getName() {
        return FormLoaderConstants.SAMPLE_FORM_LOADER;
    }
}
