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

package BLK.System.Os;

import BLK.System.Logger;
import BLK.System.Threads.Thread;
import BLK.io.Terminal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;




/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Process extends Thread
{
    private String command;
    private Integer exitValue=null;

    private java.lang.Process process = null;
    private java.io.InputStream is = null;
    private java.io.InputStream es = null;
    private java.io.OutputStream os = null;
            
    private Boolean interactive=true;

    public Process(String command)
    {
        super("Process: "+command.split(" ",2)[0]);
        this.command=command;
    }

    public String getCommand() {return command;}
    public InputStream getErrorStream() {return es;}
    public InputStream getInputStream() {return is;}
    public OutputStream getOutputStream() {return os;}

    public Boolean getInteractive() {return interactive;}
    public void setInteractive(Boolean interactive) {this.interactive = interactive;}
        
    @Override
    protected void doThread()
    {
        try
        {
            if(this.process!=null)
                this.process.destroy();

            this.process=null;

            this.is=null;
            this.os=null;
            this.es=null;

            this.process=Runtime.getRuntime().exec(this.command);

            this.is=this.process.getInputStream();
            this.es=this.process.getErrorStream();
            this.os=this.process.getOutputStream();

            if(this.interactive)
                this.waitForInteractive();
            else
                this.exitValue=this.process.waitFor();

        }
        catch (IOException ex)
        {         
            this.exitValue=Integer.MIN_VALUE;            
            Logger.getLogger().error(ex);
            if(this.process!=null)
                this.process.destroy();
        }
        catch (InterruptedException ex)
        {            
            this.exitValue=Integer.MIN_VALUE+1;
            Logger.getLogger().warn(ex);
            if(this.process!=null)
                    this.process.destroy();
        }

//        this.is=null;
//        this.os=null;
//        this.es=null;

        System.gc();
    }

    public Integer getExitValue()
    {
        if(this.exitValue==null)
            try
            {
                this.exitValue=this.process.exitValue();
                this.process=null;
            }
            catch(Exception ex)
            {}

        return this.exitValue;
    }
    
    @Override
    public void start()
    {
        super.start();
        while(this.process==null){Thread.sleepThread(1);}
    }

    public Boolean kill()
    {
        if(this.process!=null)
        {
            this.process.destroy();
            return true;
        }
        else
            return false;
    }
        
    private void waitForInteractive()
    {
        BufferedReader bris=null;       
        BufferedReader bres=null;
        
        if(this.is!=null)
            bris=new BufferedReader(new InputStreamReader(this.is));
        
        if(this.es!=null)
            bres=new BufferedReader(new InputStreamReader(this.es));
        
        while (this.getExitValue()==null && this.process!=null)
        {
            if(this.is!=null && bris!=null)
            {
                try 
                {
                    String sbris=bris.readLine();
                    if(sbris!=null)
                        Terminal.getDefault().getStdout().println(sbris);
                }
                catch (IOException ex) 
                {}
            }
            
            if(this.es!=null && bres!=null)
            {
                try 
                {
                    String sbres=bres.readLine();
                    if(sbres!=null)
                        Terminal.getDefault().getStderr().println(sbres);
                }
                catch (IOException ex) 
                {}
            }
            
            Thread.sleepThread(500);
            
        }
    }    
    
    public static Boolean basicCall(String command)
    {
        Process p = new Process(command);

        p.start();

        while (p.getExitValue()==null){Thread.sleepThread(100);}

        return p.getExitValue()==0;
    }
    
    public static String basicCallReader(String command)
    {
        if (command==null)
            return null;

        Logger.getLogger().debug("Basic Call Reader: " + command);

        Process p=new Process(command);
        p.setInteractive(false);
        p.start();
        
        
        BufferedReader bres=null;
        
        if(p.getErrorStream()!=null)
            bres=new BufferedReader(new InputStreamReader(p.getErrorStream()));
                
        while (p.getExitValue()==null)
        {
            if(p.getErrorStream()!=null && bres!=null)
            {
                try 
                {
                    String sbres=bres.readLine();
                    if(sbres!=null)
                        Terminal.getDefault().getStderr().println(sbres);
                }
                catch (IOException ex) 
                {}
            }
        }
        
        Logger.getLogger().debug("Basic Call Reader Res: " + String.valueOf(p.getExitValue()));

       
        if (p.getExitValue()==0)
        {
            try
            {
                byte[] b = new byte[p.getInputStream().available()];
                p.getInputStream().read(b);
                System.gc();
                return new String(b);
            }
            catch (IOException ex)
            {
                Logger.getLogger().error(ex);
                return null;
            }
                
          }
          else
                return null;

    }


    @Deprecated
    public InputStream getError() {return this.getErrorStream();}
    @Deprecated
    public InputStream getOutput() {return this.getInputStream(); }
    @Deprecated
    public Boolean Stop() {return this.kill();}
    @Deprecated
    public OutputStream getInput() {return this.getOutputStream();}

}
