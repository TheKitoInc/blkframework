/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.Network.Protocols.Transport.TCP;

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
public abstract class Server extends IterativeThread
{
    private int port;
    private int backlog;
    private InetAddress bindAddr;
    private int maxConnections;
    
    private ServerSocket ss=null;
    
    private ArrayList<Socket> connections = new ArrayList<Socket>();
    
    protected int getMaxConnections() {return maxConnections;}
    protected void setMaxConnections(int maxConnections) {this.maxConnections = maxConnections;}
    protected int getPort() {return port;}
    protected int getBacklog() {return backlog;}
    protected InetAddress getBindAddr() {return bindAddr;}
    protected ArrayList<Socket> getConnections() {return connections;}
    
    protected Server(int port, int backlog, InetAddress bindAddr) 
    {
        super("TCPServer:"+port);
        this.port = port;
        this.backlog = backlog;
        this.bindAddr = bindAddr;
        this.maxConnections = backlog;
    }
    protected Server(int port, int backlog)  {this(port, backlog, null);}
    protected Server(int port) {this(port, 10);}
    protected Server() {this(0);}
    
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
            if(this.reject(s))
                s.close();
            else
            {
                final Server li=this;
                (new Thread("TCPServerSocket:"+s.getInetAddress().getHostAddress()+":"+this.port) 
                {
                    @Override
                    protected void doThread() 
                    {
                        li.connections.add(s);
                        
                        try 
                        {
                            li.process(s);
                        
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

    protected Boolean reject(Socket s) {return false;}
    protected abstract void process(Socket s)  throws IOException;

    
}
