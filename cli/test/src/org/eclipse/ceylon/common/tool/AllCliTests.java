package org.eclipse.ceylon.common.tool;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class) 
@SuiteClasses({
    ToolLoaderTest.class,
    ToolLoaderTest.class,
    ToolFactoryTest.class,
    MultiplicityTest.class,
    WordWrapTest.class
})
public class AllCliTests {

    public AllCliTests() {
        // TODO Auto-generated constructor stub
    }

}
