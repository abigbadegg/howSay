package com.howsay.conf;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;

import com.darcytech.doris.alert.alertmanager.client.AlertManagerClient;
import com.darcytech.doris.alert.config.AlertProperties;
import com.darcytech.doris.alert.handler.DorisAlertPoolRejectedExecutionHandler;
import com.darcytech.doris.alert.logback.DorisLogbackMetricsAppender;
import com.darcytech.doris.alert.metric.DorisAlertCounter;
import com.darcytech.doris.alert.tool.Alerter;
import com.google.common.collect.Lists;
import com.howsay.util.MyAlerter;
import com.howsay.util.MyLogbackAppender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class prometheusConfiguration implements InitializingBean {

    @Autowired
    AlertProperties alertProperties;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://qyapi.weixin.qq.com/cgi-bin/webhook")
                .build();
    }

    @Bean
    @Lazy
    TaskExecutor dorisAlertExecutor2(AlertProperties alertProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(alertProperties.getExecutorCoreSize());
        executor.setMaxPoolSize(alertProperties.getExecutorCoreSize());
        executor.setKeepAliveSeconds((int) Duration.of(10, ChronoUnit.MINUTES).getSeconds());
        executor.setQueueCapacity(alertProperties.getExecutorQueueSize());
        executor.setRejectedExecutionHandler(new DorisAlertPoolRejectedExecutionHandler());
        executor.initialize();
        return null;
    }

    @Bean
    MyAlerter myAlerter(AlertProperties alertProperties) {
        return new MyAlerter(dorisAlertExecutor2(alertProperties));
    }

    @Override
    public void afterPropertiesSet() throws InterruptedException {
        MyLogbackAppender.initCounter(myAlerter(alertProperties));
        new Thread(() -> {
            log.error("test error");
        }, "main-subThread").start();
        TimeUnit.SECONDS.sleep(5L);
        log.info("test info");
    }
}
