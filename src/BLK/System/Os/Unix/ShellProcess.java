/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.System.Os.Unix;

import BLK.io.FileSystem.Core.File;


/**
 *
 * @author andresrg
 */
public class ShellProcess extends BLK.System.Os.Process
{
    private static File getShellScript(String command,String shell)
    {
        File tmp=File.getTemp("sh");
        
        String script="#!"+shell+"\n";
        script=script.concat(command+"\n");
        script=script.concat("exit $?\n");

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
