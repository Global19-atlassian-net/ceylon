/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.eclipse.ceylon.compiler.java.test;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class ConcurrentSuite extends Suite {

    public ConcurrentSuite(Class<?> clase, RunnerBuilder builder) throws InitializationError {
        super(clase, builder);
        setScheduler(new ConcurrentScheduler());
    }

    /* Uncomment this if you want to stop your entire suite if you have one test that fails in parallel */
//    @Override
//    public void run(final RunNotifier runNotifier) {
//        runNotifier.addListener(new RunListener(){
//            @Override
//            public void testFailure(Failure failure) throws Exception {
//                if(failure.getDescription().getMethodName().equals("testDynamicMetamodel"))
//                    runNotifier.pleaseStop();
//                super.testFailure(failure);
//            }
//        });
//        super.run(runNotifier);
//    }
}
