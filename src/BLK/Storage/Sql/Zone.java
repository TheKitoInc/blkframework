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

import BLK.Storage.Sql.Persistence.ZoneName;
import BLK.Storage.IAttribute;
import BLK.Storage.IZone;
import BLK.System.Utils.Pair;
import BLK.io.Network.Protocols.Application.Sql.Connection;
import BLK.io.Network.Protocols.Application.Sql.DataN;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Zone extends DataN implements IZone
{
    private static final String tableName="TREE_ZONE";
    private static final String tablePk="ZONE_ID";
    private static final String tableValue1="ZONE_NAME_ID";
    private static final String tableValue2="ZONE_PARENT_ID";
    
    private static ArrayList<Pair> getAL(Integer idName,Integer idParent)
    {
        ArrayList<Pair> t  = new ArrayList<Pair>();
        t.add(new Pair(Zone.tableValue1, idName));
        t.add(new Pair(Zone.tableValue2, idParent));                
        return t;
    }

    private Integer idParent;    
    private ZoneName zn;

    public static IZone getZone(Zone parent, String name)
    {
        try 
        {
            ZoneName zn=ZoneName.getName(name, parent.getCnn());            
            return new Zone(parent.getId(), zn.getId(),zn,parent.getCnn());
        } 
        catch (SQLException ex) 
        {
            throw new UnsupportedOperationException(ex.getMessage());// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
        }
    }
    private Zone(Integer idParent, Integer idName,ZoneName zn,Connection cnn) throws SQLException
    {
        super(Zone.getAL(idName, idParent), Zone.tableName, Zone.tablePk, cnn);
        this.idParent=idParent;
        this.zn=zn;
    }

    public static IZone getZone(Connection root)
    {
        try
        {
            return new Zone(root);
        }
        catch (SQLException ex)
        {
            throw new UnsupportedOperationException(ex.getMessage());// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
        }
    }
    private Zone(Connection root) throws SQLException
    {        
        super(root);
        this.zn=null;
        this.idParent=null;
    }

    public static IZone getZone(Connection root, Integer id)
    {
        try
        {
            return new Zone(root,id);
        }
        catch (SQLException ex)
        {
            throw new UnsupportedOperationException(ex.getMessage());// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
        }
    }
    private Zone(Connection root, Integer id) throws SQLException
    {
        super(id, Zone.tableName, Zone.tablePk, root);

        this.zn=ZoneName.getName((Integer)super.getValue(Zone.tableValue1), root);
        this.idParent=(Integer)super.getValue(Zone.tableValue2);        
    }



    @Override
    public IZone getSubZone(String name)
    {
        return Zone.getZone(this, name);
    }

    @Override
    public String getName()
    {
        return this.zn.getValue();
    }
 

    @Override
    public IZone getParentZone()
    {
        return Zone.getZone(this.getCnn(), this.idParent);
    }


















    


    @Override
    public String[] getSubZones()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    @Override
    public Boolean changeParent(IZone newParent)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean delete()
    {
        //pendiente atributos e hijos

        if(!super.delete())
            return false;

        if (!this.zn.inUse(Zone.tableName, Zone.tableValue1))
            zn.delete();

        return true;
    }

    @Override
    public String[] getAttributes()
    {
        return Attribute.getNames(this);
    }

    @Override
    public IAttribute getAttribute(String name)
    {
        return Attribute.getAttribute(this, name);
    }



}
