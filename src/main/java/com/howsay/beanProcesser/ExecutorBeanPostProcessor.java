package com.howsay.beanProcesser;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.alibaba.ttl.spi.TtlWrapper;
import com.alibaba.ttl.threadpool.TtlExecutors;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExecutorBeanPostProcessor implements BeanPostProcessor {

    // test
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof TtlWrapper) {
            Object unWrapper = ((TtlWrapper<?>) bean).unwrap();
            if (unWrapper instanceof ExecutorService) {
                ExecutorService executor = (ExecutorService) bean;
                ExecutorService monitor = ExecutorServiceMetrics.monitor(Metrics.globalRegistry, executor, beanName, Collections.emptyList());
                return TtlExecutors.getTtlExecutorService(monitor);
            } else if (unWrapper instanceof Executor) {
                Executor executor = (Executor) bean;
                Executor monitor = ExecutorServiceMetrics.monitor(Metrics.globalRegistry, executor, beanName, Collections.emptyList());
                return TtlExecutors.getTtlExecutor(monitor);
            }
        } else if (bean instanceof Executor) {
            log.info("ExecutorBeanPostProcessor postProcessAfterInitialization start, beanName:{}", beanName);
            if (bean instanceof ThreadPoolTaskExecutor) {
                ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) bean;
                ExecutorServiceMetrics.monitor(Metrics.globalRegistry, taskExecutor.getThreadPoolExecutor(), beanName, Collections.emptyList());
            } else if (bean instanceof ThreadPoolTaskScheduler) {
                ThreadPoolTaskScheduler taskScheduler = (ThreadPoolTaskScheduler) bean;
                ExecutorServiceMetrics.monitor(Metrics.globalRegistry, taskScheduler.getScheduledExecutor(), beanName, Collections.emptyList());
            } else if (bean instanceof ExecutorService) {
                ExecutorServiceMetrics.monitor(Metrics.globalRegistry, (ExecutorService) bean, beanName, Collections.emptyList());
            }
            ExecutorServiceMetrics.monitor(Metrics.globalRegistry, (Executor) bean, beanName, Collections.emptyList());
            log.info("ExecutorBeanPostProcessor postProcessAfterInitialization end, beanName:{}", beanName);
        }
        return bean;
    }

}
