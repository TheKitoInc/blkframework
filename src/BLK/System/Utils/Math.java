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

import java.util.Random;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Math
{
    public static double round(double val, int dec)
    {
        System.out.println(1%2);
        java.lang.String coma=Float.toString(1/2);
        if (coma.split(",").length>1)
            coma=",";
        else if (coma.split(".").length>1)
            coma=".";


        java.lang.String n=java.lang.String.valueOf(val);

        java.lang.String p1=n.split(coma)[0];
        java.lang.String p2="";
        if (n.split(coma).length>1)
            p2=n.split(coma)[1];

System.out.println(p1);
System.out.println(coma);
System.out.println(p2);


        return Double.valueOf(p1+coma+p2);
    }
    public static long plusdiv(long a, long b)
    {

        if ((a%b) >= (b/2))
            return a/b + 1;
        else
            return a/b ;
    }
    private static Integer count=0;
    public static String getUID ()
    {
        if(count==Integer.MAX_VALUE)
            count=0;
        else
            count++;

        String n=String.valueOf(System.currentTimeMillis()).concat(String.valueOf(count));
        
        return Math.numToString(new Long(n));
    }
    public static String numToString(Integer num){return numToString(new Long(num.toString()));}
    public static String numToString(Long num)
    {
        String a[] = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        Long i=new Long(num);
        String f=new String();

        while(i>=a.length)
        {
            int resto=(int) (i % a.length);
            i=i-resto;
            i=i/a.length;

            f+=a[resto];
        }

        f+=a[i.intValue()];

        return f;
    }
    public static String stringToHashString(String v)
    {
        return Math.numToString(java.lang.Math.abs(v.hashCode()));
    }

    public static java.lang.String numToString(int num,int stringLong){return numToString(new Long(java.lang.String.valueOf(num)), stringLong);}
    public static java.lang.String numToString(long num,int stringLong)
    {
        java.lang.String n=java.lang.String.valueOf(num);

        while (n.length()<stringLong)
            n="0"+n;

        return n;


    }
}
