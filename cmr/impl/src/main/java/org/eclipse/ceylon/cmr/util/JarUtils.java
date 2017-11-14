/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.jar.Pack200.Packer;
import java.util.jar.Pack200.Unpacker;
import java.util.zip.ZipEntry;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.impl.ShaSigner;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.log.Logger;

public final class JarUtils {

    /** A simple filter to determine if an entry should be included in an archive. */
    public static interface JarEntryFilter {
        public boolean avoid(String entryFullName);
    }

    public static void finishUpdatingJar(File originalFile, File outputFile, ArtifactContext context, 
            JarOutputStream jarOutputStream, JarEntryFilter filter,
            RepositoryManager repoManager, boolean verbose, Logger log,
            Set<String> folders) throws IOException {
        finishUpdatingJar(originalFile, outputFile, context, jarOutputStream, filter, repoManager, verbose, log, folders, false);
    }
    
    /**
     * Adds to the given jarOutputStream those entries in originalFile which are not in 
     */
    public static void finishUpdatingJar(File originalFile, File outputFile, ArtifactContext context, 
            JarOutputStream jarOutputStream, JarEntryFilter filter,
            RepositoryManager repoManager, boolean verbose, Logger log,
            Set<String> foldersToAdd, boolean pack200) throws IOException {
        finishUpdatingJar(originalFile, outputFile, context, jarOutputStream, filter, repoManager, verbose, log, foldersToAdd, new HashSet<String>(), pack200);
    }
    
    public static void finishUpdatingJar(File originalFile, File outputFile, ArtifactContext context, 
            JarOutputStream jarOutputStream, JarEntryFilter filter,
            RepositoryManager repoManager, boolean verbose, Logger log,
            Set<String> foldersToAdd, Set<String> foldersAlreadyAdded, boolean pack200) throws IOException {
        
        copyMissingFromOriginal(originalFile, jarOutputStream, filter, foldersToAdd);
        
        for(String folder : foldersToAdd){
            if (!foldersAlreadyAdded.contains(folder)) {
                ZipEntry dir = new ZipEntry(folder);
                jarOutputStream.putNextEntry(dir);
                jarOutputStream.closeEntry();
                foldersAlreadyAdded.add(folder);
            }
        }
        jarOutputStream.flush();
        jarOutputStream.close();
        if (pack200) {
            repack(outputFile, log);
        }
        
        File sha1File = ShaSigner.sign(outputFile, log, verbose);
        publish(outputFile, sha1File, context, repoManager, log);
    }

    public static void publish(File outputFile, File sha1File, ArtifactContext context, RepositoryManager repoManager, Logger log) {
        try {
            context.setForceOperation(true);
            // In putting we're effectively renaming
            repoManager.putArtifact(context, outputFile);
            ArtifactContext sha1Context = context.getSha1Context();
            sha1Context.setForceOperation(true);
            repoManager.putArtifact(sha1Context, sha1File);
        } catch(RuntimeException x) {
            log.error("Failed to write module to repository: "+x.getMessage());
            // fatal errors go all the way up but don't print anything if we logged an error
            throw x;
        } finally {
            // now cleanup
            outputFile.delete();
            sha1File.delete();
        }
    }

    public static void makeFolder(Set<String> foldersAlreadyAdded, JarOutputStream jarOutputStream, String folder) throws IOException {
        if (!foldersAlreadyAdded.contains(folder)) {
            ZipEntry dir = new ZipEntry(folder);
            jarOutputStream.putNextEntry(dir);
            jarOutputStream.closeEntry();
            foldersAlreadyAdded.add(folder);
        } 
    }

