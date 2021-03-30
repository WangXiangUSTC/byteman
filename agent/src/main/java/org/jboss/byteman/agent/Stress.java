package org.jboss.byteman.agent;

import org.jboss.byteman.rule.helper.Helper;

import java.util.*;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

interface StressRunnable extends Runnable {

    public void shutdown();
}

class CPUStress implements StressRunnable {
    private Thread t;
    private String threadName;
    private int num;
    private boolean flag;
   
    private ReentrantLock lock = new ReentrantLock();
    
    CPUStress( String name, int num) {
        threadName = name;
        this.num = num;
        flag = true;
        Helper.verbose("Creating " +  threadName );
    }
   
    public void run() {
        Helper.verbose("Running " +  threadName );
        
        while (true) {
            lock.lock();
            boolean exit = !flag; 
            lock.unlock();
            if (exit) {
                break;
            }
        }

        Helper.verbose("Exiting " +  threadName );
    }
   
    public void start () {
        Helper.verbose("Starting " +  threadName );
    
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
   }

    public void shutdown() {
        Helper.verbose("Shutdown " +  threadName );
        lock.lock();
        flag = false;
        lock.unlock();
    }
}
