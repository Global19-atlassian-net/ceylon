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
@noanno
class SequencedParameterInvocation(){
    void m(String a, Integer* i) {
    }
    
    void m2(String a, SequencedParameterInvocation* i) {
    }
    
    void m3(String a, Integer+ i) {
    }
    
    void m4(String a, SequencedParameterInvocation+ i) {
    }
    
    void f() {
        m("foo");
        m("foo", 1, 2, 3);
        m3("foo", 1, 2, 3);
        value ints = [1,2,3];
        // spread
        m("foo", *ints);
        m3("foo", *ints);
        // partial spread
        m("foo", 1, *ints);
        m3("foo", 1, *ints);
        m("foo", 1, 2, *ints);
        m3("foo", 1, 2, *ints);
        // spread comprehension
        m("foo", for (i in {1}) i);
        m3("foo", for (i in {1}) i);
        // partial spread comprehension
        m("foo", 1, for (i in {1}) i);
        m3("foo", 1, for (i in {1}) i);
        m("foo", 1, 2, for (i in {1}) i);
        m3("foo", 1, 2, for (i in {1}) i);
        m2("foo", this, this, this);
        m4("foo", this, this, this);
    }
}