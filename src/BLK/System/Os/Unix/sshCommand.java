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

package BLK.System.Os.Unix;

import BLK.System.Logger;
import BLK.System.Os.Process;
import java.io.IOException;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class sshCommand{
    private String user;
    private String pass;
    private String command;
    private String host;

    private String input="";
    private String inpute="";

    private Process proc =null;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public sshCommand(String user, String pass, String host, String command) {
        this.user = user;
        this.pass = pass;
        this.host = host;
        this.command = command;
    }

    private String getCmd()
    {
        return "ssh "+this.user+"@"+this.host + " " + this.command;
    }


    public void lunch()
    {
        if (proc!=null)
            return;

        proc=new Process(getCmd());
        proc.setInteractive(true);
        proc.start();
        run();

    }

 
    
    private void run()
    {
        while (proc!=null)
        {
            try
            {
                if(proc.getInputStream().available()>0)
                {
                    byte[] b = new byte[proc.getInputStream().available()];
                    proc.getInputStream().read(b);
                    input = input.concat(new String(b));
                    System.out.print(new String(b));
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger().error(ex);
            }

            try
            {
                if(proc.getErrorStream().available()>0)
                {
                    byte[] b = new byte[proc.getErrorStream().available()];
                    proc.getErrorStream().read(b);
                    inpute = inpute.concat(new String(b));
                    System.err.print(new String(b));
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger().error(ex);
            }

            try
            {
                doMagic();
            }
            catch (IOException ex)
            {
                Logger.getLogger().error(ex);
            }


            if(proc.getExitValue()!=-1)
                proc=null;
            

        }
        if (proc!=null)
        {
            proc.Stop();
            proc=null;
        }
        
    }

    public void sendText(String text) throws IOException
    {
        if(proc!=null)
            proc.getOutputStream().write(text.getBytes());
    }

    private void doMagic() throws IOException {
        if (input.toLowerCase().split("contrase").length>1)
        {
            proc.getOutputStream().write(new String(this.pass+"\n").getBytes());
            proc.getOutputStream().flush();
        }
    }



}
