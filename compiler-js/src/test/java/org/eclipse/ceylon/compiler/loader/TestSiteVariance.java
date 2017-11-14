/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.loader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils;
import org.eclipse.ceylon.cmr.resolver.javascript.JavaScriptResolver;
import org.eclipse.ceylon.compiler.typechecker.TypeChecker;
import org.eclipse.ceylon.compiler.typechecker.TypeCheckerBuilder;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.SiteVariance;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.eclipse.ceylon.compiler.js.JsCompiler;
import org.eclipse.ceylon.compiler.js.loader.JsModuleManagerFactory;
import org.eclipse.ceylon.compiler.js.loader.MetamodelGenerator;
import org.eclipse.ceylon.compiler.js.util.Options;

import util.ModelUtils;

public class TestSiteVariance {

    private Options options(String srcdir) {
        return new Options().outRepo("modules").addRepo("build/runtime")
                .optimize(true).generateSourceArchive(false)
                .addSrcDir(srcdir);
    }

    private TypeChecker typeChecker(Options opts) {
        final RepositoryManager repoman = CeylonUtils.repoManager()
                .userRepos(opts.getRepos())
                .outRepo(opts.getOutRepo())
                .buildManager();
        final TypeCheckerBuilder builder = new TypeCheckerBuilder()
            .moduleManagerFactory(new JsModuleManagerFactory("UTF-8"));
        for (File sd : opts.getSrcDirs()) {
            builder.addSrcDirectory(sd);
        }
        builder.setRepositoryManager(repoman);
        final TypeChecker tc = builder.getTypeChecker();
        JsModuleManagerFactory.setVerbose(true);
        tc.process();
        return tc;
    }

    @Test @SuppressWarnings("unchecked")
    public void testSiteVariance() throws IOException {
        //Typecheck
        Options opts = options("src/test/resources/variance/phase1");
        TypeChecker tc = typeChecker(opts);
        //Compile
        JsCompiler jsc = new JsCompiler(tc, opts);
        jsc.generate();
        Map<String, Object> model = JavaScriptResolver.readJsonModel(new File("modules/phase1/0.1/phase1-0.1-model.js"));
        Assert.assertNotNull("Model not found", model);
        model = (Map<String,Object>)model.get("phase1");
        Assert.assertNotNull("Default package not found", model);
        model = (Map<String,Object>)model.get("m2");
        Assert.assertNotNull("Function m2 not found", model);
        Map<String,Object> type = (Map<String,Object>)model.get(MetamodelGenerator.KEY_TYPE);
        List<Map<String,Object>> targs = (List<Map<String,Object>>)type.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        type = targs.get(0);
        List<List<Map<String,Object>>> paramLists = (List<List<Map<String,Object>>>)model.get(MetamodelGenerator.KEY_PARAMS);
        Map<String,Object> parm = paramLists.get(0).get(0);
        parm = (Map<String,Object>)parm.get(MetamodelGenerator.KEY_TYPE);
        targs = (List<Map<String,Object>>)parm.get(MetamodelGenerator.KEY_TYPE_PARAMS);
        parm = targs.get(0);
        Assert.assertEquals(SiteVariance.OUT.ordinal(), type.get(MetamodelGenerator.KEY_US_VARIANCE));
        Assert.assertEquals(SiteVariance.IN.ordinal(), parm.get(MetamodelGenerator.KEY_US_VARIANCE));

        //Typecheck phase2
        opts = options("src/test/resources/variance/phase2");
        tc = typeChecker(opts);
        Function m2 = (Function)tc.getPhasedUnits().getPhasedUnits().get(0).getPackage().getModule().getPackage("phase1").getDirectMember("m2", null, false);
        Assert.assertNotNull("phase1::m2 missing", m2);
        Assert.assertFalse("Missing variance overrides in return type",
                m2.getType().getVarianceOverrides().isEmpty());
        Assert.assertFalse("Missing variance overrides in parameter",
                m2.getFirstParameterList().getParameters().get(0).getType()
                    .getVarianceOverrides().isEmpty());
        jsc = new JsCompiler(tc, opts);
        jsc.generate();
        Assert.assertTrue("Should compile without errors", jsc.getErrors().isEmpty());
    }

    @After
    public void cleanup() {
        ModelUtils.deleteRecursively(new File("modules"));
    }

}
