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

package BLK.io.FileSystem;

import BLK.System.Logger;
import BLK.System.Os.Process;
import BLK.System.Utils.Hashtable;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class File extends FileSystem {

    public File(String pathname)
    {
        super(pathname);

         if (super.exists() && !super.isFile())
            throw new UnsupportedOperationException("Invalid File object: "+super.getPath());
    }
    public File(Folder parent,String name)
    {
        super(parent,name);

         if (super.exists() && !super.isFile())
            throw new UnsupportedOperationException("Invalid File object: "+super.getPath());
    }

    public FileWriter getWriter()
    {
        if(!super.exists())
            super.create();
        
        if (super.exists() && super.isWritable())
            try
            {
                return new FileWriter(super.getObject());
            }
            catch (IOException ex) {
                java.util.logging.Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        else
            return null;
    }
    public FileReader getReader()
    {
        if(!super.exists())
            super.create();

        if (super.exists() && super.isReadable())
            try
            {
                return new FileReader(this.getObject());
            }
            catch (FileNotFoundException ex)
            {
                java.util.logging.Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        else
            return null;
    }

    public FileOutputStream getOutputStream()
    {
        if(!super.exists())
            super.create();

        if (super.exists() && this.isWritable())
            try
            {
                return new FileOutputStream(super.getObject());
            }
            catch (IOException ex) {
                java.util.logging.Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        else
            return null;
    }
    public FileInputStream getInputStream()
    {
        if(!super.exists())
            super.create();

        if (super.exists() && super.isReadable())
            try
            {
                return new FileInputStream(this.getObject());
            }
            catch (FileNotFoundException ex)
            {
                java.util.logging.Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        else
            return null;
    }

    public BufferedReader getBufferedReader()
    {
        Reader r = this.getReader();

        if(r==null)
            return null;
        
        return new BufferedReader(r);
    }
    public BufferedWriter getBufferedWriter()
    {
        Writer w = this.getWriter();

        if(w==null)
            return null;

        return new BufferedWriter(w);
    }

    public static File getTemp(Folder parent){return new File(FileSystem.randomPath(parent, null,null)); }
    public static File getTemp(Folder parent,String ext){return new File(FileSystem.randomPath(parent, null,"."+ext)); }
    public static File getTemp(String ext){return new File(FileSystem.randomPath(Folder.temp, null, "."+ext)); }
    public static File getTemp(){return new File(FileSystem.randomPath(Folder.temp,null,null)); }

    public String getNameWithOutExtension()
    {
        String FName=super.getName();

        int index = FName.lastIndexOf(".");

        if (index == -1 || index == FName.length())
            return FName;
        else if(index==0)
            return null;
        else
            return FName.substring(0,index);
    }
    public String getExtension()
    {
        String FName=super.getName();
        int index = FName.lastIndexOf(".");

        if (index == -1 || index == FName.length()-1)
            return null;
        else
            return FName.substring(index + 1);
    }

    public Long getSize(){return super.getObject().length();}
    public Boolean isExecutable(){return this.getObject().canExecute();}
    public Boolean setExecutable(Boolean b) {return this.getObject().setExecutable(b);}

    public Boolean setString(String string)
    {
        if (super.exists())
            this.delete();

        if(!this.getParent().create())
            return false;

        if(!super.create())
            return false;

        Writer w=this.getBufferedWriter();

        if(w==null)
            return false;

        try
        {
            w.write(string);
            w.close();
            return true;
        }
        catch (IOException iOException)
        {
            return false;
        }

  }
    public String getString(String defstring){return getString(defstring, Boolean.TRUE);}
    public String getString(String defstring,Boolean create)
    {
    
        if(!super.exists())
        {
            if(create)
                setString(defstring);

            return defstring;
        }

        StringBuilder contents = new StringBuilder();
        BufferedReader input =  this.getBufferedReader();

        if(input==null)
            return null;
        
        try 
        {
            String line = null; //not declared within while loop
            while ((line = input.readLine()) != null) {
                if (contents.length() != 0) 
                    contents.append(System.getProperty("line.separator"));
                
                contents.append(line);
            }

            input.close();
            return contents.toString();            
        } 
        catch (IOException iOException) 
        {
            return null;
        }
  }

    public String getType()
    {
            if(super.exists() && super.isReadable())
            {
                String s = Process.basicCallReader("file -bi  \"" + this.getPath() + "\"");

                if (s!=null)
                    return s.split(" ")[0].trim();
            }

            return null;
    }

    @Deprecated
    public enum fileHashs {md5sum, sha1sum, crc32}
    @Deprecated
    public String calcHash(fileHashs hash)
    {
        if(hash==hash.crc32)
            return getCrc32(Boolean.TRUE);
        else if(hash==hash.md5sum)
            return getMd5(Boolean.TRUE);
        else if(hash==hash.sha1sum)
            return getSha1(Boolean.TRUE);
        else
            return null;
    }


    private String getHash(String command)
    {
            if(super.exists() && super.isReadable())
            {
                String s = Process.basicCallReader(command + " \"" + this.getPath() + "\"");

                if (s!=null)
                    return s.split(" ")[0].trim();
            }

            return null;
    }

    private String crc32=null;
    public String getCrc32(Boolean reCalc)
    {
        if(this.crc32==null || reCalc)
            this.crc32=getHash("crc32");

        return this.crc32;
    }
    public String getCrc32(){return getCrc32(Boolean.FALSE);}

    private String md5=null;
    public String getMd5(Boolean reCalc)
    {
        if(this.md5==null || reCalc)
            this.md5=getHash("md5sum -b");

        return this.md5;
    }
    public String getMd5(){return getMd5(Boolean.FALSE);}

    private String sha1=null;
    public String getSha1(Boolean reCalc)
    {
        if(this.sha1==null || reCalc)
            this.sha1=getHash("sha1sum -b");

        return this.sha1;
    }
    public String getSha1(){return getSha1(Boolean.FALSE);}

    private String uid=null;
    public String getUid(Boolean reCalc)
    {
        if(this.uid==null || reCalc)
        {
            String alg1=this.getCrc32(reCalc);
            String alg2=this.getMd5(reCalc);
            String alg3=this.getSha1(reCalc);

            if(alg1!=null && alg2!=null && alg3!=null)
                this.uid=(String.valueOf(this.getSize())+"z"+alg1+"x"+alg2+"y"+alg3).toUpperCase();
        }
        return this.uid;
    }
    public String getUid(){return getUid(Boolean.FALSE);}

    public Boolean archive()
    {
        String UID=this.getUid(Boolean.TRUE);
        if(UID==null)
            return false;
        throw new UnsupportedOperationException("Not supported yet.");

    }

    public boolean equals(File file)
    {
        if (this.getPath().equalsIgnoreCase(file.getPath()))
            return true;

        if (!file.exists() || !super.exists())
            return false;

        if (this.getSize().compareTo(file.getSize())!=0)
            return false;

//        if(!this.getUid().equalsIgnoreCase(file.getUid()))
//            return false;
//    
        if(Process.basicCall("cmp -s \"" + this.getPath() + "\" \"" + file.getPath() + "\""))
            return true;

        try
        {
            int BUFFER_SIZE = 65536;


            FileInputStream firstInput = getInputStream();
            FileInputStream secondInput = file.getInputStream();
            BufferedInputStream bufFirstInput = new BufferedInputStream(firstInput, BUFFER_SIZE);
            BufferedInputStream bufSecondInput = new BufferedInputStream(secondInput, BUFFER_SIZE);

            int firstByte;
            int secondByte;

            while (true)
            {

                firstByte = bufFirstInput.read();
                secondByte = bufSecondInput.read();

                if (firstByte != secondByte)
                {
                    bufFirstInput.close();
                    firstInput.close();
                    bufSecondInput.close();
                    secondInput.close();

                    return false;
                }

                if ((firstByte < 0) && (secondByte < 0))
                {
                    bufFirstInput.close();
                    firstInput.close();
                    bufSecondInput.close();
                    secondInput.close();

                    return true;
                }
            }
        }
        catch (IOException ex)
        {
            System.gc();
            Logger.getLogger().error(ex);
            return Process.basicCall("cmp \"" + this.getPath() + "\" \"" + file.getPath() + "\"");
        }




    }

    public Boolean setArray(String[] lst)
    {
        String out=new String();
        for (String e : lst)
            if(e!=null && !e.trim().isEmpty())
                out+=e+"\n";

        return this.setString(out);
    }
    public String[] getArray()
    {
        String in=this.getString("", false);
        in=in.replaceAll("\r", "");
        return in.split("\n");
    }

    public Boolean setArray(ArrayList<String> lst)
    {
        String out=new String();
        for (String e : lst)
            if(e!=null && !e.trim().isEmpty())
                out+=e+"\n";

        return this.setString(out);
    }
    public ArrayList<String> getArrayList()
    {
        ArrayList<String> al = new ArrayList<String>();

        String in=this.getString("", false);
        in=in.replaceAll("\r", "");
        for(String s: in.split("\n"))
            if(s!=null && !s.isEmpty())
                al.add(s);

        return al;
    }

    @Deprecated
    public Boolean setHashtable(Hashtable<String,String> tbl)
    {
        Enumeration<String> keys=tbl.keys();

        String out=new String();
        while (keys.hasMoreElements())
        {
            String key=keys.nextElement();
            out+=key+"="+tbl.get(key).toString()+"\n";
        }

        return this.setString(out);
    }
    @Deprecated
    public Hashtable<String,String> getHashtable()
    {
        Hashtable<String,String> out=new Hashtable<String, String>();

        for (String line : this.getArray())
            if(line!=null && !line.trim().isEmpty())
            {
                String parts[]=line.split("=", 1);

                if (parts.length==1)
                    out.put(parts[0].trim(), "");
                else if (parts.length==2)
                    out.put(parts[0].trim(), parts[1].trim());
            }

        return out;
    }

    public Boolean setHashMap(HashMap<String,String> map)
    {
        Iterator it = map.entrySet().iterator();

        String out=new String();
        while (it.hasNext())
        {
            Map.Entry e = (Map.Entry)it.next();
            out+=e.getKey()+"="+e.getValue().toString()+"\n";
        }

        return this.setString(out);
    }
    public HashMap<String,String> getHashMap()
    {
        HashMap<String,String> out=new HashMap<String, String>();

        for (String line : this.getArray())
            if(line!=null && !line.trim().isEmpty())
            {
                String parts[]=line.split("=", 2);

                if (parts.length==1)
                    out.put(parts[0].trim(), "");
                else if (parts.length==2)
                    out.put(parts[0].trim(), parts[1].trim());
            }

        return out;
    }

    public ArrayList<String> getLines()
    {
        ArrayList<String> lines=new ArrayList<String>();
        String data=this.getString("", false);

        data=data.replaceAll("\r", "\n");

        for (String line : data.split("\n"))
            if(line!=null && !line.trim().isEmpty())
                lines.add(line.trim());

        return lines;
    }

    public File getRaw()
    {
        File f=File.getTemp(this.getExtension());
        try
        {
            Transaction t = new Transaction();
            t.addOperation(Transaction.operations.copy, this, f);

            if(t.doIt())
                return f;
            else
                return null;
        }
        catch (FileSystemException ex)
        {
            Logger.getLogger().error(ex);
            return null;
        }
    }
    public Boolean setRaw(File newRaw,Boolean deleteSource)
    {
        try
        {
            Transaction t = new Transaction();
            t.addOperation(Transaction.operations.delete, this, null);

            if(deleteSource)
                t.addOperation(Transaction.operations.move, newRaw, this);
            else
                t.addOperation(Transaction.operations.copy, newRaw, this);

            return t.doIt();
        }
        catch (FileSystemException ex)
        {
            Logger.getLogger().error(ex);
            return false;
        }
    }
    
}
