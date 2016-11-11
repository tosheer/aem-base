package net.codealchemists.aem.forms.components;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.cq.sightly.WCMUsePojo;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.foundation.forms.FormsConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.query.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Data source for populating the dynamic population of constrain dropdown in abstract component dropdown.
 * Created by tosheer.kalra on 12/08/2016.
 */
public class ConstrainDataSourceUse extends WCMUsePojo {

    /**
     * Path where defined constrained type needs to be searched.
     */
    private static final String APPS_SEARCH_PATH = "/apps/";

    /**
     * Query for finding the finding the constraint defined in apps.
     */
    private static final String QUERY = "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE(s,'"
            + APPS_SEARCH_PATH + "') AND s.[" + FormsConstants.PROPERTY_RT + "]='"
            + FormsConstants.RT_FORM_CONSTRAINT + "'";

    /**
     * Key for populating the datasource value map.
     */
    private static final String DATASOURCE_VALUE = "value";

    /**
     * Value for populating the datasource value map.
     */
    private static final String DATASOURCE_KEY = "text";
    /**
     * default logger.
     */
    private final Logger logger = LoggerFactory.getLogger(ConstrainDataSourceUse.class);

    @Override
    public void activate() throws Exception {
        getRequest().setAttribute(DataSource.class.getName(), EmptyDataSource.instance());
        List<Resource> fakeResourceList = new ArrayList<>();
        logger.debug("Query for getting all forms constrains is: {}", QUERY);
        final Iterator<Resource> result = getResourceResolver().findResources(QUERY, Query.JCR_SQL2);
        while (result.hasNext()) {
            final Resource constraintResource = result.next();
            final ValueMap properties = constraintResource.getValueMap();
            final String constrainPath = constraintResource.getPath().substring(APPS_SEARCH_PATH.length());
            final ValueMap vm = new ValueMapDecorator(new HashMap<>());
            vm.put(DATASOURCE_VALUE, constrainPath);
            vm.put(DATASOURCE_KEY, properties.get(JcrConstants.JCR_TITLE, String.class));
            fakeResourceList.add(
                    new ValueMapResource(
                            getResourceResolver(), new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED, vm));
        }
        DataSource ds = new SimpleDataSource(fakeResourceList.iterator());
        getRequest().setAttribute(DataSource.class.getName(), ds);
    }
}
