/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.jboss.ceylon.test.modules;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.Versions;
import org.jboss.modules.Main;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.junit.Assert;

import ceylon.modules.bootstrap.loader.InitialModuleLoader;
import ceylon.modules.spi.Argument;
import ceylon.modules.spi.Constants;

/**
 * Modules test helper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class ModulesTest {
    private static final String RUNTIME_IMPL = "ceylon.modules.jboss.runtime.JBossRuntime";

    protected File getRepo() throws Throwable {
        return new File(getClass().getResource("/repo").toURI());
    }

    protected File getCache() throws Throwable {
        return new File(new File(getClass().getResource("/").toURI()), "cache");
    }

    protected File getAlternative() throws Throwable {
        return new File(getClass().getResource("/alternative").toURI());
    }

    protected File createModuleFile(File tmpdir, Archive module) throws Exception {
        String fullName = module.getName();

        final boolean isDefault = (Constants.DEFAULT + ".car").equals(fullName);

        String name;
        String version;

        if (isDefault) {
            name = Constants.DEFAULT.toString();
            version = null;
        } else {
            int p = fullName.indexOf("-");
            if (p < 0)
                throw new IllegalArgumentException("No name and version split: " + fullName);

            name = fullName.substring(0, p);
            version = fullName.substring(p + 1, fullName.lastIndexOf("."));
        }

        File targetDir = new File(tmpdir, toPathString(name, version));
        if (targetDir.exists() == false) {
            Assert.assertTrue(targetDir.mkdirs());
        }
        File targetFile = new File(targetDir, fullName);
        if (targetFile.exists()) {
            Assert.assertTrue(targetFile.delete());
        }

        ZipExporter exporter = module.as(ZipExporter.class);
        exporter.exportTo(targetFile, true);

        return targetDir;
    }

    protected void testArchive(Archive module, Archive... libs) throws Throwable {
        testArchive(module, null, libs);
    }

    protected void testArchive(Archive module, String run, Archive... libs) throws Throwable {
        File tmpdir = Files.createTempDirectory("ceylon-runtimetests-").toFile();
        try {
            List<File> files = new ArrayList<File>();
            files.add(createModuleFile(tmpdir, module));
            for (Archive lib : libs) {
                files.add(createModuleFile(tmpdir, lib));
            }

            String name;
            String version;

            String fullName = module.getName();
            final boolean isDefault = (Constants.DEFAULT + ".car").equals(fullName);
            if (isDefault) {
                name = Constants.DEFAULT.toString();
                version = null;
            } else {
                int p = fullName.indexOf("-");
                if (p < 0)
                    throw new IllegalArgumentException("No name and version split: " + fullName);

                name = fullName.substring(0, p);
                version = fullName.substring(p + 1, fullName.lastIndexOf("."));
            }

            Map<String, String> args = new LinkedHashMap<String, String>();
            args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.SYSTEM_REPOSITORY.toString(), getDistRepo());
            args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString(), tmpdir.toString());
            if (run != null)
                args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.RUN.toString(), run);

            execute(version != null ? name + "/" + version : name, args);
        } finally {
            FileUtil.deleteQuietly(tmpdir);
        }
    }

    protected static void delete(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                delete(f);
        }
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    protected void src(String module, String src) throws Throwable {
        src(module, src, Collections.<String, String>emptyMap());
    }

    protected void car(String module, Map<String, String> extra) throws Throwable {
        Map<String, String> args = new LinkedHashMap<String, String>();
        args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.OFFLINE.toString(), "");
        args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString(), getRepo().getPath());
        args.putAll(extra);

        execute(module, args);
    }

    protected void src(String module, String src, Map<String, String> extra) throws Throwable {
        Map<String, String> args = new LinkedHashMap<String, String>();
        args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.SOURCE.toString(), src);
        args.putAll(extra);

        execute(module, args);
    }

    protected String getDistRepo() {
        final String projectHome = System.getProperty("ceylon.runtime.home", System.getProperty("user.dir"));
        String distRepo = projectHome + File.separator + ".." + File.separator + "dist" + File.separator + "dist" + File.separator + "repo";
        return distRepo;
    }

    protected String getBootstrapModules() {
        final String projectHome = System.getProperty("ceylon.runtime.home", System.getProperty("user.dir"));
        String runtimeRepo = projectHome + File.separator + "build" + File.separator + "dist" + File.separator + "repo";
        return getDistRepo() + File.pathSeparator + runtimeRepo;
    }

    protected static String toPathString(String name, String version) {
        final StringBuilder builder = new StringBuilder();
        builder.append(name.replace('.', File.separatorChar));
        if (RepositoryManager.DEFAULT_MODULE.equals(name) == false)
            builder.append(File.separatorChar).append(version);
        builder.append(File.separatorChar);
        return builder.toString();
    }

    protected void execute(String module, Map<String, String> map) throws Throwable {
        List<String> args = new ArrayList<String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            args.add(entry.getKey());
            if (!entry.getValue().isEmpty()) {
                args.add(entry.getValue());
            }
        }
        execute(module, args);
    }

    protected void execute(String module, List<String> extra) throws Throwable {
        List<String> args = new ArrayList<String>();
        // JBoss Modules args
        args.add(Constants.MODULE_PATH.toString());
        args.add(getBootstrapModules());
        args.add(Constants.CEYLON_RUNTIME_MODULE + ":" + Versions.CEYLON_VERSION_NUMBER);
        // default Ceylon runtime args
        args.add(Constants.IMPL_ARGUMENT_PREFIX + Argument.EXECUTABLE.toString());
        args.add(RUNTIME_IMPL);
        // Set the -cacherep
        args.add(Constants.CEYLON_ARGUMENT_PREFIX + Argument.CACHE_REPOSITORY.toString());
        args.add(getCache().getPath());
        // extra args
        args.addAll(extra);
        // module_ args
        args.add(module);
        // run JBoss Modules
        System.err.print("Command line: ");
        for (String arg : args) {
            System.err.print(arg + " ");
        }
        System.err.println("");
        System.setProperty("boot.module.loader", InitialModuleLoader.class.getName());
        Main.main(args.toArray(new String[args.size()]));
    }
}

