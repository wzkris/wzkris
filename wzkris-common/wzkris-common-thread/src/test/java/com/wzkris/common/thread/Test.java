package com.wzkris.common.thread;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 100_00L,
            TimeUnit.MILLISECONDS, new SynchronousQueue<>());

    public void test() {
    }

}
