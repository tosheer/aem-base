package net.codealchemists.aem.forms.components;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.foundation.forms.FormsHelper;
import net.codealchemists.aem.forms.constants.ActionHandlerConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * FormFieldComponent would be used in all components to read values from Dialog.
 * @author tosheer.kalra
 */
public class FormFieldUse extends WCMUsePojo {

	/**
     * Constant for resourcePath property.
     */
    protected static final String RESOURCE_PATH_PROPERTY = "resourcePath";

	private String name;
	private String id;
	private String label;
	private String instructionText;
	private String fieldClass;
	private String formId;
	private String parameterValue;
    private boolean trackable;
    private String trackingLabel;
    private String placeholder;
	private boolean disabledMode;

	@Override
	public void activate() {

		name = FormsHelper.getParameterName(getResource());
		id = FormsHelper.getFieldId(getRequest(), getResource());
		label = getProperties().get(ActionHandlerConstants.LABEL_LABEL, StringUtils.EMPTY);
		instructionText = getProperties().get(ActionHandlerConstants.LABEL_INSTRUCTIONTEXT,
				StringUtils.EMPTY);
		fieldClass = getProperties().get(ActionHandlerConstants.LABEL_CLASS, StringUtils.EMPTY);

		formId = FormsHelper.getFormId(getRequest());
		parameterValue = (String) getRequest().getAttribute(name);
		if (StringUtils.isEmpty(parameterValue)) {
			parameterValue = getProperties().get(ActionHandlerConstants.LABEL_DEFAULT_VALUE, StringUtils.EMPTY);
		}
        trackable = getProperties().get(ActionHandlerConstants.LABEL_TRACKABLE_FLAG,
                false);
        trackingLabel = getProperties().get(ActionHandlerConstants.LABEL_TRACKING_LABEL,
                StringUtils.EMPTY);
        placeholder = getProperties().get(ActionHandlerConstants.LABEL_PLACEHOLDER,
                StringUtils.EMPTY);
		disabledMode = getWcmMode().isDisabled();
	}

    /**
     * This method fetches the ValueMap of all the child resources resolved by
     * the given path.
     * @param relativePath relative path of the child respect to parent resource.
     * @return List<ValueMap> List of value map of all children.
     */
    protected List<ValueMap> getChildProperties(final String relativePath) {
        return this.getChildProperties(getResource().getChild(relativePath));
    }

    /**
     * This method fetches the ValueMap of all the child resources of the given
     * resource.
     * @param resource parent resource.
     * @return List<ValueMap> List of value map of all children.
     */
    protected List<ValueMap> getChildProperties(final Resource resource) {
        final List<ValueMap> result;
        if (resource == null) {
            result = Collections.emptyList();
        } else {
            result = new ArrayList<>();
            final Iterator<Resource> children = resource.listChildren();
            Resource childResource;
            while (children.hasNext()) {
                childResource = children.next();
                Map<String, Object> map = new HashMap<>(childResource
                        .adaptTo(ValueMap.class));
                map.put(RESOURCE_PATH_PROPERTY, childResource.getPath());
                result.add(new ValueMapDecorator(map));
            }
        }
        return result;
    }

	/**
	 * @return the name of form element.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the id of form element.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the label of form element.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the instructionText of form element.
	 */
	public String getInstructionText() {
		return instructionText;
	}

	/**
	 * @return the fieldClass of form element.
	 */
	public String getFieldClass() {
		return fieldClass;
	}

	/**
	 * @return the formId of form.
	 */
	public String getFormId() {
		return formId;
	}

	/**
	 * @return parameterValue from request.
	 */
	public String getParameterValue() {
		return this.parameterValue;
	}

	/**
	 * @return true if WCM mode is disabled.
	 */
	public boolean isDisabledMode() {
		return disabledMode;
	}

	/**
	 *
	 * @return field palace holder.
	 */
	public String getPlaceholder() {
		return placeholder;
	}

	/**
	 *
	 * @return if field is tracked.
	 */
	public boolean isTrackable() {
		return trackable;
	}

	/**
	 *
	 * @return Tracking label for tracking.
	 */
	public String getTrackingLabel() {
		return trackingLabel;
	}
}
