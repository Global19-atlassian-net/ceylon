/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.api;


/**
 * Content finder.
 *
 * @author Stef Epardaud
 */
public interface ContentFinder {
    boolean isSearchable();
    
    void completeModules(ModuleQuery query, ModuleSearchResult result);

    void completeVersions(ModuleVersionQuery query, ModuleVersionResult result);
    
    void searchModules(ModuleQuery query, ModuleSearchResult result);
}
