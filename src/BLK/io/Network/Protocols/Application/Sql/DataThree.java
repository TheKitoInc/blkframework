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

package BLK.io.Network.Protocols.Application.Sql;

import BLK.System.Utils.Pair;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class DataThree extends DataN
{
    private String tableValue1;
    private String tableValue2;
    public DataThree(Integer id,String tableName,String tablePk,String tableValue1,String tableValue2,Connection cnn) throws SQLException
    {
        super(id, tableName, tablePk, cnn);
        this.tableValue1=tableValue1;
        this.tableValue2=tableValue2;
    }

    public DataThree(Object value1,Object value2,String tableName,String tablePk,String tableValue1,String tableValue2,Connection cnn) throws SQLException
    {
        super(DataThree.getFromValue(tableValue1, value1,tableValue2, value2), tableName, tablePk, cnn);
        this.tableValue1=tableValue1;
        this.tableValue2=tableValue2;
    }

    private static ArrayList<Pair> getFromValue(String tableValue1, Object value1,String tableValue2, Object value2)
    {
           ArrayList<Pair> lst= new ArrayList<Pair>();
           lst.add(new Pair(tableValue1,value1));
           lst.add(new Pair(tableValue2,value2));
           return lst;
    }

    @Override
    public Integer getId() {return super.getId();}
    public Object getValue1() {return super.getValue(this.tableValue1);}
    public Object getValue2() {return super.getValue(this.tableValue2);}
}
