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
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class DataPair extends DataN
{

    private String tableValue;
    public DataPair(Integer id,String tableName,String tablePk,String tableValue,Connection cnn) throws SQLException
    {
        super(id, tableName, tablePk, cnn);
        this.tableValue=tableValue;
    }

    public DataPair(Object value,String tableName,String tablePk,String tableValue,Connection cnn) throws SQLException
    {
        super(DataPair.getFromValue(tableValue, value), tableName, tablePk, cnn);
        this.tableValue=tableValue;
    }

    private static ArrayList<Pair> getFromValue(String tableValue, Object value)
    {
           ArrayList<Pair> lst= new ArrayList<Pair>();
           lst.add(new Pair(tableValue,value));
           return lst;
    }

    @Override
    public Integer getId() {return super.getId();}
    public Object getValue() {return super.getValue(this.tableValue);}


}
