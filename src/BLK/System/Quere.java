/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.System;

import java.util.concurrent.Semaphore;

/**
 *
 * @author andresrg
 */
public class Quere<T>
{    
    private Object[] data;

    private int c_read=-1;
    private int c_write=-1;
    
    private Semaphore writes;
    private Semaphore reads = new Semaphore(0);    
    private Semaphore sc_read = new Semaphore(1);
    private Semaphore sc_write = new Semaphore(1);
    
    public Quere(int size) 
    {
        this.data = new Object[size];
        this.writes = new Semaphore(size);        
    }
    
    private int getCRead() throws InterruptedException
    {
        this.reads.acquire();
        
        this.sc_read.acquire();
        
        this.c_read++;
        
        this.c_read=this.c_read % this.data.length; 
        
        int i = this.c_read;
        
        this.sc_read.release();
        
        return i;
    }
    
    private int getCWrite() throws InterruptedException
    {
        this.writes.acquire();
        
        this.sc_write.acquire();
        
        this.c_write++;
        
        this.c_write=this.c_write % this.data.length;
        
        int i = this.c_write;       
        
        this.sc_write.release();
        
        return i;
    }    
    
    public void write(T data) throws InterruptedException
    {
        this.data[this.getCWrite()]=data;
        this.reads.release();
    }
    
    public T read() throws InterruptedException
    {
        Object o = this.data[this.getCRead()];
        this.writes.release();
        return (T) o;
    }
}
