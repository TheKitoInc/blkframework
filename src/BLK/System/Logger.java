package BLK.System;

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



import BLK.System.Utils.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;



/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Logger implements ILogger{
    private Thread thisThread=null;
    private String loggerModule=null;
    protected static String separator=" | ";
    private static ILogger defaultLogger =null;
    public static PrintStream consoleOut=System.err;
    public static enum levels {FATAL, ERROR, WARN, INFO, DEBUG}
    private levels level=levels.INFO;
    private String logPath="/tmp/log/";
    private Logger(Thread thread,String lModule){this.thisThread=thread;this.loggerModule=lModule;}

    /*
        public void Logger(StringOLD lModule){this.Logger(Thread.currentThread(),lModule);}
        public void Logger(){this.thisThread=Thread.currentThread();}
     */

    public void debug(Exception ex){this.debug(new String(),ex);}
    public void fatal(Exception ex){this.fatal(new String(),ex);}
    public void warn(Exception ex){this.warn(new String(),ex);}
    public void error(Exception ex){this.error(new String(),ex);}
    public void info(Exception ex){this.info(new String(),ex);}

    public void debug(String message){this.debug(message,null);}
    public void fatal(String message){this.fatal(message,null);}
    public void warn(String message){this.warn(message,null);}
    public void error(String message){this.error(message,null);}
    public void info(String message){this.info(message,null);}

    public void debug(String message,int number){this.debug(message+":"+String.valueOf(number));}
    public void fatal(String message,int number){this.fatal(message+":"+String.valueOf(number));}
    public void warn(String message,int number){this.warn(message+":"+String.valueOf(number));}
    public void error(String message,int number){this.error(message+":"+String.valueOf(number));}
    public void info(String message,int number){this.info(message+":"+String.valueOf(number));}

    public void debug(String message,Exception ex){if(level!=levels.DEBUG) return;                                                                                              lineOut(levels.DEBUG, "DEBUG" + separator + threadToString(Thread.currentThread()) + separator + message + separator + exceptionToString(ex));}
    public void info(String message,Exception ex){if(level!=levels.DEBUG && level!=levels.INFO) return;                                                                         lineOut(levels.INFO, "INFO "   + separator + threadToString(Thread.currentThread()) + separator + message + separator + exceptionToString(ex));}
    public void warn(String message,Exception ex){if(level!=levels.DEBUG && level!=levels.INFO && level!=levels.WARN) return;                                                   lineOut(levels.WARN, "WARN "   + separator + threadToString(Thread.currentThread()) + separator + message + separator + exceptionToString(ex));}
    public void error(String message,Exception ex){if(level!=levels.DEBUG && level!=levels.INFO && level!=levels.WARN && level!=levels.ERROR) return;                           lineOut(levels.ERROR, "ERROR" + separator + threadToString(Thread.currentThread()) + separator + message + separator + exceptionToString(ex));}
    public void fatal(String message,Exception ex){if(level!=levels.DEBUG && level!=levels.INFO && level!=levels.WARN && level!=levels.ERROR && level!=levels.FATAL) return;    lineOut(levels.FATAL, "FATAL" + separator + threadToString(Thread.currentThread()) + separator + message + separator + exceptionToString(ex));}
    
    
    

    public static String threadToString(Thread th)
    {
        if (th!=null)
            return th.toString().concat(getStackTrace(Thread.currentThread().getStackTrace()));
        else
            return "?";
    }
    private Long time=new Long(0);
    public void lineOut(levels thislevel,String message){
        //if(!BLK.System.Utils.ArrayList.isIn(threadsToLog, Thread.currentThread()))
          //  threadsToLog.add(Thread.currentThread());

        String textLine=(new java.util.Date()).toString() + separator + (System.currentTimeMillis()-time)+ "ms" + separator + message;
        autolog(thislevel,textLine);
        consoleOut.println(textLine);

        time=System.currentTimeMillis();
    }
    private static String getStackTraceElement(StackTraceElement ste)
    {
        if(ste!=null)
            return  ste.getClassName()+"."+ste.getMethodName() + ":" + ste.getLineNumber();
        else
            return "?";
    }
    private static StackTraceElement getStackTraceAnt(StackTraceElement[] ste)
    {   
        if (ste!=null)
        {
            boolean flag=false;
            boolean thisC=false;
            for (StackTraceElement e : ste)
            {
                thisC=e.getClassName().endsWith(Logger.class.getName());

                if(flag && !thisC)
                    return (e);

                if(thisC)
                    flag=true;

            }

            return null;
        }
        else
            return null;
    }
    private static String getStackTrace(StackTraceElement[] ste)
    {   
        if (ste!=null)
            return getStackTraceElement(getStackTraceAnt(ste));
        else
            return "?";
    }
    public static String exceptionToString(Exception ex)
    {
        if (ex!=null)
            return "("+ex.getMessage()+")" + separator + (getStackTrace(ex.getStackTrace()));
        else
            return "?";
    }
    public static ILogger getLogger()
    {
        if (defaultLogger==null)
            defaultLogger=new Logger(Thread.currentThread(),"DefaultLogger");

//        try
//        {
//            FileSystemTrans trans = new FileSystemTrans();
//            trans.addOperation(FileSystemTrans.operations.create, new Folder("/tmp/"), null);
//            trans.doIt();
//        }
//        catch (FileSystemException ex)
//        {
//            System.err.println(ex.getMessage());
//        }

        (new File("/tmp")).mkdir();
        (new File("/tmp/")).mkdir();

        return defaultLogger;

    }

    private void autolog(levels thislevel,String text)
    {
        try {
            String fileName = getStackTraceAnt(Thread.currentThread().getStackTrace()).getClassName() + "." + thislevel.name();


            File logBase = new File(logPath);
            logBase.mkdirs();

            File f = new File(logBase.getAbsolutePath() + "/" + fileName);
            if (!f.exists())
                f.createNewFile();

            FileWriter fw = new FileWriter(f,true);

            

            fw.write(text+ BLK.System.Utils.StringOLD.lineSeparator());
            fw.flush();
            fw.close();
            System.gc();
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static ArrayList<Thread> threadsToLog=new ArrayList<Thread>();
    public static void addThreadToLog(Thread t){threadsToLog.add(t);}


    public static void loop() {
        
        
            String info=new String();
            for (Thread t : threadsToLog)
                info+=t.getName() + ">" + getStackTraceElement(t.getStackTrace()[0])+BLK.System.Utils.StringOLD.lineSeparator();

            BLK.System.Threads.Thread.sleepThread(10);
        

    }



}
