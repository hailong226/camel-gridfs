package com.weibangong.camel.gridfs;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.UnknownHostException;

import static de.flapdoodle.embed.mongo.distribution.Version.Main.PRODUCTION;
import static de.flapdoodle.embed.process.runtime.Network.localhostIsIPv6;
import static org.springframework.util.SocketUtils.findAvailableTcpPort;

/**
 * Created by jianghailong on 15/6/22.
 */
@Configuration
public class EmbedMongoConfiguration {

    private static final int PORT = findAvailableTcpPort();

    static {
        try {
            IMongodConfig mongodConfig = new MongodConfigBuilder()
                    .version(PRODUCTION)
                    .net(new Net(PORT, localhostIsIPv6()))
                    .build();
            MongodExecutable mongodExecutable = MongodStarter.getDefaultInstance().prepare(mongodConfig);
            mongodExecutable.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public MongoClient testMongodb() throws UnknownHostException {
        return new MongoClient("0.0.0.0", PORT);
    }
}
