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

package BLK.io;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */

public class Terminal 
{
    private static Terminal def=null;
    
    public static Terminal getDefault()
    {
        if(Terminal.def==null)
            Terminal.def=new Terminal(System.out, System.err, System.in);
        
        return Terminal.def;
    }
    
    
    private PrintStream stdout;
    private PrintStream stderr;
    private InputStream stdin;
    
    private Scanner scan;
    
    public Terminal(PrintStream stdout, PrintStream stderr, InputStream stdin) 
    {
        this.stdout = stdout;
        this.stderr = stderr;
        this.stdin = stdin;
        this.scan = new Scanner(this.stdin);
    }
    
    public void clear() 
    {
        for (int i = 0; i < 100; i++)         
            this.stdout.println();
        
        this.stdout.flush();
        Thread.currentThread().yield();
    }
   
    public String getString(String message) 
    {
        String s = null;
        while(s==null)
        {
            this.stdout.println();
            if (message!=null && !message.isEmpty()) 
                this.stdout.println(message);        

            s = this.scan.nextLine();

            if (s.isEmpty()) 
                s=null;
        }
        return s;
    }

    public void wait(String message) 
    {
        this.stdout.println();
        if (message!=null && !message.isEmpty()) 
            this.stdout.println(message + "...");
        
        this.scan.nextLine();
    }

    
    public Integer getInteger(String message) 
    {
        return this.getInteger(message, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    
    public Integer getInteger(String message, Integer min, Integer max)    
    {
        Integer i = null;
        while(i==null)
        {
            try 
            {
                i = Integer.valueOf(getString(message));
                if (i < min || i > max) 
                {
                    this.stdout.println("Out of range.");
                    i=null;
                }                
            } 
            catch (Exception ex) 
            {
                this.stdout.println("Is not a number.");
                i=null;
            }
        }
        return i;
    }

    
    public Integer askMenu(String title, ArrayList<String> items) 
    {
        printTitle(title);

        for (int i = 0; i < items.size(); i++)         
            this.stdout.println((i + 1) + ") " + items.get(i) + ".");
                
        return getInteger("Select an option.", 1, items.size());
    }

    
    public void printTitle(String title)
    {
        this.clear();
        
        String ast = "****";
        for (int i = 0; i < title.length(); i++) 
            ast += "*";
        
        
        this.stdout.println(ast);
        this.stdout.println(title);
        this.stdout.println(ast);
        this.stdout.println();
    
    }

    @Deprecated
    public Scanner getScanner() {return scan;}    
    @Deprecated
    public PrintStream getStderr() {return stderr;}    
    @Deprecated
    public InputStream getStdin() {return stdin;}   
    @Deprecated
    public PrintStream getStdout() {return stdout;}
    
            
    
}
