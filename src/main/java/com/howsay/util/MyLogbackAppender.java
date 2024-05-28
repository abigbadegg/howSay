package com.howsay.util;

import javax.annotation.Nullable;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class MyLogbackAppender extends AppenderBase<ILoggingEvent> {

    @Nullable
    private static MyAlerter myAlerter;

    public static void initCounter(MyAlerter alerter) {
        myAlerter = alerter;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        sendErrorLogAlertMessage(event);
    }

    private void sendErrorLogAlertMessage(ILoggingEvent event) {
        if (myAlerter == null || event.getLevel().toInt() != Level.ERROR_INT) {
            return;
        }
        myAlerter.alert();
    }

}
