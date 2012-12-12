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

import BLK.io.Network.Protocols.Application.Sql.Connection;
import BLK.Storage.DB.Relational.DataPair64;
import java.sql.SQLException;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class ZoneName extends DataPair64
{
    private static final String tableName="TREE_ZONE_NAME";
    private static final String tablePk="ZONE_NAME_ID";
    private static final String tableValue="ZONE_NAME_VALUE";

    private ZoneName(String value, Connection cnn) throws SQLException
    {
        super(value, ZoneName.tableName, ZoneName.tablePk, ZoneName.tableValue, cnn);
    }

    private ZoneName(Integer id, Connection cnn) throws SQLException
    {
        super(id, ZoneName.tableName, ZoneName.tablePk, ZoneName.tableValue, cnn);
    }
    
    public static ZoneName getName(String value, Connection cnn) throws SQLException
    {
        return new ZoneName(value, cnn);
    }

    public static ZoneName getName(Integer id, Connection cnn) throws SQLException
    {
        return new ZoneName(id, cnn);
    }

    @Override
    public Integer getId()
    {
        return super.getId();
    }

    @Override
    public String getValue()
    {
        return super.getValue().toString();
    }

    @Override
    public Boolean delete()
    {
        return super.delete();
    }
}
