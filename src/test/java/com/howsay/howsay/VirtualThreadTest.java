package com.howsay.howsay;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * 只有synchronized和native方法能将虚拟线程固定到平台线程，
 * 其余的阻塞方法（i/o, lock， sleep）都会使当前虚拟线程阻塞，从而从平台线程上卸载，直到阻塞结束重新绑定平台线程
 */
public class VirtualThreadTest {
    private static final int size = 30;

    public static void main(String[] args) throws Exception {
        System.out.println("测试开始-------------------");

        // 每秒统计平台线程数
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
            ThreadInfo[] threadInfo = threadBean.dumpAllThreads(false, false);
            System.out.println(threadInfo.length + " os thread");
        }, 1, 1, TimeUnit.SECONDS);

        // 跑任务
        CountDownLatch latch2 = new CountDownLatch(size);
        LocalDateTime start2 = now();
        List<Runnable> runnables2 = createCallable(latch2);
        for (Runnable runnable : runnables2) {
            Thread.startVirtualThread(runnable);
        }
        latch2.await();
        System.out.println("虚拟线程耗时（ms）2：" + start2.until(now(), MILLIS));
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
