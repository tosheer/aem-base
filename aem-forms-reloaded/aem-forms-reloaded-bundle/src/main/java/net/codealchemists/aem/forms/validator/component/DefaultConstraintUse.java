package net.codealchemists.aem.forms.validator.component;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.foundation.forms.FieldDescription;
import com.day.cq.wcm.foundation.forms.FieldHelper;
import net.codealchemists.aem.core.utils.ResourceResolverHelper;
import net.codealchemists.aem.forms.constants.ConstraintConstants;
import net.codealchemists.aem.forms.service.ConstraintValidationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For every constraint which is included by ServerSideValidationUse default resource at
 * /apps/foundation/components/form/defaults/constraint/servervalidation.jsp will be executed. This script use this Use
 * class for triggering the specific validator.
 * Created by tosheer.kalra on 27/07/2016.
 */
public class DefaultConstraintUse extends WCMUsePojo {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConstraintUse.class);


    @Override
    public void activate() throws Exception {

        final FieldDescription fieldDescription = FieldHelper.getConstraintFieldDescription(getRequest());

        final Resource constraintFieldResource =
                getResourceResolver().resolve((String)getRequest().getAttribute(
                        ConstraintConstants.CONSTRAINT_FIELD_RESOURCE_PATH_ATTRIBUTE));
        final String constraintName = getConstraintName();
        if (StringUtils.isNotEmpty(constraintName)) {
            ConstraintValidationService constraintValidationService =
                    getRequest().adaptTo(ConstraintValidationService.class);
            constraintValidationService.validate(
                    getRequest(), constraintFieldResource, fieldDescription,
                    constraintName);
        }
    }

    /**
     * Return constraint name of the constraint with which it is registered with the constrain validation service.
     * @return constraint name of the constraint with which it is registered with the constrain validation service.
     */
    private String getConstraintName() {
        ResourceResolverFactory resourceResolverFactory
                = getSlingScriptHelper().getService(ResourceResolverFactory.class);
        ResourceResolver resolverOnAppsData = null;
        try {
            resolverOnAppsData = ResourceResolverHelper.getReadResourceResolverOnAppsData(
                    resourceResolverFactory);
            final Resource constrainDataResource =
                    resolverOnAppsData.resolve(ConstraintConstants.APPS_JCR_PATH + getResource().getResourceType());
            if (constrainDataResource != null) {
                return constrainDataResource.getValueMap().get(ConstraintConstants.CONSTRAINT_NAME_PROPERTY_NAME, String.class);
            }
        } catch (LoginException e) {
            LOGGER.error("An error has occured while getting the the apps data read resolver", e);
        } finally {
            if (resolverOnAppsData != null && resolverOnAppsData.isLive()) {
                resolverOnAppsData.close();
            }
        }
        return StringUtils.EMPTY;
    }
}
