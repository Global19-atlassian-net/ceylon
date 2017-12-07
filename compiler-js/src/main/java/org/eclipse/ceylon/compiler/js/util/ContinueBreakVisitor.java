/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js.util;

import java.util.IdentityHashMap;

import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Scope;

import org.eclipse.ceylon.compiler.js.BlockWithCaptureVisitor;

public class ContinueBreakVisitor extends Visitor {

    private boolean breaks;
    private boolean continues;
    private boolean returns;
    private final Scope scope;
    private final JsIdentifierNames names;
    private String bname;
    private String cname;
    private String rname;
    private int level;
    private boolean ignore;
    private int loops;
    private final IdentityHashMap<Tree.Directive, Boolean> dirs = new IdentityHashMap<>();

    public ContinueBreakVisitor(Tree.Block n, JsIdentifierNames names) {
        scope = n.getScope();
        this.names = names;
        n.visit(this);
    }

    public void visit(Tree.Block that) {
        level++;
        if (level>1) {
            ignore=new BlockWithCaptureVisitor(that).hasCapture();
        }
        super.visit(that);
        ignore=false;
        level--;
    }
    public void visit(Tree.Break n) {
        if (ignore || loops>0) {
            return;
        }
        if (bname == null) {
            bname = names.createTempVariable();
        }
        breaks = true;
        dirs.put(n, true);
        super.visit(n);
    }
    public void visit(Tree.Continue n) {
        if (ignore || loops>0) {
            return;
        }
        if (cname == null) {
            cname = names.createTempVariable();
        }
        continues = true;
        dirs.put(n, true);
        super.visit(n);
    }
    public void visit(Tree.Return n) {
        if (ignore) {
            return;
        }
        returns = true;
        super.visit(n);
    }
    public void visit(final Tree.ForStatement that) {
        if (ignore) {
            return;
        }
        loops++;
        super.visit(that);
        loops--;
    }
    public void visit(final Tree.WhileStatement that) {
        if (ignore) {
            return;
        }
        loops++;
        super.visit(that);
        loops--;
    }


    public boolean belongs(Tree.Directive dir) {
        return dirs.containsKey(dir);
    }

    public Scope getScope() {
        return scope;
    }
    public boolean isContinues() {
        return continues;
    }
    public boolean isReturns() {
        return returns;
    }
    public boolean isBreaks() {
        return breaks;
    }

    public String getContinueName() {
        return cname;
    }
    public String getBreakName() {
        return bname;
    }
    public String getReturnName() {
        if (rname == null) {
            rname = names.createTempVariable();
        }
        return rname;
    }

}
