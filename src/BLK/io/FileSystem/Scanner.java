/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.FileSystem;

import BLK.System.Quere;
import BLK.System.Threads.IterativeThread;
import java.util.ArrayList;

/**
 *
 * @author andresrg
 */
public class Scanner extends IterativeThread
{
    private ArrayList<Folder> folders = new ArrayList<Folder>();
    private ArrayList<File> files = new ArrayList<File>();

    private Quere<FileSystem> q;
    
    public Scanner(Folder target, Quere<FileSystem> q) 
    {
        super("Scanner: "+target.getPath());
        this.folders.add(target);
        this.q=q;
    }
    
    
    @Override
    protected void doIteration() 
    {                
        if(!files.isEmpty())
        {
            File f = files.remove(0);
            try
            {
                this.q.write(f);
            }
            catch(InterruptedException ex)
            {
                throw new UnsupportedOperationException(ex.getCause());
            }
        }
        else
        {
            if(!folders.isEmpty())
            {
                Folder f = folders.remove(0);
                
                this.files=f.getFiles();
                
                for(Folder fo : f.getFolders()) 
                {
                    this.folders.add(fo);
                }
                
                try
                {
                    this.q.write(f);
                }
                catch(InterruptedException ex)
                {
                    throw new UnsupportedOperationException(ex.getCause());
                }
            }
            else
            {
                super.stop();
            }
        }        
    }
    
}
