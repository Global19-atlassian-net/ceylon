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
abstract class Buffer<T>() {
    shared formal T get();
}

@noanno
class ByteBuffer() extends Buffer<Integer>(){
    shared actual Integer get(){ return 1; }
}

@noanno
class Bug1041<T>() {
    shared String? stringProp = null;
    shared String? stringMethod() => null;
    shared T? taProp = null;
    shared T? taMethod() => null;
    
    shared void java(Bug1041Java<Integer> java) {
        value elvisOp1 = java.stringProp?.size;
        value elvisOp2 = java.stringMethod()?.size;
        value elvisOp3 = java.taProp?.sign;
        value elvisOp4 = java.taMethod()?.sign;
        value elseOp1 = java.stringProp else "";
        value elseOp2 = java.stringMethod() else "";
        value elseOp3 = java.taProp else 0;
        value elseOp4 = java.taMethod() else 0;
        Boolean existsOp1 = java.stringProp exists;
        Boolean existsOp2 = java.stringMethod() exists;
        Boolean existsOp3 = java.taProp exists;
        Boolean existsOp4 = java.taMethod() exists;
        // make sure we didn't break value type invocations in the process
        value b = ByteBuffer().get().and($00111111);
    }
    
    shared void ceylon(Bug1041<Integer> java) {
        value elvisOp1 = java.stringProp?.size;
        value elvisOp2 = java.stringMethod()?.size;
        value elvisOp3 = java.taProp?.sign;
        value elvisOp4 = java.taMethod()?.sign;
        value elseOp1 = java.stringProp else "";
        value elseOp2 = java.stringMethod() else "";
        value elseOp3 = java.taProp else 0;
        value elseOp4 = java.taMethod() else 0;
        Boolean existsOp1 = java.stringProp exists;
        Boolean existsOp2 = java.stringMethod() exists;
        Boolean existsOp3 = java.taProp exists;
        Boolean existsOp4 = java.taMethod() exists;
    }
}