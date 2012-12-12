/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.FileSystem.XPath;

import BLK.io.FileSystem.File;
import BLK.io.FileSystem.Folder;

/**
 *
 * @author andresrg
 */
public class NewClass {
    
//    
//    public void archive(File path)
//    {
//        if(!path.exists())
//            throw new UnsupportedOperationException("File not exists");
//               
//        String ext=path.getExtension();
//        
//        if(ext!=null)
//        {
//            ext=ext.toLowerCase();
//            
//            if(ext.length()>this.prefix.length())
//                ext=ext.substring(0,this.prefix.length()-1);
//                        
//        }
//        
//        if(ext!=null && ext.equalsIgnoreCase(this.prefix))
//        {
//            
//        }
//        else
//        {
//            if(!path.isReadable())
//                throw new UnsupportedOperationException("File not is readable");
//
//            String id=path.getUid();
//            
//            if(id==null)
//                throw new UnsupportedOperationException("FAIL to calc UID");
//            
//            id=id.toUpperCase();
//            
//            Folder tmp=this.getPath(id);
//            
//            File raw=new File(root, id+"."+this.prefix+this.raw_sufix);
//            File dat=new File(root, id+"."+this.prefix+this.dat_sufix);
//            
//        }
//            
//            
//        
//        
//    }
//    
//    
//    
//    
//    
//    
//    
//    
//        public SList getList(String id, String list)
//    {
//        if(list.equalsIgnoreCase(this.raw_sufix))
//            throw new UnsupportedOperationException("Reserved list to raw data");
//                    
//        Folder path = this.getPath(id);
//        return new SList(new File(path, id.toUpperCase()+"."+this.prefix+list.toLowerCase()));
//    }
//    
//    public File getRaw(String id)
//    {
//        Folder path = this.getPath(id);
//        return new File(path, id.toUpperCase()+"."+this.prefix+this.raw_sufix);
//    }

}
