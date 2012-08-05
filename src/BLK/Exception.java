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

package BLK;

import BLK.System.Logger;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Exception extends java.lang.Exception{

    public Exception(String message)
    {
        super(message);
        if(Thread.activeCount()==1)
            Logger.getLogger().fatal(this);
        else
            if (Thread.currentThread().getName().equalsIgnoreCase("main"))
                Logger.getLogger().error(this);
            else
                Logger.getLogger().warn(this);
    }

}
