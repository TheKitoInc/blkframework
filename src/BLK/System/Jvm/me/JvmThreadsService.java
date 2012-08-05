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

package BLK.System.Jvm.me;
import java.util.Enumeration;
import BLK.System.Utils.Hashtable;
import BLK.System.Logger;
import java.lang.Thread;
/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class JvmThreadsService implements java.lang.Runnable{
    private static Hashtable<Runnable,Thread> services=new Hashtable<Runnable,Thread>();
    public static void addService(Runnable newService,String serviceName){services.put(newService,new Thread(new JvmThreadsService(newService),serviceName) {});}

    public static boolean existService(String serviceName) {return existService(getServiceByName(serviceName));}


    public static Runnable getServiceByName(String serviceName)
    {
        Enumeration<Runnable> en =services.keys();
        while(en.hasMoreElements())
        {
            Runnable ir=en.nextElement();
            if(services.get(ir).getName().equalsIgnoreCase(serviceName))
                return ir;
        }
        return null;
    }
    public static boolean existService(Runnable service) {if (service !=null)return services.containsKey(service); else return false;}

    public static boolean stop(String serviceName, boolean confirm) {return stop(getServiceByName(serviceName),confirm);}

    private static boolean stop(Runnable service, boolean confirm) {
        if (confirm)
        {
            while (service.endThread()==false){}
            return true;
        }
        else
        {
            service.endThread();
            return true;
        }

    }



    private Runnable newService;
    private JvmThreadsService(Runnable newService){this.newService=newService;}
    public void run(){this.newService.run();}


    /**
     * Start the server.
     */
    public static boolean start(Runnable service){return runThread(getThread(service));}
    private boolean start(){return start(this.newService);}
    /**
     * Stop the server. Stop the listener thread.
     */
    //public static void stop(Runnable service){getThread(service).stop();}
    //private void stop(){stop(this.newService);}
    /**
     * Get the server status.
     * @return true if the server is stopped
     */
    //static boolean isStopped(Runnable service){return false;}
    //boolean isStopped(){return isStopped(this.newService);}
    /**
     * Suspend further requests
     */
    public static void suspend(Runnable service){getThread(service).interrupt();}
    private void suspend(){suspend(this.newService);}
    /**
     * Resume the server handler
     */
    //public static void resume(Runnable service){getThread(service).resume();}
    //private void resume(){resume(this.newService);}
    /**
     * Is the server suspended
     * @return true if the server is suspended
     */
    public static boolean isSuspended(Runnable service){return getThread(service).isInterrupted();}
    private boolean isSuspended(){return isSuspended(this.newService);}


    private static Thread getThread(Runnable service){return (Thread)services.get(service);}
    public static String getName(Runnable service){return getThread(service).getName();}

    private static boolean runThread(Thread t)
    {
        try {
            t.run();
            return true;
        } catch (Exception ex) {
            Logger.getLogger().error(t.getName() + " Launch Error",ex);
            return false;
        }
    }

   // public static boolean sleepThread(long millis){Thread.yield();return sleepThread(Thread.currentThread(), millis);}
    public static boolean sleepThread(Runnable service,long millis){return sleepThread(getThread(service),millis);}
    private static boolean sleepThread(Thread t,long millis)
    {
        try {
            if (t==Thread.currentThread())
                Thread.sleep(millis);
            else
                t.wait(millis);
            return true;
        } catch (InterruptedException ex) {
            Logger.getLogger().warn("Thread Wait", ex);
            return false;
        }

    }


    public static void controlStep()
    {
        Logger.getLogger().debug("Start default Control Step");
        Thread.yield();
        
        Enumeration e=services.keys();
        while (e.hasMoreElements())
            ((Runnable)e.nextElement()).controlStep();
        
        Thread.yield();
        //Logger.getLogger().info("Default Control Step");
        Logger.getLogger().debug("End default Control Step");
        sleepThread(Thread.currentThread(),100);
    }

}
