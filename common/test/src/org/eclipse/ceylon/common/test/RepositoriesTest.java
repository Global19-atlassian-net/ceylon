/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.ceylon.common.Constants;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.config.CeylonConfig;
import org.eclipse.ceylon.common.config.CeylonConfigFinder;
import org.eclipse.ceylon.common.config.Credentials;
import org.eclipse.ceylon.common.config.Repositories;
import org.eclipse.ceylon.common.config.Repositories.Repository;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

public class RepositoriesTest {

    CeylonConfig testConfig;
    Repositories repos;
    Repositories defaultRepos;
    Repositories overriddenRepos;
    
    @Before
    public void setup() throws IOException {
        testConfig = CeylonConfigFinder.loadConfigFromFile(new File("test/src/org/eclipse/ceylon/common/test/repos.config"));
        if (FileUtil.getInstallDir() == null) {
            // Set a fake installation folder
            System.setProperty("ceylon.home", "fake-install-dir");
        }
        repos = Repositories.withConfig(testConfig);
        
        CeylonConfig fakeConfig = new CeylonConfig();
        defaultRepos = Repositories.withConfig(fakeConfig);
        
        CeylonConfig overriddenConfig = CeylonConfigFinder.loadConfigFromFile(new File("test/src/org/eclipse/ceylon/common/test/overridden.config"));
        overriddenRepos = Repositories.withConfig(overriddenConfig);
    }
    
    @Test
    public void testGetRepository() {
        assertRepository(repos.getRepository("One"), "One", "foobar", null, null);
        assertRepository(repos.getRepository("Two"), "Two", "foobar", "pietjepluk", "noencryptionfornow!");
    }
    
    @Test
    public void testGetSystemRepository() {
        assertRepository(repos.getSystemRepository(), "One", "foobar", null, null);
    }
    
    @Test
    public void testGetDeafultSystemRepository() {
        File dir = new File(FileUtil.getInstallDir(), "repo");
        assertRepository(defaultRepos.getSystemRepository(), "SYSTEM", dir.getAbsolutePath(), null, null);
    }
    
    @Test
    public void testGetOverriddenSystemRepository() {
        assertRepository(overriddenRepos.getSystemRepository(), "SYSTEM", "system", null, null);
    }
    
    @Test
    public void testGetOutputRepository() {
        assertRepository(repos.getOutputRepository(), "Two", "foobar", "pietjepluk", "noencryptionfornow!");
    }
    
    @Test
    public void testGetDefaultOutputRepository() {
        assertRepository(defaultRepos.getOutputRepository(), "LOCAL", "./modules", null, null);
    }
    
    @Test
    public void testGetOverriddenOutputRepository() {
        assertRepository(overriddenRepos.getOutputRepository(), "LOCAL", "local", null, null);
    }
    
    @Test
    public void testGetCacheRepository() {
        assertRepository(repos.getCacheRepository(), "Three", "foobar", null, null);
    }
    
    @Test
    public void testGetDefaultCacheRepository() {
        File dir = defaultRepos.getCacheRepoDir();
        assertRepository(defaultRepos.getCacheRepository(), "CACHE", dir.getAbsolutePath(), null, null);
    }
    
    @Test
    public void testGetOverriddenCacheRepository() {
        assertRepository(overriddenRepos.getCacheRepository(), "CACHE", "cache", null, null);
    }
    
    @Test
    public void testGetLocalLookupRepositories() {
        Repository[] lookup = repos.getLocalLookupRepositories();
        Assert.assertTrue(lookup.length == 4);
        assertRepository(lookup[0], "Two", "foobar", "pietjepluk", "noencryptionfornow!");
        assertRepository(lookup[1], "Three", "foobar", null, null);
        assertRepository(lookup[2], "Four", new File(System.getProperty("user.home"), "foobar").getPath(), null, null);
        assertRepository(lookup[3], "%lookup-4", "foobar", null, null);
    }
    
    @Test
    public void testGetRemoteLookupRepositories() {
        Repository[] lookup = repos.getRemoteLookupRepositories();
        Assert.assertTrue(lookup.length == 2);
        assertRepository(lookup[0], "Four", new File(System.getProperty("user.home"), "foobar").getPath(), null, null);
        assertRepository(lookup[1], "%remote-2", "foobar", null, null);
    }
    
