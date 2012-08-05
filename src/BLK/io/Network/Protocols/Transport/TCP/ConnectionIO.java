/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BLK.io.Network.Protocols.Transport.TCP;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author andresrg
 */
public interface ConnectionIO
{

    public void main(Connection connection, InputStream is, OutputStream os);

}
