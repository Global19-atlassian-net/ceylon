/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.modules.bootstrap.loader;

import org.eclipse.ceylon.common.FileUtil;
import org.jboss.modules.LocalModuleLoader;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Load bootstrap modules from zipped ditribution repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DistributionModuleLoader extends BootstrapModuleLoader {

    private static final String BOOTSTRAP_DISTRIBUTION = "ceylon-runtime-bootstrap";
    private final ModuleLoader delegate;

    public DistributionModuleLoader() {
        final String ceylonRepository = getCeylonRepository();
        final File dir = new File(ceylonRepository, BOOTSTRAP_DISTRIBUTION);
        final File zip = new File(dir, BOOTSTRAP_DISTRIBUTION + ".zip");
        if (zip.exists() == false)
            throw new IllegalArgumentException("No such Ceylon Runtime Bootstrap distribution file: " + zip);

        final File unzipped = unzipDistribution(dir, zip);
        delegate = new LocalModuleLoader(new File[]{unzipped});
    }

    /**
     * Unzip bootstrap distrubution if not already present.
     *
     * @param dir the unzip destination
     * @param zip the zipped bootstrap distribution
     * @return unziped root directory
     */
    protected File unzipDistribution(File dir, File zip) {
        File exploded = new File(dir, BOOTSTRAP_DISTRIBUTION + "-exploded");
        if (exploded.exists()) {
            if (forceBootstrapUpdate() == false)
                return exploded;
            else
                delete(exploded); // cleanup
        }

        try {
            final ZipFile zipFile = new ZipFile(zip);
            try {
                final Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    final ZipEntry ze = entries.nextElement();
                    final File file = new File(exploded, ze.getName());
                    if (ze.isDirectory()) {
                        if (FileUtil.mkdirs(file) == false)
                            throw new IllegalArgumentException("Cannot create dir: " + file);
                    } else {
                        final FileOutputStream fos = new FileOutputStream(file);
                        copyStream(zipFile.getInputStream(ze), fos);
                    }
                }
            } finally {
                zipFile.close();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return exploded;
    }

    protected boolean forceBootstrapUpdate() {
        return SecurityActions.getBoolean("force.bootstrap.update");
    }

    protected static void delete(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                delete(f);
        }
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    protected static void copyStream(final InputStream in, final OutputStream out) throws IOException {
        final byte[] bytes = new byte[8192];
        int cnt;
        try {
            while ((cnt = in.read(bytes)) != -1) {
                out.write(bytes, 0, cnt);
            }
        } finally {
            safeClose(in);
            safeClose(out);
        }
    }

    protected static void safeClose(Closeable c) {
        try {
            c.close();
        } catch (Exception ignored) {
        }
    }

    @Override
    protected Module preloadModule(ModuleIdentifier identifier) throws ModuleLoadException {
        return delegate.loadModule(identifier);
    }

    @Override
    protected ModuleSpec findModule(ModuleIdentifier moduleIdentifier) throws ModuleLoadException {
        throw new ModuleLoadException("Should not be here, by-passing delegate loader?");
    }

    @Override
    public String toString() {
        return "Ceylon Bootstrap Module Loader: " + delegate;
    }
}
