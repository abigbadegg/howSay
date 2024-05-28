package com.howsay.scheduler;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TestScheduler {

    @Autowired
    ExecutorService firstExecutor;

    static int i = 0;

    @Scheduled(fixedDelay = 1000)
    public void test() throws InterruptedException {
        if (i < 2) {

        }
        i++;

        Thread.sleep(1000);
        //log.info("TestScheduler warn!, {} ", i);
    }

}
