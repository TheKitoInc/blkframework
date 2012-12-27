/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.FileSystem;

import BLK.io.FileSystem.Core.File;
import BLK.io.FileSystem.Core.Folder;
import java.sql.Savepoint;
import java.util.ArrayList;

/**
 *
 * @author andresrg
 */
public class IndexFolder extends Folder
{
    private static final String tag="index";

    public IndexFolder(String pathname) 
    {
        super(pathname);
        loadIndex();
    }

    public IndexFolder(Folder parent, String name) 
    {
        super(parent, name);
        loadIndex();
    }

    public IndexFolder() 
    {
        super();
        loadIndex();
    }

    private void loadIndex() 
    {
        ArrayList<FSObject> newIndex=new ArrayList<FSObject>();

        for(Folder f : super.getFolders())
            newIndex.add(new FSFolder(f.getName()));
                
        for(File f : super.getFiles())        
            if(!f.getExtension().equalsIgnoreCase(tag))
                newIndex.add(new FSFile(f.getName(), f.getSign()));
                        
        ArrayList<FSObject> oldIndex=this.readIndex();
        
        ArrayList<FSObject> creates = new ArrayList<FSObject>();
        ArrayList<FSObject> deletes = new ArrayList<FSObject>();

        ArrayList<FSObject> nochange = new ArrayList<FSObject>();
        ArrayList<FSObject> update = new ArrayList<FSObject>();
        
        for(FSObject fsys : oldIndex)
            if(!newIndex.contains(fsys))
                deletes.add(fsys);
            else if(fsys instanceof FSFolder && !nochange.contains(fsys))
                nochange.add(fsys);
            else if(fsys instanceof FSFile)
            {
                FSFile A = ((FSFile)fsys);
                FSFile B = (FSFile)newIndex.get(newIndex.indexOf(fsys));

                ArrayList<FSObject> addto=null;
                
                if(A.getSign().equalsIgnoreCase(B.getSign()))
                    addto=nochange;
                else
                    addto=update;
                
                if(!addto.contains(fsys))
                    addto.add(fsys);
            }

        for(FSObject fsys : newIndex)
            if(!oldIndex.contains(fsys))
                creates.add(fsys);
            else if(fsys instanceof FSFolder && !nochange.contains(fsys))
                nochange.add(fsys);
        
        
        oldIndex=null;
        this.writeIndex(newIndex);
        newIndex=null;
        System.gc();
        
        
        
        
        
        for(FSObject fsys : creates)
            System.out.println("Creado: "+fsys.getName());
        
        for(FSObject fsys : deletes)
            System.out.println("Borrado: "+fsys.getName());
        
        for(FSObject fsys : update)
            System.out.println("Update: "+fsys.getName());

        for(FSObject fsys : nochange)
            System.out.println("Idem: "+fsys.getName());
        
        
    }
    
    private ArrayList<FSObject> readIndex()
    {
        ArrayList<File> fs = super.getFiles(tag);

        String data = new  String();
        
        for(File f : fs)
            data+=f.getString("")+"\n";
        
        data=data.replaceAll("\r", "\n");
        
        String[] data2 =data.split("\n");
        
        data = new  String();
        
        for(String line : data2)
            if(!line.isEmpty())
                data+=line+"\n";

        if(fs.size()>1)
        {    
        
            File newIndex = File.getTemp(tag);

            if(newIndex.setString(data))
                for(File f : fs)
                    f.delete();
        }
        
        ArrayList<FSObject> fsys=new ArrayList<FSObject>();
        for(String line : data.split("\n"))
        {
            String[] parts=line.split("/");
            
            if(parts.length>1)
            {
                if(parts[0].equalsIgnoreCase("Folder"))
                {
                    fsys.add(new FSFolder(parts[1]));
                }
                else if(parts[0].equalsIgnoreCase("File"))
                {
                    fsys.add(new FSFile(parts[1],parts[2]));
                }                
            }
        }
        return fsys;
    }
    private Boolean writeIndex(ArrayList<FSObject> index)
    {
        String data = new  String();
        
        for(FSObject fsys : index)
        {
            if(fsys instanceof FSFolder)
                data+="Folder/"+fsys.getName()+"\n";
            else if(fsys instanceof FSFile)                                
                data+="File/"+fsys.getName()+"/"+((FSFile)fsys).getSign()+"\n";

        }
        
        ArrayList<File> fs = super.getFiles(tag);
               
        File newIndex = new File(super.randomPath(this, ".", "."+tag));
        
        if(newIndex.setString(data))
        {
            for(File f : fs)
                f.delete();
            
            return true;
        }
        else 
            return false;        
    }
    
    private class FSObject
    {
        private String name;

        public FSObject(String name) 
        {
            this.name = name;
        }

        public String getName() 
        {
            return name;
        }
       
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final FSObject other = (FSObject) obj;
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }
            return true;
        }
    }
    private class FSFile extends FSObject
    {
        private String sign;

        public FSFile(String name,String sign) 
        {
            super(name);
            this.sign=sign;
        }

        public String getSign() 
        {
            return sign;
        }
    }
    private class FSFolder extends FSObject
    {
        public FSFolder(String name) 
        {
            super(name);
        }
        
    }
}
