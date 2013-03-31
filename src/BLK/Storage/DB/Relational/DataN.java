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
public class DataN
{

    private Connection driver;
    private String tableName;
    private String tablePK;
    
    public Connection getDriver()
    {
        return driver;
    }

    public String getTableName()
    {
        return tableName;
    }

    public String getTablePK()
    {
        return tablePK;
    }

    public DataN(Connection driver,String tableName,String tablePK) throws SQLException
    {
        this.driver=driver;
        this.tableName=tableName;
        this.tablePK=tablePK;
    }

    public Integer getId(ArrayList<Pair> values) throws SQLException
    {
        ResultSet rs = this.driver.autoTable(this.tableName, values);
        return rs.getInt(this.tablePK);
    }
    
    public Object getValue(Integer pk) throws SQLException
    {
        ArrayList<Pair> sel = new ArrayList<Pair>();
        sel.add(new Pair(this.tablePK, pk));

        ResultSet rs = this.driver.autoSelect(this.tableName,sel, 1);

        if(rs==null)
            throw new SQLException("Invalid PK");

        return rs;
    }

}
