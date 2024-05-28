package com.howsay.conf;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.alibaba.ttl.threadpool.TtlExecutors;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;

@Configuration
public class SchedulerConfig{

/*    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setBeanName("tbWebScheduler");
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        Executor tbWebScheduler = ExecutorServiceMetrics.monitor(Metrics.globalRegistry, taskScheduler, "tbWebScheduler");
        taskRegistrar.setTaskScheduler(taskScheduler);
    }*/

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler testExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        // 配置线程池大小
        threadPoolTaskScheduler.setPoolSize(20);

        // 设置线程名
        threadPoolTaskScheduler.setThreadNamePrefix("task-scheduling-");
        // 设置等待任务在关机时完成
        //    threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 设置等待终止时间
        // threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        //Executor tbWebScheduler = ExecutorServiceMetrics.monitor(Metrics.globalRegistry, threadPoolTaskScheduler, "testExecutor");

        return threadPoolTaskScheduler;
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService firstExecutor() {
        ExecutorService executorService = Executors.newFixedThreadPool(10, new CustomizableThreadFactory("typhon-second-"));
        return executorService;
    }
}
