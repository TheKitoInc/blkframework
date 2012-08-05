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
package BLK.io.Network;

import BLK.System.Utils.Hashtable;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public interface IDiscover {
    public boolean stop();
    public boolean send (String data);
    public Hashtable<String, String> getNodes();
    public String getComputerName();
}
