package com.themarblefamily.marblecoin.market;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static void waitSec(int secs) {
        try {
            TimeUnit.SECONDS.sleep(secs);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
