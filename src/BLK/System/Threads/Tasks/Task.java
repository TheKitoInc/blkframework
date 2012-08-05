/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BLK.System.Threads.Tasks;

import java.util.ArrayList;

/**
 *
 * @author andresrg
 */
public abstract class Task extends BLK.System.Threads.Thread
{
    private static ArrayList<Task> tasks = new ArrayList<Task>();
    public enum Status{Waiting,Running,Finish,Fail};
    private Status s=Status.Waiting;
    protected Task(String name) {super("Task: "+name);tasks.add(this);super.start();}

    @Override
    protected void doThread()
    {
        this.s=Status.Running;
        if(doTask())
        {
            this.s=Status.Finish;
        }
        else
        {
            this.s=Status.Fail;            
        }
    }

    protected abstract Boolean doTask();

    public Status getStatus() {return s;}
}
