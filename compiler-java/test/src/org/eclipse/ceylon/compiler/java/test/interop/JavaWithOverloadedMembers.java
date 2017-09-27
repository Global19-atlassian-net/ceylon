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
package org.eclipse.ceylon.compiler.java.test.interop;

public class JavaWithOverloadedMembers<T> {
    
    public JavaWithOverloadedMembers(){}
    public JavaWithOverloadedMembers(long param){}
    
    public void method(){}
    public void method(long param){}
    public void method(long param, long param2){}
    
    public void topMethod(){}
    
    public void variadic(){}
    public void variadic(long p1){}
    public void variadic(long p1, long p2){}
    public void variadic(long... params){}
    public void variadic(double p1){}
    public void variadic(double p1, double p2){}
    public void variadic(double... params){}
    public void variadic(Object param){}
    public void variadic(Object... params){}
    public void variadic(String... params){}
    
    public void bug1575(String i){}
    public void bug1575(T t, Double d){}
    
    public void overloadedPrimitive(byte a, long b){}
    public void overloadedPrimitive(short a, long b){}
    public void overloadedPrimitive(int a, long b){}
    public void overloadedPrimitive(long a, long b){}
    public void overloadedPrimitiveVariadic(short... a){}
    public void overloadedPrimitiveVariadic(int... a){}
    public void overloadedPrimitiveVariadic(long... a){}
    
    public void ambiguousOverload(Object a, String b){}
    public void ambiguousOverload(String a, Object b){}

    public void ambiguousOverload2(int a, long b){}
    public void ambiguousOverload2(long a, int b){}
    
    public int overloadedPrimitive2(int index){ return 1; }
    public boolean overloadedPrimitive2(Object element){ return true; }
}
