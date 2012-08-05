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


import java.util.Enumeration;
import java.util.Map;
import java.lang.String;


/**a
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Hashtable<K,V> extends java.util.Hashtable<K,V>
{

    public Hashtable(Map t) {
        super(t);
    }

    public Hashtable() {super();
    }


    @Override
    public String toString()
    {
        Enumeration e = this.keys();
        String out=new String();

        while( e.hasMoreElements() )
        {
            Object k=e.nextElement();

            if(this.get((K)k)!=null)
                out+=k.toString()+"="+this.get((K)k).toString();
            else
                out+=k.toString()+"=NULL";

            out+=k.toString()+"\n";
        }

        return out;
    }


}