    protected static void copyMissingFromOriginal(File originalFile, JarOutputStream jarOutputStream,
            JarEntryFilter filter, Set<String> folders) throws IOException {
        JarFile jarFile = validJar(originalFile);
        if (jarFile != null) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()){
                JarEntry entry = entries.nextElement();
                // skip the old entry if we overwrote it
                if(filter.avoid(entry.getName()))
                    continue;
                // only preserve directories if we did not write to them
                if(entry.isDirectory() && folders.contains(entry.getName()))
                    continue;
                ZipEntry copiedEntry = new ZipEntry(entry.getName());
                // Preserve the modification time and comment
                copiedEntry.setTime(entry.getTime());
                copiedEntry.setComment(entry.getComment());
                jarOutputStream.putNextEntry(copiedEntry);
                if(!entry.isDirectory()){
                    InputStream inputStream = jarFile.getInputStream(entry);
                    copy(inputStream, jarOutputStream);
                    inputStream.close();
                }
                jarOutputStream.closeEntry();
            }
            jarFile.close();
        }
    }

    public static JarFile validJar(File originalFile) {
        if (originalFile != null && originalFile.isFile() && originalFile.canRead()) {
            try {
                return new JarFile(originalFile);
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    public static boolean isValidJar(File originalFile) {
        JarFile jarFile = validJar(originalFile);
        if (jarFile != null) {
            try {
                jarFile.close();
            } catch (IOException e) {
                // Ignore
            }
            return true;
        }
        return false;
    }

    /**
     * Takes the jar generated file and repacks it using pack200 in an attempt 
     * to reduce the file size. This is only worth doing on jars containing class files.
     */
    public static void repack(File outputFile, Logger log) throws IOException,
            FileNotFoundException {
        Packer packer = Pack200.newPacker();
        packer.properties().put(Packer.EFFORT, "9");
        packer.properties().put(Packer.KEEP_FILE_ORDER, Packer.FALSE);
        packer.properties().put(Packer.DEFLATE_HINT, Packer.TRUE);
        packer.properties().put(Packer.SEGMENT_LIMIT, "-1");
        packer.properties().put(Packer.MODIFICATION_TIME, Packer.LATEST);
        File tmp = File.createTempFile("ceylon-jarutils-", ".pack200", outputFile.getParentFile());
        try {
            try (OutputStream out = new FileOutputStream(tmp)) {
                try (JarFile in = new JarFile(outputFile)) {
                    packer.pack(in, out);
                }
            }
            
            try (JarOutputStream outStream = new JarOutputStream(new FileOutputStream(outputFile))) {
                outStream.setLevel(9);
                Unpacker unpacker = Pack200.newUnpacker();
                unpacker.unpack(tmp, outStream);
            }
        } finally {
            tmp.delete();
        }
        log.debug("[repacked jar: "+outputFile.getPath()+"]");
    }

    public static String toPlatformIndependentPath(Iterable<? extends File> sourcePaths, String prefixedSourceFile) {
        String sourceFile = FileUtil.relativeFile(sourcePaths, prefixedSourceFile);
        // zips are UNIX-friendly
        sourceFile = sourceFile.replace(File.separatorChar, '/');
        return sourceFile;
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while((read = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
    }
    
    public static long oldestFileTime(File file) {
        long mtime = Long.MAX_VALUE;
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()){
                JarEntry entry = entries.nextElement();
                if (entry.getTime() < mtime) {
                    mtime = entry.getTime();
                }
            }
        } catch (Exception ex) {
            mtime = Long.MIN_VALUE;
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return mtime;
    }

    public static String getFolder(String fileName) {
        int lastSep = fileName.lastIndexOf('/');
        // toplevel does not need a folder
        if(lastSep == -1)
            return null;
        // include the last slash to create a folder
        return fileName.substring(0, lastSep+1);
    }

    public static Properties getMetaInfProperties(File jarFile, String propFileName) throws IOException {
        JarFile jar = validJar(jarFile);
        if (jar != null) {
            try {
                JarEntry entry = jar.getJarEntry(propFileName);
                if (entry != null) {
                    InputStream inputStream = jar.getInputStream(entry);
                    try {
                        Properties previousMapping = new Properties();
                        previousMapping.load(inputStream);
                        return previousMapping;
                    } finally {
                        inputStream.close();
                    }
                }
            } finally {
                jar.close();
            }
        }
        return null;
    }
}
