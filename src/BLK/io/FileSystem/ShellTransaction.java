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

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class ShellTransaction extends Transaction
{
    Folder fo;
    public ShellTransaction(){this(Folder.temp);}
    public ShellTransaction(Folder temp)
    {
        super(temp);
        fo = new Folder(temp, "Transactions");
        fo.create();
    }

    @Override
    protected void doThread() {this.doIt();}

    @Override
    public boolean doIt()
    {
        String script=this.getScript();

        if(script!=null)
        {
            File f = new File(fo,BLK.System.Utils.Math.getUID()+".sh");
            f.setString(script);
            f.setExecutable(Boolean.TRUE);
            if(BLK.System.Os.Process.basicCall(f.getPath()))
            {
                f.delete();
                for (TransactionOperation o : super.getoTail())
                    if (o.getO()==operations.delete)
                        o.getDestination().delete();
                return true;
            }


        }



        return super.doIt();
    }


    private  String getScript()
    {

        if(super.getoTail().isEmpty())
            return null;

        String doCommand="";
        String doUndoCommand="";

        for(TransactionOperation to : super.getoTail())
        {
            if(!doCommand.isEmpty())
                doCommand+=" && \\\n";

            doCommand+=getCommand(to);


            if(!doUndoCommand.isEmpty())
                doUndoCommand="\n"+doUndoCommand;

            doUndoCommand=getUndoCommand(to)+doUndoCommand;
        }

        doCommand="doTran () {\n("+doCommand+" && return 0) || return 1\n}";
        doUndoCommand="undoTran () {\n"+doUndoCommand+"\n}";
        String script="#!/bin/sh"+"\n\n\n\n\n\n";
        script+=doCommand+"\n\n\n\n\n\n";
        script+=doUndoCommand+"\n\n\n\n\n\n";
        script+="doTran\nif [ $? = 1 ]; then\nundoTran\nexit 1\nfi\nexit 0";

        
        
        return script;
    }

    private String getCommand(TransactionOperation to)
    {
        operations o = to.getO();

        if(o==operations.create)
            return to.getSource().getCreateCommand();
        else if(o==operations.delete)
        {
            if (to.getSource() instanceof File)
                to.setDestination(File.getTemp("rback"));
            else if (to.getSource() instanceof Folder)
                to.setDestination(Folder.getTemp("rback"));

            return to.getSource().getMoveCommand(to.getDestination());
        }
        else if(o == operations.move)
            return to.getSource().getMoveCommand(to.getDestination());
        else if(o == operations.copy)
            return to.getSource().getCopyCommand(to.getDestination());
        else
            return null;
    }
    private String getUndoCommand(TransactionOperation to)
    {


        operations io=null;

        if (to.getO()==null)
            io=null;
        else if (to.getO()==operations.copy)
            io=operations.delete;
        else if (to.getO()==operations.move)
            io=operations.move;
        else if (to.getO()==operations.delete)
            io=operations.move;
        else if (to.getO()==operations.create)
            io=operations.delete;

        TransactionOperation inverO=null;
        if (to.getDestination()!=null)
            inverO=new TransactionOperation(io, to.getDestination(), to.getSource());
        else
            inverO=new TransactionOperation(io, to.getSource(), null);

        return getCommand(inverO);

    }
}
