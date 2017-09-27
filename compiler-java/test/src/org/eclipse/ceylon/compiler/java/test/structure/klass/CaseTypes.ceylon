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
shared abstract class CaseTypes(String name) 
        of foo | bar 
        {}

@noanno
shared object foo extends CaseTypes("foo") {}
@noanno
shared object bar extends CaseTypes("bar") {}

@noanno
shared abstract class CaseTypes2(String name) 
        of Foo | Bar 
        {}

@noanno
shared class Foo() extends CaseTypes2("Foo") {}
@noanno
shared class Bar() extends CaseTypes2("Bar") {}

@noanno
shared interface ICaseTypes
        of IFoo | IBar 
        {}

@noanno
shared interface IFoo satisfies ICaseTypes {}
@noanno
shared interface IBar satisfies ICaseTypes {}
