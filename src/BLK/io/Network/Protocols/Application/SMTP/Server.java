/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.Network.Protocols.Application.SMTP;

import BLK.io.Network.Protocols.Transport.TCP.Server.Events;
import BLK.io.Network.Protocols.Transport.TCP.Server.Listener;
import BLK.System.Threads.Thread;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;

/**
 *
 * @author andresrg
 */
public final class Server implements Events
{

    private Listener tcp;
    
    public Server(int backlog, int port, InetAddress bindAddr) 
    {
        this.tcp=new Listener(this, port, backlog, bindAddr);        
    }
    public Server(int backlog, int port) {this(backlog, port, null);}    
    public Server(int backlog) {this(backlog, 2525);}
    public Server() {this(10);}


    public final void pause() {this.tcp.pause();}    
    public final void stop() {this.tcp.stop();}    
    public final void start() {this.tcp.start();}
    
    
    
    @Override
    public void process(Listener li, InputStream inputStream, OutputStream outputStream, InetAddress inetAddress) 
    {
        try {
            BufferedReader b = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream));
            
            w.write("220 Server ready for transmission");
            w.newLine();
            w.flush();
            
            boolean inData=false;
            String data=new String();
            
            while (true) 
            {
                if (!b.ready()) 
                    Thread.sleepThread(500);
                else
                {
                    String inputLine = b.readLine();
                        
                    if(inData)
                    {
                        data+=inputLine+"\n\r";
                        
                        if(data.endsWith("\n\r.\n\r"))
                        {
                            w.write("250 Ok");
                            w.newLine();
                            w.flush();
                            inData=false;  
                            data=data.substring(0, data.length()-5);
                        }
                    }
                    else
                    {
                        if(inputLine.toUpperCase().startsWith("QUIT"))                    
                            break;                                
                        else if(inputLine.toUpperCase().startsWith("DATA"))                    
                        {
                            w.write("354 Start mail input; end with <CRLF>.<CRLF>");
                            w.newLine();
                            w.flush();
                            inData=true;
                            data=new String();
                        }
                        else
                        {
                            w.write("250 Ok");
                            w.newLine();
                            w.flush();
                        }
                    } 
                }
            }
            
            
            w.write("221 closing transmission");
            w.newLine();
            w.flush();
        } 
        catch (IOException iOException) 
        {
        }

    }

    @Override
    public boolean reject(InetAddress inetaddress) {
        return false;
    }
    
}
