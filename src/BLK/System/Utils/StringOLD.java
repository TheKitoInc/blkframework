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

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class StringOLD {

    java.lang.String obj=null;
    public StringOLD(java.lang.String str) {this.obj=str;}

    public StringOLD() {this.obj=new java.lang.String();}

    public ArrayList<java.lang.String> getPaths()
    {
        java.lang.String[] subs=this.obj.replaceAll("\n"," ").replaceAll("\r", " ").split(" ");
        ArrayList<java.lang.String> paths=new ArrayList<java.lang.String>();
        for (java.lang.String sub : subs)
            if (sub.split("/").length>1 || sub.split("\\\\").length>1 || (sub.length()==1 && ( sub.equals("/") || sub.equals("\\") )))
                paths.add(sub.trim());

        return paths;
    }
    public Hashtable getParamsURI()
    {

        Hashtable<java.lang.String,java.lang.String> tmp =new Hashtable<java.lang.String,java.lang.String>();
        java.lang.String[] tmp2;

        java.lang.String uri=new java.lang.String(obj);
        uri=uri.replaceAll("\\", "/");
        
        tmp2=uri.split("://");
        if(tmp2.length>1)
        {
            tmp.put("protocol", tmp2[0]);
            uri=uri.substring(tmp2[0].length()+3);
        }

        tmp2=uri.split("/");
        uri=tmp2[0];

        if(tmp2.length>1)
            tmp.put("url", tmp2[1]);
            


        return tmp;
    }

    public static java.lang.String numToString(int num,int stringLong){return numToString(new Long(java.lang.String.valueOf(num)), stringLong);}
    public static java.lang.String numToString(long num,int stringLong)
    {
        java.lang.String n=java.lang.String.valueOf(num);

        while (n.length()<stringLong)
            n="0"+n;

        return n;


    }



    public static java.lang.String lineSeparator()
    {
        return System.getProperty("line.separator");
    }
    public static StringOLD getUID()
    {
        return new StringOLD(java.util.UUID.randomUUID().toString());
    }
}
