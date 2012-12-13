/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.Network.Protocols.Transport.UDP.Server;

import java.net.InetAddress;

/**
 *
 * @author andresrg
 */
public interface Events 
{
    public void process(Listener listener, byte[] data, int remoteport, InetAddress inetaddress);    
}
