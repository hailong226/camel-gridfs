package com.weibangong.camel.gridfs;

import com.mongodb.Mongo;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;
import org.apache.camel.util.CamelContextHelper;

import java.util.Map;

/**
 * Represents the component that manages {@link GridFSEndpoint}.
 */
public class GridFSComponent extends UriEndpointComponent {

    private volatile Mongo db;
    
    public GridFSComponent() {
        super(GridFSEndpoint.class);
    }

    public GridFSComponent(CamelContext context) {
        super(context, GridFSEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        if (db == null) {
            db = CamelContextHelper.mandatoryLookup(getCamelContext(), remaining, Mongo.class);
        }
        GridFSEndpoint endpoint = new GridFSEndpoint(uri, this);
        endpoint.setConnectionBean(remaining);
        endpoint.setMongoConnection(db);
        setProperties(endpoint, parameters);
        return endpoint;
    }

    @Override
    protected void doShutdown() throws Exception {
        if (db != null) {
            db.close();
        }
        super.doShutdown();
    }

    public static CamelGridFsException wrapInCamelGridFsException(Throwable t) {
        if (t instanceof CamelGridFsException) {
            return (CamelGridFsException) t;
        } else {
            return new CamelGridFsException(t);
        }
    }
}
