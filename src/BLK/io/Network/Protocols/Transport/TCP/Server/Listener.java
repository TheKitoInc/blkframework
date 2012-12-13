/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.Network.Protocols.Transport.TCP.Server;

import BLK.System.Logger;
import BLK.System.Threads.IterativeThread;
import BLK.System.Threads.Thread;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
/**
 *
 * @author andresrg
 */
public final class Listener extends IterativeThread
{
    private int port;
    private int backlog;
    private InetAddress bindAddr;
    private int maxConnections;
    private Events events;
    
    private ServerSocket ss=null;
    
    private ArrayList<Socket> connections = new ArrayList<Socket>();
    
    public int getMaxConnections() {return maxConnections;}
    public void setMaxConnections(int maxConnections) {this.maxConnections = maxConnections;}
    public int getPort() {return port;}
    public int getBacklog() {return backlog;}
    public InetAddress getBindAddr() {return bindAddr;}
    public ArrayList<Socket> getConnections() {return connections;}
    
    public Listener(Events events, int port, int backlog, InetAddress bindAddr) 
    {
        super("TCPServer:"+port);
        this.events = events;
        this.port = port;
        this.backlog = backlog;
        this.bindAddr = bindAddr;
        this.maxConnections = backlog;
    }
    public Listener(Events events, int port, int backlog)  {this(events, port, backlog, null);}
    public Listener(Events events, int port) {this(events, port, 10);}
    public Listener(Events events) {this(events, 0);}
    
    @Override
    public final void pause() {super.pause();}
    @Override
    public final void stop() {super.stop();}
    @Override
    public final void start() {super.start();}

    @Override
    protected final boolean init() 
    {
        if(this.ss==null)
        {
            try 
            {
                this.ss=new ServerSocket(this.port, this.backlog, this.bindAddr);
                this.ss.setSoTimeout(1000);                
            } 
            catch (IOException ex) 
            {                
                Logger.getLogger().error(ex);
                return false;
            }
        }
        return super.init();
    }
    @Override
    protected final void end() 
    {
        if(this.ss!=null && !this.ss.isClosed())        
        {
            try
            {
                this.ss.close();    
            }
            catch(IOException ex) {}            
        }
        
        this.ss=null;
        
        for(Socket s : this.connections)
        {
            try
            {
                if(s!=null && !s.isClosed())
                    s.close();    
                
                this.connections.remove(s);
            }
            catch(IOException ex) {}            
        
        
        }
        System.gc();
    }
    
    
    @Override
    protected final void doIteration() 
    {   
        
        while(this.maxConnections<=this.connections.size())
            Thread.sleepThread(100);
        
        try 
        {
            final Socket s = this.ss.accept();
            if(this.events.reject(s.getInetAddress()))
                s.close();
            else
            {
                final Listener li=this;
                
                (new Thread("TCPServerSocket:"+s.getInetAddress().getHostAddress()+":"+this.port) 
                {
                    @Override
                    protected void doThread() 
                    {
                        li.connections.add(s);
                        
                        try 
                        {                            
                            li.events.process(li, s.getInputStream(), s.getOutputStream(), s.getInetAddress());
                        
                            if(!s.isClosed())                            
                                s.close();
                        } 
                        catch (IOException ex) 
                        {
                        
                        }
                        
                        li.connections.remove(s);
                    }
                }).start();
                
            }
        }
        catch(SocketTimeoutException tex){}
        catch (IOException ex) 
        {
            Logger.getLogger().warn(ex);
        }        
    }


    
}
