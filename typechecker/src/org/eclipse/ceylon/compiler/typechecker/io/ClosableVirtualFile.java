package org.eclipse.ceylon.compiler.typechecker.io;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public interface ClosableVirtualFile extends VirtualFile {
    public void close();
}
