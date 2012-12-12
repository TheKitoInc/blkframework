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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class DataN
{

    private Integer id;
    private ArrayList<Pair> lst;

    private String tablePk;
    private Connection cnn;
    private String tableName;


    private DataN(String tableName,String tablePk,Connection cnn)
    {
        this.tableName=tableName;
        this.tablePk=tablePk;
        this.cnn=cnn;
    }

    public DataN(Connection cnn)
    {
        this.id=null;
        this.lst=new ArrayList<Pair>();
        this.cnn=cnn;
    }

    public DataN(Integer id,String tableName,String tablePk,Connection cnn) throws SQLException
    {
        this(tableName, tablePk, cnn);
        this.id = id;
        this.lst=new ArrayList<Pair>();

        ArrayList<Pair> lst2= new ArrayList<Pair>();
        lst2.add(new Pair(this.tablePk,this.id));
        ResultSet rs = cnn.autoSelect(this.tableName, lst2);

        if(rs==null)
            throw new SQLException("Invalid PK");

        ResultSetMetaData rsmd = rs.getMetaData();
        int numColumns = rsmd.getColumnCount();

        for (int i=1; i<numColumns+1; i++)
        {
            String columnName = rsmd.getColumnName(i);
            if(!this.tablePk.equalsIgnoreCase(columnName))
                this.lst.add(new Pair(columnName, rs.getObject(columnName)));

        }



    }

    public DataN(ArrayList<Pair> lst,String tableName,String tablePk,Connection cnn) throws SQLException
    {
        this(tableName, tablePk, cnn);
        this.lst=lst;

        ResultSet rs = cnn.autoTable(this.tableName, this.lst);

        if(rs==null)
            throw new SQLException("Insert ERROR");

        this.id=rs.getInt(tablePk);
    }


    public Integer getId() {return id;}
    public ArrayList<Pair> getValues() {return lst;}

    public Object getValue(String tableCol)
    {
        for(Pair p : this.getValues())
        {
            if(p.getName().equalsIgnoreCase(tableCol))
                return p.getValue();
        }
        return null;
    }

    public Boolean delete()
    {
        ArrayList<Pair> lst2= new ArrayList<Pair>();
        lst2.add(new Pair(this.tablePk,this.id));
        return this.cnn.autoDelete(this.tableName, lst2);
    }
    
    public Boolean inUse(String table,String col)
    {
        ArrayList<Pair> lst2= new ArrayList<Pair>();
        lst2.add(new Pair(col,this.id));

        try
        {
            return this.cnn.autoSelect(table, lst2) != null;
        }
        catch (SQLException ex)
        {
            return true;
        }
    }

    public Connection getCnn() {
        return this.cnn;
    }
}
