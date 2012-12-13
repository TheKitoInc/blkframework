/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.Network.Protocols.Transport.TCP.Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


/**
 *
 * @author andresrg
 */
public interface Events 
{
    public void process(Listener li, InputStream inputStream, OutputStream outputStream, InetAddress inetAddress);
    public boolean reject(InetAddress inetaddress);
}
