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

package BLK.io.Network.Protocols.Application.Http;

import BLK.io.FileSystem.Core.File;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Url {
    public static File wGet(String url)
    {

        try
        {
            File ff=File.getTemp("jpg");



            InputStream tmp = (new URL(url)).openStream();
//            DataInputStream dis = new DataInputStream(tmp);

            Writer wr=ff.getWriter();

            //boolean  out=false;
            //while (true)
            //{
                BLK.System.Threads.Thread.sleepThread(5000);
                while (true)
                {
                    //out=false;
                    if (tmp.available()>0)
                    {
                    wr.write(tmp.read());
                    wr.flush();
                    }
                }
                

            //    if (out)
            //    {
                    //wr.close();
                    //return ff;
            //    }
            //    BLK.System.Jvm.JvmThreadsService.sleepThread(5000);
            //    out=true;
            //}

            
        }

        catch (MalformedURLException ex)
        {
            Logger.getLogger(Url.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }        catch (IOException ex)
        {
            Logger.getLogger(Url.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
