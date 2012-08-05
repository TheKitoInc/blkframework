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
    private IClient iclient;
    
    private ServerSocket ss=null;

    private ArrayList<ClientSocket> connections = new ArrayList<ClientSocket>();
    
    public void addClient(ClientSocket client) {this.connections.add(client);}
    public void removeClient(ClientSocket client) {this.connections.remove(client);}
    
    public Listener(int port, int backlog, InetAddress bindAddr) 
    {
        super("TCPListener:"+port);
        this.port = port;
        this.backlog = backlog;
        this.bindAddr = bindAddr;
        this.maxConnections = backlog;
    }
    public Listener(int port, int backlog)  {this(port, backlog, null);}
    public Listener(int port) {this(port, 10);}
    public Listener() {this(0);}

    @Override
    public void pause() {super.pause();}
    @Override
    public void stop() {super.stop();}
    @Override
    public void start() {super.start();}
    
    
    
    
    @Override
    protected boolean init() 
    {
        if(this.ss==null)
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

        return super.init();
    }

    
    
    
    @Override
    protected void end() 
    {
        if(this.ss!=null && !this.ss.isClosed())        
            try
            {
                this.ss.close();    
            }
            catch(IOException ex) {}            
        
        this.ss=null;
        System.gc();
    }


  
    @Override
    protected void doIteration() 
    {   
        
        while(this.maxConnections<=this.connections.size())
            Thread.sleepThread(100);
        
        try 
        {
            Socket s=this.ss.accept();
            if(ClientSocket.valid(s.getInetAddress()))                        
                ClientSocket.add(s,this);            
            else            
                if(s.isConnected() || !s.isClosed())            
                    s.close();
            
        }
        catch(SocketTimeoutException tex){}
        catch (IOException ex) 
        {
            Logger.getLogger().warn(ex);
        }        
    }

    public int getPort() {return this.ss.getLocalPort();}
    public IClient getIClient() {return this.iclient;}
    
}
