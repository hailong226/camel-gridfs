package com.weibangong.camel.gridfs;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by jianghailong on 15/6/23.
 */
public class GridFSOperationInsertTests extends AbstractGridFSTest {

    @Test
    public void testInsertWithPath() {
        Exchange exchange = template.request("direct:insert", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(Exchange.FILE_PATH, FILE_PATH);
            }
        });

        assertResponse(exchange.getOut());
    }

    @Test
    public void testInsertWithBytes() throws IOException {
        final File file = new File(FILE_PATH);

        Exchange exchange = template.request("direct:insert", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(Exchange.FILE_NAME, file.getName());
                exchange.getIn().setHeader(Exchange.FILE_CONTENT_TYPE, "image/png");

                exchange.getIn().setBody(IOUtils.toByteArray(new FileInputStream(new File(FILE_PATH))));
            }
        });
        assertResponse(exchange.getOut());
    }

    @Test
    public void testInsertWithInputStream() {
        final File file = new File(FILE_PATH);
        Exchange exchange = template.request("direct:insert", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(Exchange.FILE_NAME, file.getName());
                exchange.getIn().setHeader(Exchange.FILE_CONTENT_TYPE, "image/png");

                exchange.getIn().setBody(new FileInputStream(new File(FILE_PATH)));
            }
        });
        assertResponse(exchange.getOut());
    }

    @Test
    public void testInsertWithFile() {
        Exchange exchange = template.request("direct:insert", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody(new File(FILE_PATH));
            }
        });
        assertResponse(exchange.getOut());
    }

    private void assertResponse(Message response) {
        File file = new File(FILE_PATH);

        Assert.assertNotNull(response.getHeader(GridFSConstants.FILE_ID));
        Assert.assertNotNull(response.getHeader(GridFSConstants.FILE_MD5));

        Assert.assertNotNull(response.getHeader(GridFSConstants.FILE_UPLOAD_DATE));
        Assert.assertNotNull(response.getHeader(Exchange.FILE_LAST_MODIFIED));

        Assert.assertNotNull(response.getHeader(Exchange.FILE_LENGTH));
        Assert.assertTrue(response.getHeader(Exchange.FILE_LENGTH, Long.class) == file.length());

        Assert.assertEquals(response.getHeader(Exchange.FILE_CONTENT_TYPE, String.class), "image/png");

        Assert.assertNotNull(response.getHeader(Exchange.FILE_NAME));
        Assert.assertEquals(response.getHeader(Exchange.FILE_NAME, String.class), file.getName());
    }
}
