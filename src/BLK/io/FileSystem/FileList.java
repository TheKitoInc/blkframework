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

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class FileList extends ArrayList<String>
{
    File f;
    Long size=Long.MIN_VALUE;
    Boolean canRepeat;
    public FileList(Folder parent,String name)
    {
        super();
        this.f=new File(parent, name);
        this.canRepeat=Boolean.TRUE;
    }
    public FileList(String pathname)
    {
        super();
        this.f=new File(pathname);
        this.canRepeat=Boolean.TRUE;
    }
    private Boolean doCheck()
    {
        if(this.size!=this.f.getSize())
        {
            super.clear();
            for(String s : f.getArrayList())
                if(!super.add(s))
                    return false;
            
            this.size=this.f.getSize();
            return true;
        }
        else
            return true;
    }
    private Boolean save(){return this.f.setArray(this);}

    @Override
    public boolean add(String e)
    {
        if(this.canRepeat || !this.contains(e))
            return doCheck() && super.add(e) && this.save();
        else
            return true;
    }

    @Override
    public void add(int index, String element)
    {
        if(this.canRepeat || !this.contains(element))
        {
            doCheck();
            super.add(index, element);
            this.save();
        }
    }

    @Override
    public boolean addAll(Collection<? extends String> c)
    {
        return doCheck() && super.addAll(c) && this.save();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c)
    {
        return doCheck() && super.addAll(index, c) && this.save();
    }

    @Override
    public void clear()
    {
        doCheck();
        super.clear();
        this.save();
    }

    @Override
    public String remove(int index)
    {
        if(!doCheck())
            return null;

        String s=super.remove(index);

        if(this.save())
            return s;
        else
            return null;
    }

    //@Override
    public boolean remove(String o) {
        return doCheck() && super.remove(o) && this.save();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return doCheck() && super.removeAll(c) && this.save();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex)
    {
        doCheck()            ;
        super.removeRange(fromIndex, toIndex);
        this.save();
    }

    @Override
    public String set(int index, String element) 
    {
        if(!doCheck())
            return null;

        String s= super.set(index, element);
        
        if(this.save())
            return s;
        else
            return null;
    }


    public Boolean contains(String s)
    {
        for(String ss : this)
            if(ss.equalsIgnoreCase(s))
                return true;
        return false;
    }

    public Boolean getCanRepeat() {return canRepeat;}
    public void setCanRepeat(Boolean canRepeat) {this.canRepeat = canRepeat;}
}
