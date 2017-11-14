

package org.eclipse.ceylon.cmr.api;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * RepositoryManager API.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface RepositoryManager {
    static final String DEFAULT_MODULE = "default";

    /**
     * Resolve dependencies for the context.
     *
     * @param name    the module namespace
     * @param name    the module name
     * @param version the module version
     * @return all dependencies, null if they cannot be found
     * @throws RepositoryException for any I/O error
     */
    File[] resolve(String namespace, String name, String version) throws RepositoryException;

    /**
     * Resolve dependencies for the context.
     *
     * @param context the artifact context to resolve
     * @return all dependencies, null if they cannot be found
     * @throws RepositoryException for any I/O error
     */
    File[] resolve(ArtifactContext context) throws RepositoryException;

    /**
     * Returns an artifact as a File object, looked up by name and version
     * 
     * @param namespace the artifact namespace
     * @param name the artifact name
     * @param version the artifact version
     * @return the File representing this artifact, if found. Null otherwise
     * 
     * @throws RepositoryException if anything went wrong
     */
    File getArtifact(String namespace, String name, String version) throws RepositoryException;

    /**
     * Returns an artifact as a File object, looked up by context. This allows
     * you to specify the artifact type.
     * 
     * @param context the artifact lookup info
     * @return the File representing this artifact, if found. Null otherwise
     * 
     * @throws RepositoryException if anything went wrong
     */
    File getArtifact(ArtifactContext context) throws RepositoryException;

    /**
     * Returns an artifact as an ArtifactResult object, looked up by name and version
     * 
     * @param namespace the artifact namespace
     * @param name the artifact name
     * @param version the artifact version
     * @return the ArtifactResult representing this artifact, if found. Null otherwise
     * 
     * @throws RepositoryException if anything went wrong
     */
    ArtifactResult getArtifactResult(String namespace, String name, String version) throws RepositoryException;

    /**
     * Returns an artifact as an ArtifactResult object, looked up by context. This allows
     * you to specify the artifact type.
     * 
     * @param context the artifact lookup info
     * @return the ArtifactResult representing this artifact, if found. Null otherwise
     * 
     * @throws RepositoryException if anything went wrong
     */
    ArtifactResult getArtifactResult(ArtifactContext context) throws RepositoryException;
    
    /**
     * <p>Returns the overridden context for the given context. 
     * This is the context which {@link #getArtifactResult(ArtifactContext)} 
     * actually uses in its search. Mechanisms by which artifacts are 
     * overridden include:</p>
     * <ul>
     * <li>By user required (the {@code --overrides} CLI flag)
     * <li>Distribution compatibility
     * <ul>
     * If the given context is not overridden the same instance may be 
     * returned or a different but equal instance may be returned.
     * @param context
     * @return
     * @throws RepositoryException
     */
    ArtifactContext getArtifactOverride(ArtifactContext context) throws RepositoryException;

    /**
     * Returns ArtifactResult objects, looked up by context. This allows
     * you to specify several artifact types.
     * 
     * @param context the artifact lookup info
     * @return the list of ArtifactResult objects found. Can be empty of none of the
     * specified types was found or null if the artifact wasn't found at all
     * 
     * @throws RepositoryException if anything went wrong
     */
    List<ArtifactResult> getArtifactResults(ArtifactContext context) throws RepositoryException;

    /**
     * Publishes an artifact by name/version as an InputStream
     * 
     * @param namespace the artifact namespace
     * @param name the artifact name
     * @param version the artifact version
     * @param content the artifact content as an InputStream
     * 
     * @throws RepositoryException if anything went wrong
     */
    void putArtifact(String namespace, String name, String version, InputStream content) throws RepositoryException;

    /**
     * Publishes an artifact by name/version as a File
     * 
     * @param namespace the artifact namespace
     * @param name the artifact name
     * @param version the artifact version
     * @param content the artifact content as a File
     * 
     * @throws RepositoryException if anything went wrong
     */
    void putArtifact(String namespace, String name, String version, File content) throws RepositoryException;

    /**
     * Publishes an artifact by context as an InputStream
     * The stream is closed after this invocation.
     *
     * @param context the artifact lookup info
     * @param content the artifact content as an InputStream
     * 
     * @throws RepositoryException if anything went wrong
     */
    void putArtifact(ArtifactContext context, InputStream content) throws RepositoryException;

    /**
     * Publishes an artifact by context as a File
     * 
     * @param context the artifact lookup info
     * @param content the artifact content as a File
     * 
     * @throws RepositoryException if anything went wrong
     */
    void putArtifact(ArtifactContext context, File content) throws RepositoryException;

    /**
     * Removes an artifact by name/version
     * 
     * @param namespace the artifact namespace
     * @param name the artifact name
     * @param version the artifact version
     * 
     * @throws RepositoryException if anything went wrong
     */
    void removeArtifact(String namespace, String name, String version) throws RepositoryException;

    /**
     * Removes an artifact by context
     * 
     * @param context the artifact lookup info
     *
     * @throws RepositoryException if anything went wrong
     */
    void removeArtifact(ArtifactContext context) throws RepositoryException;

    /**
     * Returns the list of repositories for this manager.
     *
     * @return the repositories
     */
    List<CmrRepository> getRepositories();

    /**
     * Gather repositories display strings.
     *
     * @return the display strings
     */
    List<String> getRepositoriesDisplayString();
    
    /**
     * Completes a list of module names, using the given query.
     * 
     * @param query specifies the type of backend and optionally a completion match start
     * 
     * @return the list of matching module names
     */
    ModuleSearchResult completeModules(ModuleQuery query);
    
    /**
     * Completes a list of module versions, using the given query.
     * 
     * @param query specifies the module name, type of backend and optionally a completion match start
     * 
     * @return the list of matching module versions
     */
    ModuleVersionResult completeVersions(ModuleVersionQuery query);
    
    /**
     * Searches a list of module names, using the given query.
     * 
     * @param query specifies the type of backend and optionally a pattern
     * 
     * @return the list of matching module names
     */
    ModuleSearchResult searchModules(ModuleQuery query);

    /**
     * Makes sure that content cached as "unavailable" is reasessed
     */
    void refresh(boolean recurse);

    /**
     * Gets the overrides used in this repository manager
     */
    Overrides getOverrides();
    
    /**
     * Changes the overrides used in this repository manager
     */
    void setOverrides(Overrides overrides);

    /**
     * Determines if the given namespace is recognized by any of the configured repositories
     */
    boolean isValidNamespace(String namespace);
}
