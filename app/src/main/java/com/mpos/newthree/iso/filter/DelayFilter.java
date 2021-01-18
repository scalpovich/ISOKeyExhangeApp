/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2016 Alejandro P. Revilla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mpos.newthree.iso.filter;

import com.mpos.newthree.core.Configurable;
import com.mpos.newthree.core.Configuration;
import com.mpos.newthree.iso.ISOChannel;
import com.mpos.newthree.iso.ISOFilter;
import com.mpos.newthree.iso.ISOMsg;
import com.mpos.newthree.util.LogEvent;


/**
 * DelayFilter can be used in order to
 * slow down an ISOChannel. Usefull while
 * debugging an application or simulating a server
 */
public class DelayFilter implements ISOFilter, Configurable {
    int delay;
    public DelayFilter() {
        super();
        delay = 0;
    }
   /**
    * @param delay desired delay, expressed in milliseconds
    */
    public DelayFilter(int delay) {
        super();
        this.delay = delay;
    }
   /**
    * @param cfg
    * <ul>
    * <li>delay (expressed in milliseconds)
    * </ul>
    */
    public void setConfiguration (Configuration cfg) {
        delay = cfg.getInt ("delay");
    }

    public ISOMsg filter (ISOChannel channel, ISOMsg m, LogEvent evt) 
    {
        evt.addMessage ("<delay-filter delay=\""+delay+"\"/>");
        if (delay > 0) {
            try {
                Thread.sleep (delay);
            } catch (InterruptedException e) { }
        }
        return m;
    }
}

