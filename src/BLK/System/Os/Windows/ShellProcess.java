/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.System.Os.Windows;

import BLK.io.FileSystem.Core.File;


/**
 *
 * @author andresrg
 */
public class ShellProcess extends BLK.System.Os.Process
{
    private static File getShellScript(String command,String shell)
    {
        File tmp=File.getTemp("bat");
        
        String script="#!"+shell+"\n\r";
        script=script.concat(command+"\n\r");
        script=script.concat("exit /b %ERRORLEVEL%\n\r");

        tmp.setString(script);
        tmp.setExecutable(true);
        
        return tmp;
    }
    public ShellProcess(String command) 
    {
        this(command,"/bin/sh");
    }
    public ShellProcess(String command,String shell) 
    {
        super(getShellScript(command, shell).getPath());
    }    
}
