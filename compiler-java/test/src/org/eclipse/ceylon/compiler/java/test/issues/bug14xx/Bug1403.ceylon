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
shared {Element&Object*} coalesce<Element>(
        "The values, some of which may be null."
        {Element*} values) 
                => nothing;
@noanno
class Bug1403(String+ attrs) {}

@noanno
void bug1403() {
    Bug1403("", *coalesce { "" });
    Bug1403("", *{ "" });
    value it = { "" };
    Bug1403("", *it);
    Bug1403("", *[ "" ]);
    value seq = [ "" ];
    Bug1403("", *seq);
}
