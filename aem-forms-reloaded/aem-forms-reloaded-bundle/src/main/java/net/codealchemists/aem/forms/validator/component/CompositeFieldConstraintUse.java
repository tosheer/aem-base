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
 * Component for triggering the server side validation for all the fields for the current composite field in process.
 * Created by tosheer.kalra on 31/08/2016.
 */
public class CompositeFieldConstraintUse extends WCMUsePojo {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeFieldConstraintUse.class);

    private static final String COMPOSITE_PATH = "compositePath";

    @Override
    public void activate() throws Exception {

        Resource includedResource =
                getResourceResolver().resolve(getProperties().get(COMPOSITE_PATH, StringUtils.EMPTY));
        Iterator<Resource> formElements = includedResource.listChildren();
        while(formElements.hasNext()){
            FieldDescription[] formElementDescription =
                    FieldHelper.getFieldDescriptions(getRequest(), formElements.next());
            for(final FieldDescription fieldDescription : formElementDescription) {
                final Resource constraintParentResource =
                        fieldDescription.getFieldResource().getChild(ActionHandlerConstants.CONSTRAINT_TYPECONFIG_NODE);
                if (constraintParentResource != null) {
                    Iterator<Resource> iteratorResources =
                            constraintParentResource.listChildren();
                    while (iteratorResources.hasNext()) {
                        Resource constraintFieldResource = iteratorResources.next();
                        includeServerValidationForConstraint(fieldDescription, constraintFieldResource);
                    }
                }
            }
        }

    }

    /**
     * Method is called for every simple field within composite field and trigger the server side validation script
     * for each constraint added on the constraint field.
     * @param fieldDescription simple field within composite field which is under process.
     * @param constraintFieldResource constraint resource applied on the simple field.
     */
    private void includeServerValidationForConstraint(
            final FieldDescription fieldDescription, final Resource constraintFieldResource) {
        if (constraintFieldResource != null) {
            ValueMap prop = constraintFieldResource.getValueMap();
            String constraintType = prop.get(ActionHandlerConstants.CONSTRAINT_TYPE, StringUtils.EMPTY);
            if (StringUtils.isNotEmpty(constraintType)) try {
                final Resource includeResource = new CustomResourceWrapper(
                        fieldDescription.getFieldResource(),
                        constraintType,
                        FormsConstants.RST_FORM_CONSTRAINT);
                getRequest().setAttribute(
                        ConstraintConstants.CONSTRAINT_FIELD_RESOURCE_PATH_ATTRIBUTE, constraintFieldResource.getPath());
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
