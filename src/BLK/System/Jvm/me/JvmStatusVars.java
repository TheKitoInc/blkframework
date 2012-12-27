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

package BLK.System.Jvm.me;

import BLK.System.*;
import BLK.System.Utils.Hashtable;
import BLK.io.FileSystem.Core.File;
import BLK.io.FileSystem.Core.Folder;




/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class JvmStatusVars {
    private String instanceName="unknow";
    private Hashtable<String,String> vars= new Hashtable<String, String>();
    private File tmp = null;

    private boolean updateFile() {return this.tmp.setHashtable(this.vars);}
    public String getInstanceName() {return instanceName;}
    public Hashtable<String, String> getVars() {return vars;}
    public JvmStatusVars(String instanceName) {setInstanceName(instanceName);}
    public void setInstanceName(String instanceName)
    {
        this.instanceName = instanceName;
        tmp = new File(Folder.temp,instanceName+".vars");
    }
    public void set (String name, String value)
    {
        try {
            if (this.vars.get(this.instanceName+"|"+name) != null && this.vars.get(this.instanceName+"|"+name).equalsIgnoreCase(value)) {
                return;


            }
            this.vars.remove(this.instanceName+"|"+name);
            this.vars.put(this.instanceName+"|"+name, value);
            updateFile();
        } catch (Exception e) {Logger.getLogger().error(e);
        }

    }
}
