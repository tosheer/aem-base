package net.codealchemists.aem.forms.constants;

/**
 * Class keeping the various constants used in form constraints.
 * Created by tosheer.kalra on 28/07/2016.
 */
public final class ConstraintConstants {

    /**
     * CQ field constraint error message key.
     */
    public static final String CONSTRAINT_MESSAGE_KEY = "constraintMessage";

    /**
     * CQ field constraint parameter key.
     */
    public static final String CONSTRAINT_PARAMETER_KEY = "parameter";

    /**
     * Required constraint Name.
     */
    public static final String REQUIRED_CONSTRAINT_NAME = "cst-required";

    /**
     * Required constraint Name.
     */
    public static final String NUMBER_CONSTRAINT_NAME = "cst-number";

    /**
     * Pattern constraint name.
     */
    public static final String PATTERN_CONSTRAINT_NAME = "cst-pattern";

    /**
     * JCR property name for defined constraint name.
     */
    public static final String CONSTRAINT_NAME_PROPERTY_NAME = "constraintName";

    /**
     * path where defined constraint needs to be serached.
     */
    public static final String APPS_JCR_PATH = "/apps/";

    /**
     * Request attribute name for constraintDataPath.
     */
    public static final String CONSTRAINT_FIELD_RESOURCE_PATH_ATTRIBUTE = "constraintFieldResourcePath";

    /**
     * US Shipping address composite field constraint name.
     */
    public static final String US_SHIPPING_ADDRESS_CONSTRAINT_NAME = "cst-us-shipping-address";

    /**
     * US Billing address composite field constraint name.
     */
    public static final String US_BILLING_ADDRESS_CONSTRAINT_NAME = "cst-us-billing-address";

    /**
     * Request attribute for getting the validated shipping address
     */
    public static final String VALIDATED_SHIPPING_ADDRESS = "validatedShippingAddress";

    /**
     * Request attribute for getting the validated billing address
     */
    public static final String VALIDATED_BILLING_ADDRESS = "validatedBillingAddress";

    /**
     * Pattern constraint name.
     */
    public static final String MIN_LENGTH_CONSTRAINT_NAME = "cst-minlength";

    /**
     * Pattern constraint name.
     */
    public static final String MAX_LENGTH_CONSTRAINT_NAME = "cst-maxlength";

    /**
     * Max length HTML parameter name.
     */
    public static final String MAX_LENGTH_PARAMETER_NAME = "maxlength";

    /**
     * Min length HTML parameter name.
     */
    public static final String MIN_LENGTH_PARAMETER_NAME = "minlength";
}
