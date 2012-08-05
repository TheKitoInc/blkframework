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

package BLK.System.Os.Unix;

import java.io.File;
import BLK.System.Utils.ArrayList;
import BLK.System.Logger;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 * 
 */
public abstract class UnixDEV
{
    
    private static ArrayList<String> oldDevicesList=new ArrayList<String>();

    public static ArrayList<String> getDevicesList()
    {
        ArrayList<String> tmp=new ArrayList<String>();

        rastr(new File("/dev/"), tmp);

        return tmp;
    }
    private static void rastr(File base, ArrayList<String> tmp)
    {
        if (base.getAbsolutePath().equalsIgnoreCase("/dev/fd"))
            return;

        for (String l : base.list())
        {
            File f=new File(base.getAbsolutePath() + "/" + l);
            if (f.isDirectory())
                rastr(f,tmp);
            else
                tmp.add(f.getAbsolutePath());
        }

    }




    public static void loop()
    {
        
            try
            {
                ArrayList<String> lst = getDevicesList();

                compareDevices(lst);

                oldDevicesList=lst;
            }
            catch (Exception ex)
            {
                Logger.getLogger().warn(ex);
                BLK.System.Threads.Thread.sleepThread(5000);
            }
            

        
    }

    private static  void compareDevices(ArrayList<String> lst)
    {
        ArrayList<String> a;
        ArrayList<String> b;

        if (oldDevicesList.size()>lst.size())
        {
            a=oldDevicesList;
            b=lst;
        }
        else
        {
            b=oldDevicesList;
            a=lst;
        }

        for (Object o : ArrayList.getDif(a, b, true))
        {
            if (ArrayList.isIn(lst, o))
                newDevice(o.toString());
            else
                oldDevice(o.toString());
        }

    }

    private static void newDevice(String d) {
       /// System.out.println("nuevo:"+d);
    }

    private static void oldDevice(String d) {
        System.out.println("viejo:"+d);
    }

}
