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

package BLK.io.FileSystem.Core;

import BLK.System.Utils.ArrayList;
import BLK.io.FileSystem.FileSystem;
import BLK.io.FileSystem.Filter;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Folder extends FileSystem {

    public static final Folder root=new Folder();
    public static final Folder temp=new Folder(Folder.root,"tmp");

    public Folder(String pathname)
    {
        super(pathname);

         if (this.exists() && !this.isDirectory())
            throw new UnsupportedOperationException("Invalid Directory object: "+super.getPath());
    }
    public Folder(Folder parent,String name)
    {
        super(parent,name);

         if (this.exists() && !this.isDirectory())
            throw new UnsupportedOperationException("Invalid Directory object: "+super.getPath());
    }
    public Folder()
    {
        super("/");
        if (!this.exists())
            this.create();
    }

    public String[] getFilesName(Integer limit,String preFix,String subFix)
    {
        Filter f = Filter.getFileFilter(limit, preFix, subFix);
        return super.getObject().list(f);
    }
    public ArrayList<File> getFiles(Integer limit,String preFix,String subFix)
    {
        ArrayList<File> ff = new ArrayList<File>();
      
        String s[]=this.getFilesName(limit, preFix, subFix);
        if(s!=null)
            for (String o :s)
                ff.add(new File(super.getPath() + java.io.File.separatorChar + o));

        return ff;
    }
    public ArrayList<File> getFiles(Integer limit,String exten){return getFiles(limit, null,"." + exten);}
    public ArrayList<File> getFiles(String exten){return getFiles(-1, null,"." + exten);}
    public ArrayList<File> getFiles(Integer limit){return getFiles(limit, null,null);}
    public ArrayList<File> getFiles(){return getFiles(-1, null,null);}

    public String[] getFoldersName(Integer limit,String preFix,String subFix)
    {
        Filter f= Filter.getFolderFilter(limit, preFix, subFix);
        return super.getObject().list(f);
    }
    public ArrayList<Folder> getFolders(Integer limit,String preFix,String subFix)
    {
        ArrayList<Folder> fo = new ArrayList<Folder>();
        
        String s[]=this.getFoldersName(limit, preFix, subFix);
        if(s!=null)
            for (String o :s)
                fo.add(new Folder(super.getPath() + java.io.File.separatorChar + o));
        
        return fo;
    }
    public ArrayList<Folder> getFolders(Integer limit){return getFolders(limit, null, null);}
    public ArrayList<Folder> getFolders(){return getFolders(-1, null, null);}
    public ArrayList<Folder> getFolders(String preFix,String subFix){return getFolders(-1, preFix, subFix);}

    public static Folder getTemp(Folder parent,String preFix){return new Folder(FileSystem.randomPath(parent, preFix, null)); }
    public static Folder getTemp(String preFix){return new Folder(FileSystem.randomPath(Folder.temp, preFix, null)); }
    public static Folder getTemp(){return new Folder(FileSystem.randomPath(Folder.temp, null, null)); }

    public long getFreeSize() {return this.getObject().getFreeSpace();}
    public long getTotalSize() {return this.getObject().getTotalSpace();}
    public long getUsedSize() {return getTotalSize() - getFreeSize();}
}

