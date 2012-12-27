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

package BLK.Storage.FileSystem;

import BLK.Storage.IAttribute;
import BLK.Storage.IZone;
import BLK.io.FileSystem.Core.Folder;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Zone extends Folder implements IZone
{
    //private IZone parent=null;

    private Zone(Zone parent, String name)
    {
        super(parent, name.toLowerCase());
        //this.parent=parent;
        if(!super.create())
            throw new UnsupportedOperationException("");// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
    }
    private Zone(String pathname)
    {
        super(pathname);
        if(!super.create())
            throw new UnsupportedOperationException("");// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
    }

    public static IZone getZone(Zone parent, String name)
    {
        return new Zone(parent, name);
    }
    public static IZone getZone(String pathname)
    {
        return new Zone(pathname);
    }


    @Override
    public IZone getSubZone(String name)
    {
        return getZone(this, name);
    }

    
    @Override
    public IZone getParentZone()
    {

        Folder f = this.getParent();
        if(f!=null)
            return getZone(f.getPath());
        else
            return null;

//        if(this.parent==null && f!=null)
//            this.parent=getZone(f.getPath());

//        return this.parent;
    }

    @Override
    public String[] getSubZones()
    {
        return this.getFoldersName(-1, null, null);
    }

    @Override
    public Boolean delete()
    {
        for(String n : this.getSubZones())
            if(!this.getSubZone(n).delete())
                return false;

        for(String n : this.getAttributes())
            if(!this.getAttribute(n).delete())
                return false;

        return super.delete();
    }

    @Override
    public String[] getAttributes()
    {
        return this.getFilesName(-1, null, null);
    }

    @Override
    public IAttribute getAttribute(String name)
    {
        return Attribute.getAttribute(this, name);
    }

    @Override
    public Boolean changeParent(IZone newParent)
    {
        return super.move(new Folder((Zone)newParent, this.getName()));
    }



}
