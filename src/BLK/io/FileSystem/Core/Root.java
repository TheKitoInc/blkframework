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

import java.util.ArrayList;



/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Root extends Folder
{
    public  static ArrayList<Root> getRoots()
    {
         ArrayList<Root> lst = new  ArrayList<Root>();
         
         for (java.io.File path : java.io.File.listRoots())
             lst.add (new Root(path.getAbsolutePath()));


         if (System.getProperty("os.name").toLowerCase().split("win").length==1)
         {             
             String mount=BLK.System.Os.Process.basicCallReader("mount");

             mount=mount.replaceAll("\r", "\n");

             for (String line : mount.split(("\n")))
                 if (!line.isEmpty())
                     for (String part : line.split(" "))
                         if(part.split("/",2).length>1)
                             if((new java.io.File(part)).isDirectory())
                             {
                                 Root r = new Root(part);
                                 if(!lst.contains(r))
                                    lst.add (r);
                             }
         }
         
         return lst;
    }

    private Scanner scanner;

    public Root(String pathname)
    {
        super(pathname);
        this.scanner=new Scanner(this, 10, true, true);        
    }

    public Scanner getScanner() {return scanner;}
    
    @Override
    public long getFreeSize() {return super.getFreeSize();}
    @Override
    public long getTotalSize() {return super.getTotalSize();}
    @Override
    public long getUsedSize() {return super.getUsedSize();}

}
