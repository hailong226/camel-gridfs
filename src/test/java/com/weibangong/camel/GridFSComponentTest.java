package com.weibangong.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

public class GridFSComponentTest extends AbstractGridFSTest {

    @Test
    public void testGridFS() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);       
        
        assertMockEndpointsSatisfied();
    }

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
}
