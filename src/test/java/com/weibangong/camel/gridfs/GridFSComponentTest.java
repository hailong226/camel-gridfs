package com.weibangong.camel.gridfs;

import org.junit.Test;

public class GridFSComponentTest extends AbstractGridFSTest {

    @Test
    public void testGridFS() throws Exception {
        System.out.println("mock:result");
    }
/*
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("gridfs://foo")
                  .to("gridfs://bar")
                  .to("mock:result");
            }
        };
    }
*/
}
