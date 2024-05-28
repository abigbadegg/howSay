package com.howsay.howsay;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;

public class ThreadTest {

    private static final int size = 30;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("测试开始-------------------");

        // 每秒统计平台线程数
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
            ThreadInfo[] threadInfo = threadBean.dumpAllThreads(false, false);
            System.out.println(threadInfo.length + " os thread");
        }, 1, 1, TimeUnit.SECONDS);

        // 跑任务
        ExecutorService service = Executors.newFixedThreadPool(30);
        CountDownLatch latch1 = new CountDownLatch(size);

        LocalDateTime start = now();
        List<Runnable> runnables = createCallable(latch1);

        for (Runnable runnable : runnables) {
            service.submit(runnable);
        }
        latch1.await();
        System.out.println("平台线程耗时（ms）1：" + start.until(now(), MILLIS));

    }

    private static List<Runnable> createCallable(CountDownLatch latch) {
        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            runnables.add(() -> {
                try {
                    Thread.sleep(1 * 1000);
                    latch.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return runnables;
    }
}
