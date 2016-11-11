package net.codealchemists.aem.forms.actions.handler;

import net.codealchemists.aem.forms.constants.ActionHandlerConstants;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample Action Handler for form submit.
 * Created by tosheer.kalra on 26/07/2016.
 */
@Service
@Component
public class SampleFormActionHandler extends AbstractFormActionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleFormActionHandler.class);

    @Override
    public void doProcess(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        LOGGER.info("Form submit request received.");

    }

    @Override
    public String getName() {
        return ActionHandlerConstants.SAMPLE_FORM_SUBMIT_ACTION;
    }
}
