package org.jboss.byteman.agent;

import org.jboss.byteman.rule.helper.Helper;

import java.util.*;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

/*
    Stress is an interface used for inject stress on Java process, include Memory and CPU.
 */
interface Stress {
    // load the stress
    public void load();

    // quit stops the stress load
    public void quit();
}

class CPUStress implements Stress {
    private String name;
    private int cpuCount;
    private ArrayList<CPUStressThread> threads;

    CPUStress(String name, int cpuCount) {
        this.name = name;
        this.cpuCount = cpuCount;
        threads = new ArrayList<CPUStressThread>();
    }

    public void load() {
        for (int i = 0; i < cpuCount; i++) {
            CPUStressThread thread = new CPUStressThread(name + i);
            threads.add(thread);
            thread.start();
        }
    }

    public void quit() {
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).shutdown();
        }
    }
}

interface StressRunnable extends Runnable {

    public void shutdown();
}

class CPUStressThread implements StressRunnable {
    private Thread t;
    private String threadName;
    private boolean flag;
   
    private ReentrantLock lock = new ReentrantLock();
    
    CPUStressThread( String name ) {
        threadName = name;
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
