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

package BLK.io.Network.Protocols.Transport.TCP.Client;

import BLK.System.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;


/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class TCPSocketClient{
    private Socket s = null;
    private InputStream is=null;
    private OutputStream os=null;
    private String host=null;
    private int port=0;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public TCPSocketClient() {}
    
    public TCPSocketClient(InetAddress address, int port) {this.port=port;this.host=address.getHostAddress();}
    public TCPSocketClient(String host, int port)  {this.port=port;this.host=host;}

    public void connect()  throws TcpClientException {
        try
        {
            this.s = new Socket(this.host, this.port);
            this.is=this.s.getInputStream();
            this.os=this.s.getOutputStream();
        }
        catch (IOException ex)
        {
            throw new TcpClientException(ex.getMessage());
        }
    }
    public void close()  throws TcpClientException {
        try
        {
            this.os.flush();
            BLK.System.Threads.Thread.sleepThread(100);
            this.is.close();
            this.os.close();
            this.s.close();

            this.os=null;
            this.is=null;
            this.s=null;
            System.gc();
        }
        catch (IOException ex)
        {
            throw new TcpClientException(ex.getMessage());
        }
    }

    public InputStream getIs() {
        return is;
    }

    public OutputStream getOs() {
        return os;
    }

    public Socket getS() {
        return s;
    }

    public boolean isConnect ()
    {
        if(this.s.isConnected() && !this.s.isClosed() && this.s.isBound())
        {
            try
            {
                this.getOs().flush();
                return true;
            }
            catch (IOException ex)
            {
                return false;
            }
        }
        else
            return false;
        
    }

    public boolean sendString(String str)
    {
        try
        {
            this.getOs().write(str.getBytes());
            this.getOs().flush();
            return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger().error(ex);
            return false;
        }
    }
    public String bufferToString()
    {

        try
        {
            byte[] b = new byte[this.getIs().available()];
            this.getIs().read(b);
            return new String(b);
        }
        catch (IOException ex)
        {
            Logger.getLogger().error(ex);
            return new String();
        }

    }

    private String buf = new String();
    private ArrayList<String> lines = new ArrayList<String>();
    public String getNextLine()
    {
        buf=buf.concat(this.bufferToString());


        String[] Lines_ = buf.split("\r\n");
        if(buf.endsWith("\r\n"))
        {
            buf=new String();
            for (String line : Lines_)
                lines.add(line);
        }
        else
        {
            buf=Lines_[Lines_.length-1];
             for (String line : Lines_)
                 if(!line.equalsIgnoreCase(buf))
                    lines.add(line);
        }

        if (!lines.isEmpty())
            return lines.remove(0);
        else
            return null;

    }


    

    
    
    private void dataArrivlLineMode(byte[] tmp) {
        



    }

}
