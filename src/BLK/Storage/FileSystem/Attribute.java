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

package BLK.Storage.FileSystem;

import BLK.Storage.IAttribute;
import BLK.io.FileSystem.Core.File;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Attribute extends File implements IAttribute
{

    private Attribute(Zone parent, String name)
    {
        super(parent, name.toUpperCase());
        if(!super.create())
            throw new UnsupportedOperationException("");// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES

    }
    public static IAttribute getAttribute(Zone parent, String name)
    {
        return new Attribute(parent, name);
    }

    @Override
    public String getString(String def)
    {
        return super.getString(def);
    }
    @Override
    public Boolean setString(String string)
    {
        return super.setString(string);
    }

    @Override
    public File getRaw()
    {
        return super.getRaw();
    }
    @Override
    public Boolean setRaw(File newRaw, Boolean deleteSource)
    {
        return super.setRaw(newRaw, deleteSource);
    }

    @Override
    public Boolean delete() {
        return super.delete();
    }


}
