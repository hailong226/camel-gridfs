package com.weibangong.camel.gridfs;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by jianghailong on 15/6/22.
 */
public abstract class AbstractGridFSTest extends CamelTestSupport {

    protected ApplicationContext applicationContext;

    @Override
    protected CamelContext createCamelContext() throws Exception {
        applicationContext = new AnnotationConfigApplicationContext(EmbedMongoConfiguration.class);
        CamelContext ctx = SpringCamelContext.springCamelContext(applicationContext);
        PropertiesComponent pc = new PropertiesComponent("classpath:gridfs.test.properties");
        ctx.addComponent("properties", pc);
        return ctx;
    }
}
