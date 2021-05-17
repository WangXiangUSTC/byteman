package org.jboss.byteman.sample.helper;

import java.util.*;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.SynchronousQueue;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.Executors;

public class ThreadTask implements Runnable{
    int interval;
    public ThreadTask() {
        
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
    
    public void run() {
        System.out.println(Thread.currentThread().getName());
         if (interval > 0) {
            try {
                for (int i = 0; i < interval; i++) {
                    String stop = System.getProperty("THREAD_POOL_INJECT_STOP");
                    
                    if ((stop != null) && (stop.equals("true"))) {
                        System.out.println("exit the injected thread");
                        return;
                    }
                    //System.out.println(Thread.currentThread().getName() + "sleep 1 second");
                    Thread.sleep(1000);
                }
                
            } catch(Exception e) {
                System.out.println("get exception when execute new thread:" + e);
                return;        
            }
        }
    }
}