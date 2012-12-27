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

import BLK.io.FileSystem.Core.File;
import BLK.io.FileSystem.Core.Folder;
import BLK.System.Logger;
import BLK.System.Os.Process;
import java.io.IOException;
import java.util.regex.Matcher;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public abstract class FileSystem
{

    public static FileSystem getObject(String s)
    {
        java.io.File o = new java.io.File(s);

        if(!o.exists())
            throw new UnsupportedOperationException("Path not exists: "+s);
        else if(o.isFile())
            return new File(s);
        else if (o.isDirectory())
            return new Folder(s);

        throw new UnsupportedOperationException("Unknow path type: "+s);
    }

    private java.io.File object;
    protected java.io.File getObject(){return object;}

    public FileSystem(String pathname) {this.object=new java.io.File(pathname); }
    public FileSystem(Folder parent,String name) {this.object=new java.io.File(parent.getPath()+java.io.File.separator+name); }

    public Boolean exists(){return  this.object.exists();}
    public Boolean isWritable(){return this.object.canWrite();}
    public Boolean isReadable(){return this.object.canRead();}
    public String getName() {return this.object.getName();}
    public Folder getParent()
    {
        if(this.object.getParent()==null)
            return null;
        else
            return new Folder(this.object.getParent());
    }
    public String getPath() {return this.object.getAbsolutePath();}
    public String getHashPath()
    {
        String path=this.getPath();
        return String.valueOf(path.hashCode()) + String.valueOf(path.split(java.io.File.separator).length);
    }
    public String toString(){return this.getPath();}

    protected Boolean isFile() {return this.object.isFile();}
    protected Boolean isDirectory() {return this.object.isDirectory();}

    private String getCreateFileCommand(){return "touch  \"" + pathProcess(this.getPath()) + "\"";}
    private Boolean createFile()
    {
        Boolean f = false;
        try
        {
            f = this.object.createNewFile();
        }
        catch (IOException ex) {}

        if(f)
            return true;
        else if (Process.basicCall(this.getCreateFileCommand()))
            return true;
        else
            return false;
    }

    private String getCreateFolderCommand(){return "mkdir -vp \"" + pathProcess(this.getPath()) + "\"";}
    private  Boolean createFolder()
    {
        if(this.object.mkdirs())
            return true;
        else if (Process.basicCall(this.getCreateFolderCommand()))
            return true;
        else
            return false;
    }

    public String getCreateCommand()
    {
        if(this instanceof File)
            return this.getCreateFileCommand();
        else if (this instanceof Folder)
            return this.getCreateFolderCommand();
        else
            return null;
    }
    public boolean create()
    {
            if(this.exists())
                return true;
            else
            {
                if(this instanceof File)
                    return this.createFile();
                else if (this instanceof Folder)
                    return this.createFolder();
            }
            return false;
        }

    private String getDeleteFolderCommand(){return "rmdir -v \"" + pathProcess(this.getPath()) + "\"";}
    private String getDeleteFileCommand(){return "rm -fv \"" + pathProcess(this.getPath()) + "\"";}
    public String getDeleteCommand()
    {
        if (this instanceof File)
            return this.getDeleteFileCommand();
        else if (this instanceof Folder)
            return this.getDeleteFolderCommand();
        else
            return null;
    }

    public Boolean delete()
    {
        if(this instanceof Folder && (((Folder)this).getFiles(1).size()>0 || ((Folder)this).getFolders(1).size()>0))
            return false;
        //Logger.getLogger().info("DELETE "+this.getPath());
        if (!this.exists())
            return true;
        else if (this.object.delete())
            return true;
        else if (this instanceof File && Process.basicCall(this.getDeleteFileCommand()))
            return true;
        else if (this instanceof Folder && Process.basicCall(this.getDeleteFolderCommand()))
            return true;

        return false;
    }

    private String getCopyFileCommand(FileSystem destination){return "cp -v \"" + pathProcess(this.getPath()) + "\" \"" + pathProcess(destination.getPath()) + "\"";}
    private String getCopyFolderCommand(FileSystem destination){return "cp -rv \"" + pathProcess(this.getPath()) + "\" \"" + pathProcess(destination.getPath()) + "\"";}
    public String getCopyCommand(FileSystem destination)
    {
        if (this instanceof File)
            return this.getCopyFileCommand(destination);
        else if (this instanceof Folder)
            return this.getCopyFolderCommand(destination);
        else
            return null;
    }
    public boolean copy(FileSystem destination)
    {
        if(this.getPath().equals(destination.getPath()))
            return true;
        else if(destination.exists())
            return false;
        else if (!this.exists())
            return false;
        else if (this instanceof File && Process.basicCall(this.getCopyFileCommand(destination)))
            return true;
        else if (this instanceof Folder && Process.basicCall(this.getCopyFolderCommand(destination)))
            return true;
        else
            return false;
    }
    public String getMoveCommand(FileSystem destination){return "mv -v \"" + pathProcess(this.getPath()) + "\" \"" + pathProcess(destination.getPath()) + "\"";}
    public boolean move(FileSystem destination)
    {
        Logger.getLogger().info("MOVE "+this.getPath()+" > "+destination.getPath());
        if(this.getPath().equals(destination.getPath()))
            return true;
        else if (destination.exists())
            return false;
        else if (!this.exists())
            return false;
        else if (this.object.renameTo(destination.object))
            return true;
        else if (Process.basicCall(this.getMoveCommand(destination)))
            return true;
        else
            if(this.copy(destination))
                if(this.delete())
                    return true;
                else
                {
                    if (destination instanceof File)
                        destination.delete();

                    return false;
                }
            else
                return false;
    }

    protected static String randomPath(Folder parent, String preFix,String subFix)
    {
        String name=new String();
        if (preFix!=null)
            name=preFix;

        name+=BLK.System.Utils.Math.getUID();

        if (subFix!=null)
            name+=subFix;

        java.io.File o=new java.io.File(parent.getPath()+java.io.File.separator+name);

        if(o.exists())
            return randomPath(parent, preFix, subFix);

        return o.getAbsolutePath();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        else if (getClass() != obj.getClass())
            return false;

        final FileSystem other = (FileSystem) obj;
        if ((this.getPath() == null) ? (other.getPath() != null) : !this.getPath().equalsIgnoreCase(other.getPath()))
            return false;
 
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + (this.getPath() != null ? this.getPath().hashCode() : 0);
        return hash;
    }
    public Boolean setName(String newName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static String pathProcess(String path)
    {
        return path.replaceAll("\\$", Matcher.quoteReplacement("\\$"));
    }
}
