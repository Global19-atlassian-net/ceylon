package org.eclipse.ceylon.common.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class) 
@SuiteClasses({
    CeylonConfigTest.class,
    ConfigWriterTest.class,
    RepositoriesTest.class,
    KeystoresTest.class,
    ProxiesTest.class,
    AuthenticationTest.class
})
public class ConfigSuite {

}
