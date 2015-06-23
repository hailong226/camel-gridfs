package com.weibangong.camel.gridfs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by jianghailong on 15/6/23.
 */
@Configuration
@Import(EmbedMongoConfiguration.class)
@ImportResource("com/weibangong/camel/gridfs/gridFsBasicOperationsTests.xml")
public class MongoBasicOperationsConfiguration {
}
