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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.eclipse.ceylon.common.Constants;
import org.eclipse.ceylon.common.ModuleDescriptorReader;
import org.eclipse.ceylon.common.ModuleDescriptorReader.NoSuchModuleException;
import org.eclipse.ceylon.launcher.CeylonClassLoader;
import org.eclipse.ceylon.launcher.ClassLoaderSetupException;


/**
 * Ant task to extract information from a Ceylon module descriptor and 
 * set ant properties with it
 * @author tom
 */
@AntDoc("Task which sets ant properties according to information extracted "
      + "from a Ceylon module descriptor (`module.ceylon`).\n"
      + "To retrieve the name, version and license information from the `sources`\n"
      + "for module `com.example.foo` and exposing them as ant properties:\n"
      + "\n"
      + "<!-- lang: xml -->\n"
      + "    <target name=\"descriptor\" depends=\"ceylon-ant-taskdefs\">\n"
      + "      <ceylon-module-descriptor\n"
      + "            src=\"src\"\n"
      + "            module=\"com.example.foo\"\n"
      + "            name=\"modulename\"\n"
      + "            version=\"moduleversion\"\n"
      + "            license=\"modulelicense\"\n"
      + "      />\n"
      + "      <echo message=\"Name ${modulename}\" />\n"
      + "      <echo message=\"Version ${moduleversion}\" />\n"
      + "      <echo message=\"License ${modulelicense}\" />\n"
      + "    </target>\n")
public class CeylonModuleDescriptorTask extends Task {

    private Module module;
    private String versionProperty;
    private String nameProperty;
    private String licenseProperty;
    private File src;

    @AntDoc("The source directory containing ceylon modules. Defaults to `source`.")
    public void setSrc(File srcDir) {
        this.src = srcDir;
    }
    public File getSrc() {
        if (this.src == null) {
            return getProject().resolveFile(Constants.DEFAULT_SOURCE_DIR);
        }
        return src;
    }
    @AntDoc("The name of the module whose descriptor should be read")
    @Required
    public void setModule(Module module){
        this.module = module;
    }
    @AntDoc("The name of the ant property to set with the module version read from the descriptor")
    public void setVersionProperty(String versionProperty) {
        this.versionProperty = versionProperty;
    }
    @AntDoc("The name of the ant property to set with the module name read from the descriptor")
    public void setNameProperty(String nameProperty) {
        this.nameProperty = nameProperty;
    }
    @AntDoc("The name of the ant property to set with the module license read from the descriptor")
    public void setLicenseProperty(String licenseProperty) {
        this.licenseProperty = licenseProperty;
    }
    
    /**
     * Executes the task.
     * @exception BuildException if an error occurs
     */
    @Override
    public void execute() throws BuildException {
        Java7Checker.check();
        ModuleDescriptorReader reader;
        try{
            CeylonClassLoader loader = Util.getCeylonClassLoaderCachedInProject(getProject());
            try {
                reader = new ModuleDescriptorReader(loader, module.getName(), getSrc());
            } catch (NoSuchModuleException e) {
                throw new BuildException("Failed to load module", e);
            }
        }catch(ClassLoaderSetupException x){
            throw new BuildException("Failed to set up Ceylon class loader", x);
        }
        if (versionProperty != null) {
            setProjectProperty(versionProperty, reader.getModuleVersion());
        }
        if (nameProperty != null) {
            setProjectProperty(nameProperty, reader.getModuleName());
        }
        if (licenseProperty != null) {
            setProjectProperty(licenseProperty, reader.getModuleLicense());
        }
    }
    
    private void setProjectProperty(String versionProperty, String value) {
        String existingValue = getProject().getProperty(versionProperty);
        if (existingValue == null) {
            log("Setting " + versionProperty + " = " + value + " based on value in module.ceylon descriptor of module " + this.module + " in " + getSrc());
            getProject().setNewProperty(versionProperty, value);
        } else {
            log("Property " + versionProperty + " has already been set to " + existingValue);
        }
    }
    
}
