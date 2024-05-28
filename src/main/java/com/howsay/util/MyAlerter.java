package com.howsay.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class MyAlerter {

    @Autowired
    TaskExecutor dorisAlertExecutor2;

    public void alert() {
        dorisAlertExecutor2.execute(() -> log.warn("发送AlterManager异常："));
    }

}
