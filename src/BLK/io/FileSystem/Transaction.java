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

import BLK.io.FileSystem.Core.File;
import BLK.io.FileSystem.Core.Folder;
import BLK.System.Logger;
import BLK.System.Utils.ArrayList;


/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */

public class Transaction extends BLK.System.Threads.Thread
{

    private Folder temp;

    @Override
    protected void doThread() {doIt();}
    public Transaction(Folder temp) {super("Transaction"+BLK.System.Utils.Math.getUID());this.temp=temp;}
    public Transaction(){this(Folder.temp);}

    public static enum operations{move,copy,delete,create}
    public static enum status {onTail,Running,Finish,Error}
    private ArrayList<TransactionOperation> oTail = new ArrayList<TransactionOperation>();
    private status stat=status.onTail;

    protected ArrayList<TransactionOperation> getoTail() {return oTail;}
    public status getStatus() {return stat;}
    public void addOperation(operations o,FileSystem source, FileSystem destination) throws FileSystemException
    {
        //Valid params
        if (o==null)
            throw new FileSystemException("Operation can not by set to null");
        
        if (source==null)
            throw new FileSystemException("Source can not by set to null");

        if (!source.exists() && o!=operations.create)
            if (o==operations.delete)
            {
                Logger.getLogger().warn("UPS! try to add delete operation with not exist source");
                return ;
            }
            else
            {
                boolean flag=false;
                for (TransactionOperation or : oTail)
                    if (or.getDestination()!=null && or.getDestination().equals(source))
                        flag=true;

                if (!flag)
                    throw new FileSystemException("Source not exist or not is destination of other operations in this transaction");
            }

        if (destination==null && (o!=operations.delete && o!=operations.create))
            throw new FileSystemException("Destination can not by set to null");
                
        if (source instanceof Folder)
            if (destination instanceof File)
                throw new FileSystemException("Folder can not input in file");

        if ((o==operations.copy || o==operations.move) && destination.exists() && destination instanceof File)
        {
            Boolean ok=false;
            for (TransactionOperation ooo : oTail)
                if(ooo.getO()==operations.delete || ooo.getO()==operations.move)
                    if(ooo.getSource().equals(destination))
                        ok=true;

            if(!ok)
                throw new FileSystemException("Can not copy or move on a destination file");
        }

        FileSystem destination2=destination;
        if (source instanceof File)
            if (destination instanceof Folder)
                destination2=new File(destination.getPath() + "/" + source.getName());
        

        //add extra operations
        if (o==operations.copy || o==operations.move || o==operations.create)
        {
            FileSystem u=null;
            if (o==operations.copy || o==operations.move)
                u=destination2;
            else if (o==operations.create)
                u=source;


            //create parent folders to COPY MOVE CREATE
            Folder f= u.getParent();
            if (!f.exists())
                addOperation(operations.create, f, null);

        }

        //Add operations to MOVE COPY Folders
        if (source instanceof Folder && (o==operations.copy || o==operations.move))
        {
            for (File ff : ((Folder)source).getFiles())
                addOperation(o, ff, new File(destination.getPath() + "/" + source.getName()));

            for (Folder fo : ((Folder)source).getFolders())
                addOperation(o, fo, new Folder(destination.getPath() + "/" + source.getName()));
            
            return;
        }




        // create operation
        TransactionOperation op=new TransactionOperation(o, source, destination2);

        // verify if exists
        for (TransactionOperation ooo : oTail)
            if (ooo.equals(op))
                return ;

        //add operation
        oTail.add(op);
        Logger.getLogger().debug("Add:" + op.toString());

    }


