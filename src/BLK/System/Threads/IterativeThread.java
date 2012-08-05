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

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public abstract class IterativeThread extends Thread
{

    protected boolean init(){return true;}
    protected void end() {}

    private enum ThreadStatus {Stopping,Pausing,Stopped,Paused,Running};
    private ThreadStatus status;

    protected abstract void doIteration();
    public IterativeThread(String name)
    {
        super(name); 
        this.status=ThreadStatus.Stopped;
    }

    @Override
    protected final void doThread()
    {
        this.status=ThreadStatus.Running;

        if(this.init())
            while (this.status!=ThreadStatus.Stopping)
            {
                if (this.status==ThreadStatus.Pausing)
                    this.status=ThreadStatus.Paused;

                if (this.status==ThreadStatus.Paused)                            
                    Thread.sleepThread(100);            
                else
                    try
                    {
                        this.doIteration();
                    }
                    catch(Exception ex)
                    {
                        Logger.getLogger().error("Thread Iteration Fail",ex);   
                    }
            }
        
        this.end();
        this.status=ThreadStatus.Stopped;
    }
    
    @Override
    public void start()
    {        
        if (this.status==ThreadStatus.Pausing || this.status==ThreadStatus.Paused)
            this.status=ThreadStatus.Running;
        else if(this.status==ThreadStatus.Stopped)        
            super.start();        
    }

    public void stop()
    {
        if (this.status!=ThreadStatus.Stopped && this.status!=ThreadStatus.Stopping)
            this.status=ThreadStatus.Stopping;
    }
    
    public void pause()
    {
        if(this.status==ThreadStatus.Running)
            this.status=ThreadStatus.Pausing;
    }
    
    @Override
    public boolean isStopped(){return this.status==ThreadStatus.Stopped;}
    public boolean isStopping(){return this.status==ThreadStatus.Stopping;}
    
    @Override
    public boolean isRunning(){return this.status==ThreadStatus.Running;}
    
    public boolean isPausing(){return this.status==ThreadStatus.Pausing;}
    public boolean isPaused(){return this.status==ThreadStatus.Paused;}
    
}
