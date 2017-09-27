/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.ceylon.cmr.impl;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.RepositoryBuilder;
import org.eclipse.ceylon.common.FileUtil;

/**
 * Repository builder for local uses of the DefaultRepository
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class LocalRepositoryBuilder implements RepositoryBuilder {

    @Override
    public String absolute(File cwd, String token) throws URISyntaxException {
        final File file = (token.startsWith("file:") ? new File(new URI(token)) : new File(token));
        File absfile = FileUtil.absoluteFile(FileUtil.applyCwd(cwd, file));
        return absfile.getAbsolutePath();
    }

    @Override
    public CmrRepository[] buildRepository(String token) throws Exception {
        return buildRepository(token, EMPTY_CONFIG);
    }

    @Override
    public CmrRepository[] buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        final File file = (token.startsWith("file:") ? new File(new URI(token)) : new File(token));
        if (file.exists() == false)
            throw new IllegalArgumentException("Directory does not exist: " + token);
        if (file.isDirectory() == false)
            throw new IllegalArgumentException("Repository exists but is not a directory: " + token);

        FileContentStore cs = new FileContentStore(file);
        return new CmrRepository[] { new DefaultRepository(cs.createRoot()) };
    }
}
