/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.config;

/**
 * Configuration items which know about username/password credentials
 */
public class Credentials {
    
    private String user;
    private String password;
    private String keystore;
    private String alias;
    private String credentialPrompt;
    
    private Credentials(String user, String password, String keystore,
            String alias, String credentialPrompt) {
        super();
        this.user = user;
        this.password = password;
        this.keystore = keystore;
        this.alias = alias;
        this.credentialPrompt = credentialPrompt;
    }
    
    public static Credentials create(String user, String password, String keystore,
            String alias, String credentialPrompt) {
        if (user == null) {
            return null;
        }
        return new Credentials(user, password, keystore, alias, credentialPrompt);
    }

    /** 
     * The username to use when authenticating, if any.
     * @see #getPassword()
     * @see #getAlias()
     */
    public String getUser() {
        return user;
    }
    
    /** 
     * The plain text of the {@linkplain #getUser() user's} password to 
     * use when authenticating, if any.
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * The name of a [keystore] section in the config.
     */
    public String getKeystore() {
        return keystore;
    }
    
    /**
     * The alias of a key in the given {@link #getKeystore()} which holds the 
     * {@linkplain #getUser() user's} password to 
     * use when authenticating, if any.
     */
    public String getAlias() {
        return alias;
    }
    
    /**
     * The prompt to use when interactively prompting for the password
     */
    public String getCredentialPrompt() {
        return credentialPrompt;
    }
    
}
