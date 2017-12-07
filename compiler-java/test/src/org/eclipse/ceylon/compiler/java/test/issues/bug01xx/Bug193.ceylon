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

// Bug193.ceylon:26: <X>x(X) in org.eclipse.ceylon.compiler.java.test.issues.x 
// cannot be applied to <ceylon.language.Iterable<? extends ceylon.language.String>>
// (ceylon.language.ArraySequence<ceylon.language.String>)

/* The current version of this issue just doesn't make sense anymore now that Sized is gone.
   Not sure if we should find another example of this or just remove this test.
@noanno
void x<X>(X x) given X satisfies Sized { 
    print(x.size);
}
@noanno
void bug193(){
    String[] arr = {"hello"}; 
    x(arr);
    print(arr.size);
}
*/
