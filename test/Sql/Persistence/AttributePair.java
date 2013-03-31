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
import BLK.Storage.DB.Relational.DataThree;
import java.sql.SQLException;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class AttributePair extends DataThree
{
    private static final String tableName="TREE_ATTRIBUTE";
    private static final String tablePk="ATTRIBUTE_ID";    
    private static final String tableValue1="ATTRIBUTE_NAME_ID";
    private static final String tableValue2="ATTRIBUTE_VALUE_ID";

    private AttributeName an=null;
    private AttributeValue av=null;


    public static AttributePair getAttributePair(AttributeName name, AttributeValue value)
    {
        try
        {
            return new AttributePair(name,value);
        }
        catch (SQLException ex)
        {
            throw new UnsupportedOperationException(ex.getMessage());// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
        }
    }
    public static AttributePair getAttributePair(Integer id, Connection cnn)
    {
        try
        {
            return new AttributePair(id, cnn);
        }
        catch (SQLException ex)
        {
            throw new UnsupportedOperationException(ex.getMessage());// FALTA DEFINIR CONTROL DE ERRORES Y EXEPCIONES
        }
    }

    private AttributePair(AttributeName an, AttributeValue av) throws SQLException
    {
        super(an.getId(), av.getId(), AttributePair.tableName, AttributePair.tablePk, AttributePair.tableValue1, AttributePair.tableValue2, an.getCnn());
        this.an=an;
        this.av=av;        
    }

    private AttributePair(Integer id, Connection cnn) throws SQLException
    {
        super(id, AttributePair.tableName, AttributePair.tablePk,AttributePair.tableValue1, AttributePair.tableValue2, cnn);
    }

    @Override
    public Boolean delete()
    {
        if(!super.delete())
            return false;

        if (!this.getAttributeName().inUse(AttributePair.tableName, AttributePair.tableValue1))
            this.getAttributeName().delete();

        if (!this.getAttributeValue().inUse(AttributePair.tableName, AttributePair.tableValue2))
            getAttributeValue().delete();

        return true;
    }

    public AttributeName getAttributeName()
    {
        if(this.an==null)        
            this.an=AttributeName.getName((Integer)super.getValue1(), this.getCnn());
        

        return this.an;
    }

    public AttributeValue getAttributeValue()
    {
        if(this.av==null)
            this.av=AttributeValue.getValue((Integer)super.getValue2(), this.getCnn());

        return this.av;
    }



    @Override
    public Integer getId()
    {
        return super.getId();
    }




}
