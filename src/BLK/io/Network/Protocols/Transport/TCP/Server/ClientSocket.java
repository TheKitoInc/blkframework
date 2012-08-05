/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.Network.Protocols.Transport.TCP.Server;

import BLK.System.Logger;
import BLK.System.Threads.Thread;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
/**
 *
 * @author andresrg
 */
public class ClientSocket extends Thread
{    
    public static void add(Socket s, Listener l) {new ClientSocket(s,l).start();}

    private Socket s;
    private Listener l;
    
            
    private ClientSocket(Socket s, Listener l) 
    {
        super("TCPClient:"+s.getInetAddress().getHostAddress()+":"+l.getPort());
        this.s = s;
        this.l = l;        
    }
    
    @Override
    protected void doThread() 
    {
        this.l.addClient(this);
        
        try
        {
            this.l.getIClient().newConnection(this.s.getInputStream(),this.s.getOutputStream(),this.s.getInetAddress());
        }
        catch(Exception ex)
        {            
            Logger.getLogger().error(ex);
        }
        
        try
        {
            if(this.s.isConnected() || !this.s.isClosed())            
                this.s.close();
        }
        catch(IOException ex){}
        
        this.l.removeClient(this);
    }

    
    
    
    
    
    
    
   
    public static boolean valid(InetAddress inetAddress) 
    {
        return true; 
    }




}
