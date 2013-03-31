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

    public DataPair64(Connection driver, String tableName, String tablePK, String tableCol) throws SQLException
    {
        super(driver, tableName, tablePK, tableCol);
    }

    @Override
    public Integer getId(String value) throws SQLException
    {
        return super.getId(Base64.doEncode(value));
    }

    @Override
    public Object getValue(Integer pk) throws SQLException
    {
        return Base64.doDecode(super.getValue(pk).toString());
    }

}
