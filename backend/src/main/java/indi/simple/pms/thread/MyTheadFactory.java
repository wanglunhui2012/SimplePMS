package indi.simple.pms.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:32
 * @Description:
 */
class MyTheadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber;
    private final String namePrefix;

    public MyTheadFactory() {
        this("SimplePMS-ThreadPoolExecutor");
    }

    private MyTheadFactory(String name) {
        this.threadNumber = new AtomicInteger(1);
        SecurityManager s = System.getSecurityManager();
        this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = name + poolNumber.getAndIncrement();
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(this.group, r, this.namePrefix + "-thread-" + this.threadNumber.getAndIncrement(), 0L);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }

        if (t.getPriority() != 5) {
            t.setPriority(5);
        }

        return t;
    }
}
