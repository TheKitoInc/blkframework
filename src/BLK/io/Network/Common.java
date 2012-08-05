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

package BLK.io.Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import BLK.System.Utils.ArrayList;
import BLK.System.Utils.Hashtable;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public abstract class Common {
    public static String getComputerName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException ex)
        {
            BLK.System.Logger.getLogger().warn(ex);
            return "unknown";
        }
    }
    public static boolean send (String data,DatagramSocket ds){
        try {
            ds.send(new DatagramPacket(data.getBytes(),data.length(),InetAddress.getByName("255.255.255.255"),ds.getLocalPort()));
            BLK.System.Logger.getLogger().debug("SendOK: " + data);
            return true;
        } catch (Exception ex) {
            BLK.System.Logger.getLogger().error(ex.getMessage(), ex);
            return false;
        }
    }
    public static boolean ping(InetAddress ip,int timeout)
    {
        try
        {
            BLK.System.Logger.getLogger().debug("Ping: " + ip.getHostAddress().toString());
            return ip.isReachable(timeout);

        }
        catch (IOException ex)
        {
            BLK.System.Logger.getLogger().warn(ex);
            return false;
        }
    }
    public static Hashtable<InetAddress,Boolean> scanNet(ArrayList<InetAddress> li)
    {
        Hashtable<InetAddress,Boolean> ht=new Hashtable<InetAddress,Boolean>();

        for (InetAddress ia : li) 
            ht.put(ia, ping(ia,1000));
        
        return ht;
    }
}
