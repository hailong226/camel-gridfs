package com.weibangong.camel.gridfs;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by jianghailong on 15/6/23.
 */
public class GridFSOperationRemoveTests extends AbstractGridFSTest {

    @Test
    public void testRemoveFromHeader() {
        final String objectId = prepareObjectId();
        Exchange exchange = template.request("direct:remove", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(GridFSConstants.FILE_ID, objectId);
            }
        });
        Assert.assertEquals(objectId,
                exchange.getOut().getHeader(GridFSConstants.FILE_ID, String.class)
        );
    }

    private String prepareObjectId() {
        Exchange exchange = template.request("direct:insert", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(new File(FILE_PATH));
            }
        });
        return exchange.getOut().getHeader(GridFSConstants.FILE_ID, String.class);
    }
}
