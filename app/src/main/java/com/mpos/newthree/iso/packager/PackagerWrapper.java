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

package com.mpos.newthree.iso.packager;

import com.mpos.newthree.core.Configurable;
import com.mpos.newthree.core.Configuration;
import com.mpos.newthree.core.ConfigurationException;
import com.mpos.newthree.iso.ISOComponent;
import com.mpos.newthree.iso.ISOException;
import com.mpos.newthree.iso.ISOPackager;
import com.mpos.newthree.util.LogSource;
import com.mpos.newthree.util.Logger;

/**
 * Wrapper on standard packager
 * @author bharavi gade
 * @version $Revision$ $Date$
 * @see ISOPackager
 */
public abstract class PackagerWrapper 
    implements ISOPackager, LogSource, Configurable
{
    protected Logger logger = null;
    protected String realm = null;
    protected ISOPackager standardPackager = null;
    protected Configuration cfg;

    public PackagerWrapper() {
        super();
    }

    public abstract byte[] pack (ISOComponent c) throws ISOException;

    public abstract int unpack (ISOComponent c, byte[] b) throws ISOException;
    
    public String getFieldDescription(ISOComponent m, int fldNumber) {
        return standardPackager != null ? 
            standardPackager.getFieldDescription (m, fldNumber) : "";
    }
    public void setPackager(ISOPackager packger)
    {
        this.standardPackager=packger;
    }
    public ISOPackager getPackager()
    {
        return standardPackager;
    }

    public void setLogger (Logger logger, String realm) {
        this.logger = logger;
        this.realm  = realm;
        if (standardPackager instanceof LogSource)
            ((LogSource) standardPackager).setLogger (logger, realm);
    }
    public String getRealm () {
        return realm;
    }
    public Logger getLogger() {
        return logger;
    }
    /**
     * requires <code>inner-packager</code> property
     * @param cfg Configuration object
     * @throws ConfigurationException
     */
    public void setConfiguration (Configuration cfg) 
        throws ConfigurationException
    {
        this.cfg = cfg;
        String packagerName = cfg.get ("inner-packager");
        try {
            Class p = Class.forName(packagerName);
            setPackager ((ISOPackager) p.newInstance());
            if (standardPackager instanceof Configurable)
                ((Configurable)standardPackager).setConfiguration (cfg);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException ("Invalid inner-packager", e);
        } catch (InstantiationException e) {
            throw new ConfigurationException ("Invalid inner-packager", e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException ("Invalid inner-packager", e);
        }
    }
}
