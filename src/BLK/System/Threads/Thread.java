/*
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 */

package BLK.System.Threads;

import BLK.System.Logger;
import java.lang.Thread.State;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public abstract class Thread implements java.lang.Runnable 
{
    
    public static final int MIN_PRIORITY = 1;
    public static final int NORM_PRIORITY = 5;
    public static final int MAX_PRIORITY = 10;

    public static void waitAllThreads()
    {
        while (java.lang.Thread.activeCount()>1)        
            sleepThread(1000);
    }
    public static void sleepThread(long millis)
    {
        try
        {
            java.lang.Thread.yield();
            java.lang.Thread.sleep(millis);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger().warn("Thread Wait", ex);
        }
    }

    
    private java.lang.Thread t;
    private enum ThreadStatus {Stopped,Starting,Running};
    private ThreadStatus status;

    protected abstract void doThread();
    
    public void setPriority(int newPriority){t.setPriority(newPriority);}
    public int getPriority(){return t.getPriority();}

    public Thread(String name)
    {
        this.status=ThreadStatus.Stopped;
        this.t=new java.lang.Thread(this, name);
    }
    
    @Override
    public void run()
    {
        if(this.status==ThreadStatus.Running)
            return;
        
        Logger.getLogger().info("Thread Started");        
        this.status=ThreadStatus.Running;
        
        try
        {
            this.doThread();
            this.status=ThreadStatus.Stopped;
            Logger.getLogger().info("Thread Stopped");
        }
        catch(Exception ex)
        {
            this.status=ThreadStatus.Stopped;            
            Logger.getLogger().error("Thread Fail",ex);            
        }                
    }

    public String getName(){return this.t.getName();}
    public boolean isStopped(){return this.status==ThreadStatus.Stopped;}
    public boolean isStarting(){return this.status==ThreadStatus.Starting;}
    public boolean isRunning(){return this.status==ThreadStatus.Running;}
    public boolean isWaiting(){return this.t.getState()==State.TIMED_WAITING || this.t.getState()==State.WAITING;}
    public boolean isBlocked(){return this.t.getState()==State.BLOCKED;}
        
    public synchronized void start()
    {
        if(this.status!=ThreadStatus.Stopped)
            return;

        this.status=ThreadStatus.Starting;
        Logger.getLogger().info("Do Thread '"+this.t.getName()+"' Start");
        this.t.start();
    }
                
}
