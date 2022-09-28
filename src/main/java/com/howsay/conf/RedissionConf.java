package com.howsay.conf;

import java.io.IOException;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * redission配置类，当在application。yml中redis配置中引入了redission.yml，则不需要该类
 */
@Configurable
public class RedissionConf {

    @Bean
    public RedissonClient getRedissonClient() throws IOException {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("redission.yml");
        Config config = Config.fromYAML(resource.getInputStream());
        config.useClusterServers();
        return Redisson.create(config);
    }
}
