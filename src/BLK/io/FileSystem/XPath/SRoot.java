/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.FileSystem.XPath;

import BLK.System.Utils.Hash.MD5;
import BLK.io.FileSystem.File;
import BLK.io.FileSystem.Folder;
import java.util.ArrayList;

/**
 *
 * @author andresrg
 */
public class SRoot 
{
    private int seg=4;
    
    private String prefix;
    private Folder root;

    public SRoot(String prefix, Folder root) 
    {
        this.prefix = prefix.toLowerCase();        
        this.root = root;
    }
    
    private Folder getPath(String id)
    {
        String hash = id.toUpperCase();
        hash=MD5.calc(hash).toUpperCase();
        
        Folder tmp=this.root;
        
        for(int i = 0 ; i < seg ; i++)
        {
            String part = hash.substring(i, i+1);
            tmp=new Folder(tmp, part);
        }
        
        tmp=new Folder(tmp, hash.substring(seg));
        
        return tmp;
    }
    
    public ArrayList<File> getFiles(String id)
    {
        ArrayList<File> tmp = new ArrayList<File>();
        
        Folder path = this.getPath(id);
        
        if(path.exists())
        {
            ArrayList<File> lst = path.getFiles(null, id.toUpperCase()+"."+this.prefix, null);
            
            if(lst!=null)
                for (File f : lst)
                    if(f!=null)                        
                        tmp.add(f);
 
        }

        
        return tmp;
    }
    
    public File getFile(String id, String sufix)
    {
        return new File(this.getPath(id),  id.toUpperCase()+"."+this.prefix+sufix);        
    }
    
    public long getFreeSize()
    {
        return this.root.getFreeSize();
    }
    
}