    public boolean doIt()
    {
        if (oTail.size()==0)
            return false;
        
        this.stat=status.Running;
        for (TransactionOperation o : oTail)
        {
            o.setStatus(status.Running);
            if (doItOne(o))
                o.setStatus(status.Finish);
            else
            {
                o.setStatus(status.Error);
                this.stat=status.Error;
                Logger.getLogger().error("FileSystem Trans ERROR!!!!!!!",new FileSystemException(o.toString()));
                abort();
                return false;
            }
        }

        for (TransactionOperation o : oTail)
            if (o.getO()==operations.delete)
                o.getDestination().delete();
        
        this.stat=status.Finish;
        System.gc();
        return true;
    }
//    public boolean doItJob()
//    {
//        Job main = new Job();
//        this.stat=status.Running;
//        for (operation o : oTail)
//        {
//            o.setStatus(status.Running);
//            Logger.getLogger().debug("FileSystem Trans: " + o.toString());
//
//            if (o.getO()==null)
//                return false;
//            else if (o.getO()==operations.copy)
//                main.add("cp -rv \"" + o.getSource().getAbsolutePath() + "\" \"" + o.getDestination().getAbsolutePath() + "\"");
//            else if (o.getO()==operations.move)
//                main.add("mv -v \"" + o.getSource().getAbsolutePath() + "\" \"" + o.getDestination().getAbsolutePath() + "\"");
//            else if (o.getO()==operations.delete)
//            {
//                if (o.getSource() instanceof File)
//                    o.setDestination(File.getTempFile("rback"));
//                else if (o.getSource() instanceof Folder)
//                    o.setDestination(Folder.getTempFolder("rback"));
//
//                main.add("mv -v \"" + o.getSource().getAbsolutePath() + "\" \"" + o.getDestination().getAbsolutePath() + "\"");
//            }
//            else if (o.getO()==operations.create)
//                if(o.getSource() instanceof File)
//                    main.add("touch \"" + o.getSource().getAbsolutePath() + "\"");
//                else if(o.getSource() instanceof Folder)
//                    main.add("mkdir \"" + o.getSource().getAbsolutePath() + "\"");
//                else
//                    return false;
//            else
//                return false;
//
//        }
//
//        Job clear = new Job();
//        for (operation o : oTail)
//            if (o.getO()==operations.delete)
//                clear.add("rm -rvf \""+o.getDestination().getAbsolutePath()+"\"");
//
//
//        if (main.execute())
//        {
//            clear.execute();
//            this.stat=status.Finish;
//        }
//        else
//        {
//            abort();
//            this.stat=status.Error;
//        }
//
//        System.gc();
//        return true;
//
//
//    }
    private boolean doItOne(TransactionOperation o)
    {
        Logger.getLogger().debug("FileSystem Trans: " + o.toString());
        if (o.getO()==null)
            return false;
        else if (o.getO()==operations.copy)
            return o.getSource().copy(o.getDestination());        
        else if (o.getO()==operations.move)
            return o.getSource().move(o.getDestination());
        else if (o.getO()==operations.delete)
        {
            if (o.getSource() instanceof File)
                o.setDestination(File.getTemp(this.temp,"rback"));
            else if (o.getSource() instanceof Folder)
                o.setDestination(Folder.getTemp(this.temp,"rback"));

            return o.getSource().move(o.getDestination());
        }
        else if (o.getO()==operations.create)
            return o.getSource().create();
        else
            return false;
    }
    private void abort()
    {

        for (int i=oTail.size()-1;i>=0;i--)
        {
            TransactionOperation o = oTail.get(i);
            if (o.getStatus()==status.Finish || o.getStatus()==status.Running)
            {
                operations io=null;

                if (o.getO()==null)
                    io=null;
                else if (o.getO()==operations.copy)
                    io=operations.delete;
                else if (o.getO()==operations.move)
                    io=o.getO();
                else if (o.getO()==operations.delete)
                    io=operations.move;
                else if (o.getO()==operations.create)
                    io=operations.delete;

                TransactionOperation inverO=null;
                if (o.getDestination()!=null)
                    inverO=new TransactionOperation(io, o.getDestination(), o.getSource());
                else
                    inverO=new TransactionOperation(io, o.getSource(), null);

                if (!doItOne(inverO))
                    Logger.getLogger().error("FileSystem Rollback ERROR!!!!!!!",new FileSystemException(o.toString()));
            }
        }
    }


    
}
