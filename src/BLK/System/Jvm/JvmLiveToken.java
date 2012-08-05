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
package BLK.System.Jvm;

import BLK.io.FileSystem.File;
import BLK.System.Logger;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class JvmLiveToken extends Thread{
    private static JvmLiveToken th=null;
    private String appPath=null;
    private String fpid=null;
    private JvmLiveToken(String[] mainParams) {
        for (String p:mainParams)
            if(p.toLowerCase().startsWith("FSSP"))
            {
                String[] dp=p.split(":");
                if (dp.length>0)
                    fpid=dp[0];

                if (dp.length>1)
                    appPath=dp[1];

            }
    }

    public static boolean startLiveToken(String[] mainParams)
    {
        if (th==null)
        {
            th=new JvmLiveToken(mainParams);
            th.start();
            return true;
        }
        else
            return false;
    }

    @Override
    public void run()
    {
        try
        {
            File ff = new File("/tmp/" + this.fpid + ".FSSP");
            while (true) {
                ff.setString(this.appPath);
                BLK.System.Threads.Thread.sleepThread(5000);
            }
        }
        catch (Exception ex)
        {
            th=null;
            Logger.getLogger().error(ex);
        }
    }
}