    @Test
    public void testGetOtherLookupRepositories() {
        Repository[] lookup = repos.getOtherLookupRepositories();
        Assert.assertTrue(lookup.length == 2);
        assertRepository(lookup[0], "Four", new File(System.getProperty("user.home"), "foobar").getPath(), null, null);
        assertRepository(lookup[1], "%other-2", new File(System.getProperty("user.home"), "fubar").getPath(), null, null);
    }
    
    @Test
    public void testGetDefaultLocalLookupRepositories() {
        Repository[] lookup = defaultRepos.getLocalLookupRepositories();
        Assert.assertTrue(lookup.length == 1);
        assertRepository(lookup[0], "LOCAL", "./modules", null, null);
    }
    
    @Test
    public void testGetDefaultGlobalLookupRepositories() {
        Repository[] lookup = defaultRepos.getGlobalLookupRepositories();
        Assert.assertTrue(lookup.length == 1);
        File userDir = defaultRepos.getUserRepoDir();
        assertRepository(lookup[0], "USER", userDir.getAbsolutePath(), null, null);
    }
    
    @Test
    public void testGetDefaultRemoteLookupRepositories() {
        Repository[] lookup = defaultRepos.getRemoteLookupRepositories();
        Assert.assertTrue(lookup.length == 0);
    }
    
    @Test
    public void testGetDefaultOtherLookupRepositories() {
        Repository[] lookup = defaultRepos.getOtherLookupRepositories();
        Assert.assertTrue(lookup.length == 3);
        assertRepository(lookup[0], "REMOTE", Constants.REPO_URL_CEYLON, null, null);
        assertRepository(lookup[1], "MAVEN", "maven:", null, null);
        assertRepository(lookup[2], "NPM", "npm:", null, null);
    }
    
    @Test
    public void testGetOverriddenLocalLookupRepositories() {
        Repository[] lookup = overriddenRepos.getLocalLookupRepositories();
        Assert.assertTrue(lookup.length == 1);
        assertRepository(lookup[0], "LOCAL", "local", null, null);
    }
    
    @Test
    public void testGetOverriddenGlobalLookupRepositories() {
        Repository[] lookup = overriddenRepos.getGlobalLookupRepositories();
        Assert.assertTrue(lookup.length == 1);
        assertRepository(lookup[0], "USER", "user", null, null);
    }
    
    @Test
    public void testGetOverriddenOtherLookupRepositories() {
        Repository[] lookup = overriddenRepos.getOtherLookupRepositories();
        Assert.assertTrue(lookup.length == 3);
        assertRepository(lookup[0], "REMOTE", "http://remote", null, null);
        assertRepository(lookup[1], "MAVEN", "maven:", null, null);
        assertRepository(lookup[2], "NPM", "npm:", null, null);
    }
    
    @Test
    public void testSetRepositories() {
        CeylonConfig configCopy = testConfig.copy();
        Repositories testRepos = Repositories.withConfig(configCopy);
        Map<String, Repository[]> repomap = testRepos.getRepositories();
        Repository[] reps = { new Repositories.SimpleRepository("", "./mods", null) };
        repomap.put(Repositories.REPO_TYPE_LOCAL_LOOKUP, reps);
        testRepos.setRepositories(repomap);
        Repository[] lookup = testRepos.getLocalLookupRepositories();
        Assert.assertTrue(lookup.length == 1);
        assertRepository(lookup[0], "%lookup-1", "./mods", null, null);
    }
    
    @Test
    public void testSetRepositoriesByType() {
        CeylonConfig configCopy = testConfig.copy();
        Repositories testRepos = Repositories.withConfig(configCopy);
        Repository[] reps = testRepos.getRepositoriesByType(Repositories.REPO_TYPE_LOCAL_LOOKUP);
        reps = new Repositories.SimpleRepository[] { new Repositories.SimpleRepository("", "./mods", null) };
        testRepos.setRepositoriesByType(Repositories.REPO_TYPE_LOCAL_LOOKUP, reps);
        Repository[] lookup = testRepos.getLocalLookupRepositories();
        Assert.assertTrue(lookup.length == 1);
        assertRepository(lookup[0], "%lookup-1", "./mods", null, null);
    }
    
    private void assertRepository(Repository repo, String name, String url, String user, String password) {
        Assert.assertNotNull(repo);
        Assert.assertEquals(name, repo.getName());
        Assert.assertEquals(url, repo.getUrl());
        Credentials credentials = repo.getCredentials();
        Assert.assertEquals(user, credentials != null ? credentials.getUser() : null);
        Assert.assertEquals(password, credentials != null ? credentials.getPassword() : null);
    }
}
