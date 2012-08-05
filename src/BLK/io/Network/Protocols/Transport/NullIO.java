/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BLK.io.Network.Protocols.Transport;

import BLK.System.Logger;
import BLK.io.Network.Protocols.Transport.TCP.Connection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 *
 * @author andresrg
 */
public class NullIO extends ConnectionIO
{

    private static NullIO nio=new NullIO();

    
    public void main(Connection connection, InputStream is, OutputStream os)
    {
        BufferedReader f=new BufferedReader(new InputStreamReader(is));
        
        Logger.getLogger().info("New NullIO: "+connection.getName());
    }

    public static NullIO getNio() {
        return nio;
    }

    


}
