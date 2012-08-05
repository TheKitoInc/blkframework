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


import BLK.io.Network.Protocols.Transport.TCP.TcpException;
import java.net.Socket;
import BLK.System.Utils.Hashtable;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public interface ISocketServer {

    //Client connections
    public boolean acceptNewClient(Socket client,Hashtable params);
    public void clientClosed(Socket client, Hashtable params);
    public boolean closeConnection(Socket client, Hashtable params);
    public void closeError(Socket client, Hashtable params);
    //cnn data in
    public void dataArrival(byte[] tmp, Hashtable params);
    public int dataAvailable(int available, Hashtable params);
//    public String lineArrival(String buf,Hashtable params);
    //cnn data out
    public byte[] dataToSend(Hashtable params);
    public void sendDataError(TcpException ex,byte[] data, Hashtable params);
    public void bytesSend(int length, Hashtable params);
    //bind control
    public void listening(int port);
    //public void startListenError(TcpException ex);
    public boolean noMoreConnections();
    public void listenStop();

    public String lineArrival(String buf, Hashtable<String, String> clientParams, Socket client);
}
