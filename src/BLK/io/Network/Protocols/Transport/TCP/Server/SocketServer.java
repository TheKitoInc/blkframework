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
package BLK.io.Network.Protocols.Transport.TCP.Server;


import BLK.System.Logger;
import BLK.io.Network.Protocols.Transport.TCP.TcpException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import BLK.System.Utils.Hashtable;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class SocketServer implements Runnable{
    private ISocketServer ctl;
    private int port;
    private ServerSocket socket = null;    

    private enum modeOpts {ServerSocket,AcceptThread,ClientSocket};
    private modeOpts mode;

    private Socket client;
    private Hashtable<String,String> clientParams=new Hashtable<String,String>();

    public SocketServer(ISocketServer ctl,int port) throws TcpException
    {
        try
        {
            Logger.getLogger().info("StartTCPSocketPort("+String.valueOf(port)+"): INIT");
            this.ctl = ctl;
            this.port = port;
            this.socket = new ServerSocket(this.port);
            this.mode = modeOpts.ServerSocket;
            (new Thread(new SocketServer(this.ctl, this.socket),"TCPSocketPort:"+String.valueOf(port))).start();
            this.ctl.listening(this.port);
            Logger.getLogger().info("StartTCPSocketPort("+String.valueOf(port)+"): OK");
        }
        catch (IOException ex)
        {
            Logger.getLogger().error("StartTCPSocketPort("+String.valueOf(port)+"): ERROR",ex);
            throw new TcpException(ex.getMessage());
        }
    }
    private SocketServer(ISocketServer ctl,ServerSocket socket){this.mode=modeOpts.AcceptThread;this.ctl=ctl;this.socket=socket;}
    private SocketServer(Socket client,ISocketServer ctl){this.mode=modeOpts.ClientSocket;this.client=client;this.ctl=ctl;}

    public void run() {
        Logger.getLogger().info("Thread Start");
        while (this.mode==modeOpts.AcceptThread || this.mode==modeOpts.ClientSocket)
        {
            if (this.mode==modeOpts.AcceptThread)
                if(!SocketListenThread())
                    return ;

            if(this.mode==modeOpts.ClientSocket)
                if(!SocketConnectionThread())
                    return ;
        }
        Logger.getLogger().info("Thread End");
    }

    private boolean SocketConnectionThread()    {    
        
       //End thread if connection close remotely
       if (!this.client.isConnected())
       {
            Logger.getLogger().info("Remote connection closed");
            this.ctl.clientClosed(this.client,this.clientParams);
            return false;
       }
       
       //Ask interface (final server) if close connectionremotely
       if(ctl.closeConnection(this.client,this.clientParams))
       {
            try
            {
                this.client.close();
                this.ctl.clientClosed(this.client,this.clientParams);
                Logger.getLogger().info("Connection closed");
            }
            catch (IOException ex)
            {
                Logger.getLogger().warn(ex);
                this.ctl.closeError(this.client,this.clientParams);
            }
            return false;
       }

       //Ask interface (final server) data to send
        byte[] output = this.ctl.dataToSend(this.clientParams);
        try
        {

            if (output != null)
            {
                client.getOutputStream().write(output);
                this.ctl.bytesSend(output.length,this.clientParams);
            }
        }
        catch (IOException ex)
        {
            this.ctl.sendDataError(new TcpException(ex.getMessage()),output,this.clientParams);
        }

        //if remotely data in buffer; ask interface how many bytes get it and send to interface
        try
        {
            if (client.getInputStream().available() > 0)
            {

                int bytesToGet = this.ctl.dataAvailable(client.getInputStream().available(),this.clientParams);
                if(bytesToGet>0)
                {
                    if (bytesToGet>client.getInputStream().available())
                        bytesToGet=client.getInputStream().available();

                    byte[] tmp=new byte[bytesToGet];
                    client.getInputStream().read(tmp);

                    this.dataArrivalLineMode(tmp);
                    this.ctl.dataArrival(tmp,this.clientParams);

                }

            }
        }
        catch (IOException ex)
        {
            Logger.getLogger().warn(ex);
        }

        return true;
    }
    private void dataArrivalLineMode(byte[] tmp) {
        String tmp2=new String(tmp);
        String buf;

        if(this.clientParams.containsKey("INBuffer"))
        {
            buf=this.clientParams.get("INBuffer").toString();
            this.clientParams.remove("INBuffer");
        }
        else
            buf = new String("");


        for(int i=0;i<tmp2.length()-1;i++)
            if(tmp2.substring(i, i+2).equalsIgnoreCase("\r\n"))
            {
                String ret=this.ctl.lineArrival(buf,this.clientParams,this.client);
                if (ret!=null)
                {
                    try
                    {
                       client.getOutputStream().write(ret.getBytes());
                    }
                    catch (IOException ex)
                    {
                        this.ctl.sendDataError(new TcpException(ex.getMessage()),ret.getBytes(),this.clientParams);
                    }
                }
                buf="";
            }
            else
                buf+=tmp2.substring(i,i+1);

        buf+=tmp2.substring(tmp2.length(),tmp2.length());

        this.clientParams.put("INBuffer", buf);
    }
    private boolean SocketListenThread()        {
        try
        {
            Socket clttmp=socket.accept();
            if (this.ctl.acceptNewClient(clttmp,this.clientParams))
                (new Thread(new SocketServer(clttmp, this.ctl),"TCPClient:"+clttmp.getInetAddress().getHostAddress())).start();
            else
                clttmp.close();

            if(this.ctl.noMoreConnections())
            {
                this.ctl.listenStop();
                return false;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger().warn(ex);
        }

        return true;
    }

    public ServerSocket getSocket() {
        return socket;
    }

    
}
