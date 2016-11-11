package net.codealchemists.aem.forms.loader.component;

import com.adobe.cq.sightly.WCMUsePojo;
import net.codealchemists.aem.forms.constants.FormLoaderConstants;
import net.codealchemists.aem.forms.service.FormLoaderService;

/**
 * Entry point for form loader. It gets loader name from request attribute and trigger pre-population of form data.
 * Created by tosheer.kalra on 28/07/2016.
 */
public class FormLoaderUse extends WCMUsePojo {
    
    @Override
    public void activate() {
        FormLoaderService formLoaderService = getRequest().adaptTo(FormLoaderService.class);
        final String loadActionName = (String) getRequest().getAttribute(FormLoaderConstants.LOAD_ACTION_NAME);
        formLoaderService.load(getRequest(), loadActionName);
    }

}
