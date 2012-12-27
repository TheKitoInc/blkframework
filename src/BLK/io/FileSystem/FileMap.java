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

package BLK.io.FileSystem;

import BLK.io.FileSystem.Core.Folder;
import BLK.System.Utils.Pair;
import java.util.ArrayList;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class FileMap
{

    FileList fl;
    public FileMap(String pathname) {fl=new FileList(pathname);fl.setCanRepeat(false);}
    public FileMap(Folder parent, String name) {fl=new FileList(parent, name);fl.setCanRepeat(false);}

    public void clear() {fl.clear();}

    public boolean add(String key,String value){return fl.add((new Pair(key, value)).toString());}
    public boolean remove(String key,String value) {return fl.remove((new Pair(key, value)).toString());}

    public boolean remove(String key) {return fl.remove((new Pair(key, getValue(key))).toString());}
    
    public ArrayList<String> getKeys()
    {
        ArrayList<String> s=new ArrayList<String>();
        for (String line : fl)
        {
            Pair p =new Pair(line);
            s.add(p.getName());
        }
        return s;
    }
    public ArrayList<String> getValues()
    {
        ArrayList<String> s=new ArrayList<String>();
        for (String line : fl)
        {
            Pair p =new Pair(line);
            s.add(p.getString());
        }
        return s;
    }
    public String getKey(String value)
    {
        for (String line : fl)
        {
            Pair p =new Pair(line);
            if(value.equalsIgnoreCase(p.getString()))
                return p.getName();
        }
        return null;
    }
    public String getValue(String key)
    {

        for (String line : fl)
        {
            Pair p =new Pair(line);
            if(key.equalsIgnoreCase(p.getName()))
                return p.getString();
        }
        return null;
    }

    public Boolean containsKey(String key)
    {
        for (String line : fl)
        {
            Pair p =new Pair(line);
            if(key.equalsIgnoreCase(p.getName()))
                return true;
        }
        return false;
    }
}
