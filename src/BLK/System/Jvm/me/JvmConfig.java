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

import BLK.System.Jvm.*;
import BLK.System.Utils.ArrayList;
import BLK.System.Utils.Hashtable;
import BLK.io.FileSystem.Core.File;
import java.util.Enumeration;







/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class JvmConfig {
    private File config ;
    private Hashtable<String,String> params;
    
    public JvmConfig(String[] args) throws JvmException {
        String configPath="/etc/blk/default.conf";


        if (args.length>0 && (new java.io.File(args[0]).canRead()))
            configPath=args[0];
        
            this.config = new File(configPath);
            this.params=this.config.getHashtable();
    
    }

    public boolean setParam(String name, String value)
    {
        if (this.params.contains(name))
            this.params.remove(name);

        this.params.put(name, value);
        return config.setHashtable(this.params);
    }

    public String getParam(String name,String def)
    {
        if (this.params.containsKey(name))
            return this.params.get(name);
        else
        {
            setParam(name, def);
            return def;
        }
    }

    public void getParams()
    {
        ArrayList<String> keys= new ArrayList<String>();
        Enumeration<String> e= this.params.keys();
        while (e.hasMoreElements()){keys.add(e.nextElement());}
    }
}

