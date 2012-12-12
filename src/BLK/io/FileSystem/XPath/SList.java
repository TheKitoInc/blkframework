/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BLK.io.FileSystem.XPath;

import BLK.io.FileSystem.File;

/**
 *
 * @author andresrg
 */
public class SList 
{
    private File data;

    public SList(File data) 
    {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SList{" + "data=" + data + '}';
    }
}
