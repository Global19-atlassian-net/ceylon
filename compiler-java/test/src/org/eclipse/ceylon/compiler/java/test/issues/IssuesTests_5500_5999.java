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
package org.eclipse.ceylon.compiler.java.test.issues;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.jar.JarFile;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.ceylon.cmr.impl.IOUtils;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.junit.Assert;
import org.junit.Test;


public class IssuesTests_5500_5999 extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
        return new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.issues", "1");
    }
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-5500-5999";
    }

    @Test
    public void testBug5741() {
        compareWithJavaSource("bug57xx/bug5741");
    }

    @Test
    public void testBug5752() {
        compareWithJavaSource("bug57xx/bug5752/bug5752");
    }

    @Test
    public void testBug5751() {
        compilesWithoutWarnings("bug57xx/bug5751/bug5751.ceylon");
    }

    @Test
    public void testBug5774() {
        compile("bug57xx/bug5774/bug5774.ceylon");
    }

    @Test
    public void testBug5785() {
        compareWithJavaSource("bug57xx/bug5785");
    }

    @Test
    public void testBug5787() {
        compareWithJavaSource("bug57xx/bug5787");
    }

    @Test
    public void testBug5855() {
        compileAndRun("org.eclipse.ceylon.compiler.java.test.issues.bug58xx.bug5855",
                "bug58xx/bug5855.ceylon");
    }

    @Test
    public void testBug5856() {
        compareWithJavaSource("bug58xx/bug5856");
    }

    @Test
    public void testBug5866() {
        compileAndRun("org.eclipse.ceylon.compiler.java.test.issues.bug58xx.bug5866",
                "bug58xx/bug5866.ceylon");
    }
    
    @Test
    public void testBug5868() {
        compileAndRun("org.eclipse.ceylon.compiler.java.test.issues.bug58xx.bug5868",
                "bug58xx/bug5868.ceylon");
    }

    @Test
    public void testBug5919() throws IOException {
        File mrepo = new File(System.getProperty("user.home"), ".m2/repository");
        File annot = new File(mrepo, "com/android/support/support-annotations/23.3.0");
        File jar = new File(annot, "support-annotations-23.3.0.jar");
        File pom = new File(annot, "support-annotations-23.3.0.pom");
        downloadAndroidAnnotations(pom);
        downloadAndroidAnnotations(jar);
        File overridesFile = new File(getPackagePath(), "bug59xx/bug5919/overrides.xml");
        compile(Arrays.asList(
                "-overrides", overridesFile.getAbsolutePath(),
                "-apt", "com.jakewharton:butterknife-compiler/8.1.0"), 
                "bug59xx/bug5919/test.ceylon");
        
        File carFile = getModuleArchive("org.eclipse.ceylon.compiler.java.test.issues.bug59xx.bug5919", "1");
        assertTrue(carFile.exists());

        try(JarFile car = new JarFile(carFile)){
            Assert.assertNotNull(car.getEntry("org/eclipse/ceylon/compiler/java/test/issues/bug59xx/bug5919/Foo$$ViewBinder.class"));
        }
    }

    private void downloadAndroidAnnotations(File file) throws IOException {
        String repoUrl = "https://android.googlesource.com/platform/prebuilts/maven_repo/android/+/android-7.0.0_r21/com/android/support/support-annotations/23.3.0/";
        // support-annotations-23.3.0.pom?format=TEXT
        if(!file.exists()){
            File folder = file.getParentFile();
            folder.mkdirs();
            URL url = new URL(repoUrl+file.getName()+"?format=TEXT");
            File b64 = new File(folder, file.getName()+".base64");
            try{
                try(InputStream is = url.openStream();
                        OutputStream out = new FileOutputStream(b64)){
                    IOUtils.copyStream(is, out, false, false);
                    out.flush();
                }
                char[] buffer = new char[(int) b64.length()];
                try(Reader r = new FileReader(b64)){
                    r.read(buffer);
                }
                byte[] bytes = DatatypeConverter.parseBase64Binary(new String(buffer));
                try(OutputStream os = new FileOutputStream(file)){
                    os.write(bytes);
                    os.flush();
                }
            }finally{
                b64.delete();
            }
        }
    }
        
    @Test
    public void testBug5892() {
        compile("bug58xx/bug5892.ceylon");
    }

    @Test
    public void testBug5924() {
        compile("bug59xx/bug5924.ceylon");
    }

    @Test
    public void testBug5947() {
        compile("bug59xx/bug5947.ceylon");
    }

    @Test
    public void testBug5958() {
        compareWithJavaSource("bug59xx/bug5958");
        run("org.eclipse.ceylon.compiler.java.test.issues.bug59xx.bug5958");
    }

    @Test
    public void testBug5953() {
        compile("bug59xx/bug5953.ceylon");
    }

}
