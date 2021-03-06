/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ceylon.model.loader.mirror.ClassMirror;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Setter;

/**
 * Setter subclass which can contain local declarations.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class SetterWithLocalDeclarations extends Setter implements LocalDeclarationContainer {

    private Map<String,Declaration> localDeclarations;
    public final ClassMirror classMirror;
    
    public SetterWithLocalDeclarations(ClassMirror classMirror) {
        this.classMirror = classMirror;
    }
    
    @Override
    public Declaration getLocalDeclaration(String name) {
        if(localDeclarations == null)
            return null;
        return localDeclarations.get(name);
    }

    @Override
    public void addLocalDeclaration(Declaration declaration) {
        if(localDeclarations == null)
            localDeclarations = new HashMap<String, Declaration>();
        localDeclarations.put(declaration.getPrefixedName(), declaration);
    }

    @Override
    public boolean isJava() {
        Scope container = getContainer();
        while(container != null && container instanceof Declaration == false)
            container = container.getContainer();
        return container != null ? ((Declaration) container).isJava() : false;
    }
}
