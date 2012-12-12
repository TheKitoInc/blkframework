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

import BLK.System.Utils.Coder.Base64;
import BLK.io.Network.Protocols.Application.Sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class DataPair64 extends DataPair
{

    public DataPair64(String value, String tableName, String tablePk, String tableValue, Connection cnn) throws SQLException
    {
        super(DataPair64.toB64(value), tableName, tablePk, tableValue, cnn);
    }

    public DataPair64(Integer id, String tableName, String tablePk, String tableValue, Connection cnn) throws SQLException
    {
        super(id, tableName, tablePk, tableValue, cnn);
    }

    @Override
    public Integer getId()
    {
        return super.getId();
    }

    @Override
    public Object getValue()
    {
        return DataPair64.fromB64(super.getValue().toString());
    }



    private static String fromB64(String b64Str)
    {
        return Base64.doDecode(b64Str);
    }
    private static String toB64(String str)
    {
        return Base64.doEncode(str);
    }

}
