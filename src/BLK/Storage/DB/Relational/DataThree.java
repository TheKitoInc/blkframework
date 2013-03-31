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
public class DataThree extends DataN
{

    private String tableCol0;
    private String tableCol1;

    public DataThree(Connection driver,String tableName,String tablePK,String tableCol0,String tableCol1) throws SQLException
    {
        super(driver, tableName, tablePK);
        this.tableCol0=tableCol0;
        this.tableCol1=tableCol1;
    }

    public Integer getId(String value0,String value1) throws SQLException
    {
        ArrayList<Pair> tmp = new ArrayList<Pair>();
        tmp.add(new Pair(this.tableCol0, value0));
        tmp.add(new Pair(this.tableCol1, value1));
        return super.getId(tmp);
    }

    public String getValue0(Integer pk) throws SQLException
    {
        ResultSet rs = (ResultSet)super.getValue(pk);
        return rs.getString(this.tableCol0);
    }

    public String getValue1(Integer pk) throws SQLException
    {
        ResultSet rs = (ResultSet)super.getValue(pk);
        return rs.getString(this.tableCol1);
    }

}
