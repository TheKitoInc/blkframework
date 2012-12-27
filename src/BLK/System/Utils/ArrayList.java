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

package BLK.System.Utils;


import BLK.io.FileSystem.Core.File;
import java.util.Collection;
import java.util.Random;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class ArrayList<E> extends java.util.ArrayList<E>
{

    private File storage=null;
    
    public ArrayList(){super();}
//    public ArrayList(File storage)
//    {
//        super();
//        this.storage=storage;
//        for(String s : storage.getArrayList())
//            super.add((E) s);
//    }

    private Boolean updateFile()
    {
//        if(this.storage==null)
            return true;
//        else
//            return this.storage.setArray(this);
    }

    @Override
    public boolean add(E e)
    {
        return super.add(e) && updateFile();
    }

    @Override
    public void clear() {
        super.clear();
         updateFile();
    }

    @Override
    public E remove(int index) {
         E a=super.remove(index);
         updateFile();
         return a;
    }

    @Override
    public boolean remove(Object o) {
        return super.remove(o)  && updateFile();
    }












    @Override
    public boolean addAll(Collection<? extends E> c) {
        return super.addAll(c) && updateFile();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return super.addAll(index, c) && updateFile();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return super.removeAll(c) && updateFile();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        updateFile();
    }

    @Override
    public E set(int index, E element)
    {
        E a =super.set(index, element);
        updateFile();
        return a;
    }

    public E getRandomElement()
    {
        int size = this.size();
        int item = new Random().nextInt(size);
        return this.remove(item);
    }


















    public ArrayList(int initialCapacity){super(initialCapacity);}
    public ArrayList(Collection<? extends E> c){super(c);}
    
    public static ArrayList getDif(ArrayList a, ArrayList b){return getDif(a, b, true);}
    public static ArrayList getDif(ArrayList a, ArrayList b, boolean bidirect){return a.getDif(b, bidirect);}
    public ArrayList<E>  getDif(ArrayList<E>  b, boolean bidirect)
    {
        ArrayList<E> tmp=new ArrayList<E> ();

        for (E o : this)
            if (!b.isIn(o))
                tmp.add(o);

        if (bidirect)
            for (E o : b)
                if (!this.isIn(o))
                    tmp.add(o);

        return tmp;
    }

    public static ArrayList getEqual(ArrayList a, ArrayList b){return a.getEqual(b);}
    public ArrayList<E> getEqual(ArrayList<E> b)
    {
        ArrayList<E> tmp=new ArrayList<E>();

        for (E o : this)
            if (b.isIn(o) && !tmp.isIn(o))
                tmp.add(o);

        return tmp;
    }




    public static boolean isIn (ArrayList a, Object o){return a.isIn(o);}
    public boolean isIn (Object o)
    {
        for (Object oo : this)
            if (oo.equals(o))
                return true;

        return false;
    }


    public static ArrayList copy(ArrayList lst){return copy(lst,false);}
    public static ArrayList copy(ArrayList lst,boolean invert){return lst.copy(invert);}

    public ArrayList<E> copy(){return copy(false);}
    public ArrayList<E> copy(boolean invert)
    {
        ArrayList<E> tmp = new ArrayList<E>();

        if(invert)
            for (int i = this.size()-1; i>=0; i--)
                tmp.add(this.get(i));
        else
            for(E o : this)
                tmp.add(o);
        
        return tmp;
    }
}
