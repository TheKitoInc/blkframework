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

package BLK.System;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public interface ILogger {
    public void debug(Exception ex);
    public void fatal(Exception ex);
    public void warn(Exception ex);
    public void error(Exception ex);
    public void info(Exception ex);

    public void debug(String message);
    public void fatal(String message);
    public void warn(String message);
    public void error(String message);
    public void info(String message);

    public void debug(String message,int number);
    public void fatal(String message,int number);
    public void warn(String message,int number);
    public void error(String message,int number);
    public void info(String message,int number);

    public void debug(String message,Exception ex);
    public void info(String message,Exception ex);
    public void warn(String message,Exception ex);
    public void error(String message,Exception ex);
    public void fatal(String message,Exception ex);

}
