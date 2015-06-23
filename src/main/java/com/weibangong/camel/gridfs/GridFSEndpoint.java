package com.weibangong.camel.gridfs;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a GridFS endpoint.
 */
@UriEndpoint(scheme = "gridfs", title = "GridFS", syntax="gridfs:getConnectionBean", consumerClass = GridFSConsumer.class, label = "GridFS")
public class GridFSEndpoint extends DefaultEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(GridFSEndpoint.class);

    @UriPath @Metadata(required = "true")
    private String connectionBean;

    @UriParam
    private String database;

    @UriParam(defaultValue = "true")
    private boolean closeStreamOnPersist;

    @UriParam(defaultValue = "fs")
    private String bucket = "fs";

    @UriParam
    private GridFSOperation operation;

    @UriParam
    private String objectId;

    private Mongo mongoConnection;

    private GridFS gridfs;

    private DB db;

    public GridFSEndpoint() {
    }

    public GridFSEndpoint(String uri, GridFSComponent component) {
        super(uri, component);
    }

    public GridFSEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        initializeConnection();
        return new GridFSProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new GridFSConsumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }

    public void initializeConnection() throws CamelGridFsException {
        LOG.info("Initialising MongoDb endpoint: {}", this.toString());
        if (database == null) {
            throw new CamelGridFsException("Missing required endpoint configuration: database");
        }
        db = mongoConnection.getDB(database);
        if (db == null) {
            throw new IllegalStateException("Could not initialize GridFsComponent. Database " + database + " does not exist.");
        }
        gridfs = new GridFS(db);
    }

    public void setConnectionBean(String connectionBean) {
        this.connectionBean = connectionBean;
    }

    public String getConnectionBean() {
        return connectionBean;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatabase() {
        return database;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setMongoConnection(Mongo mongoConnection) {
        this.mongoConnection = mongoConnection;
    }

    public Mongo getMongoConnection() {
        return mongoConnection;
    }

    public void setCloseStreamOnPersist(boolean closeStreamOnPersist) {
        this.closeStreamOnPersist = closeStreamOnPersist;
    }

    public boolean isCloseStreamOnPersist() {
        return closeStreamOnPersist;
    }

    public void setOperation(GridFSOperation operation) {
        this.operation = operation;
    }

    public GridFSOperation getOperation() {
        return operation;
    }

    public GridFS getGridfs() {
        return gridfs;
    }
}
