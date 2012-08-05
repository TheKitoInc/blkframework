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

import BLK.System.Logger;
import BLK.System.Utils.Pair;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Connection
{


    private String user="root";
    private String password="";
    private String server="localhost";
    private String db="test";
    private String driver="mysql";

    public Connection(String user,String password,String server,String db)
    {
        this.user=user;
        this.password=password;
        this.server=server;
        this.db=db;
    }

    public String getDb() {return db;}
    public void setDb(String db) {this.db = db;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getServer() {return server;}
    public void setServer(String server) {this.server = server;}
    public String getUser() {return user;}
    public void setUser(String user) {this.user = user;}




    private java.sql.Connection cnn=null;

    public boolean chkCNN()
    {
        try
        {
            if (cnn == null)
                Logger.getLogger().info("Connecting to " + this.db + " on " + this.server);
            else if (cnn.isClosed())
                Logger.getLogger().info("Reconnecting to " + this.db + " on " + this.server);
            
            if (cnn == null || cnn.isClosed())
                cnn = DriverManager.getConnection("jdbc:"+this.driver+"://"+this.server+"/"+this.db, this.user, this.password);

            return !cnn.isClosed();
        }
        catch (SQLException ex)
        {
            Logger.getLogger().warn(ex);
            return false;
        }
    }
    @Deprecated
    public boolean sqlCmd(String command){return command(command);}
    public Boolean command(String command)
    {
        Logger.getLogger().debug("SQL_COMMAND: "+command);
        if (!chkCNN())
            return false;

        try
        {
            cnn.createStatement().execute(command);
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger().warn(ex);
        }

        return false;
    }
    @Deprecated
    public ResultSet sqlQuery(String query) throws SQLException{return query(query);}
    public ResultSet query(String query) throws SQLException
    {
        Logger.getLogger().debug("SQL_QUERY: "+query);
        if (!chkCNN())
            throw new SQLException("Fail to connect");
        else
            return cnn.createStatement().executeQuery(query);
    }

    public Boolean autoInsert(String table,ArrayList<Pair> cols) throws SQLException
    {
        String insert_a="";
        String insert_b="";

        for(Pair p : cols)
        {
            if(!insert_a.isEmpty())
                insert_a+=",";

            insert_a+=p.getName();

            if(!insert_b.isEmpty())
                insert_b+=",";

            insert_b+=Connection.getSqlValue(p.getString());
        }

        String insert="INSERT INTO " + table + " ("+insert_a+") VALUES ("+insert_b+");";
        return this.command(insert);
    }

    public ResultSet autoSelect(String table,ArrayList<Pair> cols,Integer limit) throws SQLException
    {
        String query="";
        for(Pair p : cols)
        {
            if(!query.isEmpty())
                query+=" and";

            query+=" "+getSqlPair(p.getName(), p.getString());
        }

        if(!query.isEmpty())
            query="SELECT * FROM " + table + " WHERE "+query;
        else
            query="SELECT * FROM " + table;

        if(limit!=null)
            query+=" LIMIT " + limit.toString();

        ResultSet rs=this.query(query+";");

        if(rs.next())
            return rs;
        else
            return null;
    }
    public ResultSet autoSelect(String table,ArrayList<Pair> cols) throws SQLException
    {
        return autoSelect(table, cols, null);
    }


    public ResultSet autoTable(String table,ArrayList<Pair> cols) throws SQLException
    {
        ResultSet rs=this.autoSelect(table, cols, 1);
     
        if(rs!=null)
            return rs;
        else
        {
            if(this.autoInsert(table, cols))
                    return this.autoTable(table, cols);
            else
                return null;
        }
        
    }

        public Boolean autoDelete(String table,ArrayList<Pair> cols)
    {
        String cmd="";
        for(Pair p : cols)
        {
            if(!cmd.isEmpty())
                cmd+=" and";

            cmd+=" "+getSqlPair(p.getName(), p.getValue());
        }

        if(!cmd.isEmpty())
            cmd="DELETE FROM " + table + " WHERE "+cmd+";";
        else
            cmd="DELETE FROM " + table + ";";

        return this.command(cmd);
    }

    public static String getSqlValue(Object value)
    {
        if(value==null)
            return "null";
        else
            return "'"+value.toString()+"'";
    }
    public static String getSqlPair(String key,Object value)
    {
        if(value==null)
            return key+" is null";
        else
            return key+"='"+value.toString()+"'";
    }



    
//    public static String getSelect(String table,ArrayList<String> cols,ArrayList<Pair> conditions)
//    {
//        return "SELECT " + getSql(",", "", cols) + " FROM "+table+" WHERE "+getSqlPair("and", "=", conditions)+";";
//    }
//    public static String getSelect(String table,ArrayList<String> cols)
//    {
//        return "SELECT " + getSql(",", "", cols) + " FROM "+table+";";
//    }

//    private static String getSql(String separator,String container,ArrayList<String> lst)
//    {
//        String sql="";
//        for(String s : lst)
//        {
//            if(!sql.isEmpty())
//                sql+=" "+separator+" ";
//            if(s==null)
//                sql+="null";
//            else
//                sql+=container+s+container;
//        }
//        return sql;
//    }
//    private static String getSqlPair(String separator,String comparator,ArrayList<Pair> lst)
//    {
//        String sql="";
//        for(Pair p : lst)
//        {
//            if(!sql.isEmpty())
//                sql+=" "+separator+" ";
//            if(p.getValue()==null)
//                sql+=p.getName()+comparator+"null";
//            else
//                sql+=p.getName()+comparator+"'"+p.getValue()+"'";
//        }
//        return sql;
//    }
    public String getText(String query) throws SQLException
    {
        for(Pair value : this.getRow(query))
            return value.getValue().toString();
        
        return null;
    }

    public ArrayList<Pair> getRow(String query) throws SQLException
    {
        ArrayList<Pair> p = new ArrayList<Pair>();

        ResultSet rs=this.query(query);


        if(rs.next())
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();

            for (int i=1; i<numColumns+1; i++)
            {
                String columnName = rsmd.getColumnName(i);

                p.add(new Pair(columnName, rs.getObject(columnName)));

            }
        }

        if(rs.next())
            throw new UnsupportedOperationException ("Too many results on: "+query);

        return p;
    }
    public ArrayList<Object> getCol(String tableName,String colName,String colToCompare,String value) throws SQLException
    {
       ArrayList<Object> p = new ArrayList<Object>();

       ResultSet rs=this.query("SELECT "+colName+" FROM "+tableName+" WHERE "+colToCompare+"='"+value+"'");

        while (rs.next())
            p.add(rs.getObject(1));

        return p;
    }



}
