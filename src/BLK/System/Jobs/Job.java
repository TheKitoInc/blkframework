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

package BLK.System.Jobs;

import BLK.System.Logger;
import BLK.System.Os.Process;
import BLK.System.Utils.ArrayList;


/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class Job extends ArrayList<String>{
    

    private String getBatch()
    {
        if (this.size()==0)
            return "exit 0";

        String tmp ="( ";

        for (String cmd : this)
        {
            if (!tmp.equalsIgnoreCase("( "))
                tmp+=" && ";
            
            tmp+="("+cmd+")";
        }

        tmp+=" && (exit 0) ) || exit 1";

        return tmp;
    }

    public boolean execute()
    {
        Logger.getLogger().debug("Trying to execute system job");
        return Process.basicCall(getBatch());
    }
}
