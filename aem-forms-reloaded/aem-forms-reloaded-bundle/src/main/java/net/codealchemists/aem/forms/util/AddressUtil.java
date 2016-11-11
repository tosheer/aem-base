package net.codealchemists.aem.forms.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.xss.XSSFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Util class for creating the address from the form request submitted.
 * Created by tosheer.kalra on 15/08/2016.
 */
public final class AddressUtil {

    /**
     * Private constructor to deny direct instantiation.
     */
    private AddressUtil() { }

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressUtil.class);

    /**
     * Helper method that uses reflection to build different address DTO from
     * the request params.
     *
     * @param addressClass Type of the address DTO which needs to be populated.
     * @param request request containg the data.
     * @param xssFilter cross site scripting data filter.
     * @param beanRequestFieldMapping mapping which defines which request paramter is copied to which field of address.
     * @return populated address dto.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> T buildAddressFields(Class<T> addressClass, SlingHttpServletRequest request,
                                     XSSFilter xssFilter, Map<String, String> beanRequestFieldMapping) {
        try {
            T address = addressClass.newInstance();
            for (Map.Entry<String, String> entry : beanRequestFieldMapping.entrySet()) {
                setField(address, entry.getKey(), getValueFromRequest(request, entry.getValue(), xssFilter));
            }
            return address;
        } catch (IllegalAccessException | InstantiationException e) {
            LOGGER.error("An exception happened while creating address from request parameter.", e);
        }
        return null;
    }


    /**
     * Looks for a key in request parameters and/or attributes and returns the
     * value if found.
     *
     * @param slingRequest
     * @param name
     * @return
     */
    private static String getValueFromRequest(
            SlingHttpServletRequest slingRequest, String name, XSSFilter xssFilter) {
        // 1. get the parameter from request parameters.
        String paramValue = null;
        if (xssFilter != null) {
            paramValue = xssFilter.filter(slingRequest.getParameter(name));
        } else {
            paramValue = slingRequest.getParameter(name);
        }
        if (StringUtils.isEmpty(paramValue)) {
            // 2. get the parameter from request attributes.
            paramValue = (String) slingRequest.getAttribute(name);
        }
        return paramValue;
    }

    /**
     * This method sets the field value of the given object.
     *
     * @param objectContainingField
     * @param fieldName
     * @param fieldValue
     */
    public static <F, V> void setField(F objectContainingField,
                                       String fieldName, V fieldValue) {
        // No point in going reflective way if the value to be set is null.
        if (fieldValue != null) {
            Class<?> fieldClass = objectContainingField.getClass();
            try {
                Field field = fieldClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (fieldType == fieldValue.getClass()) {
                    field.set(objectContainingField, fieldValue);
                } else if (fieldType == long.class) {
                    field.set(objectContainingField,
                            Long.parseLong((String) fieldValue));
                } else if (fieldType == boolean.class) {
                    field.set(objectContainingField,
                            Boolean.parseBoolean((String) fieldValue));
                }
            } catch (NoSuchFieldException ex) {
                LOGGER.error(
                        "NoSuchFieldException: field '{}' not found in {} : {}",
                        fieldName, fieldClass);
            } catch (IllegalAccessException ex) {
                LOGGER.error(
                        "IllegalAccessException: field '{}' is not accessible in {} : {}",
                        fieldName, fieldClass);
            }
        }
    }

    /**
     * This method gets the field value from the object passed.
     *
     * @param object object passed.
     * @param fieldName field name for which we need to get the value.
     *
     * @return field Object corresponding to the passed field name.
     */
    public static <T> Object getFieldValue(T object, String fieldName) {
        Field field;
        try {
            field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception ex) {
            LOGGER.error("field '{}' not found in {} : {}", fieldName,
                    object.getClass());
        }
        return null;
    }

}
