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

import BLK.io.FileSystem.File;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public interface IAttribute
{
    public String getString(String def);
    public Boolean setString(String string);

    public File getRaw();
    public Boolean setRaw(File newRaw, Boolean deleteSource);
    
    public Boolean delete();
    public String getName();
}
