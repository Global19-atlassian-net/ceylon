"Test that multiple imports from the same version even when visible to each other works"
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module org.eclipse.ceylon.compiler.typechecker.test.moduletestsameversionwork "1" {
    import org.eclipse.ceylon.compiler.typechecker.test.modulec "1";
    import org.eclipse.ceylon.compiler.typechecker.test.moduled "1";
}