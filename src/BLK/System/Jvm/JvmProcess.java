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

package BLK.System.Jvm;

import BLK.System.Os.Process;
import BLK.System.Utils.ArrayList;
import BLK.io.FileSystem.Core.File;


/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class JvmProcess extends Process{

    private File jarLocation=null;
    private File binLocation=null;
    private ArrayList<String> jvmOptions=new ArrayList<String>();
    private ArrayList<String> appOptions=new ArrayList<String>();
    
    public ArrayList<String> getJvmOptions(){return jvmOptions;}
    public void setJvmOptions(ArrayList<String> jvmOptions){this.jvmOptions = jvmOptions;}
    public ArrayList<String> getAppOptions(){return appOptions;}
    public void setAppOptions(ArrayList<String> appOptions){this.appOptions = appOptions;}
    public File getBinLocation(){return binLocation;}
    public final void setBinLocation(File binLocation){this.binLocation = binLocation;}
    public File getJarLocation(){return jarLocation;}
    public final void setJarLocation(File jarLocation) throws JvmException{
        if(jarLocation.exists() && jarLocation.isReadable())
            this.jarLocation = jarLocation;
        else
            throw new JvmException("can not read jar file: " + jarLocation.getPath());
    }
    
    private JvmProcess() 
    {
        super("java");
        setBinLocation(new File("java"));
    }

    public JvmProcess(File jarPath) throws JvmException
    {
        this();
        setJarLocation(jarPath);
    }

    
    public String getExecCommand()
    {
        String jvmo="";
        for (String t:this.jvmOptions)
            jvmo+="-"+t;

        String appo="";
        for (String t:this.appOptions)
            appo+=t+" ";
        
        return this.binLocation+" "+jvmo+" -jar "+this.jarLocation+" "+appo+"JPID:"+System.currentTimeMillis();
    }


}
