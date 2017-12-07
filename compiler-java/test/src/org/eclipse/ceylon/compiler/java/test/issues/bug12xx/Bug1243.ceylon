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
class Bug1243<T>(T t) {
    shared class Y<S>(S s) {}
    shared Y<Z> neww<Z=Integer|Float>() => nothing;
    shared void old<Z=Integer|Float>(Y<Z> arg) {}
    void test() {
        Y<Z> neww<Z=Integer|Float>() => nothing;
        void old<Z=Integer|Float>(Y<Z> arg) {}
    }
}
@noanno
class Bug1243b<T>(T t) {
    shared class Y<S=Integer|Float>(S s) {}
    shared Y<Z> neww<Z>() => nothing;
    shared void old<Z>(Y<Z> arg) {}
    void test() {
        Y<Z> neww<Z>() => nothing;
        void old<Z>(Y<Z> arg) {}
    }
}
@noanno
class Bug1243c<T>(T t) {
    shared class Y<S=Integer|Float>(S s) {}
    shared Y<Z> neww<Z=Integer|Float>() => nothing;
    shared void old<Z=Integer|Float>(Y<Z> arg) {}
    void test() {
        Y<Z> neww<Z=Integer|Float>() => nothing;
        void old<Z=Integer|Float>(Y<Z> arg) {}
    }
}
@noanno
void bug1243d<T>(T t) {
    class Y<S>(S s) {}
    Y<Z> neww<Z=Integer|Float>() => nothing;
    void old<Z=Integer|Float>(Y<Z> arg) {}
}
@noanno
void bug1243e<T>(T t) {
    class Y<S=Integer|Float>(S s) {}
    Y<Z> neww<Z>() => nothing;
    void old<Z>(Y<Z> arg) {}
}
@noanno
void bug1243f<T>(T t) {
    class Y<S=Integer|Float>(S s) {}
    Y<Z> neww<Z=Integer|Float>() => nothing;
    void old<Z=Integer|Float>(Y<Z> arg) {}
}
