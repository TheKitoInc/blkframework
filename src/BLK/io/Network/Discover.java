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

import BLK.io.FileSystem.Core.File;

import BLK.System.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Enumeration;
import BLK.System.Utils.Hashtable;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Discover implements Runnable, IDiscover{
    private DatagramSocket multicastSocket;
    private Hashtable<String,String> Nodes;
    private Thread th;
    private int port;
    private String ServerID;

    public String getComputerName(){return Common.getComputerName();}
    public Hashtable<String, String> getNodes(){return Nodes;}
    public boolean stop(){
        if(send("Bye"))
        {
            try{Thread.sleep(1000);}catch(Exception ex){}
            multicastSocket.disconnect();
            multicastSocket.close();
            return true;
        }
        else
            return false;
    }
    public boolean send (String data){
        try {
            this.multicastSocket.send(new DatagramPacket(data.getBytes(),data.length(),InetAddress.getByName("255.255.255.255"),this.port));
            BLK.System.Logger.getLogger().debug("SendOK: " + data);
            return true;
        } catch (Exception ex) {
            BLK.System.Logger.getLogger().error(ex.getMessage(), ex);
            return false;
        }
    }
    public void run(){

        this.send("Listen "+ServerID);
        this.send("Name "+getComputerName());

        String oDatos="";
        String oIP="";
        while(!multicastSocket.isClosed())
        {
            try {
                BLK.System.Logger.getLogger().debug("ListenMessage");

                byte[] buf = new byte[256];
                DatagramPacket message = new DatagramPacket(buf, buf.length);
                multicastSocket.receive(message);
                String Datos=(new String(buf)).trim();
                String IP=message.getAddress().getHostAddress().trim();

                if (!oDatos.equals(Datos) || !oIP.equals(IP))
                {
                    BLK.System.Logger.getLogger().info("Datos:"+Datos+"   "+"Host:"+IP);
                    this.data(Datos, IP);
                    this.updateFS();
                }
                else
                    BLK.System.Logger.getLogger().debug("PackageDiscarded   Datos:"+Datos+"   "+"Host:"+IP);

                oDatos=Datos;
                oIP=IP;
                BLK.System.Logger.getLogger().debug("EndListen");
            } catch (IOException ex) {
                if(!multicastSocket.isClosed())
                    BLK.System.Logger.getLogger().warn(ex.getMessage(), ex);
            }

        }

        System.gc();
    }

    public Discover(String ServerID) throws SocketException{
        this.port=40004;
        this.Nodes=new Hashtable<String,String>();
        this.ServerID=ServerID;
        this.multicastSocket=new DatagramSocket(this.port);
        this.th=new Thread(this);
        this.th.start();
    }
    private void data(String message,String IP){
        String params[]=message.trim().split(" ");
        BLK.System.Logger.getLogger().debug("Params: " + params.length);
        if (params.length>=1)
        {
            String IDServer="unknown";
            if((params[0].trim().equalsIgnoreCase("Listen") || params[0].trim().equalsIgnoreCase("Hello")) && params.length>1)
                IDServer=params[1].trim();

            if(params[0].trim().equalsIgnoreCase("Listen"))
            {
                this.send("Hello "+ServerID);
                this.send("Name "+getComputerName());

                if(!Nodes.containsKey(IP))
                    Nodes.put(IP, IDServer);

                this.send("OK");
            }
            else if(params[0].trim().equalsIgnoreCase("Bye"))
            {
                if(Nodes.containsKey(IP))                
                    Nodes.remove(IP);

                this.send("OK");
            }
            else if(params[0].trim().equalsIgnoreCase("Hello"))
            {
                if(!Nodes.containsKey(IP))
                    Nodes.put(IP, IDServer);

                this.send("OK");
            }
            else if(params[0].trim().equalsIgnoreCase("OK") || params[0].trim().equalsIgnoreCase("ERROR"))
            {
                BLK.System.Logger.getLogger().info("IP: "+IP+" "+params[0].trim());
            }
            else
            {
                this.send("ERROR");
            }

        }
    }
    private void updateFS(){
        java.io.File f=new java.io.File("/tmp/Servers/");
        f.mkdirs();
        Enumeration<String> e= Nodes.keys() ;
        while(e.hasMoreElements())
        {
            String IP=e.nextElement();
            String IDServer=Nodes.get(IP);            

                File ff = new File("/tmp/Servers/" + IP);
                ff.setString(IDServer);

            
        }
    }



    private static String ServerID_=null;
    public static String getServerID() {return ServerID_;}
    public static void setServerID(String ServerID__) {ServerID_ = ServerID__;}

    private static IDiscover obj;
    public static Hashtable<String,String> getServers() {return obj.getNodes();}

    private static boolean inited=false;

    public static boolean init()
    {
        if (inited)
            return false;

        try
        {
            if(ServerID_==null)
                obj = new Discover(new BLK.io.FileSystem.Core.File("/etc/ServerID").getString("BLK.UNKNOWN", true));
            else
                obj = new Discover(ServerID_);

            return true;
        }
        catch (SocketException ex)
        {
            Logger.getLogger().error(ex);
            return false;
        }
    }
    public static boolean end()
    {
        if (!inited)
            return false;

        if(obj.stop())
        {
            inited=false;
            return true;
        }
        else
            return false;
    }
}
