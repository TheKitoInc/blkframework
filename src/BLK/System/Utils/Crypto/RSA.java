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
package BLK.System.Utils.Crypto;

import java.math.BigInteger;
import java.util.*;

/**
 *
 * @author andresrg
 *
 * based on http://casidiablo.net/implementacion-del-algoritmo-de-encriptacion-rsa-en-java-parte-1/
 * 
 */
public class RSA 
{

    BigInteger n;
    BigInteger e;
    BigInteger d;
    
    public RSA(int primeSize) 
    {
        BigInteger p=RSA.getPrime(primeSize);
        BigInteger q=RSA.getPrime(primeSize);
        
        while(q.compareTo(p)==0)
            q=RSA.getPrime(primeSize);
        
        this.n = p.multiply(q);   // n = p * q
        
        // totient = (p-1)*(q-1)
        BigInteger totient = p.subtract(BigInteger.valueOf(1));
        totient = totient.multiply(q.subtract(BigInteger.valueOf(1)));
        
        // Elegimos un e coprimo de y menor que n
        do 
            this.e = new BigInteger(2 * primeSize, new Random());
        while((this.e.compareTo(totient) != -1) || (this.e.gcd(totient).compareTo(BigInteger.valueOf(1)) != 0));
        
        this.d = this.e.modInverse(totient); // d = e^1 mod totient
    }
 
    private static BigInteger getPrime(int primeSize)
    {
        BigInteger b = BigInteger.ZERO;
        
        while(!b.isProbablePrime(10))
            b = new BigInteger(primeSize, 10, new Random());
        
        return b;
    }
    public static BigInteger[] encrypt(byte[] data,BigInteger n,BigInteger e)
    {
        BigInteger[] dataBig = new BigInteger[data.length];
        
        byte[] temp = new byte[1];                 
        for(int i=0; i<dataBig.length;i++)
        {
            temp[0] = data[i];
            BigInteger temp2=new BigInteger(temp);
            
            dataBig[i] = temp2.modPow(e,n);
        }        

        return(dataBig);
    }
    public static byte[] decrypt(BigInteger[] dataBig,BigInteger n,BigInteger d) {
        
        byte[] data = new byte[dataBig.length];
        
        for(int i=0; i<dataBig.length; i++)
        {
            BigInteger temp = dataBig[i].modPow(d,n);
            
            data[i] = (byte) (temp.intValue());        
        }

        return data;
    }

    public BigInteger getN() {return n;}
    public BigInteger getE() {return e;}
    public BigInteger getD() {return d;}

    public byte[] decrypt(BigInteger[] dataBig) {return RSA.decrypt(dataBig, this.n, this.d);}
    public BigInteger[] encrypt(byte[] data) {return RSA.encrypt(data, this.n, this.e);}
        
}