package net.codealchemists.aem.forms.validator.component;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.foundation.forms.FieldDescription;
import com.day.cq.wcm.foundation.forms.FieldHelper;
import com.day.cq.wcm.foundation.forms.FormsConstants;
import com.day.cq.wcm.foundation.forms.FormsHelper;
import net.codealchemists.aem.forms.constants.ActionHandlerConstants;
import net.codealchemists.aem.forms.constants.ConstraintConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

/**
 * Component for triggering the server side validation for all the constraint for the current field in process.
 * Created by tosheer.kalra on 04/08/2016.
 */
public class ServerSideValidatorUse extends WCMUsePojo {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSideValidatorUse.class);

    /**
     * Initialization of the component iterate over all the constraint for the current field in process and call
     * the server side validation script for all the constraints.
     */
    @Override
    public void activate() {
        FieldDescription[] fieldDescriptions = FieldHelper.getFieldDescriptions(getRequest(), getResource());
        for (final FieldDescription fieldDescription : fieldDescriptions) {
            final Resource constraintParentResource =
                    fieldDescription.getFieldResource().getChild(ActionHandlerConstants.CONSTRAINT_TYPECONFIG_NODE);
            if (constraintParentResource != null) {
                Iterator<Resource> iteratorResources =
                        constraintParentResource.listChildren();
                while (iteratorResources.hasNext()) {
                    Resource consRes = iteratorResources.next();
                    includeServerValidationForConstraint(fieldDescription, consRes);
                }
            }
        }
    }

    private void includeServerValidationForConstraint(
            final FieldDescription fieldDescription, final Resource consRes) {
        if (consRes != null) {
            ValueMap prop = consRes.getValueMap();
            String constraintType = prop.get(ActionHandlerConstants.CONSTRAINT_TYPE, StringUtils.EMPTY);
            if (StringUtils.isNotEmpty(constraintType)) try {
                final Resource includeResource = new CustomResourceWrapper(
                        fieldDescription.getFieldResource(),
                        constraintType,
                        FormsConstants.RST_FORM_CONSTRAINT);
                getRequest().setAttribute(
                        ConstraintConstants.CONSTRAINT_FIELD_RESOURCE_PATH_ATTRIBUTE, consRes.getPath());
                FormsHelper.includeResource(getRequest(), getResponse(),
                        includeResource,
                        FormsConstants.SCRIPT_SERVER_VALIDATION);
                getRequest().removeAttribute(ConstraintConstants.CONSTRAINT_FIELD_RESOURCE_PATH_ATTRIBUTE);
            } catch (IOException | ServletException exception) {
                LOGGER.error(
                        "An exception has occured while triggering server side validation for {}",
                        fieldDescription.getName(), exception);
            }
        }
    }
}
