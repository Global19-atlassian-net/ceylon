/*
 * Copyright (c) 1999, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 */

package org.eclipse.ceylon.compiler.java.tools;

import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.eclipse.ceylon.compiler.java.launcher.Main;
import org.eclipse.ceylon.javax.lang.model.SourceVersion;
import org.eclipse.ceylon.javax.tools.DiagnosticListener;
import org.eclipse.ceylon.javax.tools.JavaFileManager;
import org.eclipse.ceylon.javax.tools.JavaFileObject;
import org.eclipse.ceylon.langtools.tools.javac.api.JavacTool;
import org.eclipse.ceylon.langtools.tools.javac.file.JavacFileManager;
import org.eclipse.ceylon.langtools.tools.javac.util.Context;
import org.eclipse.ceylon.langtools.tools.javac.util.Log;

/**
 * @deprecated apparently JavacTool is deprecated, we need to find what to replace it with
 */
@Deprecated
public class CeyloncTool {

    public JavacFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> diagnosticListener, Locale locale, Charset charset) {
        return getStandardFileManager(null, diagnosticListener, locale, charset);
    }
    
    public JavacFileManager getStandardFileManager(Writer out, DiagnosticListener<? super JavaFileObject> diagnosticListener, Locale locale, Charset charset) {
        Context context = new Context();
        if (diagnosticListener != null)
            context.put(DiagnosticListener.class, diagnosticListener);
        // make sure we set the out before someone else sets a default one, or uses one
        if (context.get(Log.outKey) == null) {
            if (out == null)
                context.put(Log.outKey, new PrintWriter(System.err, true));
            else
                context.put(Log.outKey, new PrintWriter(out, true));
        }
        CeylonLog.preRegister(context);
        return new CeyloncFileManager(context, true, charset);
    }
    
    public CeyloncTaskImpl getTask(Writer out, JavaFileManager fileManager, DiagnosticListener<? super JavaFileObject> diagnosticListener, Iterable<String> options, Iterable<String> classes, Iterable<? extends JavaFileObject> compilationUnits) {
//        final String kindMsg = "All compilation units must be of SOURCE kind";
        if (options != null)
            for (String option : options)
                option.getClass(); // null check
        if (classes != null) {
            for (String cls : classes)
                if (!SourceVersion.isName(cls) // implicit null check
                        && !"default".equals(cls)) // FIX for ceylon because default is not a valid name for Java
                    throw new IllegalArgumentException("Not a valid class name: " + cls);
        }

        if (fileManager == null)
            fileManager = getStandardFileManager(out, diagnosticListener, null, null);

        Context context = ((CeyloncFileManager) fileManager).getContext();
        if (diagnosticListener != null && context.get(DiagnosticListener.class) == null)
            context.put(DiagnosticListener.class, diagnosticListener);

        context.put(JavaFileManager.class, fileManager);
        JavacTool.processOptions(context, fileManager, options);
        Main compiler = new Main("ceyloncTask", context.get(Log.outKey));
        return new CeyloncTaskImpl(compiler, options, context, classes, compilationUnits);
    }
}
