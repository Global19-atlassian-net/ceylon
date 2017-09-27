package org.eclipse.ceylon.compiler.java.runtime.tools;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.ceylon.common.config.CeylonConfig;
import org.eclipse.ceylon.common.config.DefaultToolOptions;

public class Options {
    private String workingDirectory;
    private String systemRepository;
    private List<String> userRepositories = new LinkedList<String>();
    private boolean offline;
    private boolean verbose;
    private String verboseCategory;
    private boolean noDefaultRepositories;
    private String run;
    private String overrides;
    private boolean downgradeDist;
    private int timeout;
    
    public String getWorkingDirectory() {
        return workingDirectory;
    }
    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getSystemRepository() {
        return systemRepository;
    }
    public void setSystemRepository(String systemRepository) {
        this.systemRepository = systemRepository;
    }
    
    public List<String> getUserRepositories() {
        return userRepositories;
    }
    public void setUserRepositories(List<String> userRepositories) {
        this.userRepositories = userRepositories;
    }
    public void addUserRepository(String userRepository){
        userRepositories.add(userRepository);
    }
    
    public boolean isOffline() {
        return offline;
    }
    public void setOffline(boolean offline) {
        this.offline = offline;
    }
    
    public boolean isVerbose() {
        return verbose;
    }
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
        if(verbose){
            if(verboseCategory == null)
                verboseCategory = "all";
        }else{
            verboseCategory = null;
        }
    }

    public String getVerboseCategory() {
        return verboseCategory;
    }
    public void setVerboseCategory(String verboseCategory) {
        if(verboseCategory == null)
            verbose = false;
        else{
            verbose = true;
            if(verboseCategory.isEmpty())
                verboseCategory = "all";
        }
        this.verboseCategory = verboseCategory;
    }
    public boolean isVerbose(String category){
        String categories = getVerboseCategory();
        if(categories == null)
            return false;
        for(String cat : categories.split(",")){
            if(cat.equals(category))
                return true;
            if(cat.equals("all"))
                return true;
        }
        return false;
    }
    
    public boolean isNoDefaultRepositories() {
        return noDefaultRepositories;
    }
    public void setNoDefaultRepositories(boolean noDefaultRepositories) {
        this.noDefaultRepositories = noDefaultRepositories;
    }
    
    public String getRun() {
        return run;
    }
    public void setRun(String run) {
        this.run = run;
    }

    public String getOverrides() {
        return overrides;
    }
    public void setOverrides(String overrides) {
        this.overrides = overrides;
    }
    
    public boolean isDowngradeDist() {
        return downgradeDist;
    }
    public void setDowngradeDist(boolean downgradeDist) {
        this.downgradeDist = downgradeDist;
    }
    
    public int getTimeout() {
        return timeout;
    }
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Set options according to the configuration settings found in the given
     * <code>CeylonConfig</code>
     * @param config The <code>CeylonConfig</code> to take the settings from
     */
    public void mapOptions(CeylonConfig config) {
        setTimeout((int) DefaultToolOptions.getDefaultTimeout(config));
        setOffline(DefaultToolOptions.getDefaultOffline(config));
        setOverrides(DefaultToolOptions.getDefaultOverrides(config));
        setRun(DefaultToolOptions.getRunToolRun(config, null));
        setDowngradeDist(!DefaultToolOptions.getLinkWithCurrentDistribution(config));
    }

    /**
     * Create a new <code>Options</code> object initialized with the
     * settings read from the default Ceylon configuration
     * @return An initialized <code>Options</code> object
     */
    public static Options fromConfig() {
        return fromConfig(CeylonConfig.get());
    }

    /**
     * Create a new <code>Options</code> object initialized with the
     * settings read from the given configuration
     * @param config The <code>CeylonConfig</code> to take the settings from
     * @return An initialized <code>Options</code> object
     */
    public static Options fromConfig(CeylonConfig config) {
        Options options = new Options();
        options.mapOptions(config);
        return options;
    }
}
