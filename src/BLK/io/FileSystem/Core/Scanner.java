/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.FileSystem.Core;

import BLK.System.Quere;
import BLK.System.Threads.IterativeThread;
import java.util.ArrayList;

/**
 *
 * @author andresrg
 */
public class Scanner extends IterativeThread
{
    private Folder base;
    private Quere<FileSystem> quere;
    private ArrayList<Folder> list;

    private boolean addFile;
    private boolean addFolder;

    private int size;
    
    public Scanner(Folder base, int size,boolean addFile, boolean addFolder) 
    {
        super("Scanner: "+base.getPath());
        this.base = base;
        this.size = size;
        this.addFile = addFile;
        this.addFolder = addFolder;
    }

    @Override
    protected void doIteration() 
    {
        if(this.list.isEmpty())
            this.list.add(this.base);
        
        Folder f = this.list.remove(0);        
        
        if(addFile)
            for (File fi : f.getFiles())
                quere.write(fi);
        
        if(addFolder)
            quere.write(f);
        
        for (Folder fo : f.getFolders())        
            list.add(fo);
                
    }

    public FileSystem get() 
    {
        return this.quere.read();          
    }

    @Override
    protected boolean init() 
    {
        this.quere = new Quere<FileSystem>(size);
        this.list = new ArrayList<Folder>();
        System.gc();
        return super.init();
    }
       
    @Override
    public void start() {super.start();}
    @Override
    public void stop() {super.stop();}
    @Override
    public void pause() {super.pause();}
    @Override
    public boolean isStopped() {return super.isStopped();}
    @Override
    public boolean isStopping() {return super.isStopping();}
    @Override
    public boolean isRunning() {return super.isRunning();}
    @Override
    public boolean isPausing() {return super.isPausing();}
    @Override
    public boolean isPaused() {return super.isPaused();}

    public void setAddFile(boolean addFile) {this.addFile = addFile;}
    public void setAddFolder(boolean addFolder) {this.addFolder = addFolder;}

    
    
}
