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

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Filter implements FilenameFilter {

    private Integer limit;
    private Boolean container;
    private String preFix;
    private String subFix;

    private Filter(Integer limit, Boolean container, String preFix, String subFix) {
        this.limit = limit;
        this.container = container;
        this.preFix = preFix;
        this.subFix = subFix;
    }

    public static Filter getFolderFilter(Integer limit, String preFix, String subFix){return new Filter(limit, true, preFix, subFix);}
    public static Filter getFileFilter(Integer limit, String preFix, String subFix){return new Filter(limit, false, preFix, subFix);}

    public static Filter getFolderFilter(Integer limit, String subFix){return new Filter(limit, true, null, subFix);}
    public static Filter getFileFilter(Integer limit, String subFix){return new Filter(limit, false, null, subFix);}

    public static Filter getFolderFilter(String preFix, String subFix){return new Filter(-1, true, preFix, subFix);}
    public static Filter getFileFilter(String preFix, String subFix){return new Filter(-1, false, preFix, subFix);}

    public static Filter getFolderFilter(String subFix){return new Filter(-1, true, null, subFix);}
    public static Filter getFileFilter(String subFix){return new Filter(-1, false, null, subFix);}

    public boolean accept(File dir, String name)
    {

        if(this.limit!=null && this.limit==0)
            return false;

        File obj =new File(dir.getAbsolutePath() + File.separator + name);

        if(this.container==true &&  !obj.isDirectory())
            return false;

        if(this.container==false &&  !obj.isFile())
            return false;
        
        if(this.preFix!=null && name.length()>this.preFix.length() && !name.substring(0,this.preFix.length()).equalsIgnoreCase(this.preFix))
            return false;
        
        if(this.subFix!=null && name.length()>this.subFix.length() && !name.substring(name.length()-this.subFix.length(),name.length()).equalsIgnoreCase(this.subFix))
            return false;

        if(this.limit!=null)
            this.limit--;
        
        return true;

    }



}
