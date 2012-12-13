/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.Network.Protocols.Transport.UDP.Server;

import BLK.System.Logger;
import BLK.System.Threads.IterativeThread;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
/**
 *
 * @author andresrg
 */
public final class Listener extends IterativeThread
{
    private int port;
    private InetAddress bindAddr;
    private int packageBytes=1024;
    private Events events;
    
    private DatagramSocket ds=null;

    
    protected int getPort() {return port;}
    protected InetAddress getBindAddr() {return bindAddr;}

    public int getPackageBytes() {return packageBytes;}
    public void setPackageBytes(int packageBytes) {this.packageBytes = packageBytes;}
    
    public Listener(Events events, int port, InetAddress bindAddr) 
    {
        super("UDPServer:"+port);
        this.events = events;
        this.port = port;
        this.bindAddr = bindAddr;        
    }
    public Listener(Events events, int port)  {this(events, port, null);}    
    public Listener(Events events) {this(events,0);}
    
    @Override
    public final void pause() {super.pause();}
    @Override
    public final void stop() {super.stop();}
    @Override
    public final void start() {super.start();}

    @Override
    protected final boolean init() 
    {
        if(this.ds==null)
        {
            try 
            {
                this.ds=new DatagramSocket(this.port, this.bindAddr);
                this.ds.setSoTimeout(1000);                
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
        if(this.ds!=null && !this.ds.isClosed())        
            this.ds.close();              
        
        this.ds=null;        
        System.gc();
    }
    
    
    @Override
    protected final void doIteration() 
    {   
        try 
        {
            byte buf[] = new byte[this.packageBytes];
            DatagramPacket p = new DatagramPacket(buf, buf.length);            
            this.ds.receive(p);
            InetAddress remoteip = p.getAddress();
            int remoteport = p.getPort();
            
            try
            {
                byte data[] = new byte[p.getLength()];
                System.arraycopy(buf, 0, data, 0, data.length);
                this.events.process(this,data,remoteport,remoteip);
            }
            catch(Exception ex){}
            
        }
        catch(SocketTimeoutException tex){}
        catch (IOException ex) 
        {
            Logger.getLogger().warn(ex);
        }        
    }
    
    public final void send(byte[] data, int port, InetAddress ip) throws IOException
    {
            DatagramPacket dp = new DatagramPacket(data, data.length, ip, port);
            this.ds.send(dp);
    }
    
}
