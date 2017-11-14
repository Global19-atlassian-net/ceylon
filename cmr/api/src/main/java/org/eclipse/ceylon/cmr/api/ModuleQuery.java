/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.api;

import java.util.Arrays;

public class ModuleQuery {
    protected String namespace;
    protected String name;
    protected Type type;
    protected Retrieval retrieval;
    private Long start;
    private Long count;
    private long[] pagingInfo;
    private Integer jvmBinaryMajor;
    private Integer jvmBinaryMinor;
    private Integer jsBinaryMajor;
    private Integer jsBinaryMinor;
    private String memberName;
    private boolean memberSearchPackageOnly;
    private boolean memberSearchExact;

    public enum Type {
        SRC(ArtifactContext.SRC),
        CAR(ArtifactContext.CAR),
        JAR(ArtifactContext.JAR),
        JVM(ArtifactContext.CAR, ArtifactContext.JAR),
        JS(ArtifactContext.JS),
        DART(ArtifactContext.DART),
        CODE(ArtifactContext.CAR, ArtifactContext.JAR, ArtifactContext.JS),
        CEYLON_CODE(ArtifactContext.CAR, ArtifactContext.JS),
        ALL(ArtifactContext.allSuffixes());
        
        private String[] suffixes;

        Type(String... suffixes){
            this.suffixes = suffixes;
        }

        public String[] getSuffixes() {
            return suffixes;
        }
        
        public boolean includes(String... suffs) {
            return Arrays.asList(suffixes).containsAll(Arrays.asList(suffs));
        }
    }
    
    public enum Retrieval {
        ANY, ALL
    }
    
    public ModuleQuery(String name, Type type) {
        this(null, name, type, Retrieval.ANY);
    }
    
    public ModuleQuery(String namespace, String name, Type type) {
        this(namespace, name, type, Retrieval.ANY);
    }
    
    public ModuleQuery(String name, Type type, Retrieval retrieval) {
        this(null, name, type, retrieval);
    }
    
    public ModuleQuery(String namespace, String name, Type type, Retrieval retrieval) {
        this.namespace = namespace;
        this.name = name;
        this.type = type;
        this.retrieval = retrieval;
    }
    
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Retrieval getRetrieval() {
        return retrieval;
    }

    public void setRetrieval(Retrieval retrieval) {
        this.retrieval = retrieval;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public boolean isPaging() {
        return count != null || start != null;
    }

    public void setPagingInfo(long[] pagingInfo) {
        this.pagingInfo = pagingInfo;
    }

    public long[] getPagingInfo() {
        return pagingInfo;
    }

    public Integer getJvmBinaryMajor() {
        return jvmBinaryMajor;
    }

    public void setJvmBinaryMajor(Integer binaryMajor) {
        this.jvmBinaryMajor = binaryMajor;
    }

    public Integer getJvmBinaryMinor() {
        return jvmBinaryMinor;
    }

    public void setJvmBinaryMinor(Integer binaryMinor) {
        this.jvmBinaryMinor = binaryMinor;
    }

    public Integer getJsBinaryMajor() {
        return jsBinaryMajor;
    }

    public void setJsBinaryMajor(Integer binaryMajor) {
        this.jsBinaryMajor = binaryMajor;
    }

    public Integer getJsBinaryMinor() {
        return jsBinaryMinor;
    }

    public void setJsBinaryMinor(Integer binaryMinor) {
        this.jsBinaryMinor = binaryMinor;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public boolean isMemberSearchPackageOnly() {
        return memberSearchPackageOnly;
    }

    public void setMemberSearchPackageOnly(boolean memberSearchPackageOnly) {
        this.memberSearchPackageOnly = memberSearchPackageOnly;
    }

    public boolean isMemberSearchExact() {
        return memberSearchExact;
    }

    public void setMemberSearchExact(boolean memberSearchExact) {
        this.memberSearchExact = memberSearchExact;
    }

    @Override
    public String toString() {
        return "ModuleQuery[ns=" + namespace + ",name=" + name + ",type=" + type + "]";
    }

}
