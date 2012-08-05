/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BLK.io.Network.Protocols.Transport.TCP.Server;

import BLK.System.Logger;
import BLK.io.Network.Protocols.Transport.ConnectionIO;
import BLK.io.Network.Protocols.Transport.TCP.Connection;
import java.io.IOException;
import java.net.ServerSocket;



/**
 *
 * @author andresrg
 */
public class Socket extends BLK.System.Threads.IterativeThread
{
    private ServerSocket s;
    private ConnectionIO applicationProtocol;

    public Socket(Integer port,ConnectionIO applicationProtocol)
    {
        super("Socket_TCP:"+port.toString());
        this.applicationProtocol=applicationProtocol;
        try
        {
            this.s = new ServerSocket(port);
            super.start();
        }
        catch (IOException ex)
        {
            Logger.getLogger().error("Start TCP Listen on "+port.toString(),ex);
            throw new UnsupportedOperationException(ex.getMessage());
        }

    }
    public Boolean Close()
    {
        try
        {
            this.s.close();
            super.stop();
            return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger().error("Stop TCP Listen",ex);
            return false;
        }
    }
    public Boolean IsClosed()
    {
        return this.s.isClosed();
    }
    @Override
    protected void doIteration()
    {
        try
        {
            java.net.Socket c=this.s.accept();

            if(!Connection.createConnection(this,c))
                Logger.getLogger().warn("Open tcp connection fail on client " + c.getInetAddress().toString());
        }
        catch (IOException ex)
        {
            Logger.getLogger().error(ex);
        }
    }

    public Integer getPort() {return this.s.getLocalPort();}
    public ConnectionIO getApplicationProtocol() {return this.applicationProtocol;}

    }
