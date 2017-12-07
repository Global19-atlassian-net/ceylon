/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.eclipse.ceylon.ant;

import java.io.File;

/**
 * Represents a module with a name and optionally a version.
 * @author tom
 */
@AntDoc("A `<module>` element must specify a name, and may specify a version. "
        + "If the relevant ceylon task don't require a version it will be ignored.")
public class Module {
    
    private String name;
    
    private String version;
    
    public Module() {
        this(null);
    }
    
    public Module(String name) {
        this(name, null);
    }
    
    public Module(String name, String version) {
        super();
        setName(name);
        setVersion(version);
    }
    
    public static Module fromSpec(String spec) {
        int index = spec.indexOf('/');
        String name;
        String version;
        if (index != -1) {
            name = spec.substring(0, index);
            version = spec.substring(index + 1);
        } else {
            name = spec;
            version = null;
        }
        return new Module(name, version);
    }

    public String getVersion() {
        return version;
    }
    
    @AntDoc("The module version. Whether this is required depends on the "
            + "task")
    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }
    
    @AntDoc("The module name.")
    @Required
    public void setName(String name) {
        this.name = name;
    }
    
    public String toSpec() {
        if (version == null) {
            return name;
        }
        return name + "/" + version;
    }
    
    public String toVersionlessSpec() {
        return name;
    }
    
    public String toVersionedSpec() {
        if (version == null) {
            throw new RuntimeException("Module " + name + " doesn't specify a version");
        }
        return name + "/" + version;
    }

    @Override
    public String toString() {
        return toSpec();
    }
    
    public File toDir() {
        return new File(name.replace(".", "/"));
    }
    
    public File toVersionedDir() {
        if (version == null) {
            throw new RuntimeException("Module " + name + " doesn't specify a version");
        }
        return new File(toDir(), version);
    }
}
