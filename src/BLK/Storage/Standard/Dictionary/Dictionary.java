/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BLK.Storage.Standard.Dictionary;

import BLK.Storage.IZone;

/**
 *
 * @author andresrg
 */
public class Dictionary
{
    private IZone dic;
    private Dictionary(IZone rootDB)
    {
        this.dic=rootDB.getSubZone("Standard").getSubZone("Dictionary");
    }
    public static Dictionary getDictionary(IZone rootDB)
    {
        return new Dictionary(rootDB);
    }
    public Word getWord(String word)
    {
        IZone z = this.dic;
        String tmp = word.toLowerCase();
        for(byte c : tmp.getBytes())
            z=z.getSubZone(String.valueOf(c));

        return Word.getWord(z);
    }
}

