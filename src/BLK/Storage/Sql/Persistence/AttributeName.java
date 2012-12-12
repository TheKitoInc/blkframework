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
public class AttributeName extends DataPair64
{
    private static final String tableName="TREE_ATTRIBUTE_NAME";
    private static final String tablePk="ATTRIBUTE_NAME_ID";
    private static final String tableValue="ATTRIBUTE_NAME_VALUE";

    private AttributeName(String value, Connection cnn) throws SQLException
    {
        super(value, AttributeName.tableName, AttributeName.tablePk, AttributeName.tableValue, cnn);
    }

    private AttributeName(Integer id, Connection cnn) throws SQLException
    {
        super(id, AttributeName.tableName, AttributeName.tablePk, AttributeName.tableValue, cnn);
    }
    
    public static AttributeName getName(String value, Connection cnn)
    {
        try
        {
            return new AttributeName(value, cnn);
        } 
        catch (SQLException ex)
        {
            throw new UnsupportedOperationException(ex.getMessage());// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
        }
    }

    public static AttributeName getName(Integer id, Connection cnn)
    {
        try
        {
            return new AttributeName(id, cnn);
        } 
        catch (SQLException ex)
        {
            throw new UnsupportedOperationException(ex.getMessage());// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
        }
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
