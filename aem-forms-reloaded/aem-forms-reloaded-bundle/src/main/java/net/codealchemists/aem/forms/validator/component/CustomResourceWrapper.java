package net.codealchemists.aem.forms.validator.component;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceWrapper;

/**
 * ResourceWrapper for overriding resource type and resource super type of a resource.
 */
public class CustomResourceWrapper extends ResourceWrapper {
    private final String resourceType;
    private final String resourceSuperType;

    /**
     * Constructor for resource wrapper.
     * @param resource resource.
     * @param resourceType resource type.
     * @param resourceSuperType super resource type.
     */
    public CustomResourceWrapper(final Resource resource, final String resourceType,
                                 final String resourceSuperType) {
        super(resource);
        this.resourceType = resourceType;
        this.resourceSuperType = resourceSuperType;
    }

    /**
     * Returns wrapped resource supper type.
     * @return resource supper type.
     */
    public String getResourceSuperType() {
        return this.resourceSuperType;
    }

    /**
     * Returns wrapped resource type.
     * @return resource type.
     */
    public String getResourceType() {
        return this.resourceType;
    }
}
