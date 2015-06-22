package com.weibangong.camel;

import com.mongodb.gridfs.GridFSInputFile;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * The GridFS producer.
 */
public class GridFSProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(GridFSProducer.class);

    private GridFSEndpoint endpoint;

    public GridFSProducer(GridFSEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        GridFSOperation operation = this.endpoint.getOperation();
        Object header = exchange.getIn().getHeader(GridFSConstants.OPERATION_HEADER);
        if (header != null) {
            try {
                LOG.debug("Overriding default operation with operation specified on header: {}", header);
                if (header instanceof GridFSOperation) {
                    operation = ObjectHelper.cast(GridFSOperation.class, header);
                } else {
                    operation = GridFSOperation.valueOf(exchange.getIn().getHeader(GridFSConstants.OPERATION_HEADER, String.class));
                }
            } catch (Exception e) {
                throw new CamelGridFsException("Operation specified on header is not supported. Value: " + header, e);
            }
        }

        try {
            invokeOperation(operation, exchange);
        } catch (Exception e) {
            throw GridFSComponent.wrapInCamelMongoDbException(e);
        }
    }

    protected void invokeOperation(GridFSOperation operation, Exchange exchange) throws CamelGridFsException {
        switch (operation) {
            case insert:
                doInsert(exchange);
                break;
            case remove:
                doRemove(exchange);
                break;
        }
    }

    protected void doInsert(Exchange exchange) throws CamelGridFsException {
        GridFSInputFile file = calculateContentWithBody(exchange);
        if (file == null) {
            file = calculateContentWithHeader(exchange);
        }
        if (file == null) {
            throw new CamelGridFsException("must has body or header '" + Exchange.FILE_PATH + "'.");
        }
        checkProperty(exchange, file);
        file.save();

        exchange.getOut().setHeader(GridFSConstants.FILE_ID, file.getId());
        exchange.getOut().setHeader(GridFSConstants.FILE_MD5, file.getMD5());
        exchange.getOut().setHeader(GridFSConstants.FILE_UPLOAD_DATE, file.getUploadDate());
        exchange.getOut().setHeader(Exchange.FILE_NAME, file.getContentType());
        exchange.getOut().setHeader(Exchange.FILE_LENGTH, file.getLength());
        exchange.getOut().setHeader(Exchange.FILE_LAST_MODIFIED, file.getUploadDate());
    }

    protected void doRemove(Exchange exchange) throws CamelGridFsException{
    }

    private GridFSInputFile calculateContentWithBody(Exchange exchange) {
        GridFSInputFile file = null;
        Object input = exchange.getIn().getBody();
        if (input != null) {
            if (input instanceof byte[]) {
                file = this.endpoint.getGridfs().createFile(ObjectHelper.cast(byte[].class, input));
            } else if (input instanceof InputStream) {
                file = this.endpoint.getGridfs().createFile(
                        ObjectHelper.cast(InputStream.class, input),
                        this.endpoint.isCloseStreamOnPersist()
                );
            }
        }
        return file;
    }

    private GridFSInputFile calculateContentWithHeader(Exchange exchange) throws CamelGridFsException {
        GridFSInputFile file = null;
        String filepath = exchange.getIn().getHeader(Exchange.FILE_PATH, String.class);
        if (filepath != null) {
            File localFile = new File(filepath);
            if (!localFile.exists()) {
                throw new IllegalStateException("Not found file " + filepath);
            }
            try {
                file = this.endpoint.getGridfs().createFile(localFile);
            } catch (IOException e) {
                throw GridFSComponent.wrapInCamelMongoDbException(e);
            }
        }
        return file;
    }

    private void checkProperty(Exchange exchange, GridFSInputFile file) {
        String contentType = exchange.getIn().getHeader(Exchange.FILE_CONTENT_TYPE, String.class);
        if (contentType != null) {
            file.setContentType(contentType);
        }
        String filename = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);
        if (filename != null) {
            file.setFilename(filename);
        }
        Long fileChunkSize = exchange.getIn().getHeader(GridFSConstants.FILE_CHUNK_SIZE, Long.class);
        if (fileChunkSize != null) {
            file.setChunkSize(fileChunkSize);
        }
    }
}
