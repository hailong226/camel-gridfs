package com.weibangong.camel;

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

    private volatile Mongo mongo;
    
    public GridFSComponent() {
        super(GridFSEndpoint.class);
    }

    public GridFSComponent(CamelContext context) {
        super(context, GridFSEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        if (mongo == null) {
            mongo = CamelContextHelper.mandatoryLookup(getCamelContext(), remaining, Mongo.class);
        }
        Endpoint endpoint = new GridFSEndpoint(uri, this);
        parameters.put("mongoConnection", mongo);
        setProperties(endpoint, parameters);
        return endpoint;
    }

    @Override
    protected void doShutdown() throws Exception {
        if (mongo != null) {
            mongo.close();
        }
        super.doShutdown();
    }

    public static CamelGridFsException wrapInCamelMongoDbException(Throwable t) {
        if (t instanceof CamelGridFsException) {
            return (CamelGridFsException) t;
        } else {
            return new CamelGridFsException(t);
        }
    }
}
