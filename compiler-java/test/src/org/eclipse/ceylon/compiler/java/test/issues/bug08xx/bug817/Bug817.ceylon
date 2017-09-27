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
import java.lang { JString = String, ObjectArray } 

@noanno
shared void bug817() {
    bug817_1();
    bug817_2();
}

shared void bug817_1() {
    try {
        ObjectArray<JString> names = NullArray().names();
    } catch (Exception|AssertionError ex) {
        return;
    }
    throw Exception("NullPointerException expected!");
}

shared void bug817_2() {
    ObjectArray<JString>? names = NullArray().names();
    if (exists names) {
        throw Exception("Exists on `null` array has evaluated to `true`!");
    }
}
