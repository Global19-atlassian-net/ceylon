/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.junit.Assert;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.config.CeylonConfig;
import org.eclipse.ceylon.common.config.CeylonConfigFinder;
import org.eclipse.ceylon.common.config.ConfigWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConfigWriterTest {

    private File testFile;
    private File testDir;
    private CeylonConfig testConfig;
    
    @Before
    public void setup() throws IOException {
        testFile = new File("test/src/org/eclipse/ceylon/common/test/writer.config");
        testConfig = CeylonConfigFinder.loadOriginalConfigFromFile(testFile);
        testDir = new File("tmp-test-files");
        testDir.mkdirs();
    }
    
    @After
    public void teardown() throws IOException {
        FileUtil.delete(testDir);
    }
    
    @Test
    public void testSimpleDuplicate() throws IOException {
        String contents = readFile(testFile);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConfigWriter.instance().write(testConfig, testFile, out);
        Assert.assertEquals(contents, out.toString("UTF-8"));
    }
    
    @Test
    public void testSimpleWipe() throws IOException {
        String contents = readFile(new File("test/src/org/eclipse/ceylon/common/test/writer-wiped.config"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConfigWriter.instance().write(new CeylonConfig(), testFile, out);
        Assert.assertEquals(contents, out.toString("UTF-8"));
    }
    
    @Test
    public void testCRUD() throws IOException {
        CeylonConfig testConfigCopy = testConfig.copy();
        testConfigCopy.setOption("test.two", "twee");
        testConfigCopy.setOption("test.string-hello", "hola");
        testConfigCopy.setOption("test.string-world", "mundo");
        testConfigCopy.setOption("test.string-spaces", " con espacios   ");
        testConfigCopy.setOption("test.string-multiline", "wim\nzus\njet");
        testConfigCopy.setOption("test.string-multiline-with-spaces", "wim\nzus\njet");
        testConfigCopy.setOption("test.string-quoted-multiline", "wim\nzus\njet ");
        testConfigCopy.setOption("test.three", "tres");
        testConfigCopy.setOptionValues("test.multiple.strings", new String[] {"wim", "zus"});
        testConfigCopy.setOptionValues("test.section.Aap.foo", new String[] {"1", "2", "3"});
        testConfigCopy.removeOption("test.section.Noot.foo");
        testConfigCopy.removeOption("test.section.Mies.foo");
        testConfigCopy.removeOption("test.section.Mies.fooz");
        testConfigCopy.setOptionValues("test.section.Mies.bar", new String[] {"test1", "test2"});
        testConfigCopy.setOptionValues("test.newsection.baz", new String[] {"qed", "qam"});
        testConfigCopy.setOption("anothersection.buzz", "lightyear");
        
        String contents = readFile(new File("test/src/org/eclipse/ceylon/common/test/writer-crud.config"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConfigWriter.instance().write(testConfigCopy, testFile, out);
        Assert.assertEquals(contents, out.toString("UTF-8"));
    }

    @Test
    public void testWritingAndOverwriting() {
        try {
            File configFile = new File(testDir, ".ceylon/config");
            
            CeylonConfig testConfigCopy = testConfig.copy();
            testConfigCopy.setOption("test.quasar", "PKS 1127-145");
            ConfigWriter.instance().write(testConfigCopy, configFile);
            
            CeylonConfig localDirConfig = CeylonConfigFinder.loadLocalConfig(testDir);
            localDirConfig.setOption("test.pulsar", "CP 1919");
            localDirConfig.setOption("test.string-escapes2", "\n\t\"\\# ");
            localDirConfig.removeOption("test.one");
            localDirConfig.removeSection("test.multiple");
            ConfigWriter.instance().write(localDirConfig, configFile);
            
            String contents1 = readFile(new File("test/src/org/eclipse/ceylon/common/test/writer-overwriting.config"));
            String contents2 = readFile(configFile);
            Assert.assertEquals(contents1, contents2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }        
    }
    
    private String readFile(File file) {
        try {
            Reader reader = new FileReader(file);
            StringBuilder strbuf = new StringBuilder();
            try {
                char[] buf = new char[1024];
                int read;
                while ((read = reader.read(buf)) > -1) {
                    strbuf.append(buf, 0, read);
                }
            } finally {
                reader.close();
            }
            return strbuf.toString();
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }
}
