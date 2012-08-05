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
public class Word
{

    IZone word;
    private Word(IZone word)
    {
        this.word=word;
    }
    public static Word getWord(IZone word)
    {
        return new Word(word);
    }

    public Word[] getSubWords()
    {
        throw new UnsupportedOperationException();
    }

}
