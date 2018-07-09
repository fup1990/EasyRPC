package com.gome.fup.easy.rpc.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by fupeng-ds on 2018/7/9.
 */
public class EasyThreadFactory implements ThreadFactory {

    private final String threadNamePrefix;

    private final AtomicLong threadIndex = new AtomicLong(0);

    public EasyThreadFactory(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public Thread newThread(Runnable r) {
        return new Thread(r, this.threadNamePrefix + this.threadIndex.incrementAndGet());
    }
}
