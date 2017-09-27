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

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.RepositoryBuilder;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.log.Logger;

/**
 * Repository builder for NpmRepository
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class NpmRepositoryBuilder implements RepositoryBuilder {

    @Override
    public String absolute(File cwd, String token) {
        if (token.equals("npm:") || token.equals("npm:/#")) {
            return "npm:";
        } else if (token.startsWith("npm:")) {
            token = token.substring(4);
            File f = FileUtil.absoluteFile(FileUtil.applyCwd(cwd, new File(token)));
            token = f.getAbsolutePath();
            return "npm:" + token;
        } else {
            return null;
        }
    }

    @Override
    public CmrRepository[] buildRepository(String token) throws Exception {
        return buildRepository(token, EMPTY_CONFIG);
    }

    @Override
    public CmrRepository[] buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        if (token.equals("npm:/#")) {
            token = "npm:";
        }
        if (token.startsWith("npm:")) {
            CmrRepository repo = createNpmRepository(token, config.log, config.offline, config.currentDirectory);
            return new CmrRepository[] { repo };
        } else {
            return null;
        }
    }
    
    public static CmrRepository createNpmRepository(String token, Logger log, boolean offline, String currentDirectory) {
        File local = new File(currentDirectory, "node_modules");
        
        String nodePath = token.substring(4);
        if (nodePath.isEmpty()) {
            nodePath = System.getenv("NODE_PATH");
            if (nodePath == null || nodePath.isEmpty()) {
                nodePath = local.getAbsolutePath();
            }
        }

        File[] roots = FileUtil.pathToFileArray(currentDirectory, nodePath);
        
        // If we have a single root to look up NPM modules we assume
        // we can use it for output as well. If we have several we
        // take the output to be "node_modules"
        // TODO: is this heuristic correct/useful?
        File out;
        if (roots.length > 1) {
            out = local;
        } else {
            out = roots[0];
        }
        
        NpmContentStore cs = new NpmContentStore(roots, out, log, offline);
        return new NpmRepository(cs.createRoot());
    }
}
