package org.eclipse.ceylon.compiler.java.runtime.tools;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.common.config.CeylonConfig;
import org.eclipse.ceylon.common.config.DefaultToolOptions;

public class JavaRunnerOptions extends RunnerOptions {
    private ClassLoader delegateClassLoader;

    public ClassLoader getDelegateClassLoader() {
        return delegateClassLoader;
    }

    public void setDelegateClassLoader(ClassLoader delegateClassLoader) {
        this.delegateClassLoader = delegateClassLoader;
    }

    /**
     * Set options according to the configuration settings found in the given
     * <code>CeylonConfig</code>
     * @param config The <code>CeylonConfig</code> to take the settings from
     */
    public void mapOptions(CeylonConfig config) {
        setRun(DefaultToolOptions.getRunToolRun(config, Backend.Java));
    }
    
    /**
     * Create a new <code>JavaRunnerOptions</code> object initialized with the
     * settings read from the default Ceylon configuration
     * @return An initialized <code>JavaRunnerOptions</code> object
     */
    public static JavaRunnerOptions fromConfig() {
        return fromConfig(CeylonConfig.get());
    }

    /**
     * Create a new <code>JavaRunnerOptions</code> object initialized with the
     * settings read from the given configuration
     * @param config The <code>CeylonConfig</code> to take the settings from
     * @return An initialized <code>JavaRunnerOptions</code> object
     */
    public static JavaRunnerOptions fromConfig(CeylonConfig config) {
        JavaRunnerOptions options = new JavaRunnerOptions();
        options.mapOptions(config);
        return options;
    }
    
}
