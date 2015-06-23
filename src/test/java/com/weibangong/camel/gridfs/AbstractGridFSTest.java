package com.weibangong.camel.gridfs;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by jianghailong on 15/6/22.
 */
public abstract class AbstractGridFSTest extends CamelTestSupport {

    protected static final String FILE_PATH = GridFSOperationTests.class.getResource("/1.png").getPath();

    protected static Mongo mongo;
    protected static DB db;

    protected static String dbName = "gridFsTest";

    protected ApplicationContext applicationContext;

    @Override
    protected void doPostSetup() throws Exception {
        mongo = applicationContext.getBean(Mongo.class);
        db = mongo.getDB(dbName);
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        applicationContext = new AnnotationConfigApplicationContext(EmbedMongoConfiguration.class);
        CamelContext ctx = SpringCamelContext.springCamelContext(applicationContext);
        PropertiesComponent pc = new PropertiesComponent("classpath:gridfs.test.properties");
        ctx.addComponent("properties", pc);
        return ctx;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:insert").to("gridfs:testMongodb?database={{mongodb.testDb}}&operation=insert");
                from("direct:remove").to("gridfs:testMongodb?database={{mongodb.testDb}}&operation=remove");
                from("direct:findById").to("gridfs:testMongodb?database={{mongodb.testDb}}&operation=findById");
            }
        };
    }
}
