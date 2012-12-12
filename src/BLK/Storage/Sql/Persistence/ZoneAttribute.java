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

package BLK.Storage.Sql.Persistence;

import BLK.Storage.Sql.Zone;
import BLK.System.Utils.Pair;
import BLK.io.Network.Protocols.Application.Sql.Connection;
import BLK.Storage.DB.Relational.DataThree;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class ZoneAttribute extends DataThree
{
    private static final String tableName="TREE_ZONE_ATTRIBUTE";
    private static final String tablePk="ZONE_ATTRIBUTE_ID";
    private static final String tableValue1="ZONE_ATTRIBUTE_ZONE_ID";
    private static final String tableValue2="ZONE_ATTRIBUTE_ATTRIBUTE_ID";

    private Zone z=null;
    private AttributePair ap=null;


    public static ZoneAttribute getZoneAttribute(Zone z, AttributePair ap)
    {
        try
        {
            return new ZoneAttribute(z, ap);
        }
        catch (SQLException ex)
        {
            throw new UnsupportedOperationException(ex.getMessage());// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
        }
    }
    public static ZoneAttribute getZoneAttribute(Integer id, Connection cnn)
    {
        try
        {
            return new ZoneAttribute(id, cnn);
        }
        catch (SQLException ex)
        {
            throw new UnsupportedOperationException(ex.getMessage());// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
        }
    }

    public static ArrayList<ZoneAttribute> getZoneAttributes(Zone z)
    {

        ArrayList<Pair> lst = new ArrayList<Pair>();
        lst.add(new Pair(ZoneAttribute.tableValue1, z.getId()));
        try
        {
                ArrayList<ZoneAttribute> tmp = new ArrayList<ZoneAttribute>();

                ResultSet rs = z.getCnn().autoSelect(ZoneAttribute.tableName, lst);
                if (rs==null)
                    return null;
                do
                {
                   ZoneAttribute za = ZoneAttribute.getZoneAttribute(rs.getInt(ZoneAttribute.tablePk),z.getCnn());
                   tmp.add(za);
                }
                while (rs.next());

                return tmp;
        }
        catch (SQLException ex)
        {
            throw new UnsupportedOperationException(ex.getMessage());// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
        }
    }

    public static ZoneAttribute getZoneAttribute(Zone z, AttributeName an)
    {
        for(ZoneAttribute za : ZoneAttribute.getZoneAttributes(z))
            if (za.getAttributePair().getAttributeName().getValue().equalsIgnoreCase(an.getValue()))
                   return za;

            
        return null;
    }


    private ZoneAttribute(Zone z, AttributePair ap) throws SQLException
    {
        super(z.getId(), ap.getId(), ZoneAttribute.tableName, ZoneAttribute.tablePk, ZoneAttribute.tableValue1, ZoneAttribute.tableValue2, z.getCnn());
        this.z=z;
        this.ap=ap;
    }

    private ZoneAttribute(Integer id, Connection cnn) throws SQLException
    {
        super(id, ZoneAttribute.tableName, ZoneAttribute.tablePk,ZoneAttribute.tableValue1, ZoneAttribute.tableValue2, cnn);
    }

    @Override
    public Boolean delete()
    {
        if(!super.delete())
            return false;
        
        if (!this.ap.inUse(ZoneAttribute.tableName, ZoneAttribute.tableValue2))
            ap.delete();

        return true;
    }

    public AttributePair getAttributePair()
    {
        if(this.ap==null)
            this.ap=AttributePair.getAttributePair((Integer)super.getValue2(), this.getCnn());


        return this.ap;
    }

    public Zone getZone()
    {
        if(this.z==null)
            this.z=(Zone) Zone.getZone(this.getCnn(),(Integer)super.getValue1());

        return this.z;
    }



    @Override
    public Integer getId()
    {
        return super.getId();
    }




}
