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

package BLK.Storage.Sql;

import BLK.Storage.IAttribute;
import BLK.Storage.Sql.Persistence.AttributeName;
import BLK.Storage.Sql.Persistence.AttributePair;
import BLK.Storage.Sql.Persistence.AttributeValue;
import BLK.Storage.Sql.Persistence.ZoneAttribute;
import BLK.io.FileSystem.Core.File;
import java.util.ArrayList;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Attribute implements IAttribute
{


    AttributeName an = null;
    ZoneAttribute za = null;
    Zone z = null;
    private Attribute(Zone parent, String name)
    {
        this.an=AttributeName.getName(name, parent.getCnn());
        this.z=parent;
    }
    public static IAttribute getAttribute(Zone parent, String name)
    {
        return new Attribute(parent, name);
    }

    public AttributeName getAttributeName()
    {
        return this.an;
    }
    @Override
    public String getName()
    {
        return this.getAttributeName().getValue();
    }
    
    @Override
    public Boolean setString(String string)
    {
        if(this.getZoneAttribute()!=null && this.getZoneAttribute().getAttributePair().getAttributeValue().getValue().equalsIgnoreCase(string))
            return true;

        AttributeValue av = AttributeValue.getValue(string, an.getCnn());
        AttributePair ap = AttributePair.getAttributePair(this.an, av);

        if(this.getZoneAttribute()!=null)
            this.getZoneAttribute().delete();

        ZoneAttribute za = ZoneAttribute.getZoneAttribute(this.getZone(),ap);

        if(!za.getAttributePair().getAttributeValue().getValue().equalsIgnoreCase(string))
            return false;

        this.za=za;

        return true;
    }
    @Override
    public String getString(String def)
    {
        if(this.getZoneAttribute()==null)
        {
            this.setString(def);
            return def;
        }

        return this.getZoneAttribute().getAttributePair().getAttributeValue().getValue();
    }

    @Override
    public File getRaw()
    {
        throw new UnsupportedOperationException("Not supported.");
    }
    @Override
    public Boolean setRaw(File newRaw, Boolean deleteSource)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Boolean delete()
    {
        if(this.getZoneAttribute()==null)
            return true;

        return this.getZoneAttribute().delete();
    }










    public Zone getZone() {return z;}

    public ZoneAttribute getZoneAttribute() 
    {
        if(za==null)
        {
            this.za=ZoneAttribute.getZoneAttribute(this.z, this.getAttributeName());
        }

        return za;
    }

    

    public static String[] getNames(Zone z)
    {
        ArrayList<ZoneAttribute> zas  = ZoneAttribute.getZoneAttributes(z);
        String[] tmp = new String[zas.size()];
        Integer i=0;
        for (ZoneAttribute za : zas)
        {
            tmp[i]=za.getAttributePair().getAttributeName().getValue();
            i++;
        }
        return tmp;
    }

}
