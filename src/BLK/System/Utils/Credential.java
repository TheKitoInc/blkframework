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

package BLK.System.Utils;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Credential {

    private java.lang.String user=null;
    private java.lang.String pass=null;
    private java.lang.String domain=null;

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Credential other = (Credential) obj;
        if ((this.user == null) ? (other.user != null) : !this.user.equals(other.user)) {
            return false;
        }
        if ((this.pass == null) ? (other.pass != null) : !this.pass.equals(other.pass)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 71 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 71 * hash + (this.pass != null ? this.pass.hashCode() : 0);
        hash = 71 * hash + (this.domain != null ? this.domain.hashCode() : 0);
        return hash;
    }


    public java.lang.String getDomain()
    {
        return domain;
    }

    public void setDomain(java.lang.String domain)
    {
        this.domain = domain;
    }

    public java.lang.String getPass()
    {
        return pass;
    }

    public void setPass(java.lang.String pass)
    {
        this.pass = pass;
    }

    public java.lang.String getUser()
    {
        return user;
    }

    public void setUser(java.lang.String user)
    {
        this.user = user;
    }

    public Credential(java.lang.String user,java.lang.String pass,java.lang.String domain)
    {
        this.user=user;
        this.pass=pass;
        this.domain=domain;
    }


}
