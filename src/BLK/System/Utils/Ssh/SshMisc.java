/**
 * SshMisc 
 * --
 * 
 * This file provides miscellaneous functions:
 * 
 * crc
 * createString
 * getMpInt
 * addArrayOfBytes
 * getNotZeroRandomByte
 * getString
 * XORArrayOfBytes
 *
 * 
 * This file is part of "The Java Ssh Applet".
 */
  /*
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 */
package BLK.System.Utils.Ssh;


import BLK.System.Utils.Hash.MD5;
import java.io.IOException;


class SshMisc {
  static MD5 md5 = new MD5();

  /**
   * return the strint at the position offset in the data
   *  First 4 bytes are the length of the string, msb first (not
   *  including the length itself).  The following "length" bytes are
   *  the string value.  There are no terminating null characters.
   */
  static public  String getString(int offset, byte[] byteArray) throws IOException { 


    short d0 = byteArray[offset++];
    short d1 = byteArray[offset++];
    short d2 = byteArray[offset++];
    short d3 = byteArray[offset++];

    if (d0<0) d0 = (short)(256 + d0);
    if (d1<0) d1 = (short)(256 + d1);
    if (d2<0) d2 = (short)(256 + d2);
    if (d3<0) d3 = (short)(256 + d3);
		
    int length =  d0 * 16777216  //to be checked
      + d1 * 65536 
      + d2 * 256 
      + d3;
    String str = ""; //new String(byteArray,0);
    for (int i=0;i<length;i++) {
	if (byteArray[offset]>=0)
	    str += (char)(byteArray[offset++]);
	else
	    str += (char)(256+byteArray[offset++]);
    }
    return str;
  }

  static public byte getNotZeroRandomByte() {
		
    java.util.Date date = new java.util.Date();
    String randomString = String.valueOf( date.getTime() * Math.random() );
    byte[] randomBytes = md5.hash( randomString );
    int i=0;
    while (i<20) {
      byte b=0;
      if (i<randomBytes.length) b = randomBytes[i];
      if (b!=0) return  b;
      i++;
    }
    return getNotZeroRandomByte();
  }

  static public byte[] addArrayOfBytes(byte[] a, byte[]  b) {
    if (a==null) return b;
    if (b==null) return a;
    byte [] temp = new byte[a.length + b.length];
    for (int i=0;i<a.length;i++) temp[i] = a[i];
    for (int i=0;i<b.length;i++) temp[i+a.length] = b[i];
    return temp;
  }


  static public byte[] XORArrayOfBytes(byte[] a, byte[]  b) {
    if (a==null) return null;
    if (b==null) return null;
    if (a.length!=b.length) return null;
    byte[] result = new byte[a.length];
    for(int i=0; i<result.length; i++) result[i] = (byte)( ((a[i] & 0xff) ^ (b[i] & 0xff)) & 0xff);// ^ xor operator
    return result;
  }

  /**
   * Return the mp-int at the position offset in the data
   * First 2 bytes are the number of bits in the integer, msb first
   * (for example, the value 0x00012345 would have 17 bits).  The
   * value zero has zero bits.  It is permissible that the number of
   * bits be larger than the real number of bits.
   * The number of bits is followed by (bits + 7) / 8 bytes of binary
   * data, msb first, giving the value of the integer.
   */
	
  static public byte[] getMpInt(int offset, byte[] byteArray) throws IOException { 

    byte[] MpInt;

    short d0 = byteArray[offset++];
    short d1 = byteArray[offset++];

    if (d0<0) d0 = (short) (256 + d0);
    if (d1<0) d1 = (short) (256 + d1);


    int byteLength =  (d0*256 + d1 + 7)/8;
    MpInt = new byte[byteLength];
    for (int i=0;i<byteLength;i++) MpInt[i] = byteArray[offset++];
    return MpInt;
  } //getMpInt



  /**
   * Return a  Arbitrary length binary string
   * First 4 bytes are the length of the string, msb first (not
   * including the length itself).  The following "length" bytes are
   * the string value.  There are no terminating null characters.
   */
  static public byte[] 	createString(String str) throws IOException {

    int length = str.length();
    byte[] value = new byte[4 + length];
		
    value[3] = (byte) ((length) & 0xff);
    value[2] = (byte) ((length>>8) & 0xff);
    value[1] = (byte) ((length>>16) & 0xff);
    value[0] = (byte) ((length>>24) & 0xff);

    byte [] strByte = str.getBytes();
		
    for (int i=0; i<length; i++) value[i+4] = strByte[i];
    return value;
  } //createString
	






}
