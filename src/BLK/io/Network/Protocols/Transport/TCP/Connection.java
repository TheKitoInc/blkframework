/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BLK.io.Network.Protocols.Transport.TCP;

import BLK.System.Logger;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author andresrg
 */
public class Connection extends BLK.System.Threads.Thread
{
    private static ArrayList<Connection> cnns=new BLK.System.Utils.ArrayList<Connection>();
    public static Boolean createConnection(BLK.io.Network.Protocols.Transport.TCP.Server.Socket socket, java.net.Socket client)
    {
        Connection c= new Connection(socket, client);
        cnns.add(c);
        c.start();
        return true;
    }

    private BLK.io.Network.Protocols.Transport.TCP.Server.Socket socket;
    private java.net.Socket client;
    

    private Connection(BLK.io.Network.Protocols.Transport.TCP.Server.Socket socket, Socket client)
    {
        super ("TCP_Connection_From:"+client.getInetAddress().toString()+":"+socket.getPort().toString());
        this.socket=socket;
        this.client=client;
    }

    @Override
    protected void doThread()
    {        
       try
       {
            if(!client.isClosed())
                this.socket.getApplicationProtocol().configure(this, this.client.getInputStream(), this.client.getOutputStream());
        }
       catch (IOException ex)
       {
            Logger.getLogger().error(ex);
       }
    }


}