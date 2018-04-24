/* Originally based on the javac task from apache-ant-1.7.1.
 * The license in that file is as follows:
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or
 *   more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information regarding
 *   copyright ownership.  The ASF licenses this file to You under
 *   the Apache License, Version 2.0 (the "License"); you may not
 *   use this file except in compliance with the License.  You may
 *   obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an "AS
 *   IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied.  See the License for the specific language
 *   governing permissions and limitations under the License.
 *
 */

/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 */
package org.eclipse.ceylon.ant;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

@ToolEquivalent("run")
@AntDoc("-To execute the `com.example.foo.lifecycle.start` top level method in\n"+ 
        "version 1.1 of module `com.example.foo` residing\n"+
        "in the `build` directory (repository):\n"+
        "\n"+
        "<!-- lang: xml -->\n"+
        "    <target name=\"execute\" depends=\"ceylon-ant-taskdefs\">\n"+
        "      <ceylon-run run=\"com.example.foo.lifecycle.start\"\n"+ 
        "        module=\"com.example.foo/1.1\">\n"+
        "        <rep url=\"build\"/>\n"+
        "      </ceylon-run>\n"+
        "    </target>\n")
public class CeylonRunAntTask extends RepoUsingCeylonAntTask {

    static final String FAIL_MSG = "Run failed; see the compiler error output for details.";
    
    public static class Arg {
        String value;
        
        @AntDoc("A command line argument to be passed to the module.")
        public void setValue(String value) {
            this.value = value;
        }

        public void addText(String value) {
            this.value = value;
        }
    }

    private String run;
    private String module;
    private String compileFlags;
    private List<Arg> args = new ArrayList<Arg>(0);
    private boolean autoExportMavenDependencies;
    private boolean flatClasspath;
    private boolean linkWithCurrentDistribution;
    
    public CeylonRunAntTask() {
        super("run");
    }

    /**
     * Calling the run tool ATM needs a new JVM: https://github.com/ceylon/ceylon-compiler/issues/1366
     */
    protected boolean shouldSpawnJvm() {
        return true;
    }
    
    @Override
    @AntDoc("Whether the task should be run in a separate VM (default: `true`).")
    public void setFork(boolean fork) {
        super.setFork(fork);
    }

    /**
     * Set the fully qualified name of a toplevel method or class with no parameters.
     */
    @OptionEquivalent
    public void setRun(String run) {
        this.run = run;
    }

    /**
     * Set the name of a runnable module with an optional version
     */
    @AntDoc("The name and optional version of the module to run")
    @Required
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * Use a flat classpath
     */
    @OptionEquivalent
    public void setFlatClasspath(boolean flatClasspath){
        this.flatClasspath = flatClasspath;
    }
    
    @OptionEquivalent
    public void setLinkWithCurrentDistribution(boolean linkWithCurrentDistribution){
        this.linkWithCurrentDistribution = linkWithCurrentDistribution;
    }

    /**
     * Auto-export Maven dependencies
     */
    @OptionEquivalent
    public void setAutoExportMavenDependencies(boolean autoExportMavenDependencies) {
        this.autoExportMavenDependencies = autoExportMavenDependencies;
    }

    /**
     * Sets compile flags
     */
    @OptionEquivalent
    public void setCompile(String compileFlags) {
        this.compileFlags = compileFlags;
    }

    /** Adds an argument to be passed to the tool */
    @AntDoc("An argument to be passed to the module")
    public void addConfiguredArg(Arg arg) {
        this.args.add(arg);
    }

    /**
     * Check that all required attributes have been set and nothing silly has
     * been entered.
     * 
     * @exception BuildException if an error occurs
     */
    protected void checkParameters() throws BuildException {
        if(module == null || module.isEmpty()){
            throw new BuildException("Missing module parameter is required");
        }
    }

    /**
     * Perform the compilation.
     */
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        if(run != null){
            appendOptionArgument(cmd, "--run", run);
        }
        
        if(compileFlags != null){
            appendOptionArgument(cmd, "--compile", compileFlags);
        }
        
        if(autoExportMavenDependencies){
            appendOption(cmd, "--auto-export-maven-dependencies");
        }

        if(flatClasspath){
            appendOption(cmd, "--flat-classpath");
        }
        
        if (linkWithCurrentDistribution) {
            appendOption(cmd, "--link-with-current-distribution");
        }
        
        cmd.createArgument().setValue(module);

        if (!args.isEmpty()) {
            for (Arg arg : args) {
                appendOption(cmd, arg.value);
            }
        }
    }

    @Override
    protected String getFailMessage() {
        return "Run failed; see the compiler error output for details.";
    }

    
}
