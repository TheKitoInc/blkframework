/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.Network.Protocols.Transport.TCP.Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 *
 * @author andresrg
 */
public interface IClient 
{
    public void newConnection(InputStream inputStream, OutputStream outputStream, InetAddress inetAddress);
}
