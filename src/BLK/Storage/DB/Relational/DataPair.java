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

package BLK.Storage.DB.Relational;

import BLK.System.Utils.Pair;
import BLK.io.Network.Protocols.Application.Sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class DataPair extends DataN
{

    private String tableCol;
    private String tableSrchCol = null;

    public DataPair(Connection driver,String tableName,String tablePK,String tableCol,String tableSrchCol) throws SQLException
    {
        this(driver, tableName, tablePK, tableCol);
        this.tableSrchCol=tableSrchCol;
    }

    public DataPair(Connection driver,String tableName,String tablePK,String tableCol) throws SQLException
    {
        super(driver, tableName, tablePK);
        this.tableCol=tableCol;
    }
 
    public Integer getId(String value) throws SQLException
    {
        ArrayList<Pair> tmp = new ArrayList<Pair>();
        tmp.add(new Pair(this.tableCol, value));
        return super.getId(tmp);
    }

    @Override
    public Object getValue(Integer pk) throws SQLException
    {
        ResultSet rs = (ResultSet)super.getValue(pk);
        return rs.getString(this.tableCol);
    }


}
