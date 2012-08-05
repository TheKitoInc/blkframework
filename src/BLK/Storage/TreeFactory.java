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

package BLK.Storage;

import BLK.io.FileSystem.Folder;
import BLK.io.Network.Protocols.Application.Sql.Connection;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public abstract class TreeFactory
{
    private TreeFactory() {}

    public static IZone getTree(Folder root)
    {
        return BLK.Storage.FileSystem.Zone.getZone(root.getPath());
    }
    
    public static IZone getTree(Connection root)
    {
        return BLK.Storage.Sql.Zone.getZone(root);
    }

    public static Boolean sendMessage (IZone appZone,String message)
    {
        String time = String.valueOf(System.currentTimeMillis()/1000);

        IZone z = appZone;

        z=z.getSubZone("Messages");

        for(byte b : time.getBytes())
            z=z.getSubZone(String.valueOf(b));

        return z.getAttribute(time).setString(message);
    }
    
}
