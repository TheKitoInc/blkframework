/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BLK.io.Network.Protocols.Transport;

import BLK.io.Network.Protocols.Transport.TCP.Connection;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author andresrg
 */
public abstract class ConnectionIO
{

    Connection connection;
    InputStream is;
    OutputStream os;

    public final void configure(Connection  connection, InputStream is, OutputStream os) {
        this.connection = connection;
        this.is = is;
        this.os = os;
        this.ready();
    }

    private void ready() {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    
}
